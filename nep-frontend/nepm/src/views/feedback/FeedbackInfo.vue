<template>
  <div class="feedback-info">
    <div class="page-header">
      <h2>📋 反馈详情</h2>
      <span class="page-sub">查看公众监督员提交的环境问题反馈完整信息</span>
    </div>
    <el-card class="info-card">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span class="card-title">反馈编号：{{ detail.af_code }}</span>
          <el-button @click="goBack" type="primary" plain>← 返回列表</el-button>
        </div>
      </template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="反馈编号">{{ detail.af_code }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(detail.af_state)">{{ getStatusText(detail.af_state) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="监督员编号">{{ detail.supervisor_id }}</el-descriptions-item>
        <el-descriptions-item label="监督员姓名">{{ detail.supervisor_name || '-' }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detail.supervisor_tel || detail.supervisor_id }}</el-descriptions-item>
        <el-descriptions-item label="反馈类型">{{ getTypeText(detail.af_type) }}</el-descriptions-item>
        <el-descriptions-item label="预估等级">{{ detail.af_level }}</el-descriptions-item>
        <el-descriptions-item label="省份">{{ detail.province_name }}</el-descriptions-item>
        <el-descriptions-item label="城市">{{ detail.city_name }}</el-descriptions-item>
        <el-descriptions-item label="地址" :span="2">{{ detail.af_address }}</el-descriptions-item>
        <el-descriptions-item label="问题描述" :span="2">{{ detail.af_desc }}</el-descriptions-item>
        <el-descriptions-item label="指派网格员">{{ detail.gm_name || '未指派' }}</el-descriptions-item>
        <el-descriptions-item label="指派时间">{{ formatTime(detail.af_assign_time) }}</el-descriptions-item>
        <el-descriptions-item label="确认AQI">{{ detail.af_confirm_aqi || '-' }}</el-descriptions-item>
        <el-descriptions-item label="确认时间">{{ formatTime(detail.af_confirm_time) }}</el-descriptions-item>
        <el-descriptions-item label="确认备注" :span="2">{{ detail.af_confirm_desc || '-' }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script>
import api from '@/api'

export default {
  name: 'FeedbackInfo',
  data() {
    return {
      detail: {}
    }
  },
  created() {
    this.loadDetail()
  },
  methods: {
    formatTime(val) {
      if (!val) return '-'
      const d = new Date(val)
      if (isNaN(d.getTime())) return val
      return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')} ${String(d.getHours()).padStart(2,'0')}:${String(d.getMinutes()).padStart(2,'0')}`
    },
    async loadDetail() {
      const id = this.$route.params.id
      const res = await api.post('/aqiFeedback/getAqiFeedbackById', { afId: parseInt(id) })
      if (res.code === 200) {
        this.detail = res.data
      }
    },
    getTypeText(type) {
      const map = { '01': '空气有异味', '02': '雾霾严重', '03': '工业排放', '04': '其他' }
      return map[type] || type
    },
    getStatusText(state) {
      const map = { '01': '待指派', '02': '待确认', '03': '已完成' }
      return map[state] || state
    },
    getStatusType(state) {
      const map = { '01': 'warning', '02': 'primary', '03': 'success' }
      return map[state] || 'info'
    },
    goBack() {
      this.$router.push('/index/feedbackList')
    }
  }
}
</script>

<style scoped>
.feedback-info { padding: 20px; animation: fadeInUp 0.4s ease-out; }
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(12px); }
  to { opacity: 1; transform: translateY(0); }
}
.page-header { margin-bottom: 20px; }
.page-header h2 { font-size: 20px; color: #1b5e20; margin: 0 0 6px; font-weight: 700; line-height: 1.4; }
.page-sub { color: #999; font-size: 13px; line-height: 1.5; }
.info-card { border-radius: 12px; }
.card-title { font-size: 16px; font-weight: 600; color: #333; }

:deep(.el-descriptions) { font-size: 14px; }
:deep(.el-descriptions__label) { font-weight: 600; color: #555; }
:deep(.el-descriptions__content) { color: #333; line-height: 1.6; }
</style>
