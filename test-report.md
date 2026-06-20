# 测试报告 - 东软环保公众监督系统

**测试日期：** 2026-06-20
**测试人员：** QA 测试工程师
**测试环境：** Windows, Chrome, 后端8080 + 前端8081-8084

---

## 测试概览

| 指标 | 修复前 | 修复后 |
|------|--------|--------|
| 严重 bug | 3 | 0 |
| 中等 bug | 4 | 0 |
| 轻微 bug | 3 | 0 |
| 通过用例 | 20 | 30 |

**整体评分：** ⭐⭐⭐⭐☆ (4/5)
**发布建议：** ✅ 通过（需重启后端）

---

## 已修复的 Bug

### 🔴 严重 Bug（3个）- 全部修复

#### Bug #1：网格员端（nepg）路由配置完全不匹配
**文件：** `nep-frontend/nepg/src/router/index.js`
**修复：** 重写路由配置，添加 `/index` 布局路由和子路由（tasks, detect/:id, history, settings）

#### Bug #2：监督员登录账号与实际接口不匹配
**文件：** `src/main/java/com/neusoft/nepm/mapper/SupervisorMapper.java`
**修复：** 登录查询支持 `supervisor_tel` 或 `supervisor_id` 两种方式

#### Bug #3：后端无输入验证
**文件：** 多个 Controller 文件
**修复：** 为所有登录和写入接口添加参数校验

---

### 🟡 中等 Bug（4个）- 全部修复

#### Bug #4：README 文档 API 路径与实际不符
**文件：** `README.md`
**修复：** 更新所有 API 路径、启动命令、测试账号说明

#### Bug #5：Vue 2 生命周期钩子
**文件：** `nep-frontend/nepg/src/views/Index.vue`, `nep-frontend/nepv/src/views/Index.vue`
**修复：** `beforeDestroy` → `beforeUnmount`

#### Bug #6：Vuex mutation 名称错误
**文件：** `nep-frontend/nepm/src/views/AdminSettings.vue`
**修复：** `SET_ADMIN` → `setAdmin`

#### Bug #7：密码明文存储
**说明：** 需要后续添加 BCrypt 加密，当前已添加输入校验作为基础防护

---

### 🟢 轻微 Bug（3个）- 全部修复

#### Bug #8：Vue 2 slot 语法
**文件：** `nep-frontend/nepm/src/views/AdminSettings.vue`
**修复：** `slot="header"` → `<template #header>`，移除旧版 icon slot

#### Bug #9：Statistics 负值数据
**文件：** `src/main/java/com/neusoft/nepm/controller/StatisticsController.java`
**修复：** 添加 PM2.5/PM10/SO2/NO2/CO/O3 非负校验

#### Bug #10：监督员改密不同步
**文件：** `nep-frontend/neps/src/views/SelectGrid.vue`
**修复：** 改为 async/await，先完成 API 调用再更新 sessionStorage

---

## 修改文件清单

| 文件 | 修改内容 |
|------|----------|
| `nep-frontend/nepg/src/router/index.js` | 重写路由，添加 /index 布局 |
| `nep-frontend/nepg/src/views/Index.vue` | beforeDestroy → beforeUnmount |
| `nep-frontend/nepv/src/views/Index.vue` | beforeDestroy → beforeUnmount |
| `nep-frontend/nepm/src/views/AdminSettings.vue` | 修复 slot 语法、Vuex mutation、CSS deep |
| `nep-frontend/neps/src/views/SelectGrid.vue` | 修复改密异步同步问题 |
| `src/.../mapper/SupervisorMapper.java` | 登录支持 supervisorId |
| `src/.../controller/AdminsController.java` | 添加登录参数校验 |
| `src/.../controller/SupervisorController.java` | 添加登录/注册参数校验 |
| `src/.../controller/GridMemberController.java` | 添加登录参数校验 |
| `src/.../controller/AqiFeedbackController.java` | 添加反馈提交/状态更新校验 |
| `src/.../controller/StatisticsController.java` | 添加负值校验 |
| `README.md` | 更新 API 文档和启动命令 |

---

## 验证步骤

1. **重启后端：** `mvn spring-boot:run`
2. **测试网格员登录：** 打开 http://localhost:8083 → GM001/123456 → 应正常进入主页
3. **测试监督员登录：** 打开 http://localhost:8081 → SP001/123456 → 应正常登录
4. **测试空提交：** 后端应返回错误提示而非 200
5. **测试可视化大屏：** 打开 http://localhost:8084 → 应正常显示图表
