# 🎯 PASOS EXACTOS PARA DESPLEGAR EN DOKPLOY

## 📌 Prerequisitos
- ✅ VPS comprado con Dokploy instalado
- ✅ Base de datos MySQL creada en Dokploy
- ✅ Repositorio GitHub del proyecto (público o privado con acceso)

---

## 🚀 FASE 1: ACTUALIZAR CÓDIGO LOCALMENTE (10 min)

### 1.1 Descargar cambios desde GitHub
```powershell
cd "d:\Practicas 1.1\Practicas"
git pull origin main
```

### 1.2 Verificar que los archivos se actualizaron
```powershell
# Ver Dockerfile actualizado
type Dockerfile

# Ver application-prod.properties
type src\main\resources\application-prod.properties

# Ver script de BD
type scripts\init-db.sql
```

### 1.3 Commit y push de cambios si hiciste ajustes
```powershell
git add .
git commit -m "Production configuration for Dokploy deployment"
git push origin main
```

---

## 🎛️ FASE 2: CONFIGURAR EN DOKPLOY (15 min)

### 2.1 Ir a Dokploy Dashboard
1. Abre tu Dokploy en el navegador (ej: `https://tu-vps.com/dokploy`)
2. Login con tu cuenta

### 2.2 Crear/Configurar Aplicación
1. **Dashboard** → Click en **"New Application"** (o selecciona una existente)
2. Configura:
   - **Name**: `CivilPortal-API`
   - **Type**: Docker
   - **Repository**: Selecciona tu repositorio GitHub
   - **Branch**: main
   - **Dockerfile path**: `./Dockerfile`

### 2.3 Configurar Variables de Entorno ⭐ CRÍTICO
En **Settings** → **Environment Variables**, agregar:

```
# Base de Datos
DB_HOST=<nombre-exacto-del-servicio-mysql>
DB_NAME=civil_portal
DB_USER=civil_app
DB_PASSWORD=<contraseña-que-pusiste>

# JWT (GENERAR ESTO)
JWT_SECRET=<generar-con-comando-abajo>

# Spring
SPRING_PROFILES_ACTIVE=prod
SPRING_APPLICATION_NAME=CivilPortal-API
SERVER_PORT=8081
```

#### ⚠️ Generar JWT_SECRET Seguro

En tu PowerShell local, ejecuta:
```powershell
# Generar un secreto aleatorio seguro
$bytes = [System.Security.Cryptography.RandomNumberGenerator]::new().GetBytes(32)
$secret = [Convert]::ToBase64String($bytes)
Write-Host "JWT_SECRET=$secret"
```

Copia ese valor y pégalo en `JWT_SECRET` en Dokploy.

#### 📌 ¿Dónde obtener DB_HOST?

1. En Dokploy, ve a **Services** → **MySQL** (tu servicio)
2. Busca el campo **Hostname** o **Internal Hostname**
3. Debería ser algo como: `mysql-service` o `mysql-prod-db`
4. Ese es tu `DB_HOST`

### 2.4 Configurar Puertos y Networking
1. **Port Mapping**:
   - Container Port: `8081`
   - External Port: `8081` (o el que prefieras)
   - Protocol: TCP

2. **Networks**: 
   - Asegúrate que la app y MySQL están en la misma network

### 2.5 Configurar Health Check
1. En **Settings** → **Health Check**:
   - Endpoint: `http://localhost:8081/api/auth/ping`
   - Interval: 30 seconds
   - Timeout: 10 seconds

---

## 🗄️ FASE 3: INICIALIZAR BASE DE DATOS (10 min)

### 3.1 Conectarse a MySQL en Dokploy

**Opción A: Desde Dokploy UI (más fácil)**
1. Ve a **Services** → **MySQL**
2. Click en **"Connect"** o **"Terminal"**
3. Usa el cliente MySQL integrado

**Opción B: Desde PowerShell (si tienes acceso SSH)**
```powershell
# Si tienes MySQL instalado localmente
mysql -h tu-vps-ip -P 3306 -u civil_app -p civil_portal

# Cuando pida password, ingresa la que configuraste
```

### 3.2 Ejecutar Script de Inicialización

Una vez conectado a MySQL, ejecuta este script completo:

```sql
USE civil_portal;

-- Roles
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT IGNORE INTO roles (nombre, descripcion) VALUES
('ADMIN', 'Administrador del sistema'),
('INGENIERO', 'Ingeniero civil'),
('SUPERVISOR', 'Supervisor'),
('VISOR', 'Solo lectura');

-- Usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(60) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    nombres VARCHAR(120),
    apellidos VARCHAR(120),
    email VARCHAR(120),
    estado INT NOT NULL DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Relación Usuario-Rol
CREATE TABLE IF NOT EXISTS usuario_roles (
    usuario_id BIGINT NOT NULL,
    rol_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (rol_id) REFERENCES roles(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Crear usuario ADMIN
-- Contraseña: admin123 (bcrypt hash)
INSERT IGNORE INTO usuarios (username, password_hash, nombres, apellidos, email, estado)
VALUES (
    'admin',
    '$2a$10$slYQmyNdGzin7olVlspK2OPST9/PgBkqquzi.Sm1yL6DyW9/LO23O',
    'Administrador',
    'Sistema',
    'admin@civil.portal',
    1
);

-- Asignar rol ADMIN
INSERT IGNORE INTO usuario_roles (usuario_id, rol_id)
SELECT u.id, r.id FROM usuarios u, roles r
WHERE u.username = 'admin' AND r.nombre = 'ADMIN';

-- Verificar
SELECT 'ROLES:' as info;
SELECT * FROM roles;
SELECT 'USUARIO ADMIN:' as info;
SELECT username, nombres, email FROM usuarios;
SELECT 'ASIGNACIONES:' as info;
SELECT u.username, r.nombre FROM usuarios u JOIN usuario_roles ur ON u.id = ur.usuario_id JOIN roles r ON ur.rol_id = r.id;
```

