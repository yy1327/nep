import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import "font-awesome/css/font-awesome.min.css";

Vue.config.productionTip = false;

router.beforeEach((to, from, next) => {
  const gridMember = sessionStorage.getItem("gridMember");
  if (to.path.startsWith("/index") && to.path !== "/login") {
    if (!gridMember) {
      return next("/login");
    }
  }
  next();
});

new Vue({
  router,
  render: (h) => h(App),
}).$mount("#app");
