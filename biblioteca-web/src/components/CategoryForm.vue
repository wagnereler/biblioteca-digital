<!--src/components/CategoryForm.vue-->
<template>
  <div class="form-inline">
    <input
        v-model="name"
        type="text"
        placeholder="Descrição da Categoria"
        class="form-input"
    />
    <button
        type="button"
        class="btn"
        :disabled="disableSubmit"
        @click="onSubmit"
    >
      {{ isEditing ? 'Atualizar' : 'Criar Categoria' }}
    </button>

    <input
        v-model="searchName"
        type="text"
        placeholder="Pesquisar por nome"
        class="form-input"
    />
    <button
        type="button"
        class="btn"
        :disabled="!searchName.trim()"
        @click="onSearch"
    >
      Pesquisar
    </button>
  </div>
</template>

<script setup lang="ts">
import { ref, watch, computed } from 'vue'
import type { CategoryDto } from '@/types'
import { getCategories, createCategory, updateCategory } from '@/services/api'

const props = defineProps<{ modelValue?: CategoryDto }>()
const emit  = defineEmits(['update:modelValue','saved','search'] as const)

const name       = ref('')
const searchName = ref('')

const isEditing = computed(() => !!props.modelValue)
const disableSubmit = computed(() => !name.value.trim())

watch(() => props.modelValue, cat => {
  name.value = cat?.name ?? ''
}, { immediate: true })

async function onSubmit() {
  const trimmed = name.value.trim()
  if (!trimmed) return

  if (isEditing.value && props.modelValue) {
    await updateCategory(props.modelValue.id, { name: trimmed })
  } else {
    await createCategory({ name: trimmed })
  }

  name.value = ''
  emit('saved')
  emit('update:modelValue', undefined)
}

function onSearch() {
  const trimmed = searchName.value.trim()
  if (!trimmed) return
  emit('search', trimmed)
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
  min-width: 200px;
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
