<!-- src/views/CategoriesView.vue -->
<!-- src/views/CategoriesView.vue -->
<template>
  <section class="categories-view">
    <h1>Categorias</h1>

    <CategoryForm
        v-model="editingCategory"
        @saved="onSaved"
        @search="onFilter"
    />

    <div class="controls">
      <!-- ... page-size e page-info ... -->
    </div>

    <table class="categories-table">
      <!-- ... cabeçalho ... -->
      <tbody>
      <tr v-for="cat in paginatedCategories" :key="cat.id">
        <td>{{ cat.name }}</td>
        <td>{{ cat.id }}</td>
        <td class="col-actions">
          <button @click="edit(cat)">✏️</button>
          <button @click="askRemove(cat)">🗑️</button>
        </td>
      </tr>
      </tbody>
    </table>

    <div class="pager">
      <!-- ... botões de paginação ... -->
    </div>

    <!-- o nosso modal de confirmação -->
    <ConfirmModal
        :visible="showConfirm"
        title="Confirmação"
        :message="`Ao excluir a categoria “${pendingName}” você removerá também todos os livros vinculados. Deseja prosseguir?`"
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
import { getCategories, deleteCategory } from '@/services/api'
import CategoryForm  from '@/components/CategoryForm.vue'
import ConfirmModal  from '@/components/ConfirmModal.vue'

const allCategories   = ref<CategoryDto[]>([])
const editingCategory = ref<CategoryDto|undefined>()
const filterName      = ref<string>('')

// estados para o modal
const showConfirm     = ref(false)
const pendingId       = ref<string|undefined>()
const pendingName     = ref<string>('')

// PAGINAÇÃO (mesma lógica de antes)...
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
  if (currentPage.value >= totalPages.value)
    currentPage.value = Math.max(0, totalPages.value - 1)
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

// em vez de chamar delete direto, abrimos o modal
function askRemove(cat: CategoryDto) {
  pendingId.value   = cat.id
  pendingName.value = cat.name
  showConfirm.value = true
}

// quando clicar em “Excluir” no modal
async function removeConfirmed() {
  showConfirm.value = false
  if (!pendingId.value) return
  await deleteCategory(pendingId.value)
  loadCategories()
  pendingId.value = undefined
}

function prevPage() {
  if (currentPage.value > 0) currentPage.value--
}
function nextPage() {
  if (currentPage.value < totalPages.value - 1) currentPage.value++
}

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
