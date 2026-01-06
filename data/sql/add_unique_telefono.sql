-- Script de migración: Agregar restricción UNIQUE a campos telefono
-- Fecha: 2026-01-06
-- Motivo: Bug fix - Permitía duplicados en números de teléfono

-- Nota: Antes de ejecutar este script, asegúrese de que no existan
-- teléfonos duplicados en la base de datos, o elimine los duplicados primero

-- Para HSQLDB
ALTER TABLE docentes ADD CONSTRAINT UK_DOCENTE_TELEFONO UNIQUE (telefono);
ALTER TABLE estudiantes ADD CONSTRAINT UK_ESTUDIANTE_TELEFONO UNIQUE (telefono);

-- Si existen duplicados, ejecutar primero:
-- SELECT telefono, COUNT(*) FROM docentes WHERE telefono IS NOT NULL GROUP BY telefono HAVING COUNT(*) > 1;
-- SELECT telefono, COUNT(*) FROM estudiantes WHERE telefono IS NOT NULL GROUP BY telefono HAVING COUNT(*) > 1;
