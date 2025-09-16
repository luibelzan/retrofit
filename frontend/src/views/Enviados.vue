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
            @click="changePageLotes(currentPageLotes - 1)">Anterior</button>
          <span class="mx-3">Página {{ currentPageLotes }} de {{ totalPagesLotes }}</span>
          <button class="btn btn-primary" :disabled="currentPageLotes === totalPagesLotes"
            @click="changePageLotes(currentPageLotes + 1)">Siguiente</button>
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
      currentPageLotes: 1,
      contadoresEnviadosPorLoteData: null,
    };
  },
  computed: {
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
  },
  async created() {
    //await this.loadAchatarrados();
    await this.fetchData();
  },
  methods: {
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
    changePageLotes(page) {
      if (page < 1 || page > this.totalPagesLotes) return;
      this.currentPageLotes = page;
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

