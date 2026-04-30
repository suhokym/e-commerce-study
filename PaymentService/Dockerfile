FROM eclipse-temurin:21-jre

WORKDIR /app

COPY build/libs/PaymentService.jar app.jar

# 컨테이너 환경에서 JVM 최적 동작
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]