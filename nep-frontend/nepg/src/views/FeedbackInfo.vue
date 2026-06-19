<template>
  <div class="feedback-info-container">
    <div class="header">
      <h2>录入检测数据</h2>
      <button class="back-btn" @click="goBack">返回</button>
    </div>
    <div class="content">
      <div class="info-card">
        <h3>反馈信息</h3>
        <p><strong>编号：</strong>{{ detail.af_code }}</p>
        <p><strong>类型：</strong>{{ getTypeText(detail.af_type) }}</p>
        <p><strong>地址：</strong>{{ detail.af_address }}</p>
        <p><strong>描述：</strong>{{ detail.af_desc }}</p>
      </div>
      <div class="form-card">
        <h3>检测数据</h3>
        <form @submit.prevent="submitData">
          <div class="form-row">
            <div class="form-group">
              <label>PM2.5 (μg/m³)</label>
              <input
                type="number"
                step="0.01"
                v-model="form.pm25Value"
                required
              />
            </div>
            <div class="form-group">
              <label>PM10 (μg/m³)</label>
              <input
                type="number"
                step="0.01"
                v-model="form.pm10Value"
                required
              />
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>SO2 (μg/m³)</label>
              <input
                type="number"
                step="0.01"
                v-model="form.so2Value"
                required
              />
            </div>
            <div class="form-group">
              <label>NO2 (μg/m³)</label>
              <input
                type="number"
                step="0.01"
                v-model="form.no2Value"
                required
              />
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>CO (mg/m³)</label>
              <input
                type="number"
                step="0.01"
                v-model="form.coValue"
                required
              />
            </div>
            <div class="form-group">
              <label>O3 (μg/m³)</label>
              <input
                type="number"
                step="0.01"
                v-model="form.o3Value"
                required
              />
            </div>
          </div>
          <div class="form-group">
            <label>确认备注</label>
            <textarea
              v-model="form.desc"
              placeholder="请输入确认备注"
            ></textarea>
          </div>
          <button type="submit" class="submit-btn">提交检测数据</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import api from "@/api";

export default {
  name: "FeedbackInfo",
  data() {
    return {
      detail: {},
      form: {
        pm25Value: "",
        pm10Value: "",
        so2Value: "",
        no2Value: "",
        coValue: "",
        o3Value: "",
        desc: "",
      },
    };
  },
  created() {
    this.loadDetail();
  },
  methods: {
    async loadDetail() {
      const id = this.$route.params.id;
      const res = await api.post("/aqiFeedback/getAqiFeedbackById", {
        afId: parseInt(id),
      });
      if (res.code === 200) {
        this.detail = res.data;
      }
    },
    getTypeText(type) {
      const map = {
        "01": "空气有异味",
        "02": "雾霾严重",
        "03": "工业排放",
        "04": "其他",
      };
      return map[type] || type;
    },
    async submitData() {
      const gridMember = JSON.parse(sessionStorage.getItem("gridMember"));
      const id = this.$route.params.id;

      const statistics = {
        afId: parseInt(id),
        gmId: gridMember.gm_id,
        pm25Value: parseFloat(this.form.pm25Value),
        pm10Value: parseFloat(this.form.pm10Value),
        so2Value: parseFloat(this.form.so2Value),
        no2Value: parseFloat(this.form.no2Value),
        coValue: parseFloat(this.form.coValue),
        o3Value: parseFloat(this.form.o3Value),
        provinceId: this.detail.af_province_id,
        cityId: this.detail.af_city_id,
      };

      const statRes = await api.post("/statistics/saveStatistics", statistics);
      if (statRes.code === 200) {
        await api.post("/aqiFeedback/updateAqiFeedbackState", {
          afId: parseInt(id),
          state: "03",
          aqi: Math.round((statistics.pm25Value + statistics.pm10Value) / 2),
          desc: this.form.desc,
        });
        alert("提交成功");
        this.$router.push("/feedbackList");
      }
    },
    goBack() {
      this.$router.push("/feedbackList");
    },
  },
};
</script>

<style scoped>
.feedback-info-container {
  min-height: 100vh;
  background: #f5f5f5;
}
.header {
  background: #11998e;
  color: white;
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.back-btn {
  background: transparent;
  color: white;
  border: 1px solid white;
  padding: 8px 16px;
  border-radius: 5px;
  cursor: pointer;
}
.content {
  max-width: 700px;
  margin: 30px auto;
  padding: 0 20px;
}
.info-card,
.form-card {
  background: white;
  border-radius: 10px;
  padding: 25px;
  margin-bottom: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
.info-card h3,
.form-card h3 {
  margin-bottom: 15px;
  color: #333;
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
}
.info-card p {
  margin: 10px 0;
  color: #666;
}
.form-group {
  margin-bottom: 15px;
}
.form-group label {
  display: block;
  margin-bottom: 5px;
  color: #555;
}
.form-group input,
.form-group textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 14px;
}
.form-group textarea {
  height: 80px;
  resize: vertical;
}
.form-row {
  display: flex;
  gap: 15px;
}
.form-row .form-group {
  flex: 1;
}
.submit-btn {
  width: 100%;
  padding: 12px;
  background: #11998e;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
  margin-top: 10px;
}
.submit-btn:hover {
  background: #10887d;
}
</style>
