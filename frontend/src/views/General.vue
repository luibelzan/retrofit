<template>
    <div class="container mt-3">
        <h1 class="text-center mb-3">Gestión de Generales</h1>

        <button class="btn btn-info mb-3" @click="fetchGenerales()">Actualizar lista</button>

        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>
                        Código Distribuidora
                        <input type="text" class="form-control mt-1" v-model="filters.codDistribuidora"
                            placeholder="Filtrar...">
                    </th>
                    <th>
                        ID Contador
                        <input type="text" class="form-control mt-1" v-model="filters.idContador"
                            placeholder="Filtrar...">
                    </th>
                    <th>
                        Tipo Equipo
                        <input type="text" class="form-control mt-1" v-model="filters.tipEquipo"
                            placeholder="Filtrar...">
                    </th>
                    <th>
                        Fecha Envío
                        <input type="text" class="form-control mt-1" v-model="filters.fecEnvio"
                            placeholder="Filtrar...">
                    </th>
                    <th>
                        Fecha Carga
                        <input type="text" class="form-control mt-1" v-model="filters.fecCarga"
                            placeholder="Filtrar...">
                    </th>
                    <th>
                        Fecha Avería
                        <input type="text" class="form-control mt-1" v-model="filters.fecAveria"
                            placeholder="Filtrar...">
                    </th>
                    <th>
                        Descripción Avería
                        <input type="text" class="form-control mt-1" v-model="filters.desAveria"
                            placeholder="Filtrar...">
                    </th>
                    <th>
                        Descripción Observaciones
                        <input type="text" class="form-control mt-1" v-model="filters.desObservaciones"
                            placeholder="Filtrar...">
                    </th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="general in paginatedGeneral" :key="general.codDistribuidora + general.idContador">
                    <td>{{ general.codDistribuidora }}</td>
                    <td>{{ general.idContador }}</td>
                    <td>{{ general.tipEquipo }}</td>
                    <td>{{ formatDate(general.fecEnvio) }}</td>
                    <td>{{ formatDate(general.fecCarga) }}</td>
                    <td>{{ formatDate(general.fecAveria) }}</td>
                    <td>{{ general.desAveria }}</td>
                    <td>{{ general.desObservaciones }}</td>
                    <td>
                        <div class="btn-group" role="group" aria-label="Basic mixed styles example">
                            <button type="button" class="btn btn-info btn-sm" @click="viewGeneral(general)">Ver</button>
                            <button type="button" class="btn btn-warning btn-sm"
                                @click="openEditModal(general)">Editar</button>
                            <button type="button" class="btn btn-danger btn-sm"
                                @click="deleteGeneral(general)">Eliminar</button>
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

        <!-- Modal Ver General -->
        <div class="modal fade" id="viewModal" tabindex="-1" aria-labelledby="viewModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="viewModalLabel">Detalles del General</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <p><strong>Código Distribuidora:</strong> {{ currentGeneral.codDistribuidora }}</p>
                        <p><strong>ID Contador:</strong> {{ currentGeneral.idContador }}</p>
                        <p><strong>Tipo Equipo:</strong> {{ currentGeneral.tipEquipo }}</p>
                        <p><strong>Fecha Envío:</strong> {{ formatDate(currentGeneral.fecEnvio) }}</p>
                        <p><strong>Fecha Carga:</strong> {{ formatDate(currentGeneral.fecCarga) }}</p>
                        <p><strong>Fecha Avería:</strong> {{ formatDate(currentGeneral.fecAveria) }}</p>
                        <p><strong>Descripción Avería:</strong> {{ currentGeneral.desAveria }}</p>
                        <p><strong>Descripción Observaciones:</strong> {{ currentGeneral.desObservaciones }}</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Editar General -->
        <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editModalLabel">Editar General</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="updateGeneral">
                            <div class="mb-3">
                                <label for="editCodDistribuidora" class="form-label">Código Distribuidora</label>
                                <input type="text" id="editCodDistribuidora" v-model="currentGeneral.codDistribuidora"
                                    class="form-control" readonly>
                            </div>
                            <div class="mb-3">
                                <label for="editIdContador" class="form-label">ID Contador</label>
                                <input type="text" id="editIdContador" v-model="currentGeneral.idContador"
                                    class="form-control" readonly>
                            </div>
                            <div class="mb-3">
                                <label for="editTipEquipo" class="form-label">Tipo Equipo</label>
                                <input type="text" id="editTipEquipo" v-model="currentGeneral.tipEquipo"
                                    class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="editFecEnvio" class="form-label">Fecha Envío</label>
                                <input type="date" id="editFecEnvio" v-model="currentGeneral.fecEnvio"
                                    class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="editFecCarga" class="form-label">Fecha Carga</label>
                                <input type="date" id="editFecCarga" v-model="currentGeneral.fecCarga"
                                    class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="editFecAveria" class="form-label">Fecha Avería</label>
                                <input type="date" id="editFecAveria" v-model="currentGeneral.fecAveria"
                                    class="form-control">
                            </div>
                            <div class="mb-3">
                                <label for="editDesAveria" class="form-label">Descripción Avería</label>
                                <textarea id="editDesAveria" v-model="currentGeneral.desAveria" class="form-control"
                                    rows="2"></textarea>
                            </div>
                            <div class="mb-3">
                                <label for="editDesObservaciones" class="form-label">Descripción Observaciones</label>
                                <textarea id="editDesObservaciones" v-model="currentGeneral.desObservaciones"
                                    class="form-control" rows="3"></textarea>
                            </div>
                            <button type="submit" class="btn btn-primary">Actualizar</button>
                        </form>
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
            generales: [],
            currentPage: 1,
            pageSize: 10, 
            totalPages: 1, 
            currentGeneral: {
                codDistribuidora: '',
                idContador: '',
                tipEquipo: '',
                fecEnvio: '',
                fecCarga: '',
                fecAveria: '',
                desAveria: '',
                desObservaciones: ''
            },
            filters: {
                codDistribuidora: '',
                idContador: '',
                tipEquipo: '',
                fecEnvio: '',
                fecCarga: '',
                fecAveria: '',
                desAveria: '',
                desObservaciones: ''
            },
            editModalInstance: null
        };
    },
    computed: {
        filteredGenerales() {
            return this.generales.filter(general => {
                return (
                    (general.codDistribuidora || '').toLowerCase().includes((this.filters.codDistribuidora || '').toLowerCase()) &&
                    (general.idContador || '').toLowerCase().includes((this.filters.idContador || '').toLowerCase()) &&
                    (general.tipEquipo || '').toLowerCase().includes((this.filters.tipEquipo || '').toLowerCase()) &&
                    (general.fecEnvio || '').toLowerCase().includes((this.filters.fecEnvio || '').toLowerCase()) &&
                    (general.fecCarga || '').toLowerCase().includes((this.filters.fecCarga || '').toLowerCase()) &&
                    (general.fecAveria || '').toLowerCase().includes((this.filters.fecAveria || '').toLowerCase()) &&
                    (general.desAveria || '').toLowerCase().includes((this.filters.desAveria || '').toLowerCase()) &&
                    (general.desObservaciones || '').toLowerCase().includes((this.filters.desObservaciones || '').toLowerCase())
                );
            });
        },
        paginatedGeneral() {
            const start = (this.currentPage - 1) * this.pageSize;
            const end = start + this.pageSize;
            return this.filteredGenerales.slice(start, end);
        },
    },
    mounted() {
        this.fetchGenerales();
    },
    methods: {
        fetchGenerales() {
            axios.get('http://localhost:8080/api/general')
                .then(response => {
                    this.currentPage = 1;
                    this.generales = response.data;
                    this.totalPages = Math.ceil(this.filteredGenerales.length / this.pageSize);
                })
                .catch(error => {
                    console.error('Error fetching generales:', error);
                });
        },
        changePage(page) {
            if (page < 1 || page > this.totalPages) return;
            this.currentPage = page;
        },

        viewGeneral(general) {
            this.currentGeneral = { ...general };
            const modal = new Modal(document.getElementById('viewModal'));
            modal.show();
        },
        openEditModal(general) {
            this.currentGeneral = { ...general };
            this.currentGeneral.fecEnvio = this.formatDate(general.fecEnvio);
            this.currentGeneral.fecCarga = this.formatDate(general.fecCarga);
            this.currentGeneral.fecAveria = this.formatDate(general.fecAveria);
            this.editModalInstance = new Modal(document.getElementById('editModal'));
            this.editModalInstance.show();
        },
        updateGeneral() {
            axios.put(`http://localhost:8080/api/general/${this.currentGeneral.codDistribuidora}/${this.currentGeneral.idContador}`, this.currentGeneral)
                .then(response => {
                    Swal.fire('Éxito', 'General editado con éxito', 'success');
                    this.fetchGenerales();
                    this.editModalInstance.hide();
                })
                .catch(error => {
                    Swal.fire('Error', 'Error al editar el general', 'error');
                    console.error(error);
                });
        },
        deleteGeneral(general) {
            Swal.fire({
                title: '¿Estás seguro?',
                text: `Se eliminará el general con ID: ${general.idContador}`,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Sí, eliminar',
                cancelButtonText: 'Cancelar'
            }).then(result => {
                if (result.isConfirmed) {
                    axios.delete(`http://localhost:8080/api/general/${general.codDistribuidora}/${general.idContador}`) 
                        .then(response => {
                            Swal.fire('Eliminado', 'El general ha sido eliminado', 'success');
                            this.fetchGenerales();
                        })
                        .catch(error => {
                            Swal.fire('Error', 'Error al eliminar el general', 'error');
                            console.error(error);
                        });
                }
            });
        },
        formatDate(dateString) {
            const date = new Date(dateString);
            return date.getFullYear() + '-' + String(date.getMonth() + 1).padStart(2, '0') + '-' + String(date.getDate()).padStart(2, '0');
        }
    }
};
</script>

<style scoped>
.container {
    max-width: 1600px;
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