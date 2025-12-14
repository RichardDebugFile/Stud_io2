package com.studio.stud_io2.modelo;

import java.time.*;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.openxava.annotations.*;

import lombok.*;

/**
 * Entidad Asignacion - Representa una seccion de curso asignada a un docente en
 * un periodo
 * RF-05: Validacion de conflictos de horario docente (solapes)
 */
@Entity
@Getter
@Setter
@Table(name = "asignaciones")
@View(members = "principal{" +
        "curso, docente, periodo;" +
        "seccion, cupoMaximo" +
        "};" +
        "horario{" +
        "horarioInicio, horarioFin, diasSemana" +
        "}")
public class Asignacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Required
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Required
    private Docente docente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @Required
    private Periodo periodo;

    @Column(length = 10, nullable = false)
    @Required
    private String seccion; // Ejemplo: "A", "B", "M1"

    @Column(name = "cupo_maximo", nullable = false)
    @Min(value = 1, message = "El cupo máximo debe ser al menos 1")
    @Max(value = 100, message = "El cupo máximo no puede exceder 100")
    @Required
    private Integer cupoMaximo;

    @Column(name = "horario_inicio")
    private LocalTime horarioInicio;

    @Column(name = "horario_fin")
    private LocalTime horarioFin;

    @Column(name = "dias_semana", length = 50)
    private String diasSemana; // Ejemplo: "Lunes,Miércoles,Viernes"

    /**
     * Relación con matrículas
     */
    @OneToMany(mappedBy = "asignacion")
    private Collection<Matricula> matriculas;

    /**
     * Validación de horarios: Hora inicio debe ser anterior a hora fin
     */
    @AssertTrue(message = "La hora de inicio debe ser anterior a la hora de fin")
    public boolean isHorariosValidos() {
        if (horarioInicio == null || horarioFin == null) {
            return true; // Opcional
        }
        return horarioInicio.isBefore(horarioFin);
    }

    /**
     * Before persist: Validar que el periodo este activo (CU-06)
     * Solo se pueden crear asignaciones en periodos activos
     */
    @PrePersist
    private void validarPeriodoActivo() {
        if (periodo == null) {
            return; // La validación @Required se encargará
        }

        if (!periodo.isActivo()) {
            throw new javax.validation.ValidationException(
                    "El periodo académico no permite nuevas asignaciones. " +
                            "Solo se pueden crear asignaciones en periodos activos.");
        }
    }

    /**
     * Before persist/update: Validar conflictos de horario (RF-05)
     * Un docente no puede tener dos asignaciones con horarios superpuestos en el
     * mismo periodo
     */
    @PrePersist
    @PreUpdate
    private void validarConflictosHorario() {
        if (docente == null || periodo == null || horarioInicio == null || horarioFin == null) {
            return; // Las validaciones @Required se encargarán
        }

        EntityManager em = org.openxava.jpa.XPersistence.getManager();

        // Buscar otras asignaciones del mismo docente en el mismo periodo
        String jpql = "SELECT a FROM Asignacion a WHERE a.docente.id = :docenteId " +
                "AND a.periodo.id = :periodoId " +
                "AND a.horarioInicio IS NOT NULL AND a.horarioFin IS NOT NULL";

        if (this.id != null) {
            jpql += " AND a.id != :currentId";
        }

        javax.persistence.TypedQuery<Asignacion> query = em.createQuery(jpql, Asignacion.class)
                .setParameter("docenteId", docente.getId())
                .setParameter("periodoId", periodo.getId());

        if (this.id != null) {
            query.setParameter("currentId", this.id);
        }

        List<Asignacion> otrasAsignaciones = query.getResultList();

        // Verificar superposición de horarios en los mismos días
        for (Asignacion otra : otrasAsignaciones) {
            if (haySuposicionDias(this.diasSemana, otra.getDiasSemana()) &&
                    haySuposicionHorarios(this.horarioInicio, this.horarioFin,
                            otra.getHorarioInicio(), otra.getHorarioFin())) {
                throw new javax.validation.ValidationException(
                        String.format(
                                "Conflicto de horario: El docente %s ya tiene asignada la sección %s del curso %s " +
                                        "que se superpone con este horario (%s a %s)",
                                docente.getNombreCompleto(),
                                otra.getSeccion(),
                                otra.getCurso().getNombre(),
                                otra.getHorarioInicio(),
                                otra.getHorarioFin()));
            }
        }
    }

    /**
     * Verifica si dos rangos de horarios se superponen
     */
    private boolean haySuposicionHorarios(LocalTime inicio1, LocalTime fin1,
            LocalTime inicio2, LocalTime fin2) {
        // Dos rangos se superponen si: inicio1 < fin2 && fin1 > inicio2
        return inicio1.isBefore(fin2) && fin1.isAfter(inicio2);
    }

    /**
     * Verifica si dos conjuntos de días tienen al menos un día en común
     */
    private boolean haySuposicionDias(String dias1, String dias2) {
        if (dias1 == null || dias2 == null || dias1.isEmpty() || dias2.isEmpty()) {
            return false;
        }

        Set<String> set1 = new HashSet<>(Arrays.asList(dias1.split(",")));
        Set<String> set2 = new HashSet<>(Arrays.asList(dias2.split(",")));

        set1.retainAll(set2); // Intersección
        return !set1.isEmpty();
    }

    /**
     * Obtiene el número de estudiantes matriculados actualmente
     */
    @Transient
    public int getMatriculasActivas() {
        if (matriculas == null)
            return 0;
        return (int) matriculas.stream()
                .filter(m -> "ACTIVA".equals(m.getEstado()))
                .count();
    }

    /**
     * Verifica si hay cupos disponibles
     */
    @Transient
    public boolean hayCuposDisponibles() {
        return getMatriculasActivas() < cupoMaximo;
    }
}
