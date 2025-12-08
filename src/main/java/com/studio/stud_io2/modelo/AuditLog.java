package com.studio.stud_io2.modelo;

import javax.persistence.*;
import lombok.*;
import org.openxava.annotations.*;
import java.time.LocalDateTime;

/**
 * Entidad de Auditoría - Registra todas las operaciones CUD del sistema
 * Cumple RNF-06: Auditoría de cambios
 */
@Entity
@Getter
@Setter
@Table(name = "audit_logs")
@Tab(properties = "fecha, usuario, accion, entidad, entidadId, cambios")
@View(members = "fecha, usuario;" +
        "accion, entidad, entidadId;" +
        "cambios")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Hidden
    private Long id;

    @Column(nullable = false, length = 100)
    @Required
    @ReadOnly
    @DisplaySize(20)
    private String usuario;

    @Column(nullable = false, length = 50)
    @Required
    @ReadOnly
    @DisplaySize(20)
    private String entidad;

    @Column(nullable = false)
    @Required
    @ReadOnly
    private Long entidadId;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @Required
    @ReadOnly
    private TipoAccion accion;

    @Column(nullable = false)
    @Required
    @ReadOnly
    private LocalDateTime fecha;

    @Column(length = 2000)
    @ReadOnly
    @Stereotype("MEMO")
    private String cambios;

    @Column(length = 50)
    @ReadOnly
    @DisplaySize(30)
    private String ip;

    public enum TipoAccion {
        CREATE, // Creación
        UPDATE, // Actualización
        DELETE // Eliminación
    }

    @PrePersist
    private void prePersist() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
    }
}
