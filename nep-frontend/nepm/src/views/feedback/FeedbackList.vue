<template>
  <div class="feedback-page">
    <h2>📋 公众监督员申报管理</h2>
    <p class="page-desc">查看和处理公众监督员提交的环境问题反馈</p>

    <el-card class="search-card">
      <div class="search-bar">
        <el-input v-model="searchForm.afCode" placeholder="反馈编号" clearable style="width:170px"></el-input>
        <el-input v-model="searchForm.supervisorName" placeholder="监督员手机号" clearable style="width:170px"></el-input>
        <el-select v-model="searchForm.afState" placeholder="状态" clearable style="width:130px">
          <el-option label="待指派" value="01"></el-option>
          <el-option label="待确认" value="02"></el-option>
          <el-option label="已完成" value="03"></el-option>
        </el-select>
        <el-input v-model="searchForm.afAddress" placeholder="地址" clearable style="width:160px"></el-input>
        <el-button type="primary" @click="searchData">🔍 搜索</el-button>
        <el-button @click="resetSearch">重置</el-button>
      </div>
    </el-card>

    <el-card style="margin-top:16px">
      <el-table :data="tableData" stripe style="width:100%">
        <el-table-column prop="af_code" label="反馈编号" width="160" fixed>
          <template #default="scope">
            <span class="code-cell">📋 {{ scope.row.af_code }}</span>
          </template>
        </el-table-column>
        <el-table-column label="监督员" width="150">
          <template #default="scope">
            <div class="supervisor-cell">
              <span class="supervisor-icon">👤</span>
              <span>{{ scope.row.supervisor_id }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="af_type" label="申报类型" width="110">
          <template #default="scope">
            <el-tag size="small">{{ typeMap[scope.row.af_type] || scope.row.af_type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="af_level" label="AQI等级" width="100" align="center">
          <template #default="scope">
            <span class="aqi-tag">{{ scope.row.af_level }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="af_address" label="问题地点" min-width="160" show-overflow-tooltip></el-table-column>
        <el-table-column prop="af_state" label="处理状态" width="100" align="center">
          <template #default="scope">
            <el-tag :type="statusTypeMap[scope.row.af_state]" size="small">
              {{ statusMap[scope.row.af_state] || scope.row.af_state }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="create_time" label="申报时间" width="170" sortable>
          <template #default="scope">
            {{ formatTime(scope.row.create_time) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="scope">
            <el-button size="mini" type="primary" plain @click="viewInfo(scope.row.af_id)">📄 详情</el-button>
            <el-button size="mini" type="warning" v-if="scope.row.af_state === '01'" @click="assign(scope.row.af_id)">👷 指派</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @current-change="handlePageChange"
        :current-page="pageNum"
        :page-size="pageSize"
        :total="total"
        layout="total, prev, pager, next"
        style="margin-top:20px;text-align:right"
      ></el-pagination>
    </el-card>
  </div>
</template>

<script>
import api from "@/api"

export default {
  name: "FeedbackList",
  data() {
    return {
      tableData: [],
      searchForm: { afCode: "", supervisorName: "", afState: "", afAddress: "" },
      pageNum: 1,
      pageSize: 5,
      total: 0,
      statusMap: { "01": "待指派", "02": "待确认", "03": "已完成" },
      statusTypeMap: { "01": "warning", "02": "", "03": "success" },
      typeMap: { "01": "空气有异味", "02": "雾霾严重", "03": "工业排放", "04": "其他" }
    }
  },
  created() { this.loadData() },
  activated() { this.loadData() },
  methods: {
    formatTime(val) {
      if (!val) return '-'
      const d = new Date(val)
      if (isNaN(d.getTime())) return val
      return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')} ${String(d.getHours()).padStart(2,'0')}:${String(d.getMinutes()).padStart(2,'0')}`
    },
    async loadData() {
      const res = await api.post("/aqiFeedback/listAqiFeedbackPage", {
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        afCode: this.searchForm.afCode,
        afState: this.searchForm.afState,
        supervisorName: this.searchForm.supervisorName,
        afAddress: this.searchForm.afAddress
      })
      if (res.code === 200) {
        this.tableData = res.data.records || []
        this.total = res.data.total || 0
      }
    },
    searchData() { this.pageNum = 1; this.loadData() },
    resetSearch() {
      this.searchForm = { afCode: "", supervisorName: "", afState: "", afAddress: "" }
      this.pageNum = 1
      this.loadData()
    },
    handlePageChange(page) { this.pageNum = page; this.loadData() },
    viewInfo(id) { this.$router.push(`/index/feedbackInfo/${id}`) },
    assign(id) { this.$router.push(`/index/assignFeedback/${id}`) },
  },
}
</script>

<style scoped>
.feedback-page { padding: 20px; animation: fadeInUp 0.4s ease-out; }
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(12px); }
  to { opacity: 1; transform: translateY(0); }
}
.feedback-page h2 { font-size: 20px; color: #1b5e20; margin: 0 0 6px; font-weight: 700; line-height: 1.4; }
.page-desc { color: #999; font-size: 13px; margin-bottom: 18px; line-height: 1.5; }
.search-card { margin-bottom: 0; border-radius: 12px; }
.search-bar { display: flex; gap: 10px; flex-wrap: wrap; align-items: center; }
.code-cell { font-weight: 700; color: #333; }
.supervisor-cell { display: flex; align-items: center; gap: 8px; }
.supervisor-icon { font-size: 16px; }
.aqi-tag { font-weight: 600; color: #e6a23c; }

:deep(.el-table) { font-size: 14px; }
:deep(.el-table .cell) { line-height: 1.6; padding: 10px 12px; }
:deep(.el-table th .cell) { font-weight: 600; color: #333; }
</style>
