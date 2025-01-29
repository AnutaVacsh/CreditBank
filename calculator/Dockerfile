FROM bellsoft/liberica-openjre-alpine:17.0.9
EXPOSE 8080
WORKDIR /opt/app
ADD ./target/calculator-0.0.1-SNAPSHOT.jar ./service.jar
CMD ["java", "-jar", "/opt/app/service.jar"]