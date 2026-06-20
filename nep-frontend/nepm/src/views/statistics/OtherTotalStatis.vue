<template>
  <div class="statis-page">
    <div class="page-header">
      <h2>📋 综合数据概览</h2>
      <span class="page-sub">系统全局统计指标一览</span>
    </div>

    <!-- 核心指标卡片 -->
    <div class="kpi-row">
      <div class="kpi-card" v-for="kpi in kpiCards" :key="kpi.label">
        <div class="kpi-ring">
          <svg viewBox="0 0 100 100" class="kpi-svg">
            <circle cx="50" cy="50" r="42" fill="none" stroke="#f0f0f0" stroke-width="8"/>
            <circle cx="50" cy="50" r="42" fill="none"
              :stroke="kpi.color" stroke-width="8" stroke-linecap="round"
              :stroke-dasharray="kpi.percent * 2.64 + ' 264'"
              transform="rotate(-90 50 50)"
              class="kpi-arc"
            />
          </svg>
          <div class="kpi-center">
            <span class="kpi-val" :style="{ color: kpi.color }">{{ kpi.value }}</span>
          </div>
        </div>
        <div class="kpi-label">{{ kpi.label }}</div>
        <div class="kpi-sub">{{ kpi.sub }}</div>
      </div>
    </div>

    <!-- 数据卡片行 -->
    <div class="data-cards">
      <el-card class="data-card">
        <template #header><span>📡 AQI采集总数</span></template>
        <div class="big-num blue">{{ stats.aqiCount }}</div>
        <div class="big-desc">系统累计采集的空气质量数据条目</div>
      </el-card>
      <el-card class="data-card">
        <template #header><span>✅ 优良记录数</span></template>
        <div class="big-num green">{{ stats.goodCount }}</div>
        <el-progress
          :percentage="getGoodRate()"
          :color="getRateColor()"
          :stroke-width="14"
        />
        <div class="big-desc">占总监测数据的比例</div>
      </el-card>
      <el-card class="data-card">
        <template #header><span>🏘️ 覆盖省份</span></template>
        <div class="big-num orange">{{ stats.provinceCoverage }}</div>
        <div class="big-desc">已接入监测数据的省份数量</div>
      </el-card>
      <el-card class="data-card">
        <template #header><span>🏙️ 覆盖城市</span></template>
        <div class="big-num purple">{{ stats.cityCoverage }}</div>
        <div class="big-desc">已接入监测数据的城市数量</div>
      </el-card>
    </div>

    <!-- 仪表盘图 -->
    <div class="gauge-row">
      <el-card class="gauge-card">
        <template #header><span>🎯 优良率仪表盘</span></template>
        <div id="gaugeGoodRate" class="gauge-box"></div>
      </el-card>
      <el-card class="gauge-card">
        <template #header><span>📊 省份覆盖率</span></template>
        <div id="gaugeProvince" class="gauge-box"></div>
      </el-card>
    </div>
  </div>
</template>

<script>
import api from '@/api'
import * as echarts from 'echarts'

