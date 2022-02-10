FROM openjdk:8-jdk-alpine
MAINTAINER imonahhov
COPY target/khngtest-0.0.1-SNAPSHOT.jar khngtest.jar
ENTRYPOINT ["java","-jar","/khngtest.jar"]
