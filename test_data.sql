-- Script de Datos de Prueba para STD.io
-- Validación y Verificación de Software 2025

-- ============================================
-- 1. LIMPIEZA (opcional, solo si quieres empezar de cero)
-- ============================================
-- DELETE FROM asistencias;
-- DELETE FROM calificaciones;
-- DELETE FROM rubros;
-- DELETE FROM matriculas;
-- DELETE FROM asignaciones;
-- DELETE FROM cursos;
-- DELETE FROM periodos;
-- DELETE FROM docentes;
-- DELETE FROM estudiantes;

-- ============================================
-- 2. ESTUDIANTES
-- ============================================
INSERT INTO estudiantes (cedula, nombre, apellido, fecha_nacimiento, email, telefono, direccion) VALUES
('1234567890', 'Juan', 'Pérez', '2000-01-15', 'juan.perez@estudiantes.edu.ec', '0991234567', 'Av. 10 de Agosto y Patria'),
('0987654321', 'María', 'González', '1999-05-20', 'maria.gonzalez@estudiantes.edu.ec', '0987654321', 'Calle Colón 123'),
('1122334455', 'Carlos', 'Ramírez', '2001-03-10', 'carlos.ramirez@estudiantes.edu.ec', '0991122334', 'Av. Amazonas Norte'),
('2233445566', 'Ana', 'López', '2000-07-25', 'ana.lopez@estudiantes.edu.ec', '0982233445', 'Sector La Carolina'),
('3344556677', 'Luis', 'Martínez', '1999-11-30', 'luis.martinez@estudiantes.edu.ec', '0993344556', 'Calle García Moreno'),
('4455667788', 'Sofía', 'Torres', '2001-02-14', 'sofia.torres@estudiantes.edu.ec', '0984455667', 'Av. Naciones Unidas'),
('5566778899', 'Diego', 'Flores', '2000-09-08', 'diego.flores@estudiantes.edu.ec', '0995566778', 'Sector Quicentro'),
('6677889900', 'Valentina', 'Morales', '1999-12-22', 'valentina.morales@estudiantes.edu.ec', '0986677889', 'Av. 6 de Diciembre');

-- ============================================
-- 3. DOCENTES
-- ============================================
INSERT INTO docentes (cedula, nombre, apellido, email, especialidad, titulo_academico, telefono) VALUES
('1700000001', 'Roberto', 'Silva', 'roberto.silva@profesores.edu.ec', 'Matemáticas', 'PhD en Matemáticas Aplicadas', '0991000001'),
('1700000002', 'Patricia', 'Vásquez', 'patricia.vasquez@profesores.edu.ec', 'Programación', 'MSc en Ciencias de la Computación', '0991000002'),
('1700000003', 'Fernando', 'Castro', 'fernando.castro@profesores.edu.ec', 'Bases de Datos', 'PhD en Ingeniería de Software', '0991000003'),
('1700000004', 'Gabriela', 'Ruiz', 'gabriela.ruiz@profesores.edu.ec', 'Física', 'MSc en Física Teórica', '0991000004'),
('1700000005', 'Andrés', 'Mendoza', 'andres.mendoza@profesores.edu.ec', 'Estadística', 'PhD en Estadística', '0991000005');

-- ============================================
-- 4. PERIODO ACADÉMICO
-- ============================================
INSERT INTO periodos (nombre, fecha_inicio, fecha_fin, activo) VALUES
('2025-A', '2025-01-15', '2025-06-30', TRUE);

-- ============================================
-- 5. CURSOS
-- ============================================
INSERT INTO cursos (codigo, nombre, creditos, horas_por_semana, descripcion) VALUES
('MAT101', 'Cálculo I', 4, 6, 'Fundamentos de cálculo diferencial e integral'),
('PRG101', 'Programación I', 5, 8, 'Introducción a la programación con Java'),
('BDD201', 'Bases de Datos', 4, 6, 'Diseño y gestión de bases de datos relacionales'),
('FIS101', 'Física I', 4, 6, 'Mecánica clásica y termodinámica'),
('EST201', 'Estadística', 3, 4, 'Estadística descriptiva e inferencial'),
('MAT201', 'Álgebra Lineal', 4, 6, 'Vectores, matrices y transformaciones lineales');

