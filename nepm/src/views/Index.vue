<template>
  <div class="index-container">
    <el-container>
      <el-aside width="220px">
        <div class="logo">
          <h3>环保监督系统</h3>
        </div>
        <el-menu
          :default-active="activeMenu"
          router
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF">
          <el-menu-item index="/index/feedbackList">
            <span>反馈管理</span>
          </el-menu-item>
          <el-menu-item index="/index/statisticsList">
            <span>统计列表</span>
          </el-menu-item>
          <el-sub-menu index="statistics">
            <template slot="title">
              <span>数据统计</span>
            </template>
            <el-menu-item index="/index/provinceItemTotalStatis">按省统计</el-menu-item>
            <el-menu-item index="/index/aqiDistributeTotalStatis">等级分布</el-menu-item>
            <el-menu-item index="/index/aqiTrendTotalStatis">趋势分析</el-menu-item>
            <el-menu-item index="/index/otherTotalStatis">其他统计</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header>
          <div class="header-right">
            <span class="admin-name">{{ adminName }}</span>
            <el-button type="text" @click="logout">退出登录</el-button>
          </div>
        </el-header>
        <el-main>
          <router-view/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
export default {
  name: 'Index',
  computed: {
    activeMenu() {
      return this.$route.path
    },
    adminName() {
      const admin = this.$store.state.admin || JSON.parse(sessionStorage.getItem('admin'))
      return admin ? admin.adminName : '管理员'
    }
  },
  methods: {
    logout() {
      this.$store.dispatch('logout')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.index-container {
  height: 100vh;
}
.el-aside {
  background-color: #304156;
}
.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}
.el-header {
  background: white;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  justify-content: flex-end;
}
.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}
.admin-name {
  color: #666;
}
</style>
