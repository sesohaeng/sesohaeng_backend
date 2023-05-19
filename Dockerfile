FROM openjdk:11.0.10
ARG JAR_FILE=build/libs/sesohaeng-backend-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]