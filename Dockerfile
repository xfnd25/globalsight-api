FROM maven:3.9.6-eclipse-temurin-17 AS build

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk
WORKDIR /app

COPY --from=build /app/target/*.jar application.jar

EXPOSE 8080

USER appuser

ENTRYPOINT ["java", "-jar", "application.jar"]
