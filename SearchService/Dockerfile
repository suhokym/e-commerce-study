FROM eclipse-temurin:21-jre

WORKDIR /app

COPY build/libs/SearchService.jar app.jar

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
