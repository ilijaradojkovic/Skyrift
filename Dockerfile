FROM openjdk:17-jdk
RUN mvn clean install
COPY target/skyrift.jar skyrift.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "skyrift.jar"]