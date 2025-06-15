<!-- src/components/CategoryForm.vue -->
<template>
  <form @submit.prevent="onSubmit" class="category-form">
    <input
        v-model="name"
        type="text"
        placeholder="Descrição da Categoria"
        required
    />
    <button type="submit">
      {{ modelValue ? 'Atualizar Categoria' : 'Criar Categoria' }}
    </button>
  </form>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import type { CategoryDto } from '@/types'
import { createCategory, updateCategory } from '@/services/api'

/** recebe o objeto a editar (v-model) */
const props = defineProps<{ modelValue?: CategoryDto }>()

/**
 * avisa o parent que salvou
 * e também reseta o modelValue
 */
const emit = defineEmits([
  'update:modelValue',
  'saved'
])

const name = ref('')

watch(
    () => props.modelValue,
    cat => {
      name.value = cat?.name || ''
    },
    { immediate: true }
)

async function onSubmit() {
  if (props.modelValue) {
    await updateCategory(props.modelValue.id, { name: name.value })
  } else {
    await createCategory({ name: name.value })
  }
  // dispara evento de sucesso e limpa seleção
  emit('saved')
  emit('update:modelValue', undefined)
  name.value = ''
}
</script>

<style scoped>

.category-form {
  display: flex;
  align-items: flex-start;
  gap: 0.5rem;
  width: 100%;
}
.category-form input {
  flex: 1;
  min-width: 0;
  padding: 0.5rem;
  font-size: 1rem;
}
.category-form button {
  padding: 0.5rem 1rem;
  font-size: 1rem;
}

</style>
