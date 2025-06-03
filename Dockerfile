# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and POM file to leverage Docker cache
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download dependencies (this layer will be cached unless pom.xml changes)
# RUN ./mvnw dependency:go-offline -B
# Skipping go-offline for now as it can sometimes cause issues in simpler setups
# and the full build will download them anyway.

# Copy the rest of the application code
COPY src ./src

# Build the application and package it into a JAR file
# Ensure mvnw is executable
RUN chmod +x ./mvnw && ./mvnw package -DskipTests

# Argument to specify the JAR file name (useful if version changes)
ARG JAR_FILE=target/*.jar

# Copy the JAR file from the build stage to the current directory in the container
COPY ${JAR_FILE} application.jar

# Expose the port the application runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java","-jar","/app/application.jar"]
