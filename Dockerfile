# 1. Java 이미지 기반
FROM openjdk:17-jdk-slim
# 2. JAR 파일을 앱 디렉토리로 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
# 3. 포트 설정 (application.yml의 port와 같아야 함)
EXPOSE 8080
# 4. 앱 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
