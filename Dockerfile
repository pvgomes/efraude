FROM openjdk:8-alpine

COPY target/uberjar/fraude.jar /fraude/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/fraude/app.jar"]
