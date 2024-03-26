FROM eclipse-temurin:21
EXPOSE 8080
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} arosaj-server-image.jar
ENTRYPOINT ["java","-jar","arosaj-server-image.jar"]