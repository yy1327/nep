<template>
  <div class="statistics-list">
    <el-card>
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
        <el-button type="primary" @click="loadData">搜索</el-button>
      </div>
      <el-table :data="tableData" border stripe>
        <el-table-column
          prop="statisticsId"
          label="ID"
          width="80"
        ></el-table-column>
        <el-table-column prop="aqiValue" label="AQI值"></el-table-column>
        <el-table-column prop="aqiLevel" label="等级"></el-table-column>
        <el-table-column prop="pm25Value" label="PM2.5"></el-table-column>
        <el-table-column prop="pm10Value" label="PM10"></el-table-column>
        <el-table-column prop="statisticsDate" label="日期"></el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button size="mini" @click="viewInfo(scope.row.statisticsId)"
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
.search-bar {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}
.el-pagination {
  margin-top: 20px;
  text-align: right;
}
</style>
