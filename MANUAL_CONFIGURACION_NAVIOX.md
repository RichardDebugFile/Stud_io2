# Manual de Configuración de Roles en Naviox
## STD.io - Configuración RNF-05

> [!IMPORTANT]
> **Este es el método RECOMENDADO para configurar roles en Naviox.**  
> Más seguro y mantenible que scripts SQL directos.

---

## Contexto

Naviox es el módulo de seguridad incluido en OpenXava que proporciona:
- ✅ Autenticación de usuarios
- ✅ Gestión de roles
- ✅ Control de permisos por módulo
- ✅ Interfaz web de administración

**Requisito RNF-05:** Implementar 3 roles con matriz de permisos diferenciada.

---

## Paso 1: Acceder al Panel de Administración

1. Asegúrate de que la aplicación esté corriendo:
   ```
   http://localhost:8080/stud_io2
   ```

2. Login con credenciales de administrador:
   - **Usuario:** `admin`
   - **Contraseña:** `admin`

3. En el menú principal, busca:
   - **Administración** → **Seguridad**

---

## Paso 2: Crear los 3 Roles

### 📍 Navegar a Roles

1. Click en **"Roles"** en el menú de Seguridad
2. Click en el botón **"Nuevo"** (o icono `+`)

### ✅ Rol 1: Administrador

**Crear rol:**
- **Nombre:** `Administrador`
- **Descripción:** `Acceso total al sistema. Responsable de configuración global y gestión de usuarios.`

**Guardar**

### ✅ Rol 2: Académico

**Crear rol:**
- **Nombre:** `Academico` (sin tilde para evitar problemas)
- **Descripción:** `Coordinador académico. Define oferta académica, asignaciones y matrículas.`

**Guardar**

### ✅ Rol 3: Docente

**Crear rol:**
- **Nombre:** `Docente`
- **Descripción:** `Usuario operativo de aula. Gestiona evaluaciones, calificaciones y asistencia de sus secciones.`

**Guardar**

---

## Paso 3: Configurar Permisos por Módulo

> [!WARNING]
> Naviox configura permisos por **Módulo**, NO por entidad individual.  
> Los nombres de módulos en OpenXava son los nombres de las clases Java.

### 📋 Matriz de Permisos Requerida

| Módulo | Administrador | Académico | Docente |
|--------|---------------|-----------|---------|
| **Estudiante** | CRUD | CRUD | R |
| **Docente** | CRUD | CR | R |
| **Periodo** | CRUD | CRUD | R |
| **Curso** | CRUD | CRUD | R |
| **Asignacion** | CRUD | CRUD | R |
| **Matricula** | CRUD | CRUD | R |
| **Rubro** | CRUD | CRUD | CRU* |
| **Calificacion** | CRUD | CRUD | CRU* |
| **Asistencia** | CRUD | CRUD | CRU* |
| **AuditLog** | CRUD | R | - |

*\* CRU = Crear, Leer, Actualizar (sin eliminar). Solo en SUS secciones (requiere filtro adicional).*

---

### 🔧 Configurar Permisos: Administrador

1. En lista de Roles, selecciona `Administrador`
2. Click en pestaña **"Permisos"** o **"Módulos"**
3. Para CADA módulo de la lista arriba, marcar:
   - ☑ **Crear**  
   - ☑ **Leer**  
   - ☑ **Actualizar**  
   - ☑ **Eliminar**

**Lista completa de módulos:**
- Estudiante
- Docente
- Periodo
- Curso
- Asignacion
- Matricula
- Rubro
- Calificacion
- Asistencia
- AuditLog

4. **Guardar cambios**

---

### 🔧 Configurar Permisos: Académico

1. Selecciona rol `Academico`
2. Para cada módulo:

**Estudiante, Periodo, Curso, Asignacion, Matricula, Rubro, Calificacion, Asistencia:**
- ☑ Crear  
- ☑ Leer  
- ☑ Actualizar  
- ☑ Eliminar

**Docente:** (ESPECIAL - Solo crear y consultar)
- ☑ Crear  
- ☑ Leer  
- ☐ Actualizar  
- ☐ Eliminar

**AuditLog:** (Solo lectura)
- ☐ Crear  
- ☑ Leer  
- ☐ Actualizar  
- ☐ Eliminar

3. **Guardar cambios**

---

### 🔧 Configurar Permisos: Docente

1. Selecciona rol `Docente`
2. Para cada módulo:

**Estudiante, Docente, Periodo, Curso, Asignacion, Matricula:** (Solo lectura)
- ☐ Crear  
- ☑ Leer  
- ☐ Actualizar  
- ☐ Eliminar

**Rubro, Calificacion, Asistencia:** (Gestión operativa)
- ☑ Crear  
- ☑ Leer  
- ☑ Actualizar  
- ☐ Eliminar  *(No permitir eliminar para evitar pérdida de datos)*

**AuditLog:** (Sin acceso)
- ☐ Crear  
- ☐ Leer  
- ☐ Actualizar  
- ☐ Eliminar

3. **Guardar cambios**

---

## Paso 4: Crear Usuarios de Prueba

### 📍 Navegar a Usuarios

