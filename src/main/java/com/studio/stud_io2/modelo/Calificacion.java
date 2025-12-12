package com.studio.stud_io2.modelo;

import java.math.*;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;
import org.openxava.calculators.*;

import lombok.*;

/**
 * Entidad Calificacion - Representa la nota de un estudiante en un rubro especifico
 * RF-08: Registro de notas con calculo automatico de promedio y estado final
 */
@Entity
@EntityListeners(com.studio.stud_io2.listeners.AuditListener.class)
@Getter
@Setter
@Table(name = "calificaciones")
@View(members = "matricula, rubro;" +
        "nota, fecha")
public class Calificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Required
    private Matricula matricula;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Required
    private Rubro rubro;

    @Column(precision = 5, scale = 2, nullable = false)
    @Required
    @DecimalMin(value = "0.00", message = "La nota mínima es 0")
    @DecimalMax(value = "100.00", message = "La nota máxima es 100")
    private BigDecimal nota;

    @Column(name = "fecha_registro", nullable = false)
    @Required
    @DefaultValueCalculator(CurrentLocalDateCalculator.class)
    private java.time.LocalDate fecha;

    /**
     * Validacion: La matricula y el rubro deben pertenecer a la misma asignacion
     */
    @PrePersist
    @PreUpdate
    private void validarCoherenciaMatriculaRubro() {
        if (matricula == null || rubro == null) {
            return;
        }

        if (!matricula.getAsignacion().getId().equals(rubro.getAsignacion().getId())) {
            throw new javax.validation.ValidationException(
                    "Error: El rubro no pertenece a la asignación de la matrícula seleccionada");
        }
    }

    /**
     * Calcula el promedio ponderado para una matricula (RF-08)
     * 
     * @param matriculaId ID de la matricula
     * @return Promedio ponderado
     */
    public static BigDecimal calcularPromedioPonderado(Long matriculaId) {
        EntityManager em = org.openxava.jpa.XPersistence.getManager();

        // Obtener todas las calificaciones con sus ponderaciones
        String jpql = "SELECT c.nota, r.ponderacion " +
                "FROM Calificacion c " +
                "JOIN c.rubro r " +
                "WHERE c.matricula.id = :matriculaId";

        @SuppressWarnings("unchecked")
        List<Object[]> resultados = em.createQuery(jpql)
                .setParameter("matriculaId", matriculaId)
                .getResultList();

        if (resultados.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal sumaNotasPonderadas = BigDecimal.ZERO;
        BigDecimal sumaPonderaciones = BigDecimal.ZERO;

        for (Object[] row : resultados) {
            BigDecimal nota = (BigDecimal) row[0];
            BigDecimal ponderacion = (BigDecimal) row[1];

            // nota * (ponderacion / 100)
            BigDecimal notaPonderada = nota.multiply(ponderacion).divide(new BigDecimal("100"), 2,
                    RoundingMode.HALF_UP);
            sumaNotasPonderadas = sumaNotasPonderadas.add(notaPonderada);
            sumaPonderaciones = sumaPonderaciones.add(ponderacion);
        }

        // Si no hay 100% de ponderación registrada, devolver promedio parcial
        if (sumaPonderaciones.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return sumaNotasPonderadas;
    }

    /**
     * Determina el estado final (Aprobado/Reprobado) basado en el promedio (RF-08)
     * 
     * @param promedio Promedio ponderado
     * @return "APROBADO" si >= 70, "REPROBADO" si < 70
     */
    public static String determinarEstadoFinal(BigDecimal promedio) {
        BigDecimal notaMinima = new BigDecimal("70.00");
        return promedio.compareTo(notaMinima) >= 0 ? "APROBADO" : "REPROBADO";
    }
}
