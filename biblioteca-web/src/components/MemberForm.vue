<!-- src/components/MemberForm.vue -->
<template>
  <div class="form-inline">
    <!-- criação/edição -->
    <input
        v-model="name"
        type="text"
        placeholder="Nome"
        class="form-input"
    />
    <input
        v-model="email"
        type="email"
        placeholder="Email"
        class="form-input"
    />
    <input
        v-model="phone"
        type="text"
        placeholder="Telefone"
        class="form-input"
    />
    <button
        type="button"
        class="btn"
        :disabled="disableSubmit"
        @click="onSubmit"
    >
      {{ isEditing ? 'Atualizar' : 'Criar Membro' }}
    </button>

    <!-- buscas -->
    <input
        v-model="searchName"
        type="text"
        placeholder="Pesquisar por nome"
        class="form-input"
    />
    <input
        v-model.number="searchRegistration"
        type="number"
        placeholder="Pesquisar por matrícula"
        class="form-input"
    />
    <button
        type="button"
        class="btn"
        :disabled="!hasSearch"
        @click="onSearch"
    >
      Pesquisar
    </button>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, computed } from 'vue'
import type { MemberDto, MemberCreateDto } from '@/types'
import {
  createMember,
  updateMember
} from '@/services/api'

const props = defineProps<{ modelValue?: MemberDto }>()
const emit  = defineEmits<{
  (e: 'update:modelValue', v: MemberDto | undefined): void
  (e: 'saved'): void
  (e: 'search', filters: { name?: string; registration?: number }): void
}>()

// campos do form
const name              = ref('')
const email             = ref('')
const phone             = ref<string | undefined>(undefined)

// campos de busca
const searchName        = ref('')
const searchRegistration= ref<number | undefined>(undefined)

// estado
const isEditing = computed(() => !!props.modelValue)
const disableSubmit = computed(() =>
    !name.value.trim() ||
    !email.value.trim()
)
const hasSearch = computed(() =>
    !!(searchName.value.trim() || searchRegistration.value != null)
)

// preencher form em edição
watch(() => props.modelValue, member => {
  if (member) {
    name.value  = member.name
    email.value = member.email
    phone.value = member.phone
  } else {
    name.value = ''
    email.value = ''
    phone.value = undefined
  }
}, { immediate: true })

async function onSubmit() {
  const payload: MemberCreateDto = {
    name:  name.value.trim(),
    email: email.value.trim(),
    phone: phone.value?.trim() || undefined
  }

  if (isEditing.value && props.modelValue) {
    await updateMember(props.modelValue.id, payload)
  } else {
    await createMember(payload)
  }

  // notifica e reseta
  emit('saved')
  emit('update:modelValue', undefined)
}

function onSearch() {
  emit('search', {
    name: searchName.value.trim() || undefined,
    registration: searchRegistration.value
  })
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
  min-width: 150px;
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
