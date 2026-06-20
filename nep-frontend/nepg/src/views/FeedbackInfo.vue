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
            <input type="number" step="0.01" min="0" max="1000" v-model="form.pm25Value" placeholder="0.00" required />
          </div>
          <div class="form-item">
            <label>PM10 (μg/m³)</label>
            <input type="number" step="0.01" min="0" max="1000" v-model="form.pm10Value" placeholder="0.00" required />
          </div>
        </div>
        <div class="form-row">
          <div class="form-item">
            <label>SO₂ (μg/m³)</label>
            <input type="number" step="0.01" min="0" max="1000" v-model="form.so2Value" placeholder="0.00" required />
          </div>
          <div class="form-item">
            <label>NO₂ (μg/m³)</label>
            <input type="number" step="0.01" min="0" max="1000" v-model="form.no2Value" placeholder="0.00" required />
          </div>
        </div>
        <div class="form-row">
          <div class="form-item">
            <label>CO (mg/m³)</label>
            <input type="number" step="0.01" min="0" max="50" v-model="form.coValue" placeholder="0.00" required />
          </div>
          <div class="form-item">
            <label>O₃ (μg/m³)</label>
            <input type="number" step="0.01" min="0" max="1000" v-model="form.o3Value" placeholder="0.00" required />
          </div>
        </div>
        <div class="form-item">
          <label>📝 检测备注</label>
          <textarea v-model="form.desc" rows="3" placeholder="请输入检测备注信息..."></textarea>
        </div>
        <button type="submit" class="btn-submit" :disabled="submitting">
          {{ submitting ? '提交中...' : '📤 提交检测数据' }}
        </button>
      </form>
    </div>
  </div>
</template>

<script>
import api from "@/api"
import { showToast } from "@/utils/toast"

export default {
  name: "FeedbackInfo",
  data() {
    return {
      detail: {},
      submitting: false,
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
      if (this.submitting) return
      const pm25 = parseFloat(this.form.pm25Value)
      const pm10 = parseFloat(this.form.pm10Value)
      const so2 = parseFloat(this.form.so2Value)
      const no2 = parseFloat(this.form.no2Value)
      const co = parseFloat(this.form.coValue)
      const o3 = parseFloat(this.form.o3Value)
      if (isNaN(pm25) || pm25 < 0 || pm25 > 1000) { showToast('PM2.5 范围 0~1000', 'warning'); return }
      if (isNaN(pm10) || pm10 < 0 || pm10 > 1000) { showToast('PM10 范围 0~1000', 'warning'); return }
      if (isNaN(so2) || so2 < 0 || so2 > 1000) { showToast('SO₂ 范围 0~1000', 'warning'); return }
      if (isNaN(no2) || no2 < 0 || no2 > 1000) { showToast('NO₂ 范围 0~1000', 'warning'); return }
      if (isNaN(co) || co < 0 || co > 50) { showToast('CO 范围 0~50', 'warning'); return }
      if (isNaN(o3) || o3 < 0 || o3 > 1000) { showToast('O₃ 范围 0~1000', 'warning'); return }
      this.submitting = true
      try {
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
          const stateRes = await api.post("/aqiFeedback/updateAqiFeedbackState", {
            afId: parseInt(id), state: "03",
            aqi: Math.round((statistics.pm25Value + statistics.pm10Value) / 2),
            desc: this.form.desc
          })
          if (stateRes.code === 200) {
            showToast("检测数据提交成功！", "success")
            this.$router.push("/index/tasks")
          } else {
            showToast("数据已保存但状态更新失败：" + stateRes.msg, "warning")
            this.$router.push("/index/tasks")
          }
        } else {
          showToast("提交失败：" + statRes.msg, "error")
        }
      } catch (e) {
        showToast("提交失败，请检查网络连接", "error")
      } finally {
        this.submitting = false
      }
    }
  }
}
</script>

<style scoped>
.detect-page {
  max-width: 700px;
  animation: fadeInUp 0.4s ease-out;
}
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(12px); }
  to { opacity: 1; transform: translateY(0); }
}
.back-btn {
  background: none; border: none; color: #11998e;
  font-size: 14px; cursor: pointer; padding: 0; margin-bottom: 12px;
  transition: all 0.2s;
}
.back-btn:hover { color: #0d7370; text-decoration: underline; }
h2 { font-size: 20px; color: #166953; margin-bottom: 20px; font-weight: 700; line-height: 1.4; }
.detect-card {
  background: #fff; border-radius: 14px; padding: 28px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.04);
  margin-bottom: 18px;
  animation: fadeInUp 0.4s ease-out both;
}
.detect-card:nth-child(2) { animation-delay: 0.1s; }
.detect-card:nth-child(3) { animation-delay: 0.15s; }
.detect-card h3 {
  font-size: 16px; color: #166953; margin-bottom: 18px;
  padding-bottom: 14px; border-bottom: 1px solid #e8f5ec;
  font-weight: 700;
  line-height: 1.4;
}
.info-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 14px; }
.info-item { font-size: 14px; color: #555; line-height: 1.7; }
.info-item:nth-child(3), .info-item:nth-child(4) { grid-column: 1/-1; }
.info-label { color: #11998e; font-weight: 600; margin-right: 4px; }
.form-row { display: flex; gap: 16px; }
.form-item { flex: 1; margin-bottom: 18px; }
.form-item label {
  display: block; font-size: 13px; color: #666;
  font-weight: 600; margin-bottom: 8px; line-height: 1.4;
}
.form-item input, .form-item textarea {
  width: 100%; padding: 12px 14px; border: 1.5px solid #d4ede3;
  border-radius: 10px; font-size: 14px; color: #333;
  transition: all 0.25s ease; background: #f9fdf7;
}
.form-item input:focus, .form-item textarea:focus {
  outline: none; border-color: #38ef7d;
  box-shadow: 0 0 0 3px rgba(56,239,125,0.1);
  background: #fff;
}
.form-item textarea { resize: vertical; min-height: 80px; }
.btn-submit {
  width: 100%; padding: 15px; border: none; border-radius: 10px;
  background: linear-gradient(135deg, #11998e, #0d7370);
  color: #fff; font-size: 16px; font-weight: 600; cursor: pointer;
  margin-top: 10px; transition: all 0.3s ease;
  letter-spacing: 0.5px;
  box-shadow: 0 4px 14px rgba(17,153,142,0.25);
  line-height: 1.4;
}
.btn-submit:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(17,153,142,0.35);
  background: linear-gradient(135deg, #14b8a6, #11998e);
}
.btn-submit:active { transform: translateY(0); }
.btn-submit:disabled {
  opacity: 0.6; cursor: not-allowed; transform: none;
  box-shadow: none;
}

@media (max-width: 768px) {
  .detect-page { max-width: 100%; }
  .form-row { flex-direction: column; gap: 0; }
  .detect-card { padding: 20px; }
}
</style>
