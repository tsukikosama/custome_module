---
name: coding-standards
description: 项目编码规范 skill — 用于指导 Codex 在生成/修改代码时遵循本项目约定
---

# 项目编码规范

本 skill 定义了项目的编码规范和约定。当用户要求你"遵循编码规范"、"按项目规范"或启动 `/coding-standards` 时，请严格遵循以下规则生成或修改代码。

## 1. Java 文件命名与后缀

| 类型 | 后缀/前缀 | 示例 |
|------|----------|------|
| 数据库实体 | `XxxDO` | `DictDO.java`，继承 `BaseDO`，使用 `@TableName("sys_xxx")` |
| 请求参数 | `XxxReq` | `DictReq.java`，实现 `Serializable`，使用 Jakarta Validation 注解 |
| 分页查询参数 | `XxxQuery` | `DictQuery.java`，实现 `Serializable`，使用 `@Query` 注解 |
| 列表响应 | `XxxResp` | `DictResp.java`，继承 `BaseDetailResp` |
| 详情响应 | `XxxDetailResp` | `DictDetailResp.java`（如需要独立详情） |
| Controller | `XxxController` | `DictController.java`，继承 `BaseController` |
| Service 接口 | `XxxService` | `DictService.java`，继承 `BaseService` 和 `IService<DO>` |
| Service 实现 | `XxxServiceImpl` | `DictServiceImpl.java`，继承 `BaseServiceImpl`，实现接口 |
| Mapper 接口 | `XxxMapper` | `DictMapper.java`，继承 `BaseMapper<DO>`，标注 `@Mapper` |
| Mapper XML | `XxxMapper.xml` | 复杂 SQL 用 XML 实现，禁止在注解中写复杂多表 SQL |
| 配置类 | `XxxConfiguration` | `SaTokenConfiguration.java`，标注 `@Configuration` |
| 常量类 | `XxxConstants` | `CacheConstants.java`，`RegexConstants.java` |
| 枚举类 | `XxxEnum` | `DataScopeEnum.java` |
| 处理器 | `XxxHandler` | `AccountLoginHandler.java`，策略模式 |
| 工厂类 | `XxxFactory` | `LoginHandlerFactory.java` |
| 事件类 | `XxxEvent` | `SendMessageEvent.java` |
| API 接口 | `XxxApi` | 定义在 `continew-common` 的 `api/` 包下 |
| API 实现 | `XxxApiImpl` | 在调用方模块中实现 |

## 2. 包组织结构

```
top.continew.admin.{module}.{feature}/
├── api/              # 内部 API 实现（Feign 调用）
├── config/           # 配置类
├── constant/         # 常量类
├── container/        # 容器类（缓存初始化等）
├── controller/       # Controller 层
├── enums/            # 枚举类
├── handler/          # 策略/处理器类
├── mapper/           # MyBatis Plus Mapper 接口
├── model/
│   ├── entity/       # 数据库实体（DO）
│   ├── query/        # 分页查询参数
│   ├── req/          # 请求参数
│   └── resp/         # 响应参数
└── service/
    └── impl/         # Service 实现类
```

关键规则：
- **功能模块之间的 `model/` 下子包路径不同**：`entity/`、`query/`、`req/`、`resp/` 各司其职
- **通用 Controller** 放在 `continew-server` 模块下
- **被多个模块复用的 Mapper/模型** 放在 `continew-common` 模块
- **API 接口定义** 放在 `continew-common` 的 `api/` 包下，**实现**放在调用方模块

## 3. 类继承与泛型约束

### Controller
```java
// REST API 用 @RestController，@CrudRequestMapping 自动生成 CRUD 端点
@Tag(name = "XXX管理 API")
@RestController
@CrudRequestMapping(value = "/system/xxx", api = {Api.LIST, Api.GET, Api.CREATE, Api.UPDATE, Api.BATCH_DELETE})
public class XxxController extends BaseController<XxxService, XxxResp, XxxResp, XxxQuery, XxxReq> {
    // 自定义接口...
}
```

### Service 接口
```java
public interface XxxService extends BaseService<XxxResp, XxxResp, XxxQuery, XxxReq>, IService<XxxDO> {
    // 自定义方法...
}
```

### Service 实现
```java
@Service
@RequiredArgsConstructor
public class XxxServiceImpl extends BaseServiceImpl<XxxMapper, XxxDO, XxxResp, XxxResp, XxxQuery, XxxReq> implements XxxService {
    // 实现方法...
}
```

### Mapper
```java
@Mapper
public interface XxxMapper extends BaseMapper<XxxDO> {
}
```

### 实体 (DO)
```java
@Data
@TableName("sys_xxx")
public class XxxDO extends BaseDO {
    @Serial
    private static final long serialVersionUID = 1L;
    // 字段...
}
```

### 请求参数 (Req)
```java
@Data
@Schema(description = "XXX创建或修改请求参数")
public class XxxReq implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // 字段...
}
```

