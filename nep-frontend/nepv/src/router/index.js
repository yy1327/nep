import { createRouter, createWebHashHistory } from "vue-router"
import Index from "../views/Index.vue"

const routes = [
  {
    path: "/",
    redirect: "/index",
  },
  {
    path: "/index",
    name: "Index",
    component: Index,
    meta: {
      title: "环境监测数据中心",
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
  next()
})

export default router
