<template>
  <div class="select-aqi-container">
    <div class="header">
      <h2>提交反馈</h2>
      <button class="logout-btn" @click="logout">退出登录</button>
    </div>
    <div class="content">
      <div class="form-group">
        <label>反馈类型</label>
        <select v-model="form.afType" required>
          <option value="">请选择反馈类型</option>
          <option value="01">空气有异味</option>
          <option value="02">雾霾严重</option>
          <option value="03">工业排放</option>
          <option value="04">其他</option>
        </select>
      </div>
      <div class="form-group">
        <label>预估AQI等级</label>
        <select v-model="form.afLevel" required>
          <option value="">请选择AQI等级</option>
          <option v-for="a in aqiList" :key="a.aqiId" :value="a.aqiLevel">{{ a.aqiLevel }}</option>
        </select>
      </div>
      <div class="form-group">
        <label>问题描述</label>
        <textarea v-model="form.afDesc" placeholder="请详细描述问题" required></textarea>
      </div>
      <div class="form-group">
        <label>问题发生地点</label>
        <input type="text" v-model="form.afAddress" placeholder="请输入详细地址" required>
      </div>
      <div class="form-row">
        <div class="form-group">
          <label>经度</label>
          <input type="number" step="0.000001" v-model="form.afLongitude" placeholder="经度">
        </div>
        <div class="form-group">
          <label>纬度</label>
          <input type="number" step="0.000001" v-model="form.afLatitude" placeholder="纬度">
        </div>
      </div>
      <button class="submit-btn" @click="submitFeedback">提交反馈</button>
      <button class="history-btn" @click="viewHistory">查看历史</button>
    </div>
  </div>
</template>

<script>
import api from '@/api'

export default {
  name: 'SelectAqi',
  data() {
    return {
      aqiList: [],
      form: {
        afType: '',
        afLevel: '',
        afDesc: '',
        afAddress: '',
        afLongitude: '',
        afLatitude: ''
      }
    }
  },
  created() {
    this.loadAqiList()
  },
  methods: {
    async loadAqiList() {
      const res = await api.get('/aqi/listAqiAll')
      if (res.code === 200) {
        this.aqiList = res.data
      }
    },
    async submitFeedback() {
      const supervisor = JSON.parse(sessionStorage.getItem('supervisor'))
      const feedback = {
        ...this.form,
        supervisorId: supervisor.supervisorId,
        afProvinceId: supervisor.provinceId,
        afCityId: supervisor.cityId
      }
      try {
        const res = await api.post('/aqiFeedback/saveAqiFeedback', feedback)
        if (res.code === 200) {
          alert('提交成功')
          this.form = {
            afType: '',
            afLevel: '',
            afDesc: '',
            afAddress: '',
            afLongitude: '',
            afLatitude: ''
          }
        } else {
          alert(res.msg)
        }
      } catch (error) {
        alert('提交失败')
      }
    },
    viewHistory() {
      this.$router.push('/feedbackList')
    },
    logout() {
      sessionStorage.removeItem('supervisor')
      this.$router.push('/login')
    }
  }
}
</script>

<style scoped>
.select-aqi-container {
  min-height: 100vh;
  background: #f5f5f5;
}
.header {
  background: #667eea;
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
  max-width: 600px;
  margin: 30px auto;
  background: white;
  padding: 40px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
.form-group {
  margin-bottom: 20px;
}
.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #333;
  font-weight: 500;
}
.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 14px;
}
.form-group textarea {
  height: 100px;
  resize: vertical;
}
.form-row {
  display: flex;
  gap: 15px;
}
.form-row .form-group {
  flex: 1;
}
.submit-btn {
  width: 100%;
  padding: 12px;
  background: #667eea;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
  margin-bottom: 10px;
}
.submit-btn:hover {
  background: #556cd6;
}
.history-btn {
  width: 100%;
  padding: 12px;
  background: #fff;
  color: #667eea;
  border: 1px solid #667eea;
  border-radius: 5px;
  font-size: 16px;
  cursor: pointer;
}
.history-btn:hover {
  background: #f5f5f5;
}
</style>
