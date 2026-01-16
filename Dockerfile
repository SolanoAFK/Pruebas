# Build Stage
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B -q

# Build application
COPY src ./src
RUN mvn clean package -DskipTests -q

# Runtime Stage
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Create log directory
RUN mkdir -p /var/log/app && chmod 777 /var/log/app

# Copy compiled JAR from builder
COPY --from=builder /app/target/Practicas-*.jar app.jar

# Labels
LABEL maintainer="Civil Portal Team"
LABEL version="1.0.0"
LABEL description="Civil Portal API - REST SpringBoot"

# Security: Run as non-root user
RUN useradd -m -u 1000 appuser && chown -R appuser:appuser /app /var/log/app
USER appuser

# Expose port
EXPOSE 8081

# Environment
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Health Check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8081/api/auth/ping || exit 1

# Start application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=prod -jar app.jar"]
