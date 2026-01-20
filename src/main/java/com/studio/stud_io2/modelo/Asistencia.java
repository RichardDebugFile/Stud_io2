package com.studio.stud_io2.modelo;

import javax.persistence.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;

import lombok.*;

/**
 * Entidad Asistencia - Representa el registro de asistencia de un estudiante
 * RF-09: Prevencion de registros duplicados para la misma fecha
 */
@Entity
@Getter
@Setter
@Table(name = "asistencias", uniqueConstraints = @UniqueConstraint(columnNames = { "matricula_id", "fecha" }))
@View(members = "matricula;" +
        "fecha, estado;" +
        "observaciones")
public class Asistencia {

    /**
     * Enum para los estados de asistencia
     * Garantiza que solo se usen valores validos en el sistema
     */
    public enum EstadoAsistencia {
        PRESENTE,
        AUSENTE,
        TARDANZA,
        JUSTIFICADO
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Required
    private Matricula matricula;

    @Column(nullable = false)
    @Required
    @DefaultValueCalculator(CurrentLocalDateCalculator.class)
    private java.time.LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    @Required
    private EstadoAsistencia estado = EstadoAsistencia.PRESENTE;

    @Column(length = 200)
    @Stereotype("MEMO")
    private String observaciones;

    /**
     * Calcula el porcentaje de asistencia para una matricula
     *
     * @param matriculaId ID de la matricula
     * @return Porcentaje de asistencia (0-100)
     */
    public static double calcularPorcentajeAsistencia(Long matriculaId) {
        EntityManager em = org.openxava.jpa.XPersistence.getManager();

        // Total de registros
        String jpqlTotal = "SELECT COUNT(a) FROM Asistencia a WHERE a.matricula.id = :matriculaId";
        Long total = (Long) em.createQuery(jpqlTotal)
                .setParameter("matriculaId", matriculaId)
                .getSingleResult();

        if (total == 0) {
            return 0.0;
        }

        // Total de asistencias (PRESENTE, TARDANZA o JUSTIFICADO)
        String jpqlPresentes = "SELECT COUNT(a) FROM Asistencia a " +
                "WHERE a.matricula.id = :matriculaId " +
                "AND a.estado IN (:presente, :tardanza, :justificado)";
        Long presentes = (Long) em.createQuery(jpqlPresentes)
                .setParameter("matriculaId", matriculaId)
                .setParameter("presente", EstadoAsistencia.PRESENTE)
                .setParameter("tardanza", EstadoAsistencia.TARDANZA)
                .setParameter("justificado", EstadoAsistencia.JUSTIFICADO)
                .getSingleResult();

        return (presentes.doubleValue() / total.doubleValue()) * 100.0;
    }

    /**
     * Validacion de fecha dentro del rango del periodo (CU-09-TC-06)
     */
    @PrePersist
    @PreUpdate
    private void validarFechaEnRangoPeriodo() {
        if (matricula == null || fecha == null) {
            return; // @Required se encargara de esto
        }

        // Obtener el periodo a traves de la asignacion
        if (matricula.getAsignacion() != null && matricula.getAsignacion().getPeriodo() != null) {
            java.time.LocalDate fechaInicio = matricula.getAsignacion().getPeriodo().getFechaInicio();
            java.time.LocalDate fechaFin = matricula.getAsignacion().getPeriodo().getFechaFin();

            if (fechaInicio != null && fechaFin != null) {
                if (fecha.isBefore(fechaInicio) || fecha.isAfter(fechaFin)) {
                    throw new javax.validation.ValidationException(
                            String.format(
                                    "Fecha fuera del rango permitido del periodo. " +
                                            "La fecha debe estar entre %s y %s.",
                                    fechaInicio, fechaFin));
                }
            }
        }
    }

    /**
     * Validacion de permisos: Solo academicos y administradores pueden eliminar registros de asistencia (CU-09)
     * Los docentes pueden crear y modificar, pero no eliminar
     */
    @PreRemove
    private void validarPermisoEliminar() {
        if (!com.studio.stud_io2.util.SecurityHelper.esAcademicoOSuperior()) {
            throw new javax.validation.ValidationException(
                    "No tiene permisos para eliminar registros de asistencia. Solo Academicos y Administradores pueden realizar esta operacion.");
        }
    }
}
