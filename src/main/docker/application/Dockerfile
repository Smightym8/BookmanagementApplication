FROM openjdk:17-jdk-alpine
ARG JAR_FILE=*SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=production","app.jar"]