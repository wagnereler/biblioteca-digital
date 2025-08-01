<!-- src/views/BooksView.vue -->
<template>
  <section class="books-view">
    <h1>Livros</h1>
    <BookForm
        v-model="editingBook"
        @saved="onSaved"
    />

    <table class="books-table">
      <thead>
      <tr>
        <th>Título</th>
        <th>Autor</th>
        <th>ISBN</th>
        <th>Cópias</th>
        <th>Categoria</th>
        <th>Ações</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="b in books" :key="b.id">
        <td>{{ b.title }}</td>
        <td>{{ b.author }}</td>
        <td>{{ b.isbn }}</td>
        <td>{{ b.availableCopies }}</td>
        <td>{{ b.category?.name ?? '—' }}</td>
        <td>
          <button @click="edit(b)">✏️</button>
          <button @click="remove(b.id)">🗑️</button>
        </td>
      </tr>
      </tbody>
    </table>
  </section>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import type { BookDto } from '@/types'
import { getBooks, deleteBook } from '@/services/api'
import BookForm from '@/components/BookForm.vue'

const books = ref<BookDto[]>([])
const editingBook = ref<BookDto|undefined>()

async function loadBooks() {
  books.value = await getBooks()
}

function onSaved() {
  editingBook.value = undefined
  loadBooks()
}

function edit(b: BookDto) {
  editingBook.value = b
}

async function remove(id: string) {
  if (!confirm('Deseja realmente excluir este livro?')) return
  await deleteBook(id)
  loadBooks()
}

onMounted(loadBooks)
</script>

<style scoped>
.books-view {
  width: 80vw;
  margin: 0 auto;
  padding: 1rem;
}
.books-table {
  width: 100%;
  border-collapse: collapse;
}
.books-table th,
.books-table td {
  border: 1px solid #ddd;
  padding: 0.5rem;
  text-align: left;
}
.books-table th {
  background: #f5f5f5;
}
</style>
