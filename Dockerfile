FROM openjdk:21

EXPOSE 8080 

ARG JAR_FILE=target/projects-test-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} application.jar

CMD apt-get update -y

ENTRYPOINT ["java", "-jar", "/application.jar"]
