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
  beforeDestroy() { if (this.timer) clearInterval(this.timer) },
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
  width: 220px; background: linear-gradient(180deg, #0d332d, #134e42);
  display: flex; flex-direction: column;
}
.sidebar-header { text-align: center; padding: 20px 16px; border-bottom: 1px solid rgba(56,239,125,0.1); }
.sidebar-logo { font-size: 28px; margin-bottom: 6px; }
.sidebar-brand h3 { font-size: 14px; color: #c8f7dc; margin: 0; font-weight: 600; letter-spacing: 1px; }
.sidebar-brand span { font-size: 10px; color: #5ab895; letter-spacing: 2px; }
.sidebar-nav { flex: 1; padding: 8px 0; }
.nav-item {
  display: flex; align-items: center; gap: 12px;
  padding: 13px 20px; color: #5ab895; text-decoration: none;
  font-size: 14px; border-left: 3px solid transparent;
  transition: all 0.2s;
}
.nav-item:hover { background: rgba(56,239,125,0.08); color: #c8f7dc; }
.nav-item.active {
  background: rgba(56,239,125,0.12); color: #38ef7d;
  border-left-color: #38ef7d; font-weight: 600;
}
.nav-icon { font-size: 18px; }
.sidebar-footer {
  display: flex; align-items: center; gap: 12px;
  padding: 14px 20px; color: #5ab895; cursor: pointer;
  border-top: 1px solid rgba(56,239,125,0.1); font-size: 14px;
  transition: all 0.2s;
}
.sidebar-footer:hover { background: rgba(245,108,108,0.15); color: #f56c6c; }

/* 主内容 */
.grid-main { flex: 1; display: flex; flex-direction: column; overflow: hidden; }
.main-header {
  height: 52px; background: linear-gradient(135deg, #134e42, #0d332d);
  display: flex; align-items: center; justify-content: space-between;
  padding: 0 24px; box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}
.header-left { display: flex; align-items: center; }
.header-greeting { color: #c8f7dc; font-size: 14px; }
.header-right { display: flex; align-items: center; }
.header-time { color: #5ab895; font-size: 13px; }
.main-content { flex: 1; padding: 20px; background: #f0fdf6; overflow-y: auto; }
</style>
