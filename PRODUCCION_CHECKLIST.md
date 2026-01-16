# 🚀 Análisis Completo y Checklist para Producción en Dokploy

## 📊 Estado Actual del Proyecto

### ✅ LO QUE ESTÁ BIEN
1. **Dockerfile optimizado** - Build multi-stage correcto
2. **application-prod.properties** - Configurado con variables de entorno
3. **Security** - JWT implementado, BCrypt para contraseñas
4. **Base de datos** - MySQL 8.0 configurada en Dokploy
5. **API REST** - Endpoints públicos y autenticados

### ⚠️ LO QUE FALTA O NECESITA AJUSTES

---

## 🔴 CRÍTICO - Arreglar Antes de Desplegar

### 1. **JWT_SECRET No Configurado**
**Problema:** La aplicación usa un JWT secret en texto claro
```properties
app.jwt.secret=HOLA_AMIGOS_DE_LA_CARRERA_DE_INGENIERIA_DE_SISTEMAS_SEAN_BIENVENIDOS_AL_TOKENXD
```

**Solución - DEBE HACER ESTO EN DOKPLOY:**
- Ve a Dashboard → tu App → Settings → Environment Variables
- Añade esta variable:
  ```
  JWT_SECRET=tu_secreto_super_seguro_aleatorio_de_minimo_32_caracteres
  ```
- Ejemplo válido: `JWT_SECRET=$(openssl rand -base64 32)`

**En application-prod.properties:**
```properties
app.jwt.secret=${JWT_SECRET}
```

---

### 2. **database_host Incorrecto en Dokploy**
**Problema:** El Dockerfile usa `mysql:3306` pero en Dokploy el servicio tiene otro nombre

**Solución:**
En Dokploy, el servicio MySQL probablemente se llama `mysql-service` o similar.

**Verificar en Dokploy:**
1. Ve a Services → MySQL
2. Copia el hostname exacto (ej: `mysql-prod-db`)
3. En tu app, environment variables:
   ```
   SPRING_DATASOURCE_URL=jdbc:mysql://NOMBRE_DEL_SERVICIO_MYSQL:3306/civil_portal?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
   DB_HOST=NOMBRE_DEL_SERVICIO_MYSQL
   ```

---

### 3. **Puerto 8081 No Expuesto en Dockerfile**
El EXPOSE está pero el Dockerfile necesita instrucción clara

**Actualizar Dockerfile:**
```dockerfile
# ...resto del código...
EXPOSE 8081
ENV SPRING_PROFILES_ACTIVE=prod
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
```

---

## 🟡 IMPORTANTE - Variables de Entorno en Dokploy

Agregar estas variables en Dashboard → App → Settings:

```
# Base de Datos (REQUIERE)
DB_HOST=nombre-de-tu-servicio-mysql
DB_NAME=civil_portal
DB_USER=civil_app
DB_PASSWORD=CivilApp@123

# JWT (REQUIERE - Cambiar)
JWT_SECRET=tu_secreto_aleatorio_super_seguro_32_caracteres_minimo

# Spring
SPRING_PROFILES_ACTIVE=prod
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_DATASOURCE_URL=jdbc:mysql://${DB_HOST}:3306/${DB_NAME}?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC

# Aplicación
SERVER_PORT=8081
SPRING_APPLICATION_NAME=CivilPortal-API
```

---

## 🔵 RECOMENDACIONES - Mejorar Producción

### 4. **Agregar application-prod.properties Mejorado**

```properties
# ==========================================
# PRODUCCIÓN - DOKPLOY
# ==========================================

# Server
server.port=8081
server.servlet.context-path=/api

# Database
spring.datasource.url=jdbc:mysql://${DB_HOST:localhost}:3306/${DB_NAME:civil_portal}?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=false
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true

# Logging
logging.level.root=WARN
logging.level.solano.com.Practicas=INFO
logging.level.org.springframework.web=INFO
logging.level.org.hibernate.SQL=WARN
logging.file.name=/var/log/app/application.log
logging.file.max-size=10MB
logging.file.max-history=10

# JWT
app.jwt.secret=${JWT_SECRET}
app.jwt.expiration-minutes=60

# Upload Files
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB

# Connection Pool
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=20000
spring.datasource.hikari.idle-timeout=300000
spring.datasource.hikari.max-lifetime=1200000

# Actuator (Monitoreo)
management.endpoints.web.exposure.include=health,metrics
management.endpoint.health.show-details=always
```

---

### 5. **Actualizar Dockerfile para Producción**

```dockerfile
# Build stage
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copiar pom.xml y descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests -q

# Runtime stage
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Crear directorio para logs
RUN mkdir -p /var/log/app && chmod 777 /var/log/app

# Copiar el JAR compilado desde el builder
COPY --from=builder /app/target/Practicas-*.jar app.jar

# Labels
LABEL maintainer="Your Team"
LABEL version="1.0.0"
LABEL description="Civil Portal API - Spring Boot REST"

# Usuario no-root (mejor seguridad)
RUN useradd -m -u 1000 appuser && chown -R appuser:appuser /app /var/log/app
USER appuser

# Puerto
EXPOSE 8081

# Health Check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8081/api/auth/ping || exit 1

# Environment
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dspring.profiles.active=prod -jar app.jar"]
```

