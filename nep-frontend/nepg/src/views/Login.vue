<template>
  <div class="login-container">
    <div class="login-box">
      <h2>网格员登录</h2>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label>工号</label>
          <input type="text" v-model="form.gmCode" placeholder="请输入工号" required>
        </div>
        <div class="form-group">
          <label>密码</label>
          <input type="password" v-model="form.gmPwd" placeholder="请输入密码" required>
        </div>
        <button type="submit" class="login-btn">登录</button>
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
        gmCode: '',
        gmPwd: ''
      }
    }
  },
  methods: {
    async handleLogin() {
      try {
        const res = await api.post('/gridMember/getGridMemberByCodeByPass', this.form)
        if (res.code === 200) {
          sessionStorage.setItem('gridMember', JSON.stringify(res.data))
          this.$router.push('/feedbackList')
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
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
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
  background: #11998e;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
}
.login-btn:hover {
  background: #10887d;
}
</style>
