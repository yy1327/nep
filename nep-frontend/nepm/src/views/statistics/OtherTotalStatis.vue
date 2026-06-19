<template>
  <div class="other-statis">
    <el-card>
      <div slot="header">
        <span>其他统计</span>
      </div>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="AQI总数">{{ aqiCount }}</el-descriptions-item>
        <el-descriptions-item label="良好天数">{{ aqiGoodCount }}</el-descriptions-item>
        <el-descriptions-item label="省覆盖率">{{ provinceCoverage }}</el-descriptions-item>
        <el-descriptions-item label="市覆盖率">{{ cityCoverage }}</el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script>
import api from '@/api'

export default {
  name: 'OtherTotalStatis',
  data() {
    return {
      aqiCount: 0,
      aqiGoodCount: 0,
      provinceCoverage: 0,
      cityCoverage: 0
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    async loadData() {
      const [countRes, goodRes, provinceRes, cityRes] = await Promise.all([
        api.get('/statistics/getAqiCount'),
        api.get('/statistics/getAqiGoodCount'),
        api.get('/statistics/getProvinceCoverage'),
        api.get('/statistics/getCityCoverage')
      ])
      if (countRes.code === 200) this.aqiCount = countRes.data
      if (goodRes.code === 200) this.aqiGoodCount = goodRes.data
      if (provinceRes.code === 200) this.provinceCoverage = provinceRes.data
      if (cityRes.code === 200) this.cityCoverage = cityRes.data
    }
  }
}
</script>
