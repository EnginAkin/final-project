FROM openjdk:17
COPY /target/final-project-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=docker","app.jar"]
