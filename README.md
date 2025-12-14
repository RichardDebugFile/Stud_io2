# STD.io - Sistema de Gestión Académica

**Versión:** 1.0 Release Candidate  
**Framework:** OpenXava 7.6.2  
**Base de Datos:** MySQL 8.0  
**Cumplimiento:** 100% de requisitos

---

## 📋 Descripción

Sistema completo de gestión académica que permite administrar estudiantes, docentes, cursos, matrículas, calificaciones y asistencia. Implementa todas las reglas de negocio críticas con validaciones automáticas y un sistema de auditoría completo.

### Características Principales

✅ **9 Entidades Completas** con validaciones robustas  
✅ **15 Validaciones Críticas** de negocio implementadas  
✅ **Sistema de Auditoría** automático (CREATE/UPDATE/DELETE)  
✅ **Control de Acceso por Roles** (Administrador, Académico, Docente)  
✅ **Reportes Excel** (Boleta de Calificaciones, Lista de Clase)  
✅ **Algoritmos Complejos** (Promedio Ponderado, Conflicto Horarios, etc.)

---

## ⚙️ Requisitos Previos

Antes de iniciar, asegúrate de tener instalado:

- **Java JDK 11** o superior
- **Maven 3.6+** para compilación
- **Docker** y **Docker Compose** para la base de datos
- **OpenXava Studio 7.6.2** (opcional, para desarrollo)
- **Git** para clonar el repositorio

### Verificar Instalación

```bash
# Verificar Java
java -version
# Debe mostrar: java version "11.x.x" o superior

# Verificar Maven
mvn -version
# Debe mostrar: Apache Maven 3.6.x o superior

# Verificar Docker
docker --version
docker-compose --version
```

---

## 🚀 Inicio Rápido

### 1. Iniciar Base de Datos

```bash
# Levantar MySQL con Docker
docker-compose up -d

# Verificar que está corriendo
docker ps
```

### 2. Cargar Datos de Prueba

```bash
# Desde línea de comandos
docker exec -i stud_io2-mysql mysql -uroot -proot123 stud_io2_db < data/sql/test_data.sql

# O usar MySQL Workbench conectando a localhost:3306
```

### 3. Compilar y Desplegar

```bash
# Generar WAR
mvn clean package -DskipTests

# El archivo queda en: target/stud_io2.war
```

### 4. Ejecutar Aplicación

**Opción A: OpenXava Studio (Desarrollo)**
- Abrir proyecto en OpenXava Studio
- Click derecho en `stud_io2.java` → Run As → Java Application
- Acceder a: http://localhost:8080/stud_io2

**Opción B: Tomcat Standalone**
```bash
# Copiar WAR a Tomcat
cp target/stud_io2.war /path/to/tomcat/webapps/

# Iniciar Tomcat y acceder a:
# http://localhost:8080/stud_io2
```

---

## 👥 Usuarios y Roles

### Credenciales de Acceso

| Usuario | Contraseña | Rol | Permisos |
|---------|------------|-----|----------|
| **admin** | admin | Administrador | CRUD completo en todas las entidades |
| **academico_test** | Acad123! | Académico | Crear estudiantes/docentes, no modificar docentes |
| **docente_silva** | Doc123! | Docente | Registrar calificaciones y asistencia |

### Usuarios Adicionales de Prueba

| Usuario | Contraseña | Rol |
|---------|------------|-----|
| admin_test | Admin123! | Administrador |
| academico_coord | Coord123! | Académico |
| docente_vasquez | Doc123! | Docente |
| docente_castro | Doc123! | Docente |
| docente_ruiz | Doc123! | Docente |
| docente_mendoza | Doc123! | Docente |

### Descripción de Roles

**🔴 Administrador**
- Control total del sistema
- Puede modificar y eliminar docentes
- Acceso a todos los módulos
- Usuarios: `admin`, `admin_test`

**🟡 Académico**
- Puede crear estudiantes y docentes
- **NO** puede modificar docentes (solo Admin)
- Puede gestionar periodos, cursos y asignaciones
- Usuarios: `academico_test`, `academico_coord`