-- ============================================
-- 6. ASIGNACIONES (Curso + Docente + Periodo)
-- ============================================
-- Obtener el ID del periodo activo
SET @periodo_id = (SELECT id FROM periodos WHERE activo = TRUE LIMIT 1);

-- Obtener IDs de cursos
SET @curso_mat101 = (SELECT id FROM cursos WHERE codigo = 'MAT101');
SET @curso_prg101 = (SELECT id FROM cursos WHERE codigo = 'PRG101');
SET @curso_bdd201 = (SELECT id FROM cursos WHERE codigo = 'BDD201');
SET @curso_fis101 = (SELECT id FROM cursos WHERE codigo = 'FIS101');
SET @curso_est201 = (SELECT id FROM cursos WHERE codigo = 'EST201');
SET @curso_mat201 = (SELECT id FROM cursos WHERE codigo = 'MAT201');

-- Obtener IDs de docentes
SET @docente_silva = (SELECT id FROM docentes WHERE cedula = '1700000001');
SET @docente_vasquez = (SELECT id FROM docentes WHERE cedula = '1700000002');
SET @docente_castro = (SELECT id FROM docentes WHERE cedula = '1700000003');
SET @docente_ruiz = (SELECT id FROM docentes WHERE cedula = '1700000004');
SET @docente_mendoza = (SELECT id FROM docentes WHERE cedula = '1700000005');

INSERT INTO asignaciones (curso_id, docente_id, periodo_id, seccion, cupo_maximo, horario_inicio, horario_fin, dias_semana) VALUES
(@curso_mat101, @docente_silva, @periodo_id, 'A', 30, '08:00:00', '10:00:00', 'Lunes,Miércoles,Viernes'),
(@curso_prg101, @docente_vasquez, @periodo_id, 'A', 25, '10:00:00', '12:00:00', 'Martes,Jueves'),
(@curso_bdd201, @docente_castro, @periodo_id, 'A', 28, '14:00:00', '16:00:00', 'Lunes,Miércoles'),
(@curso_fis101, @docente_ruiz, @periodo_id, 'A', 30, '08:00:00', '10:00:00', 'Martes,Jueves'),
(@curso_est201, @docente_mendoza, @periodo_id, 'A', 25, '16:00:00', '18:00:00', 'Viernes'),
(@curso_mat201, @docente_silva, @periodo_id, 'B', 20, '14:00:00', '16:00:00', 'Martes,Jueves');

-- ============================================
-- 7. MATRÍCULAS
-- ============================================
-- Obtener IDs de estudiantes
SET @est_juan = (SELECT id FROM estudiantes WHERE cedula = '1234567890');
SET @est_maria = (SELECT id FROM estudiantes WHERE cedula = '0987654321');
SET @est_carlos = (SELECT id FROM estudiantes WHERE cedula = '1122334455');
SET @est_ana = (SELECT id FROM estudiantes WHERE cedula = '2233445566');
SET @est_luis = (SELECT id FROM estudiantes WHERE cedula = '3344556677');
SET @est_sofia = (SELECT id FROM estudiantes WHERE cedula = '4455667788');

-- Obtener IDs de asignaciones
SET @asig_mat101 = (SELECT id FROM asignaciones WHERE curso_id = @curso_mat101 AND seccion = 'A');
SET @asig_prg101 = (SELECT id FROM asignaciones WHERE curso_id = @curso_prg101 AND seccion = 'A');
SET @asig_bdd201 = (SELECT id FROM asignaciones WHERE curso_id = @curso_bdd201 AND seccion = 'A');
SET @asig_fis101 = (SELECT id FROM asignaciones WHERE curso_id = @curso_fis101 AND seccion = 'A');
SET @asig_est201 = (SELECT id FROM asignaciones WHERE curso_id = @curso_est201 AND seccion = 'A');

INSERT INTO matriculas (estudiante_id, asignacion_id, fecha_matricula, estado) VALUES
-- Juan Pérez: 4 materias
(@est_juan, @asig_mat101, '2025-01-10', 'ACTIVA'),
(@est_juan, @asig_prg101, '2025-01-10', 'ACTIVA'),
(@est_juan, @asig_bdd201, '2025-01-10', 'ACTIVA'),
(@est_juan, @asig_fis101, '2025-01-10', 'ACTIVA'),

-- María González: 3 materias
(@est_maria, @asig_mat101, '2025-01-11', 'ACTIVA'),
(@est_maria, @asig_prg101, '2025-01-11', 'ACTIVA'),
(@est_maria, @asig_est201, '2025-01-11', 'ACTIVA'),

