-- Script de Configuración de Roles Naviox
-- STD.io - Sistema de Gestión Académica
-- Cumple RNF-05: Autorización RBAC con 3 roles

-- ADVERTENCIA: Este script es una aproximación basada en la estructura típica de Naviox.
-- La forma RECOMENDADA de configurar roles es desde la interfaz web de Naviox.
-- Ejecutar SOLO si conoces la estructura exacta de las tablas de Naviox en tu instalación.

-- ============================================
-- PASO 1: Crear Usuarios de Prueba
-- ============================================
-- Nota: Las contraseñas deben estar hasheadas. El hash mostrado es aproximado.
-- Usuario: admin_test / Contraseña: Admin123!
-- Usuario: academico_test / Contraseña: Acad123!
-- Usuario: docente_test / Contraseña: Doc123!

INSERT INTO naviox_user (username, email, password, active) VALUES
('admin_test', 'admin@stdio.edu.ec', 'HASH_PLACEHOLDER', 1),
('academico_test', 'academico@stdio.edu.ec', 'HASH_PLACEHOLDER', 1),
('docente_test', 'docente1@stdio.edu.ec', 'HASH_PLACEHOLDER', 1);

-- ============================================
-- PASO 2: Crear los 3 Roles
-- ============================================
INSERT INTO naviox_role (name, description) VALUES
('Administrador', 'Acceso total al sistema. Gestión de usuarios y configuración global.'),
('Academico', 'Coordinador académico. Define oferta académica y gestiona matrículas.'),
('Docente', 'Usuario operativo de aula. Gestiona evaluaciones, calificaciones y asistencia.');
-- ============================================
-- PASO 3: Asignar Roles a Usuarios
-- ============================================
-- Asume que los IDs generados son 1, 2, 3 (verificar con SELECT antes de ejecutar)
SET @rol_admin = (SELECT id FROM naviox_role WHERE name = 'Administrador');
SET @rol_academico = (SELECT id FROM naviox_role WHERE name = 'Academico');
SET @rol_docente = (SELECT id FROM naviox_role WHERE name = 'Docente');

SET @usuario_admin = (SELECT id FROM naviox_user WHERE username = 'admin_test');
SET @usuario_academico = (SELECT id FROM naviox_user WHERE username = 'academico_test');
SET @usuario_docente = (SELECT id FROM naviox_user WHERE username = 'docente_test');

INSERT INTO naviox_user_role (user_id, role_id) VALUES
(@usuario_admin, @rol_admin),
(@usuario_academico, @rol_academico),
(@usuario_docente, @rol_docente);

-- ============================================
-- PASO 4: Configurar Permisos por Módulo
-- ============================================
-- Formato aproximado (verificar estructura real de naviox_permission)

-- ADMINISTRADOR: CRUD completo en todos los módulos
INSERT INTO naviox_permission (role_id, module_name, can_create, can_read, can_update, can_delete) VALUES
(@rol_admin, 'Estudiante', 1, 1, 1, 1),
(@rol_admin, 'Docente', 1, 1, 1, 1),
(@rol_admin, 'Periodo', 1, 1, 1, 1),
(@rol_admin, 'Curso', 1, 1, 1, 1),
(@rol_admin, 'Asignacion', 1, 1, 1, 1),
(@rol_admin, 'Matricula', 1, 1, 1, 1),
(@rol_admin, 'Rubro', 1, 1, 1, 1),
(@rol_admin, 'Calificacion', 1, 1, 1, 1),
(@rol_admin, 'Asistencia', 1, 1, 1, 1),
(@rol_admin, 'AuditLog', 1, 1, 1, 1);

