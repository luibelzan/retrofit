<template>
    <div class="container mt-3 border">

        <!-- Contador de registros -->
        <div v-if="registros.length > 0" class="contador-recepcionado text-center mt-4">
            <h3 class="contador-numero">Contadores Recepcionados: {{ registros.length }}</h3>
            <p class="ultimo-codigo"><strong>Último Contador Recepcionado:</strong> {{ registros[registros.length -
                1].codigoBarras }}</p>
        </div>

        <!-- Formulario principal -->
        <div class="p-4 rounded">
            <div class="form-group mb-3">
                <label for="distribuidora">Distribuidora</label>
                <select id="distribuidora" class="form-select" v-model="formData.distribuidora"
                    @change="cargarAlmacenes">
                    <option value="777">Iberdrola I-DE</option>
                    <option value="888">SAGEMCOM</option>
                    <option value="999">LANDIS&GYR</option>
                </select>
            </div>

            <div class="form-group mb-3">
                <label for="tipoEquipo">Tipo de Equipo</label>
                <select id="tipoEquipo" class="form-select" v-model="formData.tipoEquipo">
                    <option value="CN">CN</option>
                    <option value="MO">MO</option>
                </select>
            </div>

            <div class="form-group mb-3">
                <label for="almacen">Almacén</label>
                <select id="almacen" class="form-select" v-model="formData.almacen">
                    <option value="">Seleccione un almacén</option>
                    <option v-for="alm in almacenes" :key="alm.codAlmacen" :value="alm.codAlmacen">{{ alm.desAlmacen }}
                    </option>
                </select>
            </div>

            <div class="form-group mb-3">
                <label for="codigoBarras">Código de Barras</label>
                <input type="text" id="codigoBarras" class="form-control" v-model="formData.codigoBarras"
                    @keyup.enter="comprobarRecepcion" />
            </div>


            <button class="btn btn-primary" @click="comprobarRecepcion">
                Comprobar
            </button>
        </div>

        <!-- Lista de registros -->
        <div class="mt-4">
            <div v-for="(registro, index) in registros" :key="registro.codigoBarras"
                :class="getEstadoClass(registro.estado)"
                class="border-bottom border-top py-2 d-flex justify-content-between align-items-center">

                <div class="registro-info d-flex align-items-center">
                    <p><strong>{{ registro.codigoBarras }}</strong></p>
                    <p class="mx-2">/</p>
                    <p><strong>{{ registro.tipoEquipo }}</strong></p>
                    <p class="mx-2">/</p>
                    <p class="mx-2"><strong>{{ getAlmacenNombre(registro.almacen) }}</strong></p>
                </div>

                <!-- Selección de nuevo almacén -->
                <div class="d-flex align-items-center mx-3">
                    <label class="me-2">Reasignar Almacén</label>
                    <select v-model="registro.nuevoAlmacen" class="form-select me-2">
                        <option value="">Seleccione un almacén</option>
                        <option v-for="alm in almacenes" :key="alm.codAlmacen" :value="alm.codAlmacen">
                            {{ alm.desAlmacen }}
                        </option>
                    </select>
                    <button class="btn btn-success" @click="reasignarAlmacen(index)" :disabled="!registro.nuevoAlmacen">
                        Confirmar
                    </button>
                </div>

                <div class="estado mt-2">
                    <p><strong>{{ registro.estado }}</strong></p>
                </div>

                <div>
                    <button class="btn btn-danger" @click="confirmarEliminacion(index)">Eliminar</button>
                </div>
            </div>

            <button v-if="registros.length > 0" class="btn btn-primary mt-1 mb-3" @click="enviarRegistros">
                Enviar Registros
            </button>
        </div>

    </div>
</template>

<script>

