FROM eclipse-temurin:21
WORKDIR ./
EXPOSE 8080
VOLUME /tmp
COPY target/arosaj-server-image.jar arosaj-server-image.jar
ENTRYPOINT ["java","-jar","/arosaj-server-image.jar"]