### 响应参数 (Resp)
```java
@Data
@Schema(description = "XXX响应参数")
public class XxxResp extends BaseDetailResp {
    @Serial
    private static final long serialVersionUID = 1L;
    // 字段...
}
```

### 查询参数 (Query)
```java
@Data
@Schema(description = "XXX查询条件")
public class XxxQuery implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // 字段...
}
```

## 4. 代码风格约定

### 通用规则
- **使用 lombok `@Data`** 注解替代手写 getter/setter/toString/equals/hashCode
- **始终声明 `@Serial private static final long serialVersionUID = 1L;`**（实现 `Serializable` 的类）
- **使用 `@Schema(description = "...", example = "...")`** 标注请求/响应字段的描述和示例值
- **使用 Jakarta Validation 注解**：`@NotBlank`、`@NotNull`、`@Length`、`@Pattern` 等
- **字段级别的 Javadoc 注释**：用 `/** 字段说明 */`（单行描述）
- 布尔类型字段使用 `is` 前缀命名，如 `isSystem`

### 日志
- Controller 方法：`@Operation(summary = "XXX", description = "XXX")`
- ServiceImpl 中逻辑：使用 `log.warn("...")`（继承自基类的 log 对象）或 `log.error(...)`
- 不要在每个方法上加无意义的日志，只在关键分支或异常处记录

### 校验
- 校验信息使用 `message = "XX 长度不能超过 {max} 个字符"` 格式
- 名称字段校验：`@Pattern(regexp = RegexConstants.GENERAL_NAME, message = "名称长度为 2-30 个字符，支持中文、字母、数字、下划线，短横线")`
- 编码字段校验：`@Pattern(regexp = RegexConstants.GENERAL_CODE, message = "编码长度为 2-30 个字符，支持大小写字母、数字、下划线，以字母开头")`
- 自定义业务校验：使用 `CheckUtils.throwIf(condition, "消息模板 {0}", arg)` 或 `CheckUtils.throwIfNotEqual(a, b, "消息")`

### 事务
- 数据库写操作使用 `@Transactional(rollbackFor = Exception.class)`
- 放在 ServiceImpl 的方法上，不放在 Controller

### 权限注解
- 自定义接口（非 `@CrudRequestMapping` 生成的）使用 `@SaCheckPermission("system:xxx:action")`
- CRUD 权限由 `BaseController.preHandle` 自动处理，不需手动加

## 5. API 设计规范

- **管理端 API 路径前缀**：`/biz/*`（使用 `@CrudRequestMapping` 时自动处理）
- **客户端 API 路径前缀**：`/api/*`
- **API 命名**：RESTful 风格，使用复数名词
- **不要修改已有接口的返回结构**——保持向前兼容
- **不要修改与当前任务无关的功能**——最小化变更
- 响应不包装泛型结果，框架统一处理

## 6. 数据库规范

- 表名前缀：`sys_`（系统模块）、`xx_`（业务模块）等
- 字段名：全小写 + 下划线分隔（snake_case）
- 主键名：`id`
- 逻辑删除字段：`deleted`（`Long` 类型，`0` 为未删除，`id` 值为已删除）
- 通用字段由 `BaseDO` 提供：`id`、`create_user`、`create_time`、`update_user`、`update_time`、`deleted`
- 复杂多表查询使用 MyBatis Plus XML 映射文件实现，禁止在注解中写复杂 SQL
- 数据库迁移使用 Liquibase，脚本放在 `continew-server/src/main/resources/db/changelog/` 下

## 7. 文件头部 License

所有 Java 文件必须有 Apache 2.0 License header：

```java
/*
 * Copyright (c) 2022-present Charles7c Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
```

## 8. Javadoc 规范

- 类注释格式：
```java
/**
 * XXX 业务实现
 *
 * @author <author>
 * @since <yyyy/MM/dd HH:mm>
 */
```

- 方法注释：只在需要说明业务逻辑"为什么"时添加，不要为简单 getter 写注释
- 字段注释：`/** 字段说明 */` 单行（仅对 DO 类和业务上有歧义的字段）
- 常量类私有构造方法：`private XxxConstants() {}`

## 9. 分层职责

| 层 | 职责 | 禁止 |
|----|------|------|
| Controller | 参数接收、路由、权限注解 | 不包含业务逻辑 |
| Service | 业务逻辑编排、事务管理 | 不直接处理 HTTP 请求/响应 |
| ServiceImpl.beforeCreate/Update/Delete | CRUD 前置校验 | — |
| Mapper | 数据库操作 | — |

## 10. 代码生成器使用

系统的 `continew-plugin` 模块提供代码生成器，可以：
- 生成约 80-95% 的 CRUD 代码（包括前后端）
- 自动生成接口文档和参数校验
- 自动遵循项目命名规范
- 生成的部分代码需要手动微调业务逻辑

---

## 启动方式

用户可通过以下方式使用本 skill：

- `/coding-standards` — 加载编码规范
- "遵循编码规范" — 自动加载
- "按项目规范编写代码" — 自动加载
