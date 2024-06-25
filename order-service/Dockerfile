FROM openjdk:21


EXPOSE 8084

ADD target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
