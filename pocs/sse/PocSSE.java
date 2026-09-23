import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class PocSSE {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);

        server.createContext("/agenda-eventos", exchange -> {
            exchange.getResponseHeaders().add("Content-Type", "text/event-stream");
            exchange.getResponseHeaders().add("Cache-Control", "no-cache");
            exchange.sendResponseHeaders(200, 0);
            OutputStream os = exchange.getResponseBody();

            System.out.println("Barbero conectado, esperando eventos...");

            for (int i = 1; i <= 5; i++) {
                String evento = "data: Cambio en la cita #" + i + " (cliente agendo una nueva hora)\n\n";
                os.write(evento.getBytes());
                os.flush();
                System.out.println("Enviado: " + evento.trim());
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            os.close();
        });

        server.start();
        System.out.println("Servidor SSE corriendo en http://localhost:8080/agenda-eventos");
    }
}
