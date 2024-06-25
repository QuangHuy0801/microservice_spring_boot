FROM openjdk:21
EXPOSE 8080

ADD target/*.jar app.jar

# Specify the command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app.jar"]
