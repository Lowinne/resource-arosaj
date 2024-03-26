FROM eclipse-temurin:21
EXPOSE 8080
VOLUME /tmp
COPY */**.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]