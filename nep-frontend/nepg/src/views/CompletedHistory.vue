<template>
  <div class="history-page">
    <h2>✅ 已完成检测记录</h2>
    <div v-if="list.length === 0" class="empty">
      <span class="empty-icon">📭</span>
      <p>暂无已完成的检测记录</p>
    </div>
    <div v-else class="list">
      <div v-for="item in list" :key="item.af_id" class="item-card">
        <div class="item-top">
          <span class="item-code">📋 {{ item.af_code }}</span>
          <span class="item-badge">✅ 已完成</span>
        </div>
        <div class="item-body">
          <div class="info-row"><span class="info-label">类型：</span>{{ typeMap[item.af_type] || item.af_type }}</div>
          <div class="info-row"><span class="info-label">地址：</span>{{ item.af_address }}</div>
          <div class="info-row"><span class="info-label">描述：</span>{{ item.af_desc }}</div>
          <div class="info-row"><span class="info-label">检测AQI：</span>
            <span class="aqi-val">{{ item.af_confirm_aqi || '-' }}</span>
          </div>
          <div class="info-row"><span class="info-label">检测备注：</span>{{ item.af_confirm_desc || '-' }}</div>
        </div>
        <div class="item-footer">
          <span>🕐 确认时间：{{ formatTime(item.af_confirm_time) }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import api from '@/api'

export default {
  name: 'CompletedHistory',
  data() {
    return {
      list: [],
      typeMap: { '01': '空气有异味', '02': '雾霾严重', '03': '工业排放', '04': '其他' }
    }
  },
  created() { this.loadData() },
  methods: {
    formatTime(val) {
      if (!val) return '-'
      const d = new Date(val)
      if (isNaN(d.getTime())) return val
      return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')} ${String(d.getHours()).padStart(2,'0')}:${String(d.getMinutes()).padStart(2,'0')}`
    },
    async loadData() {
      const gm = JSON.parse(sessionStorage.getItem('gridMember'))
      if (!gm) return
      const res = await api.post('/aqiFeedback/listAqiFeedbackByGmId', { gmId: gm.gmId })
      if (res.code === 200) {
        this.list = (res.data || []).filter(item => item.af_state === '03')
      }
    }
  }
}
</script>

<style scoped>
.history-page {
  max-width: 800px;
  animation: fadeInUp 0.4s ease-out;
}
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(12px); }
  to { opacity: 1; transform: translateY(0); }
}
h2 { font-size: 20px; color: #166953; margin-bottom: 20px; font-weight: 700; line-height: 1.4; }
.empty {
  text-align: center; padding: 64px 20px; color: #999;
  background: #fff; border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.04);
}
.empty-icon { font-size: 52px; display: block; margin-bottom: 12px; }
.list { display: flex; flex-direction: column; gap: 14px; }
.item-card {
  background: #fff; border-radius: 14px; padding: 22px 24px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.04);
  border-left: 4px solid #67c23a;
  transition: all 0.3s ease;
}
.item-card:hover {
  box-shadow: 0 4px 16px rgba(0,0,0,0.08);
  transform: translateY(-1px);
}
.item-top { display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px; }
.item-code { font-weight: 700; color: #166953; font-size: 15px; letter-spacing: 0.3px; }
.item-badge {
  background: #e8f5e9; color: #2e7d32;
  padding: 4px 14px; border-radius: 20px;
  font-size: 12px; font-weight: 600;
}
.item-body { margin-bottom: 14px; }
.info-row { margin: 9px 0; color: #555; font-size: 14px; line-height: 1.7; }
.info-label { color: #11998e; font-weight: 600; margin-right: 4px; }
.aqi-val { font-weight: 700; color: #e6a23c; font-size: 16px; line-height: 1.4; }
.item-footer {
  padding-top: 12px; border-top: 1px solid #e8f5ec;
  color: #999; font-size: 12px;
}

@media (max-width: 768px) {
  .history-page { max-width: 100%; }
  .item-card { padding: 16px; }
}
</style>
