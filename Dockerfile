FROM eclipse-temurin:17-jdk-jammy

COPY target/NextCar-Backend-*.jar /NextCarBackend.jar

EXPOSE 8080

CMD ["java", "-jar", "/NextCarBackend.jar"]