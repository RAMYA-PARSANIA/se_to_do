FROM maven:3.8.7-openjdk-11-slim AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY TaskManager.java .
COPY TaskManagerTest.java .

# Build the application
RUN mvn clean package -DskipTests

# Use a lighter image for runtime
FROM openjdk:11-jre-slim

# Set working directory
WORKDIR /app

# Copy the JAR file from build stage
COPY --from=build /app/target/task-manager.jar .

# Run the application
CMD ["java", "-jar", "task-manager.jar"]
