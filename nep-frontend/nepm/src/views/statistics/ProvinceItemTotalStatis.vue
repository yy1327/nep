<template>
  <div class="statis-page">
    <div class="page-header">
      <h2>📊 各省空气质量统计</h2>
      <span class="page-sub">按省份维度展示监测总数、优良数及污染数</span>
    </div>

    <!-- 统计概览卡片 -->
    <div class="summary-row">
      <div class="summary-card blue">
        <span class="sc-icon">📡</span>
        <div class="sc-info">
          <div class="sc-val">{{ totalSummary.total }}</div>
          <div class="sc-label">监测总数</div>
        </div>
      </div>
      <div class="summary-card green">
        <span class="sc-icon">✅</span>
        <div class="sc-info">
          <div class="sc-val">{{ totalSummary.good }}</div>
          <div class="sc-label">优良记录</div>
        </div>
      </div>
      <div class="summary-card red">
        <span class="sc-icon">⚠️</span>
        <div class="sc-info">
          <div class="sc-val">{{ totalSummary.polluted }}</div>
          <div class="sc-label">污染记录</div>
        </div>
      </div>
      <div class="summary-card orange">
        <span class="sc-icon">🏘️</span>
        <div class="sc-info">
          <div class="sc-val">{{ tableData.length }}</div>
          <div class="sc-label">覆盖省份</div>
        </div>
      </div>
    </div>

    <!-- 图表 -->
    <el-card class="chart-card">
      <template #header>
        <div class="chart-header">
          <span>📈 各省份数据对比</span>
          <div class="custom-legend">
            <span
              v-for="item in legendItems"
              :key="item.name"
              class="legend-btn"
              :class="{ active: item.visible }"
              :style="item.visible ? { color: item.color, borderColor: item.color } : {}"
              @click="toggleSeries(item.name)"
            >
              <span class="legend-dot" :style="{ background: item.visible ? item.color : '#ccc' }"></span>
              {{ item.name }}
            </span>
          </div>
        </div>
      </template>
      <div id="provinceBarChart" class="chart-box"></div>
    </el-card>

    <!-- 排名表格 -->
    <el-card style="margin-top:16px">
      <template #header><span>📋 数据明细</span></template>
      <el-table :data="sortedTableData" stripe :default-sort="{prop:'good_rate', order:'descending'}">
        <el-table-column type="index" label="排名" width="60" align="center">
          <template #default="scope">
            <el-tag v-if="scope.$index === 0" type="warning" size="small">🥇</el-tag>
            <el-tag v-else-if="scope.$index === 1" type="info" size="small">🥈</el-tag>
            <el-tag v-else-if="scope.$index === 2" type="danger" size="small">🥉</el-tag>
            <span v-else>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="province_name" label="省份" sortable></el-table-column>
        <el-table-column prop="total_count" label="监测总数" sortable></el-table-column>
        <el-table-column prop="good_count" label="优良数" sortable>
          <template #default="scope">
            <span style="color:#67c23a;font-weight:600">{{ scope.row.good_count }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="polluted_count" label="污染数" sortable>
          <template #default="scope">
            <span style="color:#f56c6c;font-weight:600">{{ scope.row.polluted_count }}</span>
          </template>
        </el-table-column>
        <el-table-column label="优良率" width="100" sortable sort-by="good_rate">
          <template #default="scope">
            <el-progress
              :percentage="getRate(scope.row.good_count, scope.row.total_count)"
              :color="getProgressColor(getRate(scope.row.good_count, scope.row.total_count))"
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
  name: 'ProvinceItemTotalStatis',
  data() {
    return {
      tableData: [],
      chart: null,
      legendItems: [
        { name: '优良数', color: '#67c23a', visible: true },
        { name: '污染数', color: '#f56c6c', visible: true },
        { name: '总数', color: '#409eff', visible: true }
      ],
      sortedData: []
    }
  },
  computed: {
    sortedTableData() {
      return [...this.tableData]
        .map(d => ({
          ...d,
          good_rate: d.total_count ? Math.round((d.good_count / d.total_count) * 100) : 0
        }))
        .sort((a, b) => b.good_rate - a.good_rate)
    },
    totalSummary() {
      let total = 0, good = 0, polluted = 0
      this.tableData.forEach(d => {
        total += Number(d.total_count) || 0
        good += Number(d.good_count) || 0
        polluted += Number(d.polluted_count) || 0
      })
      return { total, good, polluted }
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
    getRate(good, total) {
      if (!total || total === 0) return 0
      return Math.round((good / total) * 100)
    },
    getProgressColor(rate) {
      if (rate >= 80) return '#67c23a'
      if (rate >= 60) return '#e6a23c'
      return '#f56c6c'
    },
    async loadData() {
      const res = await api.get('/statistics/listProvinceItemTotalStatis')
      if (res.code === 200) {
        this.tableData = res.data || []
        this.sortedData = [...this.tableData]
          .filter(d => d.province_name)
          .sort((a, b) => b.total_count - a.total_count)
        this.renderChart()
      }
    },
    toggleSeries(name) {
      const item = this.legendItems.find(l => l.name === name)
      if (item) {
        item.visible = !item.visible
        this.updateChart()
      }
    },
    renderChart() {
      if (!this.chart) {
        this.chart = echarts.init(document.getElementById('provinceBarChart'))
      }
      this.updateChart()
    },
    updateChart() {
      const sorted = this.sortedData
      if (!sorted.length) {
        this.chart.setOption({
          title: { text: '暂无数据', left: 'center', top: 'center', textStyle: { color: '#999', fontSize: 14 } },
          tooltip: { trigger: 'axis' },
          xAxis: { type: 'value' }, yAxis: { type: 'category', data: [] },
          series: []
        })
        return
      }
      const seriesConfig = [
        {
          name: '优良数', type: 'bar',
          data: this.legendItems[0].visible ? sorted.map(d => Number(d.good_count) || 0).reverse() : [],
          barWidth: 12, barGap: '20%',
          itemStyle: { color: '#67c23a', borderRadius: [0, 4, 4, 0] }
        },
        {
          name: '污染数', type: 'bar',
          data: this.legendItems[1].visible ? sorted.map(d => Number(d.polluted_count) || 0).reverse() : [],
          barWidth: 12,
          itemStyle: { color: '#f56c6c', borderRadius: [0, 4, 4, 0] }
        },
        {
          name: '总数', type: 'bar',
          data: this.legendItems[2].visible ? sorted.map(d => Number(d.total_count) || 0).reverse() : [],
          barWidth: 12, barGap: '20%',
          itemStyle: { color: '#409eff', borderRadius: [0, 4, 4, 0] }
        }
      ]
      this.chart.setOption({
        title: { text: '' },
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: 80, right: 40, top: 20, bottom: 20 },
        xAxis: {
          type: 'value',
          axisLabel: { color: '#999' },
          splitLine: { lineStyle: { color: '#f0f0f0' } }
        },
        yAxis: {
          type: 'category',
          data: sorted.map(d => d.province_name).reverse(),
          axisLabel: { color: '#666', fontSize: 11 },
          axisLine: { show: false },
          axisTick: { show: false }
        },
        series: seriesConfig
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
.summary-row { display: flex; gap: 16px; margin-bottom: 16px; }
.summary-card {
  flex: 1; display: flex; align-items: center; gap: 14px;
  background: #fff; border-radius: 10px; padding: 20px 22px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05); border-left: 4px solid;
  transition: all 0.3s ease;
}
.summary-card:hover { box-shadow: 0 4px 16px rgba(0,0,0,0.08); transform: translateY(-1px); }
.summary-card.blue { border-color: #409eff; }
.summary-card.green { border-color: #67c23a; }
.summary-card.red { border-color: #f56c6c; }
.summary-card.orange { border-color: #e6a23c; }
.sc-icon { font-size: 28px; }
.sc-val { font-size: 26px; font-weight: 700; color: #333; line-height: 1.2; }
.sc-label { font-size: 12px; color: #999; margin-top: 4px; }
.chart-card { margin-bottom: 16px; border-radius: 12px; }
.chart-header { display: flex; justify-content: space-between; align-items: center; font-size: 15px; font-weight: 600; color: #333; }
.chart-box { width: 100%; height: 420px; }
.custom-legend { display: flex; gap: 8px; font-size: 13px; font-weight: 400; }
.legend-btn {
  display: inline-flex; align-items: center; gap: 5px;
  padding: 4px 12px; border-radius: 16px; cursor: pointer;
  border: 1px solid #ddd; color: #999; transition: all 0.2s;
  user-select: none;
}
.legend-btn:hover { border-color: #999; }
.legend-btn.active { background: rgba(0,0,0,0.03); }
.legend-dot { width: 8px; height: 8px; border-radius: 50%; flex-shrink: 0; }
</style>
