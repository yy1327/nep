<template>
  <div class="settings-page">
    <h2>⚙️ 个人设置</h2>
    <div class="settings-row">
      <div class="card">
        <h3>👷 个人信息</h3>
        <div class="form-item">
          <label>工号</label>
          <input v-model="profile.gmCode" disabled />
        </div>
        <div class="form-item">
          <label>姓名</label>
          <input v-model="profile.gmName" placeholder="请输入姓名" />
        </div>
        <div class="form-item">
          <label>联系电话</label>
          <input v-model="profile.gmTel" placeholder="请输入联系电话" />
        </div>
        <button class="btn-save" @click="updateProfile">💾 保存修改</button>
      </div>
      <div class="card">
        <h3>🔒 修改密码</h3>
        <div class="form-item">
          <label>原密码</label>
          <input v-model="pwd.oldPwd" type="password" placeholder="请输入原密码" />
        </div>
        <div class="form-item">
          <label>新密码</label>
          <input v-model="pwd.newPwd" type="password" placeholder="请输入新密码（至少6位）" />
        </div>
        <div class="form-item">
          <label>确认密码</label>
          <input v-model="pwd.confirmPwd" type="password" placeholder="请再次输入新密码" />
        </div>
        <button class="btn-pwd" @click="changePassword">🔑 修改密码</button>
      </div>
    </div>
    <div class="card sys-card">
      <h3>🖥️ 系统信息</h3>
      <div class="sys-grid">
        <div class="sys-item"><span class="sys-label">姓名</span><span>{{ profile.gmName }}</span></div>
        <div class="sys-item"><span class="sys-label">工号</span><span>{{ profile.gmCode }}</span></div>
        <div class="sys-item"><span class="sys-label">所属省份ID</span><span>{{ profile.gmProvinceId }}</span></div>
        <div class="sys-item"><span class="sys-label">所属城市ID</span><span>{{ profile.gmCityId }}</span></div>
      </div>
    </div>
  </div>
</template>

<script>
import api from '@/api'

export default {
  name: 'GridSettings',
  data() {
    return {
      profile: { gmId: null, gmCode: '', gmName: '', gmTel: '', gmProvinceId: '', gmCityId: '' },
      pwd: { oldPwd: '', newPwd: '', confirmPwd: '' }
    }
  },
  created() { this.loadInfo() },
  methods: {
    loadInfo() {
      const raw = sessionStorage.getItem('gridMember')
      if (raw) {
        const m = JSON.parse(raw)
        this.profile = {
          gmId: m.gmId, gmCode: m.gmCode || '',
          gmName: m.gmName || '', gmTel: m.gmTel || '',
          gmProvinceId: m.gmProvinceId, gmCityId: m.gmCityId
        }
      }
    },
    async updateProfile() {
      try {
        const res = await api.post('/gridMember/updateProfile', this.profile)
        if (res.code === 200) {
          alert('保存成功')
          const raw = sessionStorage.getItem('gridMember')
          if (raw) {
            const m = JSON.parse(raw)
            m.gmName = this.profile.gmName
            m.gmTel = this.profile.gmTel
            sessionStorage.setItem('gridMember', JSON.stringify(m))
          }
        } else { alert(res.msg) }
      } catch (e) { alert('保存失败') }
    },
    async changePassword() {
      if (!this.pwd.oldPwd || !this.pwd.newPwd) { alert('请输入密码'); return }
      if (this.pwd.newPwd !== this.pwd.confirmPwd) { alert('两次密码不一致'); return }
      if (this.pwd.newPwd.length < 6) { alert('密码不能少于6位'); return }
      try {
        const res = await api.post('/gridMember/updatePassword', {
          gmId: this.profile.gmId, oldPwd: this.pwd.oldPwd, newPwd: this.pwd.newPwd
        })
        if (res.code === 200) {
          alert('密码修改成功')
          this.pwd = { oldPwd: '', newPwd: '', confirmPwd: '' }
        } else { alert(res.msg) }
      } catch (e) { alert('修改失败') }
    }
  }
}
</script>

<style scoped>
.settings-page { max-width: 800px; }
h2 { font-size: 18px; color: #166953; margin-bottom: 16px; }
.settings-row { display: flex; gap: 16px; margin-bottom: 16px; }
.card {
  flex: 1; background: #fff; border-radius: 12px;
  padding: 24px; box-shadow: 0 2px 10px rgba(0,0,0,0.05);
  border-top: 4px solid #11998e;
}
.card h3 { font-size: 16px; color: #166953; margin-bottom: 16px; padding-bottom: 10px; border-bottom: 1px solid #e8f5ec; }
.form-item { margin-bottom: 14px; }
.form-item label { display: block; font-size: 13px; color: #666; font-weight: 600; margin-bottom: 4px; }
.form-item input {
  width: 100%; padding: 10px 12px; border: 1.5px solid #d4ede3;
  border-radius: 8px; font-size: 14px; color: #333;
  transition: all 0.2s; background: #f9fdf7;
}
.form-item input:focus { outline: none; border-color: #38ef7d; box-shadow: 0 0 0 3px rgba(56,239,125,0.1); }
.form-item input:disabled { background: #eee; color: #999; }
.btn-save {
  width: 100%; padding: 12px; border: none; border-radius: 8px;
  background: linear-gradient(135deg, #11998e, #0d7370);
  color: #fff; font-size: 15px; font-weight: 600; cursor: pointer;
  margin-top: 6px;
}
.btn-save:hover { opacity: 0.9; }
.btn-pwd {
  width: 100%; padding: 12px; border: none; border-radius: 8px;
  background: linear-gradient(135deg, #e6a23c, #d48806);
  color: #fff; font-size: 15px; font-weight: 600; cursor: pointer;
  margin-top: 6px;
}
.btn-pwd:hover { opacity: 0.9; }
.sys-card { margin-top: 0; }
.sys-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
.sys-item { padding: 10px 14px; background: #f0fdf6; border-radius: 8px; display: flex; flex-direction: column; gap: 2px; }
.sys-label { font-size: 11px; color: #999; }
</style>
