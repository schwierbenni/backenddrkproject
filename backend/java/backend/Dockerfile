# Use Maven image to build the application
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /usr/src/app
COPY . .
RUN mvn clean package -DskipTests

# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:17-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the application JAR file into the container at /app
COPY --from=build /usr/src/app/target/backend-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