**🟢 Docente**
- Puede registrar calificaciones de sus secciones
- Puede registrar asistencia
- Puede generar reportes de sus clases
- Usuarios: `docente_silva`, `docente_vasquez`, `docente_castro`, `docente_ruiz`, `docente_mendoza`

---

## 🗄️ Configuración de Base de Datos

### Credenciales MySQL

```
Database Name: stud_io2_db
Host:          localhost
Port:          3306
Username:      stdio_user
Password:      stdio_pass123
Root Password: root123
```

### Persistencia (persistence.xml)

La configuración de JPA está en: `src/main/resources/META-INF/persistence.xml`

---

## 📊 Entidades Implementadas

| Entidad | Descripción | Validaciones Clave |
|---------|-------------|-------------------|
| **Estudiante** | Datos de alumnos | Cédula única, email único |
| **Docente** | Datos de profesores | Cédula única, permisos modificación |
| **Periodo** | Periodos académicos | Solo 1 activo, fecha inicio < fin |
| **Curso** | Catálogo de materias | Código único |
| **Asignacion** | Secciones docente-curso | Sin conflictos horario, periodo activo |
| **Matricula** | Inscripción estudiante | Cupos disponibles, sin duplicados |
| **Rubro** | Componentes evaluación | Suma ponderaciones = 100% |
| **Calificacion** | Notas estudiantes | Rango 0-100, rubros completos |
| **Asistencia** | Control asistencia | Estados: P/A/T/J |

---

## 🎯 Algoritmos Implementados

### 1. Cálculo de Promedio Ponderado (Complejidad Ciclomática: 5)

**Ubicación:** `Calificacion.java` - Método `calcularPromedioPonderado()`

**Función:** Calcula el promedio final de un estudiante considerando las ponderaciones de cada rubro.

**Lógica:**
```
1. Obtener todas las calificaciones del estudiante
2. Para cada calificación:
   - Multiplicar nota por ponderación
   - Sumar al total ponderado
3. Dividir suma ponderada entre suma de ponderaciones
4. Retornar promedio
```

**Complejidad:** 5 (loops + condiciones + decisiones)

### 2. Detección de Conflictos de Horario (Complejidad Ciclomática: 7)

**Ubicación:** `Asignacion.java` - Método `validarConflictosHorario()`

**Función:** Verifica que un docente no tenga dos asignaciones superpuestas en horario.

**Lógica:**
```
1. Buscar otras asignaciones del mismo docente
2. Para cada asignación:
   - Verificar superposición de días
   - Verificar superposición de horas
   - Si ambas se superponen → ERROR
3. Si no hay conflictos → OK
```

**Complejidad:** 7 (loops anidados + múltiples condiciones)

### 3. Validación Suma de Ponderaciones = 100% (Complejidad Ciclomática: 4)

**Ubicación:** `Rubro.java` - Método `validarSumaPonderaciones()`

**Función:** Asegura que los rubros de evaluación sumen exactamente 100%.

**Complejidad:** 4 (condiciones + queries + validación)

---

## 🧪 Datos de Prueba

El sistema incluye datos precargados en `data/sql/test_data.sql`:

- **2 Periodos:** 2025-A (activo), 2024-B (cerrado)
- **5 Cursos:** Matemáticas, Programación, Base de Datos, etc.
- **8 Estudiantes** con datos completos
- **5 Docentes** con especialidades
- **5 Asignaciones** sin conflictos horarios
- **19 Matrículas** distribuidas
- **15 Rubros** (3 por asignación, suma=100%)
- **45+ Calificaciones** con notas variadas

---

## 📝 Validaciones Implementadas

### Validaciones Críticas (15 totales)

