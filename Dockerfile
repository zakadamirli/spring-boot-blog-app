# Use OpenJDK as base image
FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /var/www/springtest

# Copy the built jar file
COPY target/questapp-0.0.1-SNAPSHOT.jar app.jar


# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
