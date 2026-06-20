<template>
  <div class="select-aqi-container">
    <div class="header">
      <h2>提交反馈</h2>
      <button class="logout-btn" @click="logout">退出登录</button>
    </div>
    <div class="content">
      <div class="form-group">
        <label>反馈类型</label>
        <select v-model="form.afType" required>
          <option value="">请选择反馈类型</option>
          <option value="01">空气有异味</option>
          <option value="02">雾霾严重</option>
          <option value="03">工业排放</option>
          <option value="04">其他</option>
        </select>
      </div>
      <div class="form-group">
        <label>预估AQI等级</label>
        <select v-model="form.afLevel" required>
          <option value="">请选择AQI等级</option>
          <option v-for="a in aqiList" :key="a.aqiId" :value="a.aqiLevel">
            {{ a.aqiLevel }}
          </option>
        </select>
      </div>
      <div class="form-group">
        <label>问题描述</label>
        <textarea
          v-model="form.afDesc"
          placeholder="请详细描述问题"
          required
        ></textarea>
      </div>
      <div class="form-group">
        <label>问题发生地点</label>
        <input
          type="text"
          v-model="form.afAddress"
          placeholder="请输入详细地址"
          required
        />
      </div>
      <div class="form-row">
        <div class="form-group">
          <label>经度</label>
          <input
            type="number"
            step="0.000001"
            v-model="form.afLongitude"
            placeholder="经度"
          />
        </div>
        <div class="form-group">
          <label>纬度</label>
          <input
            type="number"
            step="0.000001"
            v-model="form.afLatitude"
            placeholder="纬度"
          />
        </div>
      </div>
      <button class="submit-btn" @click="submitFeedback">提交反馈</button>
      <!-- 【新增】返回重选按钮 -->
      <button class="back-btn" @click="goBackToSelectGrid">返回重选地区</button>

      <button class="history-btn" @click="viewHistory">反馈历史</button>
    </div>
  </div>
</template>

<script>
import api from "@/api";

export default {
  name: "SelectAqi",
  data() {
    return {
      aqiList: [],
      form: {
        afType: "",
        afLevel: "",
        afDesc: "",
        afAddress: "",
        afLongitude: "",
        afLatitude: "",
      },
    };
  },
  created() {
    this.loadAqiList();
  },
  methods: {
    async loadAqiList() {
      const res = await api.get("/aqi/listAqiAll");
      if (res.code === 200) {
        this.aqiList = res.data;
      }
    },
    async submitFeedback() {
      const supervisor = JSON.parse(sessionStorage.getItem("supervisor"));
      const feedback = {
        ...this.form,
        supervisorId: supervisor.supervisorId,
        afProvinceId: supervisor.provinceId,
        afCityId: supervisor.cityId,
      };
      try {
        const res = await api.post("/aqiFeedback/saveAqiFeedback", feedback);
        if (res.code === 200) {
          alert("提交成功");
          this.form = {
            afType: "",
            afLevel: "",
            afDesc: "",
            afAddress: "",
            afLongitude: "",
            afLatitude: "",
          };
        } else {
          alert(res.msg);
        }
      } catch (error) {
        alert("提交失败");
      }
    },
    // 【新增】返回选择地区页面
    goBackToSelectGrid() {
      this.$router.push("/selectGrid");
    },
    viewHistory() {
      this.$router.push("/feedbackList");
    },
    logout() {
      sessionStorage.removeItem("supervisor");
      this.$router.push("/login");
    },
  },
};
</script>

<style scoped>
.select-aqi-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #e8f5e9 0%, #f1f8e9 30%, #f0fdf4 100%);
}
.header {
  background: linear-gradient(135deg, #1b5e20, #2e7d32, #388e3c);
  color: white;
  padding: 20px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 12px rgba(0,80,20,0.2);
}
.header h2 { margin: 0; font-size: 20px; font-weight: 700; letter-spacing: 1px; }
.logout-btn {
  background: rgba(255,255,255,0.15);
  color: white;
  border: 1px solid rgba(255,255,255,0.3);
  padding: 8px 20px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 13px;
  transition: all 0.2s;
}
.logout-btn:hover { background: rgba(255,255,255,0.3); }
.content {
  max-width: 600px;
  margin: 30px auto;
  background: white;
  padding: 40px;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0,80,20,0.08);
  animation: fadeInUp 0.4s ease-out;
}
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(16px); }
  to { opacity: 1; transform: translateY(0); }
}
.form-group {
  margin-bottom: 22px;
}
.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #555;
  font-weight: 600;
  font-size: 14px;
  line-height: 1.4;
}
.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 12px 14px;
  border: 1.5px solid #dcedc8;
  border-radius: 10px;
  font-size: 14px;
  color: #333;
  background: #f9fdf7;
  transition: all 0.25s ease;
  box-sizing: border-box;
}
.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #66bb6a;
  background: #fff;
  box-shadow: 0 0 0 3px rgba(102,187,106,0.1);
}
.form-group textarea {
  min-height: 100px;
  resize: vertical;
}
.form-row {
  display: flex;
  gap: 16px;
}
.form-row .form-group {
  flex: 1;
}
.submit-btn {
  width: 100%;
  padding: 14px;
  background: linear-gradient(135deg, #43a047, #2e7d32);
  color: white;
  border: none;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  margin-bottom: 14px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 14px rgba(46,125,50,0.25);
  letter-spacing: 1px;
  line-height: 1.4;
}
.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(46,125,50,0.35);
}
.back-btn {
  width: 100%;
  padding: 13px;
  background: #fff;
  color: #666;
  border: 1.5px solid #dcedc8;
  border-radius: 10px;
  font-size: 15px;
  cursor: pointer;
  margin-bottom: 14px;
  transition: all 0.2s;
  line-height: 1.4;
}
.back-btn:hover {
  background: #f1f8e9;
  border-color: #a5d6a7;
  color: #2e7d32;
}
.history-btn {
  width: 100%;
  padding: 13px;
  background: #fff;
  color: #43a047;
  border: 1.5px solid #a5d6a7;
  border-radius: 10px;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
  line-height: 1.4;
}
.history-btn:hover {
  background: #e8f5e9;
}

@media (max-width: 768px) {
  .content { margin: 16px; padding: 24px 20px; }
  .form-row { flex-direction: column; gap: 0; }
}
</style>
