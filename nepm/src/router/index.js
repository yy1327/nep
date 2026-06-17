import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue'
import Index from '../views/Index.vue'
import FeedbackList from '../views/feedback/FeedbackList.vue'
import FeedbackInfo from '../views/feedback/FeedbackInfo.vue'
import AssignFeedback from '../views/feedback/AssignFeedback.vue'
import StatisticsList from '../views/feedback/StatisticsList.vue'
import StatisticsInfo from '../views/feedback/StatisticsInfo.vue'
import ProvinceItemTotalStatis from '../views/statistics/ProvinceItemTotalStatis.vue'
import AqiDistributeTotalStatis from '../views/statistics/AqiDistributeTotalStatis.vue'
import AqiTrendTotalStatis from '../views/statistics/AqiTrendTotalStatis.vue'
import OtherTotalStatis from '../views/statistics/OtherTotalStatis.vue'

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
    path: '/index',
    name: 'Index',
    component: Index,
    children: [
      {
        path: 'feedbackList',
        name: 'FeedbackList',
        component: FeedbackList
      },
      {
        path: 'feedbackInfo/:id',
        name: 'FeedbackInfo',
        component: FeedbackInfo
      },
      {
        path: 'assignFeedback/:id',
        name: 'AssignFeedback',
        component: AssignFeedback
      },
      {
        path: 'statisticsList',
        name: 'StatisticsList',
        component: StatisticsList
      },
      {
        path: 'statisticsInfo/:id',
        name: 'StatisticsInfo',
        component: StatisticsInfo
      },
      {
        path: 'provinceItemTotalStatis',
        name: 'ProvinceItemTotalStatis',
        component: ProvinceItemTotalStatis
      },
      {
        path: 'aqiDistributeTotalStatis',
        name: 'AqiDistributeTotalStatis',
        component: AqiDistributeTotalStatis
      },
      {
        path: 'aqiTrendTotalStatis',
        name: 'AqiTrendTotalStatis',
        component: AqiTrendTotalStatis
      },
      {
        path: 'otherTotalStatis',
        name: 'OtherTotalStatis',
        component: OtherTotalStatis
      }
    ]
  }
]

const router = new VueRouter({
  mode: 'history',
  routes
})

export default router
