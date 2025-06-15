<template>
  <teleport to="body">
    <div v-if="visible" class="modal-backdrop" @click.self="onCancel">
      <div class="modal-container">
        <header class="modal-header">
          <h3>{{ title }}</h3>
        </header>
        <section class="modal-body">
          <p>{{ message }}</p>
        </section>
        <footer class="modal-footer">
          <button class="btn cancel" @click="onCancel">{{ cancelText }}</button>
          <button class="btn confirm" @click="onConfirm">{{ confirmText }}</button>
        </footer>
      </div>
    </div>
  </teleport>
</template>

<script setup lang="ts">


const props = defineProps<{
  visible: boolean
  title?: string
  message: string
  confirmText?: string
  cancelText?: string
}>()

const emit = defineEmits<{
  (e: 'confirm'): void
  (e: 'cancel'): void
}>()

const onConfirm = () => emit('confirm')
const onCancel  = () => emit('cancel')
</script>

<style scoped>
.modal-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.modal-container {
  background: white;
  border-radius: 4px;
  width: 90%;
  max-width: 400px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.3);
  overflow: hidden;
}
.modal-header {
  padding: 1rem;
  border-bottom: 1px solid #eee;
}
.modal-body {
  padding: 1rem;
}
.modal-footer {
  padding: 0.75rem;
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
  border-top: 1px solid #eee;
}
.btn {
  padding: 0.5rem 0.75rem;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}
.btn.cancel {
  background: #f0f0f0;
}
.btn.confirm {
  background: #d9534f;
  color: white;
}
</style>
