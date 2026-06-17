<template>
  <div class="register-container">
    <div class="register-box">
      <h2>公众监督员注册</h2>
      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label>手机号</label>
          <input type="text" v-model="form.supervisorTel" placeholder="请输入手机号" required>
        </div>
        <div class="form-group">
          <label>密码</label>
          <input type="password" v-model="form.supervisorPwd" placeholder="请输入密码" required>
        </div>
        <div class="form-group">
          <label>姓名</label>
          <input type="text" v-model="form.supervisorName" placeholder="请输入姓名" required>
        </div>
        <div class="form-group">
          <label>性别</label>
          <select v-model="form.supervisorSex" required>
            <option value="男">男</option>
            <option value="女">女</option>
          </select>
        </div>
        <div class="form-group">
          <label>年龄</label>
          <input type="number" v-model="form.supervisorAge" placeholder="请输入年龄" required>
        </div>
        <div class="form-group">
          <label>地址</label>
          <input type="text" v-model="form.supervisorAddr" placeholder="请输入详细地址" required>
        </div>
        <div class="form-row">
          <div class="form-group">
            <label>省份</label>
            <select v-model="form.provinceId" @change="loadCities" required>
              <option value="">请选择省份</option>
              <option v-for="p in provinces" :key="p.provinceId" :value="p.provinceId">{{ p.provinceName }}</option>
            </select>
          </div>
          <div class="form-group">
            <label>城市</label>
            <select v-model="form.cityId" required>
              <option value="">请选择城市</option>
              <option v-for="c in cities" :key="c.cityId" :value="c.cityId">{{ c.cityName }}</option>
            </select>
          </div>
        </div>
        <button type="submit" class="register-btn">注册</button>
        <p class="login-link">已有账号？<router-link to="/login">立即登录</router-link></p>
      </form>
    </div>
  </div>
</template>

<script>
import api from '@/api'

export default {
  name: 'Register',
  data() {
    return {
      form: {
        supervisorTel: '',
        supervisorPwd: '',
        supervisorName: '',
        supervisorSex: '男',
        supervisorAge: '',
        supervisorAddr: '',
        provinceId: '',
        cityId: ''
      },
      provinces: [],
      cities: []
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
      if (this.form.provinceId) {
        const res = await api.post('/gridCity/listGridCityByProvinceId', {
          provinceId: this.form.provinceId
        })
        if (res.code === 200) {
          this.cities = res.data
        }
      }
    },
    async handleRegister() {
      try {
        const res = await api.post('/supervisor/saveSupervisor', this.form)
        if (res.code === 200) {
          alert('注册成功')
          this.$router.push('/login')
        } else {
          alert(res.msg)
        }
      } catch (error) {
        alert('注册失败')
      }
    }
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}
.register-box {
  background: white;
  padding: 40px;
  border-radius: 10px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
  width: 500px;
}
.register-box h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}
.form-group {
  margin-bottom: 15px;
}
.form-group label {
  display: block;
  margin-bottom: 5px;
  color: #555;
}
.form-group input,
.form-group select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 14px;
}
.form-row {
  display: flex;
  gap: 15px;
}
.form-row .form-group {
  flex: 1;
}
.register-btn {
  width: 100%;
  padding: 12px;
  background: #667eea;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
  margin-top: 10px;
}
.register-btn:hover {
  background: #556cd6;
}
.login-link {
  text-align: center;
  margin-top: 20px;
  color: #666;
}
.login-link a {
  color: #667eea;
  text-decoration: none;
}
</style>
