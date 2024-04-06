# Usar una imagen base con JDK 11 y Maven
FROM maven:3.8.4-openjdk-11 AS build

# Establecer un directorio de trabajo
WORKDIR /app

# Copiar archivos de tu proyecto al directorio de trabajo
COPY . /app

# Ejecutar Maven para construir el proyecto
RUN mvn clean package

# Start with a base image containing Java runtime
FROM openjdk:11-jre-slim-buster

# Add Maintainer Info
LABEL maintainer="martinruggeri18@gmail.com"

# Make port 8080 available to the world outside this container
EXPOSE 8080

# The application's jar file
ARG JAR_FILE=target/examen-0.0.1.jar

# Add the application's jar to the container
ADD ${JAR_FILE} examen.jar

# Run the jar file 
ENTRYPOINT ["java","-jar","/examen.jar"]