import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [

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
        component: () => import('../views/CompetitorRoundView.vue')
    },
    {
        path: '/competition',
        name: 'competition',
        component: () => import('../views/CompetitionView.vue')
    },
    {
        path: '/result',
        name: 'result',
        component: () => import('../views/ResultView.vue')
    }
]

const router = new VueRouter({
    routes
})

export default router
