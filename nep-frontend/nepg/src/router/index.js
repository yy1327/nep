import Vue from "vue";
import VueRouter from "vue-router";
import Login from "../views/Login.vue";
import Index from "../views/Index.vue";
import FeedbackList from "../views/FeedbackList.vue";
import FeedbackInfo from "../views/FeedbackInfo.vue";
import CompletedHistory from "../views/CompletedHistory.vue";
import GridSettings from "../views/GridSettings.vue";

Vue.use(VueRouter);

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
    name: "Index",
    component: Index,
    redirect: "/index/tasks",
    children: [
      {
        path: "tasks",
        name: "Tasks",
        component: FeedbackList,
        meta: { title: "待检测任务" },
      },
      {
        path: "detect/:id",
        name: "DetectInfo",
        component: FeedbackInfo,
        meta: { title: "录入检测数据" },
      },
      {
        path: "history",
        name: "CompletedHistory",
        component: CompletedHistory,
        meta: { title: "已完成记录" },
      },
      {
        path: "settings",
        name: "GridSettings",
        component: GridSettings,
        meta: { title: "个人设置" },
      },
    ],
  },
];

const router = new VueRouter({
  mode: "history",
  routes,
});

router.beforeEach((to, from, next) => {
  if (to.meta && to.meta.title) {
    document.title = to.meta.title;
  }
  next();
});

export default router;
