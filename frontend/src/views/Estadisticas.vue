<template>
  <h1 class="text-center font-extrabold">Estadisticas</h1>
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


  <!-- CONTADORES ACHATARRADOS -->
  <div class="container mt-3 border rounded" v-if="mostrar">
    <div class="p-4 rounded">
      <div class="form-group mb-3">
        <h3 class="text-center">Contadores Achatarrados por Fallo</h3>
        <div class="chart-layout">
          <!-- Gráfico -->
          <div class="chart-container">
            <Bar v-if="achatarradosChart" :data="achatarradosChart" :options="chartOptions" />
          </div>

          <!-- Leyenda personalizada -->
          <div class="legend-container" v-if="achatarradosData.length">
            <h5>Códigos y Descripciones</h5>
            <ul>
              <li v-for="(item, index) in achatarradosData" :key="index">
                <strong>{{ item.codDiagnostico }}</strong> - {{ item.desDiagnostico }}
              </li>
            </ul>
          </div>
        </div>
      </div>
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

  <!-- EQUIPOS PENDIENTES DE SUSTITUCION -->
  <div class="container mt-3 border rounded" v-if="mostrar">
    <div class="p-4 rounded">
      <div class="form-group mb-3">
        <h3 class="text-center">Contadores enviados por lotes</h3>
        <div class="table-responsive">
          <table class="table table-bordered">
            <thead>
              <tr>
                <th>
                  Id Lote
                </th>
                <th>
                  Nombre Lote
                </th>
                <th>
                  Des Almacen
                </th>
                <th>
                  Cod Almacen
                </th>
                <th>
                  Numero Equipos
                </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="lote in paginatedLotes" :key="`${lote.idLote}`">
                <td>{{ lote.idLote }}</td>
                <td>{{ lote.nomLote }}</td>
                <td>{{ lote.desAlmacen }}</td>
                <td>{{ lote.codAlmacen }}</td>
                <td>{{ lote.cantidad }}</td>
              </tr>
            </tbody>
          </table>
        </div>

        <!-- Paginación -->
        <div class="d-flex justify-content-center mt-3">
          <button class="btn btn-primary" :disabled="currentPageLotes === 1"
            @click="changePage(currentPageLotes - 1)">Anterior</button>
          <span class="mx-3">Página {{ currentPageLotes }} de {{ totalPagesLotes }}</span>
          <button class="btn btn-primary" :disabled="currentPageLotes === totalPagesLotes"
            @click="changePage(currentPageLotes + 1)">Siguiente</button>
        </div>

      </div>
    </div>
  </div>


</template>

<script>
import { Bar } from "vue-chartjs";
import axios from "axios";
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
} from "chart.js";

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale);

export default {
  name: "Estadisticas",
  components: { Bar },
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
      currentPageLotes: 1,
      equiposPendientesData: null,
      achatarradosChart: null,
      contadoresEnviadosPorLoteData: null,
      achatarradosData: [],
      chartOptions: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: false },
          tooltip: {
            callbacks: {
              label: function (context) {
                return `${context.parsed.y} contadores`;
              },
            },
          },
        },
        scales: {
          x: { title: { display: true, text: "Tipo de fallo" } },
          y: { title: { display: true, text: "Cantidad" }, beginAtZero: true },
        },
      },
    };
  },
  computed: {
    paginatedEquipos() {
      if (!this.equiposPendientesData) return [];
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.equiposPendientesData.slice(start, end);
    },
    paginatedLotes() {
      if (!this.contadoresEnviadosPorLoteData) return [];
      const start = (this.currentPageLotes - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.contadoresEnviadosPorLoteData.slice(start, end);
    },
    totalPagesLotes() {
      if (!this.contadoresEnviadosPorLoteData) return 1;
      return Math.ceil(this.contadoresEnviadosPorLoteData.length / this.pageSize);
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
    async loadAchatarrados() {
      try {
        const response = await axios.get("http://localhost:8080/api/estadisticas/achatarrados-fallo", {
          params: {
                        codDistribuidora: this.formData.codDistribuidora,
                        fechaProceso: this.formData.fechaProceso,
                    },
        });
        const data = response.data;
        this.achatarradosData = data; // guardamos para la leyenda
        this.achatarradosChart = {
          labels: data.map((item) => item.codDiagnostico),
          datasets: [
            {
              label: "Cantidad",
              data: data.map((item) => item.cantidad),
              backgroundColor: "#274469",
            },
          ],
        };
        this.mostrar = true;
      } catch (error) {
        console.error("Error al cargar datos de achatarrados: ", error);
      }
    },
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
    async loadContadoresEnviadosPorLote() {
      try {
        const response = await axios.get("http://localhost:8080/api/estadisticas/enviados-lote", {
          params: {
            codDistribuidora: this.formData.codDistribuidora,
            fechaProceso: this.formData.fechaProceso,
          },
        });
        const data = response.data;
        this.contadoresEnviadosPorLoteData = data;
        this.mostrar = true;
      } catch(error) {
        console.error("Error al cargar los datos de los contadores enviados por lotes: ", error);
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
        this.loadAchatarrados();
        this.loadEquiposPendientesSustitucion();
        this.loadContadoresEnviadosPorLote();
      }
  },
};
</script>

<style scoped>
.chart-layout {
  display: flex;
  gap: 20px;
  align-items: flex-start;
}

.chart-container {
  flex: 2;
  min-height: 400px;
}

.legend-container {
  flex: 1;
  background: #f9f9f9;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.1);
}

.legend-container h5 {
  margin-bottom: 10px;
  color: #274469;
}

.legend-container ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.legend-container li {
  margin-bottom: 6px;
  font-size: 15px;
}
</style>
