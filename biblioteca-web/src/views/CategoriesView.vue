<!-- src/views/CategoriesView.vue -->
<template>
  <section class="categories-view">
    <!-- 1) Cabeçalho -->
    <h1>Categorias</h1>

    <!-- 2) Área de criação + pesquisa -->
    <div class="top-controls">
      <!-- 2.1) Formulário de criar/editar -->
      <CategoryForm
          v-model="editingCategory"
          @saved="onSaved"
      />

      <!-- 2.2) Campo de pesquisa + botão -->
      <div class="search-container">
        <input
            v-model="filter"
            type="text"
            placeholder="Nome da categoria"
        />
        <button @click="onSearch">Pesquisar</button>
      </div>
    </div>

    <!-- 3) Controles de paginação -->
    <div class="controls">
      <div class="page-size">
        <label>Itens por página:</label>
        <select v-model.number="pageSize" @change="reload">
          <option v-for="opt in pageSizeOptions" :key="opt" :value="opt">
            {{ opt }}
          </option>
        </select>
      </div>
      <div class="page-info">
        Página {{ currentPage + 1 }} de {{ totalPages }}
      </div>
    </div>

    <!-- 4) Tabela -->
    <table class="categories-table">
      <thead>
      <tr>
        <th class="col-description">Descrição</th>
        <th class="col-id">ID</th>
        <th class="col-actions">Ações</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="cat in paginated" :key="cat.id">
        <td class="col-description">{{ cat.name }}</td>
        <td class="col-id">{{ cat.id }}</td>
        <td class="col-actions">
          <button @click="edit(cat)" title="Editar">✏️</button>
          <button @click="remove(cat.id)" title="Excluir">🗑️</button>
        </td>
      </tr>
      </tbody>
    </table>

    <!-- 5) Navegação -->
    <div class="pager">
      <button :disabled="currentPage === 0" @click="prev">‹ Anterior</button>
      <button
          :disabled="currentPage >= totalPages - 1"
          @click="next"
      >Próximo ›</button>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import CategoryForm from '@/components/CategoryForm.vue'
import type { CategoryDto } from '@/types'
import { getCategories, deleteCategory } from '@/services/api'

// estado global
const all = ref<CategoryDto[]>([])
const editingCategory = ref<CategoryDto|undefined>()
const filter = ref('')

// paginação
const pageSizeOptions = [10, 20, 50]
const pageSize = ref(10)
const currentPage = ref(0)

const totalPages = computed(() =>
    Math.ceil(all.value.length / pageSize.value)
)

const paginated = computed(() => {
  const start = currentPage.value * pageSize.value
  return all.value.slice(start, start + pageSize.value)
})

// dispara a busca consultando o back (com filtro opcional)
async function reload() {
  all.value = await getCategories(
      filter.value.trim() || undefined
  )
  // ajusta página caso seja maior que o total
  if (currentPage.value >= totalPages.value) {
    currentPage.value = Math.max(0, totalPages.value - 1)
  }
}

// busca ao clicar
function onSearch() {
  currentPage.value = 0
  reload()
}

// após salvar no form, recarrega
function onSaved() {
  reload()
  editingCategory.value = undefined
}

// editar
function edit(cat: CategoryDto) {
  editingCategory.value = cat
}

// excluir com confirmação
async function remove(id: string) {
  if (!confirm('Deseja realmente excluir?')) return
  await deleteCategory(id)
  reload()
}

// pager
function prev() { if (currentPage.value > 0) currentPage.value-- }
function next() { if (currentPage.value < totalPages.value - 1) currentPage.value++ }

onMounted(reload)
</script>

<style scoped>
.categories-view {
  max-width: 900px;
  margin: 0 auto;
  padding: 1rem;
  display: flex;
  flex-direction: column;
}
h1 {
  text-align: center;
  margin-bottom: 1rem;
}
/* 2) top-controls com Criar+Pesquisar */
.top-controls {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 1rem;
  margin-bottom: 1rem;
}
.search-container {
  display: flex;
  gap: 0.5rem;
}
.search-container input {
  padding: 0.5rem;
  font-size: 1rem;
  width: 200px;
}
.search-container button {
  padding: 0.5rem 1rem;
  cursor: pointer;
}

/* 3) controls */
.controls {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 0.5rem;
}

/* 4) tabela fixa */
.categories-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
}
.categories-table th,
.categories-table td {
  border: 1px solid #ddd;
  padding: 0.75rem;
  vertical-align: top;
  word-break: break-word;
}
.col-description { width: 40%; }
.col-id          { width: 45%; }
.col-actions     { width: 15%; text-align: center; }
.col-actions button {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.2rem;
  margin: 0 0.25rem;
  vertical-align: middle;
}

/* 5) pager left */
.pager {
  display: flex;
  gap: 0.5rem;
  margin-top: 0.5rem;
  justify-content: flex-start;
}
.pager button {
  padding: 0.4rem 0.8rem;
  border: 1px solid #ccc;
  background: #fff;
}
.pager button:disabled {
  opacity: 0.5;
  cursor: default;
}
</style>
