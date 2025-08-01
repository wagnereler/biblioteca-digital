<!-- src/components/LoanForm.vue -->
<template>
  <div class="form-inline">
    <select v-model="memberId" class="form-input" required>
      <option disabled value="">Selecione Membro</option>
      <option v-for="m in members" :key="m.id" :value="m.id">
        {{ m.name }} (#{{
          m.registration }})
      </option>
    </select>

    <select v-model="bookId" class="form-input" required>
      <option disabled value="">Selecione Livro</option>
      <option v-for="b in books" :key="b.id" :value="b.id">
        {{ b.title }} — {{ b.availableCopies }} disponível(is)
      </option>
    </select>

    <button
        type="button"
        class="btn"
        :disabled="!canSubmit"
        @click="onSubmit"
    >
      Criar Empréstimo
    </button>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import type { MemberDto, BookDto, CreateLoanDto } from '@/types'
import {
  getMembers,
  getBooks,
  createLoan
} from '@/services/api'

// props/emits
const emit = defineEmits<{
  (e: 'saved'): void
}>()

// estado
const members = ref<MemberDto[]>([])
const books   = ref<BookDto[]>([])
const memberId = ref<string>('')
const bookId   = ref<string>('')

// carregamento inicial
onMounted(async () => {
  members.value = await getMembers()
  books.value   = await getBooks()
})

// validação
const canSubmit = computed(() =>
    !!memberId.value && !!bookId.value
)

async function onSubmit() {
  const payload: CreateLoanDto = {
    memberId: memberId.value,
    bookId:   bookId.value
  }
  await createLoan(payload)
  // limpa form e avisa
  memberId.value = ''
  bookId.value   = ''
  emit('saved')
}
</script>

<style scoped>
.form-inline {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  flex-wrap: wrap;
  margin-bottom: 1rem;
}
.form-input {
  flex: 1;
  min-width: 180px;
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
