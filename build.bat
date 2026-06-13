@echo off
title ContiNew Admin Docker Build

:: ============================================================
:: ContiNew Admin Docker Build Script (Windows)
:: fat-jar mode: build -> copy -> docker image -> cleanup
:: ============================================================

setlocal enabledelayedexpansion

:: Set Java 17 environment
set "JAVA_HOME=D:\java17"
set "PATH=%JAVA_HOME%\bin;%PATH%"

:: Set Maven (bundled, no system install required)
set "MAVEN_HOME=%CD%\.maven\apache-maven-3.9.9"
set "PATH=%MAVEN_HOME%\bin;%PATH%"

echo ========================================
echo  ContiNew Admin Docker Build
echo ========================================

:: Check prerequisites
where docker >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Docker not found. Make sure 'docker' is in your PATH.
    exit /b 1
)

"%JAVA_HOME%\bin\java" -version 2>&1 | find "17.0" >nul
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Java 17 not found at %JAVA_HOME%
    exit /b 1
)

:: Step 1: Maven build (fat-jar)
echo.
echo [1/4] Maven build (fat-jar profile)...
echo Running: mvn clean package -P fat-jar -DskipTests
call mvn clean package -P fat-jar -DskipTests
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Maven build failed
    exit /b 1
)
echo [OK] Maven build succeeded

:: Step 2: Copy JARs to Docker build context
echo.
echo [2/4] Copying JARs to Docker build context...

set "SERVER_JAR=continew-server\target\continew-admin.jar"
set "DOCKER_SERVER_DIR=docker\continew-admin"
set "SCHEDULE_JAR=continew-extension\continew-extension-schedule-server\target\continew-extension-schedule-server.jar"
set "DOCKER_SCHEDULE_DIR=docker\schedule-server"

:: continew-server
if exist "!SERVER_JAR!" (
    copy /Y "!SERVER_JAR!" "!DOCKER_SERVER_DIR!\"
    if !ERRORLEVEL! equ 0 (
        echo [OK] continew-admin.jar copied
    )
) else (
    echo [WARN] !SERVER_JAR! not found, skipping continew-server
)

:: schedule-server
if exist "!SCHEDULE_JAR!" (
    copy /Y "!SCHEDULE_JAR!" "!DOCKER_SCHEDULE_DIR!\"
    if !ERRORLEVEL! equ 0 (
        echo [OK] schedule-server.jar copied
    )
) else (
    echo [WARN] schedule-server jar not found, skipping
)

:: Step 3: Docker Compose build
echo.
echo [3/4] Building Docker images...
pushd docker
docker compose build
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Docker build failed
    popd
    exit /b 1
)
popd
echo [OK] Docker images built

:: Step 4: Cleanup copied JARs
echo.
echo [4/4] Cleaning up JARs from build context...
if exist "docker\continew-admin\continew-admin.jar" del "docker\continew-admin\continew-admin.jar"
if exist "docker\schedule-server\continew-extension-schedule-server.jar" del "docker\schedule-server\continew-extension-schedule-server.jar"
echo [OK] Cleanup done

echo.
echo ========================================
echo  Build complete!
echo  Start services: cd docker ^&^& docker compose up -d
echo ========================================
endlocal
