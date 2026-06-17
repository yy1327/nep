import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    admin: null
  },
  mutations: {
    setAdmin(state, admin) {
      state.admin = admin
    },
    clearAdmin(state) {
      state.admin = null
    }
  },
  actions: {
    login({ commit }, admin) {
      commit('setAdmin', admin)
      sessionStorage.setItem('admin', JSON.stringify(admin))
    },
    logout({ commit }) {
      commit('clearAdmin')
      sessionStorage.removeItem('admin')
    }
  }
})
