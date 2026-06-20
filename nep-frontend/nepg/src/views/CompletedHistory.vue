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
          <span>🕐 确认时间：{{ item.af_confirm_time || '-' }}</span>
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
.history-page { max-width: 800px; }
h2 { font-size: 18px; color: #166953; margin-bottom: 16px; }
.empty { text-align: center; padding: 60px; color: #999; background: #fff; border-radius: 12px; }
.empty-icon { font-size: 48px; display: block; margin-bottom: 10px; }
.list { display: flex; flex-direction: column; gap: 12px; }
.item-card {
  background: #fff; border-radius: 12px; padding: 20px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.04);
  border-left: 4px solid #67c23a;
}
.item-top { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.item-code { font-weight: 600; color: #166953; font-size: 15px; }
.item-badge { background: #e8f5e9; color: #388e3c; padding: 2px 12px; border-radius: 20px; font-size: 12px; font-weight: 600; }
.item-body { margin-bottom: 12px; }
.info-row { margin: 6px 0; color: #555; font-size: 14px; }
.info-label { color: #11998e; font-weight: 600; }
.aqi-val { font-weight: 700; color: #e6a23c; font-size: 16px; }
.item-footer { padding-top: 10px; border-top: 1px solid #e8f5ec; color: #999; font-size: 12px; }
</style>
