<template>
  <div class="flex flex-col items-center justify-center min-h-screen bg-gray-50 p-4">
    <div class="bg-white border rounded-2xl shadow-md p-8 max-w-md w-full text-center pb-4 pt-4">
      <!-- Texto de advertencia -->
      <p class="text-red-600 font-semibold mb-6">
        ⚠️ Atención: Antes de descargar el backup asegúrese de que nadie esté modificando la base de datos.
      </p>

      <!-- Botón centrado -->
      <button @click="downloadBackup">Descargar Backup</button>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    async downloadBackup() {
      const response = await fetch("http://localhost:8080/api/backup/download");
      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);

      const a = document.createElement("a");
      a.href = url;
      a.download = "backup_postgres.sql";
      a.click();
      window.URL.revokeObjectURL(url);
    }
  }
};
</script>
