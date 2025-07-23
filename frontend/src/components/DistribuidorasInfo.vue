<template>
  <div class="card">
    <div class="header">
      <img src="/images/sagemcom.png" alt="Sagemcom Logo" class="logo" />
      <h2>SAGEMCOM</h2>
    </div>

    <div class="stats-grid">
      <StatBox label="Total Recepcionados" :value="36" />
      <StatBox label="Pendientes de Enviar" :value="152" />
      <StatBox label="Tasa de Recuperación" :value="'74.24%'" />
      <StatBox label="Pendientes de Recepción" :value="0" />
      <StatBox label="Promedio de Días" :value="2" full />
    </div>
  </div>
</template>

<script setup lang="ts">
// Puedes poner la imagen del logo en "public/logo.svg"
import StatBox from './StatBox.vue'
</script>

<style scoped>
.card {
  background: white;
  border-radius: 8px;
  padding: 1rem;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
  max-width: 300px;
  margin: auto;
  font-family: sans-serif;
}

.header {
  text-align: center;
  border-bottom: 2px solid #222;
  padding-bottom: 0.5rem;
  margin-bottom: 1rem;
}

.logo {
  width: 30px;
  vertical-align: middle;
  margin-right: 0.5rem;
}

.stats-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0.75rem;
}

@media (max-width: 400px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
}
</style>
