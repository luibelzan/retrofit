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
                <th>
                  Contador Sust
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

                <!-- Nuevo campo editable -->
                <td>
                  <input type="text" v-model="equipo.idContadorSust" class="form-control" />
                </td>

                <td>
                  <button class="btn btn-sm btn-success" 
                          @click="actualizarContadorSust(equipo)">
                    Actualizar
                  </button>
                </td>

              </tr>
            </tbody>
          </table>
        </div>

        <div class="d-flex justify-content-between align-items-center mt-3">
          <!-- Espacio vacío para "empujar" la paginación al centro -->
          <div style="flex: 1;"></div>

          <!-- Controles de paginación centrados -->
          <div class="d-flex justify-content-center" style="flex: 1;">
            <button class="btn btn-primary" :disabled="currentPage === 1"
              @click="changePage(currentPage - 1)">Anterior</button>
            <span class="mx-3">Página {{ currentPage }} de {{ totalPages }}</span>
            <button class="btn btn-primary" :disabled="currentPage === totalPages"
              @click="changePage(currentPage + 1)">Siguiente</button>
          </div>

          <!-- Botón exportar a la derecha -->
          <div style="flex: 1; display: flex; justify-content: flex-end;">
            <button class="btn btn-success" @click="exportarExcel">Exportar a Excel</button>
          </div>
        </div>

      </div>
    </div>
  </div>


</template>

<script>

import axios from "axios";
import * as XLSX from "xlsx";
import Swal from "sweetalert2";

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
      },

    exportarExcel() {
      if (!this.equiposPendientesData || this.equiposPendientesData.length === 0) {
        alert("No hay datos para exportar");
        return;
      }

      // Convertir los datos JSON a hoja de Excel
      const ws = XLSX.utils.json_to_sheet(this.equiposPendientesData);

      // Crear libro de Excel
      const wb = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, "Equipos Pendientes");

      // Guardar archivo
      XLSX.writeFile(wb, "pendientes_sustitucion.xlsx");
    },

    async actualizarContadorSust(equipo) {
      try {
        await axios.put(
          `http://localhost:8080/api/estadisticas/equipos-garantia/${equipo.idContador}/${equipo.codDistribuidora}/sustituto`,
          { idContadorSust: equipo.idContadorSust }
        );

        Swal.fire({
          icon: "success",
          title: "¡Actualizado!",
          text: "Sustituto actualizado correctamente",
          timer: 2000,
          showConfirmButton: false
        });

      } catch (error) {
        let mensaje = "Error inesperado al guardar sustituto";

        if (error.response && error.response.data && error.response.data.error) {
          mensaje = error.response.data.error;
        }

        Swal.fire({
          icon: "error",
          title: "Error",
          text: mensaje
        });
      }
    }


  },
};


</script>