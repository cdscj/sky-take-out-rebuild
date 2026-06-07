# Sky Take-Out Platform

外卖点餐与校园跑腿服务平台，包含论坛社区模块。

## 项目结构

```
├── sky-take-out/          # 苍穹外卖 — 外卖点餐与校园跑腿后端
│   ├── sky-server/        # Spring Boot 主服务 (端口 8080)
│   ├── sky-common/        # 公共工具、JWT、OSS、异常处理
│   └── sky-pojo/          # DTO、VO、实体类
│
├── forum-master/          # 文心论坛 — 社区论坛平台
│   ├── forum-client/      # Spring Boot 客户端 (端口 8082)
│   ├── forum-service/     # 论坛业务逻辑 + MyBatis 映射
│   ├── forum-common/      # 公共工具、注解、基础类
│   ├── forum-framework/   # 安全框架、数据源、AOP
│   ├── forum-system/      # 后台管理 RBAC
│   ├── forum-quartz/      # 定时任务
│   ├── forum-generator/   # 代码生成器
│   └── forum-ui/          # Vue 2 + Element UI 前端
│
├── mp-weixin/             # 微信小程序 (uni-app 编译输出)
└── nginx-1.20.2/          # Nginx 反向代理配置
```

## 技术栈

| 层面 | 技术 |
|------|------|
| 后端框架 | Spring Boot 2.5 ~ 2.7 |
| 安全认证 | Spring Security + JWT + 双认证域(用户/管理员) |
| 数据层 | MyBatis + Druid 连接池 + MySQL 8 |
| 缓存 | Redis (Spring Cache) |
| 定时任务 | Spring @Scheduled |
| 实时通信 | WebSocket (JWT 认证) |
| 支付集成 | 微信支付 V3 API |
| 对象存储 | 阿里云 OSS |
| 前端 | Vue 2 + Element UI + TinyMCE |
| 小程序 | uni-app → 微信小程序 |

## 快速启动

### 环境要求
- JDK 8+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
- Node.js 16+ (仅前端)

### 1. 初始化数据库

```bash
mysql -u root -p < forum-master/sql/forum_20240830.sql
mysql -u root -p < forum-master/sql/quartz.sql
# sky-take-out 数据库
mysql -u root -p -e "CREATE DATABASE sky_take_out"
```

### 2. 配置环境变量

```bash
cp .env.template .env
# 编辑 .env 填入数据库/Redis/微信支付等配置
```

### 3. 启动后端

```bash
# 苍穹外卖
cd sky-take-out
mvn clean install -DskipTests
mvn spring-boot:run -pl sky-server

# 文心论坛
cd forum-master
mvn clean install -DskipTests
mvn spring-boot:run -pl forum-client
```

### 4. 启动前端

```bash
cd forum-master/forum-ui
yarn install
yarn dev
```

## 内置功能

### 苍穹外卖
- 管理员端：菜品/套餐/分类管理、订单管理、数据报表、员工管理
- 用户端：浏览菜品、购物车、下单支付、地址管理、历史订单
- 骑手端：接单配送、状态更新
- WebSocket 实时推送

### 文心论坛
- 用户注册/登录/个人中心
- 发帖/回帖/点赞/关注/举报
- 主题标签/敏感词过滤
- 后台管理（RBAC 权限模型）

## License

MIT License — 仅供学习交流使用。
