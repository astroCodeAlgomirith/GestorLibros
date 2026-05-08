package source;
import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;
import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.http.HttpConnectTimeoutException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

public class Server {
	private static final String TASK_ENDPOINT = "/task";
	private static final String STATUS_ENDPOINT = "/status";
	private static final String CPU_ENDPOINT = "/cpu";
	
	private final int port;
	private HttpServer server;
    public static void main(String[] args) throws Exception {
    	int serverPort = 8080;
		if(args.length == 1) {
			serverPort = Integer.parseInt(args[0]);
		}
		
		Server webServer = new Server(serverPort);
		webServer.startServer();
		
		System.out.println("Server is listenig on port "+ serverPort);
    	
    }
    public Server(int port) {
		this.port = port;
	}
    
    public void startServer() {
    	try {
    		this.server = HttpServer.create(new InetSocketAddress(port),0);
    	} catch (IOException e) {
			// TODO: handle exception
    		e.printStackTrace();
    		return;
		}
    	HttpContext statusContext = server.createContext(STATUS_ENDPOINT);
    	HttpContext taskContext = server.createContext(TASK_ENDPOINT);
    	HttpContext cpuContext = server.createContext(CPU_ENDPOINT);

    	cpuContext.setHandler(this::handleCpuRequest);
    	statusContext.setHandler(this::handleStatusCheckRequest);
    	taskContext.setHandler(exchange -> {
			try {
				handleTaskRequest(exchange);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
    	
    	
    	server.setExecutor(Executors.newFixedThreadPool(8));
    	server.start();
    }
    private void handleCpuRequest(HttpExchange exchange)
            throws IOException {

        if (!exchange.getRequestMethod()
                .equalsIgnoreCase("GET")) {

            exchange.sendResponseHeaders(405, -1);

            exchange.close();

            return;
        }

        OperatingSystemMXBean osBean =
                ManagementFactory.getPlatformMXBean(
                        OperatingSystemMXBean.class
                );

        double cpu = osBean.getCpuLoad() * 100;

        String response = String.format("{ \"cpu\": %.2f, \"port\": %d }", cpu,port);

        byte[] responseBytes = response.getBytes();

        exchange.getResponseHeaders().add(
                "Content-Type",
                "application/json"
        );
        exchange.sendResponseHeaders(200,responseBytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(responseBytes);
        os.close();
        exchange.close();
    }
    private void handleTaskRequest(HttpExchange exchange) throws Exception {

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            exchange.sendResponseHeaders(405, -1);
            exchange.close();
            return;
        }

        Headers headers = exchange.getRequestHeaders();

        if (headers.containsKey("X-Test") &&
                headers.get("X-Test").get(0).equalsIgnoreCase("true")) {

            String dummyResponse = "123\n";

            sendResponse(dummyResponse.getBytes(), exchange);
            return;
        }

        // Debug
        boolean isDebugMode = false;

        if (headers.containsKey("X-Debug") &&
                headers.get("X-Debug").get(0).equalsIgnoreCase("true")) {

            isDebugMode = true;
        }

        long startTime = System.nanoTime();

        byte[] requestBytes =exchange.getRequestBody().readAllBytes();

        String body = new String(requestBytes);

        Gson gson = new Gson();
        SearchRequest request = gson.fromJson(body, SearchRequest.class);

        System.out.println("Procesando:");
        System.out.println(request);

        
        List<MatchResult> results =
                TextComparator.compareBooks(
                        request.getBookA(),
                        request.getBookB(),
                        request.getN()
                );

        long finishTime = System.nanoTime();
        long duration = finishTime - startTime;
        long seconds = duration / 1_000_000_000;
        long milliseconds = (duration % 1_000_000_000) / 1_000_000;

        // Header debug
        if (isDebugMode) {

            String debugMessage =
                    String.format(
                            "Operacion completada en %d segundos con %d ms",
                            seconds,
                            milliseconds
                    );

            exchange.getResponseHeaders().put(
                    "X-Debug-Info",
                    Arrays.asList(debugMessage)
            );
        }

        // Convertir resultados a JSON
        String jsonResponse =
                gson.toJson(results);

        byte[] responseBytes =
                jsonResponse.getBytes();

        // Headers
        exchange.getResponseHeaders().add(
                "Content-Type",
                "application/json"
        );

        exchange.sendResponseHeaders(
                200,
                responseBytes.length
        );

        OutputStream os =
                exchange.getResponseBody();

        os.write(responseBytes);

        os.close();

        exchange.close();
    }

	private void sendResponse(byte[] responseBytes, HttpExchange exchange) throws IOException {
		exchange.sendResponseHeaders(200, responseBytes.length);
		OutputStream outputStream = exchange.getResponseBody();
		outputStream.write(responseBytes);
		outputStream.flush();
		outputStream.close();
		exchange.close();
		
		
	}

	
	private void handleStatusCheckRequest(HttpExchange exchange) throws IOException {
		if(!exchange.getRequestMethod().equalsIgnoreCase("get")) {
    		exchange.close();
    		return;
    	}
		String responseMessage = "El servidor esta vivo en el puerto:"+ port + "\n";
		sendResponse(responseMessage.getBytes(), exchange);
	}
	
	
    
    
}
