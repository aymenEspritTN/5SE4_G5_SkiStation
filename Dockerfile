# Use a multi-stage build for efficiency
FROM maven:3.8.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean install -DskipTests

# Second stage: Running the application
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/gestion-station-ski-0.0.1-SNAPSHOT.jar /app/gestion-station-ski-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/gestion-station-ski-0.0.1-SNAPSHOT.jar"]