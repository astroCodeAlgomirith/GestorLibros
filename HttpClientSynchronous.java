import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;




public class HttpClientSynchronous {
	private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static void main(String[] args) throws IOException, InterruptedException {

		/*
		 * HttpRequest request = HttpRequest.newBuilder() .GET()
		 * .uri(URI.create("http://localhost:8080/ipn")) .setHeader("User-Agent",
		 * "Java 11 HttpClient Bot") // add request header .build();
		 * 
		 * //HttpResponse<String> response = httpClient.send(request,
		 * HttpResponse.BodyHandlers.ofString()); HttpResponse<byte[]> response =
		 * httpClient.send( request, HttpResponse.BodyHandlers.ofByteArray() );
		 * 
		 * 
		 * HttpRequest getStatus = HttpRequest.newBuilder() .GET()
		 * .uri(URI.create("http://localhost:8080/status")) .build();
		 * 
		 * HttpResponse<String> statusResponse = httpClient.send(getStatus,
		 * HttpResponse.BodyHandlers.ofString());
		 * 
		 * HttpRequest postRequest = HttpRequest.newBuilder()
		 * .uri(URI.create("http://localhost:8080/task")) .header("Content-Type",
		 * "text/plain") .header("X-Debug", "true") // tiempo
		 * .POST(HttpRequest.BodyPublishers.ofString("5,10")) .build();
		 * 
		 * HttpResponse<String> responsePost = httpClient.send(postRequest,
		 * HttpResponse.BodyHandlers.ofString());
		 * 
		 * // print response headers HttpHeaders headers = response.headers();
		 * headers.map().forEach((k, v) -> System.out.println(k + ":" + v));
		 * 
		 * // print status code System.out.println(response.statusCode());
		 * 
		 * // print status (endpoint /status) System.out.println("Server status: "+
		 * statusResponse.body());
		 * 
		 * System.out.println("Status: " + responsePost.statusCode());
		 * System.out.println("Body: " + responsePost.body());
		 * 
		 * // print header debug si existe
		 * responsePost.headers().firstValue("X-Debug-Info") .ifPresent(value ->
		 * System.out.println("Debug: " + value));
		 * 
		 * // print response body //System.out.println(response.body());
		 * 
		 * // convertir a String solo si es texto String body = new
		 * String(response.body()); System.out.println(body);
		 * 
		 */
    	// Parte de los chistes jaja
    	
    	HttpRequest jokeAnyRequest = HttpRequest.newBuilder()
    	        .GET()
    	        .uri(URI.create("https://v2.jokeapi.dev/joke/Any?lang=es"))
    	        .build();

    	HttpResponse<String> jokeAnyResponse = httpClient.send(
    	        jokeAnyRequest,
    	        HttpResponse.BodyHandlers.ofString()
    	);

    	System.out.println("Peticion de chiste aleatorio \n");
    	System.out.println("Status: " + jokeAnyResponse.statusCode());
    	System.out.println("Body:\n" + jokeAnyResponse.body());
    	
    	HttpRequest jokeProgrammingRequest = HttpRequest.newBuilder()
    	        .GET()
    	        .uri(URI.create("https://v2.jokeapi.dev/joke/Programming?lang=es"))
    	        .build();

    	HttpResponse<String> jokeProgrammingResponse = httpClient.send(
    	        jokeProgrammingRequest,
    	        HttpResponse.BodyHandlers.ofString()
    	);

    	System.out.println("Peticion de chiste de programacion");
    	System.out.println("Status: " + jokeProgrammingResponse.statusCode());
    	System.out.println("Body:\n" + jokeProgrammingResponse.body());
    	 
    	//Parte de mandar el JSON
    	//Creamos el formato JSON
    	String jsonPost = String.format(
    	        "{\"userId\":1,\"title\":\"%s\",\"body\":\"%s\"}",
    	        "7CM2",       
    	        "Miriam G Ramirez Sanchez"   
    	);

    	// Creamos el request de POST
    	HttpRequest postJsonRequest = HttpRequest.newBuilder()
    	        .uri(URI.create("https://jsonplaceholder.typicode.com/posts"))
    	        .header("Content-Type", "application/json")
    	        .POST(HttpRequest.BodyPublishers.ofString(jsonPost))
    	        .build();

    	// Enviamos el request
    	HttpResponse<String> postJsonResponse = httpClient.send(
    	        postJsonRequest,
    	        HttpResponse.BodyHandlers.ofString()
    	);
    	
    	
    	System.out.println("formato JSON enviado:");
    	System.out.println(jsonPost);

    	System.out.println("\nStatus: " + postJsonResponse.statusCode());

    	System.out.println("Respuesta:");
    	System.out.println(postJsonResponse.body());
    	// API Translation
    	Scanner scanner = new Scanner(System.in);

        System.out.print("Escribe el mensaje que deseas traducir al español: ");
        String mensaje = scanner.nextLine();
        String mensajeEncoded = URLEncoder.encode(mensaje, StandardCharsets.UTF_8)
                .replace("+", "%20");
        String API_KEY = "";

        
    	HttpRequest translationRequest = HttpRequest.newBuilder()
    	        .GET()
    	        .uri(URI.create("https://translation.googleapis.com/language/translate/v2?target=es&key="+API_KEY+
    	        		"&q=%22"+mensajeEncoded+"%22"))
    	        .build();

    	HttpResponse<String> translationResponse = httpClient.send(
    	        translationRequest,
    	        HttpResponse.BodyHandlers.ofString()
    	);

    	System.out.println("Traduccion de "+ mensaje +" al español \n");
    	System.out.println("Status: " + translationResponse.statusCode());
    	System.out.println("Body:\n" + translationResponse.body());
    	scanner.close();
    	
    	
    	
    		
    	
    		
    		
    		
    	
    	// Parte de la clase de registrar varias combinaciones de alummnos 
		/*
		 * ArrayList<String> nombres =
		 * CombinacionesArchivos.leerArchivo("src/nombres.txt"); ArrayList<String>
		 * apellidos = CombinacionesArchivos.leerArchivo("src/apellidos.txt"); Random
		 * random = new Random(12345); // semilla fija // Generar combinaciones for
		 * (String nombre : nombres) { for (String ap1 : apellidos) { for (String ap2 :
		 * apellidos) { //String completo = nombre + " " + ap1 + " " + ap2; double
		 * numero = random.nextDouble() * 100; numero = Math.round(numero * 10) / 100.0;
		 * String json = String.format(
		 * "{\"nombre\":\"%s\",\"apellido1\":\"%s\",\"apellido2\":\"%s\",\"promedio\":%.1f}",
		 * nombre, ap1, ap2, numero );
		 * 
		 * HttpRequest postRequest = HttpRequest.newBuilder()
		 * .uri(URI.create("http://localhost:8080/alumnos")) //.header("Content-Type",
		 * "text/plain") .header("Content-Type", "application/json") .header("X-Debug",
		 * "true") // tiempo .POST(HttpRequest.BodyPublishers.ofString(json))
		 * //.POST(HttpRequest.BodyPublishers.ofString("5,10")) .build();
		 * 
		 * //Envio mi request
		 * 
		 * HttpResponse<String> responsePost = httpClient.send(postRequest,
		 * HttpResponse.BodyHandlers.ofString()); System.out.println("Json: \n");
		 * System.out.println(json); System.out.println("Status: " +
		 * responsePost.statusCode() +"\n"); System.out.println("Body: " +
		 * responsePost.body()+"\n"); // print header debug si existe
		 * responsePost.headers().firstValue("X-Debug-Info") .ifPresent(value ->
		 * System.out.println("Debug: " + value));
		 * 
		 * // print response body //System.out.println(response.body()); } } }
		 */


        
    }
}
