<template>
  <div class="admin-login-container">
    <div class="bg-grid">
      <div class="grid-line" v-for="i in 5" :key="'h'+i" :style="{ top: (i * 20) + '%' }"></div>
      <div class="grid-line vertical" v-for="i in 5" :key="'v'+i" :style="{ left: (i * 20) + '%' }"></div>
    </div>

    <div class="glow glow-top"></div>
    <div class="glow glow-bottom"></div>

    <div class="particles">
      <span v-for="i in 12" :key="i" class="particle"
        :style="{ left: (i * 8) + '%', animationDelay: (i * 0.7) + 's', animationDuration: (4 + i % 3) + 's' }">&#10022;</span>
    </div>

    <div class="admin-login-card">
      <div class="card-brand">
        <div class="brand-content">
          <div class="shield-icon">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5"/>
            </svg>
          </div>
          <h1 class="brand-title">环保监督管理中心</h1>
          <p class="brand-desc">管理员专属后台系统</p>
          <div class="brand-features">
            <div class="feature-item">
              <span class="feature-dot"></span> 反馈审核管理
            </div>
            <div class="feature-item">
              <span class="feature-dot"></span> 网格员调度
            </div>
            <div class="feature-item">
              <span class="feature-dot"></span> 数据统计分析
            </div>
            <div class="feature-item">
              <span class="feature-dot"></span> 空气质量监控
            </div>
          </div>
        </div>
      </div>

      <div class="card-form">
        <div class="form-header">
          <h2>管理员登录</h2>
          <p>请使用管理员账号登录系统</p>
        </div>
        <el-form :model="form" @submit.prevent="handleLogin" class="login-form">
          <el-form-item>
            <el-input
              v-model="form.adminCode"
              placeholder="请输入管理员账号"
              :prefix-icon="User"
              size="large"
              class="custom-input"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-input
              v-model="form.adminPwd"
              type="password"
              placeholder="请输入登录密码"
              :prefix-icon="Lock"
              size="large"
              class="custom-input"
              show-password
            ></el-input>
          </el-form-item>
          <el-button
            type="primary"
            native-type="submit"
            :loading="loading"
            class="submit-btn"
            size="large"
          >
            <span v-if="!loading">登 录 系 统</span>
          </el-button>
        </el-form>
      </div>
    </div>

    <div class="admin-footer">
      <span>东软环保公众监督系统 · 管理员控制台 · Version 2.0</span>
    </div>
  </div>
</template>

<script>
import { User, Lock } from '@element-plus/icons-vue'
import api from '@/api'

