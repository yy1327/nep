<template>
  <div class="login-container">
    <div class="login-box">
      <h2>公众监督员登录</h2>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label>手机号</label>
          <input type="text" v-model="form.tel" placeholder="请输入手机号" required>
        </div>
        <div class="form-group">
          <label>密码</label>
          <input type="password" v-model="form.pwd" placeholder="请输入密码" required>
        </div>
        <button type="submit" class="login-btn">登录</button>
        <p class="register-link">还没有账号？<router-link to="/register">立即注册</router-link></p>
      </form>
    </div>
  </div>
</template>

<script>
import api from '@/api'

export default {
  name: 'Login',
  data() {
    return {
      form: {
        tel: '',
        pwd: ''
      }
    }
  },
  methods: {
    async handleLogin() {
      try {
        const res = await api.post('/supervisor/getSupervisorByIdByPass', {
          supervisorTel: this.form.tel,
          supervisorPwd: this.form.pwd
        })
        if (res.code === 200) {
          sessionStorage.setItem('supervisor', JSON.stringify(res.data))
          this.$router.push('/selectGrid')
        } else {
          alert(res.msg)
        }
      } catch (error) {
        alert('登录失败')
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}
.login-box {
  background: white;
  padding: 40px;
  border-radius: 10px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
  width: 400px;
}
.login-box h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}
.form-group {
  margin-bottom: 20px;
}
.form-group label {
  display: block;
  margin-bottom: 5px;
  color: #555;
}
.form-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 14px;
}
.login-btn {
  width: 100%;
  padding: 12px;
  background: #667eea;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
}
.login-btn:hover {
  background: #556cd6;
}
.register-link {
  text-align: center;
  margin-top: 20px;
  color: #666;
}
.register-link a {
  color: #667eea;
  text-decoration: none;
}
</style>
