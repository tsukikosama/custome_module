# 活动详情接口添加参与用户ID功能设计

**日期**: 2026-05-08
**作者**: Claude
**状态**: 已批准

## 概述

在活动详情API中添加主动参加人的用户ID列表字段，返回逗号分隔的字符串。此功能用于补充现有的姓名列表字段，方便前端获取用户ID进行后续操作。

## 需求

### 功能需求
- 在 `ApiActivityDetailResp` 中添加 `normalUserIds` 字段
- 返回所有主动参加人（type=3）且状态为已报名（status=1）的用户ID
- ID列表使用逗号分隔（如："101,102,103"）

### 非功能需求
- 遵循项目现有代码规范和命名约定
- 不影响现有接口返回结构（向后兼容）
- 仅查询未删除的记录（deleted=0）

## 设计

### 数据模型变更

**文件**: `continew-hr-common/src/main/java/top/continew/admin/hrcommon/model/resp/ApiActivityDetailResp.java`

添加字段：
```java
/**
 * 主动参加人用户ID列表（逗号分隔）
 */
@Schema(description = "主动参加人用户ID列表（逗号分隔）", example = "101,102,103")
private String normalUserIds;
```

### 数据库查询变更

**文件**: `continew-hr-common/src/main/resources/mapper/ActivityMapper.xml`

在 `getActivityMemberInfo` 查询中（第87行之后），添加子查询：

```sql
<!-- 主动参加人用户ID列表（逗号分隔） -->
(SELECT GROUP_CONCAT(am.user_id)
 FROM biz_activity_member am
 WHERE am.activity_id = #{activityId}
   AND am.type = 3
   AND am.status = 1
   AND am.deleted = 0) AS normalUserIds
```

### 查询条件说明

| 条件 | 值 | 说明 |
|------|-----|------|
| `type` | `3` | 仅主动参加人（不包括举办人type=1、必参加人type=2） |
| `status` | `1` | 仅已报名状态（不包括已取消status=2） |
| `deleted` | `0` | 仅未删除的记录 |

### 返回数据示例

```json
{
  "id": 1,
  "title": "技术分享会：Spring Cloud微服务架构实践",
  "image": "https://example.com/images/activity1.jpg",
  "content": "本次分享会将介绍Spring Cloud微服务架构的最佳实践",
  "startTime": "2026-05-15 14:00:00",
  "endTime": "2026-05-15 17:00:00",
  "createTime": "2026-04-29 10:00:00",
  "createUser": "张三",
  "type": 2,
  "eventPeopleNums": 50,
  "attachment": "https://example.com/files/presentation.pdf",
  "speakerName": "李四",
  "requireUser": "王五,赵六",
  "normalUser": "孙七,周八,吴九",
  "normalUserIds": "101,102,103",
  "enrolledCount": 3
}
```

## 实施计划

### 步骤1：修改响应类
- 文件：`ApiActivityDetailResp.java`
- 操作：添加 `normalUserIds` 字段及注解

### 步骤2：修改SQL查询
- 文件：`ActivityMapper.xml`
- 操作：在 `getActivityMemberInfo` 查询中添加子查询

### 步骤3：验证
- 测试只有 type=3 且 status=1 的用户ID被返回
- 测试ID列表格式正确（逗号分隔）
- 测试空数据情况（无符合条件的用户）

## 技术考虑

### GROUP_CONCAT 行为
- MySQL 的 `GROUP_CONCAT` 默认最大长度为 1024 字节
- 如果用户ID列表很长，可能需要考虑分页或使用其他方案
- 当前活动人数限制（eventPeopleNums）默认不会太大，GROUP_CONCAT 足够使用

### 性能影响
- 添加的子查询使用索引（activity_id, type, status, deleted）
- 与现有 `normalUser` 查询逻辑相同，性能影响可忽略

### 向后兼容
- 新增字段不影响现有字段
- 前端可以选择性使用新字段
- 老版本API调用不受影响

## 相关文档

- 活动管理API文档：`员工内部系统开发文档.md`
- 数据库表结构：`biz_activity_member`
- 相关枚举：`ActivityMemberType`（1:演讲人, 2:必参加人, 3:主动参加）
