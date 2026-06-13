### Docker 部署文档

#### 服务架构

项目包含以下三个 Java 服务，均以 Docker 容器方式部署：

| 服务名 | 说明 | 端口 | Dockerfile 位置 |
|--------|------|------|----------------|
| `continew-server` | 管理后台 API | 18000（HTTP）/ 17889（Snail Job 客户端） | `docker/continew-admin/Dockerfile` |
| `continew-customer` | 客户端/门户 API | 18002 | `docker/continew-customer/Dockerfile` |
| `schedule-server` | Snail Job 调度中心 | 18001（HTTP）/ 17888（调度通讯） | `docker/schedule-server/Dockerfile` |

依赖的基础设施：

| 服务 | 镜像 | 端口映射 |
|------|------|---------|
| MySQL | mysql:8.0.42 | 宿主机 3307 → 容器 3306 |
| Redis | redis:7.2.8 | 6379 |

---

#### 构建与部署步骤

**1. 打包 JAR**

在项目根目录执行：

```bash
mvn clean package -DskipTests
```

打包产物：

- `continew-server/target/continew-admin.jar`
- `continew-customer/target/continew-customer.jar`
- `continew-extension/continew-extension-schedule-server/target/continew-extension-schedule-server.jar`

**2. 构建 Docker 镜像**

```bash
# 构建所有服务镜像
docker compose -f docker/docker-compose.yml build

# 或构建单个服务
docker compose -f docker/docker-compose.yml build continew-server
docker compose -f docker/docker-compose.yml build continew-customer
docker compose -f docker/docker-compose.yml build schedule-server
```

**3. 启动全部服务**

```bash
# 启动所有服务（含 MySQL、Redis）
docker compose -f docker/docker-compose.yml up -d

# 启动特定服务（依赖的基础设施需提前启动）
docker compose -f docker/docker-compose.yml up -d continew-server
```

**4. 查看日志**

```bash
docker compose -f docker/docker-compose.yml logs -f continew-server
```

**5. 停止服务**

```bash
docker compose -f docker/docker-compose.yml down
```

---

#### 配置文件说明

Docker 部署时使用 Spring 的 `prod` 配置文件（通过 Dockerfile 中的 `-Dspring.profiles.active=prod` 指定）。

**prod 配置文件路径：**

| 服务 | 配置文件 |
|------|---------|
| continew-server | `continew-server/src/main/resources/config/application-prod.yml` |
| continew-customer | `continew-customer/src/main/resources/config/application-prod.yml` |
| schedule-server | `continew-extension/continew-extension-schedule-server/src/main/resources/config/application-prod.yml` |

---

#### 环境变量配置

各服务通过环境变量注入配置，在 `docker-compose.yml` 的 `environment` 中设置：

##### 通用环境变量

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| `TZ` | 时区 | `Asia/Shanghai` |
| `DB_HOST` | MySQL 地址 | `mysql`（容器名） |
| `DB_PORT` | MySQL 端口 | `3306` |
| `DB_USER` | MySQL 用户名 | `root` |
| `DB_PWD` | MySQL 密码 | **必须修改** |
| `DB_NAME` | MySQL 数据库名 | `hr2`（continew-server/customer）/ `employee_portal_job`（schedule-server） |
| `REDIS_HOST` | Redis 地址 | `redis`（容器名） |
| `REDIS_PORT` | Redis 端口 | `6379` |
| `REDIS_PWD` | Redis 密码 | 空字符串 |
| `REDIS_DB` | Redis 数据库索引 | `2` |

##### Snail Job 调度中心环境变量（仅 schedule-server）

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| `SCHEDULE_HOST` | 调度中心地址（供客户端访问） | `172.17.0.1`（Docker 宿主机网关） |
| `SCHEDULE_PORT` | 调度中心通讯端口 | `1788` |
| `SCHEDULE_TOKEN` | 调度中心接入 Token | **必须修改** |

##### Snail Job 客户端环境变量（continew-server / continew-customer）

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| `SCHEDULE_HOST` | 调度中心地址 | `172.17.0.1`（Docker 宿主机网关） |
| `SCHEDULE_PORT` | 调度中心通讯端口 | `1788` |
| `SCHEDULE_TOKEN` | 调度中心接入 Token | **必须与 schedule-server 保持一致** |

> **注意：** `SCHEDULE_HOST` 默认值为 `172.17.0.1`（Docker 宿主机默认网关地址）。当 schedule-server 与客户端不在同一 Docker 网络时，需确保客户端能访问到 schedule-server 容器，例如将客户端 `SCHEDULE_HOST` 改为宿主机 IP 或使用 `network_mode: host`。

---

#### 生产环境部署检查清单

