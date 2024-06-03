# Use an official image as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the jar file
COPY *.jar app.jar

# Pass the JASYPT_KEY as an environment variable
ARG JASYPT_KEY
ENV JASYPT_KEY=${JASYPT_KEY}

# Run the jar file
ENTRYPOINT ["java", "-Djasypt.encryptor.password=${JASYPT_KEY}", "-jar", "app.jar"]