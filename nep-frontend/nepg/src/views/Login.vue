<template>
  <div class="grid-login-container">
    <!-- 网格背景 -->
    <div class="grid-pattern">
      <div class="grid-cell" v-for="i in 42" :key="i"></div>
    </div>

    <!-- 光晕 -->
    <div class="glow glow-left"></div>
    <div class="glow glow-right"></div>

    <!-- 登录卡片 -->
    <div class="grid-login-card">
      <!-- 左侧品牌区 -->
      <div class="card-brand">
        <div class="brand-content">
          <div class="hexagon-icon">
            <div class="hex-inner">🏗️</div>
          </div>
          <h1 class="brand-title">网格员工作台</h1>
          <p class="brand-desc">环保监督网格化管理系统</p>
          <div class="brand-features">
            <div class="feature-item">
              <div class="feature-hex">🔍</div>
              <div class="feature-text">
                <strong>实地检测</strong>
                <span>现场采集空气质量数据</span>
              </div>
            </div>
            <div class="feature-item">
              <div class="feature-hex">📋</div>
              <div class="feature-text">
                <strong>任务处理</strong>
                <span>接收并完成指派检测任务</span>
              </div>
            </div>
            <div class="feature-item">
              <div class="feature-hex">📊</div>
              <div class="feature-text">
                <strong>数据上报</strong>
                <span>提交实测数据与检测结果</span>
              </div>
            </div>
          </div>
        </div>
        <div class="brand-bg-dots">
          <span class="bg-dot" v-for="i in 20" :key="i"
            :style="{ left: ((i-1) % 5) * 22 + 6 + '%', top: (Math.floor((i-1)/5) * 28 + 8) + '%' }"></span>
        </div>
      </div>

      <!-- 右侧登录表单 -->
      <div class="card-form">
        <div class="form-header">
          <h2>网格员登录</h2>
          <p>请输入工号和密码登录系统</p>
        </div>
        <form @submit.prevent="handleLogin" class="login-form">
          <div class="input-group">
            <span class="input-icon">👷</span>
            <input
              type="text"
              v-model="form.gmCode"
              placeholder="请输入网格员工号"
              required
              class="input-field"
            />
          </div>
          <div class="input-group">
            <span class="input-icon">🔑</span>
            <input
              type="password"
              v-model="form.gmPwd"
              placeholder="请输入登录密码"
              required
              class="input-field"
            />
          </div>
          <button type="submit" class="login-btn">
            <span>登 录 系 统</span>
            <span class="btn-arrow">→</span>
          </button>
        </form>
      </div>
    </div>

    <!-- 底部 -->
    <div class="grid-footer">
      <span>🔧 东软环保公众监督系统 · 网格员工作台 · Version 1.0</span>
    </div>
  </div>
</template>

<script>
import api from '@/api'

export default {
  name: 'Login',
  data() {
    return {
      form: { gmCode: '', gmPwd: '' }
    }
  },
  methods: {
    async handleLogin() {
      try {
        const res = await api.post('/gridMember/getGridMemberByCodeByPass', this.form)
        if (res.code === 200) {
          sessionStorage.setItem('gridMember', JSON.stringify(res.data))
          this.$router.push('/index/tasks')
        } else {
          alert(res.msg)
        }
      } catch (error) {
        alert('登录失败，请检查网络连接')
      }
    }
  }
}
</script>

<style scoped>
/* ========== 容器 ========== */
.grid-login-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(160deg, #0d332d 0%, #134e42 30%, #166953 60%, #0f3d35 100%);
  position: relative;
  overflow: hidden;
  font-family: "Microsoft YaHei", "PingFang SC", sans-serif;
}

/* ========== 网格背景图案 ========== */
.grid-pattern {
  position: absolute;
  inset: 0;
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  opacity: 0.04;
}
.grid-cell {
  border: 1px solid #38ef7d;
  aspect-ratio: 1;
}

/* ========== 光晕 ========== */
.glow {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
}
.glow-left {
  width: 400px; height: 400px;
  background: radial-gradient(circle, rgba(56, 239, 125, 0.1) 0%, transparent 70%);
  bottom: -100px; left: -80px;
}
.glow-right {
  width: 500px; height: 500px;
  background: radial-gradient(circle, rgba(17, 153, 142, 0.12) 0%, transparent 70%);
  top: -120px; right: -100px;
}

/* ========== 卡片主体 ========== */
.grid-login-card {
  display: flex;
  width: 820px;
  min-height: 480px;
  background: rgba(18, 55, 46, 0.85);
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.4), 0 0 0 1px rgba(56, 239, 125, 0.1);
  backdrop-filter: blur(20px);
  position: relative;
  z-index: 1;
  animation: cardSlideUp 0.7s ease-out;
}
@keyframes cardSlideUp {
  from { opacity: 0; transform: translateY(40px); }
  to { opacity: 1; transform: translateY(0); }
}

