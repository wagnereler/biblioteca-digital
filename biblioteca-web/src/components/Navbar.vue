<!-- src/components/Navbar.vue -->
<template>
  <nav class="navbar">
    <ul class="nav-list">
      <li><router-link to="/categories">Categorias</router-link></li>
      <li><router-link to="/books">Livros</router-link></li>
      <li><router-link to="/members">Membros</router-link></li>
      <li><router-link to="/loans">Empréstimos</router-link></li>
    </ul>
  </nav>
</template>

<script setup lang="ts">
// componente puramente declarativo
</script>
