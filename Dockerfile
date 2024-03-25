FROM eclipse-temurin:21
EXPOSE 8080
COPY ./target/arosaj-server-image.jar arosaj-server-image.jar
ENTRYPOINT ["java","-jar","/arosaj-server-image.jar"]