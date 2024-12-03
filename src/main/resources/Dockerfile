FROM openjdk:21

EXPOSE 8080 

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} application.jar

CMD apt-get update -y

ENTRYPOINT ["java", "-jar", "/application.jar"]
