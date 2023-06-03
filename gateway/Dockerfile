FROM eclipse-temurin:17-jre-focal
EXPOSE 8080
ARG JAR_FILE=gateway-impl/target/*.jar
COPY ${JAR_FILE} gateway-application.jar
ENTRYPOINT ["java", "-jar", "-Xmx200m", "-Dspring-boot.run.profiles=local", "/gateway-application.jar"]