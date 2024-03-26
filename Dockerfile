FROM eclipse-temurin:21
EXPOSE 3000
VOLUME /tmp
COPY target/arosaj-server.jar arosaj-server-image.jar
ENTRYPOINT ["java","-jar","/arosaj-server-image.jar"]