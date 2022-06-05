FROM openjdk:17
ADD /target/final-project-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar", "/app.jar","--spring.profiles.active=docker"]
