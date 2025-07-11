# Java 17을 기반으로 실행
FROM openjdk:17-jdk-slim

# target 폴더의 JAR 파일을 복사
COPY ./target/*.jar app.jar

# 포트 노출
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]