# Claude Code 项目搭建提示词

> 用途：让 Claude Code 从零开始搭建东软环保公众监督系统

---

## 任务：搭建东软环保公众监督系统全栈项目

### 项目背景

请从零开始搭建一个完整的"东软环保公众监督系统"，这是一个四端架构的空气质量监督管理系统。
项目要求严格按照 《东软环保公众监督系统-实验手册.md》 中的规范实现。

### 技术栈要求

- **后端**: Spring Boot 2.7+ + JDK 1.8 + MyBatis-Plus + MySQL 5.7+
- **前端**: Vue 3 + Vue CLI 5.0.1 + Vue Router + Axios
- **前端UI**:
  - 公众监督员端 (NEPS): Font Awesome
  - 管理员端 (NEPM): Element Plus 2.2.17
  - 网格员端 (NEPG): Font Awesome
  - 可视化大屏端 (NEPV): ECharts 5.4.0

---

## 工作目录

请在当前工作目录下创建项目，使用以下目录结构：

```
.
├── database_init.sql                       # 已存在
├── 东软环保公众监督系统-实验手册.md         # 已存在
├── 作业.md                                 # 已存在
├── nepmserver/                             # 后端项目（待创建）
├── neps/                                   # 公众监督员端（待创建，端口8081）
├── nepm/                                   # 管理员端（待创建，端口8082）
├── nepg/                                   # 网格员端（待创建，端口8083）
└── nepv/                                   # 可视化大屏端（待创建，端口8084）
```

---

## 第一步：数据库初始化

### 1.1 检查 SQL 文件

读取 `database_init.sql`，确认包含以下 8 张表：

- `admins` (系统管理员)
- `supervisor` (公众监督员)
- `grid_member` (网格员)
- `grid_province` (省区域)
- `grid_city` (市区域)
- `aqi` (AQI等级)
- `aqi_feedback` (公众监督反馈)
- `statistics` (AQI数据统计)

### 1.2 数据库配置

- 数据库名：`nep_system`
- 端口：3306
- 用户名：`root`
- 密码：`123456`（请根据实际情况调整）

---

## 第二步：搭建后端项目 `nepmserver`

### 2.1 创建 Spring Boot 项目

使用 Spring Initializr 或手动创建，依赖包括：

- Spring Web
- MyBatis-Plus 3.5.3+
- MySQL Connector
- Lombok
- PageHelper 或 MyBatis-Plus 分页插件
- Swagger/Knife4j（可选）

### 2.2 项目包结构

```
com.neusoft.nepm
├── NepmserverApplication.java      # 启动类
├── config/                         # 配置类
│   ├── CorsConfig.java            # 跨域配置
│   └── MybatisPlusConfig.java     # MyBatis-Plus 配置
├── controller/                     # 控制层（8 个 Controller）
├── service/                        # 业务层接口
│   └── impl/                      # 业务层实现
├── mapper/                         # 数据访问层
├── po/                             # 实体类（8 个）
├── dto/                            # 数据传输对象
│   ├── AfPageRequestDto.java
│   ├── StatisPageRequestDto.java
│   ├── PageResponseDto.java
│   ├── ProvinceItemTotalStatisDto.java
│   ├── AqiDistributeTotalStatisDto.java
│   └── AqiTrendTotalStatisDto.java
└── common/                         # 公共类
    └── Result.java                # 统一返回结果
```

### 2.3 application.yml 配置

```yaml
server:
  port: 8080
  servlet:
    context-path: /nepm

spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/nep_system?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: root
    password: 123456

mybatis-plus:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.neusoft.nepm.po
  configuration:
    map-underscore-to-camel-case: true
```

### 2.4 必须实现的 26 个接口 API

#### 1. admins 模块（1 个接口）

- `POST /admins/getAdminsByCode` - 管理员登录

#### 2. aqi 模块（1 个接口）

- `GET /aqi/listAqiAll` - 查询所有 AQI 等级

#### 3. aqiFeedback 模块（7 个接口）

- `POST /aqiFeedback/saveAqiFeedback` - 保存反馈
- `POST /aqiFeedback/listAqiFeedbackByTelId` - 根据手机号查询
- `POST /aqiFeedback/listAqiFeedbackPage` - 分页查询（带模糊搜索）
- `POST /aqiFeedback/getAqiFeedbackById` - 根据主键查询
- `POST /aqiFeedback/updateAqiFeedbackAssign` - 更新指派信息
- `POST /aqiFeedback/listAqiFeedbackByGmId` - 根据网格员 ID 查询
- `POST /aqiFeedback/updateAqiFeedbackState` - 更新状态