-- Carlos Ramírez: 4 materias
(@est_carlos, @asig_mat101, '2025-01-12', 'ACTIVA'),
(@est_carlos, @asig_bdd201, '2025-01-12', 'ACTIVA'),
(@est_carlos, @asig_fis101, '2025-01-12', 'ACTIVA'),
(@est_carlos, @asig_est201, '2025-01-12', 'ACTIVA'),

-- Ana López: 3 materias
(@est_ana, @asig_prg101, '2025-01-13', 'ACTIVA'),
(@est_ana, @asig_bdd201, '2025-01-13', 'ACTIVA'),
(@est_ana, @asig_fis101, '2025-01-13', 'ACTIVA'),

-- Luis Martínez: 2 materias
(@est_luis, @asig_mat101, '2025-01-14', 'ACTIVA'),
(@est_luis, @asig_prg101, '2025-01-14', 'ACTIVA'),

-- Sofía Torres: 3 materias
(@est_sofia, @asig_bdd201, '2025-01-15', 'ACTIVA'),
(@est_sofia, @asig_fis101, '2025-01-15', 'ACTIVA'),
(@est_sofia, @asig_est201, '2025-01-15', 'ACTIVA');

-- ============================================
-- 8. RUBROS (Componentes de Evaluación)
-- ============================================
-- IMPORTANTE: Cada asignación debe sumar exactamente 100%

-- Cálculo I (MAT101-A): 100%
INSERT INTO rubros (asignacion_id, nombre, ponderacion, descripcion) VALUES
(@asig_mat101, 'Exámenes', 40.00, 'Dos exámenes parciales'),
(@asig_mat101, 'Deberes', 30.00, 'Tareas semanales'),
(@asig_mat101, 'Proyecto Final', 20.00, 'Proyecto integrador'),
(@asig_mat101, 'Participación', 10.00, 'Asistencia y participación en clase');

-- Programación I (PRG101-A): 100%
INSERT INTO rubros (asignacion_id, nombre, ponderacion, descripcion) VALUES
(@asig_prg101, 'Examen Parcial 1', 25.00, 'Primer parcial'),
(@asig_prg101, 'Examen Parcial 2', 25.00, 'Segundo parcial'),
(@asig_prg101, 'Proyectos', 35.00, 'Tres proyectos de programación'),
(@asig_prg101, 'Laboratorios', 15.00, 'Prácticas de laboratorio');

-- Bases de Datos (BDD201-A): 100%
INSERT INTO rubros (asignacion_id, nombre, ponderacion, descripcion) VALUES
(@asig_bdd201, 'Exámenes', 50.00, 'Exámenes teórico-prácticos'),
(@asig_bdd201, 'Proyecto BD', 35.00, 'Diseño e implementación de base de datos'),
(@asig_bdd201, 'Talleres', 15.00, 'Talleres de SQL');

-- Física I (FIS101-A): 100%
INSERT INTO rubros (asignacion_id, nombre, ponderacion, descripcion) VALUES
(@asig_fis101, 'Examen Final', 40.00, 'Examen acumulativo'),
(@asig_fis101, 'Laboratorios', 30.00, 'Prácticas de laboratorio'),
(@asig_fis101, 'Deberes', 20.00, 'Problemas semanales'),
(@asig_fis101, 'Quizzes', 10.00, 'Evaluaciones cortas');

-- Estadística (EST201-A): 100%
INSERT INTO rubros (asignacion_id, nombre, ponderacion, descripcion) VALUES
(@asig_est201, 'Exámenes', 60.00, 'Dos exámenes parciales'),
(@asig_est201, 'Proyecto Estadístico', 25.00, 'Análisis de datos reales'),
(@asig_est201, 'Tareas', 15.00, 'Ejercicios prácticos');

-- ============================================
-- 9. CALIFICACIONES
-- ============================================
-- Obtener IDs de matrículas
SET @mat_juan_mat101 = (SELECT id FROM matriculas WHERE estudiante_id = @est_juan AND asignacion_id = @asig_mat101);
SET @mat_juan_prg101 = (SELECT id FROM matriculas WHERE estudiante_id = @est_juan AND asignacion_id = @asig_prg101);
SET @mat_maria_mat101 = (SELECT id FROM matriculas WHERE estudiante_id = @est_maria AND asignacion_id = @asig_mat101);
SET @mat_maria_prg101 = (SELECT id FROM matriculas WHERE estudiante_id = @est_maria AND asignacion_id = @asig_prg101);
SET @mat_carlos_bdd201 = (SELECT id FROM matriculas WHERE estudiante_id = @est_carlos AND asignacion_id = @asig_bdd201);
SET @mat_ana_fis101 = (SELECT id FROM matriculas WHERE estudiante_id = @est_ana AND asignacion_id = @asig_fis101);

