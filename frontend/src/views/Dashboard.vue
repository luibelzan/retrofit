<template>
    <div class="dashboard-container">
        <div v-if="cargando" class="loader-container">
            <div class="loader"></div>
        </div>

        <div v-else class="kpi-cards">
            <div v-for="distribuidora in distribuidoras" :key="distribuidora.codigo" class="card">
                <div class="header">
                    <img :src="getDistribuidoraLogo(distribuidora.nombre)" alt="Logo de {{ distribuidora.nombre }}"
                        class="logo" />
                    <h3>{{ distribuidora.nombre }}</h3>
                </div>

                <div class="kpi-grid">
                    <div class="kpi-item">
                        <p class="label">Total Contadores</p>
                        <p class="value">{{ distribuidora.totalContadores }}</p>
                    </div>
                    <div class="kpi-item">
                        <p class="label">Total Recuperados</p>
                        <p class="value">{{ distribuidora.totalRecuperados }}</p>
                    </div>
                    <div class="kpi-item">
                        <p class="label">Total Achatarrados</p>
                        <p class="value">{{ distribuidora.totalAchatarrados }}</p>
                    </div>
                    <div class="kpi-item">
                        <p class="label">En proceso</p>
                        <p class="value">{{ distribuidora.totalRecepcionados }}</p>
                    </div>
                    <div class="kpi-item">
                        <p class="label">Por enviar</p>
                        <p class="value">{{ distribuidora.totalPendientesEnviar }}</p>
                    </div>
                    <div class="kpi-item">
                        <p class="label">Tasa de Recuperación</p>
                        <p class="value">{{ (distribuidora.tasaRecuperacion * 100).toFixed(2) }}%</p>
                    </div>

                    <div class="kpi-chart">
                        <Doughnut :data="getChartData(distribuidora)" :options="chartOptions" />
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>



<script>

import { Doughnut } from 'vue-chartjs'
import { Chart as ChartJS, Title, Tooltip, Legend, ArcElement, CategoryScale, LinearScale } from 'chart.js'
import ChartDataLabels from 'chartjs-plugin-datalabels';

ChartJS.register(ChartDataLabels, Title, Tooltip, Legend, ArcElement, CategoryScale, LinearScale)

export default {
    name: 'Dashboard',
    components: { Doughnut },
    data() {
        return {
            distribuidoras: [],
            cargando: true,
            chartOptions: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    datalabels: {
                        color: '#fff',
                        font: {
                            size: 20,
                            weight: 'bold',
                        },
                        formatter: function (value) {
                            return value;
                        },
                        anchor: 'center',
                        align: 'center',
                    },
                },
            },
        }
    },
    async created() {
        await this.loadDistribuidoras()
    },
    methods: {
        async loadDistribuidoras() {
            try {
                const response = await fetch('http://localhost:8080/api/dashboard/kpis')
                this.distribuidoras = await response.json()
                console.log(this.distribuidoras)
            } catch (error) {
                console.error('Error al cargar los KPIs:', error)
                alert('Ocurrió un error al cargar los datos.')
            } finally {
                this.cargando = false
            }
        },
        getDistribuidoraLogo(nombre) {
            const logos = {
                'SAGEMCOM': '/images/sagemcom.png',
                'LANDIS&GYR': '/images/landis&gyr.png',
                'IBERDROLA': '/images/iberdrola-ide.png',
                'KAIFA': '/images/kaifa.png',
            };

            return logos[nombre.toUpperCase()] || '/images/logo-celnet.png';
        },

        getChartData(distribuidora) {
            return {
                labels: ['Recuperados', 'Achatarrados'],
                datasets: [
                    {
                        data: [distribuidora.totalRecuperados, distribuidora.totalAchatarrados],
                        backgroundColor: ['#28a745', '#dc3545'],
                        hoverOffset: 4,
                    },
                ],
            }
        },
    },
}
</script>

<style scoped>
.dashboard-container {
    padding: 20px;
}

.kpi-cards {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 20px;
}

.card {
    background: white;
    border: 1px solid #ddd;
    border-radius: 5px;
    padding: 20px;
    box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.1);
    display: flex;
    flex-direction: column;
    justify-content: space-between;
}

.card h3 {
    font-size: 40px;
    margin-bottom: 15px;
    text-align: center;
    font-weight: bold;
    color: #333;
    text-transform: uppercase;
    text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.2);
    border-bottom: 3px solid #274469;
    padding-bottom: 5px;
}

.kpi-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 15px;
}

.kpi-chart {
    text-align: center;
    padding: 10px;
    background: #f9f9f9;
    border-radius: 5px;
    box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.1);
    grid-column: span 2;
}


.kpi-item {
    text-align: center;
    padding: 10px;
    background: #f9f9f9;
    border-radius: 5px;
    box-shadow: 1px 1px 3px rgba(0, 0, 0, 0.1);
    font-size: 18px;
}

.label {
    font-size: 22px;
    color: #555;
}

.value {
    font-size: 30px;
    font-weight: bold;
    color: #333;
    margin-top: 5px;
}


.loader-container {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 190px;
    margin-bottom: 200px;
}

.loader {
    border: 8px solid #f3f3f3;
    border-top: 8px solid #274469;
    border-radius: 50%;
    width: 60px;
    height: 60px;
    animation: spin 1.5s linear infinite;
}

@keyframes spin {
    0% {
        transform: rotate(0deg);
    }

    100% {
        transform: rotate(360deg);
    }
}

@media (max-width: 768px) {
    .kpi-cards {
        grid-template-columns: 1fr;
    }
}

.header {
    display: flex;
    align-items: center;
    justify-content: flex-start;
}

.logo {
    width: 50px;
    height: 50px;
    object-fit: contain;
    border-radius: 50%;
    margin-right: 10px;
    margin-bottom: 10px;
}

h3 {
    margin: 0;
    font-size: 24px;
    font-weight: bold;
}
</style>
