# Reportes Excel - Implementación Completada

## Acciones Disponibles

### 1. Boleta de Calificaciones
- **Clase:** `GenerarBoletaExcelAction`
- **Módulo:** Matricula
- **Contenido:**
  - Nombre del estudiante
  - Curso y sección
  - Periodo académico
  - Tabla de calificaciones por rubro
  - Promedio final calculado
  - Estado (APROBADO/REPROBADO)

### 2. Lista de Clase  
- **Clase:** `GenerarListaClaseExcelAction`
- **Módulo:** Asignacion
- **Contenido:**
  - Información del curso
  - Docente y horario
  - Total de estudiantes
  - Lista con: No., Cédula, Nombre, Email

## Cómo Usar

1. Abrir módulo **Matricula** o **Asignacion**
2. Seleccionar un registro
3. Click en botón **"Generar Boleta Excel"** o **"Generar Lista Excel"**
4. El archivo se genera en carpeta temporal
5. Se muestra mensaje con la ruta del archivo

## Archivos Generados

- Boleta: `boleta_[NombreEstudiante].xlsx`
- Lista: `lista_[Curso]_[Seccion].xlsx`

## Ubicación

Los archivos se guardan en: `%TEMP%` (ej: `C:\Users\...\AppData\Local\Temp\`)

## Tecnología

- Apache POI 5.2.5
- Formato XLSX (Excel 2007+)
- Auto-ajuste de columnas
