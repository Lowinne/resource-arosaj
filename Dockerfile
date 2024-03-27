FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install
FROM eclipse-temurin:21
WORKDIR /app
COPY --from=build app/target/arosaj-server.jar ./arosaj-server-image.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","arosaj-server-image.jar"]