FROM eclipse-temurin:17-jre-focal
EXPOSE 8070
ARG JAR_FILE=cars-impl/target/*.jar
COPY ${JAR_FILE} cars-application.jar
ENTRYPOINT ["java", "-jar", "/cars-application.jar"]