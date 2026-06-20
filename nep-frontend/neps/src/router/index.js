import Vue from "vue";
import VueRouter from "vue-router";
import Login from "../views/Login.vue";
import Register from "../views/Register.vue";
import SelectGrid from "../views/SelectGrid.vue";
import SelectAqi from "../views/SelectAqi.vue";
import FeedbackList from "../views/FeedbackList.vue";

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
];

const router = new VueRouter({
  mode: "history",
  routes,
});

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title;
  } else {
    document.title = "默认标题"; // 设置一个默认标题
  }
  next();
});

export default router;
