<template>
  <div class="container mt-3 border">

    <h1 class="text-center mb-1 mt-3">Proceso de Achatarrados</h1>

    <!-- Contador de contadores reparados -->
    <div v-if="contadores.length > 0" class="contador-recepcionado text-center mt-4">
      <h3 class="contador-numero">Contadores Achatarrados: {{ contadores.length }}</h3>
      <p class="ultimo-codigo"><strong>Último Contador Añadido:</strong> {{ contadores[contadores.length - 1].idContador
        }}</p>
    </div>

    <!-- Formulario principal -->
    <div class="p-4 rounded">
      <div class="form-group mb-3">
        <label for="distribuidora">Distribuidora</label>
        <select id="distribuidora" class="form-select" v-model="codDistribuidora" required>
          <option v-for="dist in distribuidoras" :key="dist.codDistribuidora" :value="dist.codDistribuidora">
            {{ dist.nomDistribuidora }}
          </option>
        </select>
      </div>

      <div class="form-group mb-3">
        <label for="diagnostico">Diagnóstico</label>
        <select id="diagnostico" class="form-select" v-model="codDiagnostico" required>
          <option v-for="diag in diagnosticos" :key="diag.codDiagnostico" :value="diag.codDiagnostico">
            {{ diag.desDiagnostico }}
          </option>
        </select>
      </div>

      <div class="form-group mb-3">
        <label for="idContador">ID Contador</label>
        <input type="text" id="idContador" class="form-control" v-model="idContador" required
          @keyup.enter="agregarContador" pattern="^[a-zA-Z0-9]{18}$" maxlength="18"
          title="Debe contener exactamente 18 caracteres alfanuméricos (letras y números, sin espacios ni caracteres especiales)" />
      </div>


      <button class="btn btn-primary" @click="agregarContador">
        Añadir
      </button>
    </div>

    <!-- Lista de contadores -->
    <div class="mt-4">
      <div v-for="(contador, index) in contadores" :key="contador.idContador"
        class="border-bottom border-top py-2 d-flex justify-content-between align-items-center">

        <div class="registro-info d-flex align-items-center">
          <p class="mx-2">-</p>
          <p><strong>{{ contador.idContador }}</strong></p>
          <p class="mx-2">/</p>
          <p><strong>{{ contador.codDiagnostico }}</strong></p>
        </div>

        <div>
          <button class="btn btn-danger" @click="eliminarContador(index)">Eliminar</button>
        </div>
      </div>

      <button v-if="contadores.length > 0" class="btn btn-success mt-3 mb-3" @click="enviarContadores">
        Enviar Contadores
      </button>
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
      distribuidoras: [],
      diagnosticos: [],
      codDistribuidora: "",
      codDiagnostico: "",
      idContador: "",
      contadores: [],
    };
  },
  methods: {
    async fetchData() {
      try {
        const resDistribuidoras = await fetch("http://localhost:8080/api/achatarrado/distribuidoras");
        this.distribuidoras = await resDistribuidoras.json();

        const resDiagnosticos = await fetch("http://localhost:8080/api/achatarrado/diagnosticosCH");
        this.diagnosticos = await resDiagnosticos.json();
      } catch (error) {
        console.error("Error cargando datos:", error);
      }
    },
    async validarContador() {
      const url = `http://localhost:8080/api/achatarrado/validar/${this.idContador}?codDistribuidora=${this.codDistribuidora}`;
      const response = await fetch(url);
      const result = await response.json();

      if (!response.ok) {
        throw new Error(result.mensaje || "Error desconocido");
      }
      return result;
    },
    async agregarContador() {
      if (this.idContador && this.codDistribuidora && this.codDiagnostico) {
        try {
          this.contadores.push({
            idContador: this.idContador,
            codDistribuidora: this.codDistribuidora,
            codDiagnostico: this.codDiagnostico,
          });
          this.idContador = "";

          Swal.fire({
            icon: "success",
            title: "Contador Añadido",
            text: "El contador se agregó correctamente.",
            timer: 1000,
            showConfirmButton: false,
          });
        } catch (error) {
          Swal.fire({
            icon: "error",
            title: "Error",
            text: error.message,
          });
        }
      }
    },
    eliminarContador(index) {
      this.contadores.splice(index, 1);
    },
    async enviarContadores() {
      try {
        const response = await fetch("http://localhost:8080/api/achatarrado/enviar", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            ids: this.contadores.map((c) => ({
              idContador: c.idContador,
              codDistribuidora: c.codDistribuidora,
            })),
            codDiagnostico: this.codDiagnostico,
          }),
        });

        if (!response.ok) throw new Error("Error al enviar contadores");
        Swal.fire({
          icon: "success",
          title: "Contadores Añadidos",
          text: "Añadido correctamente los contadores achatarrados.",
        });
        this.contadores = [];
      } catch (error) {
        Swal.fire({
          icon: "error",
          title: "Error",
          text: error.message,
        });
      }
    },
  },
  mounted() {
    this.fetchData();
  },
};
</script>


<style scoped>
.container {
  max-width: 1400px;
  background-color: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  font-family: "Quicksand", sans-serif;
}

h2,
h3 {
  text-align: center;
}

.registro-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.col-md-4 {
  display: flex;
  align-items: center;
}

.me-2 {
  margin-right: 0.5rem;
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

.btn-danger {
  border-radius: 25px;
  background-color: #335888;
  border-color: #335888;
}

.btn-danger:hover {
  background-color: #4a75ad;
  border-color: #4a75ad;
}

.contador-recepcionado {
  font-size: 1.2rem;
  background-color: #e9ecef;
  border-radius: 8px;
}

.contador-numero {
  font-size: 2.5rem;
  font-weight: bold;
  color: #274469;
}

.ultimo-codigo {
  font-size: 1.1rem;
  color: #335888;
}

.estado-warning {
  background-color: #ffeaac;
  color: #000000;
}

.estado-success {
  background-color: #c2ffd0;
  color: #000000;
}

.estado-danger {
  background-color: #ffc0c6;
  color: #000000;
}
</style>