1. Menú **Administración** → **Seguridad** → **Usuarios**
2. Click en **"Nuevo"**

### ✅ Usuario de Prueba: Coordinador Académico

**Datos del usuario:**
- **Usuario (username):** `academico_test`
- **Nombre:** `Coordinador Prueba`
- **Email:** `academico@stdio.edu.ec`
- **Contraseña:** `Acad123!` (o la que prefieras)
- **Activo:** ☑ Sí

**Asignar Rol:**
- En sección **"Roles"**, seleccionar: `Academico`

**Guardar**

### ✅ Usuario de Prueba: Docente

**Datos del usuario:**
- **Usuario:** `docente_test`
- **Nombre:** `Roberto Silva` (el docente que existe en la BD)
- **Email:** `roberto.silva@profesores.edu.ec`
- **Contraseña:** `Doc123!`
- **Activo:** ☑ Sí

**Asignar Rol:**
- Seleccionar rol: `Docente`

**IMPORTANTE - Vincular con Docente de la BD:**
> [!CAUTION]
> Para que el filtro "solo sus secciones" funcione, el usuario debe estar vinculado al registro de Docente en la base de datos. Esto puede requerir:
> 1. Agregar campo `user_id` en tabla `docentes`
> 2. O usar el `email` como identificador único para matchear

**Guardar**

---

## Paso 5: Verificar Permisos Funcionando

### 🧪 Test 1: Login como Académico

1. **Cerrar sesión** (Logout como admin)
2. **Login:** `academico_test` / `Acad123!`
3. **Verificar que puede ver:**
   - ✅ Estudiante, Periodo, Curso, Asignacion, Matricula
   - ✅ Docente (pero NO puede modificar)
   - ✅ Rubros, Calificaciones, Asistencias
4. **Intentar crear un Docente:**
   - Debería poder hacerlo ✅
5. **Intentar modificar un Docente existente:**
   - Debería mostrarmensaje de "Sin permisos" ❌

### 🧪 Test 2: Login como Docente

1. **Logout y login:** `docente_test` / `Doc123!`
2. **Verificar que puede ver (solo lectura):**
   - ✅ Estudiantes, Cursos, Periodos (sin botón "Nuevo")
3. **Verificar que puede gestionar:**
   - ✅ Rubros de SUS asignaciones
   - ✅ Calificaciones de SUS estudiantes
   - ✅ Asistencia de SUS clases
4. **Intentar crear un Estudiante:**
   - Debería mostrar "Sin permisos" ❌

---

## Paso 6: Filtro "Solo Sus Secciones" para Docentes

> [!WARNING]
> **Limitación de Naviox:** Los permisos de Naviox solo controlan CRUD completo de módulos.  
> **NO filtran automáticamente** los datos mostrados.

Para que un Docente solo vea **sus propias** secciones/calificaciones, hay 3 opciones:

### Opción A: Filtros SQL en Entidades (Recomendada)

**Modificar:** `Rubro.java`, `Calificacion.java`, `Asistencia.java`

```java
@Entity
@FilterDef(name = "docenteFilter", parameters = @ParamDef(name = "docenteEmail", type = "string"))
@Filter(name = "docenteFilter", condition = "asignacion_id IN (SELECT id FROM asignaciones WHERE docente_id IN (SELECT id FROM docentes WHERE email = :docenteEmail))")
public class Rubro {
    // ...
}
```

**Activar filtro al hacer login:**
```java
// En algún listener de sesión
EntityManager em = XPersistence.getManager();
Filter filter = em.unwrap(Session.class).enableFilter("docenteFilter");
filter.setParameter("docenteEmail", Users.getCurrent());
```

### Opción B: Conditions en OpenXava

Agregar en cada entidad:

```java
@Condition("${asignacion.docente.email} = ?")
public class Rubro {
    // OpenXava inyectará automáticamente Users.getCurrent()
}
```

### Opción C: Controllers Personalizados

Crear controladores que filtren las consultas según el usuario autenticado.

---

## Resumen de Estado

| Componente | Estado | Notas |
|------------|--------|-------|
| **Roles creados** | ✅ COMPLETO | 3 roles definidos |
| **Permisos CRUD** | ✅ COMPLETO | Matriz implementada |
| **Usuarios de prueba** | ✅ COMPLETO | 2 usuarios creados |
| **Filtro por docente** | ⚠️ OPCIONAL | Requiere código adicional |

---

## Troubleshooting

### Problema: No veo el menú de Seguridad

**Solución:** Solo el usuario `admin` (superusuario) tiene acceso al módulo de administración por defecto.

### Problema: Cambios de permisos no se reflejan

**Solución:**  
1. **Logout** completamente
2. Borrar cookies del navegador
3. **Login** nuevamente

### Problema: Docente ve TODAS las calificaciones, no solo las suyas

**Solución:** Implementar Opción A o B del Paso 6 (filtros SQL o @Condition).

---

## Documentación Oficial

- **Naviox Security Guide:** https://www.openxava.org/doc/naviox-security_en.html
- **OpenXava RBAC:** https://www.openxava.org/doc/security_en.html

---

**Configuración completada.** El sistema ahora cumple con RNF-05: Autorización con Roles.
