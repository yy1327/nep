<template>
  <div class="statis-page">
    <div class="page-header">
      <h2>🎯 空气质量等级分布</h2>
      <span class="page-sub">各AQI等级的数据分布情况及占比分析</span>
    </div>

    <!-- 图表区 -->
    <div class="chart-row">
      <el-card class="half-card">
        <div slot="header"><span>🥧 等级占比</span></div>
        <div id="aqiPieChart" class="chart-box-half"></div>
      </el-card>
      <el-card class="half-card">
        <div slot="header"><span>📊 等级统计</span></div>
        <div id="aqiBarChart" class="chart-box-half"></div>
      </el-card>
    </div>

    <!-- 数据明细 -->
    <el-card style="margin-top:16px">
      <div slot="header"><span>📋 数据明细</span></div>
      <el-table :data="tableData" stripe :default-sort="{prop:'count', order:'descending'}">
        <el-table-column prop="aqiLevel" label="AQI等级" sortable>
          <template slot-scope="scope">
            <el-tag :color="getLevelColor(scope.row.aqiLevel)" effect="dark" size="medium">
              {{ scope.row.aqiLevel }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="count" label="数量" sortable width="120" align="center"></el-table-column>
        <el-table-column label="占比" width="200" sortable sort-by="percent">
          <template slot-scope="scope">
            <el-progress
              :percentage="getPercent(scope.row.count)"
              :color="getLevelColor(scope.row.aqiLevel)"
              :stroke-width="14"
              :format="() => getPercent(scope.row.count) + '%'"
            />
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="getPercent(scope.row.count) > 30" type="danger">高占比</el-tag>
            <el-tag v-else-if="getPercent(scope.row.count) > 15" type="warning">中等</el-tag>
            <el-tag v-else type="success">正常</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import api from '@/api'
import * as echarts from 'echarts'

export default {
  name: 'AqiDistributeTotalStatis',
  data() {
    return {
      tableData: [],
      charts: {}
    }
  },
  computed: {
    totalCount() {
      return this.tableData.reduce((sum, d) => sum + Number(d.count || 0), 0)
    }
  },
  created() { this.loadData() },
  beforeDestroy() { Object.values(this.charts).forEach(c => c && c.dispose()) },
  methods: {
    getLevelColor(level) {
      const map = {
        '优': '#00E400', '良': '#FFFF00',
        '轻度污染': '#FF7E00', '中度污染': '#FF0000',
        '重度污染': '#99004C', '严重污染': '#7E0023'
      }
      return map[level] || '#409eff'
    },
    getPercent(count) {
      if (this.totalCount === 0) return 0
      return Math.round((count / this.totalCount) * 100)
    },
    async loadData() {
      const res = await api.get('/statistics/listAqiDistributeTotalStatis')
      if (res.code === 200) {
        this.tableData = res.data || []
        this.$nextTick(() => {
          this.renderPieChart()
          this.renderBarChart()
        })
      }
    },
    renderPieChart() {
      if (this.charts.pie) this.charts.pie.dispose()
      this.charts.pie = echarts.init(document.getElementById('aqiPieChart'))
      const data = this.tableData.filter(d => d.aqiLevel).map(d => ({
        value: d.count,
        name: d.aqiLevel,
        itemStyle: { color: this.getLevelColor(d.aqiLevel) }
      }))
      this.charts.pie.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { orient: 'vertical', right: 10, top: 'center', textStyle: { fontSize: 11 } },
        series: [{
          type: 'pie',
          radius: ['50%', '75%'],
          center: ['40%', '50%'],
          data,
          label: { show: false },
          emphasis: { label: { show: true, fontSize: 16, fontWeight: 'bold' } },
          itemStyle: { borderRadius: 4, borderColor: '#fff', borderWidth: 2 }
        }]
      })
    },
    renderBarChart() {
      if (this.charts.bar) this.charts.bar.dispose()
      this.charts.bar = echarts.init(document.getElementById('aqiBarChart'))
      const sorted = [...this.tableData].filter(d => d.aqiLevel).sort((a, b) => b.count - a.count)
      this.charts.bar.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: 80, right: 20, top: 20, bottom: 20 },
        xAxis: {
          type: 'value',
          axisLabel: { color: '#999' },
          splitLine: { lineStyle: { color: '#f0f0f0' } }
        },
        yAxis: {
          type: 'category',
          data: sorted.map(d => d.aqiLevel).reverse(),
          axisLabel: { color: '#666', fontSize: 11 },
          axisLine: { show: false }, axisTick: { show: false }
        },
        series: [{
          type: 'bar',
          data: sorted.map(d => ({
            value: d.count,
            itemStyle: { color: this.getLevelColor(d.aqiLevel), borderRadius: [0, 4, 4, 0] }
          })).reverse(),
          barWidth: 18,
          label: { show: true, position: 'right', color: '#666', fontSize: 11 }
        }]
      })
    }
  }
}
</script>

<style scoped>
.statis-page { padding: 20px; }
.page-header { margin-bottom: 20px; }
.page-header h2 { font-size: 20px; color: #1b5e20; margin: 0 0 6px; }
.page-sub { color: #999; font-size: 13px; }
.chart-row { display: flex; gap: 16px; }
.half-card { flex: 1; }
.chart-box-half { width: 100%; height: 320px; }
</style>
