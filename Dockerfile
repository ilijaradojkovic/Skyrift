FROM openjdk:17-jdk
ADD target/skyrift.jar skyrift.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "skyrift.jar"]
