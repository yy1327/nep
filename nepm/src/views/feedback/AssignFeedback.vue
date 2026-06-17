<template>
  <div class="assign-feedback">
    <el-card>
      <div slot="header">
        <span>指派反馈</span>
        <el-button style="float: right" @click="goBack">返回</el-button>
      </div>
      <el-form :model="form" label-width="100px">
        <el-form-item label="省份">
          <el-select v-model="form.provinceId" @change="loadGridMembers" placeholder="请选择省份">
            <el-option v-for="p in provinces" :key="p.provinceId" :label="p.provinceName" :value="p.provinceId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="城市">
          <el-select v-model="form.cityId" @change="loadGridMembers" placeholder="请选择城市">
            <el-option v-for="c in cities" :key="c.cityId" :label="c.cityName" :value="c.cityId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="网格员">
          <el-select v-model="form.gmId" placeholder="请选择网格员">
            <el-option v-for="g in gridMembers" :key="g.gm_id" :label="g.gm_name" :value="g.gm_id"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="指派备注">
          <el-input v-model="form.desc" type="textarea" placeholder="请输入指派备注"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitAssign">确认指派</el-button>
          <el-button @click="goBack">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import api from '@/api'

export default {
  name: 'AssignFeedback',
  data() {
    return {
      form: {
        provinceId: '',
        cityId: '',
        gmId: '',
        desc: ''
      },
      provinces: [],
      cities: [],
      gridMembers: []
    }
  },
  created() {
    this.loadProvinces()
  },
  methods: {
    async loadProvinces() {
      const res = await api.post('/gridProvince/listGridProvinceAll')
      if (res.code === 200) {
        this.provinces = res.data
      }
    },
    async loadCities() {
      if (this.form.provinceId) {
        const res = await api.post('/gridCity/listGridCityByProvinceId', {
          provinceId: this.form.provinceId
        })
        if (res.code === 200) {
          this.cities = res.data
        }
      }
    },
    async loadGridMembers() {
      if (this.form.provinceId && this.form.cityId) {
        const res = await api.post('/gridMember/listGridMemberByProvinceId', {
          provinceId: this.form.provinceId,
          cityId: this.form.cityId
        })
        if (res.code === 200) {
          this.gridMembers = res.data
        }
      }
    },
    async submitAssign() {
      const afId = this.$route.params.id
      const res = await api.post('/aqiFeedback/updateAqiFeedbackAssign', {
        afId: parseInt(afId),
        gmId: this.form.gmId,
        desc: this.form.desc
      })
      if (res.code === 200) {
        this.$message.success('指派成功')
        this.$router.push('/index/feedbackList')
      } else {
        this.$message.error(res.msg)
      }
    },
    goBack() {
      this.$router.push('/index/feedbackList')
    }
  }
}
</script>
