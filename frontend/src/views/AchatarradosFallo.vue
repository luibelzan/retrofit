<template>

<h1 class="text-center font-extrabold">Achatarrados por Fallo</h1>
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
  components: { Bar },
  data() {
    return {
      distribuidoras: [],
      formData: {
                codDistribuidora: "",
                fechaProceso: "",
            },
      mostrar: false,
      achatarradosChart: null,
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
      }
  },
};


</script>