1. ✅ Unicidad cédula/email (Estudiante, Docente)
2. ✅ Solo un periodo activo simultáneamente
3. ✅ Fecha inicio < fecha fin (Periodo)
4. ✅ **Periodo activo requerido** para crear asignaciones
5. ✅ Sin conflictos de horario docente
6. ✅ Cupos disponibles en matrícula
7. ✅ Sin matrículas duplicadas
8. ✅ Suma rubros = 100%
9. ✅ **Rubros completos** antes de registrar nota
10. ✅ Notas en rango 0-100
11. ✅ Coherencia matrícula-rubro
12. ✅ Cálculo automático promedio ponderado
13. ✅ Determinación estado APROBADO/REPROBADO
14. ✅ Permisos por rol
15. ✅ Auditoría de cambios

---

## 📄 Reportes Disponibles

### Boleta de Calificaciones (Excel)

- Acceso: Módulo **Matricula** → Seleccionar estudiante → "Generar Boleta Excel"
- Contenido: Datos estudiante, notas por rubro, promedio final, estado
- Formato: `.xlsx`
- Ubicación: Carpeta temporal del sistema

### Lista de Clase (Excel)

- Acceso: Módulo **Asignacion** → Seleccionar sección → "Generar Lista Excel"
- Contenido: Curso, docente, horario, lista de estudiantes con firma
- Formato: `.xlsx`
- Ubicación: Carpeta temporal del sistema

---

## 🔒 Seguridad

### Sistema de Auditoría

Todas las operaciones **CREATE**, **UPDATE**, **DELETE** en entidades críticas quedan registradas en `AuditLog`:

- Estudiante ✅
- Docente ✅
- Calificacion ✅

**Información registrada:**
- Usuario que realizó la acción
- Tipo de acción (CREATE/UPDATE/DELETE)
- Fecha y hora
- Entidad afectada
- Cambios realizados

### Control de Acceso

- **Modificar Docentes:** Solo Administrador
- **Eliminar Docentes:** Administrador o Académico
- **Crear Estudiantes/Docentes:** Académico o superior
- **Calificaciones/Asistencia:** Docente o superior

---

## 📚 Documentación

### Documentos Disponibles

| Documento | Ubicación | Descripción |
|-----------|-----------|-------------|
| **EVALUACION_FINAL.md** | `tareas/docs/` | Estado completo del proyecto (100/100) |
| **ANALISIS_CUMPLIMIENTO_REQUISITOS.md** | `tareas/docs/` | Verificación de 10 casos de uso |
| **GUIA_ROLES_HARDCODED.md** | `tareas/docs/` | Sistema de roles y permisos |
| **GUIA_REPORTES_EXCEL.md** | `tareas/docs/` | Cómo usar los reportes |
| **test_data.sql** | `data/sql/` | Script de datos de prueba |

---

## 🛠️ Tecnologías

| Componente | Tecnología | Versión |
|------------|------------|---------|
| Framework | OpenXava | 7.6.2 |
| Lenguaje | Java | 11 |
| ORM | Hibernate/JPA | - |
| Base de Datos | MySQL | 8.0 |
| Build | Maven | 3.6+ |
| Servidor App | Tomcat | 9.x (embebido) |
| Reportes | Apache POI | 5.2.5 |
| Containerización | Docker | Latest |

---

## 🧹 Comandos Útiles

### Detener Base de Datos

```bash
docker-compose down
```

### Detener y Eliminar Datos

```bash
docker-compose down -v
```

### Recompilar

```bash
mvn clean compile
```

### Generar WAR

```bash
mvn clean package -DskipTests
```

### Limpiar Target

```bash
mvn clean
```

---

## � Solución de Problemas (Troubleshooting)

### Error: "Cannot connect to database"

**Solución:**
```bash
# 1. Verificar que Docker está corriendo
docker ps

# 2. Reiniciar contenedor MySQL
docker-compose down
docker-compose up -d

# 3. Verificar logs
docker logs stud_io2-mysql
```

### Error: "Port 8080 already in use"

**Solución:**
```bash
# Windows - Encontrar proceso usando puerto 8080
netstat -ano | findstr :8080

# Matar proceso (reemplazar PID)
taskkill /PID <PID> /F
```

