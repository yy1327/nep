<template>
  <div class="grid-layout">
    <!-- 侧边栏 -->
    <div class="grid-sidebar">
      <div class="sidebar-header">
        <div class="sidebar-logo">🏗️</div>
        <div class="sidebar-brand">
          <h3>网格员工作台</h3>
          <span>环保监督网格化管理</span>
        </div>
      </div>
      <div class="sidebar-nav">
        <router-link to="/index/tasks" class="nav-item" :class="{ active: $route.path === '/index/tasks' }">
          <span class="nav-icon">📋</span>
          <span>待检测任务</span>
        </router-link>
        <router-link to="/index/history" class="nav-item" :class="{ active: $route.path === '/index/history' }">
          <span class="nav-icon">✅</span>
          <span>已完成记录</span>
        </router-link>
        <router-link to="/index/settings" class="nav-item" :class="{ active: $route.path === '/index/settings' }">
          <span class="nav-icon">⚙️</span>
          <span>个人设置</span>
        </router-link>
      </div>
      <div class="sidebar-footer" @click="logout">
        <span class="nav-icon">🚪</span>
        <span>退出登录</span>
      </div>
    </div>

    <!-- 右侧内容 -->
    <div class="grid-main">
      <div class="main-header">
        <div class="header-left">
          <span class="header-greeting">👋 {{ gridMember.gmName || '网格员' }}，今日待检测任务</span>
        </div>
        <div class="header-right">
          <span class="header-time">{{ currentTime }}</span>
        </div>
      </div>
      <div class="main-content">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'GridIndex',
  data() {
    return { currentTime: '', timer: null, gridMember: {} }
  },
  mounted() {
    this.loadMember()
    this.updateTime()
    this.timer = setInterval(this.updateTime, 1000)
  },
  beforeUnmount() { if (this.timer) clearInterval(this.timer) },
  methods: {
    loadMember() {
      const raw = sessionStorage.getItem('gridMember')
      if (raw) this.gridMember = JSON.parse(raw)
    },
    updateTime() {
      const d = new Date()
      this.currentTime = `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')} ${String(d.getHours()).padStart(2,'0')}:${String(d.getMinutes()).padStart(2,'0')}`
    },
    logout() {
      sessionStorage.removeItem('gridMember')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.grid-layout { display: flex; height: 100vh; overflow: hidden; }

/* 侧边栏 */
.grid-sidebar {
  width: 232px; background: linear-gradient(180deg, #0a2e28 0%, #0d332d 40%, #134e42 100%);
  display: flex; flex-direction: column;
  box-shadow: 4px 0 20px rgba(0,0,0,0.15);
  position: relative; z-index: 2;
}
.sidebar-header {
  text-align: center; padding: 24px 16px 20px;
  border-bottom: 1px solid rgba(56,239,125,0.08);
  background: rgba(0,0,0,0.1);
}
.sidebar-logo {
  font-size: 32px; margin-bottom: 8px;
  filter: drop-shadow(0 2px 8px rgba(56,239,125,0.2));
  animation: logoFloat 3s ease-in-out infinite;
}
@keyframes logoFloat {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-3px); }
}
.sidebar-brand h3 {
  font-size: 15px; color: #c8f7dc; margin: 0;
  font-weight: 700; letter-spacing: 2px;
  line-height: 1.4;
}
.sidebar-brand span {
  font-size: 10px; color: #4a9e80; letter-spacing: 3px;
  text-transform: uppercase; line-height: 1.4;
  display: block; margin-top: 4px;
}
.sidebar-nav { flex: 1; padding: 12px 8px; }
.nav-item {
  display: flex; align-items: center; gap: 12px;
  padding: 14px 16px; color: #5ab895; text-decoration: none;
  font-size: 14px; border-left: 3px solid transparent;
  border-radius: 0 8px 8px 0;
  transition: all 0.25s ease;
  margin-bottom: 4px;
  line-height: 1.4;
}
.nav-item:hover {
  background: rgba(56,239,125,0.08); color: #c8f7dc;
  transform: translateX(2px);
}
.nav-item.active {
  background: rgba(56,239,125,0.12); color: #38ef7d;
  border-left-color: #38ef7d; font-weight: 600;
  box-shadow: 0 2px 8px rgba(56,239,125,0.08);
}
.nav-icon { font-size: 18px; }
.sidebar-footer {
  display: flex; align-items: center; gap: 12px;
  padding: 14px 20px; color: #5ab895; cursor: pointer;
  border-top: 1px solid rgba(56,239,125,0.08); font-size: 14px;
  transition: all 0.25s ease;
}
.sidebar-footer:hover {
  background: rgba(245,108,108,0.12); color: #f56c6c;
}

/* 主内容 */
.grid-main { flex: 1; display: flex; flex-direction: column; overflow: hidden; }
.main-header {
  height: 56px;
  background: linear-gradient(135deg, #134e42, #0d332d);
  display: flex; align-items: center; justify-content: space-between;
  padding: 0 28px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.12);
  position: relative; z-index: 1;
}
.header-left { display: flex; align-items: center; }
.header-greeting { color: #c8f7dc; font-size: 14px; letter-spacing: 0.5px; line-height: 1.5; }
.header-right { display: flex; align-items: center; }
.header-time {
  color: #38ef7d; font-size: 13px;
  font-variant-numeric: tabular-nums;
  background: rgba(56,239,125,0.06);
  padding: 4px 12px; border-radius: 6px;
}
.main-content {
  flex: 1; padding: 28px;
  background: linear-gradient(180deg, #eef9f3 0%, #f0fdf6 100%);
  overflow-y: auto;
}

/* 响应式 */
@media (max-width: 768px) {
  .grid-sidebar { width: 64px; }
  .sidebar-brand, .nav-item span:not(.nav-icon), .sidebar-footer span:not(.nav-icon) { display: none; }
  .sidebar-header { padding: 16px 8px; }
  .nav-item { justify-content: center; padding: 13px 0; }
  .sidebar-footer { justify-content: center; }
  .main-content { padding: 16px; }
}
</style>
