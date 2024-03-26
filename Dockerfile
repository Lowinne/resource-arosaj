FROM eclipse-temurin:21
EXPOSE 8080
VOLUME /tmp
COPY **/*.jar arosaj-server-image.jar
ENTRYPOINT ["java","-jar","/arosaj-server-image.jar"]