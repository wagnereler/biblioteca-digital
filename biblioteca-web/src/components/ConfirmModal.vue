<!-- src/components/ConfirmModal.vue -->
<template>
  <Teleport to="body">
    <div v-if="visible" class="modal-overlay">
      <div class="modal-box">
        <header class="modal-header">
          <h3>{{ title }}</h3>
        </header>
        <div class="modal-body">
          <p>{{ message }}</p>
        </div>
        <footer class="modal-footer">
          <button class="btn btn-cancel" @click="$emit('cancel')">
            {{ cancelText }}
          </button>
          <button class="btn btn-confirm" @click="$emit('confirm')">
            {{ confirmText }}
          </button>
        </footer>
      </div>
    </div>
  </Teleport>
</template>

<script setup lang="ts">
const props = defineProps({
  visible:     { type: Boolean, required: true },
  title:       { type: String,  default: ''     },
  message:     { type: String,  default: ''     },
  confirmText: { type: String,  default: 'OK'   },
  cancelText:  { type: String,  default: 'Cancelar' },
})
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}
.modal-box {
  background: #fff;
  border-radius: 6px;
  width: 90%;
  max-width: 400px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.2);
  overflow: hidden;
}
.modal-header {
  padding: 1rem;
  border-bottom: 1px solid #eee;
}
.modal-header h3 {
  margin: 0;
  font-size: 1.25rem;
}
.modal-body {
  padding: 1rem;
}
.modal-footer {
  padding: 0.75rem 1rem;
  text-align: right;
  border-top: 1px solid #eee;
}
.btn {
  padding: 0.5rem 0.8rem;
  border: 1px solid #333;
  background: #fff;
  cursor: pointer;
  border-radius: 4px;
  font-size: 0.9rem;
}
.btn + .btn {
  margin-left: 0.5rem;
}
.btn-cancel {
  /* cancelar pode ficar neutro */
}
.btn-confirm {
  background-color: #dc3545;
  color: #fff;
  border-color: #dc3545;
}
.btn-confirm:hover {
  background-color: #c82333;
  border-color: #bd2130;
}
</style>
