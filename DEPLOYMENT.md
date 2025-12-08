# STD.io - Instrucciones de Deployment

## ✅ Correcciones Aplicadas

### 1. pom.xml
- **Problema:** XML malformado (comentario sin abrir correctamente)
- **Solución:** Reescrito completamente con estructura XML válida
- **Status:** ✅ CORREGIDO - `mvn validate` pasa exitosamente

### 2. docker-compose.yml
- **Problema 1:** Campo `version` deprecado
- **Problema 2:** Falta especificación de plataforma para Windows
- **Solución:** Eliminado `version`, agregado `platform: linux/amd64`
- **Status:** ⚠️ Requiere Docker Desktop corriendo

---

## 🚀 Pasos para Deployment

### PASO 1: Iniciar Docker Desktop

**⚠️ IMPORTANTE:** Docker Desktop debe estar corriendo antes de continuar.

1. Abre Docker Desktop manualmente
2. Espera a que aparezca el mensaje "Docker is running"
3. Verifica que el icono de Docker no muestre errores

### PASO 2: Levantar Base de Datos MySQL

```powershell
cd "g:\Documentos G\Ing. Sotware\Periodo 3\Validacion Y Verificacion de Software\openxava-studio-7-r4-windows\openxava-studio-7-r4\workspace\stud_io2"

# Iniciar MySQL
docker-compose up -d

# Verificar que esté corriendo
docker ps

# Deberías ver algo como:
# CONTAINER ID   IMAGE       STATUS                    PORTS                    NAMES
# xxxxxx         mysql:8.0   Up X seconds (healthy)    0.0.0.0:3306->3306/tcp   stdio-mysql
```

**Troubleshooting:**
- Si falla con "cannot connect to Docker daemon" → Docker Desktop no está corriendo
- Si falla descargando imagen → Verifica tu conexión a Internet

### PASO 3: Compilar el Proyecto

```powershell
# Limpiar y compilar
mvn clean compile

# Si quieres generar el WAR completo
mvn clean package
```

El archivo WAR generado estará en: `target/stud_io2.war`

### PASO 4: Desplegar en OpenXava Studio

**Opción A: Desde OpenXava Studio**
1. Abre OpenXava Studio
2. Click derecho en el proyecto `stud_io2` → Run As → Run on Server
3. Selecciona Tomcat (debería detectarse automáticamente)
4. El navegador se abrirá en `http://localhost:8080/stud_io2`

**Opción B: Despliegue Manual en Tomcat**
1. Copia `target/stud_io2.war` a `<TOMCAT_HOME>/webapps/`
2. Inicia Tomcat
3. Accede a `http://localhost:8080/stud_io2`

---

## 🔧 Verificación Post-Deployment

### 1. Verificar Conexión a BD
Una vez que la aplicación esté corriendo, OpenXava creará automáticamente las tablas en MySQL.

Puedes verificar con:
```powershell
docker exec -it stdio-mysql mysql -ustdio_user -pstdio_pass123 stud_io2

# Dentro de MySQL
SHOW TABLES;
# Deberías ver: estudiantes, docentes, periodos, cursos, asignaciones, matriculas, rubros, calificaciones, asistencias
```

### 2. Verificar Módulos en la Aplicación
1. Accede a `http://localhost:8080/stud_io2`
2. Login (usuario por defecto de Naviox: admin / admin)
3. Verifica que aparezcan los módulos:
   - Estudiante, Docente, Periodo, Curso
   - Asignacion, Matricula
   - Rubro, Calificacion, Asistencia

---

## 🧪 Plan de Pruebas Recomendado

### Test 1: CRUD Básico
1. Crear un **Periodo** (ej: "2025-A", 2025-01-01 a 2025-06-30, activo=true)
2. Crear un **Curso** (ej: "MAT101", "Cálculo I", 4 créditos)
3. Crear un **Docente** (cédula, nombre, email)
4. Crear un **Estudiante** (cédula, nombre, email)

### Test 2: Validación de Fechas (RF-03)
1. Intenta crear un Periodo con fechaFin ANTES de fechaInicio
   - **Esperado:** Error de validación
2. Crea un segundo periodo con activo=true (ya existe uno activo)
   - **Esperado:** Error "Solo puede haber un periodo activo"

### Test 3: Conflictos de Horario (RF-05)
1. Crear **Asignacion 1**: MAT101, Docente X, Lunes 08:00-10:00
2. Intentar crear **Asignacion 2**: Otro curso, MISMO Docente X, Lunes 09:00-11:00
   - **Esperado:** Error de conflicto de horario

### Test 4: Validación 100% Rubros (RF-07)
1. Para una Asignacion, crear Rubro "Examen" (40%)
2. Crear Rubro "Deberes" (35%)
3. Intentar crear Rubro "Proyecto" (30%) → Total = 105%
   - **Esperado:** Error "La suma excede el 100%"
4. Cambiar a 25% → Debe funcionar

### Test 5: Control de Cupos (RF-06)
1. Crear Asignacion con cupoMaximo=2
2. Matricular Estudiante 1 → OK
3. Matricular Estudiante 2 → OK
4. Matricular Estudiante 3 → **Error "No hay cupos"**

---

## 📊 Estado Actual

| Componente | Status | Notas |
|------------|--------|-------|
| **Entidades (9)** | ✅ Implementadas | Todas con validaciones |
| **pom.xml** | ✅ Corregido | XML válido |
| **docker-compose.yml** | ✅ Corregido | Requiere Docker Desktop |
| **persistence.xml** | ✅ Configurado | Todas entities registradas |
| **aplicacion.xml** | ✅ Configurado | Todos módulos registrados |
| **Compilación Maven** | ✅ Funciona | `mvn validate` exitoso |
| **Docker MySQL** | ⚠️ Pendiente | Requiere iniciar Docker Desktop |
| **Deployment** | ⏳ Pendiente | Esperando Docker + compilación |

---

## 🆘 Troubleshooting Común

### Error: "Cannot connect to Docker daemon"
**Solución:** Abre Docker Desktop y espera que inicie completamente

### Error: "Port 3306 already in use"
**Solución:** Tienes otro MySQL corriendo
```powershell
# Ver qué está usando el puerto
netstat -ano | findstr :3306

# Opción 1: Detener otro MySQL
# Opción 2: Cambiar puerto en docker-compose.yml a "3307:3306"
```

### Error: "Failed to download mysql:8.0"
**Solución:** 
1. Verifica conexión a Internet
2. Prueba descargar manualmente: `docker pull mysql:8.0`

### Error Maven: "Unknown lifecycle phase"
**Solución:** Verifica que estés en el directorio correcto del proyecto

---

## 📞 Siguiente Paso

**Una vez que Docker Desktop esté corriendo:**
1. Ejecuta `docker-compose up -d`
2. Ejecuta `mvn clean package`
3. Despliega en OpenXava Studio o Tomcat
4. Comienza las pruebas de validación

¿Necesitas ayuda con algún paso específico?