-- Obtener IDs de rubros
SET @rubro_mat_examenes = (SELECT id FROM rubros WHERE asignacion_id = @asig_mat101 AND nombre = 'Exámenes');
SET @rubro_mat_deberes = (SELECT id FROM rubros WHERE asignacion_id = @asig_mat101 AND nombre = 'Deberes');
SET @rubro_mat_proyecto = (SELECT id FROM rubros WHERE asignacion_id = @asig_mat101 AND nombre = 'Proyecto Final');
SET @rubro_mat_participacion = (SELECT id FROM rubros WHERE asignacion_id = @asig_mat101 AND nombre = 'Participación');

SET @rubro_prg_parc1 = (SELECT id FROM rubros WHERE asignacion_id = @asig_prg101 AND nombre = 'Examen Parcial 1');
SET @rubro_prg_parc2 = (SELECT id FROM rubros WHERE asignacion_id = @asig_prg101 AND nombre = 'Examen Parcial 2');
SET @rubro_prg_proyectos = (SELECT id FROM rubros WHERE asignacion_id = @asig_prg101 AND nombre = 'Proyectos');
SET @rubro_prg_labs = (SELECT id FROM rubros WHERE asignacion_id = @asig_prg101 AND nombre = 'Laboratorios');

-- Calificaciones de Juan en Cálculo I (promedio esperado: ~78.5)
INSERT INTO calificaciones (matricula_id, rubro_id, nota, fecha_registro) VALUES
(@mat_juan_mat101, @rubro_mat_examenes, 85.00, '2025-03-15'),      -- 40% * 85 = 34.0
(@mat_juan_mat101, @rubro_mat_deberes, 90.00, '2025-04-20'),        -- 30% * 90 = 27.0
(@mat_juan_mat101, @rubro_mat_proyecto, 75.00, '2025-06-10'),       -- 20% * 75 = 15.0
(@mat_juan_mat101, @rubro_mat_participacion, 95.00, '2025-06-15');  -- 10% * 95 = 9.5
                                                                    -- TOTAL = 85.5 (APROBADO)

-- Calificaciones de Juan en Programación I (promedio: ~82.25)
INSERT INTO calificaciones (matricula_id, rubro_id, nota, fecha_registro) VALUES
(@mat_juan_prg101, @rubro_prg_parc1, 88.00, '2025-03-10'),
(@mat_juan_prg101, @rubro_prg_parc2, 82.00, '2025-05-15'),
(@mat_juan_prg101, @rubro_prg_proyectos, 90.00, '2025-06-05'),
(@mat_juan_prg101, @rubro_prg_labs, 75.00, '2025-06-20');

-- Calificaciones de María en Cálculo I (promedio: ~63.0 - REPROBADO para testing)
INSERT INTO calificaciones (matricula_id, rubro_id, nota, fecha_registro) VALUES
(@mat_maria_mat101, @rubro_mat_examenes, 55.00, '2025-03-15'),
(@mat_maria_mat101, @rubro_mat_deberes, 70.00, '2025-04-20'),
(@mat_maria_mat101, @rubro_mat_proyecto, 60.00, '2025-06-10'),
(@mat_maria_mat101, @rubro_mat_participacion, 85.00, '2025-06-15');

-- Calificaciones de María en Programación I (promedio: ~92.75 - EXCELENTE)
INSERT INTO calificaciones (matricula_id, rubro_id, nota, fecha_registro) VALUES
(@mat_maria_prg101, @rubro_prg_parc1, 95.00, '2025-03-10'),
(@mat_maria_prg101, @rubro_prg_parc2, 92.00, '2025-05-15'),
(@mat_maria_prg101, @rubro_prg_proyectos, 98.00, '2025-06-05'),
(@mat_maria_prg101, @rubro_prg_labs, 88.00, '2025-06-20');

