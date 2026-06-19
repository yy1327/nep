<template>
  <div class="feedback-list-container">
    <div class="header">
      <h2>待检测任务</h2>
      <button class="logout-btn" @click="logout">退出登录</button>
    </div>
    <div class="content">
      <div v-if="feedbackList.length === 0" class="empty">
        <p>暂无待检测任务</p>
      </div>
      <div v-else class="feedback-list">
        <div
          v-for="item in feedbackList"
          :key="item.af_id"
          class="feedback-item"
          @click="viewInfo(item.af_id)"
        >
          <div class="item-header">
            <span class="code">{{ item.af_code }}</span>
            <span :class="['status', 'status-' + item.af_state]">{{
              getStatusText(item.af_state)
            }}</span>
          </div>
          <div class="item-body">
            <p><strong>类型：</strong>{{ getTypeText(item.af_type) }}</p>
            <p><strong>地址：</strong>{{ item.af_address }}</p>
            <p><strong>描述：</strong>{{ item.af_desc }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import api from "@/api";

export default {
  name: "FeedbackList",
  data() {
    return {
      feedbackList: [],
    };
  },
  created() {
    this.loadFeedbackList();
  },
  methods: {
    async loadFeedbackList() {
      const gridMember = JSON.parse(sessionStorage.getItem("gridMember"));
      const res = await api.post("/aqiFeedback/listAqiFeedbackByGmId", {
        gmId: gridMember.gmId,
      });
      if (res.code === 200) {
        this.feedbackList = res.data || [];
      }
    },
    getStatusText(state) {
      const map = { "01": "待指派", "02": "待确认", "03": "已完成" };
      return map[state] || state;
    },
    getTypeText(type) {
      const map = {
        "01": "空气有异味",
        "02": "雾霾严重",
        "03": "工业排放",
        "04": "其他",
      };
      return map[type] || type;
    },
    viewInfo(id) {
      this.$router.push(`/feedbackInfo/${id}`);
    },
    logout() {
      sessionStorage.removeItem("gridMember");
      this.$router.push("/login");
    },
  },
};
</script>

<style scoped>
.feedback-list-container {
  min-height: 100vh;
  background: #f5f5f5;
}
.header {
  background: #11998e;
  color: white;
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.logout-btn {
  background: transparent;
  color: white;
  border: 1px solid white;
  padding: 8px 16px;
  border-radius: 5px;
  cursor: pointer;
}
.content {
  max-width: 800px;
  margin: 30px auto;
  padding: 0 20px;
}
.empty {
  text-align: center;
  padding: 50px;
  color: #999;
}
.feedback-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}
.feedback-item {
  background: white;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  transition: transform 0.2s;
}
.feedback-item:hover {
  transform: translateY(-2px);
}
.item-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 15px;
}
.code {
  font-weight: bold;
  color: #333;
}
.status {
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
}
.status-01 {
  background: #fff3e0;
  color: #f57c00;
}
.status-02 {
  background: #e3f2fd;
  color: #1976d2;
}
.status-03 {
  background: #e8f5e9;
  color: #388e3c;
}
.item-body p {
  margin: 8px 0;
  color: #666;
}
</style>
