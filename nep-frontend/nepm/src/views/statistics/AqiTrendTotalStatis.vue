<template>
  <div class="aqi-trend">
    <el-card>
      <div slot="header">
        <span>趋势分析</span>
      </div>
      <el-table :data="tableData" border stripe>
        <el-table-column prop="month" label="月份"></el-table-column>
        <el-table-column prop="totalCount" label="总数"></el-table-column>
        <el-table-column prop="avgAqi" label="平均AQI"></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import api from '@/api'

export default {
  name: 'AqiTrendTotalStatis',
  data() {
    return {
      tableData: []
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    async loadData() {
      const res = await api.get('/statistics/listAqiTrendTotalStatis')
      if (res.code === 200) {
        this.tableData = res.data
      }
    }
  }
}
</script>
