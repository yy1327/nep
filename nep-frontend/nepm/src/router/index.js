import Vue from "vue";
import VueRouter from "vue-router";
import Login from "../views/Login.vue";
import Index from "../views/Index.vue";
import FeedbackList from "../views/feedback/FeedbackList.vue";
import FeedbackInfo from "../views/feedback/FeedbackInfo.vue";
import AssignFeedback from "../views/feedback/AssignFeedback.vue";
import StatisticsList from "../views/feedback/StatisticsList.vue";
import StatisticsInfo from "../views/feedback/StatisticsInfo.vue";
import ProvinceItemTotalStatis from "../views/statistics/ProvinceItemTotalStatis.vue";
import AqiDistributeTotalStatis from "../views/statistics/AqiDistributeTotalStatis.vue";
import AqiTrendTotalStatis from "../views/statistics/AqiTrendTotalStatis.vue";
import OtherTotalStatis from "../views/statistics/OtherTotalStatis.vue";

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
    // 标签名设置为管理员登录
    meta: {
      title: "管理员登录",
    },
  },
  {
    path: "/index",
    name: "Index",
    component: Index,
    children: [
      {
        path: "feedbackList",
        name: "FeedbackList",
        component: FeedbackList,
        meta: {
          title: "反馈列表",
          keepAlive: true,
        },
      },
      {
        path: "feedbackInfo/:id",
        name: "FeedbackInfo",
        component: FeedbackInfo,
        meta: {
          title: "反馈详情",
        },
      },
      {
        path: "assignFeedback/:id",
        name: "AssignFeedback",
        component: AssignFeedback,
        meta: {
          title: "指派网格员",
        },
      },
      {
        path: "statisticsList",
        name: "StatisticsList",
        component: StatisticsList,
        meta: {
          title: "统计列表",
          keepAlive: true,
        },
      },
      {
        path: "statisticsInfo/:id",
        name: "StatisticsInfo",
        component: StatisticsInfo,
        meta: {
          title: "统计详情",
        },
      },
      {
        path: "provinceItemTotalStatis",
        name: "ProvinceItemTotalStatis",
        component: ProvinceItemTotalStatis,
        meta: {
          title: "按省统计",
        },
      },
      {
        path: "aqiDistributeTotalStatis",
        name: "AqiDistributeTotalStatis",
        component: AqiDistributeTotalStatis,
        meta: {
          title: "等级分布",
        },
      },
      {
        path: "aqiTrendTotalStatis",
        name: "AqiTrendTotalStatis",
        component: AqiTrendTotalStatis,
        meta: {
          title: "趋势分析",
        },
      },
      {
        path: "otherTotalStatis",
        name: "OtherTotalStatis",
        component: OtherTotalStatis,
        meta: {
          title: "其它统计",
        },
      },
    ],
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
