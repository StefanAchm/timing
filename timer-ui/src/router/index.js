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
    },
    {
        path: '/register',
        name: 'register',
        component: () => import('../views/RegisterView.vue')
    },
    {
        path: '*',
        redirect: '/competition'
    }
]

const router = new VueRouter({
    routes
})

router.beforeEach((to, from, next) => {

    document.title = to.name

    next()

})

export default router
