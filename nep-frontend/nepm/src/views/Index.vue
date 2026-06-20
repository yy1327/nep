<template>
  <div class="admin-layout">
    <el-container>
      <!-- 侧边栏 -->
      <el-aside width="220px" class="admin-sidebar">
        <div class="sidebar-header">
          <div class="sidebar-logo">🛡️</div>
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
            <i class="el-icon-s-order"></i>
            <span>反馈管理</span>
          </el-menu-item>
          <el-menu-item index="/index/statisticsList">
            <i class="el-icon-s-data"></i>
            <span>统计列表</span>
          </el-menu-item>
          <el-sub-menu index="statistics">
            <template slot="title">
              <i class="el-icon-pie-chart"></i>
              <span>数据统计</span>
            </template>
            <el-menu-item index="/index/provinceItemTotalStatis">按省统计</el-menu-item>
            <el-menu-item index="/index/aqiDistributeTotalStatis">等级分布</el-menu-item>
            <el-menu-item index="/index/aqiTrendTotalStatis">趋势分析</el-menu-item>
            <el-menu-item index="/index/otherTotalStatis">综合概览</el-menu-item>
          </el-sub-menu>
          <el-menu-item index="/index/adminSettings">
            <i class="el-icon-setting"></i>
            <span>管理员设置</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- 右侧主体 -->
      <el-container class="admin-main">
        <el-header class="admin-header">
          <div class="header-left">
            <span class="header-greeting">👋 欢迎回来，</span>
            <span class="header-admin-name">{{ adminName }}</span>
          </div>
          <div class="header-right">
            <span class="header-time">{{ currentTime }}</span>
            <el-button type="primary" size="small" plain @click="logout">退出登录</el-button>
          </div>
        </el-header>
        <el-main class="admin-content">
          <keep-alive>
            <router-view v-if="$route.meta.keepAlive" />
          </keep-alive>
          <router-view v-if="!$route.meta.keepAlive" />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "Index",
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
  beforeDestroy() {
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

/* ========== 侧边栏 ========== */
.admin-sidebar {
  background: linear-gradient(180deg, #0f1e38 0%, #132241 100%) !important;
  overflow: hidden;
}
.sidebar-header {
  padding: 20px 16px;
  text-align: center;
  border-bottom: 1px solid rgba(74, 144, 217, 0.1);
}
.sidebar-logo { font-size: 32px; margin-bottom: 8px; }
.sidebar-brand h3 {
  font-size: 15px; color: #dbeafe; margin: 0;
  font-weight: 600; letter-spacing: 1px;
}
.sidebar-brand span {
  font-size: 11px; color: #4a6580;
  letter-spacing: 2px;
}

/* ========== 菜单 ========== */
.admin-menu { border-right: none !important; }
.admin-menu >>> .el-menu-item,
.admin-menu >>> .el-submenu__title {
  height: 48px; line-height: 48px;
  font-size: 14px;
}
.admin-menu >>> .el-menu-item:hover,
.admin-menu >>> .el-submenu__title:hover {
  background: rgba(74, 144, 217, 0.08) !important;
}
.admin-menu >>> .el-menu-item.is-active {
  background: linear-gradient(90deg, rgba(74, 144, 217, 0.15), transparent) !important;
  border-right: 3px solid #4a90d9;
}
.admin-menu >>> .el-submenu .el-menu-item {
  padding-left: 60px !important;
  height: 42px; line-height: 42px;
  font-size: 13px;
}

/* ========== 顶部栏 ========== */
.admin-main { background: #f0f4f8; }
.admin-header {
  background: linear-gradient(135deg, #1a3a5c, #132241);
  display: flex; align-items: center; justify-content: space-between;
  padding: 0 24px; height: 56px !important;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}
.header-left { display: flex; align-items: center; gap: 6px; }
.header-greeting { color: #8aaaD0; font-size: 13px; }
.header-admin-name { color: #dbeafe; font-size: 15px; font-weight: 600; }
.header-right { display: flex; align-items: center; gap: 16px; }
.header-time { color: #8aaaD0; font-size: 13px; }

/* ========== 内容区 ========== */
.admin-content {
  padding: 20px;
  background: #f0f4f8;
  min-height: calc(100vh - 56px);
}
</style>
