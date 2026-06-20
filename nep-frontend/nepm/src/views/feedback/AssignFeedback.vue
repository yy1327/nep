<template>
  <div class="assign-feedback">
    <div class="page-header">
      <h2>👷 指派网格员</h2>
      <span class="page-sub">为该反馈指派合适的网格员进行实地检测</span>
    </div>
    <el-card class="assign-card">
      <template #header>
        <div style="display:flex;justify-content:space-between;align-items:center">
          <span class="card-title">指派信息</span>
          <el-button @click="goBack" type="primary" plain>← 返回列表</el-button>
        </div>
      </template>
      <el-form :model="form" label-width="100px" size="large">
        <el-form-item label="省份">
          <el-select
            v-model="form.provinceId"
            @change="handleProvinceChange"
            placeholder="请选择省份"
            style="width:100%"
          >
            <el-option
              v-for="p in provinces"
              :key="p.provinceId"
              :label="p.provinceName"
              :value="p.provinceId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="城市">
          <el-select
            v-model="form.cityId"
            @change="loadGridMembers"
            placeholder="请选择城市"
            style="width:100%"
          >
            <el-option
              v-for="c in cities"
              :key="c.cityId"
              :label="c.cityName"
              :value="c.cityId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="网格员">
          <el-select v-model="form.gmId" placeholder="请选择网格员" style="width:100%">
            <el-option
              v-for="g in gridMembers"
              :key="g.gmId"
              :label="g.gmName"
              :value="g.gmId"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="指派备注">
          <el-input
            v-model="form.desc"
            type="textarea"
            :rows="3"
            placeholder="请输入指派备注"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitAssign" size="large">确认指派</el-button>
          <el-button @click="goBack" size="large">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import api from "@/api";

export default {
  name: "AssignFeedback",
  data() {
    return {
      form: {
        provinceId: "",
        cityId: "",
        gmId: "",
        desc: "",
      },
      provinces: [],
      cities: [],
      gridMembers: [],
    };
  },
  created() {
    this.loadProvinces();
  },
  methods: {
    async loadProvinces() {
      const res = await api.post("/gridProvince/listGridProvinceAll");
      if (res.code === 200) {
        this.provinces = res.data;
      }
    },
    handleProvinceChange() {
      this.form.cityId = "";
      this.form.gmId = "";
      this.gridMembers = [];
      this.loadCities();
    },
    async loadCities() {
      if (this.form.provinceId) {
        const res = await api.post("/gridCity/listGridCityByProvinceId", {
          provinceId: this.form.provinceId,
        });
        if (res.code === 200) {
          this.cities = res.data;
        } else {
          this.cities = [];
        }
      } else {
        this.cities = [];
      }
    },
    async loadGridMembers() {
      if (this.form.provinceId && this.form.cityId) {
        const res = await api.post("/gridMember/listGridMemberByProvinceId", {
          provinceId: this.form.provinceId,
          cityId: this.form.cityId,
        });
        if (res.code === 200) {
          this.gridMembers = res.data;
        } else {
          this.gridMembers = [];
        }
      } else {
        this.gridMembers = [];
      }
    },
    async submitAssign() {
      if (!this.form.gmId) {
        this.$message.warning("请选择网格员");
        return;
      }
      const afId = this.$route.params.id;
      const res = await api.post("/aqiFeedback/updateAqiFeedbackAssign", {
        afId: parseInt(afId),
        gmId: this.form.gmId,
        desc: this.form.desc,
      });
      if (res.code === 200) {
        this.$message.success("指派成功");
        this.$router.push("/index/feedbackList");
      } else {
        this.$message.error(res.msg);
      }
    },
    goBack() {
      this.$router.push("/index/feedbackList");
    },
  },
};
</script>

<style scoped>
.assign-feedback { padding: 20px; animation: fadeInUp 0.4s ease-out; }
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(12px); }
  to { opacity: 1; transform: translateY(0); }
}
.page-header { margin-bottom: 20px; }
.page-header h2 { font-size: 20px; color: #1b5e20; margin: 0 0 6px; font-weight: 700; line-height: 1.4; }
.page-sub { color: #999; font-size: 13px; line-height: 1.5; }
.assign-card { border-radius: 12px; }
.card-title { font-size: 16px; font-weight: 600; color: #333; }
</style>
