# ====== 构建阶段 ======
FROM node:18-alpine AS builder

WORKDIR /app

# 先复制依赖文件，利用 Docker 缓存层加速构建
COPY package.json package-lock.json* ./

# 安装依赖
RUN npm install --registry=https://registry.npmmirror.com

# 复制源代码
COPY . .

# 构建生产包
RUN npm run build

# ====== 运行阶段 ======
FROM nginx:1.24-alpine

# 复制构建产物到 Nginx 默认静态资源目录
COPY --from=builder /app/dist /usr/share/nginx/html

# 复制自定义 Nginx 配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 暴露 80 端口
EXPOSE 80

# 启动 Nginx（前台运行）
CMD ["nginx", "-g", "daemon off;"]
