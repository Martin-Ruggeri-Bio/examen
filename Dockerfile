# Start with a base image containing Java runtime
FROM openjdk:8-jdk-alpine

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