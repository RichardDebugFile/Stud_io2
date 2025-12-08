# Base de Datos - Datos de Prueba Cargados

## ✅ Resumen de Datos Insertados

La base de datos ha sido poblada con datos realistas para testing y demostración.

| Entidad | Cantidad | Detalles |
|---------|----------|----------|
| **Estudiantes** | 8 | Juan Pérez, María González, Carlos Ramírez, Ana López, Luis Martínez, Sofía Torres, Diego Flores, Valentina Morales |
| **Docentes** | 5 | Roberto Silva (Matemáticas), Patricia Vásquez (Programación), Fernando Castro (BD), Gabriela Ruiz (Física), Andrés Mendoza (Estadística) |
| **Periodos** | 1 | 2025-A (15 Ene - 30 Jun, **ACTIVO**) |
| **Cursos** | 6 | Cálculo I, Programación I, Bases de Datos, Física I, Estadística, Álgebra Lineal |
| **Asignaciones** | 6 | Secciones con horarios distribuidos Lunes-Viernes |
| **Matrículas** | 19 | Distribución entre 6 estudiantes activos |
| **Rubros** | 18 | Todos suman exactamente **100%** por asignación ✓ |
| **Calificaciones** | 16 | Con promedios ponderados calculados |
| **Asistencias** | 20 | Registros variados (Presente, Ausente, Tardanza) |

---

## 📊 Casos de Prueba Incluidos

### 1. Estudiante Aprobado - Juan Pérez

**Cálculo I (MAT101-A)**
- Exámenes (40%): 85.00 → 34.0 puntos
- Deberes (30%): 90.00 → 27.0 puntos
- Proyecto Final (20%): 75.00 → 15.0 puntos
- Participación (10%): 95.00 → 9.5 puntos
- **Promedio Ponderado: 85.5** ✅ **APROBADO**

**Programación I (PRG101-A)**
- **Promedio Ponderado: 85.25** ✅ **APROBADO**

**Asistencia:**
- 80% en Cálculo I (con 1 ausencia justificada, 1 tardanza)

---

### 2. Estudiante Reprobado (Caso de Testing) - María González

**Cálculo I (MAT101-A)**
- Exámenes (40%): 55.00 → 22.0 puntos
- Deberes (30%): 70.00 → 21.0 puntos
- Proyecto Final (20%): 60.00 → 12.0 puntos
- Participación (10%): 85.00 → 8.5 puntos
- **Promedio Ponderado: 63.5** ❌ **REPROBADO** (< 70)

**Programación I (PRG101-A)**
- **Promedio Ponderado: 94.25** ✅ **APROBADO** (Estudiante ejemplar en esta materia)

**Asistencia:**
- 95% en Programación I (excelente)

---

## 🔍 Validaciones Cumplidas

### ✅ RF-01: Unicidad de Estudiantes
- Todas las cédulas son únicas (10 dígitos)
- Todos los emails son únicos (@estudiantes.edu.ec)

### ✅ RF-02: Unicidad de Docentes
- Todas las cédulas docentes son únicas (1700000001-1700000005)
- Emails únicos (@profesores.edu.ec)

### ✅ RF-03: Validación de Periodos
- Solo **1 periodo activo** (2025-A)
- `fechaInicio` < `fechaFin` ✓

### ✅ RF-04: Catálogo de Cursos
- 6 materias con códigos únicos (MAT101, PRG101, BDD201, etc.)
- Créditos: 3-5, Horas/semana: 4-8

### ✅ RF-05: No Conflictos de Horario
Todos los horarios de docentes están distribuidos sin solapamientos:
- **Roberto Silva (MAT101-A):** Lun/Mié/Vie 08:00-10:00
- **Roberto Silva (MAT201-B):** Mar/Jue 14:00-16:00 ✓
- **Patricia Vásquez (PRG101-A):** Mar/Jue 10:00-12:00 ✓
- **Fernando Castro (BDD201-A):** Lun/Mié 14:00-16:00 ✓
- Sin conflictos ✓

