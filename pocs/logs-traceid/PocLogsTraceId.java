import java.util.UUID;

public class PocLogsTraceId {

    public static void main(String[] args) {
        System.out.println("=== PoC Logs estructurados con traceId (ADR-010) ===\n");

        // Simula 2 solicitudes distintas llegando al sistema
        procesarSolicitud("Agendar cita - cliente Juan");
        System.out.println();
        procesarSolicitud("Cancelar cita - cliente Maria (con error simulado)");
    }

    static void procesarSolicitud(String descripcion) {
        String traceId = UUID.randomUUID().toString().substring(0, 8); // código corto único

        log(traceId, "API", "Solicitud recibida: " + descripcion);
        log(traceId, "Backend", "Validando datos de la solicitud...");

        try {
            if (descripcion.contains("error simulado")) {
                throw new RuntimeException("No se pudo conectar con el módulo de agendamiento");
            }
            log(traceId, "Backend", "Operación completada con éxito");
            System.out.println("[Respuesta al usuario] Tu solicitud se procesó correctamente.");
        } catch (Exception e) {
            log(traceId, "ERROR", "Falla en módulo Backend: " + e.getMessage());
            System.out.println("[Respuesta al usuario] Ocurrió un error. Código de seguimiento: " + traceId);
        }
    }

    static void log(String traceId, String modulo, String mensaje) {
        System.out.println("[traceId=" + traceId + "] [" + modulo + "] " + mensaje);
    }
}
