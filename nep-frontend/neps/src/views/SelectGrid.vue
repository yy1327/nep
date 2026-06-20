<template>
  <div class="main-container">
    <!-- ========== 顶部导航栏 ========== -->
    <div class="topbar">
      <div class="topbar-left">
        <span class="logo-icon">🌳</span>
        <span class="logo-text">环保公众监督平台</span>
      </div>
      <div class="topbar-center">
        <span class="topbar-slogan">守护蓝天绿水，共建美丽家园</span>
      </div>
      <div class="topbar-right">
        <span class="user-name">👤 {{ userInfo.supervisorName || '用户' }}</span>
        <button class="logout-btn" @click="logout">退出</button>
      </div>
    </div>

    <!-- ========== 装饰带 ========== -->
    <div class="nature-strip">
      <span class="leaf" v-for="i in 20" :key="i" :style="{ left: (i * 5 - 2.5) + '%', animationDelay: (i * 0.3) + 's' }">🍃</span>
    </div>

    <div class="body-wrap">
      <!-- ========== 左侧菜单 ========== -->
      <div class="sidebar">
        <div class="sidebar-header">功能导航</div>
        <div
          v-for="tab in tabs"
          :key="tab.key"
          :class="['menu-item', { active: activeTab === tab.key }]"
          @click="switchTab(tab.key)"
        >
          <span class="menu-icon">{{ tab.icon }}</span>
          <div class="menu-text">
            <span class="menu-label">{{ tab.label }}</span>
            <span class="menu-desc">{{ tab.desc }}</span>
          </div>
          <span v-if="activeTab === tab.key" class="menu-arrow">▸</span>
        </div>
      </div>

      <!-- ========== 右侧内容区 ========== -->
      <div class="content">

        <!-- Tab1: 选择地区申报 -->
        <div v-show="activeTab === 'report'" class="tab-panel">
          <div v-if="reportStep === 1" class="card">
            <div class="card-icon">📍</div>
            <h3 class="card-title">选择申报地区</h3>
            <p class="card-hint">选择您发现环境问题所在的省份和城市</p>
            <div class="form-item">
              <label>🌏 省份</label>
              <select v-model="provinceId" @change="loadCities">
                <option value="">请选择省份</option>
                <option v-for="p in provinces" :key="p.provinceId" :value="p.provinceId">{{ p.provinceName }}</option>
              </select>
            </div>
            <div class="form-item">
              <label>🏙️ 城市</label>
              <select v-model="cityId">
                <option value="">请选择城市</option>
                <option v-for="c in cities" :key="c.cityId" :value="c.cityId">{{ c.cityName }}</option>
              </select>
            </div>
            <button class="btn-primary" :disabled="!provinceId || !cityId" @click="goReportStep2">
              <span>下一步：填写反馈</span>
              <span class="btn-arrow">→</span>
            </button>
          </div>

          <div v-if="reportStep === 2" class="card">
            <div class="card-title-row">
              <button class="btn-text" @click="reportStep = 1">← 返回重选地区</button>
              <span class="step-badge">步骤 2 / 2</span>
            </div>
            <div class="card-icon">✍️</div>
            <h3 class="card-title">填写反馈信息</h3>
            <p class="card-hint">详细描述环境问题，帮助我们快速处理</p>
            <div class="form-item">
              <label>📂 反馈类型</label>
              <select v-model="feedback.afType">
                <option value="">请选择反馈类型</option>
                <option value="01">空气有异味</option>
                <option value="02">雾霾严重</option>
                <option value="03">工业排放</option>
                <option value="04">其他</option>
              </select>
            </div>
            <div class="form-item">
              <label>📊 预估AQI等级</label>
              <select v-model="feedback.afLevel">
                <option value="">请选择AQI等级</option>
                <option v-for="a in aqiList" :key="a.aqiId" :value="a.aqiLevel">{{ a.aqiLevel }}</option>
              </select>
            </div>
            <div class="form-item">
              <label>📝 问题描述</label>
              <textarea v-model="feedback.afDesc" rows="4" placeholder="请详细描述环境问题..."></textarea>
            </div>
            <div class="form-item">
              <label>📌 问题发生地点</label>
              <input v-model="feedback.afAddress" placeholder="请输入详细地址" />
            </div>
            <div class="form-item">
              <label>🕐 问题发生时间</label>
              <input v-model="feedback.occurTime" type="datetime-local" />
            </div>
            <div class="form-row">
              <div class="form-item flex-1">
                <label>📐 经度</label>
                <input v-model="feedback.afLongitude" type="number" step="0.000001" placeholder="经度坐标" />
              </div>
              <div class="form-item flex-1">
                <label>📐 纬度</label>
                <input v-model="feedback.afLatitude" type="number" step="0.000001" placeholder="纬度坐标" />
              </div>
            </div>
            <button class="btn-primary" @click="submitFeedback">
              <span>📤 提交反馈</span>
            </button>
          </div>
        </div>

        <!-- Tab2: 申报历史 -->
        <div v-show="activeTab === 'history'" class="tab-panel">
          <div v-if="feedbackList.length === 0" class="empty-card">
            <div class="empty-icon">🌱</div>
            <p>暂无反馈记录</p>
            <p class="empty-hint">您提交的环境问题反馈将在这里显示</p>
          </div>
          <div v-else class="list-wrap">
            <div v-for="item in feedbackList" :key="item.af_id" class="feedback-card">
              <div class="fb-header">
                <span class="fb-code">📋 {{ item.af_code }}</span>
                <span :class="['fb-status', 'status-' + item.af_state]">{{ statusMap[item.af_state] || item.af_state }}</span>
              </div>
              <div class="fb-body">
                <div class="fb-row"><span class="fb-label">类型：</span>{{ typeMap[item.af_type] || item.af_type }}</div>
                <div class="fb-row"><span class="fb-label">AQI等级：</span>{{ item.af_level }}</div>
                <div class="fb-row"><span class="fb-label">地址：</span>{{ item.af_address }}</div>
                <div class="fb-row"><span class="fb-label">描述：</span>{{ item.af_desc }}</div>
              </div>
              <div class="fb-footer">🕐 {{ formatTime(item.create_time) }}</div>
            </div>
          </div>
        </div>

        <!-- Tab3: 污染知识 -->
        <div v-show="activeTab === 'knowledge'" class="tab-panel">
          <div class="card">
            <div class="card-icon">📖</div>
            <h3 class="card-title">空气质量指数（AQI）分级说明</h3>
            <p class="card-hint">根据《环境空气质量标准》（GB 3095-2012），AQI 共分为 6 个级别</p>
          </div>
          <div class="knowledge-list">
            <div v-for="level in pollutionLevels" :key="level.level" class="knowledge-card" :style="{ borderLeftColor: level.color }">
              <div class="kn-header">
                <span class="kn-badge" :style="{ background: level.color }">{{ level.level }}</span>
                <span class="kn-name">{{ level.name }}</span>
                <span class="kn-range">AQI {{ level.range }}</span>
              </div>
              <div class="kn-desc">{{ level.description }}</div>
              <div class="kn-details">
                <div class="kn-detail-item">
                  <span class="kn-detail-label">🔍 空气质量</span>
                  <span>{{ level.airQuality }}</span>
                </div>
                <div class="kn-detail-item">
                  <span class="kn-detail-label">👤 健康影响</span>
                  <span>{{ level.healthEffect }}</span>
                </div>
                <div class="kn-detail-item">
                  <span class="kn-detail-label">💡 建议措施</span>
                  <span>{{ level.suggestion }}</span>
                </div>
              </div>
              <div class="kn-color-bar" :style="{ background: level.color }"></div>
            </div>
          </div>
        </div>

        <!-- Tab4: 用户设置 -->
        <div v-show="activeTab === 'settings'" class="tab-panel">
          <div class="card">
            <div class="card-icon">👤</div>
            <h3 class="card-title">个人信息</h3>
            <div class="form-item">
              <label>姓名</label>
              <input v-model="profile.supervisorName" placeholder="请输入姓名" />
            </div>
            <div class="form-item">
              <label>手机号</label>
              <input v-model="profile.supervisorTel" disabled />
            </div>
            <div class="form-row">
              <div class="form-item flex-1">
                <label>性别</label>
                <select v-model="profile.supervisorSex">
                  <option value="男">男</option>
                  <option value="女">女</option>
                </select>
              </div>
              <div class="form-item flex-1">
                <label>年龄</label>
                <input v-model="profile.supervisorAge" type="number" placeholder="年龄" />
              </div>
            </div>
            <div class="form-item">
              <label>地址</label>
              <input v-model="profile.supervisorAddr" placeholder="请输入详细地址" />
            </div>
            <button class="btn-primary" @click="updateProfile">💾 保存修改</button>
          </div>
          <div class="card" style="margin-top:16px">
            <div class="card-icon">🔒</div>
            <h3 class="card-title">修改密码</h3>
            <div class="form-item">
              <label>新密码</label>
              <input v-model="password.newPwd" type="password" placeholder="请输入新密码（至少6位）" />
            </div>
            <div class="form-item">
              <label>确认密码</label>
              <input v-model="password.confirmPwd" type="password" placeholder="请再次输入新密码" />
            </div>
            <button class="btn-primary" @click="changePassword">🔑 修改密码</button>
          </div>
        </div>

      </div>
    </div>

    <!-- ========== 底部 ========== -->
    <div class="footer">
      <span>🌍 东软环保公众监督系统 · 共建绿色家园</span>
    </div>
  </div>
