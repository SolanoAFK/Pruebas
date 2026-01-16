# Build stage
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copiar pom.xml y descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copiar el JAR compilado desde el builder
COPY --from=builder /app/target/Practicas-*.jar app.jar

# Metadata
LABEL maintainer="Civil Portal Team"
LABEL description="Civil Portal API - Spring Boot Application"

# Puerto de la aplicación
EXPOSE 8081

# Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD java -cp app.jar -c "import java.net.URL; new URL(\"http://localhost:8081/\").openConnection().getResponseCode()" || exit 1

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
