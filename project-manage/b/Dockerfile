FROM openjdk:17-jdk-slim

#도커 외부에서 빌드 한 다음 실행시키는 방법

# JAR 파일 복사 (외부에서 copy 하는 방식 내부에서 빌드를 했기 때문에 가져올 필요 X)
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 실행 명령어
ENTRYPOINT ["java", "-jar", "/app.jar"]

#도커 내부로 copy 한다음 내부에서 빌드해서 실행시키는 방법

#WORKDIR /app

#COPY . .

# Gradle 실행 권한 부여 (필요없음 도커 내부로 copy를 했기 때문에 권한이 다 있음)
#RUN #chmod +x ./gradlew

# Gradle 빌드 실행 (외부에서 빌드해서 copy하는걸 추천 도커 내부에서 빌드하니깐 너무 오래걸림)
#RUN ./gradlew clean bootJar

# 실행 명령어
#CMD ["sh", "-c", "java -jar build/libs/*.jar"]