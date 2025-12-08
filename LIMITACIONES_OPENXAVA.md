# IMPORTANTE: Limitaciones de OpenXava Gratuito vs XavaPro

## El Problema Descubierto

**OpenXava versión gratuita NO incluye:**
- ❌ Panel de administración de usuarios
- ❌ Gestión de roles desde interfaz web
- ❌ Permisos granulares por módulo
- ❌ Multitenancy (organizaciones)

**Estos features solo están en XavaPro** (versión comercial - licencia de pago)

---

## Lo Que SÍ Tenemos en Versión Gratuita

✅ **Autenticación básica:**
- Login/Logout
- Usuarios en archivo: `naviox-users.properties`
- Formato simple: `usuario=contraseña`

✅ **Seguridad a nivel de código:**
- Podemos usar anotaciones JPA
- Podemos crear validaciones custom
- Podemos filtrar consultas por usuario

---

## Solución Alternativa: Implementar Roles Manualmente

Dado que no tenemos XavaPro, implementaremos un sistema de roles simplificado:

### Opción 1: Usar Comentarios en naviox-users.properties

```properties
# Administradores
admin=admin
admin_test=admin123

# Académicos  
academico_test=acad123

# Docentes
docente_silva=doc123
docente_vasquez=doc456
```

Luego, en el código, detectar el rol por el nombre del usuario.

### Opción 2: Crear Tabla de Roles Propia

Crear una entidad personalizada que maneje roles:

```java
@Entity
public class UsuarioRol {
    @Id
    private String username;
    
    @Enumerated(EnumType.STRING)
    private TipoRol rol;  // ADMIN, ACADEMICO, DOCENTE
    
    public enum TipoRol {
        ADMINISTRADOR, ACADEMICO, DOCENTE
    }
}
```

### Opción 3: Hardcodear Roles por Ahora

Para cumplir con el requisito de forma simple:

```java
public class SecurityHelper {
    public static String obtenerRol(String username) {
        if (username.equals("admin") || username.startsWith("admin_")) {
            return "ADMINISTRADOR";
        }
        if (username.startsWith("academico_")) {
            return "ACADEMICO";
        }
        if (username.startsWith("docente_")) {
            return "DOCENTE";
        }
        return "USUARIO";  // Default
    }
}
```

---

## Recomendación para Tu Proyecto Académico

**Para Validación y Verificación de Software:**

1. **Documentar la limitación:**
   - OpenXava gratuito no soporta gestión de roles completa
   - Se requiere XavaPro ($500+ USD) para implementar RNF-05 completamente

2. **Implementar Opción 3 (Hardcoded):**
   - Rápida de implementar
   - Cumple el requisito funcionalmente
   - Demuestra que entiendes el concepto

3. **Crear usuarios de prueba en `naviox-users.properties`:**
   ```properties
   admin=admin
   academico_test=acad123
   docente_silva=doc123
   ```

4. **Filtrar consultas basadas en usuario autenticado:**
   - Ejemplo: Un docente solo ve sus secciones
   - Usar `Users.getCurrent()` en queries JPQL

---

## Sobre la Auditoría

El sistema de auditoría que implementamos SÍ debería funcionar con OpenXava gratuito. Si no funciona, probablemente sea porque:

1. **Problema de Transacciones:**
   - El listener intenta crear una transacción dentro de otra
   - Hibernate puede estar rollback todo

2. **Solución:** Usar eventos asíncronos o logging simple

---

## Decisión Requerida

¿Quieres que implemente:

**A) Sistema de roles hardcoded** (2 horas)
- Funciona sin XavaPro
- Cumple requisito básico
- Usuarios de prueba en properties file

**B) Solo documentar limitación** (30 min)
- Explicar que se requiere XavaPro
- Mostrar cómo SE HARÍA si tuviéramos XavaPro
- Justificar en reporte de V&V

**C) Comprar/Obtener trial de XavaPro**
- Tiene trial gratuito de 30 días
- Luego cuesta ~$500 USD
- Daría acceso a todas las funcionalidades

**¿Cuál opción prefieres?**
