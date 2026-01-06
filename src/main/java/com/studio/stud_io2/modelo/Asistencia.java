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

    @Column(length = 20, nullable = false)
    @Required
    private String estado = "PRESENTE"; // PRESENTE, AUSENTE, TARDANZA, JUSTIFICADO

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

        // Total de asistencias (PRESENTE o TARDANZA)
        String jpqlPresentes = "SELECT COUNT(a) FROM Asistencia a " +
                "WHERE a.matricula.id = :matriculaId " +
                "AND a.estado IN ('PRESENTE', 'TARDANZA', 'JUSTIFICADO')";
        Long presentes = (Long) em.createQuery(jpqlPresentes)
                .setParameter("matriculaId", matriculaId)
                .getSingleResult();

        return (presentes.doubleValue() / total.doubleValue()) * 100.0;
    }

    /**
     * Validación de permisos: Solo académicos y administradores pueden eliminar registros de asistencia (CU-09)
     * Los docentes pueden crear y modificar, pero no eliminar
     */
    @PreRemove
    private void validarPermisoEliminar() {
        if (!com.studio.stud_io2.util.SecurityHelper.esAcademicoOSuperior()) {
            throw new javax.validation.ValidationException(
                    "No tiene permisos para eliminar registros de asistencia. Solo Académicos y Administradores pueden realizar esta operación.");
        }
    }
}
