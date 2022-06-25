FROM maven:3.8.6-openjdk-18-slim as build
COPY src /app/src
COPY pom.xml /app
RUN mvn -f /app/pom.xml clean package

FROM openjdk:14-jdk-alpine
COPY --from=build /app/target/cursach-1.0.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]