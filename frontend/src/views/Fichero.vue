<template>
  <div class="container mt-4">
    <h1 class="text-center mb-3">Gestión de Ficheros</h1>
    <!-- Botones para crear y actualizar lista 
    <button class="btn btn-primary mb-3 me-2" @click="openCreateModal">Crear Fichero</button>
    -->
    <button class="btn btn-info mb-3" @click="fetchTFicheros">Actualizar lista</button>

    <!-- Tabla de Ficheros -->
    <table class="table table-bordered">
      <thead>
        <tr>
          <th>Distribuidora
            <input type="text" class="form-control mt-1" v-model="filters.tipoDistribuidora" placeholder="Filtrar...">
          </th>
          <th>Fecha Carga
            <input type="text" class="form-control mt-1" v-model="filters.fechaCarga" placeholder="Filtrar...">
          </th>
          <th>Nombre Fichero
            <input type="text" class="form-control mt-1" v-model="filters.nombreFichero" placeholder="Filtrar...">
          </th>
          <th>Tamaño (Kb)
            <input type="text" class="form-control mt-1" v-model="filters.tamanio" placeholder="Filtrar...">
          </th>
          <th>Registros
            <input type="text" class="form-control mt-1" v-model="filters.registro" placeholder="Filtrar...">
          </th>
          <th>Cargados
            <input type="text" class="form-control mt-1" v-model="filters.cargado" placeholder="Filtrar...">
          </th>
          <th>Invalidos
            <input type="text" class="form-control mt-1" v-model="filters.invalido" placeholder="Filtrar...">
          </th>
          <th>Usuario
            <input type="text" class="form-control mt-1" v-model="filters.usuario" placeholder="Filtrar...">
          </th>
          <th>Tipo fichero
            <input type="text" class="form-control mt-1" v-model="filters.tipoFichero" placeholder="Filtrar...">
          </th>
          <th>Acciones</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="fichero in paginatedFicheros" :key="fichero.codDistribuidora + fichero.fecCarga">
          <td>{{ fichero.codDistribuidora }}</td>
          <td>{{ formatDate(fichero.fecCarga) }}</td>
          <td>{{ fichero.nomFichero }}</td>
          <td>{{ fichero.tamFichero }}</td>
          <td>{{ fichero.numRegistros }}</td>
          <td>{{ fichero.numCargados }}</td>
          <td>{{ fichero.numInvalidos }}</td>
          <td>{{ fichero.codUsuario }}</td>
          <td>{{ fichero.tipFicheros }}</td>
          <td>
            <div class="btn-group" role="group" aria-label="Basic mixed styles example">
              <button type="button" class="btn btn-info btn-sm" @click="openViewModal(fichero)">Ver</button>
              <button type="button" class="btn btn-warning btn-sm" @click="openEditModal(fichero)">Editar</button>
              <button type="button" class="btn btn-danger btn-sm" @click="deleteTFichero(fichero)">Eliminar</button>
            </div>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Paginación -->
    <div class="d-flex justify-content-center mt-3">
      <button class="btn btn-primary" :disabled="currentPage === 1"
        @click="changePage(currentPage - 1)">Anterior</button>
      <span class="mx-3">Página {{ currentPage }} de {{ totalPages }}</span>
      <button class="btn btn-primary" :disabled="currentPage === totalPages"
        @click="changePage(currentPage + 1)">Siguiente</button>
    </div>

    <!-- Modal Crear Fichero 
    <div class="modal fade" id="createModal" tabindex="-1" aria-labelledby="createModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="createModalLabel">Crear Fichero</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="createTFichero">
              <div class="mb-3">
                <label for="codDistribuidora" class="form-label">Código Distribuidora</label>
                <input type="text" class="form-control" id="codDistribuidora" v-model="newFichero.codDistribuidora" required>
              </div>
              <div class="mb-3">
                <label for="fecCarga" class="form-label">Fecha de Carga</label>
                <input type="date" class="form-control" id="fecCarga" v-model="newFichero.fecCarga" required>
              </div>
              <div class="mb-3">
                <label for="nomFichero" class="form-label">Nombre Fichero</label>
                <input type="text" class="form-control" id="nomFichero" v-model="newFichero.nomFichero" required>
              </div>
              <div class="mb-3">
                <label for="tamFichero" class="form-label">Tamaño Fichero (KB)</label>
                <input type="number" class="form-control" id="tamFichero" v-model="newFichero.tamFichero" required>
              </div>
              <div class="mb-3">
                <label for="numRegistros" class="form-label">Número de Registros</label>
                <input type="number" class="form-control" id="numRegistros" v-model="newFichero.numRegistros" required>
              </div>
              <div class="mb-3">
                <label for="numCargados" class="form-label">Número de Cargados</label>
                <input type="number" class="form-control" id="numCargados" v-model="newFichero.numCargados" required>
              </div>
              <div class="mb-3">
                <label for="numInvalidos" class="form-label">Número de Invalidos</label>
                <input type="number" class="form-control" id="numInvalidos" v-model="newFichero.numInvalidos" required>
              </div>
              <div class="mb-3">
                <label for="codUsuario" class="form-label">Código Usuario</label>
                <input type="text" class="form-control" id="codUsuario" v-model="newFichero.codUsuario" required>
              </div>
              <div class="mb-3">
                <label for="tipFichero" class="form-label">Tipo Fichero</label>
                <input type="text" class="form-control" id="tipFichero" v-model="newFichero.tipFichero" required>
              </div>
              <button type="submit" class="btn btn-primary">Crear</button>
            </form>
          </div>
        </div>
      </div>
    </div>
    -->

    <!-- Modal Editar Fichero -->
    <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="editModalLabel">Editar Fichero</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <form @submit.prevent="updateTFichero">
              <div class="mb-3">
                <label for="editCodDistribuidora" class="form-label">Código Distribuidora</label>
                <input type="text" class="form-control" id="editCodDistribuidora"
                  v-model="currentFichero.codDistribuidora" required>
              </div>
              <div class="mb-3">
                <label for="editFecCarga" class="form-label">Fecha de Carga</label>
                <input type="date" class="form-control" id="editFecCarga"
                  v-model="currentFichero.fecCarga.split('T')[0]" required>
              </div>
              <div class="mb-3">
                <label for="editNomFichero" class="form-label">Nombre Fichero</label>
                <input type="text" class="form-control" id="editNomFichero" v-model="currentFichero.nomFichero"
                  required>
              </div>
              <div class="mb-3">
                <label for="editTamFichero" class="form-label">Tamaño Fichero (KB)</label>
                <input type="number" class="form-control" id="editTamFichero" v-model="currentFichero.tamFichero"
                  required>
              </div>
              <div class="mb-3">
                <label for="editNumRegistros" class="form-label">Número de Registros</label>
                <input type="number" class="form-control" id="editNumRegistros" v-model="currentFichero.numRegistros"
                  required>
              </div>
              <div class="mb-3">
                <label for="editNumCargados" class="form-label">Número de Cargados</label>
                <input type="number" class="form-control" id="editNumCargados" v-model="currentFichero.numCargados"
                  required>
              </div>
              <div class="mb-3">
                <label for="editNumInvalidos" class="form-label">Número de Invalidos</label>
                <input type="number" class="form-control" id="editNumInvalidos" v-model="currentFichero.numInvalidos"
                  required>
              </div>
              <div class="mb-3">
                <label for="editCodUsuario" class="form-label">Código Usuario</label>
                <input type="text" class="form-control" id="editCodUsuario" v-model="currentFichero.codUsuario"
                  required>
              </div>
              <div class="mb-3">
                <label for="editTipFichero" class="form-label">Tipo Fichero</label>
                <input type="text" class="form-control" id="editTipFichero" v-model="currentFichero.tipFicheros"
                  required>
              </div>
              <button type="submit" class="btn btn-primary">Actualizar</button>
            </form>
          </div>
        </div>
      </div>
    </div>


    <!-- Modal Ver Fichero -->
    <div class="modal fade" id="viewModal" tabindex="-1" aria-labelledby="viewModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="viewModalLabel">Detalles del Fichero</h5>
            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <p><strong>Código Distribuidora:</strong> {{ currentFichero.codDistribuidora }}</p>
            <p><strong>Fecha Carga:</strong> {{ currentFichero.fecCarga }}</p>
            <p><strong>Nombre Fichero:</strong> {{ currentFichero.nomFichero }}</p>
            <p><strong>Tamaño Fichero (KB):</strong> {{ currentFichero.tamFichero }}</p>
            <p><strong>Número de Registros:</strong> {{ currentFichero.numRegistros }}</p>
            <p><strong>Número de Cargados:</strong> {{ currentFichero.numCargados }}</p>
            <p><strong>Número de Invalidos:</strong> {{ currentFichero.numInvalidos }}</p>
            <p><strong>Código Usuario:</strong> {{ currentFichero.codUsuario }}</p>
            <p><strong>Tipo Fichero:</strong> {{ currentFichero.tipFicheros }}</p>
          </div>
        </div>
      </div>
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
      ficheros: [],
      currentPage: 1,
      pageSize: 10, 
      totalPages: 1, 
      newFichero: {
        codDistribuidora: '',
        fecCarga: '',
        nomFichero: '',
        tamFichero: null,
        numRegistros: null,
        numCargados: null,
        numInvalidos: null,
        codUsuario: null,
        tipFicheros: ''
      },
      currentFichero: {
        codDistribuidora: '',
        fecCarga: '',
        nomFichero: '',
        tamFichero: null,
        numRegistros: null,
        numCargados: null,
        numInvalidos: null,
        codUsuario: null,
        tipFicheros: ''
      },
      filters: {
        tipoDistribuidora: '',
        fechaCarga: '',
        nombreFichero: '',
        tamanio: '',
        registro: '',
        cargado: '',
        invalido: '',
        usuario: '',
        tipoFichero: '',
      },
    };
  },
  computed: {
    filteredFicheros() {
      return this.ficheros.filter((fichero) => {
        return (
          fichero.codDistribuidora.toLowerCase().includes(this.filters.tipoDistribuidora.toLowerCase()) &&
          fichero.fecCarga.toLowerCase().includes(this.filters.fechaCarga.toLowerCase()) &&
          fichero.nomFichero.toLowerCase().includes(this.filters.nombreFichero.toLowerCase()) &&
          fichero.tamFichero.toString().includes(this.filters.tamanio) &&
          fichero.numRegistros.toString().includes(this.filters.registro) &&
          fichero.numCargados.toString().includes(this.filters.cargado) &&
          fichero.numInvalidos.toString().includes(this.filters.invalido) &&
          fichero.codUsuario.toString().includes(this.filters.usuario) &&
          fichero.tipFicheros.toLowerCase().includes(this.filters.tipoFichero.toLowerCase())
        );
      });
    },

    paginatedFicheros() {
      const start = (this.currentPage - 1) * this.pageSize;
      const end = start + this.pageSize;
      return this.filteredFicheros.slice(start, end);
    },
  },
  methods: {
    fetchTFicheros() {
      axios.get('http://localhost:8080/api/t_ficheros')
        .then(response => {
          this.currentPage = 1;
          this.ficheros = response.data;
          this.totalPages = Math.ceil(this.filteredFicheros.length / this.pageSize);
        })
        .catch(error => {
          console.error('Error al obtener ficheros:', error);
        });
    },
    changePage(page) {
      if (page < 1 || page > this.totalPages) return;
      this.currentPage = page;
    },
    openCreateModal() {
      this.newFichero = {
        codDistribuidora: '',
        fecCarga: '',
        nomFichero: '',
        tamFichero: null,
        numRegistros: null,
        numCargados: null,
        numInvalidos: null,
        codUsuario: null,
        tipFicheros: ''
      };
      const modal = new Modal(document.getElementById('createModal'));
      modal.show();
    },
    createTFichero() {
      if (!this.newFichero.codDistribuidora || !this.newFichero.fecCarga || !this.newFichero.nomFichero) {
        alert('Por favor, complete todos los campos obligatorios.');
        return;
      }
      axios.post('http://localhost:8080/api/t_ficheros', this.newFichero)
        .then(response => {
          this.fetchTFicheros();
          const modal = new Modal(document.getElementById('createModal'));
          modal.hide();
          alert('Fichero creado exitosamente.');
        })
        .catch(error => {
          console.error('Error al crear fichero:', error);
          alert('Error al crear fichero.');
        });
    },
    openEditModal(fichero) {
      this.currentFichero = { ...fichero };
      const modal = new Modal(document.getElementById('editModal'));
      modal.show();
    },
    updateTFichero() {
      if (!this.currentFichero.fecCarga || !this.currentFichero.nomFichero) {
        alert('Por favor, complete todos los campos obligatorios.');
        return;
      }
      axios.put(`http://localhost:8080/api/t_ficheros/${this.currentFichero.codDistribuidora}/${this.currentFichero.fecCarga}`, this.currentFichero)
        .then(response => {
          this.fetchTFicheros();
          const modal = new Modal(document.getElementById('editModal'));
          modal.hide();
          alert('Fichero actualizado exitosamente.');
        })
        .catch(error => {
          console.error('Error al actualizar fichero:', error);
          alert('Error al actualizar fichero.');
        });
    },
    deleteTFichero(fichero) {
      const confirmDelete = confirm(`¿Seguro que deseas eliminar el fichero ${fichero.nomFichero}?`);
      if (confirmDelete) {
        axios.delete(`http://localhost:8080/api/t_ficheros/${fichero.codDistribuidora}/${fichero.fecCarga}`)
          .then(response => {
            this.fetchTFicheros();
            alert('Fichero eliminado exitosamente.');
          })
          .catch(error => {
            console.error('Error al eliminar fichero:', error);
            alert('Error al eliminar fichero.');
          });
      }
    },
    openViewModal(fichero) {
      this.currentFichero = { ...fichero };
      const modal = new Modal(document.getElementById('viewModal'));
      modal.show();
    },
    formatDate(dateString) {
      const date = new Date(dateString);
      return date.getFullYear() + '-' + String(date.getMonth() + 1).padStart(2, '0') + '-' + String(date.getDate()).padStart(2, '0');
    }
  },
  mounted() {
    this.fetchTFicheros();
  }
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

.table th,
.table td {
  vertical-align: middle;
  text-align: center;
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