<template>
  <div class="container mt-3 border">
    <div class="mb-3">
      <label for="distribuidora" class="form-label">Código Distribuidora</label>
      <select id="distribuidora" v-model="selectedDistribuidora" @change="onDistribuidoraChange" class="form-select">
        <option value="" disabled>Seleccione una distribuidora</option>
        <option value="777">Iberdrola I-DE</option>
        <option value="888">SAGEMCOM</option>
        <option value="999">LANDIS&GYR</option>
      </select>
    </div>

    <div v-if="selectedDistribuidora" class="mb-3">
      <label for="fileInput" class="form-label">Importar archivo</label>
      <input type="file" id="fileInput" @change="validateFile" class="form-control" accept=".xlsx, .xls" />
    </div>

    <div class="d-flex gap-2 mt-3">
      <button @click="uploadFile" :disabled="!isFileValid || loading" class="btn btn-primary">
        <span v-if="loading" class="loading-dots">
          Cargando<span class="dot">.</span><span class="dot">.</span><span class="dot">.</span>
        </span>
        <span v-else>Enviar</span>
      </button>
      <button @click="cancelUpload" class="btn btn-secondary">Cancelar</button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { Modal } from 'bootstrap';
import Swal from 'sweetalert2';

export default {
  data() {
    return {
      selectedDistribuidora: "",
      selectedFile: null,
      isFileValid: false,
      loading: false,
      cancelTokenSource: null, 
    };
  },
  methods: {
    onDistribuidoraChange() {
      this.selectedFile = null; 
      this.isFileValid = false; 
      this.loading = false; 
    },
    validateFile(event) {
      const file = event.target.files[0];
      const validExtensions = [".xlsx", ".xls"];

      if (file) {
        const fileExtension = file.name.slice(file.name.lastIndexOf(".")).toLowerCase();
        if (validExtensions.includes(fileExtension)) {
          this.selectedFile = file;
          this.isFileValid = true;
        } else {
          this.isFileValid = false;
          Swal.fire({
            icon: "error",
            title: "Archivo no válido",
            text: "Por favor, selecciona un archivo .xlsx o .xls.",
          });
        }
      }
    },
    async uploadFile() {
      if (!this.selectedDistribuidora || !this.selectedFile || !this.isFileValid) return;

      this.loading = true;
      this.cancelTokenSource = axios.CancelToken.source();

      const formData = new FormData();
      formData.append("file", this.selectedFile);
      formData.append("codDistribuidora", this.selectedDistribuidora);

      try {
        const response = await axios.post(
          "http://localhost:8080/upload",
          formData,
          {
            headers: {
              "Content-Type": "multipart/form-data",
            },
            cancelToken: this.cancelTokenSource.token,
          }
        );
        Swal.fire({
          icon: "success",
          title: "Éxito",
          text: response.data.mensaje,
        });
      } catch (error) {
        if (axios.isCancel(error)) {
          Swal.fire({
            icon: "info",
            title: "Carga cancelada",
            text: "El proceso de carga ha sido cancelado.",
          });
        } else {
          Swal.fire({
            icon: "error",
            title: "Error al cargar el archivo",
            text: error.response?.data?.error || "Ocurrió un error inesperado.",
          });
        }
      } finally {
        this.loading = false;
      }
    },
    cancelUpload() {
      if (this.cancelTokenSource) {
        this.cancelTokenSource.cancel("Carga cancelada por el usuario.");
      }
      this.resetState();
    },
    resetState() {
      this.selectedDistribuidora = "";
      this.selectedFile = null;
      this.isFileValid = false;
      this.loading = false;
      this.cancelTokenSource = null;
    },
  },
};
</script>

<style scoped>
.container {
  max-width: 1400px;
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  font-family: "Quicksand", sans-serif;
}

.btn-primary {
  border-radius: 25px;
  background-color: #274469;
  border-color: #274469;
}

.btn-primary:hover {
  background-color: #1d314b;
  border-color: #1d314b;
}

.btn-secondary {
  border-radius: 25px;
}

.mb-3 {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.form-select {
  color: rgba(0, 0, 0, 0.5);
}

.loading-dots .dot {
  animation: blink 1.5s infinite;
}

.loading-dots .dot:nth-child(1) {
  animation-delay: 0s;
}

.loading-dots .dot:nth-child(2) {
  animation-delay: 0.2s;
}

.loading-dots .dot:nth-child(3) {
  animation-delay: 0.4s;
}

@keyframes blink {
  0% {
    opacity: 0.2;
  }
  50% {
    opacity: 1;
  }
  100% {
    opacity: 0.2;
  }
}
</style>
