# This is the base image of the app that comes from Maven to build the app
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# This defines the base directory to find all the necessary stuff
WORKDIR /app

# Now being in the /app, we want pom.xml file from there to build the app
COPY pom.xml .

# Then all the dependencies required for the app, as listed in pom file are installed
RUN mvn dependency:go-offline -B

# The source code is then copied into the ./src file
COPY src ./src

# Then it will package everything up
RUN mvn clean package

# From now on, in the following steps, it is going to copy the jar that has auto-generated code
# that we need for our service app to work, and here we also expose that service to a port
FROM openjdk:21-jdk AS runner

WORKDIR /app

COPY --from=builder ./app/target/billing-service-0.0.1-SNAPSHOT.jar ./app.jar

# Here we define the port for the REST services
EXPOSE 4001
# Here the port for gRPC services is defined
EXPOSE 9001

# Here we define the entry point on how to start a container based on this image
ENTRYPOINT ["java", "-jar", "app.jar"]
