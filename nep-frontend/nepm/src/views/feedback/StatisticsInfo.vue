<template>
  <div class="statistics-info">
    <div class="page-header">
      <h2>📊 统计详情</h2>
      <span class="page-sub">查看空气质量监测数据的详细信息</span>
    </div>
    <el-card class="info-card">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span class="card-title">统计ID：{{ detail.statisticsId }}</span>
          <el-button @click="goBack" type="primary" plain>← 返回列表</el-button>
        </div>
      </template>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="统计ID">{{
          detail.statisticsId
        }}</el-descriptions-item>
        <el-descriptions-item label="AQI值">{{
          detail.aqiValue
        }}</el-descriptions-item>
        <el-descriptions-item label="AQI等级">{{
          detail.aqiLevel
        }}</el-descriptions-item>
        <el-descriptions-item label="PM2.5"
          >{{ detail.pm25Value }} μg/m³</el-descriptions-item
        >
        <el-descriptions-item label="PM10"
          >{{ detail.pm10Value }} μg/m³</el-descriptions-item
        >
        <el-descriptions-item label="SO2"
          >{{ detail.so2Value }} μg/m³</el-descriptions-item
        >
        <el-descriptions-item label="NO2"
          >{{ detail.no2Value }} μg/m³</el-descriptions-item
        >
        <el-descriptions-item label="CO"
          >{{ detail.coValue }} mg/m³</el-descriptions-item
        >
        <el-descriptions-item label="O3"
          >{{ detail.o3Value }} μg/m³</el-descriptions-item
        >
        <el-descriptions-item label="统计日期">{{
          formatDate(detail.statisticsDate)
        }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script>
import api from "@/api";

export default {
  name: "StatisticsInfo",
  data() {
    return {
      detail: {},
    };
  },
  created() {
    this.loadDetail();
  },
  methods: {
    formatDate(val) {
      if (!val) return '-'
      const d = new Date(val)
      if (isNaN(d.getTime())) return val
      return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')}`
    },
    async loadDetail() {
      const id = this.$route.params.id;
      const res = await api.post("/statistics/getStatisticsById", {
        statisticsId: parseInt(id),
      });
      if (res.code === 200) {
        this.detail = res.data;
      }
    },
    goBack() {
      this.$router.push("/index/statisticsList");
    },
  },
};
</script>

<style scoped>
.statistics-info { padding: 20px; animation: fadeInUp 0.4s ease-out; }
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
