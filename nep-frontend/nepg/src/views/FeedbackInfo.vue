<template>
  <div class="detect-page">
    <button class="back-btn" @click="$router.push('/index/tasks')">← 返回任务列表</button>
    <h2>🔬 录入检测数据</h2>
    <div class="detect-card">
      <h3>📋 反馈信息</h3>
      <div class="info-grid">
        <div class="info-item"><span class="info-label">编号：</span>{{ detail.af_code }}</div>
        <div class="info-item"><span class="info-label">类型：</span>{{ typeMap[detail.af_type] || detail.af_type }}</div>
        <div class="info-item"><span class="info-label">地址：</span>{{ detail.af_address }}</div>
        <div class="info-item"><span class="info-label">描述：</span>{{ detail.af_desc }}</div>
      </div>
    </div>
    <div class="detect-card">
      <h3>🧪 空气质量检测数据</h3>
      <form @submit.prevent="submitData">
        <div class="form-row">
          <div class="form-item">
            <label>PM2.5 (μg/m³)</label>
            <input type="number" step="0.01" v-model="form.pm25Value" placeholder="0.00" required />
          </div>
          <div class="form-item">
            <label>PM10 (μg/m³)</label>
            <input type="number" step="0.01" v-model="form.pm10Value" placeholder="0.00" required />
          </div>
        </div>
        <div class="form-row">
          <div class="form-item">
            <label>SO₂ (μg/m³)</label>
            <input type="number" step="0.01" v-model="form.so2Value" placeholder="0.00" required />
          </div>
          <div class="form-item">
            <label>NO₂ (μg/m³)</label>
            <input type="number" step="0.01" v-model="form.no2Value" placeholder="0.00" required />
          </div>
        </div>
        <div class="form-row">
          <div class="form-item">
            <label>CO (mg/m³)</label>
            <input type="number" step="0.01" v-model="form.coValue" placeholder="0.00" required />
          </div>
          <div class="form-item">
            <label>O₃ (μg/m³)</label>
            <input type="number" step="0.01" v-model="form.o3Value" placeholder="0.00" required />
          </div>
        </div>
        <div class="form-item">
          <label>📝 检测备注</label>
          <textarea v-model="form.desc" rows="3" placeholder="请输入检测备注信息..."></textarea>
        </div>
        <button type="submit" class="btn-submit">📤 提交检测数据</button>
      </form>
    </div>
  </div>
</template>

<script>
import api from "@/api"

export default {
  name: "FeedbackInfo",
  data() {
    return {
      detail: {},
      form: { pm25Value: "", pm10Value: "", so2Value: "", no2Value: "", coValue: "", o3Value: "", desc: "" },
      typeMap: { "01": "空气有异味", "02": "雾霾严重", "03": "工业排放", "04": "其他" }
    }
  },
  created() { this.loadDetail() },
  methods: {
    async loadDetail() {
      const id = this.$route.params.id
      const res = await api.post("/aqiFeedback/getAqiFeedbackById", { afId: parseInt(id) })
      if (res.code === 200) this.detail = res.data
    },
    async submitData() {
      const gm = JSON.parse(sessionStorage.getItem("gridMember"))
      const id = this.$route.params.id
      const statistics = {
        afId: parseInt(id), gmId: gm.gmId,
        pm25Value: parseFloat(this.form.pm25Value), pm10Value: parseFloat(this.form.pm10Value),
        so2Value: parseFloat(this.form.so2Value), no2Value: parseFloat(this.form.no2Value),
        coValue: parseFloat(this.form.coValue), o3Value: parseFloat(this.form.o3Value),
        provinceId: this.detail.af_province_id, cityId: this.detail.af_city_id
      }
      const statRes = await api.post("/statistics/saveStatistics", statistics)
      if (statRes.code === 200) {
        await api.post("/aqiFeedback/updateAqiFeedbackState", {
          afId: parseInt(id), state: "03",
          aqi: Math.round((statistics.pm25Value + statistics.pm10Value) / 2),
          desc: this.form.desc
        })
        alert("检测数据提交成功！✅")
        this.$router.push("/index/tasks")
      }
    }
  }
}
</script>

<style scoped>
.detect-page { max-width: 700px; }
.back-btn { background: none; border: none; color: #11998e; font-size: 14px; cursor: pointer; padding: 0; margin-bottom: 10px; }
.back-btn:hover { text-decoration: underline; }
h2 { font-size: 18px; color: #166953; margin-bottom: 16px; }
.detect-card {
  background: #fff; border-radius: 12px; padding: 24px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.04); margin-bottom: 16px;
}
.detect-card h3 { font-size: 15px; color: #166953; margin-bottom: 14px; padding-bottom: 10px; border-bottom: 1px solid #e8f5ec; }
.info-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; }
.info-item { font-size: 14px; color: #555; }
.info-item:nth-child(3), .info-item:nth-child(4) { grid-column: 1/-1; }
.info-label { color: #11998e; font-weight: 600; }
.form-row { display: flex; gap: 14px; }
.form-item { flex: 1; margin-bottom: 14px; }
.form-item label { display: block; font-size: 13px; color: #666; font-weight: 600; margin-bottom: 4px; }
.form-item input, .form-item textarea {
  width: 100%; padding: 10px 12px; border: 1.5px solid #d4ede3;
  border-radius: 8px; font-size: 14px; color: #333;
  transition: all 0.2s; background: #f9fdf7;
}
.form-item input:focus, .form-item textarea:focus { outline: none; border-color: #38ef7d; box-shadow: 0 0 0 3px rgba(56,239,125,0.1); }
.form-item textarea { resize: vertical; }
.btn-submit {
  width: 100%; padding: 14px; border: none; border-radius: 8px;
  background: linear-gradient(135deg, #11998e, #0d7370);
  color: #fff; font-size: 16px; font-weight: 600; cursor: pointer;
  margin-top: 6px; transition: all 0.2s;
}
.btn-submit:hover { transform: translateY(-1px); box-shadow: 0 4px 14px rgba(17,153,142,0.3); }
</style>
