import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import SelectGrid from '../views/SelectGrid.vue'
import SelectAqi from '../views/SelectAqi.vue'
import FeedbackList from '../views/FeedbackList.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/selectGrid',
    name: 'SelectGrid',
    component: SelectGrid
  },
  {
    path: '/selectAqi',
    name: 'SelectAqi',
    component: SelectAqi
  },
  {
    path: '/feedbackList',
    name: 'FeedbackList',
    component: FeedbackList
  }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

export default router
