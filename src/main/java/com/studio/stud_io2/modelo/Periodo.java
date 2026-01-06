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
     * Before persist/update: Validar que solo haya un periodo activo (RF-03)
     */
    @PrePersist
    @PreUpdate
    private void validarPeriodoActivoUnico() {
        if (this.activo) {
            EntityManager em = org.openxava.jpa.XPersistence.getManager();

            // Contar periodos activos excluyendo el actual (si tiene ID)
            String jpql;
            Long count;

            if (this.id == null) {
                // Nuevo registro
                jpql = "SELECT COUNT(p) FROM Periodo p WHERE p.activo = true";
                count = (Long) em.createQuery(jpql).getSingleResult();
            } else {
                // Actualización
                jpql = "SELECT COUNT(p) FROM Periodo p WHERE p.activo = true AND p.id != :currentId";
                count = (Long) em.createQuery(jpql)
                        .setParameter("currentId", this.id)
                        .getSingleResult();
            }

            if (count > 0) {
                throw new javax.validation.ValidationException(
                        "Ya existe un periodo activo. Solo puede haber un periodo activo a la vez.");
            }
        }

        // Validación de permisos (CU-03): Solo académicos y administradores
        if (!com.studio.stud_io2.util.SecurityHelper.esAcademicoOSuperior()) {
            throw new javax.validation.ValidationException(
                    "No tiene permisos para gestionar períodos académicos. Solo Académicos y Administradores pueden realizar esta operación.");
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
