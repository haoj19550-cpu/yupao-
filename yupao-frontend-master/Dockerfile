# ====== 运行阶段（本地已构建，仅需运行时环境） ======
# 前置条件：本地执行 npm run build 后，
#           将生成的 dist/ 文件夹复制到本文件同目录下

FROM nginx:1.24-alpine

# 复制本地预构建的前端产物
COPY dist/ /usr/share/nginx/html/

# 复制自定义 Nginx 配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 暴露 80 端口
EXPOSE 80

# 启动 Nginx（前台运行）
CMD ["nginx", "-g", "daemon off;"]
