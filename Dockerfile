# Use OpenJDK base image (available without Docker Hub login)
FROM openjdk:11-jre-slim

# Set working directory
WORKDIR /app

# Copy the pre-built JAR file from Jenkins workspace
COPY target/task-manager.jar .

# Run the application
CMD ["java", "-jar", "task-manager.jar"]
