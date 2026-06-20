<template>
  <div class="settings-page">
    <div class="page-header">
      <h2>⚙️ 管理员设置</h2>
      <span class="page-sub">管理管理员账号信息与系统安全</span>
    </div>

    <div class="settings-row">
      <!-- 个人信息卡片 -->
      <el-card class="settings-card">
        <template #header>
          <div class="card-header">
            <span>👤 个人信息</span>
          </div>
        </template>
        <el-form :model="profile" label-width="100px" size="medium">
          <el-form-item label="管理员账号">
            <el-input v-model="profile.adminCode" disabled>
            </el-input>
          </el-form-item>
          <el-form-item label="管理员姓名">
            <el-input v-model="profile.adminName" placeholder="请输入姓名">
            </el-input>
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="profile.adminTel" placeholder="请输入联系电话">
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="updateProfile" :loading="profileLoading">
              💾 保存修改
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 修改密码卡片 -->
      <el-card class="settings-card">
        <template #header>
          <div class="card-header">
            <span>🔒 修改密码</span>
          </div>
        </template>
        <el-form :model="pwdForm" label-width="100px" size="medium">
          <el-form-item label="原密码">
            <el-input v-model="pwdForm.oldPwd" type="password" placeholder="请输入原密码" show-password>
            </el-input>
          </el-form-item>
          <el-form-item label="新密码">
            <el-input v-model="pwdForm.newPwd" type="password" placeholder="请输入新密码（至少6位）" show-password>
            </el-input>
          </el-form-item>
          <el-form-item label="确认密码">
            <el-input v-model="pwdForm.confirmPwd" type="password" placeholder="请再次输入新密码" show-password>
            </el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="warning" @click="changePassword" :loading="pwdLoading">
              🔑 修改密码
            </el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- 系统信息卡片 -->
    <el-card class="system-card">
      <template #header>
        <div class="card-header">
          <span>🖥️ 系统信息</span>
        </div>
      </template>
      <div class="system-info">
        <div class="info-item">
          <span class="info-label">系统名称</span>
          <span class="info-value">东软环保公众监督系统</span>
        </div>
        <div class="info-item">
          <span class="info-label">管理员账号</span>
          <span class="info-value">{{ profile.adminCode }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">最后登录</span>
          <span class="info-value">{{ loginTime }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">系统版本</span>
          <span class="info-value">Version 1.0.0</span>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import api from '@/api'

export default {
  name: 'AdminSettings',
  data() {
    return {
      profile: { adminId: null, adminCode: '', adminName: '', adminTel: '' },
      pwdForm: { oldPwd: '', newPwd: '', confirmPwd: '' },
      profileLoading: false,
      pwdLoading: false,
      loginTime: ''
    }
  },
  created() {
    this.loadAdminInfo()
    this.loginTime = new Date().toLocaleString('zh-CN')
  },
  methods: {
    loadAdminInfo() {
      const admin = this.$store.state.admin || JSON.parse(sessionStorage.getItem('admin'))
      if (admin) {
        this.profile = {
          adminId: admin.adminId,
          adminCode: admin.adminCode || '',
          adminName: admin.adminName || '',
          adminTel: admin.adminTel || ''
        }
      }
    },
    async updateProfile() {
      this.profileLoading = true
      try {
        const res = await api.post('/admins/updateProfile', this.profile)
        if (res.code === 200) {
          this.$message.success('个人信息更新成功')
          // 同步更新本地存储
          const admin = this.$store.state.admin || JSON.parse(sessionStorage.getItem('admin'))
          if (admin) {
            admin.adminName = this.profile.adminName
            admin.adminTel = this.profile.adminTel
            sessionStorage.setItem('admin', JSON.stringify(admin))
            this.$store.commit('setAdmin', admin)
          }
        } else {
          this.$message.error(res.msg)
        }
      } catch (e) {
        this.$message.error('保存失败，请检查网络连接')
      } finally {
        this.profileLoading = false
      }
    },
    async changePassword() {
      if (!this.pwdForm.oldPwd) {
        this.$message.warning('请输入原密码')
        return
      }
      if (!this.pwdForm.newPwd || !this.pwdForm.confirmPwd) {
        this.$message.warning('请输入新密码')
        return
      }
      if (this.pwdForm.newPwd !== this.pwdForm.confirmPwd) {
        this.$message.error('两次输入的新密码不一致')
        return
      }
      if (this.pwdForm.newPwd.length < 6) {
        this.$message.warning('密码不能少于6位')
        return
      }
      this.pwdLoading = true
      try {
        const res = await api.post('/admins/updatePassword', {
          adminId: this.profile.adminId,
          oldPwd: this.pwdForm.oldPwd,
          newPwd: this.pwdForm.newPwd
        })
        if (res.code === 200) {
          this.$message.success('密码修改成功')
          this.pwdForm = { oldPwd: '', newPwd: '', confirmPwd: '' }
        } else {
          this.$message.error(res.msg)
        }
      } catch (e) {
        this.$message.error('修改失败，请检查网络连接')
      } finally {
        this.pwdLoading = false
      }
    }
  }
}
</script>

<style scoped>
.settings-page { padding: 20px; max-width: 900px; animation: fadeInUp 0.4s ease-out; }
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(12px); }
  to { opacity: 1; transform: translateY(0); }
}
.page-header { margin-bottom: 20px; }
.page-header h2 { font-size: 20px; color: #1b5e20; margin: 0 0 6px; font-weight: 700; line-height: 1.4; }
.page-sub { color: #999; font-size: 13px; line-height: 1.5; }

.settings-row { display: flex; gap: 16px; margin-bottom: 16px; }
.settings-card { flex: 1; border-radius: 12px; }
.card-header { font-size: 15px; font-weight: 600; color: #333; }

:deep(.el-form-item__label) { line-height: 1.6; padding-bottom: 4px !important; }
:deep(.el-form-item) { margin-bottom: 20px; }

.system-card { margin-top: 0; border-radius: 12px; }
.system-info { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
.info-item {
  display: flex; flex-direction: column; gap: 6px;
  padding: 16px 20px; background: #f9fdf7; border-radius: 10px;
  transition: all 0.2s ease;
}
.info-item:hover { background: #e8f5e9; }
.info-label { font-size: 12px; color: #999; line-height: 1.4; }
.info-value { font-size: 14px; color: #333; font-weight: 500; line-height: 1.5; }

/* Element UI 输入框美化 */
:deep(.el-input__inner) {
  border-radius: 8px;
}
:deep(.el-button--primary) {
  background: linear-gradient(135deg, #43a047, #2e7d32) !important;
  border: none !important;
  border-radius: 8px;
  font-weight: 600;
}
:deep(.el-button--primary:hover) {
  background: linear-gradient(135deg, #66bb6a, #43a047) !important;
}
:deep(.el-button--warning) {
  background: linear-gradient(135deg, #e6a23c, #d48806) !important;
  border: none !important;
  border-radius: 8px;
  color: #fff !important;
  font-weight: 600;
}
:deep(.el-button--warning:hover) {
  background: linear-gradient(135deg, #f0b955, #e6a23c) !important;
}

@media (max-width: 768px) {
  .settings-page { max-width: 100%; }
  .settings-row { flex-direction: column; }
  .system-info { grid-template-columns: 1fr; }
}
</style>
