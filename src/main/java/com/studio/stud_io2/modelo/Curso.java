package com.studio.stud_io2.modelo;

import javax.persistence.*;
import javax.validation.constraints.*;
import org.openxava.annotations.*;
import lombok.*;

/**
 * Entidad Curso - Representa una materia del catálogo académico
 * RF-04: Catálogo de materias y créditos
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
    @Min(value = 1, message = "Los créditos deben ser al menos 1")
    @Max(value = 10, message = "Los créditos no pueden exceder 10")
    @Required
    private Integer creditos;

    @Column(name = "horas_por_semana")
    @Min(value = 1, message = "Las horas por semana deben ser al menos 1")
    @Max(value = 20, message = "Las horas por semana no pueden exceder 20")
    private Integer horasPorSemana;

    @Column(length = 500)
    @Stereotype("MEMO")
    private String descripcion;
}
