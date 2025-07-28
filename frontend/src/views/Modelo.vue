<template>
    <div class="container mt-3">
        <h1 class="text-center mb-3">Gestión de Modelos</h1>

        <button class="btn btn-primary mb-3 me-2" @click="openCreateModal">Crear Modelo</button>
        <button class="btn btn-info mb-3" @click="fetchModelos()">Actualizar lista</button>

        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>
                        Código Modelo
                        <input type="text" class="form-control mt-1" v-model="filters.codModelo"
                            placeholder="Filtrar...">
                    </th>
                    <th>
                        Código Fabricante
                        <input type="text" class="form-control mt-1" v-model="filters.codFabricante"
                            placeholder="Filtrar...">
                    </th>
                    <th>
                        Tipo de Contador
                        <input type="text" class="form-control mt-1" v-model="filters.tipContador"
                            placeholder="Filtrar...">
                    </th>
                    <th>
                        Número de Fases
                        <input type="text" class="form-control mt-1" v-model="filters.numFases"
                            placeholder="Filtrar...">
                    </th>
                    <th>
                        Tipo de Medida
                        <input type="text" class="form-control mt-1" v-model="filters.tipMedida"
                            placeholder="Filtrar...">
                    </th>

                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="modelo in paginatedModelos" :key="`${modelo.codModelo}-${modelo.codFabricante}`">
                    <td>{{ modelo.codModelo }}</td>
                    <td>{{ modelo.codFabricante }}</td>
                    <td>{{ modelo.tipContador }}</td>
                    <td>{{ modelo.numFases }}</td>
                    <td>{{ modelo.tipMedida }}</td>
                    <td>
                        <div class="btn-group" role="group" aria-label="Basic mixed styles example">
                            <button type="button" class="btn btn-info btn-sm " @click="viewModelo(modelo)">Ver</button>
                            <button type="button" class="btn btn-warning btn-sm"
                                @click="openEditModal(modelo)">Editar</button>
                            <button type="button" class="btn btn-danger btn-sm"
                                @click="deleteModelo(modelo)">Eliminar</button>
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

        <!-- Modal Crear Modelo -->
        <div class="modal fade" id="createModal" tabindex="-1" aria-labelledby="createModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="createModalLabel">Crear Modelo</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="createModelo">
                            <div class="mb-3">
                                <label for="createCodModelo" class="form-label">Código Modelo</label>
                                <input type="text" id="createCodModelo" v-model="newModelo.codModelo"
                                    class="form-control" pattern="^[A-Za-z0-9]{2}$"
                                    title="El código modelo debe ser alfanumérico y tener exactamente 2 caracteres."
                                    required>
                            </div>
                            <div class="mb-3">
                                <label for="createCodFabricante" class="form-label">Código Fabricante</label>
                                <input type="text" id="createCodFabricante" v-model="newModelo.codFabricante"
                                    class="form-control" pattern="^[A-Za-z0-9]{1}$"
                                    title="El código fabricante debe ser alfanumérico y tener exactamente 1 carácter."
                                    required>
                            </div>
                            <div class="mb-3">
                                <label for="createTipContador" class="form-label">Tipo de Contador</label>
                                <input type="number" id="createTipContador" v-model="newModelo.tipContador"
                                    class="form-control" min="1" max="999"
                                    title="El tipo de contador debe ser un número entre 1 y 999.">
                            </div>
                            <div class="mb-3">
                                <label for="createNumFases" class="form-label">Número de Fases</label>
                                <input type="text" id="createNumFases" v-model="newModelo.numFases" class="form-control"
                                    pattern="^[0-9]{1,2}$"
                                    title="El número de fases debe ser un valor numérico de 1 o 2 dígitos.">
                            </div>
                            <div class="mb-3">
                                <label for="createTipMedida" class="form-label">Tipo de Medida</label>
                                <input type="text" id="createTipMedida" v-model="newModelo.tipMedida"
                                    class="form-control" pattern="^[A-Za-z0-9]{1,2}$"
                                    title="El tipo de medida debe ser alfanumérico y de 1 o 2 caracteres.">
                            </div>

                            <button type="submit" class="btn btn-primary">Crear</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>


        <!-- Modal Editar Modelo -->
        <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editModalLabel">Editar Modelo</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="updateModelo">
                            <div class="mb-3">
                                <label for="editCodModelo" class="form-label">Código Modelo</label>
                                <input type="text" id="editCodModelo" v-model="currentModelo.codModelo"
                                    class="form-control" readonly>
                            </div>
                            <div class="mb-3">
                                <label for="editCodFabricante" class="form-label">Código Fabricante</label>
                                <input type="text" id="editCodFabricante" v-model="currentModelo.codFabricante"
                                    class="form-control" readonly>
                            </div>
                            <div class="mb-3">
                                <label for="editTipContador" class="form-label">Tipo de Contador</label>
                                <input type="number" id="editTipContador" v-model="currentModelo.tipContador"
                                    class="form-control" min="1" max="999"
                                    title="El tipo de contador debe ser un número entre 1 y 999.">
                            </div>
                            <div class="mb-3">
                                <label for="editNumFases" class="form-label">Número de Fases</label>
                                <input type="text" id="editNumFases" v-model="currentModelo.numFases"
                                    class="form-control" pattern="^[0-9]{1,2}$"
                                    title="El número de fases debe ser un valor numérico de 1 o 2 dígitos.">
                            </div>
                            <div class="mb-3">
                                <label for="editTipMedida" class="form-label">Tipo de Medida</label>
                                <input type="text" id="editTipMedida" v-model="currentModelo.tipMedida"
                                    class="form-control" pattern="^[A-Za-z0-9]{1,2}$"
                                    title="El tipo de medida debe ser alfanumérico y de 1 o 2 caracteres.">
                            </div>
                            <button type="submit" class="btn btn-primary">Actualizar</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Ver Modelo -->
        <div class="modal fade" id="viewModal" tabindex="-1" aria-labelledby="viewModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="viewModalLabel">Detalles del Modelo</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <p><strong>Código Modelo:</strong> {{ currentModelo.codModelo }}</p>
                        <p><strong>Código Fabricante:</strong> {{ currentModelo.codFabricante }}</p>
                        <p><strong>Tipo de Contador:</strong> {{ currentModelo.tipContador }}</p>
                        <p><strong>Número de Fases:</strong> {{ currentModelo.numFases }}</p>
                        <p><strong>Tipo de Medida:</strong> {{ currentModelo.tipMedida }}</p>
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
            modelos: [],
            currentPage: 1,
            pageSize: 10,
            totalPages: 1,
            newModelo: {
                codModelo: '',
                codFabricante: '',
                tipContador: '',
                numFases: '',
                tipMedida: '',
            },
            currentModelo: {
                codModelo: '',
                codFabricante: '',
                tipContador: '',
                numFases: '',
                tipMedida: '',
            },
            filters: {
                codModelo: '',
                codFabricante: '',
                tipContador: '',
                numFases: '',
                tipMedida: '',
            },
            createModalInstance: null,
            editModalInstance: null,
            viewModalInstance: null,
        };
    },
    computed: {
        filteredModelos() {
            return this.modelos.filter(modelo => {
                return (
                    (!this.filters.codModelo || modelo.codModelo.includes(this.filters.codModelo)) &&
                    (!this.filters.codFabricante || modelo.codFabricante.includes(this.filters.codFabricante)) &&
                    (!this.filters.tipContador || modelo.tipContador.toString().includes(this.filters.tipContador)) &&
                    (!this.filters.numFases || modelo.numFases.includes(this.filters.numFases)) &&
                    (!this.filters.tipMedida || modelo.tipMedida.includes(this.filters.tipMedida))

                );
            });
        },

        paginatedModelos() {
            const start = (this.currentPage - 1) * this.pageSize;
            const end = start + this.pageSize;
            return this.filteredModelos.slice(start, end);
        },
    },
    methods: {
        fetchModelos() {
            axios.get('http://localhost:8080/api/modelos')
                .then(response => {
                    this.currentPage = 1;
                    this.modelos = response.data;
                    this.totalPages = Math.ceil(this.filteredModelos.length / this.pageSize);
                })
                .catch(error => {
                    console.error("Error al obtener los modelos:", error);
                });
        },
        changePage(page) {
            if (page < 1 || page > this.totalPages) return;
            this.currentPage = page;
        },
        openCreateModal() {
            this.newModelo = {
                codModelo: '',
                codFabricante: '',
                tipContador: '',
                numFases: '',
                tipMedida: '',
            };
            this.createModalInstance = new Modal(document.getElementById('createModal'));
            this.createModalInstance.show();
        },
        createModelo() {
            axios.post('http://localhost:8080/api/modelos', this.newModelo)
                .then(response => {
                    this.modelos.push(response.data);
                    this.createModalInstance.hide();
                    Swal.fire({
                        title: 'El modelo ha sido creado.',
                        icon: 'success',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: 'Aceptar',
                    });
                })
                .catch(error => {
                    console.error("Error al crear el modelo:", error);
                    Swal.fire({
                        title: 'No se pudo crear el modelo.',
                        icon: 'error',
                        confirmButtonColor: '#d33',
                        confirmButtonText: 'Aceptar',
                    });
                });
        },
        openEditModal(modelo) {
            this.currentModelo = { ...modelo };
            this.editModalInstance = new Modal(document.getElementById('editModal'));
            this.editModalInstance.show();
        },
        updateModelo() {
            axios.put('http://localhost:8080/api/modelos', this.currentModelo)
                .then(response => {
                    const index = this.modelos.findIndex(m => m.codModelo === this.currentModelo.codModelo && m.codFabricante === this.currentModelo.codFabricante);
                    this.modelos.splice(index, 1, response.data);
                    this.editModalInstance.hide();
                    Swal.fire({
                        title: 'El modelo ha sido actualizado.',
                        icon: 'success',
                        confirmButtonColor: '#3085d6',
                        confirmButtonText: 'Aceptar',
                    });
                })
                .catch(error => {
                    console.error("Error al actualizar el modelo:", error);
                    Swal.fire({
                        title: 'No se pudo actualizar el modelo.',
                        icon: 'error',
                        confirmButtonColor: '#d33',
                        confirmButtonText: 'Aceptar',
                    });
                });
        },
        viewModelo(modelo) {
            this.currentModelo = { ...modelo };
            this.viewModalInstance = new Modal(document.getElementById('viewModal'));
            this.viewModalInstance.show();
        },
        deleteModelo(modelo) {
            Swal.fire({
                title: '¿Estás seguro?',
                text: `No podrás recuperar este modelo con código ${modelo.codModelo}.`,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#d33',
                cancelButtonColor: '#3085d6',
                confirmButtonText: 'Sí, eliminar',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    axios.delete(`http://localhost:8080/api/modelos/${modelo.codModelo}/${modelo.codFabricante}`)
                        .then(() => {
                            this.modelos = this.modelos.filter(m => m.codModelo !== modelo.codModelo || m.codFabricante !== modelo.codFabricante);
                            Swal.fire({
                                title: 'El modelo ha sido eliminado.',
                                icon: 'success',
                                confirmButtonColor: '#3085d6',
                                confirmButtonText: 'Aceptar',
                            });
                        })
                        .catch(error => {
                            console.error("Error al eliminar el modelo:", error);
                            Swal.fire({
                                title: 'No se pudo eliminar el modelo.',
                                icon: 'error',
                                confirmButtonColor: '#d33',
                                confirmButtonText: 'Aceptar',
                            });
                        });
                }
            });
        },
    },
    mounted() {
        this.fetchModelos();
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
