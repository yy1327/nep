<template>
  <div class="feedback-list">
    <el-card>
      <div class="search-bar">
        <el-input
          v-model="searchForm.afCode"
          placeholder="反馈编号"
          clearable
          style="width: 150px"
        ></el-input>
        <el-select
          v-model="searchForm.afState"
          placeholder="状态"
          clearable
          style="width: 120px"
        >
          <el-option label="待指派" value="01"></el-option>
          <el-option label="待确认" value="02"></el-option>
          <el-option label="已完成" value="03"></el-option>
        </el-select>
        <el-button type="primary" @click="loadData">搜索</el-button>
      </div>
      <el-table :data="tableData" border stripe>
        <el-table-column
          prop="af_code"
          label="反馈编号"
          width="150"
        ></el-table-column>
        <el-table-column prop="supervisor_id" label="监督员"></el-table-column>
        <el-table-column prop="af_type" label="类型">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.af_type)">
              {{ getTypeText(scope.row.af_type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="af_level" label="等级"></el-table-column>
        <el-table-column prop="af_address" label="地址"></el-table-column>
        <el-table-column prop="af_state" label="状态">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.af_state)">{{
              getStatusText(scope.row.af_state)
            }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="create_time"
          label="创建时间"
          width="180"
        ></el-table-column>
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="viewInfo(scope.row.af_id)"
              >详情</el-button
            >
            <el-button
              size="mini"
              type="warning"
              v-if="scope.row.af_state === '01'"
              @click="assign(scope.row.af_id)"
              >指派</el-button
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
  name: "FeedbackList",
  data() {
    return {
      tableData: [],
      searchForm: {
        afCode: "",
        afState: "",
      },
      page_num: 1,
      page_size: 10,
      total: 0,
    };
  },
  created() {
    this.loadData();
  },
  // 【新增】当被 keep-alive 缓存的组件激活时调用
  activated() {
    // 这里重新加载数据，确保返回时看到最新的状态
    // 注意：这也会保留你之前的搜索条件和分页，因为 data 没有被重置
    this.loadData();
  },
  methods: {
    async loadData() {
      const res = await api.post("/aqiFeedback/listAqiFeedbackPage", {
        pageNum: this.page_num,
        pageSize: this.page_size,
        ...this.searchForm,
      });
      if (res.code === 200) {
        this.tableData = res.data.records || [];
        this.total = res.data.total || 0;
      }
    },
    getTypeText(type) {
      const map = {
        "01": "空气有异味",
        "02": "雾霾严重",
        "03": "工业排放",
        "04": "其他",
      };
      return map[type] || type;
    },
    getStatusText(state) {
      const map = { "01": "待指派", "02": "待确认", "03": "已完成" };
      return map[state] || state;
    },
    getStatusType(state) {
      const map = {
        "01": "warning",
        "02": "primary",
        "03": "success",
        "04": "danger",
      };
      return map[state] || "info";
    },
    handlePageChange(page) {
      this.page_num = page;
      this.loadData();
    },
    viewInfo(id) {
      this.$router.push(`/index/feedbackInfo/${id}`);
    },
    assign(id) {
      this.$router.push(`/index/assignFeedback/${id}`);
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
