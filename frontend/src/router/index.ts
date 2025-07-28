import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '@/views/Dashboard.vue'
import ETLProceso from '@/views/ETLProceso.vue'
import RecepcionContador from '@/views/RecepcionContador.vue'
import Recuperado from '@/views/Recuperado.vue'
import Achatarrado from '@/views/Achatarrado.vue'

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
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router
