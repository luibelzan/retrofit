<template>
    <div class="container mt-3">
        <h1 class="text-center mb-3">Gestión de Lotes</h1>
        <button class="btn btn-primary mb-3" @click="abrirModalCrear">Crear Nuevo Lote</button>

        <!-- Tabla de lotes -->
        <div class="table-responsive">
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th>ID Lote</th>
                        <th>Código Distribuidora</th>
                        <th>Nombre del Lote</th>
                        <th>Código Almacén</th>
                        <th>Fecha</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <tr v-for="lote in lotes" :key="lote.idLote">
                        <td>{{ lote.idLote }}</td>
                        <td>{{ lote.codDistribuidora }}</td>
                        <td>{{ lote.nomLote }}</td>
                        <td>{{ lote.codAlmacen }}</td>
                        <td>{{ formatoFecha(lote.fecLote) }}</td>
                        <td>
                            <div class="btn-group" role="group" aria-label="Basic mixed styles example">
                                <button class="btn btn-info btn-sm" @click="abrirModalVer(lote)">Ver</button>
                                <button class="btn btn-warning btn-sm" @click="abrirModalEditar(lote)">Editar</button>
                                <button class="btn btn-danger btn-sm" @click="eliminarLote(lote)">Eliminar</button>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <!-- Modal Crear Lote -->
        <div v-if="modalCrearVisible" class="modal fade show" id="createModal" tabindex="-1"
            aria-labelledby="createModalLabel" aria-hidden="false">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="createModalLabel">Crear Nuevo Lote</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"
                            @click="cerrarModal"></button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="crearLote">
                            <div class="mb-3">
                                <label>ID Lote</label>
                                <input v-model="nuevoLote.idLote" class="form-control" required />
                            </div>
                            <div class="mb-3">
                                <label>Código Distribuidora</label>
                                <input v-model="nuevoLote.codDistribuidora" class="form-control" required />
                            </div>
                            <div class="mb-3">
                                <label>Nombre del Lote</label>
                                <input v-model="nuevoLote.nomLote" class="form-control" required />
                            </div>
                            <div class="mb-3">
                                <label>Código Almacén</label>
                                <input v-model="nuevoLote.codAlmacen" class="form-control" required />
                            </div>
                            <div class="mb-3">
                                <label>Fecha</label>
                                <input v-model="nuevoLote.fecLote" type="date" class="form-control" required />
                            </div>
                            <button type="submit" class="btn btn-primary">Crear</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Ver Lote -->
        <div v-if="modalVerVisible" class="modal fade show" id="viewModal" tabindex="-1"
            aria-labelledby="viewModalLabel" aria-hidden="false">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="viewModalLabel">Detalles del Lote</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"
                            @click="cerrarModal"></button>
                    </div>
                    <div class="modal-body">
                        <p><strong>ID Lote:</strong> {{ loteActual.idLote }}</p>
                        <p><strong>Código Distribuidora:</strong> {{ loteActual.codDistribuidora }}</p>
                        <p><strong>Nombre del Lote:</strong> {{ loteActual.nomLote }}</p>
                        <p><strong>Código Almacén:</strong> {{ loteActual.codAlmacen }}</p>
                        <p><strong>Fecha:</strong> {{ formatoFecha(loteActual.fecLote) }}</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"
                            @click="cerrarModal">Cerrar</button>
                        <button type="button" class="btn btn-success" id="generateExcelBtn"
                            @click="generarExcel(loteActual)">Generar Excel</button>
                    </div>
                </div>
            </div>
        </div>

        <!-- Modal Editar Lote -->
        <div v-if="modalEditarVisible" class="modal fade show" id="editModal" tabindex="-1"
            aria-labelledby="editModalLabel" aria-hidden="false">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="editModalLabel">Editar Lote</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"
                            @click="cerrarModal"></button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="editarLote">
                            <div class="mb-3">
                                <label>Nombre del Lote</label>
                                <input v-model="loteActual.nomLote" class="form-control" required />
                            </div>
                            <div class="mb-3">
                                <label>Código Almacén</label>
                                <input v-model="loteActual.codAlmacen" class="form-control" required />
                            </div>
                            <div class="mb-3">
                                <label>Fecha del Lote</label>
                                <input v-model="loteActual.fecLote" class="form-control" required />
                            </div>
                            <button type="submit" class="btn btn-primary">Guardar</button>
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
            lotes: [],
            almacenes: [],
            nuevoLote: {
                idLote: "",
                codDistribuidora: "",
                nomLote: "",
                codAlmacen: "",
                fecLote: "",
            },
            loteActual: {
                idLote: "",
                codDistribuidora: "",
                nomLote: "",
                codAlmacen: "",
                fecLote: "",
            },
            modalCrearVisible: false,
            modalEditarVisible: false,
            modalVerVisible: false,
        };
    },
    mounted() {
        this.fetchLotes();
        this.cargarAlmacenes();
    },
    methods: {
        fetchLotes() {
            axios
                .get("http://localhost:8080/api/lotes")
                .then((response) => {
                    this.lotes = response.data;
                })
                .catch((error) => {
                    console.error("Error al obtener los lotes:", error);
                });
        },
        formatoFecha(fecha) {
            if (!fecha) return "";
            return new Date(fecha).toLocaleDateString("es-ES");
        },
        abrirModalCrear() {
            this.modalCrearVisible = true;
            this.resetNuevoLote();
        },
        crearLote() {
            axios
                .post("http://localhost:8080/api/lotes", this.nuevoLote)
                .then(() => {
                    this.fetchLotes();
                    this.modalCrearVisible = false;
                    Swal.fire("Éxito", "Lote creado correctamente", "success");
                })
                .catch(() => {
                    Swal.fire("Error", "Hubo un error al crear el lote", "error");
                });
        },
        abrirModalVer(lote) {
            this.loteActual = { ...lote };
            this.modalVerVisible = true;
        },
        abrirModalEditar(lote) {
            this.loteActual = { ...lote };
            this.modalEditarVisible = true;
        },
        editarLote() {
            axios
                .put(`http://localhost:8080/api/lotes/${this.loteActual.codDistribuidora}/${this.loteActual.idLote}`, this.loteActual)
                .then(() => {
                    this.fetchLotes();
                    this.modalEditarVisible = false;
                    Swal.fire("Éxito", "Lote actualizado correctamente", "success");
                })
                .catch(() => {
                    Swal.fire("Error", "Hubo un error al actualizar el lote", "error");
                });
        },
        eliminarLote(lote) {
            Swal.fire({
                title: "¿Estás seguro?",
                text: "No podrás revertir esta acción",
                icon: "warning",
                showCancelButton: true,
                confirmButtonText: "Sí, eliminar",
                cancelButtonText: "Cancelar",
            }).then((result) => {
                if (result.isConfirmed) {
                    axios
                        .delete(`http://localhost:8080/api/lotes/${lote.codDistribuidora}/${lote.idLote}`)
                        .then(() => {
                            this.fetchLotes();
                            Swal.fire("Eliminado", "Lote eliminado correctamente", "success");
                        })
                        .catch(() => {
                            Swal.fire("Error", "Hubo un error al eliminar el lote", "error");
                        });
                }
            });
        },

        cerrarModal() {
            this.modalCrearVisible = false;
            this.modalEditarVisible = false;
            this.modalVerVisible = false;
        },
        resetNuevoLote() {
            this.nuevoLote = {
                idLote: "",
                codDistribuidora: "",
                nomLote: "",
                codAlmacen: "",
                fecLote: "",
            };
        },

        generarExcel(lote) {
            if (!lote || !lote.codDistribuidora || !lote.idLote || !lote.codAlmacen) {
                Swal.fire("Error", "Lote no válido", "error");
                return;
            }

            // Obtener el nombre del almacén en base al código
            const nombreAlmacen = this.getAlmacenNombre(lote.codAlmacen).replace(/\s+/g, "_");

            axios.get(`http://localhost:8080/api/lotes/${lote.codDistribuidora}/${lote.idLote}/contadores/excel`, {
                responseType: 'blob'
            })
                .then(response => {
                    const url = window.URL.createObjectURL(new Blob([response.data]));
                    const link = document.createElement('a');
                    link.href = url;

                    // Nombre del archivo con el nombre del almacén
                    const fileName = `Lote_${lote.idLote}_Almacen_${nombreAlmacen}.xlsx`;
                    link.setAttribute('download', fileName);
                    document.body.appendChild(link);
                    link.click();
                    document.body.removeChild(link);
                })
                .catch(() => {
                    Swal.fire("Error", "No se pudo generar el archivo Excel", "error");
                });
        },

        cargarAlmacenes() {
            axios
                .get("http://localhost:8080/api/almacenes") 
                .then((response) => {
                    this.almacenes = response.data;
                })
                .catch((error) => {
                    Swal.fire("Error", "Error al cargar los almacenes.", "error");
                });
        },


        getAlmacenNombre(codigoAlmacen) {
            if (!this.almacenes || this.almacenes.length === 0) {
                return "Almacén no encontrado";
            }

            const almacen = this.almacenes.find(alm => alm.codAlmacen === codigoAlmacen);
            return almacen ? almacen.desAlmacen : "Almacén no encontrado";
        },

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

.modal.show {
    display: block;
    background-color: rgba(0, 0, 0, 0.5);
}
</style>