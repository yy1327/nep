<template>
  <div class="statistics-info">
    <el-card>
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span>统计详情</span>
          <el-button @click="goBack">返回</el-button>
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
          detail.statisticsDate
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