#### 4. gridCity 模块（1 个接口）

- `POST /gridCity/listGridCityByProvinceId` - 根据省 ID 查询市

#### 5. gridMember 模块（2 个接口）

- `POST /gridMember/listGridMemberByProvinceId` - 根据省市查询网格员
- `POST /gridMember/getGridMemberByCodeByPass` - 网格员登录

#### 6. gridProvince 模块（1 个接口）

- `POST /gridProvince/listGridProvinceAll` - 查询所有省

#### 7. statistics 模块（10 个接口）

- `POST /statistics/saveStatistics` - 保存统计数据
- `POST /statistics/listStatisticsPage` - 分页查询
- `POST /statistics/getStatisticsById` - 根据主键查询
- `GET /statistics/listProvinceItemTotalStatis` - 按省分组统计
- `GET /statistics/listAqiDistributeTotalStatis` - 按 AQI 等级统计
- `GET /statistics/listAqiTrendTotalStatis` - 月度趋势统计
- `GET /statistics/getAqiCount` - AQI 总数
- `GET /statistics/getAqiGoodCount` - 良好天数
- `GET /statistics/getProvinceCoverage` - 省覆盖率
- `GET /statistics/getCityCoverage` - 市覆盖率

#### 8. supervisor 模块（3 个接口）

- `POST /supervisor/getSupervisorById` - 根据手机号查询（注册验证）
- `POST /supervisor/saveSupervisor` - 注册
- `POST /supervisor/getSupervisorByIdByPass` - 监督员登录

### 2.5 统一返回结果类

```java
public class Result<T> {
    private Integer code;    // 200 成功，其他失败
    private String msg;      // 提示信息
    private T data;          // 返回数据
    // getter/setter
}
```

---

## 第三步：搭建前端项目

### 3.1 公众监督员端 `neps`（端口 8081）

#### 创建命令

```bash
vue create neps
# 手动选择：Manually select features
# 模块选择：Babel, Router
# history 模式：N
# 配置文件位置：In package.json
# 保存预设：N
```

#### 安装依赖

```bash
cd neps
npm install font-awesome@4.7.0 axios@1.1.3 --save
```

#### vue.config.js

```javascript
module.exports = {
  transpileDependencies: true,
  devServer: {
    port: 8081
  }
}
```

#### 页面要求

- `Login.vue` - 登录页（手机号 + 密码）
- `Register.vue` - 注册页（手机号、密码、姓名、性别、年龄、地址、省市）
- `SelectGrid.vue` - 选择省市
- `SelectAqi.vue` - 选择 AQI 等级并提交反馈
- `FeedbackList.vue` - 历史反馈列表

#### 路由守卫（main.js）

```javascript
router.beforeEach((to, from, next) => {
  const supervisor = sessionStorage.getItem('supervisor');
  if (['/selectGrid', '/selectAqi', '/feedbackList'].includes(to.path)) {
    if (!supervisor) {
      router.push('/login');
    }
  }
  next();
});
```

### 3.2 管理员端 `nepm`（端口 8082）

#### 创建命令

```bash
vue create nepm
# 模块选择：Babel, Router, Vuex
```

#### 安装依赖

```bash
cd nepm
npm install element-plus@2.2.17 @element-plus/icons-vue@2.0.10 axios@1.1.3 --save
```

#### vue.config.js：端口 8082

#### 页面要求

- `Login.vue` - 管理员登录
- `Index.vue` - 主布局（侧边栏 + 顶部）
- `views/feedback/FeedbackList.vue` - 反馈列表（带分页、模糊搜索）
- `views/feedback/FeedbackInfo.vue` - 反馈详情
- `views/feedback/AssignFeedback.vue` - 指派反馈
- `views/feedback/StatisticsList.vue` - 统计列表
- `views/feedback/StatisticsInfo.vue` - 统计详情
- `views/statistics/ProvinceItemTotalStatis.vue` - 按省统计
- `views/statistics/AqiDistributeTotalStatis.vue` - 等级分布
- `views/statistics/AqiTrendTotalStatis.vue` - 趋势分析
- `views/statistics/OtherTotalStatis.vue` - 其他统计

### 3.3 网格员端 `nepg`（端口 8083）

#### 创建命令

