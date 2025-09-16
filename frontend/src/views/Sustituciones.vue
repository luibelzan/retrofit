<template>

<h1 class="text-center font-extrabold">Equipos Pendientes de Sustitucion</h1>
  <!-- FILTRO -->
  <div class="container mt-3 border rounded">
    <div class="p-4 rounded">
      <div class="form-group mb-3">
        <label for="distribuidora">Distribuidora</label>
        <select id="distribuidora" class="form-select" v-model="formData.codDistribuidora" required>
          <option v-for="dist in distribuidoras" :key="dist.codDistribuidora" :value="dist.codDistribuidora">
            {{ dist.nomDistribuidora }}
          </option>
        </select>
      </div>

      <div class="form-group mb-3">
        <label>Fecha inicio</label>
        <input type="date" v-model="formData.fechaProceso" class="form-control" required>
      </div>

      <button class="btn btn-primary" @click="loadStatistics">
        Generar Excel
      </button>
    </div>
  </div>


  <!-- EQUIPOS PENDIENTES DE SUSTITUCION -->
  <div class="container mt-3 border rounded" v-if="mostrar">
    <div class="p-4 rounded">
      <div class="form-group mb-3">
        <h3 class="text-center">Equipos en garantia pendientes de sustitucion</h3>
        <div class="table-responsive">
          <table class="table table-bordered">
            <thead>
              <tr>
                <th>
                  Id Contador
                </th>
                <th>
                  Cod Modelo
                </th>
                <th>
                  Anio Fabricacion
                </th>
                <th>
                  Fecha Recepcion
                </th>
                <th>
                  Cod Almacen
                </th>
                <th>
                  Des Almacen
                </th>
                <th>
                  Des Diagnostico
                </th>
                <th>
                  Fecha Proceso
                </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="equipo in paginatedEquipos" :key="`${equipo.idContador}`">
                <td>{{ equipo.idContador }}</td>
                <td>{{ equipo.codModelo }}</td>
                <td>{{ equipo.anoFabricacion }}</td>
                <td>{{ equipo.fecRecepcion }}</td>
                <td>{{ equipo.codAlmacen }}</td>
                <td>{{ equipo.desAlmacen }}</td>
                <td>{{ equipo.desDiagnostico }}</td>
                <td>{{ equipo.fecProceso }}</td>
              </tr>
            </tbody>
          </table>
        </div>
        <!-- Paginación -->
        <div class="d-flex justify-content-center mt-3">
          <button class="btn btn-primary" :disabled="currentPage === 1"
            @click="changePage(currentPage - 1)">Anterior</button>
          <span class="mx-3">Página {{ currentPage }} de {{ totalPages }}</span>
          <button class="btn btn-primary" :disabled="currentPage === totalPages"
            @click="changePage(currentPage + 1)">Siguiente</button>
        </div>
      </div>
    </div>
  </div>


</template>

<script>

import axios from "axios";

export default {
  data() {
    return {
      distribuidoras: [],
      formData: {
                codDistribuidora: "",
                fechaProceso: "",
            },
      mostrar: false,
      currentPage: 1,
      pageSize: 10, // cantidad de filas por página
      equiposPendientesData: null,
    };
  },
  computed: {
    paginatedEquipos() {
      if (!this.equiposPendientesData) return [];
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.equiposPendientesData.slice(start, end);
    },
    totalPages() {
      if (!this.equiposPendientesData) return 1;
      return Math.ceil(this.equiposPendientesData.length / this.pageSize);
    },
  },
  async created() {
    //await this.loadAchatarrados();
    await this.fetchData();
  },
  methods: {
    async loadEquiposPendientesSustitucion() {
      try {
        const response = await axios.get("http://localhost:8080/api/estadisticas/equipos-garantia", {
          params: {
            codDistribuidora: this.formData.codDistribuidora,
            fechaProceso: this.formData.fechaProceso,
          },
        });
        const data = response.data;
        this.equiposPendientesData = data;
        this.mostrar = true;
      } catch(error) {
        console.error("Error al cargar los datos de los equipos pendientes de sustitucion: ", error);
      }
    },
    changePage(page) {
            if (page < 1 || page > this.totalPages) return;
            this.currentPage = page;
        },
    async fetchData() {
            try {
                const resDistribuidoras = await fetch("http://localhost:8080/api/achatarrado/distribuidoras");
                this.distribuidoras = await resDistribuidoras.json();
            } catch (error) {
                console.error("Error cargando datos:", error);
            }
        },
      async loadStatistics() {
        this.loadEquiposPendientesSustitucion();
      }
  },
};


</script>