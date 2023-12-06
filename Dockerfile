FROM openjdk:17-jdk-slim

LABEL maintainer="Ngor SECK"

EXPOSE 8080

RUN mkdir -p /app/data

ADD target/dockervolume.jar dockervolume.jar

ENTRYPOINT ["java", "-jar", "dockervolume.jar"]