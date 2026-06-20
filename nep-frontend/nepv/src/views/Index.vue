<template>
  <div class="dashboard-container">
    <!-- 顶部标题 -->
    <div class="header">
      <div class="header-left">
        <span class="logo">🌍</span>
        <div>
          <h1>环境监测数据中心</h1>
          <p>东软空气质量环保公众监督平台</p>
        </div>
      </div>
      <div class="header-right">
        <span class="live-dot"></span>
        <span class="header-time">{{ currentTime }}</span>
      </div>
    </div>

    <!-- 主体 -->
    <div class="main-content">
      <!-- 左栏 -->
      <div class="left-col">
        <div class="panel">
          <div class="panel-title"><span>📊</span> 监测覆盖率</div>
          <div class="gauges">
            <div id="gaugeProvince" class="gauge-chart"></div>
            <div id="gaugeCity" class="gauge-chart"></div>
          </div>
        </div>
        <div class="panel">
          <div class="panel-title"><span>🥧</span> AQI等级分布</div>
          <div id="pieChart" class="chart-box"></div>
        </div>
        <div class="panel">
          <div class="panel-title"><span>📈</span> 月度趋势</div>
          <div id="trendChart" class="chart-box"></div>
        </div>
      </div>

      <!-- 中栏 -->
      <div class="center-col">
        <div class="stats-row">
          <div class="stat-card c1">
            <div class="sc-icon">📡</div>
            <div class="sc-info">
              <div class="sc-val">{{ stats.aqiCount }}</div>
              <div class="sc-lbl">AQI采集总数</div>
            </div>
          </div>
          <div class="stat-card c2">
            <div class="sc-icon">✅</div>
            <div class="sc-info">
              <div class="sc-val">{{ stats.goodCount }}</div>
              <div class="sc-lbl">优良记录数</div>
            </div>
          </div>
          <div class="stat-card c3">
            <div class="sc-icon">🏘️</div>
            <div class="sc-info">
              <div class="sc-val">{{ stats.provinceCoverage }}</div>
              <div class="sc-lbl">覆盖省份</div>
            </div>
          </div>
          <div class="stat-card c4">
            <div class="sc-icon">🏙️</div>
            <div class="sc-info">
              <div class="sc-val">{{ stats.cityCoverage }}</div>
              <div class="sc-lbl">覆盖城市</div>
            </div>
          </div>
        </div>
        <div class="panel map-panel flex-1">
          <div class="panel-title"><span>🗺️</span> 全国空气质量监测分布图</div>
          <div id="chinaMap" class="chart-map"></div>
        </div>
      </div>

      <!-- 右栏 -->
      <div class="right-col">
        <div class="panel">
          <div class="panel-title"><span>💨</span> 优良率仪表盘</div>
          <div id="gaugeGoodRate" class="gauge-big"></div>
        </div>
        <div class="panel">
          <div class="panel-title"><span>⚠️</span> 污染等级统计</div>
          <div id="levelChart" class="chart-box"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import api from "@/api"
import * as echarts from "echarts"

