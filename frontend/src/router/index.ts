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
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router
