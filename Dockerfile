FROM openjdk:17-jdk-slim-buster

WORKDIR /opt

COPY target/*.jar .

EXPOSE 8080

CMD exec $JAVA_HOME/bin/java $JAVA_OPTS -jar /opt/NextCar-Backend-*.jar