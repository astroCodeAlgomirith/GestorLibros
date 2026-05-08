package source;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
public class FileManagerLanterna {

    public static void main(String[] args)
            throws Exception {

        DefaultTerminalFactory factory =
                new DefaultTerminalFactory();

        Screen screen =
                new TerminalScreen(
                        factory.createTerminal()
                );

        screen.startScreen();

        WindowBasedTextGUI gui =
                new MultiWindowTextGUI(screen);

        BasicWindow window =
                new BasicWindow(
                        "Administrador de Archivos"
                );

        Panel panel = new Panel();

        panel.setLayoutManager(
                new GridLayout(1)
        );

        Label filesLabel =
                new Label(listFiles());

        panel.addComponent(filesLabel);

        panel.addComponent(
                new Button(
                        "Actualizar",
                        () -> {

                            filesLabel.setText(
                                    listFiles()
                            );
                        }
                )
        );
        panel.addComponent(
                new Button(
                        "Eliminar Todos",
                        () -> {

                            deleteAllFiles();

                            filesLabel.setText(
                                    listFiles()
                            );
                        }
                )
        );

        panel.addComponent(
                new Button(
                        "Salir",
                        window::close
                )
        );

        window.setComponent(panel);

        gui.addWindowAndWait(window);

        screen.stopScreen();
    }

    private static String listFiles() {

        StringBuilder builder =
                new StringBuilder();

        File folder =
                new File("resources/LIBROS_TXT");

        File[] files = folder.listFiles();

        if (files == null) {
            return "No hay archivos";
        }

        for (File file : files) {
            builder.append(file.getName())
                    .append("\n");
        }

        return builder.toString();
    }

    private static void deleteAllFiles() {

        File folder =
                new File("resources/LIBROS_TXT");

        File[] files = folder.listFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {
            file.delete();
        }
    }
}