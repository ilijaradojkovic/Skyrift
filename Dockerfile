# Build stage
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests

# Run stage
FROM openjdk:17-jdk
WORKDIR /app
COPY --from=build /app/target/skyrift.jar skyrift.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "skyrift.jar"]
