FROM openjdk:21-slim
EXPOSE 33306
COPY ./build/libs/*.jar app.jar
CMD ["java", "-jar", "app.jar"]
