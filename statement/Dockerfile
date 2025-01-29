FROM bellsoft/liberica-openjre-alpine:17.0.9
EXPOSE 8082
WORKDIR /opt/app
ADD ./target/Statement.jar ./service.jar
CMD ["java", "-jar", "/opt/app/service.jar"]