export default {
  name: "Index",
  data() {
    return {
      currentTime: "",
      stats: { aqiCount: 0, goodCount: 0, provinceCoverage: 0, cityCoverage: 0 },
      provinceData: [],
      aqiDistData: [],
      trendData: [],
      charts: {},
    }
  },
  mounted() {
    this.updateTime()
    setInterval(this.updateTime, 1000)
    this.loadData()
    window.addEventListener("resize", this.resizeAll)
  },
  beforeUnmount() {
    window.removeEventListener("resize", this.resizeAll)
    Object.values(this.charts).forEach(c => c && c.dispose())
  },
  methods: {
    updateTime() {
      const d = new Date()
      const wk = ["日","一","二","三","四","五","六"]
      this.currentTime = `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')} 星期${wk[d.getDay()]} ${String(d.getHours()).padStart(2,'0')}:${String(d.getMinutes()).padStart(2,'0')}:${String(d.getSeconds()).padStart(2,'0')}`
    },
    resizeAll() { Object.values(this.charts).forEach(c => c && c.resize()) },
    async loadData() {
      const [countR, goodR, prvR, cityR, provD, aqiD, trendD] = await Promise.all([
        api.get("/statistics/getAqiCount"), api.get("/statistics/getAqiGoodCount"),
        api.get("/statistics/getProvinceCoverage"), api.get("/statistics/getCityCoverage"),
        api.get("/statistics/listProvinceItemTotalStatis"), api.get("/statistics/listAqiDistributeTotalStatis"),
        api.get("/statistics/listAqiTrendTotalStatis"),
      ])
      if (countR.code===200) this.stats.aqiCount = countR.data
      if (goodR.code===200) this.stats.goodCount = goodR.data
      if (prvR.code===200) this.stats.provinceCoverage = prvR.data
      if (cityR.code===200) this.stats.cityCoverage = cityR.data
      if (provD.code===200) { this.provinceData = provD.data; this.renderGauges(); this.renderChinaMap() }
      if (aqiD.code===200) { this.aqiDistData = aqiD.data; this.renderPie(); this.renderLevel(); this.renderGoodRate() }
      if (trendD.code===200) { this.trendData = trendD.data; this.renderTrend() }
    },

    /* 仪表盘 */
    renderGauges() {
      const pRate = Math.min(Math.round(this.stats.provinceCoverage / (this.provinceData.length||1) * 100), 100)
      const cRate = Math.min(Math.round(this.stats.cityCoverage / ((this.provinceData.length||1)*3) * 100), 100)
      const gOpt = (n, v, c) => ({
        series: [{
          type: "gauge", radius: "88%", center: ["50%","58%"], startAngle: 210, endAngle: -30, min:0, max:100,
          axisLine: { lineStyle: { width: 14, color: [[0.3,"#FF6E76"],[0.6,"#FDDD60"],[0.8,"#58D9F9"],[1,"#00E676"]] } },
          pointer: { length:"60%", width:5, itemStyle:{ color:c } },
          axisTick: { length:8, lineStyle:{ color:"auto", width:2 } },
          splitLine: { length:14, lineStyle:{ color:"auto", width:3 } },
          axisLabel: { distance:14, color:"#9aa7c7", fontSize:9 },
          title: { offsetCenter:[0,"82%"], fontSize:11, color:"#9aa7c7" },
          detail: { fontSize:20, offsetCenter:[0,"50%"], formatter:"{value}%", color:c, fontWeight:"bold" },
          data: [{ value:v, name:n }]
        }]
      })
      const g1 = echarts.init(document.getElementById("gaugeProvince"))
      g1.setOption(gOpt("省覆盖率", pRate, "#58D9F9"))
      this.charts.g1 = g1
      const g2 = echarts.init(document.getElementById("gaugeCity"))
      g2.setOption(gOpt("市覆盖率", cRate, "#00E676"))
      this.charts.g2 = g2
    },

    /* 大仪表盘 */
    renderGoodRate() {
      const goodRate = this.stats.aqiCount ? Math.round(this.stats.goodCount / this.stats.aqiCount * 100) : 0
      const g3 = echarts.init(document.getElementById("gaugeGoodRate"))
      g3.setOption({
        series: [{
          type: "gauge", radius: "85%", center: ["50%","60%"], startAngle: 220, endAngle: -40, min:0, max:100,
          progress: { show:true, width:18, itemStyle:{ color: goodRate>=80?"#00E676":goodRate>=60?"#FF7E00":"#FF6E76" } },
          axisLine: { lineStyle:{ width:18, color:[[0.3,"#FF6E76"],[0.6,"#FF7E00"],[0.8,"#FDDD60"],[1,"#00E676"]] } },
          pointer: { show:false },
          axisTick: { show:false }, splitLine: { show:false }, axisLabel: { show:false },
          title: { offsetCenter:[0,"35%"], fontSize:15, color:"#9aa7c7" },
          detail: { fontSize:34, offsetCenter:[0,"70%"], formatter:"{value}%", color:"#fff", fontWeight:"bold" },
          data: [{ value:goodRate, name:"优良率" }]
        }]
      })
      this.charts.g3 = g3
    },

    /* 饼图 */
    renderPie() {
      const colors = { "优":"#00E400","良":"#FFFF00","轻度污染":"#FF7E00","中度污染":"#FF0000","重度污染":"#99004C","严重污染":"#7E0023" }
      const pieData = this.aqiDistData.filter(d=>d.aqiLevel).map(d=>({ value:d.count, name:d.aqiLevel, itemStyle:{ color:colors[d.aqiLevel]||"#409EFF" } }))
      const c = echarts.init(document.getElementById("pieChart"))
      c.setOption({
        tooltip:{ trigger:"item", formatter:"{b}: {c} ({d}%)" },
        legend:{ orient:"vertical", right:8, top:"center", textStyle:{ color:"#9aa7c7", fontSize:10 } },
        series:[{ type:"pie", radius:["42%","72%"], center:["38%","52%"], data:pieData, label:{ show:false },
          emphasis:{ label:{ show:true, fontSize:14 } }, itemStyle:{ borderRadius:3, borderColor:"#0a1230", borderWidth:2 } }]
      })
      this.charts.pie = c
    },

    /* 趋势折线 */
    renderTrend() {
      const c = echarts.init(document.getElementById("trendChart"))
      c.setOption({
        tooltip:{ trigger:"axis" },
        legend:{ data:["次数","平均AQI"], bottom:0, textStyle:{ color:"#9aa7c7", fontSize:10 } },
        grid:{ left:45, right:45, top:20, bottom:35 },
        xAxis:{ type:"category", data:this.trendData.map(d=>d.month), axisLabel:{ color:"#9aa7c7", fontSize:9, rotate:30 }, axisLine:{ lineStyle:{ color:"#1e3a5f" } } },
        yAxis:[
          { type:"value", axisLabel:{ color:"#9aa7c7",fontSize:9 }, splitLine:{ lineStyle:{ color:"rgba(255,255,255,0.05)" } } },
          { type:"value", axisLabel:{ color:"#9aa7c7",fontSize:9 }, splitLine:{ show:false } }
        ],
        series:[
          { name:"次数", type:"line", data:this.trendData.map(d=>d.totalCount), smooth:true, symbol:"circle", symbolSize:5,
            lineStyle:{ width:2, color:"#409EFF" }, itemStyle:{ color:"#409EFF" },
            areaStyle:{ color: new echarts.graphic.LinearGradient(0,0,0,1,[{offset:0,color:"rgba(64,158,255,0.25)"},{offset:1,color:"rgba(64,158,255,0.02)"}]) } },
          { name:"平均AQI", type:"line", yAxisIndex:1, data:this.trendData.map(d=>Number(d.avgAqi||0).toFixed(1)), smooth:true,
            symbol:"diamond", symbolSize:5, lineStyle:{ width:2, color:"#FF7E00" }, itemStyle:{ color:"#FF7E00" },
            areaStyle:{ color: new echarts.graphic.LinearGradient(0,0,0,1,[{offset:0,color:"rgba(255,126,0,0.2)"},{offset:1,color:"rgba(255,126,0,0.02)"}]) } }
        ]
      })
      this.charts.trend = c
    },



    /* 中国地图 - 按AQI等级着色 */
    async renderChinaMap() {
      try {
        const resp = await fetch("https://geo.datav.aliyun.com/areas_v3/bound/100000_full.json")
        const geoJson = await resp.json()
        echarts.registerMap("china", geoJson)

        const nameMap = {
          "北京市":"北京","天津市":"天津","上海市":"上海","重庆市":"重庆",
          "河北省":"河北","山西省":"山西","辽宁省":"辽宁","吉林省":"吉林",
          "黑龙江省":"黑龙江","江苏省":"江苏","浙江省":"浙江","安徽省":"安徽",
          "福建省":"福建","江西省":"江西","山东省":"山东","河南省":"河南",
          "湖北省":"湖北","湖南省":"湖南","广东省":"广东","海南省":"海南",
          "四川省":"四川","贵州省":"贵州","云南省":"云南","陕西省":"陕西",
          "甘肃省":"甘肃","青海省":"青海","台湾省":"台湾",
          "内蒙古自治区":"内蒙古","广西壮族自治区":"广西","西藏自治区":"西藏",
          "宁夏回族自治区":"宁夏","新疆维吾尔自治区":"新疆","香港":"香港","澳门":"澳门"
        }

        // 等级定义: key=等级值(用于排序), val={label, color, maxRate}
        const levels = [
          { val:1, label:"优", color:"#00E400", maxRate:5 },
          { val:2, label:"良", color:"#FFFF00", maxRate:15 },
          { val:3, label:"轻度污染", color:"#FF7E00", maxRate:30 },
          { val:4, label:"中度污染", color:"#FF0000", maxRate:50 },
          { val:5, label:"重度污染", color:"#99004C", maxRate:70 },
          { val:6, label:"严重污染", color:"#7E0023", maxRate:100 }
        ]

        // 计算每个省份的污染率和AQI等级（key用完整省名匹配地图）
        const provinceInfo = {}
        this.provinceData.forEach(d => {
          const dbName = d.province_name  // 数据库名如"河北"、"北京"
          const short = nameMap[dbName] || dbName
          // 反向查找完整省名用于地图匹配
          let fullName = dbName
          for (const [k, v] of Object.entries(nameMap)) {
            if (v === short || k === dbName) { fullName = k; break }
          }
          const total = Number(d.total_count) || 1
          const pollutedRate = Math.round(Number(d.polluted_count) / total * 100)
          let level = levels[levels.length - 1]
          for (const l of levels) {
            if (pollutedRate <= l.maxRate) { level = l; break }
          }
          provinceInfo[fullName] = {
            value: level.val,
            levelLabel: level.label,
            levelColor: level.color,
            total: Number(d.total_count),
            good: Number(d.good_count),
            polluted: Number(d.polluted_count),
            pollutedRate
          }
        })

        // 用完整省名构建地图数据（与GeoJSON的name字段匹配）
        const mapData = Object.keys(nameMap).map(fullName => {
          if (provinceInfo[fullName]) {
            return { name: fullName, ...provinceInfo[fullName] }
          }
          return { name: fullName, value: 0, levelLabel:"无数据", levelColor:"#2a2a3a", total:0, good:0, polluted:0, pollutedRate:0 }
        })

        const c = echarts.init(document.getElementById("chinaMap"))
        c.setOption({
          tooltip: {
            trigger:"item",
            formatter: p => {
              if (!p.data || !p.data.total) return `<b>${p.name}</b><br/>暂无数据`
              const d = p.data
              const short = nameMap[p.name] || p.name
              return `<b>${short}</b><br/>
                监测总数：${d.total}<br/>
                优良数：<span style="color:#00E676">${d.good}</span>&nbsp;&nbsp;污染数：<span style="color:#FF5252">${d.polluted}</span><br/>
                污染率：${d.pollutedRate}%<br/>
                AQI等级：<span style="color:${d.levelColor};font-size:14px;font-weight:bold">${d.levelLabel}</span>`
            }
          },
          visualMap: {
            type:"piecewise",
            seriesIndex:0,
            left:10, bottom:20,
            pieces: [
              ...levels.map(l => ({ min: l.val, max: l.val, label: l.label, color: l.color })),
              { min:0, max:0, label:"无数据", color:"#2a2a3a" }
            ],
            textStyle:{ color:"#9aa7c7",fontSize:10 },
            orient:"vertical", itemWidth:14, itemHeight:14
          },
          series:[{
            type:"map", map:"china",
            roam:true,
            select:{ itemStyle:{ areaColor:"#4a90d9" }, label:{ show:true, color:"#fff",fontSize:13,fontWeight:"bold" } },
            label:{ show:true, fontSize:9, color:"#aaa" },
            itemStyle:{ borderColor:"#0a1230", borderWidth:1.5 },
            emphasis:{ label:{ show:true, color:"#fff",fontSize:13,fontWeight:"bold" }, itemStyle:{ areaColor:"#4a90d9" } },
            data: mapData.map(d => ({
              name: d.name,
              value: d.value,
              itemStyle: { areaColor: d.levelColor },
              levelLabel: d.levelLabel,
              levelColor: d.levelColor,
              total: d.total,
              good: d.good,
              polluted: d.polluted,
              pollutedRate: d.pollutedRate
            }))
          }]
        })
        this.charts.chinaMap = c
      } catch (e) {
        console.error("地图加载失败:", e)
      }
    },

    /* 污染等级柱状 */
    renderLevel() {
      const colors = { "优":"#00E400","良":"#FFFF00","轻度污染":"#FF7E00","中度污染":"#FF0000","重度污染":"#99004C","严重污染":"#7E0023" }
      const data = this.aqiDistData.filter(d=>d.aqiLevel).sort((a,b)=>b.count-a.count)
      const c = echarts.init(document.getElementById("levelChart"))
      c.setOption({
        tooltip:{ trigger:"axis", axisPointer:{ type:"shadow" } },
        grid:{ left:70, right:20, top:10, bottom:10 },
        xAxis:{ type:"value", axisLabel:{ color:"#9aa7c7",fontSize:9 }, splitLine:{ lineStyle:{ color:"rgba(255,255,255,0.05)" } } },
        yAxis:{ type:"category", data:data.map(d=>d.aqiLevel).reverse(), axisLabel:{ color:"#9aa7c7",fontSize:10 }, axisLine:{ show:false }, axisTick:{ show:false } },
        series:[{
          type:"bar", barWidth:16,
          data: data.map(d=>({ value:d.count, itemStyle:{ color:colors[d.aqiLevel]||"#409EFF", borderRadius:[0,4,4,0] } })).reverse(),
          label:{ show:true, position:"right", color:"#9aa7c7", fontSize:10 }
        }]
      })
      this.charts.level = c
    },
  },
}
</script>

