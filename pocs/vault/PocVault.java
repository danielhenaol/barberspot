import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PocVault {

    static final String TOKEN = "hvs.MlVdVWv6TGS5ROgYII5hQJvK";
    static final String VAULT_URL = "http://127.0.0.1:8200/v1/secret/data/barberspot";

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        // 1. Guardar una credencial (simula la clave de Brevo)
        String cuerpoGuardar = """
            {"data": {"brevo_api_key": "xkeysib-simulada-12345"}}
            """;
        HttpRequest guardar = HttpRequest.newBuilder()
                .uri(URI.create(VAULT_URL))
                .header("X-Vault-Token", TOKEN)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(cuerpoGuardar))
                .build();
        HttpResponse<String> respGuardar = client.send(guardar, HttpResponse.BodyHandlers.ofString());
        System.out.println("Guardar credencial -> Código: " + respGuardar.statusCode());

        // 2. Leerla de vuelta (simula lo que haría el Backend al iniciar)
        HttpRequest leer = HttpRequest.newBuilder()
                .uri(URI.create(VAULT_URL))
                .header("X-Vault-Token", TOKEN)
                .GET()
                .build();
        HttpResponse<String> respLeer = client.send(leer, HttpResponse.BodyHandlers.ofString());

        System.out.println("\n=== Resultado ===");
        System.out.println("Código: " + respLeer.statusCode());
        System.out.println("Contenido: " + respLeer.body());
    }
}