### 3.3 Verificar que se creó correctamente

Deberías ver output como:
```
ROLES:
id | nombre    | descripcion
1  | ADMIN     | ...
2  | INGENIERO | ...

USUARIO ADMIN:
username | nombres           | email
admin    | Administrador     | admin@civil.portal

ASIGNACIONES:
username | nombre
admin    | ADMIN
```

---

## 🚀 FASE 4: DESPLEGAR LA APLICACIÓN (5-10 min)

### 4.1 En Dokploy Dashboard

1. Ve a tu **Aplicación**
2. Click en **"Deploy"** o **"Rebuild and Deploy"**
3. Observa el progreso en **Logs**

### 4.2 Monitorear el Despliegue

En la sección **Logs**, deberías ver:
```
[Build Stage]
Sending build context...
Building image...
maven build started...
maven build successful...

[Push Stage]
Pushing image to registry...

[Container Stage]
Starting container...
Container started successfully

[Log Output]
Tomcat started on port 8081
Application 'CivilPortal-API' started successfully
```

### 4.3 Esperar hasta que esté en estado "Running"

- Status debe mostrar: ✅ **Running**
- Logs deben mostrar la app activa
- Health check debe pasar

---

## ✅ FASE 5: VALIDAR QUE FUNCIONA (10 min)

### 5.1 Obtener URL de tu Aplicación

En Dokploy, tu app tiene una URL pública:
- `https://tu-vps-ip:8081` o
- `https://subdominio.tu-dominio.com` (si tienes DNS configurado)

### 5.2 Pruebas desde PowerShell

```powershell
# 1. Health Check
curl -X GET "https://tu-vps-ip:8081/api/auth/ping" `
  -Headers @{"Content-Type"="application/json"}

# Respuesta esperada:
# {"success":true,"message":"Pong","data":"API activa"}

# 2. Login
$loginBody = @{
    username = "admin"
    password = "admin123"
} | ConvertTo-Json

$response = curl -X POST "https://tu-vps-ip:8081/api/auth/login" `
  -Headers @{"Content-Type"="application/json"} `
  -Body $loginBody

$token = ($response | ConvertFrom-Json).data.token
Write-Host "Token recibido: $token"

# 3. Usar token para acceder a endpoint protegido
curl -X GET "https://tu-vps-ip:8081/api/usuarios" `
  -Headers @{
    "Content-Type"="application/json"
    "Authorization"="Bearer $token"
  }
```

### 5.3 Pruebas desde Postman

1. Importa [CivilPortal_Postman_Collection.json](../CivilPortal_Postman_Collection.json)
2. Cambia la variable `baseUrl` a tu VPS
3. Ejecuta pruebas

---

## 🔍 TROUBLESHOOTING

| Problema | Causa | Solución |
|----------|-------|----------|
| Build fails | Dockerfile incorrecto | Verifica que el Dockerfile está actualizado |
| "Connection refused" | BD no conecta | Verificar DB_HOST, credenciales, BD corriendo |
| JWT error | JWT_SECRET no configurado | Agregar JWT_SECRET en Environment Variables |
| "403 Forbidden" | Token inválido/expirado | Verificar token, regenerar con login |
| Container exits | Error en la app | Ver logs, buscar "Exception" |
| "Port in use" | Puerto ocupado | Cambiar puerto o liberar el actual |

---

## 📊 MONITOREO POST-DESPLIEGUE

### Configurar Alertas en Dokploy
1. **Settings** → **Alerts**
2. Configurar notificaciones por:
   - CPU > 80%
   - Memory > 80%
   - Container crash
   - Build failure

### Revisar Logs Regularmente
1. **Logs** → Scroll para ver última actividad
2. Buscar errores de BD o autenticación
3. Monitorear uso de memoria

### Backups de BD
1. En tu servicio MySQL, configurar backups automáticos
2. Guardar en storage externo si es posible

---

## ✨ CHECKLIST FINAL

- [ ] Código actualizado localmente
- [ ] Push a GitHub exitoso
- [ ] Variables de entorno configuradas en Dokploy
- [ ] BD MySQL corriendo y accesible
- [ ] Script init-db.sql ejecutado
- [ ] Usuario admin creado
- [ ] Aplicación desplegada exitosamente
- [ ] Health check pasando
- [ ] Login funciona
- [ ] JWT válido y funciona
- [ ] Endpoints protegidos accesibles con token
- [ ] Logs monitoreados

---

## 🆘 SOPORTE

Si algo falla:
1. Revisa los **Logs** en Dokploy
2. Busca el **error exacto**
3. Verifica **credenciales de BD**
4. Confirma **JWT_SECRET configurado**
5. Prueba **conectividad entre servicios**

¿Problema específico? Comparte el error exacto de los logs.
