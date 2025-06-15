<!-- src/views/CategoriesView.vue -->
<template>
  <section class="categories-view">
    <!-- 1) Título -->
    <h2>Categorias</h2>

    <!-- 2) Formulário + pesquisa (já concentrado no CategoryForm) -->
    <CategoryForm
        v-model="editingCategory"
        @saved="onSaved"
        @search="onFilter"
    />

    <!-- 3) Controles de paginação -->
    <div class="controls">
      <div class="page-size">
        <label>Itens por página:</label>
        <select v-model.number="pageSize" @change="loadCategories">
          <option
              v-for="opt in pageSizeOptions"
              :key="opt"
              :value="opt"
          >{{ opt }}</option>
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
      <tr v-for="cat in paginatedCategories" :key="cat.id">
        <td class="col-description">{{ cat.name }}</td>
        <td class="col-id">{{ cat.id }}</td>
        <td class="col-actions">
          <button @click="edit(cat)"   title="Editar">✏️</button>
          <button @click="remove(cat.id)" title="Excluir">🗑️</button>
        </td>
      </tr>
      </tbody>
    </table>

    <!-- 5) Pager à esquerda-->
    <div class="pager">
      <button
          :disabled="currentPage === 0"
          @click="prevPage"
      >‹ Anterior</button>
      <button
          :disabled="currentPage >= totalPages - 1"
          @click="nextPage"
      >Próximo ›</button>
    </div>
  </section>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import CategoryForm from '@/components/CategoryForm.vue'
import type { CategoryDto } from '@/types'
import { getCategories, deleteCategory } from '@/services/api'

const allCategories   = ref<CategoryDto[]>([])
const editingCategory = ref<CategoryDto|undefined>()
const filterName      = ref<string>('')

// PAGINAÇÃO
const pageSizeOptions = [10, 20, 50]
const pageSize        = ref<number>(10)
const currentPage     = ref<number>(0)

const totalPages = computed(() =>
    Math.ceil(allCategories.value.length / pageSize.value)
)

const paginatedCategories = computed(() => {
  const start = currentPage.value * pageSize.value
  return allCategories.value.slice(start, start + pageSize.value)
})

async function loadCategories() {
  allCategories.value =
      await getCategories(filterName.value || undefined)
  // reajusta página se ultrapassar
  if (currentPage.value >= totalPages.value) {
    currentPage.value = Math.max(0, totalPages.value - 1)
  }
}

function onSaved() {
  loadCategories()
  editingCategory.value = undefined
}

function onFilter(name: string) {
  filterName.value   = name
  currentPage.value  = 0
  loadCategories()
}

function edit(cat: CategoryDto) {
  editingCategory.value = cat
}

async function remove(id: string) {
  if (!confirm('Ao excluir uma categoria você excluirá todos os livros relacionados! \nDeseja realmente excluir esta categoria?')) return
  await deleteCategory(id)
  loadCategories()
}

function prevPage() { if (currentPage.value > 0) currentPage.value-- }
function nextPage() { if (currentPage.value < totalPages.value - 1) currentPage.value++ }

onMounted(loadCategories)
</script>

<style scoped>
.categories-view {
  width: 80vw;     /* 80% da viewport */
  margin: 0 auto;
  padding: 1rem;
}
h1 {
  text-align: center;
  margin-bottom: 1rem;
}
/* controles (já no form) */
.controls {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 0.5rem;
}
.page-size label {
  margin-right: 0.5rem;
}
/* tabela sem scroll interno */
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
.col-description { width: 60%; }
.col-id          { width: 25%; }
.col-actions     { width: 15%; text-align: center; }
.col-actions button {
  background: none;
  border: none;
  cursor: pointer;
  margin: 0 0.25rem;
  font-size: 1.2rem;
}
/* pager à esquerda */
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
  cursor: pointer;
}
.pager button:disabled {
  opacity: 0.5;
  cursor: default;
}
</style>