---

### 6. **Script de Inicialización de BD (Para Dokploy)**

Crear archivo: `scripts/init-db.sql`

```sql
-- Script de inicialización para producción
USE civil_portal;

-- Crear tabla si no existe
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Insertar roles por defecto
INSERT IGNORE INTO roles (nombre, descripcion) VALUES
('ADMIN', 'Administrador del sistema'),
('INGENIERO', 'Ingeniero civil'),
('SUPERVISOR', 'Supervisor de proyectos'),
('VISOR', 'Solo lectura');

-- Crear usuario admin (contraseña: admin123 - bcrypt hash)
INSERT IGNORE INTO usuarios (username, password_hash, nombres, apellidos, email, estado, created_at)
VALUES (
    'admin',
    '$2a$10$slYQmyNdGzin7olVlspK2OPST9/PgBkqquzi.Sm1yL6DyW9/LO23O',
    'Administrador',
    'Sistema',
    'admin@civil.portal',
    1,
    NOW()
);

-- Asignar rol ADMIN
INSERT IGNORE INTO usuario_roles (usuario_id, rol_id)
SELECT u.id, r.id FROM usuarios u, roles r
WHERE u.username = 'admin' AND r.nombre = 'ADMIN';

-- Verificar
SELECT u.id, u.username, r.nombre FROM usuarios u 
LEFT JOIN usuario_roles ur ON u.id = ur.usuario_id 
LEFT JOIN roles r ON ur.rol_id = r.id;
```

---

## 📋 CHECKLIST FINAL ANTES DE DESPLEGAR

### Fase 1: Preparar el Código
- [ ] Actualizar `application-prod.properties` con las propiedades sugeridas
- [ ] Actualizar `Dockerfile` con la versión mejorada
- [ ] Crear script `scripts/init-db.sql`
- [ ] Commit y push a GitHub
  ```bash
  git add .
  git commit -m "chore: Production configuration for Dokploy"
  git push origin main
  ```

### Fase 2: Configurar en Dokploy
- [ ] Ve a tu App en Dashboard
- [ ] Click en "Settings"
- [ ] Agregar Variables de Entorno:
  - [ ] `JWT_SECRET` (generar con: `openssl rand -base64 32`)
  - [ ] `DB_HOST` (nombre exacto del servicio MySQL)
  - [ ] `DB_USER` (civil_app)
  - [ ] `DB_PASSWORD` (contraseña segura)
  - [ ] `DB_NAME` (civil_portal)
  - [ ] `SPRING_PROFILES_ACTIVE` (prod)

### Fase 3: Conectar Base de Datos
- [ ] Conectarse a MySQL en Dokploy
- [ ] Ejecutar script `init-db.sql`
- [ ] Verificar que se creó el usuario admin

### Fase 4: Desplegar
- [ ] Cambiar rama a "main" si es necesario
- [ ] Click en "Deploy" en Dokploy
- [ ] Esperar a que termine (5-10 minutos)
- [ ] Ver logs: Click en "Logs"
- [ ] Buscar mensajes como:
  ```
  Tomcat started on port 8081
  Application 'CivilPortal-API' started
  ```

### Fase 5: Validar
- [ ] Probar endpoint público: `GET https://tu-dominio/api/auth/ping`
- [ ] Probar login: `POST https://tu-dominio/api/auth/login`
  ```json
  {
    "username": "admin",
    "password": "admin123"
  }
  ```
- [ ] Verificar token JWT recibido
- [ ] Usar token para acceder a endpoints protegidos

### Fase 6: Monitoreo
- [ ] Configurar alertas en Dokploy
- [ ] Revisar logs regularmente
- [ ] Backup automático de BD configurado

---

## 🔐 Seguridad - TODO ESTO ES IMPORTANTE

**ANTES de ir a producción:**
1. ❌ **NO** dejar secrets en código
2. ✅ Usar variables de entorno en Dokploy
3. ✅ JWT_SECRET debe ser aleatorio y largo (32+ caracteres)
4. ✅ Cambiar contraseñas por defecto
5. ✅ HTTPS habilitado (Dokploy debería ofrecerlo)
6. ✅ Database password fuerte
7. ✅ Logs sin mostrar información sensible

---

## 🆘 Troubleshooting

| Problema | Solución |
|----------|----------|
| "Connection refused" | Verificar DB_HOST es correcto, BD está running |
| "JWT secret not set" | Configurar JWT_SECRET en variables de entorno |
| "Port already in use" | Cambiar puerto en application-prod.properties |
| "Build fails" | Revisar logs, verificar pom.xml válido |
| "403 Forbidden" | Verificar token JWT, rutas públicas en SecurityConfig |

---

## 📞 Próximos Pasos

1. Actualiza los archivos sugeridos
2. Push a GitHub
3. Configura variables en Dokploy
4. Ejecuta init-db.sql
5. Despliega
6. Prueba todos los endpoints
7. Configura monitoreo

¿Necesitas ayuda con algún paso específico?
