# Stage 1: Base image and build process
FROM openjdk:19-jdk AS build
LABEL authors="EmmanuelYi"

# Set working directory inside the container
WORKDIR /app

# Copy Maven-related files
COPY pom.xml ./
COPY mvnw ./
COPY .mvn .mvn

# Set execution permission for the Maven wrapper
RUN chmod +x ./mvnw

# Download dependencies (useful for caching)
RUN ./mvnw dependency:go-offline -B

# Copy the application source code
COPY src ./src

# Build the application
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the final image
FROM openjdk:19-jdk
LABEL authors="EmmanuelYi"

# Expose the application port
EXPOSE 5000

# Set the location for temporary files
VOLUME /tmp

# Copy the built JAR from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Set the default command to run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]