</template>

<script>
import api from "@/api";

export default {
  name: "SelectGrid",
  data() {
    return {
      activeTab: "report",
      tabs: [
        { key: "report", label: "选择地区申报", icon: "📝", desc: "提交环境问题" },
        { key: "history", label: "申报历史", icon: "📋", desc: "查看反馈记录" },
        { key: "knowledge", label: "污染知识", icon: "📖", desc: "AQI分级说明" },
        { key: "settings", label: "用户设置", icon: "⚙️", desc: "管理账号信息" },
      ],
      userInfo: {},
      provinces: [],
      cities: [],
      provinceId: "",
      cityId: "",
      aqiList: [],
      feedback: {
        afType: "", afLevel: "", afDesc: "", afAddress: "",
        afLongitude: "", afLatitude: "", occurTime: "",
      },
      reportStep: 1,
      feedbackList: [],
      statusMap: { "01": "待指派", "02": "待确认", "03": "已完成" },
      typeMap: { "01": "空气有异味", "02": "雾霾严重", "03": "工业排放", "04": "其他" },
      pollutionLevels: [
        { level: "一级", name: "优", range: "0 ~ 50", color: "#00E400", airQuality: "空气质量令人满意，基本无空气污染", healthEffect: "各类人群可正常活动", suggestion: "无需防护，适宜户外运动和开窗通风" },
        { level: "二级", name: "良", range: "51 ~ 100", color: "#FFFF00", airQuality: "空气质量可接受，某些污染物可能对极少数敏感人群有轻微影响", healthEffect: "极少数异常敏感人群应减少户外活动", suggestion: "敏感人群长时间户外活动时需关注身体状况" },
        { level: "三级", name: "轻度污染", range: "101 ~ 150", color: "#FF7E00", airQuality: "易感人群症状有轻度加剧，健康人群出现刺激症状", healthEffect: "儿童、老年人及心脏病、呼吸系统疾病患者应减少高强度户外锻炼", suggestion: "建议敏感人群减少户外活动，普通人群长时间户外活动时注意防护" },
        { level: "四级", name: "中度污染", range: "151 ~ 200", color: "#FF0000", airQuality: "进一步加剧易感人群症状，可能对健康人群心脏、呼吸系统有影响", healthEffect: "儿童、老年人及心脏病、呼吸系统疾病患者避免长时间户外活动", suggestion: "外出时佩戴防护口罩，减少开窗时间，敏感人群尽量留在室内" },
        { level: "五级", name: "重度污染", range: "201 ~ 300", color: "#99004C", airQuality: "心脏病和肺病患者症状显著加剧，运动耐受力降低，健康人群普遍出现症状", healthEffect: "儿童、老年人及心脏病、肺病患者应留在室内停止户外活动", suggestion: "必须外出时佩戴N95口罩，关闭门窗使用空气净化器" },
        { level: "六级", name: "严重污染", range: "> 300", color: "#7E0023", airQuality: "健康人群运动耐受力降低，有明显强烈症状", healthEffect: "儿童、老年人和病人应当留在室内避免体力消耗，一般人群应避免户外活动", suggestion: "尽量不要外出，紧闭门窗开启空气净化设备，如不适应及时就医" }
      ],
      profile: {
        supervisorName: "", supervisorTel: "", supervisorSex: "男",
        supervisorAge: "", supervisorAddr: "", provinceId: null, cityId: null,
      },
      password: { newPwd: "", confirmPwd: "" },
    };
  },
  created() {
    this.loadUserInfo();
    this.loadProvinces();
    this.loadAqiList();
    this.loadFeedbackList();
  },
  methods: {
    formatTime(val) {
      if (!val) return '-'
      const d = new Date(val)
      if (isNaN(d.getTime())) return val
      return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')} ${String(d.getHours()).padStart(2,'0')}:${String(d.getMinutes()).padStart(2,'0')}:${String(d.getSeconds()).padStart(2,'0')}`
    },
    loadUserInfo() {
      const raw = sessionStorage.getItem("supervisor");
      if (raw) {
        this.userInfo = JSON.parse(raw);
        this.profile = {
          supervisorName: this.userInfo.supervisorName || "",
          supervisorTel: this.userInfo.supervisorTel || "",
          supervisorSex: this.userInfo.supervisorSex || "男",
          supervisorAge: this.userInfo.supervisorAge || "",
          supervisorAddr: this.userInfo.supervisorAddr || "",
          provinceId: this.userInfo.provinceId || null,
          cityId: this.userInfo.cityId || null,
        };
      }
    },
    switchTab(key) {
      this.activeTab = key;
      if (key === "history") this.loadFeedbackList();
      if (key === "settings") this.loadUserInfo();
      if (key === "report" && this.reportStep === 2) this.reportStep = 1;
    },
    logout() {
      sessionStorage.removeItem("supervisor");
      this.$router.push("/login");
    },
    async loadProvinces() {
      const res = await api.post("/gridProvince/listGridProvinceAll");
      if (res.code === 200) this.provinces = res.data;
    },
    async loadCities() {
      this.cityId = "";
      if (this.provinceId) {
        const res = await api.post("/gridCity/listGridCityByProvinceId", { provinceId: this.provinceId });
        if (res.code === 200) this.cities = res.data;
      } else {
        this.cities = [];
      }
    },
    async loadAqiList() {
      const res = await api.get("/aqi/listAqiAll");
      if (res.code === 200) this.aqiList = res.data;
    },
    goReportStep2() {
      if (!this.provinceId || !this.cityId) { alert("请选择省份和城市"); return; }
      this.reportStep = 2;
    },
    async submitFeedback() {
      const data = { ...this.feedback, supervisorId: this.userInfo.supervisorId, afProvinceId: this.provinceId, afCityId: this.cityId };
      try {
        const res = await api.post("/aqiFeedback/saveAqiFeedback", data);
        if (res.code === 200) {
          alert("提交成功！感谢您为环境保护贡献力量 🌍");
          this.feedback = { afType: "", afLevel: "", afDesc: "", afAddress: "", afLongitude: "", afLatitude: "", occurTime: "" };
          this.reportStep = 1;
        } else { alert("提交失败: " + res.msg); }
      } catch (e) { alert("提交失败，请检查网络连接"); }
    },
    async loadFeedbackList() {
      if (!this.userInfo.supervisorId) { this.loadUserInfo(); if (!this.userInfo.supervisorId) return; }
      const res = await api.post("/aqiFeedback/listAqiFeedbackByTelId", { telId: this.userInfo.supervisorId });
      if (res.code === 200) this.feedbackList = res.data || [];
    },
    async updateProfile() {
      try {
        const data = {
          supervisorId: this.userInfo.supervisorId, supervisorTel: this.profile.supervisorTel,
          supervisorPwd: this.userInfo.supervisorPwd, supervisorName: this.profile.supervisorName,
          supervisorSex: this.profile.supervisorSex,
          supervisorAge: this.profile.supervisorAge ? Number(this.profile.supervisorAge) : null,
          supervisorAddr: this.profile.supervisorAddr,
          provinceId: this.profile.provinceId ? Number(this.profile.provinceId) : null,
          cityId: this.profile.cityId ? Number(this.profile.cityId) : null,
        };
        const res = await api.post("/supervisor/saveSupervisor", data);
        if (res.code === 200) {
          alert("保存成功");
          this.userInfo = { ...this.userInfo, ...data };
          sessionStorage.setItem("supervisor", JSON.stringify(this.userInfo));
        } else { alert("保存失败: " + res.msg); }
      } catch (e) { alert("保存失败，请检查网络连接"); }
    },
    async changePassword() {
      if (!this.password.newPwd || !this.password.confirmPwd) { alert("请输入新密码"); return; }
      if (this.password.newPwd !== this.password.confirmPwd) { alert("两次输入的密码不一致"); return; }
      if (this.password.newPwd.length < 6) { alert("密码不能少于6位"); return; }
      const newPwd = this.password.newPwd;
      try {
        const res = await api.post("/supervisor/saveSupervisor", {
          supervisorId: this.userInfo.supervisorId, supervisorTel: this.userInfo.supervisorTel,
          supervisorPwd: newPwd, supervisorName: this.userInfo.supervisorName,
          supervisorSex: this.userInfo.supervisorSex,
          supervisorAge: this.userInfo.supervisorAge ? Number(this.userInfo.supervisorAge) : null,
          supervisorAddr: this.userInfo.supervisorAddr,
          provinceId: this.userInfo.provinceId ? Number(this.userInfo.provinceId) : null,
          cityId: this.userInfo.cityId ? Number(this.userInfo.cityId) : null,
        });
        if (res.code === 200) {
          this.userInfo.supervisorPwd = newPwd;
          sessionStorage.setItem("supervisor", JSON.stringify(this.userInfo));
          alert("密码修改成功");
          this.password = { newPwd: "", confirmPwd: "" };
        } else {
          alert("修改失败: " + res.msg);
        }
      } catch (e) {
        alert("修改失败，请检查网络连接");
      }
    },
  },
};
</script>

