<template>
  <div :class="['stat-box', { full }]">
    <div class="label">{{ label }}</div>
    <div class="value">{{ value }}</div>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  label: string
  value: string | number
  full?: boolean
}>()
</script>

<style scoped>
.stat-box {
  background-color: #f9f9f9;
  padding: 1rem;
  border-radius: 8px;
  text-align: center;
  flex: 1;
}

.label {
  font-size: 0.85rem;
  color: #666;
  margin-bottom: 0.25rem;
}

.value {
  font-weight: bold;
  font-size: 1.4rem;
  color: #111;
}

.full {
  grid-column: span 2;
}
</style>
