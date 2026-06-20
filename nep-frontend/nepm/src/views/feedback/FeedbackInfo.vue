<template>
  <div class="feedback-info">
    <el-card>
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span>反馈详情</span>
          <el-button @click="goBack">返回</el-button>
        </div>
      </template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="反馈编号">{{ detail.af_code }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(detail.af_state)">{{ getStatusText(detail.af_state) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="监督员">{{ detail.supervisor_name }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ detail.supervisor_tel }}</el-descriptions-item>
        <el-descriptions-item label="反馈类型">{{ getTypeText(detail.af_type) }}</el-descriptions-item>
        <el-descriptions-item label="预估等级">{{ detail.af_level }}</el-descriptions-item>
        <el-descriptions-item label="省份">{{ detail.province_name }}</el-descriptions-item>
        <el-descriptions-item label="城市">{{ detail.city_name }}</el-descriptions-item>
        <el-descriptions-item label="地址" :span="2">{{ detail.af_address }}</el-descriptions-item>
        <el-descriptions-item label="问题描述" :span="2">{{ detail.af_desc }}</el-descriptions-item>
        <el-descriptions-item label="指派网格员">{{ detail.gm_name || '未指派' }}</el-descriptions-item>
        <el-descriptions-item label="指派时间">{{ detail.af_assign_time || '-' }}</el-descriptions-item>
        <el-descriptions-item label="确认AQI">{{ detail.af_confirm_aqi || '-' }}</el-descriptions-item>
        <el-descriptions-item label="确认时间">{{ detail.af_confirm_time || '-' }}</el-descriptions-item>
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
