---
name: docker-build
description: Docker 镜像构建 skill — 用于指导 Docker 打包、构建镜像、docker-compose 部署
---

# Docker 镜像构建指南

本 skill 描述了本项目 Docker 镜像构建的完整流程和规范。

## 1. 项目版本

当前版本号定义在根 `pom.xml` 的 `properties` 中：

```xml
<revision>4.2.0-SNAPSHOT</revision>
```

## 2. 构建模式

项目支持两种 JAR 打包模式：

| 模式 | profile | 说明 |
|------|---------|------|
| **胖包 (fat-jar)** | `-P fat-jar` | 单 JAR 包含所有依赖和配置，独立可执行 |
| **瘦包 (thin-jar)** | 默认 (thin-jar) | JAR、依赖(`lib/`)、配置(`config/`)分离 |

### 瘦包输出结构

```
continew-server/target/app/
├── bin/                  # 应用 JAR
│   └── continew-admin.jar
├── config/               # 配置文件
│   └── application-prod.yml
├── lib/                  # 依赖 JAR
│   └── *.jar
└── db/                   # 数据库脚本
```

## 3. Dockerfile

### continew-server

位置：`docker/continew-admin/Dockerfile`

```dockerfile
FROM bellsoft/liberica-openjdk-debian:17.0.14
ARG JAR_FILE=./bin/*.jar
COPY ${JAR_FILE} /app/bin/app.jar
WORKDIR /app/bin
ENTRYPOINT ["java", "-jar", "-XX:+UseZGC", "-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=prod", "app.jar"]
```

- 基础镜像：bellsoft/liberica-openjdk-debian:17.0.14（全量 JDK，兼容性好）
- 如需更小体积，可换为 bellsoft/liberica-openjdk-alpine:17.0.14
- JVM 参数：UseZGC（低延迟垃圾回收器）
- 激活 profile：prod

## 4. Docker Compose

位置：`docker/docker-compose.yml`

### 服务列表

| 服务名 | 镜像 | 端口 | 说明 |
|--------|------|------|------|
| mysql | mysql:8.0.42 | 3306 | 数据库 |
| redis | redis:7.2.8 | 6379 | 缓存 |
| **continew-server** | continew-server:4.2.0-SNAPSHOT | 18000, 17889 | 后端服务 |
| continew-web | nginx:1.27.0 | 80, 443 | 前端 |
| schedule-server | schedule-server:1.8.0 | 18001, 17888 | 调度服务端 |

### continew-server 环境变量

| 变量 | 说明 |
|------|------|
| DB_HOST / DB_PORT / DB_USER / DB_PWD / DB_NAME | 数据库连接 |
| REDIS_HOST / REDIS_PORT / REDIS_PWD / REDIS_DB | Redis 连接 |
| SCHEDULE_HOST / SCHEDULE_PORT / SCHEDULE_TOKEN | 调度服务连接 |

### 数据卷挂载

```
/docker/continew-admin/config/   → /app/config/        # 外部配置覆盖
/docker/continew-admin/data/file/ → /app/data/file/    # 上传文件
/docker/continew-admin/logs/     → /app/logs/          # 日志
/docker/continew-admin/lib/      → /app/lib/           # 外部依赖
```

## 5. 部署流程

### 5.1 本地构建 Docker 镜像

#### 方式一：瘦包 + Docker（推荐，与 CI/CD 一致）

```bash
# 第 1 步：Maven 打包（瘦包模式，默认）
cd continew-server
mvn clean package -DskipTests

# 第 2 步：准备 Docker build context
# 将 target/app 目录复制到 docker/continew-admin/ 下的 bin/ 目录
# 或者修改 Dockerfile 的 COPY 路径指向 target/app

# 方式 A：直接使用现有 Dockerfile
cp -r continew-server/target/app docker/continew-admin/bin
cd docker
docker compose build continew-server

# 方式 B：手动构建
cd continew-server/target
docker build -f ../../docker/continew-admin/Dockerfile -t continew-server:4.2.0-SNAPSHOT .
```

#### 方式二：胖包模式

```bash
# 第 1 步：Maven 打包（胖包模式）
mvn clean package -P fat-jar -DskipTests

# 第 2 步：构建镜像
# 需要临时修改 Dockerfile 中的 ARG JAR_FILE 路径或单独编写 Dockerfile
# 胖包模式输出：continew-server/target/continew-admin.jar
```

### 5.2 CI/CD 自动部署

由 `.github/workflows/deploy.yml` 实现：

1. GitHub Actions 检出代码 → Maven 打包 → SCP 拷贝到服务器 → docker-compose 重启

```bash
# 服务器上实际执行的命令：
cd <deploy-path>
docker compose up --force-recreate --build -d continew-server
docker images | grep none | awk '{print $3}' | xargs -r docker rmi
```

### 5.3 完整本地部署步骤

```bash
# 1. 编译打包
mvn clean package -DskipTests

# 2. 构建并启动所有服务
cd docker
docker compose up -d

# 3. 仅构建并重启 continew-server
docker compose up --build -d continew-server

# 4. 查看日志
docker compose logs -f continew-server

# 5. 清理 dangling 镜像
docker images | grep none | awk '{print $3}' | xargs -r docker rmi

# 6. 停止并清理
docker compose down
```

## 6. 多模块扩展

如果要为其他模块（如 `continew-customer`）新增 Docker 构建：

### 添加 Dockerfile

```
docker/customer/Dockerfile
```

### 添加到 docker-compose.yml

```yaml
continew-customer:
  build: ./customer
  image: continew-customer:4.2.0-SNAPSHOT
  container_name: continew-customer
  restart: always
  ports:
    - '18002:18002'
  volumes:
    - /docker/continew-customer/config/:/app/config/
    - /docker/continew-customer/logs/:/app/logs/
  environment:
    TZ: Asia/Shanghai
    DB_HOST: 172.17.0.1
    # ...
  depends_on:
    - redis
    - mysql
```

## 7. 注意事项

- **镜像标签**：与项目版本保持一致（当前 `4.2.0-SNAPSHOT`）
- **基础镜像选择**：debian 版兼容性好，alpine 版体积小（约 128MB vs 全量 JDK）
- **JVM 参数**：使用 ZGC 垃圾回收器，适合低延迟场景
- **外部配置覆盖**：通过 volume 挂载 `config/` 目录可覆盖容器内配置
- **thin-jar 注意事项**：Dockerfile 期望 JAR 在 `bin/` 子目录下，构建 context 需包含 `bin/`、`config/`、`lib/` 等目录
- **不要在 Dockerfile 中硬编码环境变量**——通过 docker-compose 的 `environment` 或 `.env` 文件注入
