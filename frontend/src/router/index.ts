import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '@/views/Dashboard.vue'
import ETLProceso from '@/views/ETLProceso.vue'
import RecepcionContador from '@/views/RecepcionContador.vue'
import Recuperado from '@/views/Recuperado.vue'
import Achatarrado from '@/views/Achatarrado.vue'
import Almacenes from '@/views/Almacenes.vue'
import Diagnostico from '@/views/Diagnostico.vue'
import Distribuidora from '@/views/Distribuidora.vue'
import Fabricante from '@/views/Fabricante.vue'
import Modelo from '@/views/Modelo.vue'
import Fichero from '@/views/Fichero.vue'
import Proceso from '@/views/Proceso.vue'
import General from '@/views/General.vue'
import Lotes from '@/views/Lotes.vue'
import AsignarLote from '@/views/AsignarLote.vue'
import ReportesGeneral from '@/views/ReportesGeneral.vue'
import Estadisticas from '@/views/Estadisticas.vue'
import Enviados from '@/views/Enviados.vue'
import AchatarradosFallo from '@/views/AchatarradosFallo.vue'
import Sustituciones from '@/views/Sustituciones.vue'
import ReportesRecepcion from '@/views/ReportesRecepcion.vue'
import ReportesRecuperados from '@/views/ReportesRecuperados.vue'
import ReportesAchatarrados from '@/views/ReportesAchatarrados.vue'
import ReportesContadoresPorEnviar from '@/views/ReportesContadoresPorEnviar.vue'
import ReportesSustituidos from '@/views/ReportesSustituidos.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: Dashboard
  },
  {
    path: '/excel',
    name: 'ETLproceso',
    component: ETLProceso
  },
  {
    path: '/recepcion',
    name: 'RecepcionContador',
    component: RecepcionContador,
  },
  {
    path: '/recuperado',
    name: 'Recuperado',
    component: Recuperado,
  },
  {
    path: '/achatarrado',
    name: 'Achatarrado',
    component: Achatarrado,
  },
  {
    path: '/almacenes',
    name: 'Almacenes',
    component: Almacenes,
  },
  {
    path: '/diagnostico',
    name: 'Diagnostico',
    component: Diagnostico,
  },
  {
    path: '/distribuidora',
    name: 'Distribuidora',
    component: Distribuidora,
  },
  {
    path: '/fabricante',
    name: 'Fabricante',
    component: Fabricante,
  },
  {
    path: '/modelo',
    name: 'Modelo',
    component: Modelo,
  },
  {
    path: '/fichero',
    name: 'Fichero',
    component: Fichero,
  },
  {
    path: '/proceso',
    name: 'Proceso',
    component: Proceso,
  },
  {
    path: '/general',
    name: 'General',
    component: General,
  },
  {
    path: '/lotes',
    name: 'Lotes',
    component: Lotes,
  },
  {
    path: '/asignar',
    name: 'AsignarLote',
    component: AsignarLote,
  },
  {
    path: '/reportes-general',
    name: 'ReportesGeneral',
    component: ReportesGeneral,
  },
  {
    path: '/enviados-lotes',
    name: 'EnviadosLotes',
    component: Enviados,
  },
  {
    path: '/achatarrados-fallo',
    name: 'AchatarradosFallo',
    component: AchatarradosFallo,
  },
  {
    path: '/sustituciones',
    name: 'Sustituciones',
    component: Sustituciones,
  },
  {
    path: '/reportes-recepcion',
    name: 'ReportesRecepcion',
    component: ReportesRecepcion,
  },
  {
    path: '/reportes-recuperados',
    name: 'ReportesRecuperados',
    component: ReportesRecuperados,
  },
  {
    path: '/reportes-achatarrados',
    name: 'ReportesAchatarrados',
    component: ReportesAchatarrados,
  },
  {
    path: '/reportes-sustituidos',
    name: 'ReportesSustituidos',
    component: ReportesSustituidos,
  },
  {
    path: '/estadisticas',
    name: 'Estadisticas',
    component: Estadisticas,
  },
  {
    path: '/reportes-contadores-por-enviar',
    name: 'ReportesContadoresPorEnviar',
    component: ReportesContadoresPorEnviar,
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router
