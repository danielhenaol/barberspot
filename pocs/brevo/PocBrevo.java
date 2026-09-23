import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class PocBrevo {

    static final String API_KEY = "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";
    static final String REMITENTE = "danielhl1812@gmail.com";
    static final String DESTINATARIO = "danielhl1812@gmail.com";

    public static void main(String[] args) throws Exception {
        String cuerpoJson = """
            {
              "sender": {"email": "%s"},
              "to": [{"email": "%s"}],
              "subject": "BarberSpot - Confirmacion de cita (PoC)",
              "textContent": "Tu cita del sabado a las 3:00pm con Juan quedo confirmada."
            }
            """.formatted(REMITENTE, DESTINATARIO);

        boolean enviado = intentarEnviar(cuerpoJson, 3);

        System.out.println("\n=== Resultado final ===");
        System.out.println(enviado
                ? "[OK] Notificacion enviada y confirmada por Brevo."
                : "[FALLIDO] No se pudo enviar tras varios intentos.");
    }

    static boolean intentarEnviar(String cuerpoJson, int maxIntentos) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        for (int intento = 1; intento <= maxIntentos; intento++) {
            System.out.println("Intento " + intento + " de " + maxIntentos + "...");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                    .header("api-key", API_KEY)
                    .header("Content-Type", "application/json")
                    .header("accept", "application/json")
                    .timeout(Duration.ofSeconds(10))
                    .POST(HttpRequest.BodyPublishers.ofString(cuerpoJson))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("  -> Código: " + response.statusCode() + " | " + response.body());

            if (response.statusCode() == 201) return true;
            if (intento < maxIntentos) Thread.sleep(1500);
        }
        return false;
    }
}
