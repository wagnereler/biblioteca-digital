<!-- src/views/LoansView.vue -->
<template>
  <section class="loans-view">
    <h1>Empréstimos</h1>

    <!-- 1) form de criação de empréstimo -->
    <LoanForm @saved="onSaved" />

    <!-- 2) filtros: pesquisa por membro ou livro -->
    <div class="form-inline">
      <input
          v-model="searchMember"
          type="text"
          placeholder="Pesquisar por membro"
          class="form-input"
      />
      <input
          v-model="searchBook"
          type="text"
          placeholder="Pesquisar por livro"
          class="form-input"
      />
      <button
          class="btn"
          :disabled="!hasLoanFilter"
          @click="onFilterLoans"
      >
        Filtrar
      </button>
    </div>

    <!-- 3) controles de paginação -->
    <div class="controls">
      <div class="page-size">
        <label>Itens por página:</label>
        <select v-model.number="pageSize" @change="resetPage">
          <option v-for="opt in pageSizeOptions" :key="opt" :value="opt">
            {{ opt }}
          </option>
        </select>
      </div>
      <div class="page-info">
        Página {{ currentPage + 1 }} de {{ totalPages }}
      </div>
    </div>

    <!-- 4) tabela de empréstimos -->
    <table class="loans-table">
      <thead>
      <tr>
        <th>Livro</th>
        <th>Membro</th>
        <th>Empréstimo</th>
        <th>Vencimento</th>
        <th>Devolução</th>
        <th>Status</th>
        <th>Ações</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="l in paginatedLoans" :key="l.id">
        <td>{{ l.book.title }}</td>
        <td>{{ l.member.name }}</td>
        <td>{{ l.loanDate }}</td>
        <td>{{ l.dueDate }}</td>
        <td>{{ l.returnDate ?? '—' }}</td>
        <td>{{ l.status }}</td>
        <td class="col-actions">
          <button
              v-if="l.status !== 'RETURNED'"
              @click="askReturn(l)"
              title="Marcar Devolução"
          >
            ↩️
          </button>
        </td>
      </tr>
      </tbody>
    </table>

    <!-- 5) pager -->
    <div class="pager">
      <button :disabled="currentPage === 0" @click="prevPage">‹ Anterior</button>
      <button
          :disabled="currentPage >= totalPages - 1"
          @click="nextPage"
      >
        Próximo ›
      </button>
    </div>

    <!-- 6) confirmação de devolução -->
    <ConfirmModal
        :visible="showConfirm"
        title="Confirmar Devolução"
        :message="`Marcar como devolvido “${pendingLoan?.book.title}” para ${pendingLoan?.member.name}?`"
        confirmText="Devolver"
        cancelText="Cancelar"
        @confirm="returnConfirmed"
        @cancel="showConfirm = false"
    />
  </section>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import type { LoanDto } from '@/types'
import {
  getLoans,
  returnLoan
} from '@/services/api'
import LoanForm from '@/components/LoanForm.vue'
import ConfirmModal from '@/components/ConfirmModal.vue'

// dados brutos
const allLoans = ref<LoanDto[]>([])

// filtros de pesquisa
const searchMember = ref('')
const searchBook   = ref('')
const hasLoanFilter = computed(() =>
    !!(searchMember.value.trim() || searchBook.value.trim())
)

// resultados filtrados
const filteredLoans = computed(() => {
  return allLoans.value.filter(loan => {
    const matchMember = searchMember.value
        ? loan.member.name.toLowerCase().includes(searchMember.value.toLowerCase())
        : true
    const matchBook = searchBook.value
        ? loan.book.title.toLowerCase().includes(searchBook.value.toLowerCase())
        : true
    return matchMember && matchBook
  })
})

// paginação
const pageSizeOptions = [10, 20, 50]
const pageSize    = ref(10)
const currentPage = ref(0)
const totalPages = computed(() =>
    Math.ceil(filteredLoans.value.length / pageSize.value)
)
const paginatedLoans = computed(() => {
  const start = currentPage.value * pageSize.value
  return filteredLoans.value.slice(start, start + pageSize.value)
})

// modal de devolução
const showConfirm = ref(false)
const pendingLoan = ref<LoanDto | undefined>()

// carrega todos empréstimos
async function loadLoans() {
  allLoans.value = await getLoans()
}

// handlers
function onSaved() {
  loadLoans()
}

function onFilterLoans() {
  currentPage.value = 0
}
function resetPage() {
  currentPage.value = 0
}

function askReturn(l: LoanDto) {
  pendingLoan.value = l
  showConfirm.value = true
}

async function returnConfirmed() {
  if (!pendingLoan.value) return
  await returnLoan(pendingLoan.value.id)
  showConfirm.value = false
  pendingLoan.value = undefined
  loadLoans()
}

function prevPage() {
  if (currentPage.value > 0) currentPage.value--
}
function nextPage() {
  if (currentPage.value < totalPages.value - 1) currentPage.value++
}

// on mount
onMounted(loadLoans)
</script>

<style scoped>
.loans-view {
  width: 80vw;
  margin: 0 auto;
  padding: 1rem;
}
h1 {
  text-align: center;
  margin-bottom: 1rem;
}

/* form-inline + form-input + btn para filtros */
.form-inline {
  display: flex;
  align-items: flex-start;
  gap: 0.75rem;
  flex-wrap: wrap;
  width: 100%;
  margin-bottom: 1rem;
}
.form-input {
  flex: 1;
  min-width: 160px;
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
.loans-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 0.5rem;
}
.loans-table th,
.loans-table td {
  border: 1px solid #ddd;
  padding: 0.75rem;
  text-align: left;
  word-break: break-word;
}
.col-actions {
  text-align: center;
}
.col-actions button {
  background: none;
  border: none;
  cursor: pointer;
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
