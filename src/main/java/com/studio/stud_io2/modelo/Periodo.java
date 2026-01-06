package com.studio.stud_io2.modelo;

import javax.persistence.*;
import javax.validation.constraints.*;
import org.openxava.annotations.*;
import lombok.*;

/**
 * Entidad Periodo - Representa un periodo lectivo/académico
 * RF-03: Fecha Inicio debe ser anterior a Fecha Fin
 * RF-03: Solo puede existir un periodo activo simultáneamente
 */
@Entity
@Getter
@Setter
@Table(name = "periodos")
@View(members = "nombre, activo;" +
        "fechas{" +
        "fechaInicio, fechaFin" +
        "}")
public class Periodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    @Required
    private String nombre; // Ejemplo: "2025-A", "Primer Semestre 2025"

    @Column(name = "fecha_inicio", nullable = false)
    @Required
    private java.time.LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    @Required
    private java.time.LocalDate fechaFin;

    @Column(nullable = false)
    private boolean activo = false;

    /**
     * Validación: Fecha Inicio debe ser anterior a Fecha Fin (RF-03)
     */
    @AssertTrue(message = "La fecha de inicio debe ser anterior a la fecha de fin")
    public boolean isFechasValidas() {
        if (fechaInicio == null || fechaFin == null) {
            return true; // La validación @Required se encargará
        }
        return fechaInicio.isBefore(fechaFin);
    }

    /**
     * Validación de permisos antes de cualquier operación (CU-03)
     */
    @PrePersist
    @PreUpdate
    private void validarPermisos() {
        // Validación de permisos PRIMERO (rápido, sin queries)
        if (!com.studio.stud_io2.util.SecurityHelper.esAcademicoOSuperior()) {
            throw new javax.validation.ValidationException(
                    "No tiene permisos para gestionar períodos académicos. Solo Académicos y Administradores pueden realizar esta operación.");
        }

        // Validación de período activo único (RF-03)
        if (this.activo) {
            EntityManager em = org.openxava.jpa.XPersistence.getManager();

            // Deshabilitar auto-flush temporalmente para evitar recursión infinita
            javax.persistence.FlushModeType flushModeOriginal = em.getFlushMode();
            em.setFlushMode(javax.persistence.FlushModeType.COMMIT);

            try {
                // Usar SQL nativo para evitar StackOverflowError (no activa callbacks JPA)
                String sql = "SELECT COUNT(*) FROM periodos WHERE activo = true";

                javax.persistence.Query query;
                if (this.id == null) {
                    // Nuevo registro - buscar cualquier período activo
                    query = em.createNativeQuery(sql);
                } else {
                    // Actualización - excluir el período actual
                    sql += " AND id != ?";
                    query = em.createNativeQuery(sql);
                    query.setParameter(1, this.id);
                }

                Long count = ((Number) query.getSingleResult()).longValue();

                if (count > 0) {
                    throw new javax.validation.ValidationException(
                            "Ya existe un periodo activo. Solo puede haber un periodo activo a la vez.");
                }
            } finally {
                // Restaurar el flush mode original
                em.setFlushMode(flushModeOriginal);
            }
        }
    }

    /**
     * Validación de permisos: Solo académicos y administradores pueden eliminar períodos (CU-03)
     */
    @PreRemove
    private void validarPermisoEliminar() {
        if (!com.studio.stud_io2.util.SecurityHelper.esAcademicoOSuperior()) {
            throw new javax.validation.ValidationException(
                    "No tiene permisos para eliminar períodos. Solo Académicos y Administradores pueden realizar esta operación.");
        }
    }
}
