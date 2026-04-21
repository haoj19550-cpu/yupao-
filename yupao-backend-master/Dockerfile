# ====== 运行阶段（本地已构建，仅需运行时环境） ======
# 前置条件：本地执行 mvn package -DskipTests 后，
#           将 target/yupao-backend-0.0.1-SNAPSHOT.jar 复制到本文件同目录下并重命名为 app.jar

FROM eclipse-temurin:8-jre-alpine

WORKDIR /app

# 复制本地预构建的 JAR 包
COPY app.jar ./app.jar

# 暴露 8080 端口
EXPOSE 8080

# 设置 JVM 参数并启动
ENTRYPOINT ["sh", "-c", "java -Xms128m -Xmx256m -XX:+UseSerialGC -XX:+HeapDumpOnOutOfMemoryError -Djava.security.egd=file:/dev/./urandom -jar app.jar --spring.profiles.active=prod --server.port=${PORT:-8080}"]
