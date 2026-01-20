package com.studio.stud_io2.modelo;

import javax.persistence.*;
import javax.validation.constraints.*;
import org.openxava.annotations.*;
import lombok.*;

/**
 * Entidad Docente - Representa un profesor en el sistema academico
 * RF-02: Garantiza unicidad de Cedula y control de perfil
 */
@Entity
@EntityListeners(com.studio.stud_io2.listeners.AuditListener.class)
@Getter
@Setter
@Table(name = "docentes")
@View(members = "datos{" +
        "cedula, nombre, apellido" +
        "};" +
        "profesional{" +
        "especialidad, tituloAcademico" +
        "};" +
        "contacto{" +
        "email, telefono" +
        "}")
public class Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, unique = true, nullable = false)
    @Required
    @Pattern(regexp = "\\d{10}", message = "La cedula debe tener 10 digitos")
    private String cedula;

    @Column(length = 50, nullable = false)
    @Required
    private String nombre;

    @Column(length = 50, nullable = false)
    @Required
    private String apellido;

    @Column(length = 100, unique = true, nullable = false)
    @Required
    @Email(message = "Formato de email invalido")
    private String email;

    @Column(length = 15, unique = true)
    @Pattern(regexp = "\\d{7,15}", message = "El telefono debe contener entre 7 y 15 digitos")
    private String telefono;

    @Column(length = 100)
    private String especialidad;

    @Column(length = 100, name = "titulo_academico")
    private String tituloAcademico;

    /**
     * Nombre completo calculado para reportes y vistas
     */
    @Transient
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /**
     * Validacion de permisos: Solo academicos y administradores pueden crear docentes (CU-02)
     * RNF-05: Control de acceso basado en roles
     */
    @PrePersist
    private void validarPermisoCrear() {
        if (!com.studio.stud_io2.util.SecurityHelper.esAcademicoOSuperior()) {
            throw new javax.validation.ValidationException(
                    "No tiene permisos para crear docentes. Solo Academicos y Administradores pueden realizar esta operacion.");
        }
    }

    /**
     * Validacion de permisos: Solo administradores pueden modificar docentes (CU-05)
     * Cumple RNF-05: Academicos pueden crear pero no modificar
     */
    @PreUpdate
    private void validarPermisoModificar() {
        if (!com.studio.stud_io2.util.SecurityHelper.esAdministrador()) {
            throw new javax.validation.ValidationException(
                    "Solo los administradores pueden modificar datos de docentes");
        }
    }

    /**
     * Validacion de permisos: Solo admin y academicos pueden eliminar
     */
    @PreRemove
    private void validarPermisoEliminar() {
        if (!com.studio.stud_io2.util.SecurityHelper.esAcademicoOSuperior()) {
            throw new javax.validation.ValidationException(
                    "No tiene permisos para eliminar docentes");
        }
    }
}
