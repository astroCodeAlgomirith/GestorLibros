package source;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
public class CpuMonitorLanterna {

    private static final String[] WORKERS = {
            "http://localhost:8081/cpu",
            "http://localhost:8082/cpu",
            "http://localhost:8083/cpu"
    };

    public static void main(String[] args)
            throws Exception {

        DefaultTerminalFactory factory =
                new DefaultTerminalFactory();

        Screen screen =
                new TerminalScreen(
                        factory.createTerminal()
                );

        screen.startScreen();

        MultiWindowTextGUI gui =
                new MultiWindowTextGUI(screen);

        BasicWindow window =
                new BasicWindow(
                        "CPU Monitor"
                );

        Panel panel = new Panel();

        Label label = new Label("");

        panel.addComponent(label);

        window.setComponent(panel);
        new Thread(() -> {

            try {

                while (true) {

                    StringBuilder builder =
                            new StringBuilder();

                    for (String worker : WORKERS) {

                        String response =
                                get(worker);

                        builder.append(response)
                                .append("\n");
                    }

                    label.setText(
                            builder.toString()
                    );

                    Thread.sleep(1000);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();

        gui.addWindowAndWait(window);

        screen.stopScreen();
    }

    private static String get(String url)
            throws Exception {

        HttpClient client =
                HttpClient.newHttpClient();

        HttpRequest request =
                HttpRequest.newBuilder()
                        .GET()
                        .uri(URI.create(url))
                        .build();

        HttpResponse<String> response =
                client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        return response.body();
    }
}