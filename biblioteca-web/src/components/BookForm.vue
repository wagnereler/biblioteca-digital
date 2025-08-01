<!-- src/components/BookForm.vue -->
<template>
  <form @submit.prevent="onSubmit" class="form-inline">
    <input v-model="form.title"
           placeholder="Título"
           required
           class="form-input" />
    <input v-model="form.author"
           placeholder="Autor"
           required
           class="form-input" />
    <input v-model="form.isbn"
           placeholder="ISBN"
           required
           class="form-input" />
    <input v-model.number="form.availableCopies"
           type="number" min="1"
           placeholder="Cópias"
           required
           class="form-input" />
    <select v-model="form.categoryId"
            required
            class="form-input">
      <option disabled value="">Selecione categoria</option>
      <option v-for="c in categories"
              :key="c.id"
              :value="c.id">
        {{ c.name }}
      </option>
    </select>
    <button type="submit"
            class="btn"
            :disabled="disableSubmit">
      {{ form.id ? 'Atualizar' : 'Criar' }}
    </button>
  </form>
</template>

<script setup lang="ts">
import { ref, onMounted, watch, computed } from 'vue'
import type { CategoryDto, BookDto, BookCreateDto } from '@/types'
import { getCategories, createBook, updateBook } from '@/services/api'

interface Form {
  id?: string
  title: string
  author: string
  isbn: string
  availableCopies: number
  categoryId: string
}

// props & emits
const props = defineProps<{ modelValue?: BookDto }>()
const emit  = defineEmits(['update:modelValue','saved'] as const)

// estado
const form = ref<Form>({ title:'', author:'', isbn:'', availableCopies:1, categoryId:'' })

// carregar categorias
const categories = ref<CategoryDto[]>([])
onMounted(async () => {
  categories.value = await getCategories()
})

// preencher form em edição
watch(() => props.modelValue, book => {
  if (book) {
    form.value = {
      id:             book.id,
      title:          book.title,
      author:         book.author,
      isbn:           book.isbn,
      availableCopies: book.availableCopies,
      categoryId:     book.category.id
    }
  } else {
    reset()
  }
}, { immediate: true })

// não permitir submit vazio
const disableSubmit = computed(() =>
    !form.value.title.trim() ||
    !form.value.author.trim() ||
    !form.value.isbn.trim() ||
    form.value.availableCopies < 1 ||
    !form.value.categoryId
)

function reset() {
  form.value = { title:'', author:'', isbn:'', availableCopies:1, categoryId:'' }
}

async function onSubmit() {
  const payload: BookCreateDto = {
    title:           form.value.title.trim(),
    author:          form.value.author.trim(),
    isbn:            form.value.isbn.trim(),
    availableCopies: form.value.availableCopies,
    category:        { id: form.value.categoryId }
  }

  if (form.value.id) {
    await updateBook(form.value.id, payload)
  } else {
    await createBook(payload)
  }

  emit('saved')
  emit('update:modelValue', undefined)
  reset()
}
</script>

<style scoped>
.form-inline {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  flex-wrap: wrap;
  width: 100%;
  margin-bottom: 1rem;
}
.form-input {
  flex: 1;
  min-width: 120px;
  padding: 0.5rem;
  border: 1px solid #ccc;
  border-radius: 4px;
}
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
</style>
