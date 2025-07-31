<template>
    <div class="container mt-3">
        <h1 class="text-center mb-3">Gestión de Procesos</h1>

        <button class="btn btn-primary mb-3 me-2" @click="openCreateModal">Crear Proceso</button>
        <button class="btn btn-info mb-3" @click="fetchProcesos">Actualizar lista</button>

        <div class="table-responsive">
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>
                            Código Distribuidora
                            <input type="text" class="form-control mt-1" v-model="filters.codDistribuidora"
                                placeholder="Filtrar...">
                        </th>
                        <th>
                            Código Contador
                            <input type="text" class="form-control mt-1" v-model="filters.idContador"
                                placeholder="Filtrar...">
                        </th>
                        <th>
                            Fecha Recepción
                            <input type="date" class="form-control mt-1" v-model="filters.fecRecepcion"
                                placeholder="Filtrar...">
                        </th>
                        <th>
                            Tipo Equipo
                            <input type="text" class="form-control mt-1" v-model="filters.tipEquipo"
                                placeholder="Filtrar...">
                        </th>
                        <th>
                            Tipo Diagnóstico
                            <input type="text" class="form-control mt-1" v-model="filters.tipDiagnostico"
                                placeholder="Filtrar...">
                        </th>
                        <th>
                            Código Diagnóstico
                            <input type="text" class="form-control mt-1" v-model="filters.codDiagnostico"
                                placeholder="Filtrar...">
                        </th>
                        <th>
                            Código Fabricante
                            <input type="text" class="form-control mt-1" v-model="filters.codFabricante"
                                placeholder="Filtrar...">
                        </th>
                        <th>
                            Código Modelo
                            <input type="text" class="form-control mt-1" v-model="filters.codModelo"
                                placeholder="Filtrar...">
                        </th>
                        <th>
                            Año Fabricación
                            <input type="text" class="form-control mt-1" v-model="filters.anoFabricacion"
                                placeholder="Filtrar...">
                        </th>
                        <th>
                            Código Almacén
                            <input type="text" class="form-control mt-1" v-model="filters.codAlmacen"
                                placeholder="Filtrar...">
                        </th>
                        <th>
                            Fecha Avería
                            <input type="date" class="form-control mt-1" v-model="filters.fecAveria"
                                placeholder="Filtrar...">
                        </th>
                        <th>
                            Descripción Avería
                            <input type="text" class="form-control mt-1" v-model="filters.desAveria"
                                placeholder="Filtrar...">
                        </th>
                        <th>
                            Observaciones
                            <input type="text" class="form-control mt-1" v-model="filters.desObservaciones"
                                placeholder="Filtrar...">
                        </th>
                        <th>
                            Id Lote
                            <input type="text" class="form-control mt-1" v-model="filters.idLote"
                                placeholder="Filtrar...">
                        </th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="proceso in paginatedProcesos"
                        :key="proceso.codDistribuidora + proceso.idContador + proceso.fecRecepcion">
                        <td>{{ proceso.codDistribuidora }}</td>
                        <td>{{ proceso.idContador }}</td>
                        <td>{{ formatDate(proceso.fecRecepcion) }}</td>
                        <td>{{ proceso.tipEquipo }}</td>
                        <td>{{ proceso.tipDiagnostico }}</td>
                        <td>{{ proceso.codDiagnostico }}</td>
                        <td>{{ proceso.codFabricante }}</td>
                        <td>{{ proceso.codModelo }}</td>
                        <td>{{ proceso.anoFabricacion }}</td>
                        <td>{{ proceso.codAlmacen }}</td>
                        <td>{{ proceso.fecAveria }}</td>
                        <td>{{ proceso.desAveria }}</td>
                        <td>{{ proceso.desObservaciones }}</td>
                        <td>{{ proceso.idLote }}</td>
                        <td>
                            <div class="btn-group" role="group">
                                <button type="button" class="btn btn-info btn-sm"
                                    @click="viewProceso(proceso)">Ver</button>
                                <button type="button" class="btn btn-warning btn-sm"
                                    @click="openEditModal(proceso)">Editar</button>
                                <button type="button" class="btn btn-danger btn-sm"
                                    @click="deleteProceso(proceso)">Eliminar</button>
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

        <!-- Modal Crear Proceso -->
        <div class="modal fade" id="createModal" tabindex="-1" aria-labelledby="createModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="createModalLabel">Crear Proceso</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="createProceso">
                            <div class="mb-3">
                                <label for="createCodDistribuidora" class="form-label">Código Distribuidora</label>
                                <input type="text" id="createCodDistribuidora" v-model="newProceso.codDistribuidora"
                                    class="form-control" required pattern="\w{3}" title="Debe contener 3 caracteres">
                            </div>
                            <div class="mb-3">
                                <label for="createIdContador" class="form-label">Código Contador</label>
                                <input type="text" id="createIdContador" v-model="newProceso.idContador"
                                    class="form-control" required pattern=".{1,20}" title="Máximo 20 caracteres">
                            </div>
                            <div class="mb-3">
                                <label for="createFecRecepcion" class="form-label">Fecha Recepción</label>
                                <input type="date" id="createFecRecepcion" v-model="newProceso.fecRecepcion"
                                    class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="createTipEquipo" class="form-label">Tipo Equipo</label>
                                <input type="text" id="createTipEquipo" v-model="newProceso.tipEquipo"
                                    class="form-control" required pattern=".{1,2}" title="Máximo 2 caracteres">
                            </div>
                            <div class="mb-3">
                                <label for="createTipDiagnostico" class="form-label">Tipo Diagnóstico</label>
                                <input type="text" id="createTipDiagnostico" v-model="newProceso.tipDiagnostico"
                                    class="form-control" required pattern=".{1,2}" title="Máximo 2 caracteres">
                            </div>
                            <div class="mb-3">
                                <label for="createCodDiagnostico" class="form-label">Código Diagnóstico</label>
                                <input type="number" id="createCodDiagnostico" v-model="newProceso.codDiagnostico"
                                    class="form-control" required min="1" max="9999" title="Introduce un código válido">
                            </div>
                            <div class="mb-3">
                                <label for="createCodFabricante" class="form-label">Código Fabricante</label>
                                <input type="text" id="createCodFabricante" v-model="newProceso.codFabricante"
                                    class="form-control" required pattern=".{1,1}" title="1 carácter">
                            </div>
                            <div class="mb-3">
                                <label for="createCodModelo" class="form-label">Código Modelo</label>
                                <input type="text" id="createCodModelo" v-model="newProceso.codModelo"
                                    class="form-control" required pattern=".{1,2}" title="Máximo 2 caracteres">
                            </div>
                            <div class="mb-3">
                                <label for="createAnoFabricacion" class="form-label">Año Fabricación</label>
                                <input type="text" id="createAnoFabricacion" v-model="newProceso.anoFabricacion"
                                    class="form-control" required pattern="\d{4}" title="Debe contener 4 dígitos">
                            </div>
                            <div class="mb-3">
                                <label for="createCodAlmacen" class="form-label">Código Almacén</label>
                                <input type="number" id="createCodAlmacen" v-model="newProceso.codAlmacen"
                                    class="form-control" required min="1" title="Número válido">
                            </div>
                            <div class="mb-3">
                                <label for="createFecAveria" class="form-label">Fecha Avería</label>
                                <input type="date" id="createFecAveria" v-model="newProceso.fecAveria"
                                    class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="createDesAveria" class="form-label">Descripción Avería</label>
                                <input type="text" id="createDesAveria" v-model="newProceso.desAveria"
                                    class="form-control" required pattern=".{1,250}" title="Máximo 250 caracteres">
                            </div>
                            <div class="mb-3">
                                <label for="createDesObservaciones" class="form-label">Observación</label>
                                <input type="text" id="createDesObservaciones" v-model="newProceso.desObservaciones"
                                    class="form-control" required pattern=".{1,250}" title="Máximo 250 caracteres">
                            </div>
                            <button type="submit" class="btn btn-primary">Crear</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Editar Proceso -->
        <div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editModalLabel">Editar Proceso</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="updateProceso">
                            <div class="mb-3">
                                <label for="editCodDistribuidora" class="form-label">Código Distribuidora</label>
                                <input type="text" id="editCodDistribuidora" v-model="currentProceso.codDistribuidora"
                                    class="form-control" readonly pattern="\w{3}" title="Debe contener 3 caracteres">
                            </div>
                            <div class="mb-3">
                                <label for="editIdContador" class="form-label">Código Contador</label>
                                <input type="text" id="editIdContador" v-model="currentProceso.idContador"
                                    class="form-control" readonly pattern=".{1,20}" title="Máximo 20 caracteres">
                            </div>
                            <div class="mb-3">
                                <label for="editFecRecepcion" class="form-label">Fecha Recepción</label>
                                <input type="date" id="editFecRecepcion" v-model="currentProceso.fecRecepcion"
                                    class="form-control" readonly>
                            </div>
                            <div class="mb-3">
                                <label for="editTipEquipo" class="form-label">Tipo Equipo</label>
                                <input type="text" id="editTipEquipo" v-model="currentProceso.tipEquipo"
                                    class="form-control" required pattern=".{1,2}" title="Máximo 2 caracteres">
                            </div>
                            <div class="mb-3">
                                <label for="editTipDiagnostico" class="form-label">Tipo Diagnóstico</label>
                                <input type="text" id="editTipDiagnostico" v-model="currentProceso.tipDiagnostico"
                                    class="form-control" required pattern=".{1,2}" title="Máximo 2 caracteres">
                            </div>
                            <div class="mb-3">
                                <label for="editCodDiagnostico" class="form-label">Código Diagnóstico</label>
                                <input type="text" id="editCodDiagnostico" v-model="currentProceso.codDiagnostico"
                                    class="form-control" required min="1" max="9999" title="Número entre 1 y 9999">
                            </div>
                            <div class="mb-3">
                                <label for="editCodFabricante" class="form-label">Código Fabricante</label>
                                <input type="text" id="editCodFabricante" v-model="currentProceso.codFabricante"
                                    class="form-control" required pattern=".{1,1}" title="1 carácter">
                            </div>
                            <div class="mb-3">
                                <label for="editCodModelo" class="form-label">Código Modelo</label>
                                <input type="text" id="editCodModelo" v-model="currentProceso.codModelo"
                                    class="form-control" required pattern=".{1,2}" title="Máximo 2 caracteres">
                            </div>
                            <div class="mb-3">
                                <label for="editAnoFabricacion" class="form-label">Año Fabricación</label>
                                <input type="text" id="editAnoFabricacion" v-model="currentProceso.anoFabricacion"
                                    class="form-control" required pattern="\d{4}" title="Debe contener 4 dígitos">
                            </div>
                            <div class="mb-3">
                                <label for="editCodAlmacen" class="form-label">Código Almacén</label>
                                <input type="text" id="editCodAlmacen" v-model="currentProceso.codAlmacen"
                                    class="form-control" required min="1" title="Número válido">
                            </div>
                            <div class="mb-3">
                                <label for="editFecAveria" class="form-label">Fecha Avería</label>
                                <input type="date" id="editFecAveria" v-model="currentProceso.fecAveria"
                                    class="form-control" required>
                            </div>
                            <div class="mb-3">
                                <label for="editDesAveria" class="form-label">Descripción Avería</label>
                                <input type="text" id="editDesAveria" v-model="currentProceso.desAveria"
                                    class="form-control" required pattern=".{1,250}" title="Máximo 250 caracteres">
                            </div>
                            <div class="mb-3">
                                <label for="editDesObservaciones" class="form-label">Observación</label>
                                <input type="text" id="editDesObservaciones" v-model="currentProceso.desObservaciones"
                                    class="form-control" required pattern=".{1,250}" title="Máximo 250 caracteres">
                            </div>
                            <button type="submit" class="btn btn-primary">Actualizar</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        <!-- Modal Ver Proceso -->
        <div class="modal fade" id="viewModal" tabindex="-1" aria-labelledby="viewModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="viewModalLabel">Detalles del Proceso</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <p><strong>Código Distribuidora:</strong> {{ currentProceso.codDistribuidora }}</p>
                        <p><strong>Código Contador:</strong> {{ currentProceso.idContador }}</p>
                        <p><strong>Fecha Recepción:</strong> {{ formatDate(currentProceso.fecRecepcion) }}</p>
                        <p><strong>Tipo Equipo:</strong> {{ currentProceso.tipEquipo }}</p>
                        <p><strong>Tipo Diagnóstico:</strong> {{ currentProceso.tipDiagnostico }}</p>
                        <p><strong>Código Diagnóstico:</strong> {{ currentProceso.codDiagnostico }}</p>
                        <p><strong>Código Fabricante:</strong> {{ currentProceso.codFabricante }}</p>
                        <p><strong>Código Modelo:</strong> {{ currentProceso.codModelo }}</p>
                        <p><strong>Año Fabricación:</strong> {{ currentProceso.anoFabricacion }}</p>
                        <p><strong>Código Almacén:</strong> {{ currentProceso.codAlmacen }}</p>
                        <p><strong>Fecha Avería:</strong> {{ formatDate(currentProceso.fecAveria) }}</p>
                        <p><strong>Descripción Avería:</strong> {{ currentProceso.desAveria }}</p>
                        <p><strong>Observación:</strong> {{ currentProceso.desObservaciones }}</p>
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
            procesos: [],
            currentPage: 1,
            pageSize: 10,
            totalPages: 1,
            filters: {
                codDistribuidora: '',
                idContador: '',
                fecRecepcion: '',
                tipEquipo: '',
                tipDiagnostico: '',
                codDiagnostico: '',
                codFabricante: '',
                codModelo: '',
                anoFabricacion: '',
                codAlmacen: '',
                fecAveria: '',
                desAveria: '',
                desObservaciones: '',
                idLote: ""
            },
            newProceso: {
                codDistribuidora: '',
                idContador: '',
                fecRecepcion: '',
                tipEquipo: '',
                tipDiagnostico: '',
                codDiagnostico: '',
                codFabricante: '',
                codModelo: '',
                anoFabricacion: '',
                codAlmacen: '',
                fecAveria: '',
                desAveria: '',
                desObservaciones: '',
                idLote: ""
            },
            currentProceso: {
                codDistribuidora: '',
                idContador: '',
                fecRecepcion: '',
                tipEquipo: '',
                tipDiagnostico: '',
                codDiagnostico: '',
                codFabricante: '',
                codModelo: '',
                anoFabricacion: '',
                codAlmacen: '',
                fecAveria: '',
                desAveria: '',
                desObservaciones: '',
                idLote: ""
            },
            createModalInstance: null,
            editModalInstance: null
        };
    },
    computed: {
        filteredProcesos() {
            return this.procesos.filter(proceso => {
                return Object.keys(this.filters).every(key => {
                    const filterValue = this.filters[key];
                    const processValue = proceso[key];
                    if (!filterValue) {
                        return true;
                    }
                    if (processValue == null) {
                        return false;
                    }
                    return filterValue.toLowerCase().includes(processValue.toString().toLowerCase()) ||
                        processValue.toString().toLowerCase().includes(filterValue.toLowerCase());
                });
            });
        },
        paginatedProcesos() {
            const start = (this.currentPage - 1) * this.pageSize;
            const end = start + this.pageSize;
            return this.filteredProcesos.slice(start, end);
        },
    },

    mounted() {
        this.fetchProcesos(); 
    },

    methods: {
        fetchProcesos() {
            axios.get('http://localhost:8080/api/procesos')
                .then(response => {
                    this.currentPage = 1;
                    this.procesos = response.data;
                    this.totalPages = Math.ceil(this.filteredProcesos.length / this.pageSize);
                })
                .catch(error => {
                    console.error("Hubo un error al cargar los procesos:", error);
                });
        },
        changePage(page) {
            if (page < 1 || page > this.totalPages) return;
            this.currentPage = page;
        },
        openCreateModal() {
            this.newProceso = {
                codDistribuidora: '',
                idContador: '',
                fecRecepcion: '',
                tipEquipo: '',
                tipDiagnostico: '',
                codDiagnostico: '',
                codFabricante: '',
                codModelo: '',
                anoFabricacion: '',
                codAlmacen: '',
                fecAveria: '',
                desAveria: '',
                desObservaciones: '',
                idLote: ""
            };
            this.createModalInstance = new Modal(document.getElementById('createModal'));
            this.createModalInstance.show();
        },
        createProceso() {
            axios.post('http://localhost:8080/api/procesos', this.newProceso)
                .then(response => {
                    Swal.fire('Éxito', 'Proceso creado con éxito', 'success');
                    this.procesos.push(response.data);
                    this.createModalInstance.hide();
                    this.fetchProcesos(); 
                })
                .catch(error => {
                    Swal.fire('Error', 'Error al crear el proceso', 'error');
                    console.error("Hubo un error al crear el proceso:", error);
                });
        },
        openEditModal(proceso) {
            this.currentProceso = { ...proceso };
            this.currentProceso.fecRecepcion = this.formatDate(proceso.fecRecepcion);
            this.currentProceso.fecAveria = this.formatDate(proceso.fecAveria);
            this.editModalInstance = new Modal(document.getElementById('editModal'));
            this.editModalInstance.show();
        },
        updateProceso() {
            axios.put(`http://localhost:8080/api/procesos/${this.currentProceso.codDistribuidora}/${this.currentProceso.idContador}/${this.currentProceso.fecRecepcion}`, this.currentProceso)
                .then(response => {
                    const index = this.procesos.findIndex(proceso => proceso.idContador === this.currentProceso.idContador);
                    if (index !== -1) {
                        this.procesos.splice(index, 1, response.data);
                    }
                    Swal.fire('Éxito', 'Proceso editado con éxito', 'success');
                    this.editModalInstance.hide();
                    this.fetchProcesos(); 
                })
                .catch(error => {
                    Swal.fire('Error', 'Error al editar el proceso', 'error');
                    console.error("Hubo un error al actualizar el proceso:", error);
                });
        },
        viewProceso(proceso) {
            this.currentProceso = { ...proceso };
            const modal = new Modal(document.getElementById('viewModal'));
            modal.show();
        },
        deleteProceso(proceso) {
            Swal.fire({
                title: '¿Estás seguro?',
                text: `Se eliminará el Proceso con ID de contador: ${proceso.idContador}`,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Sí, eliminar',
                cancelButtonText: 'Cancelar'
            }).then(result => {
                if (result.isConfirmed) {
                    const formatDate = this.formatDate(proceso.fecRecepcion);
                    axios.delete(`http://localhost:8080/api/procesos/${proceso.codDistribuidora}/${proceso.idContador}/${formatDate}`)
                        .then(() => {
                            Swal.fire('Eliminado', 'El proceso ha sido eliminado', 'success');
                            this.fetchProcesos();
                        })
                        .catch(error => {
                            Swal.fire('Error', 'Error al eliminar el proceso', 'error');
                            console.error("Hubo un error al eliminar el proceso:", error);
                        });
                }
            });
        },
        formatDate(dateString) {
            const date = new Date(dateString);
            return date.getFullYear() + '-' + String(date.getMonth() + 1).padStart(2, '0') + '-' + String(date.getDate()).padStart(2, '0');
        }
    },
};
</script>

<style scoped>
.container {
    max-width: 1800px;
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