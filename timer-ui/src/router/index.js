import Vue from 'vue'
import VueRouter from 'vue-router'
import TimerApi from "@/plugins/timer-api";

Vue.use(VueRouter)

const routes = [

    {
        path: '/rounds',
        name: 'rounds',
        component: () => import('../views/RoundsView.vue'),
        meta: { requiresAuth: true, role: 'JUDGE' }
    },
    {
        path: '/competitor-rounds',
        name: 'competitor-rounds',
        component: () => import('../views/CompetitorRoundView.vue'),
        meta: { requiresAuth: true, role: 'JUDGE' }
    },
    {
        path: '/competition',
        name: 'competition',
        component: () => import('../views/CompetitionView.vue'),
        meta: { requiresAuth: true, role: 'JUDGE' }
    },
    {
        path: '/register',
        name: 'register',
        component: () => import('../views/RegisterView.vue'),
        meta: { requiresAuth: true, role: 'JUDGE' }
    },
    {
        path: '/admin',
        name: 'admin',
        component: () => import('../views/AdminView.vue'),
        meta: { requiresAuth: true, role: 'JUDGE' }
    },
    {
        path: '/liveResults',
        name: 'liveResults',
        component: () => import('../views/LiveResultsView.vue'),
        meta: {mobile: true, requiresAuth: false}
    },
    {
        path: '/live',
        name: 'live',
        component: () => import('../views/LiveStartView.vue'),
        meta: {mobile: true, requiresAuth: false, type: 'live'}
    },
    {
        path: '/liveStart/:type?',
        name: 'liveStart',
        component: () => import('../views/LiveStartView.vue'),
        meta: {mobile: true, requiresAuth: false, type: 'herren'}
    },
    {
        path: '*',
        redirect: '/live',
        meta: { requiresAuth: false}
    },
    {
        path: '/login',
        name: 'Login',
        component: () => import('@/views/LoginView.vue'),
        meta: { requiresAuth: false }
    }
]

const router = new VueRouter({
    mode: 'history',
    routes
})

router.beforeEach((to, from, next) => {

    document.title = to.name

    const user = TimerApi.getCurrentUser()

    if (to.meta.requiresAuth && !user) {
        next({name: 'Login'})
    } else {
        next()
    }

})

export default router