export default {
  name: 'OtherTotalStatis',
  data() {
    return {
      stats: { aqiCount: 0, goodCount: 0, provinceCoverage: 0, cityCoverage: 0 },
      charts: {}
    }
  },
  computed: {
    kpiCards() {
      const goodRate = this.getGoodRate()
      return [
        { label: 'AQI总数', value: this.stats.aqiCount, sub: '累计采集', color: '#409eff', percent: Math.min(this.stats.aqiCount / 100, 100) },
        { label: '优良率', value: goodRate + '%', sub: '质量良好', color: '#67c23a', percent: goodRate },
        { label: '省覆盖', value: this.stats.provinceCoverage, sub: '个省份', color: '#e6a23c', percent: Math.min(this.stats.provinceCoverage / 34 * 100, 100) },
        { label: '市覆盖', value: this.stats.cityCoverage, sub: '个城市', color: '#6f42c1', percent: Math.min(this.stats.cityCoverage / 100, 100) }
      ]
    }
  },
  created() { this.loadData() },
  beforeUnmount() { Object.values(this.charts).forEach(c => c && c.dispose()) },
  methods: {
    getGoodRate() {
      if (!this.stats.aqiCount) return 0
      return Math.round((this.stats.goodCount / this.stats.aqiCount) * 100)
    },
    getRateColor() {
      const rate = this.getGoodRate()
      if (rate >= 80) return '#67c23a'
      if (rate >= 60) return '#e6a23c'
      return '#f56c6c'
    },
    async loadData() {
      const [countRes, goodRes, provinceRes, cityRes] = await Promise.all([
        api.get('/statistics/getAqiCount'),
        api.get('/statistics/getAqiGoodCount'),
        api.get('/statistics/getProvinceCoverage'),
        api.get('/statistics/getCityCoverage')
      ])
      if (countRes.code === 200) this.stats.aqiCount = countRes.data
      if (goodRes.code === 200) this.stats.goodCount = goodRes.data
      if (provinceRes.code === 200) this.stats.provinceCoverage = provinceRes.data
      if (cityRes.code === 200) this.stats.cityCoverage = cityRes.data
      this.$nextTick(() => this.renderGauges())
    },
    renderGauges() {
      const goodRate = this.getGoodRate()
      const provinceRate = Math.min(Math.round(this.stats.provinceCoverage / 34 * 100), 100)

      const gaugeOpt = (name, value, color) => ({
        series: [{
          type: 'gauge',
          radius: '85%', center: ['50%', '58%'],
          startAngle: 210, endAngle: -30,
          min: 0, max: 100,
          axisLine: { lineStyle: { width: 16, color: [[0.3, '#f56c6c'],[0.6, '#e6a23c'],[0.8, '#409eff'],[1, '#67c23a']] } },
          pointer: { length: '60%', width: 6, itemStyle: { color } },
          axisTick: { length: 10, lineStyle: { color: 'auto', width: 2 } },
          splitLine: { length: 16, lineStyle: { color: 'auto', width: 3 } },
          axisLabel: { distance: 16, color: '#999', fontSize: 10 },
          title: { offsetCenter: [0, '80%'], fontSize: 13, color: '#666' },
          detail: { fontSize: 26, offsetCenter: [0, '52%'], formatter: '{value}%', color, fontWeight: 'bold' },
          data: [{ value, name }]
        }]
      })

      let g1 = echarts.init(document.getElementById('gaugeGoodRate'))
      g1.setOption(gaugeOpt('优良率', goodRate, '#67c23a'))
      this.charts.g1 = g1

      let g2 = echarts.init(document.getElementById('gaugeProvince'))
      g2.setOption(gaugeOpt('省份覆盖率', provinceRate, '#e6a23c'))
      this.charts.g2 = g2
    }
  }
}
</script>

<style scoped>
.statis-page { padding: 20px; }
.page-header { margin-bottom: 20px; }
.page-header h2 { font-size: 20px; color: #1b5e20; margin: 0 0 6px; font-weight: 700; line-height: 1.4; }
.page-sub { color: #999; font-size: 13px; line-height: 1.5; }

/* KPI 圆环卡片 */
.kpi-row { display: flex; gap: 16px; margin-bottom: 16px; }
.kpi-card {
  flex: 1; background: #fff; border-radius: 10px; padding: 20px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05); text-align: center;
}
.kpi-ring { width: 100px; height: 100px; margin: 0 auto 10px; position: relative; }
.kpi-svg { width: 100%; height: 100%; }
.kpi-arc { animation: arcFill 1.5s ease-out forwards; }
@keyframes arcFill {
  from { stroke-dasharray: 0 264; }
}
.kpi-center {
  position: absolute; inset: 0;
  display: flex; align-items: center; justify-content: center;
}
.kpi-val { font-size: 22px; font-weight: 700; line-height: 1.2; }
.kpi-label { font-size: 14px; color: #333; font-weight: 600; margin-top: 6px; }
.kpi-sub { font-size: 12px; color: #999; margin-top: 4px; }

/* 数据卡片 */
.data-cards { display: flex; gap: 16px; margin-bottom: 16px; flex-wrap: wrap; }
.data-card { flex: 1; min-width: 200px; }
.big-num { font-size: 42px; font-weight: 700; text-align: center; margin: 12px 0; line-height: 1.2; }
.big-num.blue { color: #409eff; }
.big-num.green { color: #67c23a; }
.big-num.orange { color: #e6a23c; }
.big-num.purple { color: #6f42c1; }
.big-desc { text-align: center; color: #999; font-size: 12px; margin-top: 8px; line-height: 1.5; }

/* 仪表盘 */
.gauge-row { display: flex; gap: 16px; margin-top: 16px; }
.gauge-card { flex: 1; }
.gauge-box { width: 100%; height: 280px; }
</style>
