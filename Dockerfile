# ==========================================
# STAGE 1: BUILD THE APPLICATION
# ==========================================
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copy Maven wrapper and project configuration
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Make Maven wrapper executable
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests


# ==========================================
# STAGE 2: RUN THE APPLICATION
# ==========================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create a non-root user
RUN addgroup -S spring && adduser -S spring -G spring \
    && mkdir -p /app/data \
    && chown -R spring:spring /app

COPY --from=build /app/target/*.jar app.jar

USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]