<style scoped>
/* ========== 全局 ========== */
.main-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #e8f5e9 0%, #f1f8e9 30%, #f0fdf4 60%, #e8f5e9 100%);
  display: flex;
  flex-direction: column;
  font-family: "Microsoft YaHei", "PingFang SC", sans-serif;
}
.body-wrap { display: flex; flex: 1; }

/* ========== 顶部栏 ========== */
.topbar {
  height: 60px;
  background: linear-gradient(135deg, #1b5e20, #2e7d32, #388e3c);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  color: #fff;
  box-shadow: 0 2px 12px rgba(0, 80, 20, 0.2);
  position: relative;
  z-index: 10;
}
.topbar-left { display: flex; align-items: center; gap: 10px; }
.logo-icon { font-size: 24px; }
.logo-text { font-size: 18px; font-weight: 700; letter-spacing: 2px; }
.topbar-center { flex: 1; text-align: center; }
.topbar-slogan {
  font-size: 13px;
  opacity: 0.85;
  letter-spacing: 3px;
  animation: sloganPulse 3s ease-in-out infinite;
}
@keyframes sloganPulse {
  0%, 100% { opacity: 0.7; }
  50% { opacity: 1; }
}
.topbar-right { display: flex; align-items: center; gap: 16px; }
.user-name { font-size: 14px; }
.logout-btn {
  background: rgba(255,255,255,0.15);
  color: #fff;
  border: 1px solid rgba(255,255,255,0.3);
  padding: 5px 16px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 13px;
  transition: all 0.2s;
}
.logout-btn:hover { background: rgba(255,255,255,0.3); }

/* ========== 装饰带 ========== */
.nature-strip {
  height: 28px;
  background: linear-gradient(90deg, #c8e6c9, #dcedc8, #c8e6c9);
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
}
.leaf {
  position: absolute;
  font-size: 12px;
  opacity: 0.4;
  animation: leafFloat 6s ease-in-out infinite;
}
@keyframes leafFloat {
  0%, 100% { transform: translateY(0) rotate(0deg); opacity: 0.3; }
  50% { transform: translateY(-6px) rotate(15deg); opacity: 0.7; }
}

/* ========== 侧边栏 ========== */
.sidebar {
  width: 210px;
  background: #fff;
  border-right: 1px solid #c8e6c9;
  padding: 0 0 16px;
  box-shadow: 2px 0 12px rgba(0, 80, 20, 0.04);
}
.sidebar-header {
  padding: 18px 20px 12px;
  font-size: 12px;
  color: #81c784;
  font-weight: 600;
  letter-spacing: 2px;
  text-transform: uppercase;
}
.menu-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 14px 20px;
  cursor: pointer;
  color: #666;
  font-size: 14px;
  border-left: 3px solid transparent;
  transition: all 0.2s;
  margin: 2px 0;
}
.menu-item:hover {
  background: #f1f8e9;
  color: #388e3c;
}
.menu-item.active {
  background: #e8f5e9;
  color: #2e7d32;
  border-left-color: #43a047;
  font-weight: 600;
}
.menu-icon { font-size: 20px; }
.menu-text { display: flex; flex-direction: column; gap: 3px; }
.menu-label { font-size: 14px; line-height: 1.3; }
.menu-desc { font-size: 11px; color: #aaa; line-height: 1.3; }
.menu-item.active .menu-desc { color: #81c784; }
.menu-arrow { margin-left: auto; color: #43a047; font-size: 14px; }

/* ========== 内容区 ========== */
.content { flex: 1; padding: 28px; overflow-y: auto; }
.tab-panel { max-width: 720px; margin: 0 auto; }

/* ========== 卡片 ========== */
.card {
  background: #fff;
  border-radius: 14px;
  padding: 32px;
  box-shadow: 0 2px 16px rgba(0, 80, 20, 0.06);
  border: 1px solid #e8f5e9;
}
.card-icon {
  width: 46px; height: 46px;
  background: linear-gradient(135deg, #c8e6c9, #a5d6a7);
  border-radius: 12px;
  display: flex; align-items: center; justify-content: center;
  font-size: 22px;
  margin-bottom: 16px;
}
.card-title {
  font-size: 20px; font-weight: 700;
  color: #1b5e20;
  margin-bottom: 8px;
  line-height: 1.4;
}
.card-hint {
  color: #999; font-size: 13px; margin-bottom: 28px;
  line-height: 1.5;
}
.card-title-row {
  display: flex; justify-content: space-between; align-items: center;
  margin-bottom: 8px;
}

/* ========== 表单 ========== */
.form-item { margin-bottom: 20px; }
.form-item label {
  display: block; margin-bottom: 8px;
  font-weight: 600; color: #555; font-size: 14px; line-height: 1.4;
}
.form-item input,
.form-item select,
.form-item textarea {
  width: 100%; padding: 11px 14px;
  border: 1.5px solid #dcedc8;
  border-radius: 8px;
  font-size: 14px; color: #333;
  transition: all 0.2s;
  background: #f9fdf7;
}
.form-item input:focus,
.form-item select:focus,
.form-item textarea:focus {
  outline: none;
  border-color: #66bb6a;
  background: #fff;
  box-shadow: 0 0 0 3px rgba(102, 187, 106, 0.1);
}
.form-item input:disabled { background: #f0f0f0; color: #999; }
.form-item textarea { resize: vertical; min-height: 80px; }
.form-row { display: flex; gap: 16px; }
.flex-1 { flex: 1; }

/* ========== 按钮 ========== */
.btn-primary {
  width: 100%; padding: 14px;
  background: linear-gradient(135deg, #43a047, #2e7d32);
  color: #fff;
  border: none; border-radius: 8px;
  font-size: 15px; font-weight: 600;
  cursor: pointer;
  transition: all 0.2s;
  display: flex; align-items: center; justify-content: center; gap: 8px;
  box-shadow: 0 2px 8px rgba(46, 125, 50, 0.2);
  line-height: 1.4;
}
.btn-primary:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 14px rgba(46, 125, 50, 0.3);
}
.btn-primary:disabled {
  opacity: 0.5; cursor: not-allowed; transform: none;
}
.btn-arrow { font-size: 18px; }
.btn-text {
  background: none; border: none;
  color: #43a047; font-size: 14px; cursor: pointer; padding: 0;
}
.btn-text:hover { color: #2e7d32; text-decoration: underline; }
.step-badge {
  background: #e8f5e9; color: #388e3c;
  padding: 4px 14px; border-radius: 20px;
  font-size: 12px; font-weight: 600;
}

/* ========== 历史列表 ========== */
.empty-card {
  background: #fff; border-radius: 14px; padding: 60px;
  text-align: center; color: #999; font-size: 16px;
  border: 2px dashed #dcedc8;
}
.empty-icon { font-size: 48px; margin-bottom: 12px; }
.empty-hint { font-size: 13px; color: #bbb; margin-top: 6px; }
.list-wrap { display: flex; flex-direction: column; gap: 14px; }
.feedback-card {
  background: #fff; border-radius: 12px; padding: 20px;
  box-shadow: 0 2px 10px rgba(0, 80, 20, 0.05);
  border-left: 4px solid #a5d6a7;
  transition: all 0.2s;
}
.feedback-card:hover { box-shadow: 0 4px 16px rgba(0, 80, 20, 0.1); }
.fb-header {
  display: flex; justify-content: space-between; align-items: center;
  margin-bottom: 14px;
}
.fb-code { font-weight: 600; color: #2e7d32; font-size: 15px; }
.fb-status { padding: 3px 14px; border-radius: 20px; font-size: 12px; font-weight: 500; }
.status-01 { background: #fff3e0; color: #e65100; }
.status-02 { background: #e3f2fd; color: #1565c0; }
.status-03 { background: #e8f5e9; color: #2e7d32; }
.fb-body { margin-bottom: 12px; }
.fb-row { margin: 7px 0; color: #555; font-size: 14px; line-height: 1.6; }
.fb-label { color: #388e3c; font-weight: 600; margin-right: 4px; }
.fb-footer {
  padding-top: 12px; border-top: 1px solid #e8f5e9;
  color: #999; font-size: 12px;
}

/* ========== 污染知识 ========== */
.knowledge-list { display: flex; flex-direction: column; gap: 12px; margin-top: 16px; }
.knowledge-card {
  background: #fff; border-radius: 12px; padding: 20px 24px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.04); border-left: 6px solid;
  position: relative; overflow: hidden; transition: all 0.2s;
}
.knowledge-card:hover { box-shadow: 0 4px 16px rgba(0,0,0,0.08); }
.kn-color-bar { position: absolute; bottom: 0; left: 0; right: 0; height: 3px; opacity: 0.25; }
.kn-header { display: flex; align-items: center; gap: 12px; margin-bottom: 14px; }
.kn-badge { color: #fff; padding: 3px 14px; border-radius: 20px; font-size: 12px; font-weight: 600; }
.kn-name { font-size: 17px; font-weight: 700; color: #333; line-height: 1.3; }
.kn-range { margin-left: auto; font-size: 13px; color: #999; }
.kn-desc {
  color: #555; font-size: 14px; margin-bottom: 14px;
  padding: 12px 16px; background: #f1f8e9; border-radius: 8px; line-height: 1.7;
}
.kn-details { display: flex; flex-direction: column; gap: 10px; margin-top: 4px; }
.kn-detail-item { font-size: 13px; color: #666; line-height: 1.6; display: flex; gap: 8px; }
.kn-detail-label { font-weight: 600; color: #388e3c; white-space: nowrap; }

/* ========== 底部 ========== */
.footer {
  text-align: center; padding: 16px;
  color: #a5d6a7; font-size: 12px;
  border-top: 1px solid #e8f5e9;
  background: #fff;
  letter-spacing: 1px;
  line-height: 1.5;
}

/* ========== 移动端适配 ========== */
@media (max-width: 768px) {
  .topbar-center { display: none; }
  .topbar { padding: 0 16px; }
  .body-wrap { flex-direction: column; }
  .sidebar {
    width: 100%; border-right: none;
    border-bottom: 1px solid #c8e6c9;
    padding: 0;
    display: flex; overflow-x: auto;
  }
  .sidebar-header { display: none; }
  .menu-item {
    flex-direction: column; gap: 2px;
    padding: 10px 14px; border-left: none;
    border-bottom: 3px solid transparent;
    min-width: 80px; text-align: center;
  }
  .menu-item.active { border-left: none; border-bottom-color: #43a047; }
  .menu-text { align-items: center; }
  .menu-desc { display: none; }
  .menu-arrow { display: none; }
  .nature-strip { display: none; }
  .content { padding: 16px; }
  .tab-panel { max-width: 100%; }
  .card { padding: 20px; border-radius: 12px; }
  .form-row { flex-direction: column; gap: 0; }
  .knowledge-card .kn-details { flex-direction: column; }
}
</style>
