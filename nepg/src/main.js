import Vue from 'vue'
import App from './App.vue'
import router from './router'
import 'font-awesome/css/font-awesome.min.css'

Vue.config.productionTip = false

router.beforeEach((to, from, next) => {
  const gridMember = sessionStorage.getItem('gridMember')
  if (['/feedbackList', '/feedbackInfo'].includes(to.path)) {
    if (!gridMember) {
      router.push('/login')
    }
  }
  next()
})

new Vue({
  router,
  render: h => h(App)
}).$mount('#app')
