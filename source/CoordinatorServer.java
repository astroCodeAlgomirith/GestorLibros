/*
 * PROYECTO 4 Y 5
 * Nombre: Miriam G Ramirez Sanchez
 * Grupo: 7CM2
 */

package source;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class CoordinatorServer {

    private static final int PORT = 8080;

    public static void main(String[] args)
            throws Exception {

        HttpServer server =
                HttpServer.create(
                        new InetSocketAddress(PORT),
                        0
                );

        server.createContext(
                "/search",
                CoordinatorServer::handleSearch
        );

        server.setExecutor(
                Executors.newFixedThreadPool(8)
        );

        server.start();

        System.out.println(
                "Coordinator activo en puerto "
                        + PORT
        );
    }

    private static void handleSearch(
            HttpExchange exchange
    ) throws IOException {

        try {

            // Verificar metodo
            if (!exchange.getRequestMethod()
                    .equalsIgnoreCase("GET")) {

                exchange.sendResponseHeaders(405, -1);

                exchange.close();

                return;
            }

            // Obtener parametros
            Map<String, String> params =
                    QueryUtils.queryToMap(
                            exchange.getRequestURI()
                                    .getQuery()
                    );

            int n = Integer.parseInt(
                    params.getOrDefault(
                            "n",
                            "3"
                    )
            );

            int page = Integer.parseInt(
                    params.getOrDefault(
                            "page",
                            "1"
                    )
            );

            int size = Integer.parseInt(
                    params.getOrDefault(
                            "size",
                            "50"
                    )
            );

            System.out.println(
                    "Busqueda recibida:"
            );

            System.out.println(
                    "n = " + n
            );

            System.out.println(
                    "page = " + page
            );

            System.out.println(
                    "size = " + size
            );

            // Leer libros
            File folder =
                    new File(
                            "resources/LIBROS_TXT"
                    );

            File[] files =
                    folder.listFiles(
                            (dir, name) ->
                                    name.endsWith(".txt")
                    );

            if (files == null ||
                    files.length == 0) {

                String error =
                        "{ \"error\": \"No hay libros\" }";

                byte[] errorBytes =
                        error.getBytes();

                exchange.sendResponseHeaders(
                        500,
                        errorBytes.length
                );

                OutputStream os =
                        exchange.getResponseBody();

                os.write(errorBytes);

                os.close();

                exchange.close();

                return;
            }

            // Lista de libros
            List<String> books =
                    new ArrayList<>();

            for (File file : files) {

                books.add(
                        file.getPath()
                );
            }

            System.out.println(
                    "Libros encontrados: "
                            + books.size()
            );

            // Generar tareas
            List<SearchRequest> tasks =
                    new ArrayList<>();

            for (int i = 0;
                 i < books.size();
                 i++) {

                for (int j = i + 1;
                     j < books.size();
                     j++) {

                    SearchRequest request =
                            new SearchRequest(
                                    books.get(i),
                                    books.get(j),
                                    n
                            );

                    tasks.add(request);
                }
            }

            System.out.println(
                    "Tareas generadas: "
                            + tasks.size()
            );

            // Workers GCP
            List<String> workers = List.of(

			/*
			 * "http://localhost:8081/task", "http://localhost:8082/task",
			 * "http://localhost:8083/task"
			 */


                    "http://34.10.214.254:8080/task",
                    "http://35.239.170.50:8080/task",
                    "http://34.9.79.32:8080/task"
            );

            // Aggregator
            Aggregator aggregator =
                    new Aggregator();

            System.out.println(
                    "Enviando tareas a workers..."
            );

            List<MatchResult> results =
                    aggregator.sendTasksToWorkers(
                            workers,
                            tasks
                    );

            System.out.println(
                    "Resultados obtenidos: "
                            + results.size()
            );

            // Paginacion
            int start =
                    (page - 1) * size;

            int end =
                    Math.min(
                            start + size,
                            results.size()
                    );

            List<MatchResult> pageResults =
                    new ArrayList<>();

            if (start < results.size()) {

                pageResults =
                        results.subList(start, end);
            }

            // Respuesta final
            SearchResponse response =
                    new SearchResponse();

            response.setPage(page);

            response.setSize(size);

            response.setTotalResults(
                    results.size()
            );

            response.setResults(pageResults);

            Gson gson = new Gson();

            String json =
                    gson.toJson(response);

            byte[] responseBytes =
                    json.getBytes();

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

        } catch (Exception e) {

            e.printStackTrace();

            String error =
                    "{ \"error\": \""
                            + e.getMessage()
                            + "\" }";

            byte[] errorBytes =
                    error.getBytes();

            exchange.sendResponseHeaders(
                    500,
                    errorBytes.length
            );

            OutputStream os =
                    exchange.getResponseBody();

            os.write(errorBytes);

            os.close();

            exchange.close();
        }
    }
}