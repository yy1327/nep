<template>
  <div class="select-grid-container">
    <div class="header">
      <h2>选择地区</h2>
      <button class="logout-btn" @click="logout">退出登录</button>
    </div>
    <div class="content">
      <div class="form-group">
        <label>省份</label>
        <select v-model="provinceId" @change="loadCities" required>
          <option value="">请选择省份</option>
          <option v-for="p in provinces" :key="p.provinceId" :value="p.provinceId">{{ p.provinceName }}</option>
        </select>
      </div>
      <div class="form-group">
        <label>城市</label>
        <select v-model="cityId" required>
          <option value="">请选择城市</option>
          <option v-for="c in cities" :key="c.cityId" :value="c.cityId">{{ c.cityName }}</option>
        </select>
      </div>
      <button class="next-btn" @click="goNext" :disabled="!provinceId || !cityId">下一步</button>
    </div>
  </div>
</template>

<script>
import api from '@/api'

export default {
  name: 'SelectGrid',
  data() {
    return {
      provinces: [],
      cities: [],
      provinceId: '',
      cityId: ''
    }
  },
  created() {
    this.loadProvinces()
  },
  methods: {
    async loadProvinces() {
      const res = await api.post('/gridProvince/listGridProvinceAll')
      if (res.code === 200) {
        this.provinces = res.data
      }
    },
    async loadCities() {
      if (this.provinceId) {
        const res = await api.post('/gridCity/listGridCityByProvinceId', {
          provinceId: this.provinceId
        })
        if (res.code === 200) {
          this.cities = res.data
        }
      }
    },
    goNext() {
      const supervisor = JSON.parse(sessionStorage.getItem('supervisor'))
      supervisor.provinceId = this.provinceId
      supervisor.cityId = this.cityId
      sessionStorage.setItem('supervisor', JSON.stringify(supervisor))
      this.$router.push('/selectAqi')
    },
    logout() {
      sessionStorage.removeItem('supervisor')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.select-grid-container {
  min-height: 100vh;
  background: #f5f5f5;
}
.header {
  background: #667eea;
  color: white;
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.logout-btn {
  background: transparent;
  color: white;
  border: 1px solid white;
  padding: 8px 16px;
  border-radius: 5px;
  cursor: pointer;
}
.content {
  max-width: 500px;
  margin: 50px auto;
  background: white;
  padding: 40px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
.form-group {
  margin-bottom: 20px;
}
.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #333;
  font-weight: 500;
}
.form-group select {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 14px;
}
.next-btn {
  width: 100%;
  padding: 12px;
  background: #667eea;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
}
.next-btn:disabled {
  background: #ccc;
  cursor: not-allowed;
}
.next-btn:hover:not(:disabled) {
  background: #556cd6;
}
</style>
