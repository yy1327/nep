<template>
  <div class="statistics-list">
    <div class="page-header">
      <h2>📊 统计列表</h2>
      <span class="page-sub">查看所有空气质量监测统计数据</span>
    </div>
    <el-card class="search-card">
      <div class="search-bar">
        <el-select
          v-model="searchForm.provinceId"
          placeholder="省份"
          clearable
          @change="loadData"
        >
          <el-option
            v-for="p in provinces"
            :key="p.provinceId"
            :label="p.provinceName"
            :value="p.provinceId"
          ></el-option>
        </el-select>
        <el-select
          v-model="searchForm.aqiLevel"
          placeholder="AQI等级"
          clearable
          @change="loadData"
        >
          <el-option label="优" value="优"></el-option>
          <el-option label="良" value="良"></el-option>
          <el-option label="轻度污染" value="轻度污染"></el-option>
          <el-option label="中度污染" value="中度污染"></el-option>
          <el-option label="重度污染" value="重度污染"></el-option>
          <el-option label="严重污染" value="严重污染"></el-option>
        </el-select>
        <el-button type="primary" @click="loadData">🔍 搜索</el-button>
      </div>
      <el-table :data="tableData" border stripe style="width:100%">
        <el-table-column
          prop="statisticsId"
          label="ID"
          width="80"
        ></el-table-column>
        <el-table-column prop="aqiValue" label="AQI值" width="100" align="center"></el-table-column>
        <el-table-column prop="aqiLevel" label="等级" width="120">
          <template #default="scope">
            <el-tag size="small">{{ scope.row.aqiLevel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="pm25Value" label="PM2.5" width="120" align="center"></el-table-column>
        <el-table-column prop="pm10Value" label="PM10" width="120" align="center"></el-table-column>
        <el-table-column prop="statisticsDate" label="日期" width="170">
          <template #default="scope">
            {{ formatDate(scope.row.statisticsDate) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" align="center">
          <template #default="scope">
            <el-button size="small" type="primary" plain @click="viewInfo(scope.row.statisticsId)"
              >详情</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @current-change="handlePageChange"
        :current-page="page_num"
        :page-size="page_size"
        :total="total"
        layout="total, prev, pager, next"
        style="margin-top:20px;text-align:right"
      >
      </el-pagination>
    </el-card>
  </div>
</template>

<script>
import api from "@/api";

export default {
  name: "StatisticsList",
  data() {
    return {
      tableData: [],
      provinces: [],
      searchForm: {
        provinceId: "",
        aqiLevel: "",
      },
      page_num: 1,
      page_size: 10,
      total: 0,
    };
  },
  created() {
    this.loadProvinces();
    this.loadData();
  },
  methods: {
    formatDate(val) {
      if (!val) return '-'
      const d = new Date(val)
      if (isNaN(d.getTime())) return val
      return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')}`
    },
    async loadProvinces() {
      const res = await api.post("/gridProvince/listGridProvinceAll");
      if (res.code === 200) {
        this.provinces = res.data;
      }
    },
    async loadData() {
      const res = await api.post("/statistics/listStatisticsPage", {
        pageNum: this.page_num,
        pageSize: this.page_size,
        ...this.searchForm,
      });
      if (res.code === 200) {
        this.tableData = res.data.records || [];
        this.total = res.data.total || 0;
      }
    },
    handlePageChange(page) {
      this.page_num = page;
      this.loadData();
    },
    viewInfo(id) {
      this.$router.push(`/index/statisticsInfo/${id}`);
    },
  },
};
</script>

<style scoped>
.statistics-list { padding: 20px; animation: fadeInUp 0.4s ease-out; }
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(12px); }
  to { opacity: 1; transform: translateY(0); }
}
.page-header { margin-bottom: 20px; }
.page-header h2 { font-size: 20px; color: #1b5e20; margin: 0 0 6px; font-weight: 700; line-height: 1.4; }
.page-sub { color: #999; font-size: 13px; line-height: 1.5; }
.search-card { border-radius: 12px; }
.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  flex-wrap: wrap;
  align-items: center;
}
:deep(.el-table) { font-size: 14px; }
:deep(.el-table .cell) { line-height: 1.6; padding: 10px 12px; }
:deep(.el-table th .cell) { font-weight: 600; color: #333; }
:deep(.el-descriptions) { font-size: 14px; }
:deep(.el-descriptions__label) { font-weight: 600; color: #555; }
:deep(.el-descriptions__content) { color: #333; line-height: 1.6; }
</style>
