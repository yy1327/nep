import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

const app = createApp(App)

router.beforeEach((to, from, next) => {
  const supervisor = sessionStorage.getItem('supervisor')
  if (['/selectGrid', '/selectAqi', '/feedbackList'].includes(to.path)) {
    if (!supervisor) {
      return next('/login')
    }
  }
  next()
})

app.use(router)
app.mount('#app')
