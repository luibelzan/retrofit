<template>
    <div class="container mt-3 border">
        <div class="p-4 rounded">
            <div class="form-group mb-3">
                <label for="distribuidora">Distribuidora</label>
                <select id="distribuidora" class="form-select" v-model="formData.codDistribuidora" required>
                    <option v-for="dist in distribuidoras" :key="dist.codDistribuidora" :value="dist.codDistribuidora">
                        {{ dist.nomDistribuidora }}
                    </option>
                </select>
            </div>

            <div class="form-group mb-3">
                <label>Fecha inicio</label>
                <input type="date" v-model="formData.fechaInicio" class="form-control" required>
            </div>

            <div class="form-group mb-3">
                <label>Fecha Fin</label>
                <input type="date" v-model="formData.fechaFin" class="form-control" required>
            </div>

            <button class="btn btn-primary" @click="generarExcel">
                Generar Excel
            </button>
        </div>
    </div>
</template>

<script>
import axios from "axios";
import Swal from "sweetalert2";


export default {
    data() {
        return {
            distribuidoras: [],
            formData: {
                codDistribuidora: "",
                fechaInicio: "",
                fechaFin: "",
            }
        }
    },
    methods: {
        async fetchData() {
            try {
                const resDistribuidoras = await fetch("http://localhost:8080/api/achatarrado/distribuidoras");
                this.distribuidoras = await resDistribuidoras.json();
            } catch (error) {
                console.error("Error cargando datos:", error);
            }
        },

        async generarExcel() {
            if(!this.formData.codDistribuidora || !this.formData.fechaInicio || !this.formData.fechaFin) {
                Swal.fire({
                    icon: "warning",
                    title: "Campos incompletos",
                    text: "Debes seleccionar distribuidora y un rango de fechas.",
                });
                return;
            }
            try {
                const response = await axios.get("http://localhost:8080/api/reportes/excel", {
                    params: {
                        codDistribuidora: this.formData.codDistribuidora,
                        fechaInicio: this.formData.fechaInicio,
                        fechaFin: this.formData.fechaFin,
                    },
                    responseType: "blob"
                });

                //Descargar archivo
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const link = document.createElement("a");
                link.href = url;
                link.setAttribute("download", "Reporte.xlsx");
                document.body.appendChild(link);
                link.click();
            } catch(error) {
                alert("Error al generar Excel");
            }
        }
    },
    mounted() {
        this.fetchData();
    }
}

</script>