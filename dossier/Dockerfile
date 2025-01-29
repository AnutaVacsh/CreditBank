FROM bellsoft/liberica-openjre-alpine:17.0.9
EXPOSE 8083
WORKDIR /opt/app
ADD ./target/Dossier.jar ./service.jar
CMD ["java", "-jar", "/opt/app/service.jar"]