<style scoped>
.dashboard-container { min-height:100vh; background:#070f26; color:#fff; font-family:"Microsoft YaHei",sans-serif; display:flex; flex-direction:column; }

/* 顶部 */
.header {
  display:flex; align-items:center; justify-content:space-between;
  padding:16px 34px; background:rgba(10,18,48,0.95);
  border-bottom:1px solid rgba(64,158,255,0.12);
  box-shadow: 0 2px 16px rgba(0,0,0,0.3);
  flex-shrink: 0;
}
.header-left { display:flex; align-items:center; gap:16px; }
.logo { font-size:30px; filter: drop-shadow(0 2px 8px rgba(64,158,255,0.3)); flex-shrink: 0; }
.header-left h1 { font-size:20px; font-weight:700; color:#dbeafe; margin:0; letter-spacing:2px; line-height: 1.4; }
.header-left p { font-size:12px; color:#4a6580; margin:6px 0 0; letter-spacing:1px; line-height: 1.4; }
.header-right { display:flex; align-items:center; gap:12px; flex-shrink: 0; }
.live-dot { width:8px; height:8px; background:#00E676; border-radius:50%; animation:livePulse 2s infinite; box-shadow: 0 0 8px rgba(0,230,118,0.5); flex-shrink: 0; }
@keyframes livePulse { 0%,100%{opacity:1} 50%{opacity:.3} }
.header-time { color:#58D9F9; font-size:14px; letter-spacing:1px; font-variant-numeric: tabular-nums; line-height: 1.4; }

/* 主体三栏 */
.main-content { display:flex; padding:14px 18px 18px; gap:16px; flex:1; min-height:0; }
.left-col { flex:0 0 28%; display:flex; flex-direction:column; gap:14px; min-height:0; overflow-y:auto; }
.center-col { flex:1; display:flex; flex-direction:column; gap:14px; min-height:0; overflow-y:auto; }
.right-col { flex:0 0 24%; display:flex; flex-direction:column; gap:14px; min-height:0; overflow-y:auto; }

/* 面板 */
.panel {
  background:rgba(12,22,50,0.85); border:1px solid rgba(64,158,255,0.1);
  border-radius:10px; padding:16px 18px; position:relative;
  box-shadow: 0 2px 12px rgba(0,0,0,0.2);
}
.panel::before { content:""; position:absolute; top:0;left:0;right:0; height:2px;
  background:linear-gradient(90deg, transparent, rgba(64,158,255,0.4), transparent); }
.panel-title { font-size:13px; font-weight:600; color:#bcd4f5; margin-bottom:10px; padding-bottom:10px;
  border-bottom:1px solid rgba(64,158,255,0.08); display:flex; align-items:center; gap:8px; line-height: 1.4; }
.panel-title span { color:#409EFF; flex-shrink: 0; }
.flex-1 { flex:1; display:flex; flex-direction:column; min-height:0; }

/* 统计卡片 */
.stats-row { display:flex; gap:14px; flex-shrink: 0; }
.stat-card {
  flex:1; display:flex; align-items:center; gap:12px;
  background:rgba(12,22,50,0.85); border:1px solid rgba(64,158,255,0.1);
  border-radius:10px; padding:18px 20px; position:relative; overflow:hidden;
  transition: all 0.3s ease;
  box-shadow: 0 2px 12px rgba(0,0,0,0.2);
}
.stat-card:hover { border-color: rgba(64,158,255,0.25); transform: translateY(-1px); }
.stat-card::before { content:""; position:absolute; top:0; left:0;right:0; height:2px; }
.c1::before { background:linear-gradient(90deg, transparent, #409EFF, transparent); }
.c2::before { background:linear-gradient(90deg, transparent, #67C23A, transparent); }
.c3::before { background:linear-gradient(90deg, transparent, #E6A23C, transparent); }
.c4::before { background:linear-gradient(90deg, transparent, #6f42c1, transparent); }
.sc-icon { font-size:26px; flex-shrink: 0; }
.sc-val { font-size: 24px; font-weight: 700; color:#fff; line-height: 1.2; }
.c1 .sc-val { color:#409EFF; } .c2 .sc-val { color:#67C23A; } .c3 .sc-val { color:#E6A23C; } .c4 .sc-val { color:#6f42c1; }
.sc-lbl { font-size: 11px; color:#9aa7c7; margin-top: 4px; line-height: 1.3; }

/* 图表容器 */
.chart-box { width:100%; height:200px; }
.chart-map { width:100%; flex:1; min-height:300px; }
.gauge-chart { width:100%; height:150px; }
.gauge-big { width:100%; height:260px; }
.gauges { display:flex; }
</style>
