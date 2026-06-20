import { createRouter, createWebHashHistory } from "vue-router"
import Login from "../views/Login.vue"
import Register from "../views/Register.vue"
import SelectGrid from "../views/SelectGrid.vue"
import SelectAqi from "../views/SelectAqi.vue"
import FeedbackList from "../views/FeedbackList.vue"

const routes = [
  {
    path: "/",
    redirect: "/login",
  },
  {
    path: "/login",
    name: "Login",
    component: Login,
    meta: {
      title: "监督员登录",
    },
  },
  {
    path: "/register",
    name: "Register",
    component: Register,
    meta: {
      title: "监督员注册",
    },
  },
  {
    path: "/selectGrid",
    name: "SelectGrid",
    component: SelectGrid,
    meta: {
      title: "环保公众监督",
    },
  },
  {
    path: "/selectAqi",
    name: "SelectAqi",
    component: SelectAqi,
    meta: {
      title: "反馈详情",
    },
  },
  {
    path: "/feedbackList",
    name: "FeedbackList",
    component: FeedbackList,
    meta: {
      title: "反馈列表",
    },
  },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title
  } else {
    document.title = "环保公众监督系统"
  }
  const publicPages = ['/login', '/register']
  const hasSession = !!sessionStorage.getItem('supervisor')
  if (!publicPages.includes(to.path) && !hasSession) {
    next('/login')
  } else if (publicPages.includes(to.path) && hasSession) {
    next('/selectGrid')
  } else {
    next()
  }
})

export default router
