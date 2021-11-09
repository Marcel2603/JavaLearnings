FROM alpine

RUN apk update

RUN apk add openjdk11

COPY target/server.jar /opt/app.jar

EXPOSE 9000

CMD java -jar /opt/app.jar