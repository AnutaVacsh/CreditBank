FROM bellsoft/liberica-openjre-alpine:17.0.9
EXPOSE 8084
WORKDIR /opt/app
ADD ./target/Gateway.jar ./service.jar
CMD ["java", "-jar", "/opt/app/service.jar"]