export default {
    name: "RecepcionRegistros",
    data() {
        return {
            formData: {
                distribuidora: "",
                tipoEquipo: "",
                almacen: "",
                codigoBarras: "",
            },
            almacenes: [],
            registros: [],
        };
    },
    methods: {
        async cargarAlmacenes() {
            const { distribuidora } = this.formData;
            try {
                const response = await axios.get(`http://localhost:8080/api/almacenes?codDistribuidora=${distribuidora}`);
                this.almacenes = response.data;
            } catch (error) {
                Swal.fire("Error", "Error al cargar los almacenes.", "error");
            }
        },
        comprobarRecepcion() {
            const registro = { ...this.formData };

            axios
                .post("http://localhost:8080/api/recepcion/comprobar", registro)
                .then((response) => {
                    this.registros.push({
                        ...registro,
                        nuevoAlmacen: registro.almacen,
                        estado: response.data.message,
                    });
                    this.formData.codigoBarras = '';
                })
                .catch(() => {
                    Swal.fire("Error", "Error al comprobar el registro.", "error");
                });
        },
        reasignarAlmacen(index) {
            const registro = this.registros[index];
            if (!registro.nuevoAlmacen) {
                Swal.fire({
                    icon: "warning",
                    title: "Seleccione un almacén",
                    text: "Debe seleccionar un nuevo almacén antes de reasignar.",
                });
                return;
            }

            registro.almacen = registro.nuevoAlmacen;
            registro.nuevoAlmacen = "";

            Swal.fire({
                icon: "success",
                title: "Reasignado",
                text: "El almacén ha sido reasignado.",
                timer: 1000,
                showConfirmButton: false,
            });
        },
        confirmarEliminacion(index) {
            Swal.fire({
                title: "¿Estás seguro?",
                text: "El registro será eliminado.",
                icon: "warning",
                showCancelButton: true,
                confirmButtonText: "Sí, eliminar",
                cancelButtonText: "Cancelar",
            }).then((result) => {
                if (result.isConfirmed) {
                    this.eliminarRegistro(index);
                    Swal.fire("Eliminado", "El registro ha sido eliminado.", "success");
                }
            });
        },
        eliminarRegistro(index) {
            this.registros.splice(index, 1);
        },
        enviarRegistros() {
            axios
                .post("http://localhost:8080/api/recepcion/enviar", this.registros)
                .then(() => {
                    Swal.fire("Éxito", "Registros enviados exitosamente.", "success");
                    this.registros = [];
                })
                .catch(() => {
                    Swal.fire("Error", "Error al enviar los registros.", "error");
                });
        },
        getAlmacenNombre(codigoAlmacen) {
            const almacen = this.almacenes.find(alm => alm.codAlmacen === codigoAlmacen);
            return almacen ? almacen.desAlmacen : "Almacén no encontrado";
        },
        getEstadoClass(estado) {
            if (estado === "Este contador ya ha sido recepcionado previamente, revíselo antes de continuar.") {
                return 'estado-warning';
            } else if (estado === "Contador veríficado correctamente.") {
                return 'estado-success';
            } else if (estado === "No existen el elemento escaneado, se creará una entrada nueva.") {
                return 'estado-danger';
            }
            return '';
        },
    },
};
</script>



<style scoped>
.container {
    max-width: 1400px;
    background-color: #f8f9fa;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    font-family: "Quicksand", sans-serif;
}

h2,
h3 {
    text-align: center;
}

.registro-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.col-md-4 {
    display: flex;
    align-items: center;
}

.me-2 {
    margin-right: 0.5rem;
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

.btn-danger {
    border-radius: 25px;
    background-color: #335888;
    border-color: #335888;
}

.btn-danger:hover {
    background-color: #4a75ad;
    border-color: #4a75ad;
}

.contador-recepcionado {
    font-size: 1.2rem;
    background-color: #e9ecef;
    border-radius: 8px;
}

.contador-numero {
    font-size: 2.5rem;
    font-weight: bold;
    color: #274469;
}

.ultimo-codigo {
    font-size: 1.1rem;
    color: #335888;
}

.estado-warning {
    background-color: #ffeaac;
    color: #000000;
}

.estado-success {
    background-color: #c2ffd0;
    color: #000000;
}

.estado-danger {
    background-color: #ffc0c6;
    color: #000000;
}
</style>