/*
 * PROYECTO 4 Y 5
 * Nombre: Miriam G Ramirez Sanchez
 * Grupo: 7CM2
 */

package source;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Aggregator {

    private final WebClient webClient;

    public Aggregator() {
        this.webClient = new WebClient();
    }

    public List<MatchResult> sendTasksToWorkers(
            List<String> workers,
            List<SearchRequest> tasks
    ) throws Exception {

        List<CompletableFuture<String>> futures =
                new ArrayList<>();

        Gson gson = new Gson();

        int workerIndex = 0;
        for (SearchRequest task : tasks) {

            String workerUrl =
                    workers.get(workerIndex);

            String json = gson.toJson(task);

            CompletableFuture<String> future =
                    webClient.sendTask(
                            workerUrl,
                            json.getBytes()
                    );

            futures.add(future);

            workerIndex =
                    (workerIndex + 1) % workers.size();
        }
        CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        ).join();

        // Lista final
        List<MatchResult> allResults =
                new ArrayList<>();

        Type listType =
                new TypeToken<List<MatchResult>>() {
                }.getType();

        // Convertir respuestas JSON -> objetos
        for (CompletableFuture<String> future : futures) {

            String response = future.get();

            List<MatchResult> partialResults =
                    gson.fromJson(response, listType);

            if (partialResults != null) {
                allResults.addAll(partialResults);
            }
        }

        return allResults;
    }
}