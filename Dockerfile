# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and POM file to leverage Docker cache
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
# If you have mvnw.cmd in your project root, uncomment the next line
# COPY mvnw.cmd ./mvnw.cmd

# Optional: Download dependencies to leverage Docker cache better
# RUN chmod +x ./mvnw && ./mvnw dependency:go-offline -B

# Copy the rest of the application code
COPY src ./src

# Build the application and package it into a JAR file
# Ensure mvnw is executable
RUN chmod +x ./mvnw && ./mvnw package -DskipTests

# ---- START DEBUGGING STEP ----
# List contents of target directory to see if JAR is there
RUN echo "Listing contents of /app/target:" && (find /app/target -type f -print0 | xargs -0 sh -c 'ls -l "$@"' -- || echo "Target directory is empty or does not exist or find/xargs failed")
# ---- END DEBUGGING STEP ----

# Specify the exact JAR file name (replace if your version/artifactId changes)
ARG JAR_FILE=target/globalsight-api-0.0.1-SNAPSHOT.jar

# Copy the specific JAR file to application.jar
RUN mv ${JAR_FILE} /app/application.jar

# Expose the port the application runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java","-jar","/app/application.jar"]