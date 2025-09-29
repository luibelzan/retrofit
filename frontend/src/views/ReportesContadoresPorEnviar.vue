<template>
    <div class="container mt-3 border">
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
                <label>Fecha de Recepcion</label>
                <input type="date" v-model="formData.fecha" class="form-control" required>
            </div>

            <button class="btn btn-primary" @click="loadContadoresPorEnviar">
                Enviar
            </button>
        </div>
    </div>

    <!-- REPORTE CONTADORES POR ENVIAR -->
  <div class="container mt-3 border rounded" v-if="mostrar">
    <div class="p-4 rounded">
      <div class="form-group mb-3">
        <h3 class="text-center">Contadores Por Enviar En Garantia</h3>
        <div class="table-responsive">
          <table class="table table-bordered">
            <thead>
              <tr>
                <th>
                  Cod Distribuidora
                  <input type="text" class="form-control mt-1" v-model="filters.codDistribuidora"
                            placeholder="Filtrar...">
                </th>
                <th>
                  Cod Almacen
                  <input type="text" class="form-control mt-1" v-model="filters.codAlmacen"
                            placeholder="Filtrar...">
                </th>
                <th>
                  Des Almacen
                  <input type="text" class="form-control mt-1" v-model="filters.desAlmacen"
                            placeholder="Filtrar...">
                </th>
                <th>
                  Contadores Enviados
                  <input type="text" class="form-control mt-1" v-model="filters.contadoresEnviados"
                            placeholder="Filtrar...">
                </th>
                <th>
                  Contadores Recepcionados
                  <input type="text" class="form-control mt-1" v-model="filters.contadoresRecepcionados"
                            placeholder="Filtrar...">
                </th>
                <th>
                  Contadores Recuperados
                  <input type="text" class="form-control mt-1" v-model="filters.contadoresRecuperados"
                            placeholder="Filtrar...">
                </th>
                <th>
                  Contadores Sustituibles
                  <input type="text" class="form-control mt-1" v-model="filters.contadoresSustituiblesGarantia"
                            placeholder="Filtrar...">
                </th>
                <th>
                  Contadores Sin Garantia
                  <input type="text" class="form-control mt-1" v-model="filters.contadoresSinGarantia"
                            placeholder="Filtrar...">
                </th>
                <th>
                  Total Equipos
                  <input type="text" class="form-control mt-1" v-model="filters.totalEquipos"
                            placeholder="Filtrar...">
                </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="contador in paginatedContadores" :key="`${contador.codAlmacen}`">
                <td>{{ contador.codDistribuidora }}</td>
                <td>{{ contador.codAlmacen }}</td>
                <td>{{ contador.desAlmacen }}</td>
                <td>{{ contador.contadoresEnviados }}</td>
                <td>{{ contador.contadoresRecepcion }}</td>
                <td>{{ contador.contadoresRecuperados }}</td>
                <td>{{ contador.contadoresSustituiblesGarantia }}</td>
                <td>{{ contador.contadoresSinGarantia }}</td>
                <td>{{ contador.totalEquiposADevolver }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Paginación -->
        <div class="d-flex justify-content-between align-items-center mt-3">
          <!-- Espacio vacío para "empujar" la paginación al centro -->
          <div style="flex: 1;"></div>
          <div class="d-flex justify-content-center mt-3">
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

export default {
    data() {
        return {
            distribuidoras: [],
            formData: {
                codDistribuidora: "",
                fecha: "",
            },
            mostrar: false,
            filters: {
              codDistribuidora: "",
              codAlmacen: "",
              desAlmacen: "",
              contadoresEnviados: "",
              contadoresRecepcionados: "",
              contadoresRecuperados: "",
              contadoresSustituiblesGarantia: "",
              contadoresSinGarantia: "",
              totalEquipos: "",
            },
            currentPage: 1,
            pageSize: 10,
            contadoresPorEnviarData: null,
        };
    },
    computed: {
        paginatedContadores() {
            if (!this.contadoresPorEnviarData) return [];
            const start = (this.currentPage - 1) * this.pageSize;
            const end = start + this.pageSize;
            return this.filteredContadores.slice(start, end);
        },
        totalPages() {
            if (!this.contadoresPorEnviarData) return 1;
            return Math.ceil(this.filteredContadores.length / this.pageSize);
        },
        filteredContadores() {
            return this.contadoresPorEnviarData.filter(envio => {
                return Object.keys(this.filters).every(key => {
                const filterValue = this.filters[key];
                const processValue = envio[key];
                if(!filterValue) {
                    return true;
                }
                if(processValue == null) {
                    return false;
                }
                return filterValue.toLowerCase().includes(processValue.toString().toLowerCase()) ||
                    processValue.toString().toLowerCase().includes(filterValue.toLowerCase());
                });
            });
        },
    },
    methods: {
        async fetchData() {
            try {
                const resDistribuidoras = await fetch("http://localhost:8080/api/achatarrado/distribuidoras");
                this.distribuidoras = await resDistribuidoras.json();
            } catch (error) {
                console.error("Error cargando datos:", error);
            }
        },

        async loadContadoresPorEnviar() {
          try {
            const response = await axios.get("http://localhost:8080/api/reportes/contadores-por-enviar", {
              params: {
                codDistribuidora: this.formData.codDistribuidora,
                fecha: this.formData.fecha,
              },
            });
            const data = response.data;
            this.contadoresPorEnviarData = data;
            this.mostrar = true;
          } catch(error) {
            console.error("Error al cargar los datos: ", error);
          }
        },

        changePage(page) {
            if (page < 1 || page > this.totalPages) return;
            this.currentPage = page;
        },

        exportarExcel() {
          if (!this.contadoresPorEnviarData || this.contadoresPorEnviarData.length === 0) {
            alert("No hay datos para exportar");
            return;
          }

          // Convertir los datos JSON a hoja de Excel
          const ws = XLSX.utils.json_to_sheet(this.contadoresPorEnviarData);

          // Crear libro de Excel
          const wb = XLSX.utils.book_new();
          XLSX.utils.book_append_sheet(wb, ws, "Contadores Por Enviar");

          // Guardar archivo
          XLSX.writeFile(wb, "contadores_por_enviar.xlsx");
        }
        
    },
    mounted() {
        this.fetchData();
    }
}

</script>