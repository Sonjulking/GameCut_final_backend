# 베이스 이미지
FROM openjdk:17-jdk-slim

# 작업 디렉토리 생성
WORKDIR /app

# build된 JAR 파일을 복사
COPY target/*.jar app.jar

# 포트 오픈
EXPOSE 8081

# JAR 실행
ENTRYPOINT ["java", "-jar", "app.jar"]