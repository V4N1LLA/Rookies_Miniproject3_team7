# 1단계: Vite로 앱 빌드
FROM node:18 AS build

WORKDIR /app

# 패키지 파일 복사 및 의존성 설치
COPY package*.json ./
COPY vite.config.mjs ./
COPY tailwind.config.js ./
COPY postcss.config.js ./
RUN npm install

# 소스 복사 및 빌드 실행
COPY . .
RUN npm run build

# 2단계: Nginx로 정적 파일 서빙
FROM nginx:alpine

# Nginx 라우팅 처리를 위한 설정 복사
COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf

# 빌드된 정적 파일 복사
COPY --from=build /app/dist /usr/share/nginx/html

# 기본 포트 노출
EXPOSE 80

# Nginx 실행
CMD ["nginx", "-g", "daemon off;"]