# Guía: Dockerizar y Desplegar en Dokploy

## 📋 Archivos Creados

- `Dockerfile` - Build de la aplicación Spring Boot (multi-stage)
- `.dockerignore` - Archivos a ignorar en la imagen
- `docker-compose.yml` - Stack local de desarrollo (MySQL + App)
- `DOCKER_DOKPLOY_GUIDE.md` - Esta guía

## 🚀 Opción 1: Ejecutar Localmente con Docker Compose

### Prerequisitos
- Docker Desktop instalado
- Puerto 3306 y 8081 disponibles

### Pasos
```bash
# 1. Clonar o navegar al proyecto
cd d:\Practicas\ 1.1\Practicas

# 2. Construir e iniciar los servicios
docker-compose up --build

# 3. La app estará disponible en: http://localhost:8081
# 4. MySQL en: localhost:3306
```

### Detener servicios
```bash
docker-compose down
```

## 🌐 Opción 2: Desplegar en Dokploy

### Prerequisitos
- Cuenta en [Dokploy](https://dokploy.com)
- Proyecto en GitHub o forma local

### Pasos en Dokploy

1. **Crear Nuevo Proyecto**
   - Dashboard → New Project
   - Conectar repositorio GitHub (o subir código)

2. **Crear Aplicación**
   - Project → New Application
   - Seleccionar tipo: `Docker`
   - Nombre: `civil-portal-api`

3. **Configurar Build**
   - Source: GitHub (recomendado)
   - Branch: `main` o tu rama
   - Dockerfile: `./Dockerfile`
   - Build Context: `.`

4. **Variables de Entorno**
   ```
   SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/civil_portal?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   SPRING_DATASOURCE_USERNAME=civil_app
   SPRING_DATASOURCE_PASSWORD=CivilApp@123
   SPRING_JPA_HIBERNATE_DDL_AUTO=update
   SPRING_PROFILES_ACTIVE=dev
   APP_JWT_SECRET=HOLA_AMIGOS_DE_LA_CARRERA_DE_INGENIERIA_DE_SISTEMAS_SEAN_BIENVENIDOS_AL_TOKENXD
   APP_JWT_EXPIRATION_MINUTES=60
   ```

5. **Servicios de Base de Datos**
   - Project → New Service
   - Tipo: `MySQL`
   - Version: `8.0`
   - Database: `civil_portal`
   - User: `civil_app`
   - Password: `CivilApp@123`

6. **Conectar Servicios**
   - En la app, agregar variable:
   ```
   SPRING_DATASOURCE_URL=jdbc:mysql://mysql-service:3306/civil_portal?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
   ```

7. **Configurar Puertos**
   - Container Port: `8081`
   - Public Port: `8081` (o el que prefiera)

8. **Deploy**
   - Click en "Deploy"
   - Monitorear logs
   - Acceder a la URL pública generada

## 📊 Comparativa: Local vs Dokploy

| Aspecto | Docker Compose | Dokploy |
|---------|-----------------|---------|
| Hosting | Local | Cloud |
| Costo | Gratis | Pago (depende plan) |
| Escalabilidad | Manual | Automática |
| CI/CD | Manual | Integrado |
| Backups | Manual | Automático |
| Monitoreo | Limitado | Completo |

## 🔧 Modificaciones para Producción

### 1. Variables Sensibles
En Dokploy, usar **Secrets** en lugar de vars simples:
- Database passwords
- JWT secret (generar aleatorio fuerte)
- API keys

### 2. Recursos
En `docker-compose.yml` agregar limits:
```yaml
app:
  deploy:
    resources:
      limits:
        cpus: '0.5'
        memory: 512M
      reservations:
        cpus: '0.25'
        memory: 256M
```

### 3. Logs y Monitoreo
```yaml
app:
  logging:
    driver: "json-file"
    options:
      max-size: "10m"
      max-file: "3"
```

## 🐛 Troubleshooting

### Error: "Connection refused to MySQL"
- Verificar que MySQL esté running: `docker ps`
- Usar nombre del servicio: `mysql` (en compose) o variable env correcta

### Error: "Port already in use"
- Cambiar puerto en `docker-compose.yml` o Dokploy
- Verificar qué usa los puertos:
  ```bash
  netstat -ano | findstr ":3306"  # Windows
  lsof -i :3306                   # Linux/Mac
  ```

### App no conecta a BD
- Verificar credenciales en vars de entorno
- Conectarse directo a MySQL:
  ```bash
  mysql -h localhost -u civil_app -p
  # password: CivilApp@123
  ```

## 📝 Checklist Pre-Deploy a Producción

- [ ] JWT Secret actualizado (fuerte y único)
- [ ] Database password cambiado
- [ ] SSL/HTTPS configurado
- [ ] Health checks activos
- [ ] Logs centralizados
- [ ] Backups de BD configurados
- [ ] Monitoreo y alertas activos
- [ ] Domain/DNS configurado
- [ ] Versión Java confirmada (Java 17)

## 📚 Referencias

- [Dokploy Docs](https://dokploy.com/docs)
- [Spring Boot Docker](https://spring.io/guides/topicals/spring-boot-docker)
- [Docker Compose Reference](https://docs.docker.com/compose/compose-file/)
