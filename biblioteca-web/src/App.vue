<!-- src/App.vue -->
<template>
  <div id="app">
    <Navbar />
    <router-view/>
  </div>
</template>

<script setup lang="ts">
import Navbar from '@/components/Navbar.vue'
</script>

<style>
/* se já tiver global, pode manter vazio ou ajustes mínimos */
#app {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}
</style>

