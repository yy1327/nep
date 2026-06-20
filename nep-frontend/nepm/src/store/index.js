import { createStore } from 'vuex'

export default createStore({
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
