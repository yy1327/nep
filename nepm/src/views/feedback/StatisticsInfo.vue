<template>
  <div class="statistics-info">
    <el-card>
      <div slot="header">
        <span>统计详情</span>
        <el-button style="float: right" @click="goBack">返回</el-button>
      </div>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="统计ID">{{ detail.statistics_id }}</el-descriptions-item>
        <el-descriptions-item label="AQI值">{{ detail.aqi_value }}</el-descriptions-item>
        <el-descriptions-item label="AQI等级">{{ detail.aqi_level }}</el-descriptions-item>
        <el-descriptions-item label="PM2.5">{{ detail.pm25_value }} μg/m³</el-descriptions-item>
        <el-descriptions-item label="PM10">{{ detail.pm10_value }} μg/m³</el-descriptions-item>
        <el-descriptions-item label="SO2">{{ detail.so2_value }} μg/m³</el-descriptions-item>
        <el-descriptions-item label="NO2">{{ detail.no2_value }} μg/m³</el-descriptions-item>
        <el-descriptions-item label="CO">{{ detail.co_value }} mg/m³</el-descriptions-item>
        <el-descriptions-item label="O3">{{ detail.o3_value }} μg/m³</el-descriptions-item>
        <el-descriptions-item label="统计日期">{{ detail.statistics_date }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script>
import api from '@/api'

export default {
  name: 'StatisticsInfo',
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
      const res = await api.post('/statistics/getStatisticsById', { statisticsId: parseInt(id) })
      if (res.code === 200) {
        this.detail = res.data
      }
    },
    goBack() {
      this.$router.push('/index/statisticsList')
    }
  }
}
</script>
