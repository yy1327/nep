<template>
  <div class="province-statis">
    <el-card>
      <div slot="header">
        <span>按省统计</span>
      </div>
      <el-table :data="tableData" border stripe>
        <el-table-column prop="province_name" label="省份"></el-table-column>
        <el-table-column prop="total_count" label="总数"></el-table-column>
        <el-table-column prop="good_count" label="良好数"></el-table-column>
        <el-table-column prop="polluted_count" label="污染数"></el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import api from '@/api'

export default {
  name: 'ProvinceItemTotalStatis',
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
      const res = await api.get('/statistics/listProvinceItemTotalStatis')
      if (res.code === 200) {
        this.tableData = res.data
      }
    }
  }
}
</script>
