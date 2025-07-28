<template>
  <footer class="footer">
    <div class="container">
      <div class="row">
        <div class="col text-center mt-3">
          <p>&copy; {{ new Date().getFullYear() }} - CelNet Comunicaciones</p>
        </div>
      </div>
    </div>
  </footer>
</template>

<script>
export default {
  name: "footerComponent",
};
</script>

<style scoped>
.footer {
  background-color: #6c757d; /* Fondo claro */
  padding: 10px 0;
  color: #ffffff;
  font-size: 15px;
}

.container {
  max-width: 800px;
  margin: auto;
}
</style>

  