### ✅ RF-06: Control de Cupos
- Todas las matrículas están dentro del `cupoMaximo`
- No hay matrículas duplicadas (unique constraint)

### ✅ RF-07: Rubros Suman 100%
**Verificación por Asignación:**
- MAT101-A: 40 + 30 + 20 + 10 = **100%** ✓
- PRG101-A: 25 + 25 + 35 + 15 = **100%** ✓
- BDD201-A: 50 + 35 + 15 = **100%** ✓
- FIS101-A: 40 + 30 + 20 + 10 = **100%** ✓
- EST201-A: 60 + 25 + 15 = **100%** ✓

### ✅ RF-08: Cálculo de Promedios
Promedios ponderados calculados correctamente:
- Juan (MAT101): 85.5
- María (MAT101): 63.5 (< 70, REPROBADO)
- María (PRG101): 94.25 (EXCELENTE)

### ✅ RF-09: Asistencias Sin Duplicados
- Unique constraint en (matricula_id, fecha)
- Variedad de estados: PRESENTE, AUSENTE, TARDANZA, JUSTIFICADO

---

## 🎯 Cómo Explorar los Datos

### En OpenXava (http://localhost:8080/stud_io2)

1. **Ver Estudiantes:**
   - Ve al módulo "Estudiante"
   - Busca "Juan Pérez" o "María González"
   - Verifica cédula única, email, dirección

2. **Ver Asignaciones:**
   - Módulo "Asignacion"
   - Observa: Cálculo I - Sección A (Prof. Silva, Lun/Mié/Vie 08:00-10:00)

3. **Ver Matrículas:**
   - Módulo "Matricula"
   - Filtra por estudiante "Juan Pérez"
   - Verás sus 4 materias inscritas

4. **Ver Rubros:**
   - Módulo "Rubro"
   - Filtra por asignación "Cálculo I - A"
   - Verifica que sumen exactamente 100%

5. **Ver Calificaciones:**
   - Módulo "Calificacion"
   - Busca calificaciones de Juan en Cálculo I
   - Observa notas por rubro

6. **Ver Asistencias:**
   - Módulo "Asistencia"
   - Filtra por estudiante "María González"
   - Observa registros de asistencia

---

## 🔄 Reiniciar Datos

Si quieres borrar todos los datos de prueba y empezar de cero:

```sql
-- Ejecuta en MySQL
USE stud_io2;

DELETE FROM asistencias;
DELETE FROM calificaciones;
DELETE FROM rubros;
DELETE FROM matriculas;
DELETE FROM asignaciones;
DELETE FROM cursos;
DELETE FROM periodos;
DELETE FROM docentes;
DELETE FROM estudiantes;
```

Luego vuelve a ejecutar `test_data.sql`:
```powershell
Get-Content test_data.sql | docker exec -i stdio-mysql mysql -ustdio_user -pstdio_pass123 stud_io2
```

---

## 📈 Estadísticas Actuales

- **Total de matrículas activas:** 19
- **Promedio de materias por estudiante:** ~3.2
- **Tasa de aprobación en datos muestra:** 75% (3/4 calificaciones > 70)
- **Asistencia promedio:** ~87.5%
- **Cursos con más demanda:** Programación I, Cálculo I

---

## 🧪 Escenarios de Testing Disponibles

1. ✅ **Estudiante con múltiples materias** (Juan - 4 materias)
2. ✅ **Estudiante reprobando una materia** (María - MAT101)
3. ✅ **Docente con múltiples secciones** (Roberto Silva - 2 secciones)
4. ✅ **Asignación con cupo limitado** (todas tienen límites)  
5. ✅ **Rubros sumando exactamente 100%** (todas las asignaciones)
6. ✅ **Asistencias con justificaciones** (Juan tiene ausencia justificada)
7. ✅ **Diferentes tipos de asistencia** (Presente, Ausente, Tardanza)
8. ✅ **Periodo activo único** (solo 2025-A está activo)

---

¡Listo para comenzar las pruebas de validación! 🚀
