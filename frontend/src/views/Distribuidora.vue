<template>
    <div class="container mt-3">
        <h1 class="text-center mb-3">Gestión de Distribuidoras</h1>

        <button class="btn btn-primary mb-3 me-2" @click="openCreateModal">Crear Distribuidora</button>
        <button class="btn btn-info mb-3" @click="fetchDistribuidoras()">Actualizar lista</button>

        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>
                        Código Distribuidora
                        <input type="text" class="form-control mt-1" v-model="filters.codDistribuidora"
                            placeholder="Filtrar...">
                    </th>
                    <th>
                        Nombre Distribuidora
                        <input type="text" class="form-control mt-1" v-model="filters.nomDistribuidora"
                            placeholder="Filtrar...">
                    </th>
                    <th>
                        Tipo Proceso
                        <input type="text" class="form-control mt-1" v-model="filters.tipProceso"
                            placeholder="Filtrar...">
                    </th>
                    <th>
                        Precio Contador
                        <input type="text" class="form-control mt-1" v-model="filters.precioContador"
                            placeholder="Filtrar...">
                    </th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="distribuidora in filteredDistribuidoras" :key="distribuidora.codDistribuidora">
                    <td>{{ distribuidora.codDistribuidora }}</td>
                    <td>{{ distribuidora.nomDistribuidora }}</td>
                    <td>{{ distribuidora.tipProceso }}</td>
                    <td>{{ distribuidora.precioContador }}</td>
                    <td>
                        <div class="btn-group" role="group" aria-label="Basic mixed styles example">
                            <button type="button" class="btn btn-info btn-sm "
                                @click="viewDistribuidora(distribuidora)">Ver</button>
                            <button type="button" class="btn btn-warning btn-sm"
                                @click="openEditModal(distribuidora)">Editar</button>
                            <button type="button" class="btn btn-danger btn-sm"
                                @click="deleteDistribuidora(distribuidora)">Eliminar</button>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>

        <!-- Modal Crear Distribuidora -->
        <div class="modal fade" id="createModal" tabindex="-1" aria-labelledby="createModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="createModalLabel">Crear Distribuidora</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="createDistribuidora">
                            <div class="mb-3">
                                <label for="createCodDistribuidora" class="form-label">Código Distribuidora</label>
                                <input type="text" id="createCodDistribuidora"
                                    v-model="newDistribuidora.codDistribuidora" class="form-control"
                                    pattern="^[A-Za-z0-9]{3}$"
                                    title="El código distribuidora debe ser alfanumérico y tener exactamente 3 caracteres."
                                    required>
                            </div>
                            <div class="mb-3">
                                <label for="createNomDistribuidora" class="form-label">Nombre Distribuidora</label>
                                <input type="text" id="createNomDistribuidora"
                                    v-model="newDistribuidora.nomDistribuidora" class="form-control"
                                    pattern="^[A-Za-z0-9\s]{1,30}$"
                                    title="El nombre de la distribuidora debe ser alfanumérico y puede tener hasta 30 caracteres."
                                    required>
                            </div>
                            <div class="mb-3">
                                <label for="createTipProceso" class="form-label">Tipo Proceso</label>
                                <input type="text" id="createTipProceso" v-model="newDistribuidora.tipProceso"
                                    class="form-control" pattern="^[A-Za-z0-9]{1}$"
                                    title="El tipo de proceso debe ser alfanumérico y tener exactamente 1 carácter."
                                    required>
                            </div>
                            <div class="mb-3">
                                <label for="createPrecioContador" class="form-label">Precio Contador</label>
                                <input type="number" id="createPrecioContador" v-model="newDistribuidora.precioContador"
                                    class="form-control" required>
                            </div>
                            <button type="submit" class="btn btn-primary">Crear</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>


        <!-- Modal Editar Distribuidora -->
        <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editModalLabel">Editar Distribuidora</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="updateDistribuidora">
                            <div class="mb-3">
                                <label for="editCodDistribuidora" class="form-label">Código Distribuidora</label>
                                <input type="text" id="editCodDistribuidora"
                                    v-model="currentDistribuidora.codDistribuidora" class="form-control" readonly>
                            </div>
                            <div class="mb-3">
                                <label for="editNomDistribuidora" class="form-label">Nombre Distribuidora</label>
                                <input type="text" id="editNomDistribuidora"
                                    v-model="currentDistribuidora.nomDistribuidora" class="form-control" required
                                    pattern="^[A-Za-z0-9\s]{1,30}$"
                                    title="El nombre de la distribuidora debe ser alfanumérico y puede tener hasta 30 caracteres.">
                            </div>
                            <div class="mb-3">
                                <label for="editTipProceso" class="form-label">Tipo Proceso</label>
                                <input type="text" id="editTipProceso" v-model="currentDistribuidora.tipProceso"
                                    class="form-control" required pattern="^[A-Za-z0-9]{1}$"
                                    title="El tipo de proceso debe ser alfanumérico y tener exactamente 1 carácter.">
                            </div>
                            <div class="mb-3">
                                <label for="editPrecioContador" class="form-label">Precio Contador</label>
                                <input type="number" id="editPrecioContador"
                                    v-model="currentDistribuidora.precioContador" class="form-control" required>
                            </div>
                            <button type="submit" class="btn btn-primary">Actualizar</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Ver Distribuidora -->
        <div class="modal fade" id="viewModal" tabindex="-1" aria-labelledby="viewModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="viewModalLabel">Detalles de la Distribuidora</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <p><strong>Código Distribuidora:</strong> {{ currentDistribuidora.codDistribuidora }}</p>
                        <p><strong>Nombre Distribuidora:</strong> {{ currentDistribuidora.nomDistribuidora }}</p>
                        <p><strong>Tipo Proceso:</strong> {{ currentDistribuidora.tipProceso }}</p>
                        <p><strong>Precio Contador:</strong> {{ currentDistribuidora.precioContador }}</p>
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
            distribuidoras: [],
            newDistribuidora: {
                codDistribuidora: '',
                nomDistribuidora: '',
                tipProceso: '',
                precioContador: null,
            },
            currentDistribuidora: {
                codDistribuidora: '',
                nomDistribuidora: '',
                tipProceso: '',
                precioContador: null,
            },
            filters: {
                codDistribuidora: '',
                nomDistribuidora: '',
                tipProceso: '',
                precioContador: '',
            },
            createModalInstance: null,
            editModalInstance: null,
            viewModalInstance: null,
        };
    },
    computed: {
        filteredDistribuidoras() {
            return this.distribuidoras.filter(distribuidora => {
                return (
                    distribuidora.codDistribuidora.toLowerCase().includes(this.filters.codDistribuidora.toLowerCase()) &&
                    distribuidora.nomDistribuidora.toLowerCase().includes(this.filters.nomDistribuidora.toLowerCase()) &&
                    distribuidora.tipProceso.toLowerCase().includes(this.filters.tipProceso.toLowerCase()) &&
                    distribuidora.precioContador.toString().includes(this.filters.precioContador)
                );
            });
        },
    },
    methods: {
        fetchDistribuidoras() {
            axios.get('http://localhost:8080/api/distribuidoras')
                .then(response => {
                    this.distribuidoras = response.data;
                })
                .catch(error => {
                    console.error("Hubo un error al obtener las distribuidoras:", error);
                });
        },
        openCreateModal() {
            this.createModalInstance = new Modal(document.getElementById('createModal'));
            this.createModalInstance.show();
        },
        createDistribuidora() {
            axios.post('http://localhost:8080/api/distribuidoras', this.newDistribuidora)
                .then(() => {
                    this.fetchDistribuidoras();
                    this.createModalInstance.hide();
                    Swal.fire({
                        title: 'Distribuidora creada exitosamente.',
                        icon: 'success',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: 'Aceptar',
                    });
                })
                .catch(error => {
                    console.error("Hubo un error al crear la distribuidora:", error);
                    Swal.fire({
                        title: 'Error al crear distribuidora.',
                        icon: 'error',
                        confirmButtonColor: '#d33',
                        confirmButtonText: 'Aceptar',
                    });
                });
        },
        openEditModal(distribuidora) {
            this.currentDistribuidora = { ...distribuidora };
            this.editModalInstance = new Modal(document.getElementById('editModal'));
            this.editModalInstance.show();
        },
        updateDistribuidora() {
            axios.put(`http://localhost:8080/api/distribuidoras/${this.currentDistribuidora.codDistribuidora}`, this.currentDistribuidora)
                .then(() => {
                    this.fetchDistribuidoras();
                    this.editModalInstance.hide();
                    Swal.fire({
                        title: 'Distribuidora actualizada correctamente.',
                        icon: 'success',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: 'Aceptar',
                    });
                })
                .catch(error => {
                    console.error("Hubo un error al actualizar la distribuidora:", error);
                    Swal.fire({
                        title: 'Error al actualizar distribuidora.',
                        icon: 'error',
                        confirmButtonColor: '#d33',
                        confirmButtonText: 'Aceptar',
                    });
                });
        },
        viewDistribuidora(distribuidora) {
            this.currentDistribuidora = { ...distribuidora };
            this.viewModalInstance = new Modal(document.getElementById('viewModal'));
            this.viewModalInstance.show();
        },
        deleteDistribuidora(distribuidora) {
            Swal.fire({
                title: '¿Estás seguro?',
                text: `No podrás recuperar esta distribuidora una vez eliminada.`,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#d33',
                cancelButtonColor: '#3085d6',
                confirmButtonText: 'Sí, eliminar',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.delete(`http://localhost:8080/api/distribuidoras/${distribuidora.codDistribuidora}`)
                        .then(() => {
                            this.fetchDistribuidoras();
                            Swal.fire({
                                title: 'Distribuidora eliminada.',
                                icon: 'success',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: 'Aceptar',
                            });
                        })
                        .catch(error => {
                            console.error("Hubo un error al eliminar la distribuidora:", error);
                            Swal.fire({
                                title: 'Error al eliminar distribuidora.',
                                icon: 'error',
                                confirmButtonColor: '#d33',
                                confirmButtonText: 'Aceptar',
                            });
                        });
                }
            });
        },
        resetNewDistribuidora() {
            this.newDistribuidora = {
                codDistribuidora: '',
                nomDistribuidora: '',
                tipProceso: '',
                precioContador: null,
            };
        },
    },
    mounted() {
        this.fetchDistribuidoras();

        const createModalEl = document.getElementById('createModal');
        createModalEl.addEventListener('hidden.bs.modal', this.resetNewDistribuidora);
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
