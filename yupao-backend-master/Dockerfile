# ====== 构建阶段 ======
FROM maven:3.8-jdk-8-slim AS builder

WORKDIR /app

# 先复制 pom.xml，利用 Docker 缓存层加速依赖下载
COPY pom.xml .

# 下载依赖（仅 pom.xml 变化时才重新下载）
RUN mvn dependency:go-offline -B

# 复制源代码
COPY src ./src

# 构建产物
RUN mvn package -DskipTests -B

# ====== 运行阶段 ======
FROM openjdk:8-jre-alpine

WORKDIR /app

# 复制构建产物
COPY --from=builder /app/target/yupao-backend-0.0.1-SNAPSHOT.jar app.jar

# 暴露 8080 端口
EXPOSE 8080

# 设置 JVM 参数并启动
# CloudBase 会注入 PORT 环境变量，Spring Boot 通过 --server.port 读取
ENTRYPOINT ["sh", "-c", "java -Xms128m -Xmx256m -XX:+UseSerialGC -XX:+HeapDumpOnOutOfMemoryError -Djava.security.egd=file:/dev/./urandom -jar app.jar --spring.profiles.active=prod --server.port=${PORT:-8080}"]
