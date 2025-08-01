<!-- src/views/MembersView.vue -->
<template>
  <section class="members-view">
    <h1>Membros</h1>

    <!-- 1) formulário de Membro e filtros -->
    <MemberForm
        v-model="editingMember"
        @saved="onSaved"
        @search="onFilter"
    />

    <!-- 2) paginação -->
    <div class="controls">
      <div class="page-size">
        <label>Itens por página:</label>
        <select v-model.number="pageSize" @change="loadMembers">
          <option v-for="opt in pageSizeOptions" :key="opt" :value="opt">
            {{ opt }}
          </option>
        </select>
      </div>
      <div class="page-info">
        Página {{ currentPage + 1 }} de {{ totalPages }}
      </div>
    </div>

    <!-- 3) tabela de membros -->
    <table class="members-table">
      <thead>
      <tr>
        <th>Nome</th>
        <th>Email</th>
        <th>Telefone</th>
        <th>Matrícula</th>
        <th>Ações</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="m in paginatedMembers" :key="m.id">
        <td>{{ m.name }}</td>
        <td>{{ m.email }}</td>
        <td>{{ m.phone ?? '—' }}</td>
        <td>{{ m.registration }}</td>
        <td class="col-actions">
          <button @click="edit(m)" title="Editar">✏️</button>
          <button @click="askRemove(m)" title="Excluir">🗑️</button>
        </td>
      </tr>
      </tbody>
    </table>

    <!-- 4) pager -->
    <div class="pager">
      <button :disabled="currentPage === 0" @click="prevPage">‹ Anterior</button>
      <button :disabled="currentPage >= totalPages - 1" @click="nextPage">Próximo ›</button>
    </div>

    <!-- 5) confirmação de remoção -->
    <ConfirmModal
        :visible="showConfirm"
        title="Confirmação"
        :message="`Excluir “${pendingMember?.name}”?`"
        confirmText="Excluir"
        cancelText="Cancelar"
        @confirm="removeConfirmed"
        @cancel="showConfirm = false"
    />
  </section>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import type { MemberDto } from '@/types'
import {
  getMembers,
  deleteMember
} from '@/services/api'
import MemberForm from '@/components/MemberForm.vue'
import ConfirmModal from '@/components/ConfirmModal.vue'

// lista e edição
const allMembers      = ref<MemberDto[]>([])
const editingMember   = ref<MemberDto | undefined>()

// filtros vindos do MemberForm
const filterName         = ref<string | undefined>(undefined)
const filterRegistration = ref<number | undefined>(undefined)

// paginação
const pageSizeOptions = [10, 20, 50]
const pageSize        = ref<number>(10)
const currentPage     = ref<number>(0)
const totalPages = computed(() =>
    Math.ceil(allMembers.value.length / pageSize.value)
)
const paginatedMembers = computed(() => {
  const start = currentPage.value * pageSize.value
  return allMembers.value.slice(start, start + pageSize.value)
})

// modal de exclusão
const showConfirm    = ref(false)
const pendingMember  = ref<MemberDto | undefined>()

// carregamento da API
async function loadMembers() {
  const data = await getMembers(filterName.value, filterRegistration.value)
  allMembers.value = data
  if (currentPage.value >= totalPages.value) {
    currentPage.value = Math.max(0, totalPages.value - 1)
  }
}

// handlers
function onSaved() {
  editingMember.value = undefined
  loadMembers()
}

function onFilter(filters: { name?: string; registration?: number }) {
  filterName.value         = filters.name
  filterRegistration.value = filters.registration
  currentPage.value        = 0
  loadMembers()
}

function edit(m: MemberDto) {
  editingMember.value = m
}

function askRemove(m: MemberDto) {
  pendingMember.value = m
  showConfirm.value   = true
}

async function removeConfirmed() {
  showConfirm.value = false
  if (!pendingMember.value) return
  await deleteMember(pendingMember.value.id)
  pendingMember.value = undefined
  loadMembers()
}

function prevPage() {
  if (currentPage.value > 0) currentPage.value--
}

function nextPage() {
  if (currentPage.value < totalPages.value - 1) currentPage.value++
}

// on mount
onMounted(loadMembers)
</script>

<style scoped>
.members-view {
  width: 80vw;
  margin: 0 auto;
  padding: 1rem;
}
h1 {
  text-align: center;
  margin-bottom: 1rem;
}
.controls {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}
.page-size label {
  margin-right: 0.5rem;
}
.members-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 0.5rem;
}
.members-table th,
.members-table td {
  border: 1px solid #ddd;
  padding: 0.75rem;
  text-align: left;
}
.col-actions {
  text-align: center;
}
.col-actions button {
  background: none;
  border: none;
  cursor: pointer;
  margin: 0 0.25rem;
  font-size: 1.2rem;
}
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
