FROM openjdk:17-jdk
ADD target/berzza.jar berzza.jar
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "berzza.jar"]
