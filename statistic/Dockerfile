FROM eclipse-temurin:17-jre-focal
EXPOSE 8085
ARG JAR_FILE=statistic-impl/target/*.jar
COPY ${JAR_FILE} statistic-application.jar
ENTRYPOINT ["java", "-jar", "-Xmx200m", "-Dspring-boot.run.profiles=local", "/statistic-application.jar"]