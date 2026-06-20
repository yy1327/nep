<template>
  <div class="feedback-list-container">
    <div class="header">
      <h2>历史反馈</h2>
      <button class="back-btn" @click="goBack">返回</button>
    </div>
    <div class="content">
      <div v-if="feedbackList.length === 0" class="empty">
        <p>暂无反馈记录</p>
      </div>
      <div v-else class="feedback-list">
        <div v-for="item in feedbackList" :key="item.af_id" class="feedback-item">
          <div class="item-header">
            <span class="code">{{ item.af_code }}</span>
            <span :class="['status', 'status-' + item.af_state]">{{ getStatusText(item.af_state) }}</span>
          </div>
          <div class="item-body">
            <p><strong>类型：</strong>{{ getTypeText(item.af_type) }}</p>
            <p><strong>等级：</strong>{{ item.af_level }}</p>
            <p><strong>地址：</strong>{{ item.af_address }}</p>
            <p><strong>描述：</strong>{{ item.af_desc }}</p>
          </div>
          <div class="item-footer">
            <span>{{ formatTime(item.create_time) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import api from '@/api'

export default {
  name: 'FeedbackList',
  data() {
    return {
      feedbackList: []
    }
  },
  created() {
    this.loadFeedbackList()
  },
  methods: {
    formatTime(val) {
      if (!val) return '-'
      const d = new Date(val)
      if (isNaN(d.getTime())) return val
      return `${d.getFullYear()}-${String(d.getMonth()+1).padStart(2,'0')}-${String(d.getDate()).padStart(2,'0')} ${String(d.getHours()).padStart(2,'0')}:${String(d.getMinutes()).padStart(2,'0')}`
    },
    async loadFeedbackList() {
      const supervisor = JSON.parse(sessionStorage.getItem('supervisor'))
      const res = await api.post('/aqiFeedback/listAqiFeedbackByTelId', {
        telId: supervisor.supervisorId
      })
      if (res.code === 200) {
        this.feedbackList = res.data
      }
    },
    getStatusText(state) {
      const map = {
        '01': '待指派',
        '02': '待确认',
        '03': '已完成'
      }
      return map[state] || state
    },
    getTypeText(type) {
      const map = {
        '01': '空气有异味',
        '02': '雾霾严重',
        '03': '工业排放',
        '04': '其他'
      }
      return map[type] || type
    },
    goBack() {
      this.$router.push('/selectAqi')
    }
  }
}
</script>

<style scoped>
.feedback-list-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #e8f5e9 0%, #f1f8e9 30%, #f0fdf4 100%);
}
.header {
  background: linear-gradient(135deg, #1b5e20, #2e7d32, #388e3c);
  color: white;
  padding: 20px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 12px rgba(0,80,20,0.2);
}
.header h2 { margin: 0; font-size: 20px; font-weight: 700; letter-spacing: 1px; }
.back-btn {
  background: rgba(255,255,255,0.15);
  color: white;
  border: 1px solid rgba(255,255,255,0.3);
  padding: 8px 20px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 13px;
  transition: all 0.2s;
}
.back-btn:hover { background: rgba(255,255,255,0.3); }
.content {
  max-width: 800px;
  margin: 30px auto;
  padding: 0 20px;
}
.empty {
  text-align: center;
  padding: 60px 20px;
  color: #999;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0,80,20,0.04);
  border: 2px dashed #dcedc8;
}
.empty p { margin: 0; font-size: 16px; }
.feedback-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.feedback-item {
  background: white;
  border-radius: 14px;
  padding: 22px 24px;
  box-shadow: 0 2px 12px rgba(0,80,20,0.05);
  border-left: 4px solid #a5d6a7;
  transition: all 0.3s ease;
  animation: fadeInUp 0.4s ease-out both;
}
.feedback-item:nth-child(1) { animation-delay: 0.05s; }
.feedback-item:nth-child(2) { animation-delay: 0.1s; }
.feedback-item:nth-child(3) { animation-delay: 0.15s; }
.feedback-item:hover {
  box-shadow: 0 4px 16px rgba(0,80,20,0.1);
  transform: translateY(-1px);
}
.item-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}
.code {
  font-weight: 700;
  color: #2e7d32;
  font-size: 15px;
  letter-spacing: 0.3px;
}
.status {
  padding: 4px 14px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
}
.status-01 {
  background: #fff3e0;
  color: #e65100;
}
.status-02 {
  background: #e3f2fd;
  color: #1565c0;
}
.status-03 {
  background: #e8f5e9;
  color: #2e7d32;
}
.item-body p {
  margin: 8px 0;
  color: #555;
  font-size: 14px;
  line-height: 1.7;
}
.item-body strong {
  color: #388e3c;
  margin-right: 4px;
}
.item-footer {
  margin-top: 14px;
  padding-top: 14px;
  border-top: 1px solid #e8f5e9;
  color: #999;
  font-size: 12px;
}

@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(12px); }
  to { opacity: 1; transform: translateY(0); }
}

@media (max-width: 768px) {
  .content { margin: 16px; padding: 0 12px; }
  .feedback-item { padding: 16px; }
}
</style>
