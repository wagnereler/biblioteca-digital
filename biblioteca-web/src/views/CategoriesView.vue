<!-- src/views/CategoriesView.vue -->
<template>
  <section class="categories-view">
    <h1>Categorias</h1>

    <CategoryForm
        v-model="editingCategory"
        @saved="onSaved"
        @search="onFilter"
    />

    <!-- controles de paginação e filtro já estão dentro do form -->
    <div class="controls">
      <div class="page-size">
        <label>Itens por página:</label>
        <select v-model.number="pageSize" @change="loadCategories">
          <option v-for="opt in pageSizeOptions" :key="opt" :value="opt">
            {{ opt }}
          </option>
        </select>
      </div>
      <div class="page-info">
        Página {{ currentPage + 1 }} de {{ totalPages }}
      </div>
    </div>

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
          <button @click="edit(cat)" title="Editar">✏️</button>
          <button @click="askRemove(cat)" title="Excluir">🗑️</button>
        </td>
      </tr>
      </tbody>
    </table>

    <div class="pager">
      <button :disabled="currentPage === 0" @click="prevPage">‹ Anterior</button>
      <button :disabled="currentPage >= totalPages - 1" @click="nextPage">Próximo ›</button>
    </div>

    <ConfirmModal
        :visible="showConfirm"
        title="Confirmação"
        :message="`Excluir “${pendingName}”? Todos os livros vinculados serão removidos.`"
        confirmText="Excluir"
        cancelText="Cancelar"
        @confirm="removeConfirmed"
        @cancel="showConfirm = false"
    />
  </section>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import type { CategoryDto } from '@/types'
import {
  getCategories,
  deleteCategory
} from '@/services/api'
import CategoryForm from '@/components/CategoryForm.vue'
import ConfirmModal from '@/components/ConfirmModal.vue'

const allCategories   = ref<CategoryDto[]>([])
const editingCategory = ref<CategoryDto|undefined>()
const filterName      = ref<string>('')

// modal de confirmação
const showConfirm = ref(false)
const pendingId   = ref<string>()
const pendingName = ref<string>('')

// paginação
const pageSizeOptions = [10,20,50]
const pageSize        = ref(10)
const currentPage     = ref(0)

const totalPages = computed(() =>
    Math.ceil(allCategories.value.length / pageSize.value)
)

const paginatedCategories = computed(() => {
  const start = currentPage.value * pageSize.value
  return allCategories.value.slice(start, start + pageSize.value)
})

async function loadCategories() {
  allCategories.value = await getCategories(filterName.value || undefined)
  if (currentPage.value >= totalPages.value) {
    currentPage.value = Math.max(0, totalPages.value - 1)
  }
}

function onSaved() {
  loadCategories()
  editingCategory.value = undefined
}

function onFilter(name: string) {
  filterName.value  = name
  currentPage.value = 0
  loadCategories()
}

function edit(cat: CategoryDto) {
  editingCategory.value = cat
}

function askRemove(cat: CategoryDto) {
  pendingId.value   = cat.id
  pendingName.value = cat.name
  showConfirm.value = true
}

async function removeConfirmed() {
  showConfirm.value = false
  if (!pendingId.value) return
  await deleteCategory(pendingId.value)
  loadCategories()
  pendingId.value = undefined
}

function prevPage() { if (currentPage.value > 0) currentPage.value-- }
function nextPage() { if (currentPage.value < totalPages.value - 1) currentPage.value++ }

onMounted(loadCategories)
</script>

<style scoped>
.categories-view {
  width: 80vw;
  margin: 0 auto;
  padding: 1rem;
}
h1 {
  text-align: center;
  margin-bottom: 1rem;
}
/* controles de paginação */
.controls {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 0.5rem;
}
/* tabela fixa, sem scroll interno */
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
