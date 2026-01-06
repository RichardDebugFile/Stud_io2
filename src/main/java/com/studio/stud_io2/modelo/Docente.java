package com.studio.stud_io2.modelo;

import javax.persistence.*;
import javax.validation.constraints.*;
import org.openxava.annotations.*;
import lombok.*;

/**
 * Entidad Docente - Representa un profesor en el sistema académico
 * RF-02: Garantiza unicidad de Cédula y control de perfil
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
    @Pattern(regexp = "\\d{10}", message = "La cédula debe tener 10 dígitos")
    private String cedula;

    @Column(length = 50, nullable = false)
    @Required
    private String nombre;

    @Column(length = 50, nullable = false)
    @Required
    private String apellido;

    @Column(length = 100, unique = true, nullable = false)
    @Required
    @Email(message = "Formato de email inválido")
    private String email;

    @Column(length = 15, unique = true)
    @Pattern(regexp = "\\d{7,15}", message = "El teléfono debe contener entre 7 y 15 dígitos")
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
     * Validación de permisos: Solo académicos y administradores pueden crear docentes (CU-02)
     * RNF-05: Control de acceso basado en roles
     */
    @PrePersist
    private void validarPermisoCrear() {
        if (!com.studio.stud_io2.util.SecurityHelper.esAcademicoOSuperior()) {
            throw new javax.validation.ValidationException(
                    "No tiene permisos para crear docentes. Solo Académicos y Administradores pueden realizar esta operación.");
        }
    }

    /**
     * Validación de permisos: Solo administradores pueden modificar docentes (CU-05)
     * Cumple RNF-05: Académicos pueden crear pero no modificar
     */
    @PreUpdate
    private void validarPermisoModificar() {
        if (!com.studio.stud_io2.util.SecurityHelper.esAdministrador()) {
            throw new javax.validation.ValidationException(
                    "Solo los administradores pueden modificar datos de docentes");
        }
    }

    /**
     * Validación de permisos: Solo admin y académicos pueden eliminar
     */
    @PreRemove
    private void validarPermisoEliminar() {
        if (!com.studio.stud_io2.util.SecurityHelper.esAcademicoOSuperior()) {
            throw new javax.validation.ValidationException(
                    "No tiene permisos para eliminar docentes");
        }
    }
}
