# Use a multi-stage build for efficiency
FROM maven:3.8.4-openjdk-11 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean install -DskipTests

# Second stage: Running the application
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/gestion-station-ski-0.0.1-SNAPSHOT.jar /app/gestion-station-ski-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/gestion-station-ski-0.0.1-SNAPSHOT.jar"]