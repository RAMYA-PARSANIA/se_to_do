# Use Eclipse Temurin JRE base image
FROM eclipse-temurin:11-jre-alpine

# Set working directory
WORKDIR /app

# Copy the pre-built JAR file from Jenkins workspace
COPY target/task-manager.jar .

# Run the application
CMD ["java", "-jar", "task-manager.jar"]
