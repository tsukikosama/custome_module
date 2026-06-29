## 项目概述

本项目基于 [ContiNew Admin](https://github.com/continew-org/continew-admin) 框架改造，扩展为**多端可用的中后台管理系统框架**。在保留原框架 RBAC 权限体系、多租户、代码生成器等开箱即用能力的基础上，将系统拆分为两个独立部署的应用，分别面向管理后台和员工/客户端场景，共享同一套基础数据。

- **管理后台应用**（continew-server）：面向管理员，提供完整的系统管理、权限控制、订单审核等功能
- **员工客户端应用**（continew-customer）：面向员工/终端用户，提供下单、查询、个人信息管理等轻量级 API

项目采用多模块 Maven 结构，基于 Spring Boot 3 + Java 17，集成了 MyBatis Plus、Sa-Token、JetCache 等主流技术栈。

## 应用架构

```
┌─────────────────────────────────────────────────────────────┐
│                       客户端/门户                            │
└─────────────────────┬───────────────────────────────────────┘
                      │ /api/*
┌─────────────────────▼───────────────────────────────────────┐
│              continew-customer (员工客户端应用)                │
│         CustomerApplication (端口建议 8081)                   │
│         API: /api/dept, /api/file, /api/message, ...         │
└──────────┬────────────────────────────────────┬──────────────┘
           │                                     │
           │       ┌─────────────────────────────┘
           │       │
┌──────────▼───────▼──────────────────────────────────────────┐
│                    continew-common (公共模块)                 │
│        共享实体: UserDO, DeptDO, OrderDO                     │
│        共享工具、配置、基础 Mapper                            │
└──────────┬───────┬──────────────────────────────┬───────────┘
           │       │                              │
┌──────────▼───────▼────────┐  ┌──────────────────▼───────────┐
│   continew-system         │  │  continew-plugin              │
│   系统管理模块              │  │  插件模块 (多租户/代码生成等)   │
└───────────────────────────┘  └──────────────────────────────┘
           │
┌──────────▼──────────────────────────────────────────────────┐
│           continew-server (管理后台应用)                      │
│      ContiNewAdminApplication (端口 8080)                    │
│      API: /biz/*                                             │
└──────────────────────────────────────────────────────────────┘
```

## 应用说明

### 管理后台应用 (continew-server)

面向系统管理员，提供完整的管理后台功能：

- **系统管理**：用户管理、角色管理、部门管理、菜单管理、字典管理
- **订单管理**：订单审核、发货管理
- **系统监控**：操作日志、在线用户、系统配置
- **通知公告**：系统通知发布与管理

启动类：`top.continew.admin.ContiNewAdminApplication`

### 员工客户端应用 (continew-customer)

面向员工/终端用户，提供轻量级 HTTP API：

- **个人信息**：用户信息查询、密码修改
- **数据查询**：部门列表查询、消息/通知列表
- **文件操作**：文件上传
- **统计概览**：用户统计、消息未读数

启动类：`top.continew.admin.customer.CustomerApplication`

两个应用共享同一套数据库，通过 continew-common 模块复用基础实体和工具类。

## 构建与开发命令

### 项目构建

```bash
# 清理编译（含 Spotless 插件自动代码格式化）
mvn clean compile

# 打包应用
mvn clean package

# 跳过测试构建（默认配置已跳过）
mvn clean package -DskipTests

# 运行指定测试
mvn test -Dtest=TestClassName
```

### 本地启动

```bash
# 启动管理后台（端口 8080）
cd continew-server
mvn spring-boot:run

# 启动员工客户端（端口 8081）
cd continew-customer
mvn spring-boot:run
```

### 打包部署

```bash
# 打包管理后台
cd continew-server
mvn clean package -Dspotless.skip=true
java -jar target/continew-admin.jar

# 打包员工客户端
cd continew-customer
mvn clean package -Dspotless.skip=true
java -jar target/continew-customer.jar
```

### Docker 部署

项目提供完整的 Docker 编排支持：

```bash
# 一键启动所有服务
cd docker
docker-compose up -d
```

包含服务：continew-admin、Nginx、Redis、Schedule Server。

## 模块结构

| 模块 | 说明 |
|------|------|
| **continew-server** | 管理后台应用，启动类 `ContiNewAdminApplication`，提供管理端 API |
| **continew-customer** | 员工客户端应用，启动类 `CustomerApplication`，提供客户端 API |
| **continew-system** | 系统管理模块（用户、角色、部门、菜单、字典等） |
| **continew-common** | 公共模块（工具类、公共配置、基础实体等），被两端共享 |
| **continew-plugin** | 插件模块（开放 API、多租户、定时任务、代码生成器） |
| **continew-extension** | 扩展模块（调度服务端） |

## API 规范

| 前缀 | 所属应用 | 说明 |
|------|---------|------|
| `/biz/*` | continew-server | 管理后台 API |
| `/api/*` | continew-customer | 员工客户端 API |

## 技术栈

| 类别 | 选型 |
|------|------|
| 核心框架 | Spring Boot 3.x + JDK 17 |
| 认证授权 | Sa-Token + JWT（jwt-simple 模式） |
| ORM | MyBatis Plus + CosId 分布式 ID 生成 |
| 缓存 | JetCache + Redis |
| 接口文档 | NextDoc4j，访问 `/doc.html` |
| 文件存储 | X File Storage，支持多种存储后端 |
| 任务调度 | Snail Job 分布式调度 |
| Excel 处理 | Fast Excel 导入导出 |
| 数据库迁移 | Liquibase |
| 消息通知 | 站内信通知 |

## 配置与环境

### 配置文件

- 管理后台：`continew-server/src/main/resources/config/application.yml`
- 员工客户端：`continew-customer/src/main/resources/config/application.yml`
- 环境配置：`application-dev.yml`（开发）、`application-prod.yml`（生产）

### 关键配置项

- `DB_HOST`、`DB_PORT`、`DB_NAME`、`DB_USER`、`DB_PWD`：数据库连接
- `REDIS_HOST`、`REDIS_PORT`、`REDIS_PWD`：Redis 连接

## 功能特性

### RBAC 权限体系
- **用户管理**：系统用户账号管理，支持多角色分配
- **角色管理**：基于角色的权限控制，支持数据权限隔离
- **部门管理**：树形组织结构管理
- **菜单管理**：动态菜单配置，支持按钮级别权限

### 其他内置功能
- **字典管理**：系统字典数据维护
- **通知公告**：系统通知和公告发布
- **文件管理**：上传文件统一管理
- **操作日志**：用户操作审计日志
- **在线用户**：当前在线用户会话管理
- **多租户**：支持租户级别数据隔离
- **代码生成器**：一键生成 CRUD 前后端代码

## 开发指南

### Controller 开发
- 继承 `BaseController` 获得自动 CRUD 能力
- 使用 `@CrudRequestMapping` 注解自动生成 CRUD 端点
- 管理端 API 使用 `/biz/*` 前缀，客户端 API 使用 `/api/*` 前缀
- 完整接口文档，访问 `/doc.html`

### Service 层
- 业务逻辑放在 ServiceImpl 中，Controller 不含业务逻辑
- 数据库操作使用 `@Transactional` 事务注解

### 数据库操作
- 简单 CRUD 使用 MyBatis Plus 基类方法
- 复杂多表查询使用 XML 映射文件实现
- 多表查询禁止使用 MyBatis Plus 注解，必须使用 XML

### 请求/响应规范
- `Req` 类作为请求参数，使用 Jakarta Validation 校验注解
- `Resp` 类作为响应参数（列表 Resp、详情 DetailResp）

### git提交
- 本项目有实现git 提交的代码检查最好让claude code 帮你实现git 提交

## 官方资源

- ContiNew Admin 项目地址：https://github.com/continew-org/continew-admin
- 文档中心：https://continew.top/docs/admin/

## 模板规范
- 如果用户没有描述具体实现 你要参考其他的模块进行系统的实现 保证代码的统一性