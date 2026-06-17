<template>
  <div class="dashboard-container">
    <div class="header">
      <h1>东软环保公众监督系统 - 数据可视化大屏</h1>
      <div class="time">{{ currentTime }}</div>
    </div>
    <div class="content">
      <div class="left-panel">
        <div class="card">
          <h3>按省统计</h3>
          <div id="provinceChart" style="height: 300px"></div>
        </div>
      </div>
      <div class="center-panel">
        <div class="card">
          <h3>统计概览</h3>
          <div class="stats-grid">
            <div class="stat-item">
              <div class="stat-value">{{ stats.aqiCount }}</div>
              <div class="stat-label">AQI总数</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ stats.goodCount }}</div>
              <div class="stat-label">良好天数</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ stats.provinceCoverage }}</div>
              <div class="stat-label">省覆盖</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ stats.cityCoverage }}</div>
              <div class="stat-label">市覆盖</div>
            </div>
          </div>
        </div>
      </div>
      <div class="right-panel">
        <div class="card">
          <h3>等级分布</h3>
          <div id="aqiChart" style="height: 300px"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import api from '@/api'
import * as echarts from 'echarts'

export default {
  name: 'Index',
  data() {
    return {
      currentTime: '',
      stats: {
        aqiCount: 0,
        goodCount: 0,
        provinceCoverage: 0,
        cityCoverage: 0
      }
    }
  },
  mounted() {
    this.updateTime()
    setInterval(this.updateTime, 1000)
    this.loadData()
  },
  methods: {
    updateTime() {
      const now = new Date()
      this.currentTime = now.toLocaleString('zh-CN')
    },
    async loadData() {
      const [countRes, goodRes, provinceRes, cityRes, provinceStatis, aqiStatis] = await Promise.all([
        api.get('/statistics/getAqiCount'),
        api.get('/statistics/getAqiGoodCount'),
        api.get('/statistics/getProvinceCoverage'),
        api.get('/statistics/getCityCoverage'),
        api.get('/statistics/listProvinceItemTotalStatis'),
        api.get('/statistics/listAqiDistributeTotalStatis')
      ])

      if (countRes.code === 200) this.stats.aqiCount = countRes.data
      if (goodRes.code === 200) this.stats.goodCount = goodRes.data
      if (provinceRes.code === 200) this.stats.provinceCoverage = provinceRes.data
      if (cityRes.code === 200) this.stats.cityCoverage = cityRes.data

      if (provinceStatis.code === 200) {
        this.renderProvinceChart(provinceStatis.data)
      }
      if (aqiStatis.code === 200) {
        this.renderAqiChart(aqiStatis.data)
      }
    },
    renderProvinceChart(data) {
      const chart = echarts.init(document.getElementById('provinceChart'))
      const option = {
        backgroundColor: 'transparent',
        tooltip: { trigger: 'axis' },
        xAxis: {
          type: 'category',
          data: data.map(d => d.province_name),
          axisLabel: { color: '#fff', rotate: 45 }
        },
        yAxis: {
          type: 'value',
          axisLabel: { color: '#fff' }
        },
        series: [
          {
            name: '总数',
            type: 'bar',
            data: data.map(d => d.total_count),
            itemStyle: { color: '#409EFF' }
          }
        ]
      }
      chart.setOption(option)
    },
    renderAqiChart(data) {
      const chart = echarts.init(document.getElementById('aqiChart'))
      const colors = ['#00E400', '#FFFF00', '#FF7E00', '#FF0000', '#99004C', '#7E0023']
      const option = {
        backgroundColor: 'transparent',
        tooltip: { trigger: 'item' },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'center',
          textStyle: { color: '#fff' }
        },
        series: [
          {
            name: 'AQI分布',
            type: 'pie',
            radius: ['40%', '70%'],
            data: data.map((d, i) => ({
              value: d.count,
              name: d.aqiLevel,
              itemStyle: { color: colors[i % colors.length] }
            }))
          }
        ]
      }
      chart.setOption(option)
    }
  }
}
</script>

<style scoped>
.dashboard-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #0a0a2e 0%, #1a1a4e 100%);
  color: white;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 40px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}
.header h1 {
  font-size: 24px;
  background: linear-gradient(90deg, #409EFF, #67C23A);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}
.time {
  color: #409EFF;
  font-size: 16px;
}
.content {
  display: flex;
  padding: 30px;
  gap: 30px;
  height: calc(100vh - 80px);
}
.left-panel, .right-panel {
  flex: 1;
}
.center-panel {
  flex: 1.5;
}
.card {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 10px;
  padding: 20px;
  height: 100%;
}
.card h3 {
  margin-bottom: 20px;
  color: #409EFF;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  padding-bottom: 10px;
}
.stats-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 30px;
  padding: 40px 20px;
}
.stat-item {
  text-align: center;
}
.stat-value {
  font-size: 48px;
  font-weight: bold;
  background: linear-gradient(90deg, #409EFF, #67C23A);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}
.stat-label {
  margin-top: 10px;
  color: #aaa;
}
</style>
