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

### 2. Docker 镜像构建

| 项目 | 内容 |
|------|------|
| **文件名** | `docker-build.md` |
| **触发方式** | `/docker-build`、"打包 Docker"、"构建镜像"、"docker 部署" |
| **适用范围** | Docker 镜像构建、docker-compose 部署、CI/CD 流程 |
| **覆盖内容** | 胖包/瘦包构建模式、Dockerfile 说明、docker-compose 服务配置、本地与 CI/CD 部署流程、多模块扩展指南 |

### 3. 代码审查

| 项目 | 内容 |
|------|------|
| **文件名** | `code-review.md` |
| **触发方式** | `/code-review`、"检查代码"、"代码审查"、"审查提交" |
| **适用范围** | 提交代码前的自动检查 |
| **覆盖内容** | License Header、文件命名规范、代码规范（序列化/包路径/注解/API兼容性）、危险操作检查、格式化检查 |

**自动触发：** 该 skill 同时配置了 git pre-commit hook（`.git/hooks/pre-commit`），在每次 `git commit` 时自动执行以下检查：
1. License Header 完整性检查
2. 危险代码模式检查（`System.out.println`、`e.printStackTrace()`、`@Autowired` 等）
3. 新增文件命名规范检查
4. Spotless 格式检查（需安装 Maven）

检查不通过时会阻止提交，并给出修复提示。也可通过 `git commit --no-verify` 临时跳过。

---

## 如何新增 Skill

### 文件位置
所有自定义 Skill 存放在 `.claude/skills/` 目录下，每个 Skill 一个独立的子目录。

### 文件结构
```
.claude/skills/
├── <skill-name>/
│   └── SKILL.md    # Skill 定义文件（必需）
└── README.md
```

### SKILL.md 文件格式
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
Skill 文件放入 `.claude/skills/<name>/SKILL.md` 后，Claude Code 会自动发现并注册，无需额外配置。

---

> 最后更新：2026-06-08
