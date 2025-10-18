# 1. Build Stage: Compiles the source code using a JDK and Gradle
FROM gradle:8.4-jdk21-alpine AS build
WORKDIR /app

# Copy the build configuration and source code
COPY build.gradle settings.gradle ./
COPY src ./src

# Build the project and create the executable JAR
# Use '-x test' to skip running tests during the build stage
RUN gradle bootJar -x test

# 2. Runtime Stage: Uses a minimal JRE for a smaller final image
# --- FIX APPLIED HERE: Using eclipse-temurin:21-jre-jammy instead ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Expose the application port
EXPOSE 8080

# Copy the built JAR from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Define the entry point to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
