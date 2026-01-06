package com.studio.stud_io2.modelo;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;

import lombok.*;

/**
 * Entidad Matricula - Representa la inscripcion de un estudiante en una seccion
 * RF-06: Validacion de cupos disponibles y prevencion de duplicados
 */
@Entity
@Getter
@Setter
@Table(name = "matriculas", uniqueConstraints = @UniqueConstraint(columnNames = { "estudiante_id", "asignacion_id" }))
@View(members = "estudiante, asignacion;" +
        "fechaMatricula, estado")
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Required
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Required
    private Asignacion asignacion;

    @Column(name = "fecha_matricula", nullable = false)
    @Required
    @DefaultValueCalculator(CurrentLocalDateCalculator.class)
    private java.time.LocalDate fechaMatricula;

    @Column(length = 20, nullable = false)
    @Required
    private String estado = "ACTIVA"; // ACTIVA, CANCELADA, RETIRADA

    /**
     * Before persist: Validar cupos disponibles y permisos (RF-06)
     */
    @PrePersist
    private void validarCuposYPermisos() {
        // Validación de permisos: Solo académicos y administradores pueden crear matrículas
        if (!com.studio.stud_io2.util.SecurityHelper.esAcademicoOSuperior()) {
            throw new javax.validation.ValidationException(
                    "No tiene permisos para crear matrículas. Solo Académicos y Administradores pueden realizar esta operación.");
        }

        if (asignacion == null) {
            return; // La validación @Required se encargará
        }

        EntityManager em = org.openxava.jpa.XPersistence.getManager();

        // Contar matrículas activas en esta asignación
        String jpql = "SELECT COUNT(m) FROM Matricula m " +
                "WHERE m.asignacion.id = :asignacionId " +
                "AND m.estado = 'ACTIVA'";

        Long count = (Long) em.createQuery(jpql)
                .setParameter("asignacionId", asignacion.getId())
                .getSingleResult();

        if (count >= asignacion.getCupoMaximo()) {
            throw new javax.validation.ValidationException(
                    String.format("No hay cupos disponibles en la sección %s del curso %s. " +
                            "Cupo máximo: %d, Matriculados: %d",
                            asignacion.getSeccion(),
                            asignacion.getCurso().getNombre(),
                            asignacion.getCupoMaximo(),
                            count));
        }
    }

    /**
     * Validación de permisos: Solo académicos y administradores pueden modificar matrículas
     */
    @PreUpdate
    private void validarPermisoModificar() {
        if (!com.studio.stud_io2.util.SecurityHelper.esAcademicoOSuperior()) {
            throw new javax.validation.ValidationException(
                    "No tiene permisos para modificar matrículas. Solo Académicos y Administradores pueden realizar esta operación.");
        }
    }

    /**
     * Validación de permisos: Solo académicos y administradores pueden eliminar matrículas
     */
    @PreRemove
    private void validarPermisoEliminar() {
        if (!com.studio.stud_io2.util.SecurityHelper.esAcademicoOSuperior()) {
            throw new javax.validation.ValidationException(
                    "No tiene permisos para eliminar matrículas. Solo Académicos y Administradores pueden realizar esta operación.");
        }
    }
}
