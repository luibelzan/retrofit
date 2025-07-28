<template>
    <div class="container mt-3">
        <h1 class="text-center mb-3">Gestión de Almacenes</h1>

        <button class="btn btn-primary mb-3 me-2" @click="openCreateModal">Crear Almacén</button>
        <button class="btn btn-info mb-3" @click="fetchAlmacenes()">Actualizar lista</button>

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
                            Cod Almacén
                            <input type="number" class="form-control mt-1" v-model="filters.codAlmacen"
                                placeholder="Filtrar...">
                        </th>
                        <th>
                            Descripción
                            <input type="text" class="form-control mt-1" v-model="filters.desAlmacen"
                                placeholder="Filtrar...">
                        </th>
                        <th>
                            Dirección
                            <input type="text" class="form-control mt-1" v-model="filters.dirAlmacen"
                                placeholder="Filtrar...">
                        </th>
                        <th>
                            Cod Postal
                            <input type="text" class="form-control mt-1" v-model="filters.cpAlmacen"
                                placeholder="Filtrar...">
                        </th>
                        <th>
                            Nom Encargado
                            <input type="text" class="form-control mt-1" v-model="filters.nomEncargado"
                                placeholder="Filtrar...">
                        </th>
                        <th>
                            Tel Encargado
                            <input type="text" class="form-control mt-1" v-model="filters.telEncargado"
                                placeholder="Filtrar...">
                        </th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="almacen in paginatedAlmacenes" :key="almacen.codAlmacen">
                        <td>{{ almacen.codDistribuidora }}</td>
                        <td>{{ almacen.codAlmacen }}</td>
                        <td>{{ almacen.desAlmacen }}</td>
                        <td>{{ almacen.dirAlmacen }}</td>
                        <td>{{ almacen.cpAlmacen }}</td>
                        <td>{{ almacen.nomEncargado }}</td>
                        <td>{{ almacen.telEncargado }}</td>
                        <td>
                            <div class="btn-group" role="group" aria-label="Basic mixed styles example">
                                <button type="button" class="btn btn-info btn-sm"
                                    @click="viewAlmacen(almacen)">Ver</button>
                                <button type="button" class="btn btn-warning btn-sm"
                                    @click="openEditModal(almacen)">Editar</button>
                                <button type="button" class="btn btn-danger btn-sm"
                                    @click="deleteAlmacen(almacen)">Eliminar</button>
                            </div>
                        </td>
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

        <!-- Modal Crear Almacén -->
        <div class="modal fade" id="createModal" tabindex="-1" aria-labelledby="createModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="createModalLabel">Crear Almacén</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="createAlmacen">
                            <div class="mb-3">
                                <label for="createCodDistribuidora" class="form-label">Código Distribuidora</label>
                                <input type="text" id="createCodDistribuidora" v-model="newAlmacen.codDistribuidora"
                                    class="form-control" pattern="^[A-Za-z0-9]{3}$"
                                    title="El código distribuidora debe ser alfanumérico y tener exactamente 3 caracteres."
                                    required>
                            </div>
                            <div class="mb-3">
                                <label for="createCodAlmacen" class="form-label">Código Almacén</label>
                                <input type="number" id="createCodAlmacen" v-model="newAlmacen.codAlmacen"
                                    class="form-control" title="El código almacén debe ser un número." required>
                            </div>
                            <div class="mb-3">
                                <label for="createDesAlmacen" class="form-label">Descripción</label>
                                <input type="text" id="createDesAlmacen" v-model="newAlmacen.desAlmacen"
                                    class="form-control" pattern="^[A-Za-z0-9\s]{1,30}$"
                                    title="La descripción debe ser alfanumérica y tener un máximo de 30 caracteres."
                                    required>
                            </div>
                            <div class="mb-3">
                                <label for="createDirAlmacen" class="form-label">Dirección</label>
                                <input type="text" id="createDirAlmacen" v-model="newAlmacen.dirAlmacen"
                                    class="form-control" pattern="^[A-Za-z0-9\s,.-]{1,200}$"
                                    title="La dirección debe ser alfanumérica y puede contener espacios, comas, puntos y guiones. Máximo 200 caracteres."
                                    required>
                            </div>
                            <div class="mb-3">
                                <label for="createCpAlmacen" class="form-label">Código Postal</label>
                                <input type="text" id="createCpAlmacen" v-model="newAlmacen.cpAlmacen"
                                    class="form-control" pattern="^\d{5}$"
                                    title="El código postal debe ser numérico y tener exactamente 5 dígitos.">
                            </div>
                            <div class="mb-3">
                                <label for="createNomEncargado" class="form-label">Nombre Encargado</label>
                                <input type="text" id="createNomEncargado" v-model="newAlmacen.nomEncargado"
                                    class="form-control" pattern="^[A-Za-z\s]{1,40}$"
                                    title="El nombre del encargado debe ser alfabético y puede tener hasta 40 caracteres.">
                            </div>
                            <div class="mb-3">
                                <label for="createTelEncargado" class="form-label">Teléfono Encargado</label>
                                <input type="text" id="createTelEncargado" v-model="newAlmacen.telEncargado"
                                    class="form-control" pattern="^\d{9}$"
                                    title="El teléfono debe ser un número de 9 dígitos (sin guiones ni espacios).">
                            </div>
                            <button type="submit" class="btn btn-primary">Crear</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>


        <!-- Modal Editar Almacén -->
        <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editModalLabel">Editar Almacén</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="updateAlmacen">
                            <div class="mb-3">
                                <label for="editCodDistribuidora" class="form-label">Código Distribuidora</label>
                                <input type="text" id="editCodDistribuidora" v-model="currentAlmacen.codDistribuidora"
                                    class="form-control" readonly>
                            </div>
                            <div class="mb-3">
                                <label for="editCodAlmacen" class="form-label">Código Almacén</label>
                                <input type="number" id="editCodAlmacen" v-model="currentAlmacen.codAlmacen"
                                    class="form-control" readonly>
                            </div>
                            <div class="mb-3">
                                <label for="editDesAlmacen" class="form-label">Descripción</label>
                                <input type="text" id="editDesAlmacen" v-model="currentAlmacen.desAlmacen"
                                    class="form-control" pattern="^[A-Za-z0-9\s]{1,30}$"
                                    title="La descripción debe ser alfanumérica y tener un máximo de 30 caracteres.">
                            </div>
                            <div class="mb-3">
                                <label for="editDirAlmacen" class="form-label">Dirección</label>
                                <input type="text" id="editDirAlmacen" v-model="currentAlmacen.dirAlmacen"
                                    class="form-control" pattern="^[A-Za-z0-9\s,.-]{1,200}$"
                                    title="La dirección debe ser alfanumérica y puede contener espacios, comas, puntos y guiones. Máximo 200 caracteres.">
                            </div>
                            <div class="mb-3">
                                <label for="editCpAlmacen" class="form-label">Código Postal</label>
                                <input type="text" id="editCpAlmacen" v-model="currentAlmacen.cpAlmacen"
                                    class="form-control" pattern="^\d{5}$"
                                    title="El código postal debe ser numérico y tener exactamente 5 dígitos.">>
                            </div>
                            <div class="mb-3">
                                <label for="editNomEncargado" class="form-label">Nombre Encargado</label>
                                <input type="text" id="editNomEncargado" v-model="currentAlmacen.nomEncargado"
                                    class="form-control" pattern="^[A-Za-z\s]{1,40}$"
                                    title="El nombre del encargado debe ser alfabético y puede tener hasta 40 caracteres.">>
                            </div>
                            <div class="mb-3">
                                <label for="editTelEncargado" class="form-label">Teléfono Encargado</label>
                                <input type="text" id="editTelEncargado" v-model="currentAlmacen.telEncargado"
                                    class="form-control" pattern="^\d{9}$"
                                    title="El teléfono debe ser un número de 9 dígitos (sin guiones ni espacios).">>
                            </div>
                            <button type="submit" class="btn btn-primary">Actualizar</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Ver Almacén -->
        <div class="modal fade" id="viewModal" tabindex="-1" aria-labelledby="viewModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="viewModalLabel">Detalles del Almacén</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <p><strong>Código Distribuidora:</strong> {{ currentAlmacen.codDistribuidora }}</p>
                        <p><strong>Código Almacén:</strong> {{ currentAlmacen.codAlmacen }}</p>
                        <p><strong>Descripción:</strong> {{ currentAlmacen.desAlmacen }}</p>
                        <p><strong>Dirección:</strong> {{ currentAlmacen.dirAlmacen }}</p>
                        <p><strong>Código Postal:</strong> {{ currentAlmacen.cpAlmacen }}</p>
                        <p><strong>Nombre Encargado:</strong> {{ currentAlmacen.nomEncargado }}</p>
                        <p><strong>Teléfono Encargado:</strong> {{ currentAlmacen.telEncargado }}</p>
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
            almacenes: [],
            currentPage: 1,
            pageSize: 10, 
            totalPages: 1, 
            newAlmacen: {
                codDistribuidora: '',
                codAlmacen: null,
                desAlmacen: '',
                dirAlmacen: '',
                cpAlmacen: '',
                nomEncargado: '',
                telEncargado: '',
            },
            currentAlmacen: {},
            filters: {
                codDistribuidora: '',
                codAlmacen: '',
                desAlmacen: '',
                dirAlmacen: '',
                cpAlmacen: '',
                nomEncargado: '',
                telEncargado: '',
            },
            createModalInstance: null,
            editModalInstance: null,
            viewModalInstance: null,
        };
    },
    computed: {
        filteredAlmacenes() {
            return this.almacenes.filter((almacen) => {
                return (
                    almacen.codDistribuidora.toLowerCase().includes(this.filters.codDistribuidora.toLowerCase()) &&
                    almacen.codAlmacen.toString().includes(this.filters.codAlmacen) &&
                    almacen.desAlmacen.toLowerCase().includes(this.filters.desAlmacen.toLowerCase()) &&
                    almacen.dirAlmacen.toLowerCase().includes(this.filters.dirAlmacen.toLowerCase()) &&
                    almacen.cpAlmacen.includes(this.filters.cpAlmacen) &&
                    almacen.nomEncargado.toLowerCase().includes(this.filters.nomEncargado.toLowerCase()) &&
                    almacen.telEncargado.includes(this.filters.telEncargado)
                );
            });
        },
        paginatedAlmacenes() {
            const start = (this.currentPage - 1) * this.pageSize;
            const end = start + this.pageSize;
            return this.filteredAlmacenes.slice(start, end);
        },
    },
    methods: {
        fetchAlmacenes() {
            axios.get('http://localhost:8080/api/almacenes')
                .then(response => {
                    this.currentPage = 1;
                    this.almacenes = response.data;
                    this.totalPages = Math.ceil(this.filteredAlmacenes.length / this.pageSize);
                })
                .catch(error => {
                    console.error(error);
                });
        },
        changePage(page) {
            if (page < 1 || page > this.totalPages) return;
            this.currentPage = page;
        },
        openCreateModal() {
            this.createModalInstance = new Modal(document.getElementById('createModal'));
            this.createModalInstance.show();
        },
        createAlmacen() {
            axios.post('http://localhost:8080/api/almacenes', this.newAlmacen)
                .then(() => {
                    this.fetchAlmacenes();
                    this.createModalInstance.hide();
                    Swal.fire({
                        title: 'El almacén ha sido creado.',
                        icon: 'success',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: 'Aceptar',
                    });
                })
                .catch(error => {
                    console.error(error);
                    Swal.fire({
                        title: 'No se pudo crear el almacén.',
                        icon: 'error',
                        confirmButtonColor: '#d33',
                        confirmButtonText: 'Aceptar',
                    });
                });
        },
        openEditModal(almacen) {
            this.currentAlmacen = { ...almacen };
            this.editModalInstance = new Modal(document.getElementById('editModal'));
            this.editModalInstance.show();
        },
        updateAlmacen() {
            axios.put('http://localhost:8080/api/almacenes', this.currentAlmacen)
                .then(() => {
                    this.fetchAlmacenes();
                    this.editModalInstance.hide();
                    Swal.fire({
                        title: 'El almacén ha sido editado.',
                        icon: 'success',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: 'Aceptar',
                    });
                })
                .catch(error => {
                    console.error(error);
                    Swal.fire({
                        title: 'No se pudo editar el almacén.',
                        icon: 'error',
                        confirmButtonColor: '#d33',
                        confirmButtonText: 'Aceptar',
                    });
                });
        },
        viewAlmacen(almacen) {
            this.currentAlmacen = { ...almacen };
            this.viewModalInstance = new Modal(document.getElementById('viewModal'));
            this.viewModalInstance.show();
        },
        deleteAlmacen(almacen) {
            Swal.fire({
                title: '¿Estás seguro?',
                text: "No podrás recuperar este almacén una vez eliminado.",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#d33',
                cancelButtonColor: '#3085d6',
                confirmButtonText: 'Sí, eliminar',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.delete(`http://localhost:8080/api/almacenes/${almacen.codDistribuidora}/${almacen.codAlmacen}`)
                        .then(() => {
                            this.fetchAlmacenes();
                            Swal.fire({
                                title: 'El almacén ha sido eliminado.',
                                icon: 'success',
                                confirmButtonColor: '#d33',
                                confirmButtonText: 'Aceptar',
                            });
                        })
                        .catch(error => {
                            console.error(error);
                            Swal.fire({
                                title: 'No se pudo eliminar el almacén.',
                                icon: 'error',
                                confirmButtonColor: '#d33',
                                confirmButtonText: 'Aceptar',
                            });
                        });
                }
            });
        },
        resetNewAlmacen() {
            this.newAlmacen = {
                codDistribuidora: '',
                codAlmacen: null,
                desAlmacen: '',
                dirAlmacen: '',
                cpAlmacen: '',
                nomEncargado: '',
                telEncargado: '',
            };
        },
    },
    mounted() {
        this.fetchAlmacenes();

        const createModalEl = document.getElementById('createModal');
        createModalEl.addEventListener('hidden.bs.modal', this.resetNewAlmacen);
    },
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
