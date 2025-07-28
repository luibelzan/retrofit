<template>
  <div class="container">
    <h2>Asignar Lote a Contadores</h2>

    <form @submit.prevent="asignarLote">
      <div class="mb-3">
        <label for="idLote" class="form-label">Seleccionar Lote:</label>
        <select v-model="idLote" class="form-select" required>
          <option value="">Seleccione un Lote</option>
          <option v-for="lote in lotes" :key="lote.idLote" :value="lote.idLote">
            {{ lote.idLote }}
          </option>
        </select>
      </div>

      <div class="mb-3">
        <label for="codDistribuidora" class="form-label">Distribuidora</label>
        <select v-model="codDistribuidora" class="form-select" required>
          <option disabled value="">Seleccione una opción</option>
          <option value="777">Iberdrola I-DE</option>
          <option value="888">SAGEMCOM</option>
          <option value="999">LANDIS&GYR</option>
          <option value="666">KAIFA</option>
        </select>
      </div>

      <div class="mb-3">
        <label for="idContador" class="form-label">ID Contador</label>
        <input v-model="idContador" type="text" class="form-control" id="idContador" />
        <button type="button" class="btn btn-primary mt-2" @click="validarContador">Validar Contador</button>
      </div>

      <ul class="list-group mb-3">
        <li v-for="(contador, index) in contadores" :key="index" class="list-group-item d-flex justify-content-between">
          {{ contador }}
          <button type="button" class="btn btn-danger btn-sm" @click="eliminarContador(index)">Eliminar</button>
        </li>
      </ul>

      <button type="submit" class="btn btn-success">Asignar Lote</button>
    </form>
  </div>
</template>

<script>
import axios from "axios";
import Swal from "sweetalert2";

export default {
  data() {
    return {
      codDistribuidora: "",
      idLote: "",
      idContador: "",
      lotes: [],
      contadores: [],
      mensaje: "",
      error: ""
    };
  },
  mounted() {
    this.cargarLotes();
  },
  methods: {
    async cargarLotes() {
      try {
        const response = await axios.get("http://localhost:8080/api/lotes");
        this.lotes = response.data;
      } catch (error) {
        this.error = "Error al cargar lotes";
      }
    },
    async validarContador() {
      try {
        if (!this.idContador) {
          Swal.fire("Error", "Ingrese el ID del contador", "error");
          return;
        }

        const response = await axios.post("http://localhost:8080/api/lotes/validarContador", null, {
          params: {
            idContador: this.idContador
          }
        });

        this.contadores.push(this.idContador);
        this.idContador = "";
        Swal.fire("Éxito", "Contador válido y agregado a la lista", "success");
      } catch (error) {
        Swal.fire("Error", String(error.response?.data || "Error desconocido"), "error");
      }
    },


    async asignarLote() {
      if (!this.codDistribuidora || !this.idLote || this.contadores.length === 0) {
        this.error = "Complete todos los campos antes de asignar el lote";
        return;
      }
      try {
        const formData = new URLSearchParams();
        formData.append("idLote", this.idLote);
        formData.append("idContadores", this.contadores.join(","));
        formData.append("codDistribuidora", this.codDistribuidora);

        const response = await axios.post("http://localhost:8080/api/lotes/asignar", formData);

        // Mostrar SweetAlert2 en caso de éxito
        Swal.fire({
          icon: "success",
          title: "¡Asignación completada!",
          text: response.data,
          confirmButtonColor: "#274469"
        });

        // Reiniciar datos después de asignación
        this.mensaje = response.data;
        this.error = "";
        this.contadores = [];
        this.idLote = "";
        this.codDistribuidora = "";
      } catch (error) {
        this.error = "Error al asignar lote: " + (error.response?.data || "Error desconocido");
      }
    },


    eliminarContador(contador) {
      this.contadores = this.contadores.filter(c => c !== contador);
    },

  },
};
</script>

<style scoped>
.container {
  max-width: 600px;
  margin-top: 2rem;
}
</style>


<style scoped>
.container {
  max-width: 1800px;
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  font-family: "Quicksand", sans-serif;
}

.table th,
.table td {
  vertical-align: middle;
}

.modal-header {
  background-color: #f8f9fa;
}

.modal-title {
  color: #33598a;
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

.btn-info {
  border-radius: 25px;
  background-color: #969696;
  border-color: #969696;
}

.btn-info:hover {
  background-color: #d3d3d3;
  border-color: #d3d3d3;
}

.btn-warning {
  border-radius: 25px;
  background-color: #335888;
  border-color: #335888;
}

.btn-warning:hover {
  background-color: #4a75ad;
  border-color: #4a75ad;
}

.btn-danger {
  border-radius: 25px;
  background-color: #f14848;
  border-color: #f14848;
}

.btn-danger:hover {
  background-color: #ff7979;
  border-color: #ff7979;
}
</style>