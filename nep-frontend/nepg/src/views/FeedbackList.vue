<template>
  <div class="task-page">
    <h2>📋 待检测任务</h2>
    <div v-if="feedbackList.length === 0" class="empty">
      <span class="empty-icon">🎉</span>
      <p>暂无待检测任务</p>
    </div>
    <div v-else class="list">
      <div v-for="item in feedbackList" :key="item.af_id" class="task-card" @click="viewInfo(item.af_id)">
        <div class="task-top">
          <span class="task-code">📋 {{ item.af_code }}</span>
          <span :class="['task-status', 'status-'+item.af_state]">{{ statusMap[item.af_state] || item.af_state }}</span>
        </div>
        <div class="task-body">
          <div class="task-row"><span class="task-label">类型：</span>{{ typeMap[item.af_type] || item.af_type }}</div>
          <div class="task-row"><span class="task-label">地址：</span>{{ item.af_address }}</div>
          <div class="task-row"><span class="task-label">描述：</span>{{ item.af_desc }}</div>
        </div>
        <div class="task-arrow">→</div>
      </div>
    </div>
  </div>
</template>

<script>
import api from "@/api"

export default {
  name: "FeedbackList",
  data() {
    return {
      feedbackList: [],
      statusMap: { "01": "待指派", "02": "待检测", "03": "已完成" },
      typeMap: { "01": "空气有异味", "02": "雾霾严重", "03": "工业排放", "04": "其他" }
    }
  },
  created() { this.loadData() },
  activated() { this.loadData() },
  methods: {
    async loadData() {
      const gm = JSON.parse(sessionStorage.getItem("gridMember"))
      if (!gm) return
      const res = await api.post("/aqiFeedback/listAqiFeedbackByGmId", { gmId: gm.gmId })
      if (res.code === 200) this.feedbackList = (res.data || []).filter(item => item.af_state !== '03')
    },
    viewInfo(id) { this.$router.push(`/index/detect/${id}`) },
  },
}
</script>

<style scoped>
.task-page { max-width: 800px; }
h2 { font-size: 18px; color: #166953; margin-bottom: 16px; }
.empty { text-align: center; padding: 60px; color: #999; background: #fff; border-radius: 12px; }
.empty-icon { font-size: 48px; display: block; margin-bottom: 10px; }
.list { display: flex; flex-direction: column; gap: 12px; }
.task-card {
  background: #fff; border-radius: 12px; padding: 18px 20px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.04);
  cursor: pointer; transition: all 0.2s;
  border-left: 4px solid #11998e; position: relative;
}
.task-card:hover { transform: translateY(-2px); box-shadow: 0 4px 16px rgba(0,0,0,0.08); }
.task-top { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; }
.task-code { font-weight: 600; color: #166953; font-size: 15px; }
.task-status { padding: 2px 12px; border-radius: 20px; font-size: 12px; font-weight: 600; }
.status-01 { background: #fff3e0; color: #f57c00; }
.status-02 { background: #e3f2fd; color: #1976d2; }
.status-03 { background: #e8f5e9; color: #388e3c; }
.task-body { margin-bottom: 4px; }
.task-row { margin: 5px 0; color: #555; font-size: 14px; }
.task-label { color: #11998e; font-weight: 600; }
.task-arrow {
  position: absolute; right: 20px; top: 50%; transform: translateY(-50%);
  color: #11998e; font-size: 20px; opacity: 0.3; transition: opacity 0.2s;
}
.task-card:hover .task-arrow { opacity: 1; }
</style>
