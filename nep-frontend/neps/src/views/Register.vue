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
import { showToast } from '@/utils/toast'

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
        const data = {
          supervisorTel: this.form.supervisorTel,
          supervisorPwd: this.form.supervisorPwd,
          supervisorName: this.form.supervisorName,
          supervisorSex: this.form.supervisorSex,
          supervisorAge: this.form.supervisorAge ? Number(this.form.supervisorAge) : null,
          supervisorAddr: this.form.supervisorAddr,
          provinceId: this.form.provinceId ? Number(this.form.provinceId) : null,
          cityId: this.form.cityId ? Number(this.form.cityId) : null
        }
        const res = await api.post('/supervisor/saveSupervisor', data)
        if (res.code === 200) {
          showToast('注册成功', 'success')
          this.$router.push('/login')
        } else {
          showToast('注册失败: ' + res.msg, 'error')
        }
      } catch (error) {
        console.error('注册错误:', error)
        if (error.response) {
          showToast('服务器错误: ' + error.response.status, 'error')
        } else if (error.request) {
          showToast('无法连接到服务器，请确认后端已启动', 'error')
        } else {
          showToast('请求失败: ' + error.message, 'error')
        }
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
  background: linear-gradient(160deg, #e8f5e9 0%, #c8e6c9 30%, #a5d6a7 60%, #81c784 100%);
  padding: 20px;
  position: relative;
  overflow: hidden;
}
.register-container::before {
  content: "";
  position: absolute;
  width: 500px; height: 500px;
  background: radial-gradient(circle, rgba(255,255,255,0.25) 0%, transparent 70%);
  top: -100px; left: 50%; transform: translateX(-50%);
  border-radius: 50%;
}
.register-box {
  background: rgba(255, 255, 255, 0.95);
  padding: 44px 40px;
  border-radius: 20px;
  box-shadow: 0 20px 60px rgba(46, 125, 50, 0.15), 0 4px 16px rgba(0, 0, 0, 0.05);
  width: 500px;
  max-width: 100%;
  backdrop-filter: blur(10px);
  position: relative;
  z-index: 1;
  animation: cardIn 0.6s ease-out;
}
@keyframes cardIn {
  from { opacity: 0; transform: translateY(30px); }
  to { opacity: 1; transform: translateY(0); }
}
.register-box h2 {
  text-align: center;
  margin-bottom: 36px;
  color: #1b5e20;
  font-size: 24px;
  font-weight: 700;
  letter-spacing: 2px;
  line-height: 1.4;
}
.form-group {
  margin-bottom: 20px;
}
.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #555;
  font-weight: 600;
  font-size: 14px;
  line-height: 1.4;
}
.form-group input,
.form-group select {
  width: 100%;
  padding: 12px 14px;
  border: 1.5px solid #dcedc8;
  border-radius: 10px;
  font-size: 14px;
  color: #333;
  background: #f9fdf7;
  transition: all 0.25s ease;
  box-sizing: border-box;
}
.form-group input:focus,
.form-group select:focus {
  outline: none;
  border-color: #66bb6a;
  background: #fff;
  box-shadow: 0 0 0 3px rgba(102, 187, 106, 0.1);
}
.form-row {
  display: flex;
  gap: 16px;
}
.form-row .form-group {
  flex: 1;
}
.register-btn {
  width: 100%;
  padding: 14px;
  background: linear-gradient(135deg, #43a047, #2e7d32);
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  margin-top: 14px;
  transition: all 0.3s ease;
  letter-spacing: 2px;
  box-shadow: 0 4px 14px rgba(46, 125, 50, 0.25);
  line-height: 1.4;
}
.register-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(46, 125, 50, 0.35);
}
.register-btn:active {
  transform: translateY(0);
}
.login-link {
  text-align: center;
  margin-top: 28px;
  color: #666;
  font-size: 14px;
  line-height: 1.5;
}
.login-link a {
  color: #43a047;
  text-decoration: none;
  font-weight: 600;
  transition: color 0.2s;
}
.login-link a:hover {
  color: #2e7d32;
  text-decoration: underline;
}

@media (max-width: 768px) {
  .register-box { padding: 32px 24px; }
  .form-row { flex-direction: column; gap: 0; }
}
</style>