-- ACADÉMICO: CRUD en casi todo, excepto modificar docentes
INSERT INTO naviox_permission (role_id, module_name, can_create, can_read, can_update, can_delete) VALUES
(@rol_academico, 'Estudiante', 1, 1, 1, 1),
(@rol_academico, 'Docente', 1, 1, 0, 0),  -- Solo crear y leer
(@rol_academico, 'Periodo', 1, 1, 1, 1),
(@rol_academico, 'Curso', 1, 1, 1, 1),
(@rol_academico, 'Asignacion', 1, 1, 1, 1),
(@rol_academico, 'Matricula', 1, 1, 1, 1),
(@rol_academico, 'Rubro', 1, 1, 1, 1),
(@rol_academico, 'Calificacion', 1, 1, 1, 1),
(@rol_academico, 'Asistencia', 1, 1, 1, 1),
(@rol_academico, 'AuditLog', 0, 1, 0, 0);  -- Solo lectura de auditoría

-- DOCENTE: Solo lectura en maestros, gestión de aula
INSERT INTO naviox_permission (role_id, module_name, can_create, can_read, can_update, can_delete) VALUES
(@rol_docente, 'Estudiante', 0, 1, 0, 0),  -- Solo lectura
(@rol_docente, 'Docente', 0, 1, 0, 0),     -- Solo lectura
(@rol_docente, 'Periodo', 0, 1, 0, 0),     -- Solo lectura
(@rol_docente, 'Curso', 0, 1, 0, 0),       -- Solo lectura
(@rol_docente, 'Asignacion', 0, 1, 0, 0),  -- Solo lectura (ver su carga)
(@rol_docente, 'Matricula', 0, 1, 0, 0),   -- Solo lectura
(@rol_docente, 'Rubro', 1, 1, 1, 0),       -- Configurar sus rubros (sin eliminar)
(@rol_docente, 'Calificacion', 1, 1, 1, 0),-- Registrar sus notas
(@rol_docente, 'Asistencia', 1, 1, 1, 0),  -- Tomar asistencia
(@rol_docente, 'AuditLog', 0, 0, 0, 0);    -- Sin acceso

-- ============================================
-- VERIFICACIÓN
-- ============================================
-- Ejecutar estas consultas para verificar:

-- Ver todos los roles
SELECT * FROM naviox_role;

-- Ver usuarios y sus roles
SELECT 
    u.username,
    r.name as rol
FROM naviox_user u
JOIN naviox_user_role ur ON u.id = ur.user_id
JOIN naviox_role r ON ur.role_id = r.id;

-- Ver permisos del rol Docente
SELECT 
    module_name,
    can_create as C,
    can_read as R,
    can_update as U,
    can_delete as D
FROM naviox_permission
WHERE role_id = @rol_docente
ORDER BY module_name;

-- ============================================
-- NOTAS IMPORTANTES
-- ============================================
/*
1. ESTRUCTURA DESCONOCIDA:
   Este script asume nombres de tablas típicos de Naviox, pero pueden variar.
   DEBES VERIFICAR la estructura real ejecutando:
   SHOW TABLES LIKE 'naviox%';
   DESCRIBE naviox_user;
   DESCRIBE naviox_role;
   DESCRIBE naviox_permission;

2. CONTRASEÑAS:
   Las contraseñas deben ser hasheadas con el algoritmo que use Naviox (usualmente BCrypt).
   NO USES 'HASH_PLACEHOLDER' en producción.
   
   Para generar hashes BCrypt en MySQL:
   -- Requiere función personalizada o hacerlo desde la aplicación

3. MÉTODO RECOMENDADO:
   La forma OFICIAL y SEGURA de configurar roles en Naviox es:
   
   a) Acceder a http://localhost:8080/stud_io2
   b) Login como admin/admin
   c) Ir a: Administración → Seguridad → Roles
   d) Crear los 3 roles manualmente
   e) Asignar permisos desde la interfaz
   
   Este script SQL es solo para automatización avanzada.

4. FILTRADO POR USUARIO AUTENTICADO:
   Para que un Docente solo vea SUS PROPIAS secciones, se requiere:
   - Modificar las consultas JPQL en las entidades
   - Agregar @Condition en OpenXava
   - O implementar filtros personalizados
   
   Esto NO se hace con permisos de Naviox, sino con lógica de aplicación.

5. ALTERNATIVA SIN SCRIPT SQL:
   Si no conoces la estructura de Naviox, sigue el manual en:
   MANUAL_CONFIGURACION_NAVIOX.md
*/
