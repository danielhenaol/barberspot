import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.RSAPublicKeySpec;
import java.math.BigInteger;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PocAuth0 {

    static final String DOMAIN = "dev-6rgs7jzxbji3x1ij.us.auth0.com";
    static final String TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IlZoTk9SZFdleWFETHFJWXU4QzBfbCJ9.eyJpc3MiOiJodHRwczovL2Rldi02cmdzN2p6eGJqaTN4MWlqLnVzLmF1dGgwLmNvbS8iLCJzdWIiOiJ0NUhCcmp0VVJvRTJTdFA1cEJZVnpKa1k4WEE5dDZ4OUBjbGllbnRzIiwiYXVkIjoiaHR0cHM6Ly9iYXJiZXJzcG90LWFwaSIsImlhdCI6MTc5MDA0MzU5NSwiZXhwIjoxNzkwMTI5OTk1LCJndHkiOiJjbGllbnQtY3JlZGVudGlhbHMiLCJhenAiOiJ0NUhCcmp0VVJvRTJTdFA1cEJZVnpKa1k4WEE5dDZ4OSJ9.ctGGEP5464u1dBaGeoj0rIf_jwEcW0DcBz33C9BmAta1SJ2n1--20vDPOF6WPIgV2Cw38cMAnA-SlfG_mJi9Ky4DoiA8ngu_45jRjw0wzAKUgYaW1yhZZRJ3Loy3F7Vy8TOAHVZKtt6GCaVKgblkkz62BfLRpQ533VKkYXePoKwcarqrmVUK8O0OAnk5wnM72ORaCe_1PvaDUKurTZB682ixsOcr1OZYR974bLnv4QHnxVdc5AU_ag_aasdbBNj2pzu7QulIQsVXAsBJw6q-BGDAftDUa19OYyd6qTyqOwQ1Hge14PKlMFrwyB-oiyqBFQufFBv5l6NqbS3Un7WGlA";

    public static void main(String[] args) throws Exception {
        String[] partes = TOKEN.split("\\.");
        String headerJson = new String(Base64.getUrlDecoder().decode(partes[0]));
        String kid = extraerCampo(headerJson, "kid");
        System.out.println("Algoritmo/kid del token: " + headerJson);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("https://" + DOMAIN + "/.well-known/jwks.json"))
                .build();
        String jwks = client.send(req, HttpResponse.BodyHandlers.ofString()).body();

        String n = extraerLlave(jwks, kid, "n");
        String e = extraerLlave(jwks, kid, "e");

        BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(n));
        BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(e));
        PublicKey publicKey = KeyFactory.getInstance("RSA")
                .generatePublic(new RSAPublicKeySpec(modulus, exponent));

        String datosFirmados = partes[0] + "." + partes[1];
        byte[] firma = Base64.getUrlDecoder().decode(partes[2]);

        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(publicKey);
        sig.update(datosFirmados.getBytes());
        boolean valido = sig.verify(firma);

        System.out.println("\n=== Resultado ===");
        System.out.println(valido
                ? "[OK] El token es AUTÉNTICO: fue firmado por Auth0 y no ha sido alterado."
                : "[RECHAZADO] La firma no es válida.");

        String payload = new String(Base64.getUrlDecoder().decode(partes[1]));
        System.out.println("Contenido del token (payload): " + payload);
    }

    static String extraerCampo(String json, String campo) {
        Matcher m = Pattern.compile("\"" + campo + "\":\"([^\"]+)\"").matcher(json);
        return m.find() ? m.group(1) : null;
    }

    static String extraerLlave(String jwks, String kid, String campo) {
        int idx = jwks.indexOf("\"kid\":\"" + kid + "\"");
        int fin = jwks.indexOf("}", idx);
        String bloque = jwks.substring(Math.max(0, idx - 400), fin);
        return extraerCampo(bloque, campo);
    }
}
