# 项目自定义 Skills

本文档列出本项目中所有自定义 Claude Code Skills。每个 Skill 通过 `/skill-name` 或自然语言触发。

---

## 当前 Skills

### 1. 项目编码规范

| 项目 | 内容 |
|------|------|
| **文件名** | `coding-standards.md` |
| **触发方式** | `/coding-standards`、"遵循编码规范"、"按项目规范编写代码" |
| **适用范围** | 所有 Java 代码生成、修改、审查 |
| **覆盖内容** | 文件命名、包组织结构、类继承体系、代码风格、API 设计、数据库规范、Javadoc、分层职责等 10 个方面 |

---

## 如何新增 Skill

### 文件位置
所有自定义 Skill 存放在 `.claude/skills/` 目录下，每个 Skill 一个独立的 `.md` 文件。

### 文件格式
```markdown
---
name: skill-name
description: 简短描述，用于 Claude 识别
---

# Skill 名称

技能内容...
```

关键点：
- `name` 字段对应 `/skill-name` 触发命令（如 `name: coding-standards` 则通过 `/coding-standards` 触发）
- `description` 是 Claude 判断何时自动触发该 skill 的依据，写清楚适用范围
- 文件内容可使用 Markdown 格式，Claude 会自动读取并遵循

### 自动发现
Skill 文件放入 `.claude/skills/` 后，Claude Code 会自动发现并注册，无需额外配置。

---

> 最后更新：2026-06-08
