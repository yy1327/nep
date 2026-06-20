import { createRouter, createWebHashHistory } from "vue-router"
import Login from "../views/Login.vue"
import Index from "../views/Index.vue"
import FeedbackList from "../views/FeedbackList.vue"
import FeedbackInfo from "../views/FeedbackInfo.vue"
import CompletedHistory from "../views/CompletedHistory.vue"
import GridSettings from "../views/GridSettings.vue"

const routes = [
  {
    path: "/",
    redirect: "/login",
  },
  {
    path: "/login",
    name: "Login",
    component: Login,
    meta: { title: "网格员登录" },
  },
  {
    path: "/index",
    component: Index,
    children: [
      {
        path: "tasks",
        name: "Tasks",
        component: FeedbackList,
        meta: { title: "待检测任务" },
      },
      {
        path: "detect/:id",
        name: "Detect",
        component: FeedbackInfo,
        meta: { title: "录入检测数据" },
      },
      {
        path: "history",
        name: "History",
        component: CompletedHistory,
        meta: { title: "已完成记录" },
      },
      {
        path: "settings",
        name: "Settings",
        component: GridSettings,
        meta: { title: "个人设置" },
      },
    ],
  },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
  if (to.meta && to.meta.title) {
    document.title = to.meta.title
  }
  next()
})

export default router
