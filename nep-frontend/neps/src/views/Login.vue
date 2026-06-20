<template>
  <div class="login-container">
    <!-- 背景装饰 -->
    <div class="bg-decoration">
      <span class="bg-leaf" v-for="i in 8" :key="i"
        :style="{ left: (12 * i - 6) + '%', top: (i % 2 === 0 ? 10 : 80) + '%', animationDelay: (i * 0.4) + 's', fontSize: (18 + i * 3) + 'px' }">🍃</span>
      <span class="bg-leaf" v-for="i in 5" :key="'b'+i"
        :style="{ left: (20 * i) + '%', top: (i % 2 === 0 ? 85 : 5) + '%', animationDelay: (i * 0.6 + 2) + 's', fontSize: (14 + i * 2) + 'px', opacity: '0.15' }">🌿</span>
    </div>

    <!-- 登录卡片 -->
    <div class="login-card">
      <!-- 顶部图标区 -->
      <div class="card-header">
        <div class="logo-circle">🌳</div>
        <h1 class="app-title">环保公众监督平台</h1>
        <p class="app-subtitle">守护蓝天绿水，共建美丽家园</p>
      </div>

      <!-- 表单 -->
      <form @submit.prevent="handleLogin" class="login-form">
        <div class="input-group">
          <span class="input-icon">📱</span>
          <input
            type="text"
            v-model="form.tel"
            placeholder="请输入手机号"
            required
            class="input-field"
          />
        </div>
        <div class="input-group">
          <span class="input-icon">🔒</span>
          <input
            type="password"
            v-model="form.pwd"
            placeholder="请输入密码"
            required
            class="input-field"
          />
        </div>
        <button type="submit" class="login-btn">
          <span>登 录</span>
          <span class="btn-icon">→</span>
        </button>
      </form>

      <!-- 底部链接 -->
      <div class="card-footer">
        <span class="footer-text">还没有账号？</span>
        <router-link to="/register" class="register-link">立即注册</router-link>
      </div>
    </div>

    <!-- 底部版权 -->
    <div class="copy-right">东软环保公众监督系统 © 2026</div>
  </div>
</template>

<script>
import api from '@/api'
import { showToast } from '@/utils/toast'

export default {
  name: 'Login',
  data() {
    return {
      form: { tel: '', pwd: '' }
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
          showToast(res.msg, 'error')
        }
      } catch (error) {
        showToast('登录失败，请检查网络连接', 'error')
      }
    }
  }
}
</script>

<style scoped>
/* ========== 整体背景 ========== */
.login-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(160deg, #e8f5e9 0%, #c8e6c9 30%, #a5d6a7 60%, #81c784 100%);
  position: relative;
  overflow: hidden;
  font-family: "Microsoft YaHei", "PingFang SC", sans-serif;
}

/* ========== 背景装饰 ========== */
.bg-decoration {
  position: absolute;
  inset: 0;
  pointer-events: none;
}
.bg-leaf {
  position: absolute;
  opacity: 0.12;
  animation: bgFloat 8s ease-in-out infinite;
}
@keyframes bgFloat {
  0%, 100% { transform: translateY(0) rotate(0deg); }
  25% { transform: translateY(-15px) rotate(8deg); }
  50% { transform: translateY(-8px) rotate(-5deg); }
  75% { transform: translateY(-20px) rotate(3deg); }
}

/* ========== 光晕 ========== */
.login-container::before {
  content: "";
  position: absolute;
  width: 500px; height: 500px;
  background: radial-gradient(circle, rgba(255,255,255,0.25) 0%, transparent 70%);
  top: -100px; left: 50%; transform: translateX(-50%);
  border-radius: 50%;
}
.login-container::after {
  content: "";
  position: absolute;
  width: 300px; height: 300px;
  background: radial-gradient(circle, rgba(255,255,255,0.15) 0%, transparent 70%);
  bottom: -60px; right: 10%;
  border-radius: 50%;
}

