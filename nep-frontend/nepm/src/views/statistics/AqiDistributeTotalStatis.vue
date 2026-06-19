<template>
  <div class="aqi-distribute">
    <el-card>
      <div slot="header">
        <span>等级分布</span>
      </div>
      <el-table :data="tableData" border stripe>
        <el-table-column prop="aqiLevel" label="AQI等级"></el-table-column>
        <el-table-column prop="count" label="数量"></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import api from '@/api'

export default {
  name: 'AqiDistributeTotalStatis',
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
      const res = await api.get('/statistics/listAqiDistributeTotalStatis')
      if (res.code === 200) {
        this.tableData = res.data
      }
    }
  }
}
</script>
