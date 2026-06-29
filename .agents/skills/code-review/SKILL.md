---
name: code-review
description: 代码审查 skill — 用于检查提交的代码是否符合项目规范、是否存在常见问题
---

# 代码审查规范

本 skill 定义了提交代码时需要自动检查的项目。当提交代码时，将自动执行以下检查规则。

## 1. License Header 检查

所有 Java 文件必须具备 Apache 2.0 License header：

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

## 2. 文件命名检查

| 文件类型 | 预期后缀 | 检查规则 |
|---------|---------|---------|
| 实体类 | `*DO.java` | 必须以 `DO` 结尾，继承 `BaseDO` |
| 请求参数 | `*Req.java` | 必须以 `Req` 结尾，实现 `Serializable` |
| 列表响应 | `*Resp.java` | 必须以 `Resp` 结尾，继承 `BaseDetailResp` |
| 查询参数 | `*Query.java` | 必须以 `Query` 结尾，实现 `Serializable` |
| Controller | `*Controller.java` | 必须以 `Controller` 结尾，继承 `BaseController` |
| Service | `*Service.java` | Service 接口继承 `BaseService` 和 `IService` |
| Service 实现 | `*ServiceImpl.java` | 必须以 `ServiceImpl` 结尾 |
| Mapper | `*Mapper.java` | 必须以 `Mapper` 结尾，继承 `BaseMapper` |
| 配置类 | `*Configuration.java` | 必须以 `Configuration` 结尾，标注 `@Configuration` |

## 3. 代码规范检查

### 3.1 序列化
- 实现 `Serializable` 的类必须声明 `@Serial private static final long serialVersionUID = 1L;`

### 3.2 包路径
- 业务代码必须在 `top.continew.admin.{module}` 包下
- 禁止将 Controller 放在非 controller 子包中
- 禁止将 DO 放在非 entity 子包中

### 3.3 注解使用
- DO 类必须使用 `@Data`（lombok）、`@TableName`
- Controller 必须使用 `@RestController`、`@CrudRequestMapping` 或 `@RequestMapping`
- Service 实现类必须使用 `@Service`、`@RequiredArgsConstructor`

### 3.4 API 兼容性
- 禁止修改已有接口的返回结构
- 禁止删除已有接口参数
- 新增字段必须添加 `@Schema(description = "...")`

## 4. 常见问题检查

### 4.1 危险操作
- 禁止 `System.out.println`（应使用日志）
- 禁止 `e.printStackTrace()`
- 禁止硬编码密码/密钥（应使用配置文件）
- 禁止 `@GetMapping`/`@PostMapping` 等简写注解替代 `@RequestMapping`（统一风格用 `@CrudRequestMapping`）

### 4.2 数据库操作
- 多表查询必须使用 XML 映射文件，禁止在 MyBatis Plus 注解中写复杂 SQL
- 事务必须使用 `@Transactional(rollbackFor = Exception.class)`（放在 ServiceImpl 方法上）

### 4.3 依赖注入
- 使用 `@RequiredArgsConstructor` + `private final` 构造器注入，禁止 `@Autowired` 字段注入

### 4.4 校验
- 业务校验使用 `CheckUtils.throwIf(condition, "消息")` 或 `CheckUtils.throwIfNotEqual(a, b, "消息")`
- 参数校验使用 Jakarta Validation 注解（`@NotBlank`、`@NotNull`、`@Length`、`@Pattern`）

## 5. 格式化检查

- 项目使用 Spotless 插件自动格式化（Maven 编译时自动执行）
- 提交前建议运行 `mvn spotless:check` 检查格式
- 格式问题可通过 `mvn spotless:apply` 自动修复

## 6. 检查流程

提交时自动执行的检查流程：

```
1. Spotless 格式检查  →  失败则提示运行 mvn spotless:apply
2. License Header 检查  →  缺失则提示添加
3. 文件命名检查  →  不符合规范则提示
4. 代码规范检查  →  发现违规则提示
5. 危险操作检查  →  发现则阻止提交
```
