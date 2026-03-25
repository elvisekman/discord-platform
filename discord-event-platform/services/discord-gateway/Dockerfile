# ── Build stage ──────────────────────────────────────────────────────────────
FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /app

# Cache dependency downloads separately from source changes
COPY pom.xml .
RUN mvn dependency:go-offline -q

COPY src ./src
RUN mvn package -DskipTests -q

# ── Runtime stage ─────────────────────────────────────────────────────────────
FROM eclipse-temurin:25-jre-jammy

ENV JAVA_OPTS=""

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