/* ========== 左侧品牌区 ========== */
.card-brand {
  flex: 1;
  background: linear-gradient(160deg, #134e42 0%, #0d332d 50%, #166953 100%);
  position: relative;
  padding: 44px 36px;
  display: flex;
  align-items: center;
  overflow: hidden;
}
.card-brand::before {
  content: "";
  position: absolute;
  inset: 0;
  background: radial-gradient(circle at 40% 40%, rgba(56, 239, 125, 0.06) 0%, transparent 50%);
}
.brand-content { position: relative; z-index: 1; }
.brand-bg-dots {
  position: absolute;
  inset: 0;
  opacity: 0.05;
}
.bg-dot {
  position: absolute;
  width: 5px; height: 5px;
  background: #38ef7d;
  border-radius: 50%;
}

/* 六边形图标 */
.hexagon-icon {
  width: 68px; height: 68px;
  background: linear-gradient(135deg, rgba(56, 239, 125, 0.2), rgba(17, 153, 142, 0.1));
  border: 2px solid rgba(56, 239, 125, 0.3);
  border-radius: 16px;
  display: flex; align-items: center; justify-content: center;
  margin-bottom: 24px;
  clip-path: polygon(30% 0%, 70% 0%, 100% 30%, 100% 70%, 70% 100%, 30% 100%, 0% 70%, 0% 30%);
}
.hex-inner {
  font-size: 28px;
  clip-path: polygon(30% 0%, 70% 0%, 100% 30%, 100% 70%, 70% 100%, 30% 100%, 0% 70%, 0% 30%);
  width: 100%; height: 100%;
  display: flex; align-items: center; justify-content: center;
  background: rgba(255,255,255,0.05);
}
.brand-title {
  font-size: 22px; font-weight: 700;
  color: #c8f7dc;
  letter-spacing: 2px;
  margin-bottom: 8px;
}
.brand-desc {
  font-size: 13px; color: #5ab895;
  letter-spacing: 1px;
  margin-bottom: 28px;
}
.brand-features { display: flex; flex-direction: column; gap: 14px; }
.feature-item { display: flex; align-items: flex-start; gap: 12px; }
.feature-hex {
  width: 36px; height: 36px;
  background: rgba(56, 239, 125, 0.1);
  border: 1px solid rgba(56, 239, 125, 0.2);
  border-radius: 8px;
  display: flex; align-items: center; justify-content: center;
  font-size: 16px; flex-shrink: 0;
}
.feature-text { display: flex; flex-direction: column; gap: 2px; }
.feature-text strong { font-size: 13px; color: #c8f7dc; }
.feature-text span { font-size: 11px; color: #5ab895; }

/* ========== 右侧表单区 ========== */
.card-form {
  flex: 1;
  padding: 50px 40px;
  display: flex; flex-direction: column; justify-content: center;
}
.form-header { margin-bottom: 32px; }
.form-header h2 {
  font-size: 22px; font-weight: 700;
  color: #c8f7dc;
  margin-bottom: 8px;
  letter-spacing: 2px;
}
.form-header p {
  font-size: 13px; color: #5ab895;
}
.login-form { width: 100%; }

/* ========== 输入框 ========== */
.input-group {
  position: relative;
  margin-bottom: 20px;
}
.input-icon {
  position: absolute;
  left: 14px; top: 50%; transform: translateY(-50%);
  font-size: 18px; z-index: 1;
  opacity: 0.5; transition: opacity 0.2s;
}
.input-group:focus-within .input-icon { opacity: 1; }
.input-field {
  width: 100%;
  padding: 14px 14px 14px 44px;
  border: 2px solid rgba(56, 239, 125, 0.2);
  border-radius: 10px;
  font-size: 15px;
  color: #c8f7dc;
  background: rgba(56, 239, 125, 0.05);
  transition: all 0.3s;
  box-sizing: border-box;
}
.input-field::placeholder { color: #3d7a65; }
.input-field:focus {
  outline: none;
  border-color: #38ef7d;
  background: rgba(56, 239, 125, 0.1);
  box-shadow: 0 0 0 4px rgba(56, 239, 125, 0.06);
}

/* ========== 按钮 ========== */
.login-btn {
  width: 100%;
  padding: 14px;
  background: linear-gradient(135deg, #11998e, #0d7370);
  color: #fff;
  border: none;
  border-radius: 10px;
  font-size: 17px;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  letter-spacing: 6px;
  transition: all 0.3s;
  box-shadow: 0 4px 14px rgba(17, 153, 142, 0.3);
  margin-top: 8px;
}
.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(17, 153, 142, 0.4);
  background: linear-gradient(135deg, #38ef7d, #11998e);
}
.login-btn:active { transform: translateY(0); }
.btn-arrow {
  font-size: 20px;
  transition: transform 0.3s;
}
.login-btn:hover .btn-arrow { transform: translateX(4px); }

/* ========== 底部 ========== */
.grid-footer {
  position: absolute;
  bottom: 20px;
  color: rgba(90, 184, 149, 0.4);
  font-size: 12px;
  letter-spacing: 1px;
  z-index: 1;
}

/* ========== 响应式 ========== */
@media (max-width: 860px) {
  .grid-login-card {
    width: 92%;
    flex-direction: column;
  }
  .card-brand { padding: 30px 24px; }
  .brand-features { display: none; }
  .card-form { padding: 30px 28px; }
}
</style>
