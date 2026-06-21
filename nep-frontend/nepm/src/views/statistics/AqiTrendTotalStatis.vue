<template>
  <div class="statis-page">
    <div class="page-header">
      <h2>📈 月度AQI趋势分析</h2>
      <span class="page-sub">12个月内空气质量监测数据变化趋势</span>
    </div>

    <!-- 趋势概览 -->
    <div class="trend-summary">
      <div class="trend-card" v-for="item in trendSummary" :key="item.label">
        <span class="tr-label">{{ item.label }}</span>
        <span class="tr-value" :style="{ color: item.color }">{{ item.value }}</span>
      </div>
    </div>

    <!-- 趋势图 -->
    <el-card>
      <template #header><span>📉 月度变化趋势</span></template>
      <div id="trendLineChart" class="chart-box-tall"></div>
    </el-card>

    <!-- 数据表格 -->
    <el-card style="margin-top:16px">
      <template #header><span>📋 月度数据明细</span></template>
      <el-table :data="tableData" stripe>
        <el-table-column prop="month" label="月份" width="120" sortable>
          <template #default="scope">
            <span style="font-weight:600">{{ scope.row.month }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="totalCount" label="监测次数" width="140" sortable align="center">
          <template #default="scope">
            <el-tag type="primary">{{ scope.row.totalCount }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="avgAqi" label="平均AQI" width="140" sortable align="center">
          <template #default="scope">
            <span :style="{ color: getAqiColor(scope.row.avgAqi), fontWeight: 'bold', fontSize: '16px' }">
              {{ Number(scope.row.avgAqi || 0).toFixed(1) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="AQI等级" width="120" align="center">
          <template #default="scope">
            <el-tag :color="getAqiLevelColor(scope.row.avgAqi)" effect="dark" size="medium">
              {{ getAqiLevel(scope.row.avgAqi) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="较上月变化" width="140" align="center">
          <template #default="scope">
            <span v-if="scope.$index === 0" style="color:#999">-</span>
            <span v-else :style="{ color: getChangeColor(scope.row, scope.$index) }">
              {{ getChange(scope.row, scope.$index) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="趋势" min-width="200">
          <template #default="scope">
            <el-progress
              :percentage="getTrendPercent(scope.row.avgAqi)"
              :color="getProgressGradient(scope.row.avgAqi)"
              :stroke-width="10"
            />
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
  name: 'AqiTrendTotalStatis',
  data() {
    return { tableData: [], chart: null }
  },
  computed: {
    trendSummary() {
      if (!this.tableData.length) return []
      const total = this.tableData.reduce((s, d) => s + Number(d.totalCount || 0), 0)
      const avgAqi = this.tableData.reduce((s, d) => s + Number(d.avgAqi || 0), 0) / this.tableData.length
      const maxMonth = [...this.tableData].sort((a, b) => b.totalCount - a.totalCount)[0]
      const minMonth = [...this.tableData].sort((a, b) => a.totalCount - b.totalCount)[0]
      return [
        { label: '累计监测', value: total + ' 次', color: '#409eff' },
        { label: '月均AQI', value: avgAqi.toFixed(1), color: this.getAqiColor(avgAqi) },
        { label: '最高月份', value: maxMonth ? maxMonth.month : '-', color: '#f56c6c' },
        { label: '最低月份', value: minMonth ? minMonth.month : '-', color: '#67c23a' }
      ]
    }
  },
  mounted() {
    this.loadData()
    this._resizeHandler = () => { if (this.chart) this.chart.resize() }
    window.addEventListener('resize', this._resizeHandler)
  },
  beforeUnmount() {
    window.removeEventListener('resize', this._resizeHandler)
    if (this.chart) this.chart.dispose()
  },
  methods: {
    getAqiColor(val) {
      val = Number(val)
      if (val > 200) return '#7E0023'
      if (val > 150) return '#99004C'
      if (val > 100) return '#FF0000'
      if (val > 50) return '#FF7E00'
      return '#00E400'
    },
    getAqiLevel(val) {
      val = Number(val)
      if (val > 300) return '严重污染'
      if (val > 200) return '重度污染'
      if (val > 150) return '中度污染'
      if (val > 100) return '轻度污染'
      if (val > 50) return '良'
      return '优'
    },
    getAqiLevelColor(val) {
      return this.getAqiColor(val)
    },
    getChange(row, idx) {
      const prev = Number(this.tableData[idx - 1].avgAqi || 0)
      const curr = Number(row.avgAqi || 0)
      const diff = curr - prev
      if (diff > 0) return '↑' + diff.toFixed(1)
      if (diff < 0) return '↓' + Math.abs(diff).toFixed(1)
      return '持平'
    },
    getChangeColor(row, idx) {
      const prev = Number(this.tableData[idx - 1].avgAqi || 0)
      const curr = Number(row.avgAqi || 0)
      if (curr > prev) return '#f56c6c'
      if (curr < prev) return '#67c23a'
      return '#999'
    },
    getTrendPercent(val) {
      return Math.min(Math.round((Number(val) / 300) * 100), 100)
    },
    getProgressGradient(val) {
      return this.getAqiColor(val)
    },
    async loadData() {
      const res = await api.get('/statistics/listAqiTrendTotalStatis')
      if (res.code === 200) {
        this.tableData = res.data || []
        this.renderChart()
      }
    },
    renderChart() {
      if (!this.chart) {
        this.chart = echarts.init(document.getElementById('trendLineChart'))
      }
      if (!this.tableData.length) {
        this.chart.setOption({
          title: { text: '暂无数据', left: 'center', top: 'center', textStyle: { color: '#999', fontSize: 14 } },
          tooltip: { trigger: 'axis' },
          legend: { show: false },
          xAxis: { type: 'category', data: [] },
          yAxis: [{ type: 'value' }, { type: 'value' }],
          series: []
        })
        return
      }
      this.chart.setOption({
        title: { text: '' },
        tooltip: { trigger: 'axis', backgroundColor: 'rgba(255,255,255,0.96)', borderColor: '#e8e8e8', textStyle: { color: '#333' } },
        legend: { show: true, data: ['监测次数', '平均AQI'], bottom: 0, textStyle: { color: '#999', fontSize: 11 } },
        grid: { left: 50, right: 50, top: 30, bottom: 40 },
        xAxis: {
          type: 'category',
          data: this.tableData.map(d => d.month || ''),
          axisLabel: { color: '#666', fontSize: 11, rotate: 30 },
          axisLine: { lineStyle: { color: '#ddd' } }
        },
        yAxis: [
          {
            type: 'value', name: '次数',
            nameTextStyle: { color: '#999', fontSize: 11 },
            axisLabel: { color: '#999', fontSize: 11 },
            splitLine: { lineStyle: { color: '#f0f0f0' } }
          },
          {
            type: 'value', name: 'AQI',
            nameTextStyle: { color: '#999', fontSize: 11 },
            axisLabel: { color: '#999', fontSize: 11 },
            splitLine: { show: false }
          }
        ],
        series: [
          {
            name: '监测次数', type: 'line',
            data: this.tableData.map(d => Number(d.totalCount) || 0),
            smooth: true, symbol: 'circle', symbolSize: 6,
            lineStyle: { width: 2.5, color: '#409EFF' },
            itemStyle: { color: '#409EFF', borderWidth: 2 },
            emphasis: { itemStyle: { borderColor: '#fff', borderWidth: 2 } },
            areaStyle: { color: new echarts.graphic.LinearGradient(0,0,0,1,[
              { offset: 0, color: 'rgba(64,158,255,0.3)' },
              { offset: 1, color: 'rgba(64,158,255,0.02)' }
            ])}
          },
          {
            name: '平均AQI', type: 'line', yAxisIndex: 1,
            data: this.tableData.map(d => Number(d.avgAqi) || 0),
            smooth: true, symbol: 'diamond', symbolSize: 6,
            lineStyle: { width: 2.5, color: '#FF7E00' },
            itemStyle: { color: '#FF7E00', borderWidth: 2 },
            emphasis: { itemStyle: { borderColor: '#fff', borderWidth: 2 } },
            areaStyle: { color: new echarts.graphic.LinearGradient(0,0,0,1,[
              { offset: 0, color: 'rgba(255,126,0,0.2)' },
              { offset: 1, color: 'rgba(255,126,0,0.02)' }
            ])}
          }
        ]
      })
    }
  }
}
</script>

<style scoped>
.statis-page { padding: 20px; }
.page-header { margin-bottom: 20px; }
.page-header h2 { font-size: 20px; color: #1b5e20; margin: 0 0 6px; font-weight: 700; line-height: 1.4; }
.page-sub { color: #999; font-size: 13px; line-height: 1.5; }
.trend-summary { display: flex; gap: 16px; margin-bottom: 16px; }
.trend-card {
  flex: 1; background: #fff; border-radius: 10px; padding: 18px 22px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05); text-align: center;
  transition: all 0.3s ease;
}
.trend-card:hover { box-shadow: 0 4px 16px rgba(0,0,0,0.08); transform: translateY(-1px); }
.tr-label { font-size: 12px; color: #999; display: block; margin-bottom: 8px; }
.tr-value { font-size: 24px; font-weight: 700; line-height: 1.2; }
.chart-box-tall { width: 100%; height: 400px; }
</style>
