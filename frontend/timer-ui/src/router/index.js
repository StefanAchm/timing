import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from "@/views/HomeView";

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        name: 'home',
        component: HomeView
    },
    {
        path: '/competitors',
        name: 'competitors',
        component: () => import('../views/CompetitorsView.vue')
    },
    {
        path: '/rounds',
        name: 'rounds',
        component: () => import('../views/RoundsView.vue')
    },
    {
        path: '/competition',
        name: 'competition',
        component: () => import('../views/CompetitionView.vue')
    }
]

const router = new VueRouter({
    routes
})

export default router
