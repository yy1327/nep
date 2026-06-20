import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

const app = createApp(App)

router.beforeEach((to, from, next) => {
  const gridMember = sessionStorage.getItem('gridMember')
  if (to.path.startsWith('/feedbackInfo') || to.path === '/feedbackList') {
    if (!gridMember) {
      return next('/login')
    }
  }
  next()
})

app.use(router)
app.mount('#app')
