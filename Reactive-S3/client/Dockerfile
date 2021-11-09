FROM alpine

RUN apk update
RUN apk add openjdk11

COPY target/client.jar /opt/app.jar

EXPOSE 9001

CMD java -jar /opt/app.jar