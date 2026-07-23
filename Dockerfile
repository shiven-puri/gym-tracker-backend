# Use lightweight official OpenJDK 21 runtime
FROM eclipse-temurin:21-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy compiled JAR into container
COPY target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Command to run application
ENTRYPOINT ["java", "-jar", "app.jar"]