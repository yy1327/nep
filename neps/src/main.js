import Vue from 'vue'
import App from './App.vue'
import router from './router'
import 'font-awesome/css/font-awesome.min.css'

Vue.config.productionTip = false

router.beforeEach((to, from, next) => {
  const supervisor = sessionStorage.getItem('supervisor')
  if (['/selectGrid', '/selectAqi', '/feedbackList'].includes(to.path)) {
    if (!supervisor) {
      return next('/login')
    }
  }
  next()
})

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
