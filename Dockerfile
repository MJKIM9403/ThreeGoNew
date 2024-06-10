# jdk 17(amazoncorretto:17) 환경으로 구성
FROM amazoncorretto:17

# 인자 설정 :: 변수명 JAR_FILE
# build/libs(빌드 시 jar파일 생성 경로) 하위의 모든 jar파일
ARG JAR_FILE=build/libs/*.jar

# Docker Image 생성 시 JAR_FILE을 app.jar로 복사
COPY ${JAR_FILE} app.jar

RUN apt-get update && apt-get install -y sudo

# 타임존 설정(KTS)
RUN sudo ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

# 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]