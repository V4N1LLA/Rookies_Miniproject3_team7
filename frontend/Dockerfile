FROM nginx:alpine

# Nginx 라우팅 처리를 위한 설정 복사
COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf

# 로컬에서 복사된 dist 폴더 정적 파일 복사
COPY dist /usr/share/nginx/html

# 기본 포트 노출
EXPOSE 80

# Nginx 실행
CMD ["nginx", "-g", "daemon off;"]