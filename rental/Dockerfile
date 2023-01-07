FROM eclipse-temurin:17-jre-focal
EXPOSE 8060
ARG JAR_FILE=rental-impl/target/*.jar
COPY ${JAR_FILE} rental_application.jar
ENTRYPOINT ["java", "-jar", "/rental_application.jar"]