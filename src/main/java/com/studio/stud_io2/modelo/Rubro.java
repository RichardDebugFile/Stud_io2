package com.studio.stud_io2.modelo;

import java.math.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;

import lombok.*;

/**
 * Entidad Rubro - Representa un componente de evaluacion (examen, deberes,
 * proyecto, etc.)
 * RF-07: La suma de ponderaciones de todos los rubros de una asignacion debe
 * ser exactamente 100%
 */
@Entity
@Getter
@Setter
@Table(name = "rubros")
@View(members = "asignacion;" +
        "nombre, ponderacion;" +
        "descripcion")
public class Rubro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Required
    private Asignacion asignacion;

    @Column(length = 50, nullable = false)
    @Required
    private String nombre; // Ejemplo: "Examen Final", "Deberes", "Proyecto"

    @Column(precision = 5, scale = 2, nullable = false)
    @Required
    @DecimalMin(value = "0.01", message = "La ponderacion debe ser mayor que 0")
    @DecimalMax(value = "100.00", message = "La ponderacion no puede exceder 100")
    private BigDecimal ponderacion; // Valor entre 0.01 y 100.00

    @Column(length = 200)
    @Stereotype("MEMO")
    private String descripcion;

    /**
     * Before persist/update: Validar que la suma total de ponderaciones sea 100%
     * (RF-07)
     * REGLA DE ORO para Validación y Verificación
     */
    @PrePersist
    @PreUpdate
    private void validarSumaPonderaciones() {
        if (asignacion == null) {
            return; // La validación @Required se encargará
        }

        EntityManager em = org.openxava.jpa.XPersistence.getManager();

        // Calcular la suma de ponderaciones de otros rubros (excluyendo el actual si ya
        // existe)
        String jpql = "SELECT COALESCE(SUM(r.ponderacion), 0) FROM Rubro r " +
                "WHERE r.asignacion.id = :asignacionId";

        if (this.id != null) {
            jpql += " AND r.id != :currentId";
        }

        javax.persistence.Query query = em.createQuery(jpql)
                .setParameter("asignacionId", asignacion.getId());

        if (this.id != null) {
            query.setParameter("currentId", this.id);
        }

        BigDecimal sumaOtros = (BigDecimal) query.getSingleResult();
        BigDecimal sumaTotal = sumaOtros.add(this.ponderacion);

        // Verificar que la suma sea exactamente 100
        if (sumaTotal.compareTo(new BigDecimal("100.00")) > 0) {
            throw new javax.validation.ValidationException(
                    String.format("Error: La suma de ponderaciones excede el 100%%. " +
                            "Suma actual de otros rubros: %.2f%%, " +
                            "Esta ponderación: %.2f%%, " +
                            "Total: %.2f%%",
                            sumaOtros, this.ponderacion, sumaTotal));
        }
    }

    /**
     * Método auxiliar para verificar si la asignación tiene exactamente 100% de
     * ponderación
     */
    public static boolean asignacionTienePonderacionCompleta(Long asignacionId) {
        EntityManager em = org.openxava.jpa.XPersistence.getManager();

        String jpql = "SELECT COALESCE(SUM(r.ponderacion), 0) FROM Rubro r " +
                "WHERE r.asignacion.id = :asignacionId";

        BigDecimal suma = (BigDecimal) em.createQuery(jpql)
                .setParameter("asignacionId", asignacionId)
                .getSingleResult();

        return suma.compareTo(new BigDecimal("100.00")) == 0;
    }

    /**
     * Validación de permisos: Solo académicos y administradores pueden eliminar rubros (CU-07)
     * Los docentes pueden crear y modificar, pero no eliminar
     */
    @PreRemove
    private void validarPermisoEliminar() {
        if (!com.studio.stud_io2.util.SecurityHelper.esAcademicoOSuperior()) {
            throw new javax.validation.ValidationException(
                    "No tiene permisos para eliminar rubros. Solo Académicos y Administradores pueden realizar esta operación.");
        }
    }
}