| # | 检查项 | 说明 |
|---|--------|------|
| 1 | **修改 MySQL 密码** | 修改 `docker-compose.yml` 中 `MYSQL_ROOT_PASSWORD` 及所有 `DB_PWD` 环境变量 |
| 2 | **修改 Redis 密码** | 如启用 Redis 密码，需同步修改 `REDIS_PWD` 和各服务的 `application-prod.yml` 中 Redis 密码默认值 |
| 3 | **修改 Snail Job Token** | 修改 `SCHEDULE_TOKEN` 环境变量，各服务间须保持一致 |
| 4 | **修改 JWT 秘钥** | `application.yml` 中 `sa-token.jwt-secret-key` 需更换为随机字符串 |
| 5 | **修改字段加密密钥** | `application-prod.yml` 中 `continew-starter.encrypt.field.password` 需更换 |
| 6 | **配置应用 URL** | 修改 `application-prod.yml` 中 `application.url`、`portal-url`、`client-url` 为实际域名 |
| 7 | **配置 JustAuth 回调域名** | `application-prod.yml` 中各 OAuth 回调 `redirect-uri` 需与实际域名一致 |
| 8 | **配置跨域域名** | `application-prod.yml` 中 `allowed-origins` 需包含实际前端域名 |
| 9 | **配置 WebSocket 跨域** | `application-prod.yml` 中 WebSocket 的 `allowed-origins` 与实际前端域名一致 |
| 10 | **调整文件存储路径** | `application-prod.yml`（customer 模块）中 `file.upload.path` 改为容器内可挂载路径，建议使用 Docker 数据卷 |
| 11 | **数据库初始化** | 确保 MySQL 中已创建各服务所需的数据库，或启用 Liquibase 自动建表 |
| 12 | **检查宿主机目录挂载** | `docker-compose.yml` 中 `volumes` 挂载的宿主机目录（如 `/docker/` 下各目录）需预先创建 |
| 13 | **调整调度中心地址** | 如果 schedule-server 与客户端网络不通，需调整 `SCHEDULE_HOST` 为宿主机 IP 或放入同一 Docker 网络 |

---

#### 数据卷挂载说明

`docker-compose.yml` 中配置的宿主机目录挂载：

| 容器 | 宿主机路径 | 容器路径 | 说明 |
|------|-----------|----------|------|
| mysql | `/docker/mysql/conf/` | `/etc/mysql/conf.d/` | MySQL 自定义配置 |
| mysql | `/docker/mysql/data/` | `/var/lib/mysql/` | MySQL 数据持久化 |
| continew-server | `/docker/continew-admin/config/` | `/app/config/` | 外部配置文件（覆盖 jar 内配置） |
| continew-server | `/docker/continew-admin/data/file/` | `/app/data/file/` | 上传文件存储 |
| continew-server | `/docker/continew-admin/logs/` | `/app/logs/` | 应用日志 |
| continew-server | `/docker/continew-admin/lib/` | `/app/lib/` | 外部依赖库 |
| continew-customer | `/docker/continew-customer/config/` | `/app/config/` | 外部配置文件 |
| continew-customer | `/docker/continew-customer/data/file/` | `/app/data/file/` | 上传文件存储 |
| continew-customer | `/docker/continew-customer/logs/` | `/app/logs/` | 应用日志 |
| schedule-server | `/docker/schedule-server/logs/` | `/app/logs/` | 调度中心日志 |

> **注意：** 宿主机挂载目录需预先创建（`mkdir -p /docker/...`），否则 Docker 会以 root 权限自动创建，可能导致权限问题。

---

#### 需要修改的配置项（详细说明）

##### 1. 数据库密码

`docker-compose.yml` 中 MySQL 密码和所有服务的 `DB_PWD` 环境变量使用相同的值，同时 `application-prod.yml` 中数据库密码的默认值（Spring 占位符的 fallback）也需同步修改。

```yaml
# docker-compose.yml
environment:
  MYSQL_ROOT_PASSWORD: your_strong_password  # 修改此处
  DB_PWD: your_strong_password               # 修改此处
```

```yaml
# application-prod.yml (continew-server & continew-customer)
spring.datasource.password: ${DB_PWD:your_strong_password}
```

##### 2. 文件存储路径

`continew-customer` 模块的 `application.yml` 中配置了文件上传路径：

```yaml
# continew-customer/.../application.yml
file:
  upload:
    path: D:/nginx-1.26.1/file/          # 改为 Docker 容器内路径，例如 /app/data/file/
    url-prefix: /file/
    full-url-prefix: http://localhost:8085/upload/  # 改为实际的访问域名
```

Docker 部署时建议将文件存储路径映射到数据卷，并修改 `full-url-prefix` 为实际的访问地址。

##### 3. Snail Job 调度中心 API URL

`application-prod.yml` 中 Snail Job 客户端的 API URL 默认使用 `127.0.0.1:18001`，在容器内需确保能访问到 schedule-server。如果 schedule-server 在同一个 Docker Compose 网络中，可直接用服务名访问：

```yaml
snail-job.server.api.url: http://schedule-server:18001/snail-job
```

##### 4. 前端部署

前端静态资源部署方式（在 `docker-compose.yml` 中已注释）：

```yaml
continew-web:
  image: nginx:1.27.0
  container_name: continew-web
  restart: always
  ports:
    - '80:80'
    - '443:443'
  volumes:
    - /docker/nginx/conf/nginx.conf:/etc/nginx/nginx.conf
    - /docker/nginx/cert/:/etc/nginx/cert/
    - /docker/nginx/logs/:/var/log/nginx/
    - /docker/continew-admin/web/:/usr/share/nginx/html/
```

如有前端需要部署，取消该段注释，并将前端构建产物放入 `/docker/continew-admin/web/` 目录，同时配置 Nginx 反向代理到后端 API。

---

#### 常见问题

**Q: 容器启动后访问报 502/连接拒绝？**

A: 检查各服务是否已完全启动。MySQL 和 Redis 需要先行启动完成，Java 服务启动较慢（约 30-60 秒）。查看日志：`docker compose logs -f continew-server`。

**Q: Snail Job 客户端无法连接到调度中心？**

A: 确认 `SCHEDULE_HOST` 配置正确。如果 schedule-server 与客户端在同一个 Docker Compose 网络中，可直接使用服务名 `schedule-server`；否则使用宿主机 IP。

**Q: 数据库连接失败？**

A: 确认 MySQL 容器已正常启动，且 `DB_HOST` 配置正确。MySQL 首次启动需要初始化（约 1-2 分钟）。
