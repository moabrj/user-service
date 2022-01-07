FROM openjdk:17-alpine
COPY target/user-service-0.0.1-SNAPSHOT.jar jar/user-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/jar/user-service-0.0.1-SNAPSHOT.jar"]
EXPOSE 8000