export default {
  name: 'Login',
  // eslint-disable-next-line vue/no-unused-components
  components: { User, Lock },
  data() {
    return {
      form: {
        adminCode: '',
        adminPwd: ''
      },
      loading: false
    }
  },
  methods: {
    async handleLogin() {
      this.loading = true
      try {
        const res = await api.post('/admins/getAdminsByCode', this.form)
        if (res.code === 200) {
          this.$store.dispatch('login', res.data)
          this.$router.push('/index/feedbackList')
        } else {
          this.$message.error(res.msg)
        }
      } catch (error) {
        this.$message.error('登录失败，请检查网络连接')
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.admin-login-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(160deg, #0a1628 0%, #132241 30%, #1a2d4a 60%, #0f1e38 100%);
  position: relative;
  overflow: hidden;
  font-family: "Microsoft YaHei", "PingFang SC", sans-serif;
}

.bg-grid {
  position: absolute;
  inset: 0;
  opacity: 0.04;
}

.grid-line {
  position: absolute;
  left: 0; right: 0;
  height: 1px;
  background: #4a90d9;
}

.grid-line.vertical {
  top: 0; bottom: 0;
  width: 1px;
  height: auto;
  left: auto;
}

.glow {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
}

.glow-top {
  width: 500px; height: 500px;
  background: radial-gradient(circle, rgba(74, 144, 217, 0.12) 0%, transparent 70%);
  top: -150px; right: -100px;
}

.glow-bottom {
  width: 400px; height: 400px;
  background: radial-gradient(circle, rgba(41, 128, 185, 0.1) 0%, transparent 70%);
  bottom: -120px; left: -80px;
}

.particles {
  position: absolute;
  inset: 0;
  pointer-events: none;
}

.particle {
  position: absolute;
  top: -20px;
  color: rgba(74, 144, 217, 0.3);
  font-size: 8px;
  animation: particleFall linear infinite;
}

@keyframes particleFall {
  0% { transform: translateY(-20px) rotate(0deg); opacity: 0; }
  10% { opacity: 0.6; }
  90% { opacity: 0.3; }
  100% { transform: translateY(105vh) rotate(360deg); opacity: 0; }
}

.admin-login-card {
  display: flex;
  width: 820px;
  min-height: 480px;
  background: rgba(20, 35, 60, 0.85);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.4), 0 0 0 1px rgba(74, 144, 217, 0.1);
  backdrop-filter: blur(20px);
  position: relative;
  z-index: 1;
  animation: cardSlideUp 0.7s ease-out;
}

@keyframes cardSlideUp {
  from { opacity: 0; transform: translateY(40px); }
  to { opacity: 1; transform: translateY(0); }
}

.card-brand {
  flex: 1;
  background: linear-gradient(160deg, #1a3a5c 0%, #152b4a 50%, #1e3d5c 100%);
  position: relative;
  padding: 54px 40px;
  display: flex;
  align-items: center;
  overflow: hidden;
}

.card-brand::before {
  content: "";
  position: absolute;
  inset: 0;
  background: radial-gradient(circle at 30% 50%, rgba(74, 144, 217, 0.08) 0%, transparent 60%);
}

.brand-content {
  position: relative;
  z-index: 1;
}

.shield-icon {
  width: 64px; height: 64px;
  background: linear-gradient(135deg, rgba(46, 125, 50, 0.3), rgba(129, 199, 132, 0.1));
  border: 1.5px solid rgba(129, 199, 132, 0.4);
  border-radius: 16px;
  display: flex; align-items: center; justify-content: center;
  margin-bottom: 24px;
}

.shield-icon svg {
  width: 32px;
  height: 32px;
  color: #81C784;
}

.brand-title {
  font-size: 22px; font-weight: 700;
  color: #dbeafe;
  letter-spacing: 2px;
  margin-bottom: 10px;
  line-height: 1.4;
}

.brand-desc {
  font-size: 13px; color: #7a9ec8;
  letter-spacing: 1px;
  margin-bottom: 32px;
  line-height: 1.5;
}

.brand-features {
  display: flex; flex-direction: column; gap: 14px;
}

.feature-item {
  font-size: 13px; color: #8aaaD0;
  display: flex; align-items: center; gap: 12px;
  line-height: 1.5;
}

.feature-dot {
  width: 6px; height: 6px;
  background: #81C784;
  border-radius: 50%;
  flex-shrink: 0;
}

.card-form {
  flex: 1;
  padding: 52px 44px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.form-header {
  margin-bottom: 36px;
}

.form-header h2 {
  font-size: 22px; font-weight: 700;
  color: #dbeafe;
  margin-bottom: 10px;
  letter-spacing: 2px;
}

.form-header p {
  font-size: 13px; color: #6b8db5;
  line-height: 1.5;
}

.login-form {
  width: 100%;
}

:deep(.custom-input .el-input__wrapper) {
  background: rgba(74, 144, 217, 0.08) !important;
  border: 1.5px solid rgba(74, 144, 217, 0.2) !important;
  border-radius: 8px !important;
  box-shadow: none !important;
  padding: 0 14px !important;
}

:deep(.custom-input .el-input__wrapper.is-focus) {
  border-color: #81C784 !important;
  box-shadow: 0 0 0 3px rgba(129, 199, 132, 0.1) !important;
}

:deep(.custom-input .el-input__inner) {
  color: #dbeafe !important;
  height: 46px !important;
  font-size: 14px !important;
  line-height: 46px !important;
  background: transparent !important;
}

:deep(.custom-input .el-input__inner::placeholder) {
  color: #4a6580 !important;
}

:deep(.custom-input .el-input__prefix) {
  color: #81C784 !important;
  display: flex !important;
  align-items: center !important;
}

.submit-btn {
  width: 100% !important;
  height: 50px !important;
  border-radius: 8px !important;
  font-size: 16px !important;
  font-weight: 600 !important;
  letter-spacing: 4px !important;
  margin-top: 10px !important;
  background: linear-gradient(135deg, #2E7D32, #1B5E20) !important;
  border: none !important;
  transition: all 0.3s !important;
  line-height: 1.4;
}

.submit-btn:hover {
  background: linear-gradient(135deg, #43A047, #2E7D32) !important;
  transform: translateY(-1px);
  box-shadow: 0 4px 16px rgba(46, 125, 50, 0.4) !important;
}

.submit-btn:active {
  transform: translateY(0);
}

.admin-footer {
  position: absolute;
  bottom: 20px;
  color: rgba(107, 141, 181, 0.5);
  font-size: 12px;
  letter-spacing: 1px;
  z-index: 1;
  line-height: 1.5;
}

@media (max-width: 860px) {
  .admin-login-card {
    width: 92%;
    flex-direction: column;
    min-height: auto;
  }
  .card-brand {
    padding: 30px 24px;
  }
  .brand-features {
    display: none;
  }
  .card-form {
    padding: 30px 28px;
  }
  .admin-footer { position: relative; margin-top: 20px; }
}
@media (max-width: 480px) {
  .admin-login-card { width: 96%; border-radius: 12px; }
  .brand-title { font-size: 18px; }
  .form-header h2 { font-size: 18px; }
  .submit-btn { height: 44px !important; font-size: 15px !important; letter-spacing: 2px !important; }
}
</style>
