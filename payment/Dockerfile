FROM eclipse-temurin:17-jre-focal
EXPOSE 8050
ARG JAR_FILE=payment-impl/target/*.jar
COPY ${JAR_FILE} payment-application.jar
ENTRYPOINT ["java", "-jar", "/payment-application.jar"]