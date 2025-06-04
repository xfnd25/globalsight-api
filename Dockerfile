# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and POM file to leverage Docker cache
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Optional: Download dependencies to leverage Docker cache better
# RUN chmod +x ./mvnw && ./mvnw dependency:go-offline -B

# Copy the rest of the application code
COPY src ./src

# Build the application and package it into a JAR file
# Ensure mvnw is executable
RUN chmod +x ./mvnw && ./mvnw package -DskipTests

# Specify the exact JAR file name (replace if your version/artifactId changes)
ARG JAR_FILE=target/globalsight-api-0.0.1-SNAPSHOT.jar

# Copy the specific JAR file to application.jar
COPY ${JAR_FILE} application.jar

# Expose the port the application runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java","-jar","/app/application.jar"]