import Vue from "vue";
import VueRouter from "vue-router";
import Login from "../views/Login.vue";
import FeedbackList from "../views/FeedbackList.vue";
import FeedbackInfo from "../views/FeedbackInfo.vue";

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
    meta: { title: "网格员登录" }, // 1.添加 meta 标题
  },
  {
    path: "/feedbackList",
    name: "FeedbackList",
    component: FeedbackList,
    meta: { title: "反馈列表" }, // 1.添加 meta 标题
  },
  {
    path: "/feedbackInfo/:id",
    name: "FeedbackInfo",
    component: FeedbackInfo,
    meta: { title: "反馈详情" }, // 1.添加 meta 标题
  },
];

const router = new VueRouter({
  mode: "history",
  routes,
});

// 2. 添加全局前置守卫，动态修改 document.title
router.beforeEach((to, from, next) => {
  // 如果路由配置了 meta.title，则使用它
  if (to.meta && to.meta.title) {
    document.title = to.meta.title;
  } else {
    // 否则使用默认标题
    document.title = "系统默认标题";
  }
  next();
});

export default router;
