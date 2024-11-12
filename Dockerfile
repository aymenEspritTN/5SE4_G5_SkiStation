# Start with a base image that includes OpenJDK 17
FROM openjdk:17-jdk-alpine

# Expose the port the application will run on
EXPOSE 8089

# Copy the JAR file to the container and rename it to app.jar
COPY target/*.jar app.jar

# Set the entry point to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]