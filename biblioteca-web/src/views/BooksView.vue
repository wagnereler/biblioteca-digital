<!-- src/views/BooksView.vue -->
<template>
  <section class="books-view">
    <h1>Livros</h1>

    <!-- 1) form de criação/edição -->
    <div class="form-inline">
      <BookForm
          v-model="editingBook"
          @saved="onSaved"
      />
    </div>

    <!-- 2) form de pesquisa -->
    <div class="form-inline">
      <input
          v-model="searchTitle"
          type="text"
          placeholder="Título"
          class="form-input"
      />
      <input
          v-model="searchAuthor"
          type="text"
          placeholder="Autor"
          class="form-input"
      />
      <input
          v-model="searchIsbn"
          type="text"
          placeholder="ISBN"
          class="form-input"
      />
      <select
          v-model="searchCategory"
          class="form-input"
      >
        <option value="">Todas as categorias</option>
        <option
            v-for="c in categories"
            :key="c.id"
            :value="c.name"
        >
          {{ c.name }}
        </option>
      </select>
      <button
          class="btn"
          :disabled="!hasFilter"
          @click="onFilter"
      >
        Pesquisar
      </button>
    </div>

    <!-- 3) controles de paginação -->
    <div class="controls">
      <div class="page-size">
        <label>Itens por página:</label>
        <select v-model.number="pageSize" @change="loadBooks">
          <option v-for="opt in pageSizeOptions" :key="opt" :value="opt">
            {{ opt }}
          </option>
        </select>
      </div>
      <div class="page-info">
        Página {{ currentPage + 1 }} de {{ totalPages }}
      </div>
    </div>

    <!-- 4) tabela de livros -->
    <table class="books-table">
      <thead>
      <tr>
        <th class="col-title">Título</th>
        <th class="col-author">Autor</th>
        <th class="col-isbn">ISBN</th>
        <th class="col-copies">Cópias</th>
        <th class="col-category">Categoria</th>
        <th class="col-actions">Ações</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="book in paginatedBooks" :key="book.id">
        <td>{{ book.title }}</td>
        <td>{{ book.author }}</td>
        <td>{{ book.isbn }}</td>
        <td>{{ book.availableCopies }}</td>
        <td>{{ book.category?.name ?? '—' }}</td>
        <td class="col-actions">
          <button @click="edit(book)" title="Editar">✏️</button>
          <button @click="askRemove(book)" title="Excluir">🗑️</button>
        </td>
      </tr>
      </tbody>
    </table>

    <!-- 5) pager -->
    <div class="pager">
      <button :disabled="currentPage === 0" @click="prevPage">‹ Anterior</button>
      <button :disabled="currentPage >= totalPages - 1" @click="nextPage">Próximo ›</button>
    </div>

    <!-- 6) modal de confirmação -->
    <ConfirmModal
        :visible="showConfirm"
        title="Confirmação"
        :message="`Deseja realmente excluir “${pendingBook?.title}”?`"
        confirmText="Excluir"
        cancelText="Cancelar"
        @confirm="removeConfirmed"
        @cancel="showConfirm = false"
    />
  </section>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import type { BookDto, CategoryDto } from '@/types'
import {
  getBooks,
  deleteBook,
  getCategories
} from '@/services/api'
import BookForm from '@/components/BookForm.vue'
import ConfirmModal from '@/components/ConfirmModal.vue'

// lista de livros + edição
const allBooks      = ref<BookDto[]>([])
const editingBook   = ref<BookDto | undefined>()

// lista de categorias (para o filtro)
const categories    = ref<CategoryDto[]>([])

// filtros de pesquisa
const searchTitle    = ref('')
const searchAuthor   = ref('')
const searchIsbn     = ref('')
const searchCategory = ref('') // name da categoria

const hasFilter = computed(() =>
    !!(
        searchTitle.value.trim() ||
        searchAuthor.value.trim() ||
        searchIsbn.value.trim() ||
        searchCategory.value
    )
)

// paginação
const pageSizeOptions = [10, 20, 50]
const pageSize        = ref<number>(10)
const currentPage     = ref<number>(0)
const totalPages = computed(() =>
    Math.ceil(allBooks.value.length / pageSize.value)
)
const paginatedBooks = computed(() => {
  const start = currentPage.value * pageSize.value
  return allBooks.value.slice(start, start + pageSize.value)
})

// modal de exclusão
const showConfirm  = ref(false)
const pendingBook  = ref<BookDto | undefined>()

// carrega dados
async function loadCategories() {
  categories.value = await getCategories()
}
async function loadBooks() {
  const filters: Record<string,string> = {}
  if (searchTitle.value.trim())    filters.title    = searchTitle.value.trim()
  if (searchAuthor.value.trim())   filters.author   = searchAuthor.value.trim()
  if (searchIsbn.value.trim())     filters.isbn     = searchIsbn.value.trim()
  if (searchCategory.value)        filters.category = searchCategory.value

  allBooks.value = await getBooks(filters)
  if (currentPage.value >= totalPages.value) {
    currentPage.value = Math.max(0, totalPages.value - 1)
  }
}

// handlers
function onSaved() {
  editingBook.value = undefined
  loadBooks()
}
function onFilter() {
  currentPage.value = 0
  loadBooks()
}
function edit(book: BookDto) {
  editingBook.value = book
}
function askRemove(book: BookDto) {
  pendingBook.value = book
  showConfirm.value = true
}
async function removeConfirmed() {
  showConfirm.value = false
  if (!pendingBook.value) return
  await deleteBook(pendingBook.value.id)
  pendingBook.value = undefined
  loadBooks()
}
function prevPage() { if (currentPage.value > 0) currentPage.value-- }
function nextPage() { if (currentPage.value < totalPages.value - 1) currentPage.value++ }

onMounted(() => {
  loadCategories()
  loadBooks()
})
</script>

<style scoped>
.books-view {
  width: 80vw;
  margin: 0 auto;
  padding: 1rem;
}
h1 {
  text-align: center;
  margin-bottom: 1rem;
}

/* container flexível para formulários */
.form-inline {
  display: flex;
  align-items: flex-start;
  gap: 0.75rem;
  flex-wrap: wrap;
  width: 100%;
  margin-bottom: 1rem;
}

/* inputs e selects flexíveis */
.form-input {
  flex: 1;
  min-width: 140px;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}

/* botões padrão */
.btn {
  padding: 0.5rem 0.8rem;
  border: 1px solid #333;
  background: #fff;
  cursor: pointer;
}
.btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

/* paginação */
.controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}
.page-size label {
  margin-right: 0.5rem;
}

/* tabela */
.books-table {
  width: 100%;
  border-collapse: collapse;
  table-layout: fixed;
  margin-bottom: 0.5rem;
}
.books-table th,
.books-table td {
  border: 1px solid #ddd;
  padding: 0.75rem;
  vertical-align: top;
  word-break: break-word;
}
.col-title    { width: 25%; }
.col-author   { width: 20%; }
.col-isbn     { width: 15%; }
.col-copies   { width: 10%; text-align: center; }
.col-category { width: 20%; }
.col-actions  { width: 10%; text-align: center; }
.col-actions button {
  background: none;
  border: none;
  cursor: pointer;
  margin: 0 0.25rem;
  font-size: 1.2rem;
}

/* pager */
.pager {
  display: flex;
  gap: 0.5rem;
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
