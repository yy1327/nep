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
.task-page { max-width: 800px; animation: fadeInUp 0.4s ease-out; }
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(12px); }
  to { opacity: 1; transform: translateY(0); }
}
h2 { font-size: 20px; color: #166953; margin-bottom: 20px; font-weight: 700; letter-spacing: 0.5px; line-height: 1.4; }
.empty {
  text-align: center; padding: 64px 20px; color: #999;
  background: #fff; border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.04);
}
.empty-icon { font-size: 52px; display: block; margin-bottom: 12px; }
.list { display: flex; flex-direction: column; gap: 14px; }
.task-card {
  background: #fff; border-radius: 14px; padding: 20px 24px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.04);
  cursor: pointer; transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border-left: 4px solid #11998e; position: relative;
  animation: fadeInUp 0.4s ease-out both;
}
.task-card:nth-child(1) { animation-delay: 0.05s; }
.task-card:nth-child(2) { animation-delay: 0.1s; }
.task-card:nth-child(3) { animation-delay: 0.15s; }
.task-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 24px rgba(17,153,142,0.12);
}
.task-top { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.task-code { font-weight: 700; color: #166953; font-size: 15px; letter-spacing: 0.3px; }
.task-status {
  padding: 4px 14px; border-radius: 20px;
  font-size: 12px; font-weight: 600; letter-spacing: 0.5px;
}
.status-01 { background: #fff3e0; color: #e65100; }
.status-02 { background: #e3f2fd; color: #1565c0; }
.status-03 { background: #e8f5e9; color: #2e7d32; }
.task-body { margin-bottom: 4px; padding-top: 4px; }
.task-row { margin: 8px 0; color: #555; font-size: 14px; line-height: 1.7; }
.task-label { color: #11998e; font-weight: 600; margin-right: 4px; }
.task-arrow {
  position: absolute; right: 24px; top: 50%; transform: translateY(-50%);
  color: #11998e; font-size: 22px; opacity: 0;
  transition: all 0.3s ease;
  line-height: 1;
}
.task-card:hover .task-arrow { opacity: 1; transform: translateY(-50%) translateX(3px); }

@media (max-width: 768px) {
  .task-page { max-width: 100%; }
  .task-card { padding: 16px; }
  .task-arrow { display: none; }
}
</style>