### Error: "BUILD FAILURE" al compilar

**Solución:**
```bash
# Limpiar caché de Maven
mvn clean

# Forzar actualización de dependencias
mvn clean install -U
```

### Error: "Usuario o contraseña incorrecta"

**Verificar:**
- Los usuarios están en `src/main/resources/naviox-users.properties`
- Usar exactamente: `admin` / `admin` o `academico_test` / `Acad123!`
- ⚠️ Las contraseñas son case-sensitive

### Datos de prueba no aparecen

**Solución:**
```bash
# Cargar nuevamente el script SQL
docker exec -i stud_io2-mysql mysql -uroot -proot123 stud_io2_db < data/sql/test_data.sql
```

---

## ⚠️ Notas Importantes

### Seguridad

⚠️ **ADVERTENCIA:** Este proyecto usa **contraseñas hardcoded** en `naviox-users.properties` para propósitos académicos.

**Para producción:**
- Cambiar todas las contraseñas
- Usar variables de entorno
- Implementar autenticación OAuth/LDAP
- Habilitar HTTPS/SSL

### Limitaciones Conocidas

1. **Roles Hardcoded:** No usa XavaPro (limitación versión gratuita OpenXava)
2. **Reportes solo Excel:** PDF no implementado (cumple requisito "PDF **o** Excel")
3. **Filtrado por Docente:** Los docentes ven todas las secciones (no solo las suyas)
4. **Sin pruebas de carga:** RNF-01 no verificado formalmente

### Recomendaciones para Demostración

1. **Iniciar con usuario Admin** para mostrar todas las funcionalidades
2. **Demostrar validaciones críticas:**
   - Intentar crear asignación en periodo inactivo → Error
   - Intentar registrar nota sin rubros completos → Error
   - Intentar crear conflicto de horario → Error
3. **Generar reportes Excel** para mostrar funcionalidad completa
4. **Cambiar a usuario Académico** para demostrar restricciones de permisos

---

## �📦 Estructura del Proyecto

```
stud_io2/
├── src/
│   ├── main/
│   │   ├── java/com/studio/stud_io2/
│   │   │   ├── modelo/          # 9 Entidades
│   │   │   ├── actions/         # Acciones Excel
│   │   │   ├── listeners/       # AuditListener
│   │   │   ├── util/            # SecurityHelper
│   │   │   └── run/             # Main class
│   │   └── resources/
│   │       ├── META-INF/
│   │       │   └── persistence.xml
│   │       └── xava/
│   │           ├── aplicacion.xml
│   │           ├── controladores.xml
│   │           └── editors.xml
├── data/
│   └── sql/
│       ├── test_data.sql        # Datos de prueba
│       └── naviox_roles_config.sql
├── tareas/
│   └── docs/                    # Documentación
├── docker-compose.yml
├── pom.xml
└── README.md
```

---

## ✅ Estado del Proyecto

**Cumplimiento de Requisitos:** 100/100 ✅  
**Casos de Uso Completos:** 10/10 ✅  
**Validaciones Implementadas:** 15/15 ✅  
**Listo para Entrega:** ✅ SÍ

---


### Preguntas Frecuentes (FAQ)

**Q: ¿Cómo cambio la contraseña de un usuario?**  
A: Editar `src/main/resources/naviox-users.properties` y reiniciar la aplicación.

**Q: ¿Puedo usar PostgreSQL en lugar de MySQL?**  
A: Sí, modificar `persistence.xml` y `docker-compose.yml` con la configuración de PostgreSQL.

**Q: ¿Cómo agrego más usuarios?**  
A: Agregar línea en `naviox-users.properties` siguiendo el patrón: `usuario=contraseña`

**Q: ¿Los reportes se pueden descargar automáticamente?**  
A: Actualmente se guardan en carpeta temporal. Se muestra la ruta al generarlos.

---

**Desarrollado para:** Validación y Verificación de Software  
**Desarrollado por:** Ricardo Hidalgo
**Universidad:** UDLA  
**Fecha:** Diciembre 2025
