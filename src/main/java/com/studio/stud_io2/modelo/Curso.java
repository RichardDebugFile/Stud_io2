package com.studio.stud_io2.modelo;

import javax.persistence.*;
import javax.validation.constraints.*;
import org.openxava.annotations.*;
import lombok.*;

/**
 * Entidad Curso - Representa una materia del catalogo academico
 * RF-04: Catalogo de materias y creditos
 */
@Entity
@Getter
@Setter
@Table(name = "cursos")
@View(members = "codigo, nombre;" +
        "academico{" +
        "creditos, horasPorSemana" +
        "};" +
        "descripcion")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10, unique = true, nullable = false)
    @Required
    private String codigo; // Ejemplo: "MAT101", "PROG202"

    @Column(length = 100, nullable = false)
    @Required
    private String nombre;

    @Column(nullable = false)
    @Min(value = 1, message = "Los creditos deben ser al menos 1")
    @Max(value = 10, message = "Los creditos no pueden exceder 10")
    @Required
    private Integer creditos;

    @Column(name = "horas_por_semana")
    @Min(value = 1, message = "Las horas por semana deben ser al menos 1")
    @Max(value = 20, message = "Las horas por semana no pueden exceder 20")
    private Integer horasPorSemana;

    @Column(length = 500)
    @Stereotype("MEMO")
    private String descripcion;

    /**
     * Validacion de permisos: Solo academicos y administradores pueden crear cursos (CU-04)
     */
    @PrePersist
    private void validarPermisoCrear() {
        if (!com.studio.stud_io2.util.SecurityHelper.esAcademicoOSuperior()) {
            throw new javax.validation.ValidationException(
                    "No tiene permisos para crear cursos. Solo Academicos y Administradores pueden realizar esta operacion.");
        }
    }

    /**
     * Validacion de permisos: Solo academicos y administradores pueden modificar cursos (CU-04)
     */
    @PreUpdate
    private void validarPermisoModificar() {
        if (!com.studio.stud_io2.util.SecurityHelper.esAcademicoOSuperior()) {
            throw new javax.validation.ValidationException(
                    "No tiene permisos para modificar cursos. Solo Academicos y Administradores pueden realizar esta operacion.");
        }
    }

    /**
     * Validacion de permisos: Solo academicos y administradores pueden eliminar cursos (CU-04)
     */
    @PreRemove
    private void validarPermisoEliminar() {
        if (!com.studio.stud_io2.util.SecurityHelper.esAcademicoOSuperior()) {
            throw new javax.validation.ValidationException(
                    "No tiene permisos para eliminar cursos. Solo Academicos y Administradores pueden realizar esta operacion.");
        }
    }
}
