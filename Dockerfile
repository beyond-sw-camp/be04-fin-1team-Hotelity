FROM openjdk:17-alpine

# X11 라이브러리와 fontconfig 설치
RUN apk update && apk add fontconfig ttf-dejavu && rm -rf /var/cache/apk/*

COPY build/libs/*.jar app.jar

# 환경 변수 설정 및 출력
ARG JASYPT_KEY
ENV JASYPT_KEY=$JASYPT_KEY
ENV JAVA_OPTS="-Djava.awt.headless=true"

CMD echo "JASYPT_KEY value is: $JASYPT_KEY"

# 애플리케이션 실행
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --jasypt.encryptor.password=${JASYPT_KEY}"]
