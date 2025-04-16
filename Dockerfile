FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/*.jar app.jar

RUN mkdir -p /app/logs
VOLUME /app/logs

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]