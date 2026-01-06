package com.studio.stud_io2.modelo;

import javax.persistence.*;
import javax.validation.constraints.*;
import org.openxava.annotations.*;
import lombok.*;

/**
 * Entidad Estudiante - Representa un alumno en el sistema académico
 * RF-01: Garantiza unicidad de Cédula y Correo Institucional
 */
@Entity
@EntityListeners(com.studio.stud_io2.listeners.AuditListener.class)
@Getter
@Setter
@Table(name = "estudiantes")
@View(members = "datos{" +
        "cedula, nombre, apellido;" +
        "fechaNacimiento" +
        "};" +
        "contacto{" +
        "email, telefono, direccion" +
        "}")
public class Estudiante {

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

    @Column(name = "fecha_nacimiento")
    private java.time.LocalDate fechaNacimiento;

    @Column(length = 100, unique = true, nullable = false)
    @Required
    @Email(message = "Formato de email inválido")
    private String email;

    @Column(length = 15)
    @Pattern(regexp = "\\d{7,15}", message = "El teléfono debe contener entre 7 y 15 dígitos")
    private String telefono;

    @Column(length = 200)
    @Stereotype("MEMO")
    private String direccion;

    /**
     * Nombre completo calculado para reportes y vistas
     */
    @Transient
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    /**
     * Validación de permisos: Solo académicos y administradores pueden crear estudiantes (CU-01)
     * RNF-05: Control de acceso basado en roles
     */
    @PrePersist
    private void validarPermisoCrear() {
        if (!com.studio.stud_io2.util.SecurityHelper.esAcademicoOSuperior()) {
            throw new javax.validation.ValidationException(
                    "No tiene permisos para crear estudiantes. Solo Académicos y Administradores pueden realizar esta operación.");
        }
    }

    /**
     * Validación de permisos: Solo académicos y administradores pueden modificar estudiantes (CU-01)
     */
    @PreUpdate
    private void validarPermisoModificar() {
        if (!com.studio.stud_io2.util.SecurityHelper.esAcademicoOSuperior()) {
            throw new javax.validation.ValidationException(
                    "No tiene permisos para modificar estudiantes. Solo Académicos y Administradores pueden realizar esta operación.");
        }
    }

    /**
     * Validación de permisos: Solo académicos y administradores pueden eliminar estudiantes (CU-01)
     */
    @PreRemove
    private void validarPermisoEliminar() {
        if (!com.studio.stud_io2.util.SecurityHelper.esAcademicoOSuperior()) {
            throw new javax.validation.ValidationException(
                    "No tiene permisos para eliminar estudiantes. Solo Académicos y Administradores pueden realizar esta operación.");
        }
    }
}