/* ========== 卡片 ========== */
.login-card {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20px;
  padding: 44px 36px 32px;
  width: 400px;
  box-shadow: 0 20px 60px rgba(46, 125, 50, 0.15), 0 4px 16px rgba(0, 0, 0, 0.05);
  backdrop-filter: blur(10px);
  position: relative;
  z-index: 1;
  animation: cardIn 0.6s ease-out;
}
@keyframes cardIn {
  from { opacity: 0; transform: translateY(30px); }
  to { opacity: 1; transform: translateY(0); }
}

/* ========== 卡片头部 ========== */
.card-header {
  text-align: center;
  margin-bottom: 36px;
}
.logo-circle {
  width: 72px; height: 72px;
  background: linear-gradient(135deg, #e8f5e9, #c8e6c9, #a5d6a7);
  border-radius: 50%;
  display: inline-flex;
  align-items: center; justify-content: center;
  font-size: 36px;
  margin-bottom: 16px;
  box-shadow: 0 4px 16px rgba(46, 125, 50, 0.15);
  animation: logoBounce 3s ease-in-out infinite;
}
@keyframes logoBounce {
  0%, 100% { transform: scale(1); }
  50% { transform: scale(1.06); }
}
.app-title {
  font-size: 24px;
  font-weight: 700;
  color: #1b5e20;
  letter-spacing: 2px;
  margin-bottom: 8px;
  line-height: 1.4;
}
.app-subtitle {
  font-size: 13px;
  color: #81c784;
  letter-spacing: 3px;
  line-height: 1.5;
}

/* ========== 输入框 ========== */
.login-form {
  margin-bottom: 20px;
}
.input-group {
  position: relative;
  margin-bottom: 22px;
}
.input-icon {
  position: absolute;
  left: 14px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 18px;
  z-index: 1;
  opacity: 0.5;
  transition: opacity 0.2s;
}
.input-group:focus-within .input-icon {
  opacity: 1;
}
.input-field {
  width: 100%;
  padding: 15px 15px 15px 46px;
  border: 2px solid #dcedc8;
  border-radius: 10px;
  font-size: 15px;
  color: #333;
  background: #f9fdf7;
  transition: all 0.3s;
  box-sizing: border-box;
  line-height: 1.4;
}
.input-field::placeholder {
  color: #bbb;
}
.input-field:focus {
  outline: none;
  border-color: #66bb6a;
  background: #fff;
  box-shadow: 0 0 0 4px rgba(102, 187, 106, 0.1);
}

/* ========== 登录按钮 ========== */
.login-btn {
  width: 100%;
  padding: 15px;
  background: linear-gradient(135deg, #43a047, #2e7d32);
  color: #fff;
  border: none;
  border-radius: 10px;
  font-size: 17px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  letter-spacing: 6px;
  transition: all 0.3s;
  box-shadow: 0 4px 14px rgba(46, 125, 50, 0.25);
  margin-top: 8px;
  line-height: 1.4;
}
.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(46, 125, 50, 0.35);
}
.login-btn:active {
  transform: translateY(0);
}
.btn-icon {
  font-size: 20px;
  transition: transform 0.3s;
}
.login-btn:hover .btn-icon {
  transform: translateX(4px);
}

/* ========== 底部链接 ========== */
.card-footer {
  text-align: center;
  padding-top: 18px;
  border-top: 1px solid #e8f5e9;
}
.footer-text {
  color: #999;
  font-size: 14px;
  line-height: 1.5;
}
.register-link {
  color: #43a047;
  font-size: 14px;
  font-weight: 600;
  text-decoration: none;
  margin-left: 4px;
  transition: color 0.2s;
}
.register-link:hover {
  color: #2e7d32;
  text-decoration: underline;
}

/* ========== 版权 ========== */
.copy-right {
  position: absolute;
  bottom: 20px;
  color: rgba(255, 255, 255, 0.6);
  font-size: 12px;
  letter-spacing: 1px;
  z-index: 1;
  line-height: 1.5;
}

/* ========== 响应式 ========== */
@media (max-width: 480px) {
  .login-card {
    width: 92%;
    padding: 32px 24px 24px;
    border-radius: 16px;
  }
  .logo-circle { width: 60px; height: 60px; font-size: 30px; }
  .app-title { font-size: 20px; }
  .login-btn { padding: 12px; font-size: 15px; letter-spacing: 3px; }
}
</style>