```bash
vue create nepg
# 模块选择：Babel, Router
```

#### 安装依赖

```bash
cd nepg
npm install font-awesome@4.7.0 axios@1.1.3 --save
```

#### vue.config.js：端口 8083

#### 页面要求

- `Login.vue` - 网格员登录
- `FeedbackList.vue` - 待检测任务列表
- `FeedbackInfo.vue` - 录入检测数据（PM2.5、PM10、SO2、NO2、CO、O3）

### 3.4 可视化大屏端 `nepv`（端口 8084）

#### 创建命令

```bash
vue create nepv
# 模块选择：Babel, Router
```

#### 安装依赖

```bash
cd nepv
npm install font-awesome@4.7.0 axios@1.1.2 echarts@5.4.0 jquery@1.11.3 --save
```

#### vue.config.js：端口 8084

#### 页面要求

- `Index.vue` - 大屏可视化（中国地图 + 统计图表）

---

## 第四步：完整业务流程验证

1. **公众监督员端**：注册 → 登录 → 选择地区 → 提交反馈
2. **管理员端**：登录 → 查看反馈 → 指派给网格员
3. **网格员端**：登录 → 查看任务 → 录入检测数据
4. **可视化大屏端**：查看统计数据

---

## 第五步：测试账号

请在初始化数据时添加以下测试账号：

### 管理员

- 账号：admin
- 密码：123456

### 公众监督员

- 手机号：13800001001
- 密码：123456

### 网格员

- 工号：GM001
- 密码：123456

---

## 验收标准

- [x] 数据库 8 张表能成功创建并插入测试数据
- [x] 后端能成功启动在 8080 端口，26 个接口全部可访问
- [x] 三个前端能分别启动在 8081/8082/8083
- [x] 完整的业务流程能跑通
- [x] 代码结构清晰，遵循 MVC 架构
- [x] 所有接口都有统一的 Result 包装

---

## 重要提示

1. 严格按照实验手册的接口路径、参数、返回值定义实现
2. 注意跨域问题，后端需要配置 CorsConfig
3. 前端所有 axios 请求的 baseURL 设置为 `http://localhost:8080/nepm/`
4. 注意密码加密（可以使用 MD5 或 BCrypt）
5. 实体类字段需要与数据库表字段对应
6. 状态字段（state）使用：0-待指派, 1-待确认, 2-已完成

---

## 工作方式

请按以下顺序逐步完成，**每创建一个文件/目录，都必须明确告诉我**：

- 📁 **创建位置**：文件的完整绝对路径
- 📄 **文件名**：创建的具体文件名
- 📝 **文件作用**：这个文件是做什么的
- ✅ **完成情况**：当前进度（已完成 X / 总共 Y 个文件）

### 报告格式要求

每完成一个文件后，请使用以下格式输出：

```
✅ 已创建文件
📁 路径：[文件绝对路径]
📄 文件名：[文件名]
📝 作用：[简述功能]
📊 当前进度：[X/Y]
```

### 工作步骤

1. **创建项目目录结构** → 报告所有创建的目录路径
2. **创建后端项目** → 每个 Java 文件、配置文件都要报告路径
3. **创建后端 8 个实体类** → 逐个报告 `po/` 下的文件路径
4. **创建后端 26 个接口** → 每个 Controller、Service、Mapper 都要报告
5. **测试后端接口** → 报告测试结果
6. **创建前端 neps（公众监督员端）** → 报告 `neps/src/` 下的所有文件
7. **创建前端 nepm（管理员端）** → 报告 `nepm/src/` 下的所有文件
8. **创建前端 nepg（网格员端）** → 报告 `nepg/src/` 下的所有文件
9. **端到端联调测试** → 报告测试日志

### 路径示例

假设在 `D:\project\` 下创建项目，应该这样报告：

```
✅ 已创建目录
📁 路径：D:\project\nepmserver
📝 作用：后端项目根目录
📊 当前进度：1/100
```

```
✅ 已创建文件
📁 路径：D:\project\nepmserver\pom.xml
📄 文件名：pom.xml
📝 作用：Maven 项目配置文件
📊 当前进度：2/100
```

> 如果在实现过程中遇到问题，请先尝试自己解决 5 分钟，再向我询问。

---

## 开始指令

> 现在开始吧！请先创建项目目录结构，**每创建一个目录/文件都要按照上面的报告格式输出**，让我清楚知道文件创建到哪了。
