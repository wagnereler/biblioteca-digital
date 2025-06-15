<!-- src/components/CategoryForm.vue -->
<template>
  <div class="form-inline">
    <!-- 1) Criação / Edição -->
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

    <!-- 2) Pesquisa -->
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
import { createCategory, updateCategory } from '@/services/api'

// Props / Emits
const props = defineProps<{ modelValue?: CategoryDto }>()
const emit  = defineEmits(['update:modelValue','saved','search'])

// Estados internos
const name       = ref<string>('')
const searchName = ref<string>('')

// Sinaliza se estamos editando um registro existente
const isEditing = computed(() => !!props.modelValue)

// Não permite enviar quando o campo estiver vazio
const disableSubmit = computed(() => !name.value.trim())

// Sincroniza o formulário quando vem um modelValue do pai
watch(
    () => props.modelValue,
    cat => {
      name.value = cat?.name ?? ''
    },
    { immediate: true }
)

async function onSubmit() {
  const trimmed = name.value.trim()
  if (!trimmed) return  // impede envio vazio

  if (isEditing.value && props.modelValue) {
    await updateCategory(props.modelValue.id, { name: trimmed })
  } else {
    await createCategory({ name: trimmed })
  }

  // limpa e notifica o pai
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
  align-items: center;    /* agora todos no mesmo nível vertical */
  gap: 0.5rem;
  flex-wrap: wrap;
  width: 100%;
  margin-bottom: 1rem;
}
.form-input {
  flex: 1;               /* cresce para preencher o espaço */
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
