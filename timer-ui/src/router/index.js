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
        path: '/competitor-rounds',
        name: 'competitor-rounds',
        component: () => import('../views/CompetitorRoundView2.vue')
    },
    {
        path: '/competition',
        name: 'competition',
        component: () => import('../views/CompetitionView.vue')
    },
    {
        path: '/competition2',
        name: 'competition2',
        component: () => import('../views/CompetitionView2.vue')
    },
    {
        path: '/rounds/:roundId',
        name: 'round',
        component: () => import('../views/CompetitorRoundView.vue')
    }
]

const router = new VueRouter({
    routes
})

export default router
