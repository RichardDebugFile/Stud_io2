package com.studio.stud_io2.util;

import org.openxava.util.Users;

/**
 * Helper de Seguridad - Gestión de roles sin XavaPro
 * 
 * Implementa un sistema de roles basado en convenciones de nombres de usuario
 * ya que OpenXava gratuito no incluye gestión de roles desde interfaz.
 * 
 * CONVENCIONES:
 * - admin* → ADMINISTRADOR
 * - academico_* → ACADEMICO
 * - docente_* → DOCENTE
 * - Otros → USUARIO (sin permisos especiales)
 */
public class SecurityHelper {

    /**
     * Enum de roles del sistema (RNF-05)
     */
    public enum Rol {
        ADMINISTRADOR, // Acceso total
        ACADEMICO, // Gestión académica
        DOCENTE, // Operación de aula
        USUARIO // Usuario genérico (sin permisos)
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
        if (username == null || username.trim().isEmpty()) {
            return Rol.USUARIO;
        }

        String user = username.toLowerCase().trim();

        // ADMINISTRADORES: admin o admin_*
        if (user.equals("admin") || user.startsWith("admin_")) {
            return Rol.ADMINISTRADOR;
        }

        // ACADÉMICOS: academico_*
        if (user.startsWith("academico_")) {
            return Rol.ACADEMICO;
        }

        // DOCENTES: docente_*
        if (user.startsWith("docente_")) {
            return Rol.DOCENTE;
        }

        // Por defecto: usuario sin rol específico
        return Rol.USUARIO;
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
     * Convención: docente_nombre → nombre@profesores.edu.ec
     * 
     * @param username Nombre de usuario (ej: docente_silva)
     * @return Email del docente (ej: roberto.silva@profesores.edu.ec)
     */
    public static String getEmailDocenteFromUsername(String username) {
        if (username == null || !username.startsWith("docente_")) {
            return null;
        }

        // Casos hardcoded para usuarios de prueba
        switch (username.toLowerCase()) {
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
                String nombre = username.substring(8); // Quitar "docente_"
                return nombre + "@profesores.edu.ec";
        }
    }

    /**
     * Valida si el usuario actual tiene permiso para operación específica
     * 
     * @param operacion Tipo de operación (CREATE, READ, UPDATE, DELETE)
     * @param modulo    Nombre del módulo
     * @return true si tiene permiso
     */
    public static boolean tienePermiso(String operacion, String modulo) {
        Rol rol = getRolActual();

        // ADMINISTRADOR: Todo permitido
        if (rol == Rol.ADMINISTRADOR) {
            return true;
        }

        // ACADÉMICO: Casi todo excepto modificar docentes
        if (rol == Rol.ACADEMICO) {
            if (modulo.equals("Docente") && (operacion.equals("UPDATE") || operacion.equals("DELETE"))) {
                return false; // Académicos no pueden modificar/eliminar docentes
            }
            return true;
        }

        // DOCENTE: Solo operaciones de aula en sus secciones
        if (rol == Rol.DOCENTE) {
            // Módulos de solo lectura
            if (modulo.equals("Estudiante") || modulo.equals("Docente") ||
                    modulo.equals("Periodo") || modulo.equals("Curso") ||
                    modulo.equals("Asignacion") || modulo.equals("Matricula")) {
                return operacion.equals("READ");
            }

            // Módulos de gestión (sin eliminar)
            if (modulo.equals("Rubro") || modulo.equals("Calificacion") || modulo.equals("Asistencia")) {
                return !operacion.equals("DELETE");
            }

            return false;
        }

        // USUARIO: Sin permisos por defecto
        return false;
    }
}
