import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue'
import FeedbackList from '../views/FeedbackList.vue'
import FeedbackInfo from '../views/FeedbackInfo.vue'

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
    path: '/feedbackList',
    name: 'FeedbackList',
    component: FeedbackList
  },
  {
    path: '/feedbackInfo/:id',
    name: 'FeedbackInfo',
    component: FeedbackInfo
  }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

export default router
