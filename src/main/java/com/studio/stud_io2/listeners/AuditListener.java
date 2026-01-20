package com.studio.stud_io2.listeners;

import javax.persistence.*;
import com.studio.stud_io2.modelo.AuditLog;
import org.openxava.jpa.XPersistence;
import org.openxava.util.Users;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

/**
 * Listener JPA SIMPLIFICADO para auditoria
 * Version corregida que funciona con las transacciones de OpenXava
 */
public class AuditListener {

    @PostPersist
    public void onPostPersist(Object entity) {
        logAction(entity, AuditLog.TipoAccion.CREATE);
    }

    @PostUpdate
    public void onPostUpdate(Object entity) {
        logAction(entity, AuditLog.TipoAccion.UPDATE);
    }

    @PostRemove
    public void onPostRemove(Object entity) {
        logAction(entity, AuditLog.TipoAccion.DELETE);
    }

    private void logAction(Object entity, AuditLog.TipoAccion accion) {
        try {
            // Evitar loops infinitos
            if (entity instanceof AuditLog) {
                return;
            }

            // Crear log en la MISMA transaccion (sin begin/commit manual)
            EntityManager em = XPersistence.getManager();

            AuditLog log = new AuditLog();
            log.setUsuario(getCurrentUser());
            log.setEntidad(entity.getClass().getSimpleName());
            log.setEntidadId(extractId(entity));
            log.setAccion(accion);
            log.setFecha(LocalDateTime.now());
            log.setCambios(generarDescripcion(entity, accion));
            log.setIp("localhost");

            // SOLO persist - la transaccion es manejada por OpenXava
            em.persist(log);

        } catch (Exception e) {
            // Silently fail - no queremos romper la operacion principal
            System.err.println("[AUDIT] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getCurrentUser() {
        try {
            String user = Users.getCurrent();
            return (user != null && !user.trim().isEmpty()) ? user : "SYSTEM";
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }

    private Long extractId(Object entity) {
        try {
            // Intentar con getter primero
            return (Long) entity.getClass().getMethod("getId").invoke(entity);
        } catch (Exception e) {
            // Buscar con reflexion
            try {
                for (Field field : entity.getClass().getDeclaredFields()) {
                    if (field.isAnnotationPresent(Id.class)) {
                        field.setAccessible(true);
                        Object value = field.get(entity);
                        return value != null ? ((Number) value).longValue() : 0L;
                    }
                }
            } catch (Exception ex) {
                // Ignore
            }
            return 0L;
        }
    }

    private String generarDescripcion(Object entity, AuditLog.TipoAccion accion) {
        String entityName = entity.getClass().getSimpleName();
        Long id = extractId(entity);

        switch (accion) {
            case CREATE:
                return String.format("Creo %s #%d", entityName, id);
            case UPDATE:
                return String.format("Modifico %s #%d", entityName, id);
            case DELETE:
                return String.format("Elimino %s #%d", entityName, id);
            default:
                return "Accion desconocida";
        }
    }
}
