# 东软环保公众监督系统

基于 Spring Boot + Vue 的全栈环保监督管理系统，包含后端 API 和 4 个前端应用。

---

## 系统架构

```
nep/
├── nepmserver/       # Spring Boot 后端 (端口 8080)
├── neps/             # 公众监督员前端 (端口 8081)
├── nepm/             # 管理员前端 (端口 8082)
├── nepg/             # 网格员前端 (端口 8083)
└── nepv/             # 数据可视化大屏 (端口 8084)
```

---

## 环境要求

- JDK 1.8+
- Node.js 16+
- MySQL 5.7+（数据库 `nep_system` 已在外部配置好）
- Maven 3.6+

---

## 快速启动

### 1. 启动后端

```bash
cd nepmserver
mvn spring-boot:run
```

后端启动后运行在 `http://localhost:8080`

### 2. 启动前端（分别在 4 个终端中执行）

```bash
# 公众监督员端
cd neps
npm run dev

# 管理员端
cd nepm
npm run dev

# 网格员端
cd nepg
npm run dev

# 可视化大屏
cd nepv
npm run dev
```

---

## 访问地址

| 应用 | 地址 | 说明 |
|------|------|------|
| 后端 API | http://localhost:8080 | Spring Boot REST API |
| 公众监督员端 | http://localhost:8081 | 监督员注册、提交反馈 |
| 管理员端 | http://localhost:8082 | 管理员后台管理 |
| 网格员端 | http://localhost:8083 | 网格员处理任务 |
| 可视化大屏 | http://localhost:8084 | AQI 数据可视化 |

---

## 测试账号

### 管理员

| 账号 | 密码 | 说明 |
|------|------|------|
| `admin` | `123456` | 系统管理员 |

### 公众监督员

| 账号 | 密码 | 姓名 | 城市 |
|------|------|------|------|
| `SP001` | `123456` | 张三 | 北京市 |
| `SP002` | `123456` | 李四 | 上海市 |
| `SP003` | `123456` | 王五 | 广州市 |

### 空气质量监测网格员

| 账号 | 密码 | 姓名 | 城市 |
|------|------|------|------|
| `GM001` | `123456` | 赵网格 | 北京市 |
| `GM002` | `123456` | 钱网格 | 上海市 |
| `GM003` | `123456` | 孙网格 | 广州市 |

---

## 业务流程

```
公众监督员提交反馈 (neps)
        ↓
管理员查看并指派网格员 (nepm)
        ↓
网格员实地检测并确认 (nepg)
        ↓
数据统计在可视化大屏展示 (nepv)
```

---

## 后端 API 接口

| 模块 | 接口 | 说明 |
|------|------|------|
| 管理员 | `POST /api/admins/login` | 管理员登录 |
| 监督员 | `POST /api/supervisor/login` | 监督员登录 |
| 监督员 | `POST /api/supervisor/register` | 监督员注册 |
| 网格员 | `POST /api/grid-member/login` | 网格员登录 |
| 反馈 | `POST /api/aqi-feedback/submit` | 提交反馈 |
| 反馈 | `GET /api/aqi-feedback/list` | 查询反馈列表 |
| 反馈 | `PUT /api/aqi-feedback/assign` | 指派网格员 |
| 反馈 | `PUT /api/aqi-feedback/confirm` | 网格员确认 |
| 统计 | `GET /api/statistics/list` | 查询统计数据 |
| AQI | `GET /api/aqi/list` | 查询 AQI 等级 |

完整 API 文档请查看各 Controller 源码。

---

## 数据库配置

在 `nepmserver/src/main/resources/application.yml` 中配置：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/nep_system?useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: ZHANGyan123w
```

---

## 技术栈

- **后端**: Spring Boot 2.7 + MyBatis-Plus + MySQL
- **前端**: Vue 3 + Vite + Element Plus + Axios
- **可视化**: ECharts

---

## GitHub 仓库

```bash
git clone https://github.com/yy1327/nep.git
```
