FROM bellsoft/liberica-openjre-alpine:17.0.9
EXPOSE 8081
WORKDIR /opt/app
ADD ./target/Deal.jar ./service.jar
CMD ["java", "-jar", "/opt/app/service.jar"]