<template>
  <div class="admin-layout">
    <el-container>
      <el-aside width="220px" class="admin-sidebar">
        <div class="sidebar-header">
          <div class="sidebar-logo">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5"/>
            </svg>
          </div>
          <div class="sidebar-brand">
            <h3>环保监督管理中心</h3>
            <span>管理员控制台</span>
          </div>
        </div>
        <el-menu
          :default-active="activeMenu"
          router
          background-color="#132241"
          text-color="#8aaaD0"
          active-text-color="#4a90d9"
          class="admin-menu"
        >
          <el-menu-item index="/index/feedbackList">
            <el-icon><Document /></el-icon>
            <span>反馈管理</span>
          </el-menu-item>
          <el-menu-item index="/index/statisticsList">
            <el-icon><DataLine /></el-icon>
            <span>统计列表</span>
          </el-menu-item>
          <el-sub-menu index="statistics">
            <template #title>
              <el-icon><PieChart /></el-icon>
              <span>数据统计</span>
            </template>
            <el-menu-item index="/index/provinceItemTotalStatis">按省统计</el-menu-item>
            <el-menu-item index="/index/aqiDistributeTotalStatis">等级分布</el-menu-item>
            <el-menu-item index="/index/aqiTrendTotalStatis">趋势分析</el-menu-item>
            <el-menu-item index="/index/otherTotalStatis">综合概览</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>

      <el-container class="admin-main">
        <el-header class="admin-header">
          <div class="header-left">
            <span class="header-greeting">欢迎回来，</span>
            <span class="header-admin-name">{{ adminName }}</span>
          </div>
          <div class="header-right">
            <span class="header-time">{{ currentTime }}</span>
            <el-button type="primary" size="small" plain @click="logout">退出登录</el-button>
          </div>
        </el-header>
        <el-main class="admin-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import { Document, DataLine, PieChart } from '@element-plus/icons-vue'

export default {
  name: "Index",
  components: {
    Document,
    DataLine,
    PieChart
  },
  data() {
    return { currentTime: "", timer: null }
  },
  computed: {
    activeMenu() {
      return this.$route.path
    },
    adminName() {
      const admin = this.$store.state.admin || JSON.parse(sessionStorage.getItem("admin"))
      return admin ? admin.adminName : "管理员"
    }
  },
  mounted() {
    this.updateTime()
    this.timer = setInterval(this.updateTime, 1000)
  },
  beforeUnmount() {
    if (this.timer) clearInterval(this.timer)
  },
  methods: {
    updateTime() {
      const d = new Date()
      this.currentTime = `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')} ${String(d.getHours()).padStart(2,'0')}:${String(d.getMinutes()).padStart(2,'0')}:${String(d.getSeconds()).padStart(2,'0')}`
    },
    logout() {
      this.$store.dispatch("logout")
      this.$router.push("/login")
    }
  }
}
</script>

<style scoped>
.admin-layout { height: 100vh; }

.admin-sidebar {
  background: linear-gradient(180deg, #0a1628 0%, #0f1e38 40%, #132241 100%) !important;
  overflow: hidden;
  box-shadow: 4px 0 20px rgba(0,0,0,0.15);
}

.sidebar-header {
  padding: 24px 16px 20px;
  text-align: center;
  border-bottom: 1px solid rgba(74, 144, 217, 0.08);
  background: rgba(0,0,0,0.1);
}

.sidebar-logo {
  width: 52px;
  height: 52px;
  margin: 0 auto 12px;
  background: linear-gradient(135deg, #2E7D32, #66bb6a);
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 12px rgba(46, 125, 50, 0.3);
}

.sidebar-logo svg {
  width: 28px;
  height: 28px;
  color: white;
}

.sidebar-brand h3 {
  font-size: 15px; color: #dbeafe; margin: 0;
  font-weight: 700; letter-spacing: 1.5px;
  line-height: 1.4;
}

.sidebar-brand span {
  font-size: 10px; color: #4a6580;
  letter-spacing: 2px;
  line-height: 1.4;
  display: block; margin-top: 4px;
}

.admin-menu { border-right: none !important; }

:deep(.el-menu-item),
:deep(.el-sub-menu__title) {
  height: 48px; line-height: 48px;
  font-size: 14px;
  letter-spacing: 0.3px;
}

:deep(.el-menu-item:hover),
:deep(.el-sub-menu__title:hover) {
  background: rgba(74, 144, 217, 0.08) !important;
}

:deep(.el-menu-item.is-active) {
  background: linear-gradient(90deg, rgba(74, 144, 217, 0.15), transparent) !important;
  border-right: 3px solid #4a90d9;
}

:deep(.el-sub-menu .el-menu-item) {
  padding-left: 60px !important;
  height: 42px; line-height: 42px;
  font-size: 13px;
}

.admin-main { background: #f0f4f8; }

.admin-header {
  background: linear-gradient(135deg, #1a3a5c, #132241);
  display: flex; align-items: center; justify-content: space-between;
  padding: 0 28px; height: 56px !important;
  box-shadow: 0 2px 12px rgba(0,0,0,0.12);
}

.header-left { display: flex; align-items: center; gap: 6px; }
.header-greeting { color: #8aaaD0; font-size: 13px; line-height: 1.5; }
.header-admin-name { color: #dbeafe; font-size: 15px; font-weight: 700; line-height: 1.5; }
.header-right { display: flex; align-items: center; gap: 16px; }
.header-time {
  color: #58D9F9; font-size: 13px;
  font-variant-numeric: tabular-nums;
}

.admin-content {
  padding: 24px;
  background: #f0f4f8;
  min-height: calc(100vh - 56px);
}
</style>
