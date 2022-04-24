FROM alpine

RUN apk update

RUN apk add --no-cache openjdk11

COPY target/server.jar /opt/app.jar

EXPOSE 8000

CMD java  -Djava.rmi.server.hostname=0.0.0.0 \
                         -Dcom.sun.management.jmxremote \
                         -Dcom.sun.management.jmxremote.port=8010 \
                         -Dcom.sun.management.jmxremote.rmi.port=8010 \
                         -Dcom.sun.management.jmxremote.local.only=false \
                         -Dcom.sun.management.jmxremote.authenticate=false \
                         -Dcom.sun.management.jmxremote.ssl=false \
                         -jar /opt/app.jar