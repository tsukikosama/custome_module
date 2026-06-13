#!/bin/bash
# ============================================================
# ContiNew Admin Docker 构建脚本（Linux/Mac）
# 使用 fat-jar 模式打包并构建 Docker 镜像
# ============================================================

set -e

echo "========================================"
echo " ContiNew Admin Docker Build"
echo "========================================"

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"

# Step 1: Maven 构建（fat-jar）
echo "[1/4] Maven 构建（fat-jar 模式）..."
cd "$ROOT_DIR"
mvn clean package -P fat-jar -DskipTests
echo "[OK] Maven 构建成功"

# Step 2: 复制 JAR 到 Docker 构建上下文
echo "[2/4] 复制 JAR 到 Docker 构建上下文..."

# continew-server
if [ -f "continew-server/target/continew-admin.jar" ]; then
    cp -f "continew-server/target/continew-admin.jar" "docker/continew-admin/continew-admin.jar"
    echo "[OK] continew-admin.jar 已复制"
else
    echo "[WARN] continew-server/target/continew-admin.jar 不存在，跳过"
fi

# schedule-server
if [ -f "continew-extension/continew-extension-schedule-server/target/continew-extension-schedule-server.jar" ]; then
    cp -f "continew-extension/continew-extension-schedule-server/target/continew-extension-schedule-server.jar" "docker/schedule-server/continew-extension-schedule-server.jar"
    echo "[OK] schedule-server.jar 已复制"
else
    echo "[WARN] schedule-server jar 不存在，跳过"
fi

# Step 3: Docker Compose 构建
echo "[3/4] Docker Compose 构建镜像..."
cd "$ROOT_DIR/docker"
docker-compose build
echo "[OK] Docker 镜像构建成功"

# Step 4: 清理构建上下文的 JAR
echo "[4/4] 清理 Docker 构建上下文中的 JAR..."
rm -f "$ROOT_DIR/docker/continew-admin/continew-admin.jar"
rm -f "$ROOT_DIR/docker/schedule-server/continew-extension-schedule-server.jar"
echo "[OK] 清理完成"

echo "========================================"
echo " 构建完成！"
echo " 启动服务: cd docker && docker-compose up -d"
echo "========================================"
