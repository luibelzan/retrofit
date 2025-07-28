import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '@/views/Dashboard.vue'
import ETLProceso from '@/views/ETLProceso.vue'
import RecepcionContador from '@/views/RecepcionContador.vue'
import Recuperado from '@/views/Recuperado.vue'
import Achatarrado from '@/views/Achatarrado.vue'
import Almacenes from '@/views/Almacenes.vue'
import Diagnostico from '@/views/Diagnostico.vue'
import Distribuidora from '@/views/Distribuidora.vue'

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
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router
