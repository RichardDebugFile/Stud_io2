package com.studio.stud_io2.util;

import org.openxava.util.*;

/**
 * Helper de Seguridad - Gestión de roles sin XavaPro
 * 
 * Implementa un sistema de roles basado en convenciones de nombres de usuario
 * ya que OpenXava gratuito no incluye gestión de roles desde interfaz.
 * 
 * CONVENCIONES:
 * - admin* =†’ ADMINISTRADOR
 * - academico_* =†’ ACADEMICO
 * - docente_* = DOCENTE
 * - Otros =’ USUARIO (sin permisos especiales)
 */
public class SecurityHelper {

    /**
     * Enum de roles del sistema (RNF-05)
     */
    public enum Rol {
        ADMINISTRADOR, // Acceso total
        ACADEMICO, // GestiÃ³n acadÃ©mica
        DOCENTE, // OperaciÃ³n de aula
        USUARIO // Usuario genÃ©rico (sin permisos)
    }

    /**
     * Obtiene el rol del usuario actualmente autenticado
     * 
     * @return Rol del usuario actual
     */
    public static Rol getRolActual() {
        try {
            String username = Users.getCurrent();
            return getRol(username);
        } catch (Exception e) {
            return Rol.USUARIO;
        }
    }

    /**
     * Determina el rol basándose en el nombre de usuario
     * 
     * @param username Nombre de usuario
     * @return Rol correspondiente
     */
    
    public static Rol getRol(String username) {
    	// Decisión D1
        if (username == null || username.trim().isEmpty()) {
            return Rol.USUARIO; // Salida E1
        }

        String user = username.toLowerCase().trim();

        // ADMINISTRADORES: admin o admin_*
        // Decisión D2
        if (user.equals("admin") || user.startsWith("admin_")) {
            return Rol.ADMINISTRADOR; // Salida E2
        }

        // ACADÉMICOS: academico_*
        //Decisión D3
        if (user.startsWith("academico_")) {
            return Rol.ACADEMICO; // Salida E3
        }

        // DOCENTES: docente_*
        // Decisión D4
        if (user.startsWith("docente_")) {
            return Rol.DOCENTE; // Salida E4
        }

        // Por defecto: usuario sin rol especÃ­fico
        return Rol.USUARIO; // Salida E5
    }

    /**
     * Verifica si el usuario actual es administrador
     */
    public static boolean esAdministrador() {
        return getRolActual() == Rol.ADMINISTRADOR;
    }

    /**
     * Verifica si el usuario actual es académico o administrador
     */
    public static boolean esAcademicoOSuperior() {
        Rol rol = getRolActual();
        return rol == Rol.ADMINISTRADOR || rol == Rol.ACADEMICO;
    }

    /**
     * Verifica si el usuario actual es docente
     */
    public static boolean esDocente() {
        return getRolActual() == Rol.DOCENTE;
    }

    /**
     * Obtiene el username actual
     */
    public static String getUsuarioActual() {
        try {
            return Users.getCurrent();
        } catch (Exception e) {
            return "anonymous";
        }
    }

    /**
     * Obtiene el email del docente basándose en el username
     * Convención: docente_nombre =†’ nombre@profesores.edu.ec
     * 
     * @param username Nombre de usuario (ej: docente_silva)
     * @return Email del docente (ej: roberto.silva@profesores.edu.ec)
     */
    public static String getEmailDocenteFromUsername(String username) {
        if (username == null) {
            return null;
        }

        String user = username.toLowerCase();

        if (!user.startsWith("docente_")) {
            return null;
        }

        // Casos hardcoded para usuarios de prueba
        switch (user) {
            case "docente_silva":
                return "roberto.silva@profesores.edu.ec";
            case "docente_vasquez":
                return "patricia.vasquez@profesores.edu.ec";
            case "docente_castro":
                return "fernando.castro@profesores.edu.ec";
            case "docente_ruiz":
                return "gabriela.ruiz@profesores.edu.ec";
            case "docente_mendoza":
                return "andres.mendoza@profesores.edu.ec";
            default:
                // Generar email basado en convención
                String nombre = user.substring(8); // Quitar "docente_"
                return nombre + "@profesores.edu.ec";
        }
    }

    /**
     * Valida si el usuario actual tiene permiso para operación específica
     * 
     * @param operacion Tipo de operaciÃ³n (CREATE, READ, UPDATE, DELETE)
     * @param modulo    Nombre del mÃ³dulo
     * @return true si tiene permiso
     */
    public static boolean tienePermiso(String operacion, String modulo) {
        Rol rol = getRolActual();

        // ADMINISTRADOR: Todo permitido
        if (rol == Rol.ADMINISTRADOR) {
            return true;
        }

        // ACADÃ‰MICO: Casi todo excepto modificar docentes
        if (rol == Rol.ACADEMICO) {
            if (modulo.equals("Docente") && (operacion.equals("UPDATE") || operacion.equals("DELETE"))) {
                return false; // AcadÃ©micos no pueden modificar/eliminar docentes
            }
            return true;
        }

        // DOCENTE: Solo operaciones de aula en sus secciones
        if (rol == Rol.DOCENTE) {
            // MÃ³dulos de solo lectura
            if (modulo.equals("Estudiante") || modulo.equals("Docente") ||
                    modulo.equals("Periodo") || modulo.equals("Curso") ||
                    modulo.equals("Asignacion") || modulo.equals("Matricula")) {
                return operacion.equals("READ");
            }

            // MÃ³dulos de gestiÃ³n (sin eliminar)
            if (modulo.equals("Rubro") || modulo.equals("Calificacion") || modulo.equals("Asistencia")) {
                return !operacion.equals("DELETE");
            }

            return false;
        }

        // USUARIO: Sin permisos por defecto
        return false;
    }
}
