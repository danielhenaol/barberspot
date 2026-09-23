import redis.clients.jedis.Jedis;
import java.util.HashMap;
import java.util.Map;

public class PocRedis {

    static final String HOST = "creature-supporting-acoustics-82225.db.redis.io"; // sin el puerto
    static final int PUERTO = 16077; // el número después de los ":"
    static final String PASSWORD = "twPt7XCLCWAHQliXQJErAXMVgXAZit0a";

    // Simula la base de datos "de verdad" (en lugar de PostgreSQL)
    static Map<String, String> baseDeDatosSimulada = new HashMap<>();

    public static void main(String[] args) {
        baseDeDatosSimulada.put("servicio:1", "Corte clasico - $25000");

        System.out.println("=== PoC Redis: patron cache-aside (ADR-019) ===\n");

        System.out.println(">> Primera consulta (no deberia estar en cache):");
        consultarServicio("servicio:1");

        System.out.println("\n>> Segunda consulta (ya deberia venir de la cache):");
        consultarServicio("servicio:1");

        System.out.println("\n>> Simulando que Redis no esta disponible:");
        consultarServicioConFallback("servicio:1", true);
    }

    static void consultarServicio(String clave) {
        try (Jedis jedis = new Jedis(HOST, PUERTO)) {
            jedis.auth(PASSWORD);

            String valorEnCache = jedis.get(clave);
            if (valorEnCache != null) {
                System.out.println("  [CACHE HIT] " + valorEnCache);
            } else {
                System.out.println("  [CACHE MISS] Consultando 'base de datos'...");
                String valorReal = baseDeDatosSimulada.get(clave);
                jedis.setex(clave, 60, valorReal); // guarda en cache 60 segundos
                System.out.println("  Guardado en cache: " + valorReal);
            }
        }
    }

    static void consultarServicioConFallback(String clave, boolean simularFalloRedis) {
        try {
            if (simularFalloRedis) throw new RuntimeException("Redis no disponible (simulado)");
            // aqui iria la logica normal con Jedis
        } catch (Exception e) {
            System.out.println("  [FALLBACK] " + e.getMessage() + " -> Consultando 'base de datos' directamente.");
            String valorReal = baseDeDatosSimulada.get(clave);
            System.out.println("  Resultado sin cache: " + valorReal);
        }
    }
}
