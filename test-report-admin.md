# 测试报告 - 管理员页面交互Bug

**测试日期：** 2026-06-21
**测试范围：** 管理员页面全部模块（反馈管理、统计列表、数据统计4页面、管理员设置）
**测试类型：** 代码审查 + 交互逻辑分析

---

## 🔴 已发现并修复的Bug

### Bug #1：ECharts折线图渲染崩溃（LineView.render TypeError）
- **严重级别：** 🔴严重
- **类型：** 功能/JS报错
- **影响页面：** 月度AQI趋势分析（AqiTrendTotalStatis.vue）
- **根因：** 折线图 `avgAqi` 系列数据使用 `.toFixed(1)` 返回字符串（如 `"42.5"`），而非数字。ECharts 的 `LineView.render` 在渲染时尝试读取 `type` 属性，遇到字符串数据导致 `Cannot read properties of undefined (reading 'type')`。同时 `totalCount` 数据未做 null 防护。
- **修复：** `Number(d.avgAqi || 0).toFixed(1)` → `Number(d.avgAqi) || 0`，确保传递数字类型；增加 null 防护。

### Bug #2：ECharts柱状图渲染崩溃（barPolar TypeError）
- **严重级别：** 🔴严重
- **类型：** 功能/JS报错
- **影响页面：** 各省空气质量统计（ProvinceItemTotalStatis.vue）
- **根因：** 柱状图 series 数据直接使用 `d.good_count`/`d.polluted_count`/`d.total_count`，未做 `Number()` 转换。当数据含 null/undefined 时，ECharts 内部 barPolar 布局计算失败。此外 `province_name` 经 LEFT JOIN 可能为 null，未过滤。
- **修复：** 所有 series 数据增加 `Number()` 转换和默认值 0；过滤掉 `province_name` 为 null 的记录。

### Bug #3：后端优良记录统计SQL错误
- **严重级别：** 🟡中等
- **类型：** 功能
- **影响页面：** 综合数据概览（OtherTotalStatis.vue）
- **根因：** `StatisticsMapper.getAqiGoodCount()` SQL 为 `WHERE aqi_level = '良'`，但前端标签显示"优良记录数"，实际应同时统计"优"和"良"两个等级。导致优良率偏低、仪表盘显示不准确。
- **修复：** `aqi_level = '良'` → `aqi_level IN ('优','良')`

### Bug #4：图表缺少空数据防护
- **严重级别：** 🟡中等
- **类型：** UI/健壮性
- **影响页面：** 所有统计图表页面（4个）
- **根因：** 当 API 返回空数据时，图表仍尝试初始化并 setOption 空数组，可能导致渲染异常或空白图表无提示。
- **修复：** 所有图表渲染前增加空数据判断，空数据时显示"暂无数据"提示。

### Bug #5：图表缺少窗口resize适配
- **严重级别：** 🟢轻微
- **类型：** UI
- **影响页面：** 所有统计图表页面（4个）
- **根因：** 浏览器窗口大小变化时图表不会自动调整尺寸，导致图表溢出或变形。
- **修复：** 在 `mounted` 中注册 `window.resize` 事件监听，`beforeUnmount` 中注销。

### Bug #6：gauge图表全量数据防护
- **严重级别：** 🟡中等
- **类型：** 功能
- **影响页面：** 综合数据概览（OtherTotalStatis.vue）
- **根因：** `renderGauges()` 中 gauge 的 `data[0].value` 直接使用计算值，未做 `Number()` 转换；当所有 API 都返回 0 时仍会初始化 gauge 可能导致异常渲染。
- **修复：** 增加 `Number(value) || 0` 防护；增加空数据前置判断。

---

## 🟢 未发现问题的模块

| 模块 | 状态 | 备注 |
|------|------|------|
| 管理员登录（Login.vue） | ✅ | 无明显问题 |
| 主布局框架（Index.vue） | ✅ | 侧边栏、header 正常 |
| 反馈列表（FeedbackList.vue） | ✅ | 搜索、分页、状态显示正常 |
| 反馈详情（FeedbackInfo.vue） | ✅ | 字段展示完整 |
| 指派网格员（AssignFeedback.vue） | ✅ | 省→市→网格员级联正常 |
| 统计列表（StatisticsList.vue） | ✅ | 搜索、分页正常 |
| 统计详情（StatisticsInfo.vue） | ✅ | 数据展示完整 |
| 管理员设置（AdminSettings.vue） | ✅ | 信息修改、密码修改逻辑正确 |
| 路由守卫（router/index.js） | ✅ | 未登录拦截正常 |
| API配置（api/index.js） | ✅ | 拦截器逻辑正确 |

---

## 修复文件清单

| 文件 | 修改内容 |
|------|----------|
| `nep-frontend/nepm/src/views/statistics/AqiTrendTotalStatis.vue` | 修复 avgAqi 字符串问题、空数据防护、resize适配 |
| `nep-frontend/nepm/src/views/statistics/AqiDistributeTotalStatis.vue` | 空数据防护、数据 Number 转换、resize适配 |
| `nep-frontend/nepm/src/views/statistics/ProvinceItemTotalStatis.vue` | 数据 Number 转换、null province_name 过滤、空数据防护、resize适配 |
| `nep-frontend/nepm/src/views/statistics/OtherTotalStatis.vue` | gauge 数据防护、空数据判断、resize适配 |
| `src/main/java/com/neusoft/nepm/mapper/StatisticsMapper.java` | 修复 getAqiGoodCount SQL，包含优+良 |

---

## 质量评估

**整体评分：** ⭐⭐⭐⭐☆ (4/5)
**发布建议：** ⚠️ 条件通过（修复上述Bug后可通过）
