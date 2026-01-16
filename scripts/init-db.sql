-- ==========================================
-- INICIALIZACIÓN DE BD PARA PRODUCCIÓN
-- ==========================================
-- Ejecutar este script en Dokploy después de crear la BD

USE civil_portal;

-- ==========================================
-- 1. CREAR TABLA DE ROLES
-- ==========================================
CREATE TABLE IF NOT EXISTS roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==========================================
-- 2. INSERTAR ROLES POR DEFECTO
-- ==========================================
INSERT IGNORE INTO roles (nombre, descripcion) VALUES
('ADMIN', 'Administrador del sistema'),
('INGENIERO', 'Ingeniero civil con acceso completo'),
('SUPERVISOR', 'Supervisor de proyectos'),
('VISOR', 'Solo lectura de información');

-- ==========================================
-- 3. CREAR TABLA DE USUARIOS
-- ==========================================
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(60) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    nombres VARCHAR(120),
    apellidos VARCHAR(120),
    email VARCHAR(120),
    estado INT NOT NULL DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==========================================
-- 4. CREAR TABLA DE RELACIÓN USUARIO-ROL
-- ==========================================
CREATE TABLE IF NOT EXISTS usuario_roles (
    usuario_id BIGINT NOT NULL,
    rol_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (rol_id) REFERENCES roles(id) ON DELETE CASCADE,
    INDEX idx_rol_id (rol_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==========================================
-- 5. INSERTAR USUARIO ADMIN
-- ==========================================
-- Contraseña: admin123
-- Hash BCrypt: $2a$10$slYQmyNdGzin7olVlspK2OPST9/PgBkqquzi.Sm1yL6DyW9/LO23O
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

-- ==========================================
-- 6. ASIGNAR ROL ADMIN AL USUARIO
-- ==========================================
INSERT IGNORE INTO usuario_roles (usuario_id, rol_id)
SELECT u.id, r.id FROM usuarios u, roles r
WHERE u.username = 'admin' AND r.nombre = 'ADMIN';

-- ==========================================
-- 7. CREAR TABLA DE PERMISOS (OPCIONAL)
-- ==========================================
CREATE TABLE IF NOT EXISTS permisos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==========================================
-- 8. CREAR TABLA ROL-PERMISO
-- ==========================================
CREATE TABLE IF NOT EXISTS rol_permisos (
    rol_id BIGINT NOT NULL,
    permiso_id BIGINT NOT NULL,
    PRIMARY KEY (rol_id, permiso_id),
    FOREIGN KEY (rol_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (permiso_id) REFERENCES permisos(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ==========================================
-- VERIFICACIÓN
-- ==========================================
-- Ver roles creados
SELECT 'ROLES CREADOS:' as info;
SELECT id, nombre, descripcion FROM roles;

-- Ver usuario admin
SELECT '' as info;
SELECT 'USUARIO ADMIN CREADO:' as info;
SELECT id, username, nombres, apellidos, email, estado FROM usuarios WHERE username = 'admin';

-- Ver asignación de roles
SELECT '' as info;
SELECT 'ROLES ASIGNADOS:' as info;
SELECT u.id, u.username, r.nombre FROM usuarios u 
LEFT JOIN usuario_roles ur ON u.id = ur.usuario_id 
LEFT JOIN roles r ON ur.rol_id = r.id 
WHERE u.username = 'admin';
