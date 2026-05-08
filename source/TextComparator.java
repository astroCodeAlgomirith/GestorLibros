/*
 * PROYECTO 4 Y 5
 * Nombre: Miriam G Ramirez Sanchez
 * Grupo: 7CM2
 */

package source;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class TextComparator {

    public static List<MatchResult> compareBooks(
            String pathBookA,
            String pathBookB,
            int n
    ) throws IOException {

        //Lee archivos
        String textA = Files.readString(Paths.get(pathBookA));
        String textB = Files.readString(Paths.get(pathBookB));
        String normalizedA = normalizeText(textA);
        String normalizedB = normalizeText(textB);

        // Obtener frases de n palabras
        Map<String, List<Integer>> mapA =
                buildPhraseMap(normalizedA, n);

        Map<String, List<Integer>> mapB =
                buildPhraseMap(normalizedB, n);

        // Lista final de coincidencias
        List<MatchResult> results = new ArrayList<>();

        // Buscar frases iguales
        for (String phrase : mapA.keySet()) {

            if (mapB.containsKey(phrase)) {

                List<Integer> offsetsA = mapA.get(phrase);
                List<Integer> offsetsB = mapB.get(phrase);

                //crear resultado por cada coincidencia
                for (Integer offsetA : offsetsA) {

                    for (Integer offsetB : offsetsB) {

                        MatchResult result = new MatchResult();

                        result.setBookA(
                                Paths.get(pathBookA)
                                        .getFileName()
                                        .toString()
                        );

                        result.setBookB(
                                Paths.get(pathBookB)
                                        .getFileName()
                                        .toString()
                        );

                        result.setOffsetA(offsetA);
                        result.setOffsetB(offsetB);

                        result.setPhrase(phrase);

                        results.add(result);
                    }
                }
            }
        }

        return results;
    }

    //Convierte texto
    private static String normalizeText(String text) {

        return text
                .toLowerCase()
                .replaceAll("[^a-záéíóúñü\\s]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }
    private static Map<String, List<Integer>> buildPhraseMap(
            String text,
            int n
    ) {

        Map<String, List<Integer>> phraseMap =
                new HashMap<>();

        // Palabras
        String[] words = text.split(" ");

        List<Integer> wordOffsets =
                calculateWordOffsets(text, words);

        for (int i = 0; i <= words.length - n; i++) {

            StringBuilder phraseBuilder =
                    new StringBuilder();

            for (int j = 0; j < n; j++) {

                phraseBuilder.append(words[i + j]);

                if (j < n - 1) {
                    phraseBuilder.append(" ");
                }
            }

            String phrase = phraseBuilder.toString();

            int offset = wordOffsets.get(i);

            phraseMap
                    .computeIfAbsent(
                            phrase,
                            k -> new ArrayList<>()
                    )
                    .add(offset);
        }

        return phraseMap;
    }
    private static List<Integer> calculateWordOffsets(
            String text,
            String[] words
    ) {

        List<Integer> offsets =
                new ArrayList<>();

        int currentIndex = 0;

        for (String word : words) {

            int index = text.indexOf(word, currentIndex);

            offsets.add(index);

            currentIndex = index + word.length();
        }

        return offsets;
    }
}