# 생성됨 - 2025-07-11 생성됨
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# Maven 빌드에 필요한 파일들 복사
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# 의존성 다운로드 (캐시 최ㅅ적화)
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

# 소스 코드 복사
COPY src src

# 애플리케이션 빌드
RUN ./mvnw package -DskipTests -B

# 빌드된 JAR 파일을 app.jar로 복사
RUN cp target/*.jar app.jar

# 포트 노출
EXPOSE 8081

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]