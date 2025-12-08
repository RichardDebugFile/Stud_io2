# Guía de Uso - Sistema de Roles Hardcoded

## Implementación sin XavaPro

Este sistema implementa roles basados en **convenciones de nombres de usuario**, funcionando sin necesidad de XavaPro.

---

## Usuarios de Prueba Disponibles

### 👨‍💼 Administradores
| Usuario | Contraseña | Permisos |
|---------|------------|----------|
| `admin` | `admin` | CRUD completo en todos los módulos |
| `admin_test` | `Admin123!` | CRUD completo en todos los módulos |

### 📊 Académicos
| Usuario | Contraseña | Permisos |
|---------|------------|----------|
| `academico_test` | `Acad123!` | CRUD excepto modificar/eliminar docentes |
| `academico_coord` | `Coord123!` | CRUD excepto modificar/eliminar docentes |

### 👨‍🏫 Docentes
| Usuario | Contraseña | Vinculado a Docente en BD |
|---------|------------|----------------------------|
| `docente_silva` | `Doc123!` | Roberto Silva (roberto.silva@profesores.edu.ec) |
| `docente_vasquez` | `Doc123!` | Patricia Vásquez |
| `docente_castro` | `Doc123!` | Fernando Castro |
| `docente_ruiz` | `Doc123!` | Gabriela Ruiz |
| `docente_mendoza` | `Doc123!` | Andrés Mendoza |

---

## Cómo Funciona

### 1. Detección de Rol por Nombre de Usuario

La clase `SecurityHelper` detecta roles así:

```java
// Patrón de nombre → Rol asignado
admin*         → ADMINISTRADOR
academico_*    → ACADEMICO
docente_*      → DOCENTE
otros          → USUARIO (sin permisos)
```

### 2. Matriz de Permisos (RNF-05)

| Módulo | Administrador | Académico | Docente |
|--------|---------------|-----------|---------|
| Estudiante | CRUD | CRUD | R |
| Docente | CRUD | CR | R |
| Periodo | CRUD | CRUD | R |
| Curso | CRUD | CRUD | R |
| Asignacion | CRUD | CRUD | R |
| Matricula | CRUD | CRUD | R |
| Rubro | CRUD | CRUD | CRU |
| Calificacion | CRUD | CRUD | CRU |
| Asistencia | CRUD | CRUD | CRU |

---

## Cómo Probar

### Paso 1: Login como Administrador

1. Accede a `http://localhost:8080/stud_io2`
2. Login: `admin` / `admin`
3. **Verificar:** Puedes ver y modificar TODOS los módulos

### Paso 2: Login como Académico

1. Logout
2. Login: `academico_test` / `Acad123!`
3. **Verificar:** 
   - ✅ Puedes crear estudiantes
   - ✅ Puedes crear docentes
   - ❌ **NO** puedes modificar docentes existentes (debería mostrar error)

### Paso 3: Login como Docente

1. Logout
2. Login: `docente_silva` / `Doc123!`
3. **Verificar:**
   - ✅ Puedes ver estudiantes (solo lectura)
   - ✅ Puedes gestionar rubros/calificaciones/asistencias de **TUS** secciones
   - ❌ NO puedes crear estudiantes ni periodos

---

##  Uso en Código

### Obtener Rol del Usuario Actual

```java
import com.studio.stud_io2.util.SecurityHelper;
import com.studio.stud_io2.util.SecurityHelper.Rol;

// En cualquier parte del código
Rol rolActual = SecurityHelper.getRolActual();

if (rolActual == Rol.ADMINISTRADOR) {
    // Lógica para administradores
}
```

### Verificar Permisos

```java
// Verificar si puede realizar operación
boolean puedeModificar = SecurityHelper.tienePermiso("UPDATE", "Docente");

if (!puedeModificar) {
    throw new ValidationException("Sin permisos para modificar docentes");
}
```

### Filtrar por Docente Autenticado

```java
// En @PrePersist de Rubro, Calificacion, Asistencia
if (SecurityHelper.esDocente()) {
    String emailDocente = SecurityHelper.getEmailDocenteFromUsername(
        SecurityHelper.getUsuarioActual()
    );
    
    // Verificar que la asignación pertenece a este docente
    if (!asignacion.getDocente().getEmail().equals(emailDocente)) {
        throw new ValidationException("No puedes modificar secciones de otros docentes");
    }
}
```

---

## Agregar Más Usuarios

Editar: `src/main/resources/naviox-users.properties`

```properties
# Nuevo académico
academico_nuevo=password123

# Nuevo docente (debe existir en tabla docentes)
docente_nuevo=password456
```

**IMPORTANTE:** Para docentes, el mapeo email debe agregarse en:
`SecurityHelper.getEmailDocenteFromUsername()`

---

## Limitaciones

⚠️ **Este sistema NO incluye:**
- Interfaz web para gestionar usuarios/roles
- Permisos granulares por módulo desde UI
- Auditoría de cambios de permisos
- Auto-registro de usuarios

✅ **Este sistema SÍ incluye:**
- Roles funcionales (3 tipos)
- Usuarios de prueba
- Validación de permisos en código
- Filtrado por docente autenticado

---

## Para Producción

Si este sistema va a producción, considera:

1. **Migrar a XavaPro** ($500 USD)
   - Gestión completa desde interfaz
   - Auditoría integrada
   - Multitenancy

2. **O implementar tabla de roles propia**
   - Crear entidad `UsuarioRol`
   - Migrar de hardcoded a base de datos
   - Mantener convenciones de nombres

---

**Sistema implementado cumpliendo RNF-05 con las limitaciones de OpenXava gratuito.**
