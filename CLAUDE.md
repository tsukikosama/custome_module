# CLAUDE.md

本文档为 Claude Code（claude.ai/code）在此仓库中工作时的指导说明。

## 项目概述

本项目基于 [ContiNew Admin](https://github.com/continew-org/continew-admin) 框架构建。ContiNew Admin（Continue New Admin）是一个持续迭代优化的前后端分离中后台管理系统框架，开箱即用，旨在提供舒适的开发体验。

项目采用多模块 Maven 结构，基于 Spring Boot 3 + Java 17，集成了 MyBatis Plus、Sa-Token、JetCache 等主流技术栈，提供了 RBAC 权限体系、多租户支持、代码生成器等开箱即用的功能。

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

# 启动应用（在 continew-server 模块下）
cd continew-server
mvn spring-boot:run
```

### 数据库管理
```bash
# 使用 Liquibase 进行数据库版本管理
# SQL 脚本位于：continew-server/src/main/resources/db/changelog/
# MySQL 脚本：continew-server/src/main/resources/db/changelog/mysql/
# PostgreSQL 脚本：continew-server/src/main/resources/db/changelog/postgresql/
```

## 架构与模块结构

### 模块组织
| 模块 | 说明 |
|------|------|
| **continew-server** | 主部署模块，启动类 `ContiNewAdminApplication`，包含通用 Controller 和配置 |
| **continew-system** | 系统管理模块（用户、角色、部门、菜单、字典等） |
| **continew-customer** | Client API 模块，对外提供 HTTP API 供第三方客户端/门户调用 |
| **continew-common** | 公共模块（工具类、公共配置、基础实体等） |
| **continew-plugin** | 插件模块（开放 API、多租户、定时任务、代码生成器） |
| **continew-extension** | 扩展模块（调度服务端） |

### 核心架构模式
- **CRUD 基类**：Controller 继承 `BaseController<S, T, DetailResp, Q, Req>` 实现自动 CRUD
- **分层架构**：Controller → Service → Mapper → 实体(DO) 标准分层
- **MyBatis Plus**：简单 CRUD 使用 MyBatis Plus，复杂多表查询使用 XML 映射文件
- **Lombok**：全局配置 `@EqualsAndHashCode(callSuper = true)` 和 `@ToString(callSuper = true)`

## 文件存放规范

### Java 源文件目录结构

每个业务模块（如 `continew-system`）内按功能包组织，包下按分层建子包：

```
top.continew.admin.{module}.{feature}/
├── api/              # 内部 API 实现（供其他模块 Feign 调用）
├── config/           # 配置类
├── constant/         # 常量类
├── container/        # 容器类（缓存初始化等）
├── controller/       # Controller 层
├── enums/            # 枚举类
├── handler/          # 策略/处理器类
├── model/
│   ├── entity/       # 数据库实体（DO）
│   ├── query/        # 分页查询参数
│   ├── req/          # 请求参数
│   └── resp/         # 响应参数
├── mapper/           # MyBatis Plus Mapper 接口
└── service/
    └── impl/         # Service 实现类
```

**各模块文件放置规则：**

| 文件类型 | 所属模块 | 说明 |
|---------|---------|------|
| `*Controller.java` | 功能模块 或 `continew-server` | API 入口，功能模块内的 Controller 放在对应模块包下，通用的放在 server 模块 |
| `*Service.java` / `*ServiceImpl.java` | 功能模块 | Service 接口与实现放在同一模块 |
| `*Mapper.java` / `*Mapper.xml` | 功能模块 或 `continew-common` | Mapper 及 XML 优先放在对应功能模块下；被多个模块复用的放在 `continew-common` |
| `*DO.java` / `*Req.java` / `*Resp.java` | 功能模块 | 与业务功能相关的模型类放在对应模块的 `model/` 下 |
| 基类/公共模型 | `continew-common` | 所有模块共享的基类（如 `BaseController`、`BaseDO`）放在 common 模块 |
| `*Configuration.java` | 功能模块 或 `continew-common` | 特定模块的配置放在自身模块内，全局配置放在 common 模块 |
| `*ApiImpl.java` | 功能模块 | API 实现类放在调用方模块，API 接口定义在 `continew-common` 的 `api/` 包下 |
| `application-*.yml` | `continew-server` | 应用配置文件统一放在 server 模块的 `config/` 目录下 |
| 前端静态资源 | `continew-server` | 模板文件（`.html`等）放在 server 模块的 `resources/templates/` 下 |

## 文件命名规范

### Java 文件命名

| 文件类型 | 后缀/前缀 | 示例 | 说明 |
|---------|----------|------|------|
| 数据库实体 | `XxxDO` | `RoleDO.java` | 对应数据库表，使用 `@TableName` 映射 |
| 请求参数 | `XxxReq` | `DictReq.java` | 接收前端请求参数，使用 Jakarta Validation 注解 |
| 分页查询参数 | `XxxQuery` | `OnlineUserQuery.java` | 分页列表查询参数 |
| 列表响应 | `XxxResp` | `DictResp.java` | 列表接口返回值 |
| 详情响应 | `XxxDetailResp` | `DictDetailResp.java` | 详情接口返回值 |
| Controller | `XxxController` | `DictController.java` | API 控制器，继承 `BaseController` |
| Service 接口 | `XxxService` | `DictService.java` | 业务接口，继承 `BaseService` |
| Service 实现 | `XxxServiceImpl` | `DictServiceImpl.java` | 业务实现，继承 `BaseServiceImpl` |
| Mapper 接口 | `XxxMapper` | `DictMapper.java` | MyBatis Plus Mapper，继承 `BaseMapper` |
| Mapper XML | `XxxMapper.xml` | `DictMapper.xml` | 复杂 SQL 映射文件，与 Mapper 接口同名 |
| 配置类 | `XxxConfiguration` | `SaTokenConfiguration.java` | Spring 配置类，使用 `@Configuration` |
| 常量类 | `XxxConstants` | `CacheConstants.java` | 常量定义 |
| 枚举类 | `XxxEnum` | `DataScopeEnum.java` | 枚举定义 |
| API 接口 | `XxxApi` | `DeptApi.java` | Feign 调用接口，放在 `continew-common` 的 `api/` 下 |
| API 实现 | `XxxApiImpl` | `DeptApiImpl.java` | Feign 接口实现 |


### SQL/脚本文件命名

| 文件类型 | 命名规则 | 示例 |
|---------|---------|------|
| 建表脚本 | `{业务}_table.sql` | `main_table.sql` |
| 初始化数据 | `{业务}_data.sql` | `main_data.sql` |
| 字典数据 | `{业务}_dict.sql` | `main_dict.sql` |
| 菜单数据 | `{业务}_menu.sql` | `main_menu.sql` |

### 命名总则
- **Java 类名**：采用大驼峰（PascalCase），按后缀区分层次
- **Java 包名**：全小写，按模块+功能组织
- **配置文件**：全小写，短横线分隔（kebab-case）
- **SQL 脚本**：全小写，下划线分隔（snake_case）
- **数据库表/字段**：全小写，下划线分隔（snake_case）

### 技术栈
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

## API 开发指南
### Controller 开发
- 继承 `BaseController` 获得自动 CRUD 能力
- 使用 `@CrudRequestMapping` 注解自动生成 CRUD 端点
- 管理端 API 使用 `/biz/*` 前缀，客户端 API 使用 `/api/*` 前缀
- 提供完整的接口文档，包含参数示例说明

### Service 层
- 业务逻辑放在 ServiceImpl 中，Controller 不含业务逻辑
- 数据库操作使用 `@Transactional` 事务注解
- 遵循已有的 Service 接口和实现模式

### 数据库操作
- 简单 CRUD 使用 MyBatis Plus 基类方法
- 复杂多表查询在 `src/main/resources/mapper/` 下创建 XML 映射文件实现
- 多表查询禁止使用 MyBatis Plus 注解实现，必须使用 XML
- 表名映射到 DO 类，遵循项目命名规范

### 请求/响应规范
- `Req` 类作为请求参数，使用 Jakarta Validation 校验注解
- `Resp` 类作为响应参数（列表 Resp、详情 DetailResp）
- 使用恰当的校验注解并文档化约束条件
- 如果要给前端返回错误提示 需要使用CheckUtils里面的方法
- 分页的参数是page 和 size 
## 代码质量与规范

### 代码格式化
- **自动格式化**：Maven 编译时通过 Spotless 插件自动格式化代码，无需手动调整
- **风格指南**：遵循阿里巴巴 Java 开发手册（黄山版）
- **Lombok 配置**：`lombok.config` 中全局配置，禁用了部分存在风险的注解
- **注释覆盖率**：项目保持 >45% 的注释覆盖率

## 重要注意事项

- **不修改已有接口的返回结构**——保持兼容性
- **不修改与当前任务无关的功能**——最小化变更
- **优先复用已有 Service**——先检查是否有相似功能再新建
- **多表 SQL 必须在 XML 映射文件中实现**——禁止在 MyBatis Plus 注解中写复杂 SQL
- **Controller 不包含业务逻辑**——委托给 Service 层处理
- **始终使用 Lombok 注解**——遵循项目已有模式
- **测试黄金路径和边界情况**——确保功能端到端可用
- **实现完功能之后不需要去**——编译测试
- **实现功能需要把对应功能写入api接口.md功能文档中 文档的模板在api接口.md里面**--项目文档
- ** 如果执行的代码是批量的操作，你要使用批量的操作，而不是便利然后一次一次执行。
