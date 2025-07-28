<template>
    <div class="container mt-3">
        <h1 class="text-center mb-3">Gestión de Fabricantes</h1>

        <button class="btn btn-primary mb-3 me-2" @click="openCreateModal">Crear Fabricante</button>
        <button class="btn btn-info mb-3" @click="fetchFabricantes()">Actualizar lista</button>

        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>
                        Código Fabricante
                        <input type="text" class="form-control mt-1" v-model="filters.codFabricante"
                            placeholder="Filtrar...">
                    </th>
                    <th>
                        Descripción Fabricante
                        <input type="text" class="form-control mt-1" v-model="filters.desFabricante"
                            placeholder="Filtrar...">
                    </th>
                    <th>
                        Código Fabricante IB
                        <input type="text" class="form-control mt-1" v-model="filters.codFabricanteIb"
                            placeholder="Filtrar...">
                    </th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="fabricante in filteredFabricantes" :key="fabricante.codFabricante">
                    <td>{{ fabricante.codFabricante }}</td>
                    <td>{{ fabricante.desFabricante }}</td>
                    <td>{{ fabricante.codFabricanteIb }}</td>
                    <td>
                        <div class="btn-group" role="group" aria-label="Basic mixed styles example">
                            <button type="button" class="btn btn-info btn-sm "
                                @click="viewFabricante(fabricante)">Ver</button>
                            <button type="button" class="btn btn-warning btn-sm"
                                @click="openEditModal(fabricante)">Editar</button>
                            <button type="button" class="btn btn-danger btn-sm"
                                @click="deleteFabricante(fabricante)">Eliminar</button>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>

        <!-- Modal Crear Fabricante -->
        <div class="modal fade" id="createModal" tabindex="-1" aria-labelledby="createModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="createModalLabel">Crear Fabricante</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="createFabricante">
                            <div class="mb-3">
                                <label for="createCodFabricante" class="form-label">Código Fabricante</label>
                                <input type="text" id="createCodFabricante" v-model="newFabricante.codFabricante"
                                    class="form-control" pattern="^[A-Za-z0-9]{1}$"
                                    title="El código fabricante debe ser alfanumérico y tener exactamente 1 carácter."
                                    required>
                            </div>
                            <div class="mb-3">
                                <label for="createDesFabricante" class="form-label">Descripción Fabricante</label>
                                <input type="text" id="createDesFabricante" v-model="newFabricante.desFabricante"
                                    class="form-control"
                                    required>
                            </div>
                            <div class="mb-3">
                                <label for="createCodFabricanteIb" class="form-label">Código Fabricante IB</label>
                                <input type="text" id="createCodFabricanteIb" v-model="newFabricante.codFabricanteIb"
                                    class="form-control" pattern="^[A-Za-z0-9]{2}$"
                                    title="El código fabricante IB debe ser alfanumérico y tener exactamente 2 caracteres."
                                    required>
                            </div>
                            <button type="submit" class="btn btn-primary">Crear</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>


        <!-- Modal Editar Fabricante -->
        <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editModalLabel">Editar Fabricante</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="updateFabricante">
                            <div class="mb-3">
                                <label for="editDesFabricante" class="form-label">Descripción Fabricante</label>
                                <input type="text" id="editDesFabricante" v-model="currentFabricante.desFabricante"
                                    class="form-control">
                            </div>
                            <div class="mb-3">
                                <label for="editCodFabricanteIb" class="form-label">Código Fabricante IB</label>
                                <input type="text" id="editCodFabricanteIb" v-model="currentFabricante.codFabricanteIb"
                                    class="form-control" required pattern="^[A-Za-z0-9]{2}$"
                                    title="El código fabricante IB debe ser alfanumérico y tener exactamente 2 caracteres.">
                            </div>
                            <button type="submit" class="btn btn-primary">Actualizar</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Ver Fabricante -->
        <div class="modal fade" id="viewModal" tabindex="-1" aria-labelledby="viewModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="viewModalLabel">Detalles del Fabricante</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <p><strong>Código Fabricante:</strong> {{ currentFabricante.codFabricante }}</p>
                        <p><strong>Descripción Fabricante:</strong> {{ currentFabricante.desFabricante }}</p>
                        <p><strong>Código Fabricante IB:</strong> {{ currentFabricante.codFabricanteIb }}</p>
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
            fabricantes: [],
            newFabricante: {
                codFabricante: '',
                desFabricante: '',
                codFabricanteIb: '',
            },
            currentFabricante: {
                codFabricante: '',
                desFabricante: '',
                codFabricanteIb: '',
            },
            filters: {
                codFabricante: '',
                desFabricante: '',
                codFabricanteIb: '',
            },
            createModalInstance: null,
            editModalInstance: null,
            viewModalInstance: null,
        };
    },
    computed: {
        filteredFabricantes() {
            return this.fabricantes.filter(fabricante => {
                return (
                    fabricante.codFabricante.toLowerCase().includes(this.filters.codFabricante.toLowerCase()) &&
                    fabricante.desFabricante.toLowerCase().includes(this.filters.desFabricante.toLowerCase()) &&
                    fabricante.codFabricanteIb.toLowerCase().includes(this.filters.codFabricanteIb.toLowerCase())
                );
            });
        },
    },
    methods: {
        fetchFabricantes() {
            axios.get('http://localhost:8080/api/fabricantes')
                .then(response => {
                    this.fabricantes = response.data;
                })
                .catch(error => {
                    console.error("Hubo un error al obtener los fabricantes:", error);
                });
        },
        openCreateModal() {
            this.newFabricante = {
                codFabricante: '',
                desFabricante: '',
                codFabricanteIb: '',
            };
            this.createModalInstance = new Modal(document.getElementById('createModal'));
            this.createModalInstance.show();
        },
        createFabricante() {
            axios.post('http://localhost:8080/api/fabricantes', this.newFabricante)
                .then(() => {
                    this.fetchFabricantes();
                    this.createModalInstance.hide();
                    Swal.fire({
                        title: 'Fabricante creado exitosamente.',
                        icon: 'success',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: 'Aceptar',
                    });
                })
                .catch(error => {
                    console.error("Hubo un error al crear el fabricante:", error);
                    Swal.fire({
                        title: 'Error al crear fabricante.',
                        icon: 'error',
                        confirmButtonColor: '#d33',
                        confirmButtonText: 'Aceptar',
                    });
                });
        },
        openEditModal(fabricante) {
            this.currentFabricante = { ...fabricante };
            this.editModalInstance = new Modal(document.getElementById('editModal'));
            this.editModalInstance.show();
        },
        updateFabricante() {
            axios.put(`http://localhost:8080/api/fabricantes/${this.currentFabricante.codFabricante}`, this.currentFabricante)
                .then(() => {
                    this.fetchFabricantes();
                    this.editModalInstance.hide();
                    Swal.fire({
                        title: 'Fabricante actualizado correctamente.',
                        icon: 'success',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: 'Aceptar',
                    });
                })
                .catch(error => {
                    console.error("Hubo un error al actualizar el fabricante:", error);
                    Swal.fire({
                        title: 'Error al actualizar fabricante.',
                        icon: 'error',
                        confirmButtonColor: '#d33',
                        confirmButtonText: 'Aceptar',
                    });
                });
        },
        viewFabricante(fabricante) {
            this.currentFabricante = { ...fabricante };
            this.viewModalInstance = new Modal(document.getElementById('viewModal'));
            this.viewModalInstance.show();
        },
        deleteFabricante(fabricante) {
            Swal.fire({
                title: '¿Estás seguro?',
                text: `No podrás recuperar este fabricante una vez eliminado.`,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#d33',
                cancelButtonColor: '#3085d6',
                confirmButtonText: 'Sí, eliminar',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.delete(`http://localhost:8080/api/fabricantes/${fabricante.codFabricante}`)
                        .then(() => {
                            this.fetchFabricantes();
                            Swal.fire({
                                title: 'Fabricante eliminado.',
                                icon: 'success',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: 'Aceptar',
                            });
                        })
                        .catch(error => {
                            console.error("Hubo un error al eliminar el fabricante:", error);
                            Swal.fire({
                                title: 'Error al eliminar fabricante.',
                                icon: 'error',
                                confirmButtonColor: '#d33',
                                confirmButtonText: 'Aceptar',
                            });
                        });
                }
            });
        },
        resetNewFabricante() {
            this.newFabricante = {
                codFabricante: '',
                desFabricante: '',
                codFabricanteIb: '',
            };
        },
    },
    mounted() {
        this.fetchFabricantes();
        
        const createModalEl = document.getElementById('createModal');
        createModalEl.addEventListener('hidden.bs.modal', this.resetNewFabricante);
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
