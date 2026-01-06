-- Script DDL para STD.io - Sistema de Gestión Académica
-- Generado basándose en las entidades JPA del proyecto
-- Base de datos: MySQL 8.0

SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- TABLA: estudiantes (RF-01)
-- ============================================
CREATE TABLE IF NOT EXISTS estudiantes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cedula VARCHAR(10) NOT NULL UNIQUE,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    fecha_nacimiento DATE,
    email VARCHAR(100) UNIQUE,
    telefono VARCHAR(15),
    direccion VARCHAR(200)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- TABLA: docentes (RF-02)
-- ============================================
CREATE TABLE IF NOT EXISTS docentes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cedula VARCHAR(10) NOT NULL UNIQUE,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE,
    telefono VARCHAR(15),
    especialidad VARCHAR(100),
    titulo_academico VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- TABLA: periodos (RF-03)
-- ============================================
CREATE TABLE IF NOT EXISTS periodos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    activo BOOLEAN DEFAULT FALSE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- TABLA: cursos (RF-04)
-- ============================================
CREATE TABLE IF NOT EXISTS cursos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(10) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    creditos INT NOT NULL,
    horas_por_semana INT,
    descripcion VARCHAR(500)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- TABLA: asignaciones (RF-05)
-- ============================================
CREATE TABLE IF NOT EXISTS asignaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    curso_id BIGINT NOT NULL,
    docente_id BIGINT NOT NULL,
    periodo_id BIGINT NOT NULL,
    seccion VARCHAR(10),
    cupo_maximo INT,
    horario_inicio TIME,
    horario_fin TIME,
    dias_semana VARCHAR(100),
    FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE RESTRICT,
    FOREIGN KEY (docente_id) REFERENCES docentes(id) ON DELETE RESTRICT,
    FOREIGN KEY (periodo_id) REFERENCES periodos(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- TABLA: matriculas (RF-06)
-- ============================================
CREATE TABLE IF NOT EXISTS matriculas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    estudiante_id BIGINT NOT NULL,
    asignacion_id BIGINT NOT NULL,
    fecha_matricula DATE,
    estado VARCHAR(20) DEFAULT 'ACTIVA',
    UNIQUE KEY unique_matricula (estudiante_id, asignacion_id),
    FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id) ON DELETE RESTRICT,
    FOREIGN KEY (asignacion_id) REFERENCES asignaciones(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- TABLA: rubros (RF-07)
-- ============================================
CREATE TABLE IF NOT EXISTS rubros (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    asignacion_id BIGINT NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    ponderacion DECIMAL(5,2) NOT NULL,
    descripcion VARCHAR(200),
    FOREIGN KEY (asignacion_id) REFERENCES asignaciones(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- TABLA: calificaciones (RF-08)
-- ============================================
CREATE TABLE IF NOT EXISTS calificaciones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    matricula_id BIGINT NOT NULL,
    rubro_id BIGINT NOT NULL,
    nota DECIMAL(5,2) NOT NULL,
    fecha_registro DATE,
    FOREIGN KEY (matricula_id) REFERENCES matriculas(id) ON DELETE RESTRICT,
    FOREIGN KEY (rubro_id) REFERENCES rubros(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- TABLA: asistencias (RF-09)
-- ============================================
CREATE TABLE IF NOT EXISTS asistencias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    matricula_id BIGINT NOT NULL,
    fecha DATE NOT NULL,
    estado VARCHAR(20) NOT NULL,
    observaciones VARCHAR(200),
    UNIQUE KEY unique_asistencia (matricula_id, fecha),
    FOREIGN KEY (matricula_id) REFERENCES matriculas(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================
-- TABLA: auditlog (RNF-06)
-- ============================================
CREATE TABLE IF NOT EXISTS auditlog (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(100),
    entidad VARCHAR(50),
    entidad_id BIGINT,
    accion VARCHAR(20),
    fecha DATETIME,
    cambios VARCHAR(2000),
    ip VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- VERIFICACIÓN
-- ============================================
SHOW TABLES;