-- ============================================
-- 10. ASISTENCIAS
-- ============================================
-- Asistencias de Juan en Cálculo I (80% asistencia aproximada)
INSERT INTO asistencias (matricula_id, fecha, estado, observaciones) VALUES
(@mat_juan_mat101, '2025-01-20', 'PRESENTE', NULL),
(@mat_juan_mat101, '2025-01-22', 'PRESENTE', NULL),
(@mat_juan_mat101, '2025-01-24', 'AUSENTE', 'Justificado - Cita médica'),
(@mat_juan_mat101, '2025-01-27', 'PRESENTE', NULL),
(@mat_juan_mat101, '2025-01-29', 'PRESENTE', NULL),
(@mat_juan_mat101, '2025-01-31', 'PRESENTE', NULL),
(@mat_juan_mat101, '2025-02-03', 'TARDANZA', 'Llegó 10 minutos tarde'),
(@mat_juan_mat101, '2025-02-05', 'PRESENTE', NULL),
(@mat_juan_mat101, '2025-02-07', 'PRESENTE', NULL),
(@mat_juan_mat101, '2025-02-10', 'AUSENTE', NULL);

-- Asistencias de María en Programación I (95% asistencia - estudiante ejemplar)
INSERT INTO asistencias (matricula_id, fecha, estado, observaciones) VALUES
(@mat_maria_prg101, '2025-01-21', 'PRESENTE', NULL),
(@mat_maria_prg101, '2025-01-23', 'PRESENTE', NULL),
(@mat_maria_prg101, '2025-01-28', 'PRESENTE', NULL),
(@mat_maria_prg101, '2025-01-30', 'PRESENTE', NULL),
(@mat_maria_prg101, '2025-02-04', 'PRESENTE', NULL),
(@mat_maria_prg101, '2025-02-06', 'PRESENTE', NULL),
(@mat_maria_prg101, '2025-02-11', 'PRESENTE', NULL),
(@mat_maria_prg101, '2025-02-13', 'PRESENTE', NULL),
(@mat_maria_prg101, '2025-02-18', 'TARDANZA', 'Tráfico'),
(@mat_maria_prg101, '2025-02-20', 'PRESENTE', NULL);

-- ============================================
-- VERIFICACIONES
-- ============================================
-- Puedes ejecutar estas consultas para verificar los datos:

-- Contar registros por tabla
SELECT 'Estudiantes' as Tabla, COUNT(*) as Total FROM estudiantes
UNION ALL SELECT 'Docentes', COUNT(*) FROM docentes
UNION ALL SELECT 'Periodos', COUNT(*) FROM periodos
UNION ALL SELECT 'Cursos', COUNT(*) FROM cursos
UNION ALL SELECT 'Asignaciones', COUNT(*) FROM asignaciones
UNION ALL SELECT 'Matrículas', COUNT(*) FROM matriculas
UNION ALL SELECT 'Rubros', COUNT(*) FROM rubros
UNION ALL SELECT 'Calificaciones', COUNT(*) FROM calificaciones
UNION ALL SELECT 'Asistencias', COUNT(*) FROM asistencias;

-- Verificar que todas las ponderaciones sumen 100%
SELECT 
    a.id as asignacion_id,
    c.codigo,
    c.nombre,
    a.seccion,
    SUM(r.ponderacion) as total_ponderacion
FROM asignaciones a
JOIN cursos c ON a.curso_id = c.id
JOIN rubros r ON r.asignacion_id = a.id
GROUP BY a.id, c.codigo, c.nombre, a.seccion
HAVING SUM(r.ponderacion) != 100.00;
-- Debería estar vacío (sin resultados) si todo está bien

-- Ver promedios de estudiantes
SELECT 
    e.nombre,
    e.apellido,
    c.codigo as curso,
    m.estado,
    ROUND(
        (SELECT SUM((cal.nota * rub.ponderacion) / 100) 
         FROM calificaciones cal 
         JOIN rubros rub ON cal.rubro_id = rub.id 
         WHERE cal.matricula_id = m.id), 2
    ) as promedio_ponderado
FROM matriculas m
JOIN estudiantes e ON m.estudiante_id = e.id
JOIN asignaciones a ON m.asignacion_id = a.id
JOIN cursos c ON a.curso_id = c.id
WHERE m.id IN (
    SELECT DISTINCT matricula_id FROM calificaciones
)
ORDER BY e.apellido, e.nombre, c.codigo;
