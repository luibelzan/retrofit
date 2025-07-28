<template>
    <div class="container mt-3">
        <h1 class="text-center mb-3">Gestión de Diagnósticos</h1>

        <button class="btn btn-primary mb-3 me-2" @click="openCreateModal">Crear Diagnóstico</button>
        <button class="btn btn-info mb-3" @click="fetchDiagnosticos()">Actualizar lista</button>

        <div class="table-responsive">
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>
                            Tipo Diagnóstico
                            <input type="text" class="form-control mt-1" v-model="filters.tipoDiagnostico"
                                placeholder="Filtrar...">
                        </th>
                        <th>
                            Código Diagnóstico
                            <input type="text" class="form-control mt-1" v-model="filters.codigoDiagnostico"
                                placeholder="Filtrar...">
                        </th>
                        <th>
                            Descripción
                            <input type="text" class="form-control mt-1" v-model="filters.descripcion"
                                placeholder="Filtrar...">
                        </th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="diagnostico in paginatedDiagnosticos" :key="diagnostico.codDiagnostico">
                        <td>{{ diagnostico.tipDiagnostico }}</td>
                        <td>{{ diagnostico.codDiagnostico }}</td>
                        <td>{{ diagnostico.desDiagnostico }}</td>
                        <td>
                            <div class="btn-group" role="group" aria-label="Basic mixed styles example">
                                <button type="button" class="btn btn-info btn-sm "
                                    @click="viewDiagnostico(diagnostico)">Ver</button>
                                <button type="button" class="btn btn-warning btn-sm"
                                    @click="openEditModal(diagnostico)">Editar</button>
                                <button type="button" class="btn btn-danger btn-sm"
                                    @click="deleteDiagnostico(diagnostico)">Eliminar</button>
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

        <!-- Modal Crear Diagnóstico -->
        <div class="modal fade" id="createModal" tabindex="-1" aria-labelledby="createModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="createModalLabel">Crear Diagnóstico</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="createDiagnostico">
                            <div class="mb-3">
                                <label for="createTipoDiagnostico" class="form-label">Tipo Diagnóstico</label>
                                <input type="text" id="createTipoDiagnostico" v-model="newDiagnostico.tipDiagnostico"
                                    class="form-control" pattern="^[A-Za-z0-9]{2}$"
                                    title="El tipo de diagnóstico debe ser alfanumérico y tener exactamente 2 caracteres."
                                    required>
                            </div>
                            <div class="mb-3">
                                <label for="createCodigoDiagnostico" class="form-label">Código Diagnóstico</label>
                                <input type="text" id="createCodigoDiagnostico" v-model="newDiagnostico.codDiagnostico"
                                    class="form-control" pattern="^[0-9]{2}$"
                                    title="El código diagnóstico debe ser un número de exactamente 2 dígitos." required>
                            </div>
                            <div class="mb-3">
                                <label for="createDescripcion" class="form-label">Descripción</label>
                                <input type="text" id="createDescripcion" v-model="newDiagnostico.desDiagnostico"
                                    class="form-control" pattern="^[A-Za-z0-9\s]{1,50}$"
                                    title="La descripción debe ser alfanumérica y puede tener hasta 50 caracteres."
                                    required>
                            </div>
                            <button type="submit" class="btn btn-primary">Crear</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>


        <!-- Modal Editar Diagnóstico -->
        <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editModalLabel">Editar Diagnóstico</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="updateDiagnostico">
                            <div class="mb-3">
                                <label for="editCodigoDiagnostico" class="form-label">Código Diagnóstico</label>
                                <input type="text" id="editCodigoDiagnostico"
                                    v-model="currentDiagnostico.codDiagnostico" class="form-control" readonly>
                            </div>
                            <div class="mb-3">
                                <label for="editTipoDiagnostico" class="form-label">Tipo Diagnóstico</label>
                                <input type="text" id="editTipoDiagnostico" v-model="currentDiagnostico.tipDiagnostico"
                                    class="form-control" readonly pattern="^[A-Za-z0-9]{2}$"
                                    title="El tipo de diagnóstico debe ser alfanumérico y tener exactamente 2 caracteres.">
                            </div>

                            <div class="mb-3">
                                <label for="editDescripcion" class="form-label">Descripción</label>
                                <input type="text" id="editDescripcion" v-model="currentDiagnostico.desDiagnostico"
                                    class="form-control" required pattern="^[A-Za-z0-9\s]{1,50}$"
                                    title="La descripción debe ser alfanumérica y puede tener hasta 50 caracteres.">
                            </div>
                            <button type="submit" class="btn btn-primary">Actualizar</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Ver Diagnóstico -->
        <div class="modal fade" id="viewModal" tabindex="-1" aria-labelledby="viewModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="viewModalLabel">Detalles del Diagnóstico</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <p><strong>Tipo Diagnóstico:</strong> {{ currentDiagnostico.tipDiagnostico }}</p>
                        <p><strong>Código Diagnóstico:</strong> {{ currentDiagnostico.codDiagnostico }}</p>
                        <p><strong>Descripción:</strong> {{ currentDiagnostico.desDiagnostico }}</p>
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
            diagnosticos: [],
            currentPage: 1,
            pageSize: 10, 
            totalPages: 1,
            newDiagnostico: {
                tipDiagnostico: '',
                codDiagnostico: '',
                desDiagnostico: '',
            },
            currentDiagnostico: {},
            filters: {
                tipoDiagnostico: '',
                codigoDiagnostico: '',
                descripcion: '',
            },
            createModalInstance: null,
            editModalInstance: null,
            viewModalInstance: null,
        };
    },
    computed: {
        filteredDiagnosticos() {
            return this.diagnosticos.filter((diagnostico) => {
                return (
                    diagnostico.tipDiagnostico.toLowerCase().includes(this.filters.tipoDiagnostico.toLowerCase()) &&
                    diagnostico.codDiagnostico.toString().includes(this.filters.codigoDiagnostico) &&
                    diagnostico.desDiagnostico.toLowerCase().includes(this.filters.descripcion.toLowerCase())
                );
            });
        },
        // Calcular almacenes paginados según la página actual
        paginatedDiagnosticos() {
            const start = (this.currentPage - 1) * this.pageSize;
            const end = start + this.pageSize;
            return this.filteredDiagnosticos.slice(start, end);
        },
    },
    methods: {
        fetchDiagnosticos() {
            axios.get('http://localhost:8080/api/diagnosticos')
                .then(response => {
                    this.currentPage = 1;
                    this.diagnosticos = response.data;
                    this.totalPages = Math.ceil(this.filteredDiagnosticos.length / this.pageSize);
                })
                .catch(error => {
                    console.error("Hubo un error al obtener los diagnósticos:", error);
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
        createDiagnostico() {
            axios.post('http://localhost:8080/api/diagnosticos', this.newDiagnostico)
                .then(() => {
                    this.fetchDiagnosticos();
                    this.createModalInstance.hide();
                    Swal.fire({
                        title: 'El diagnóstico ha sido creado.',
                        icon: 'success',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: 'Aceptar',
                    });
                })
                .catch(error => {
                    console.error("Hubo un error al crear el diagnóstico:", error);
                    Swal.fire({
                        title: 'No se pudo crear el diagnóstico.',
                        icon: 'error',
                        confirmButtonColor: '#d33',
                        confirmButtonText: 'Aceptar',
                    });
                });
        },
        openEditModal(diagnostico) {
            this.currentDiagnostico = { ...diagnostico };
            this.editModalInstance = new Modal(document.getElementById('editModal'));
            this.editModalInstance.show();
        },
        updateDiagnostico() {
            axios.put('http://localhost:8080/api/diagnosticos', this.currentDiagnostico)
                .then(() => {
                    this.fetchDiagnosticos();
                    this.editModalInstance.hide();
                    Swal.fire({
                        title: 'El diagnóstico ha sido editado.',
                        icon: 'success',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: 'Aceptar',
                    });
                })
                .catch(error => {
                    console.error("Hubo un error al actualizar el diagnóstico:", error);
                    Swal.fire({
                        title: 'No se pudo editar el diagnóstico.',
                        icon: 'error',
                        confirmButtonColor: '#d33',
                        confirmButtonText: 'Aceptar',
                    });
                });
        },
        viewDiagnostico(diagnostico) {
            this.currentDiagnostico = { ...diagnostico };
            this.viewModalInstance = new Modal(document.getElementById('viewModal'));
            this.viewModalInstance.show();
        },
        deleteDiagnostico(diagnostico) {
            Swal.fire({
                title: '¿Estás seguro?',
                text: `No podrás recuperar este diagnóstico con código ${diagnostico.codDiagnostico}.`,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#d33',
                cancelButtonColor: '#3085d6',
                confirmButtonText: 'Sí, eliminar',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.delete(`http://localhost:8080/api/diagnosticos/${diagnostico.tipDiagnostico}/${diagnostico.codDiagnostico}`)
                        .then(() => {
                            this.fetchDiagnosticos();
                            Swal.fire({
                                title: 'El diagnóstico ha sido eliminado.',
                                icon: 'success',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: 'Aceptar',
                            });
                        })
                        .catch(error => {
                            console.error("Hubo un error al eliminar el diagnóstico:", error);
                            Swal.fire({
                                title: 'No se pudo eliminar el diagnóstico.',
                                icon: 'error',
                                confirmButtonColor: '#d33',
                                confirmButtonText: 'Aceptar',
                            });
                        });
                }
            });
        },
        resetNewDiagnostico() {
            this.newDiagnostico = {
                tipDiagnostico: '',
                codDiagnostico: '',
                desDiagnostico: '',
            };
        },
    },
    mounted() {
        this.fetchDiagnosticos();

        const createModalEl = document.getElementById('createModal');
        createModalEl.addEventListener('hidden.bs.modal', this.resetNewDiagnostico);
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
