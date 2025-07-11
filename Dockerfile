FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/productos-service-1.0.0.jar producto-service.jar
EXPOSE 1000
ENTRYPOINT ["java", "-jar", "producto-service.jar", "--spring.profiles.active=docker"]
