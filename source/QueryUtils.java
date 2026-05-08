/*
 * PROYECTO 4 Y 5
 * Nombre: Miriam G Ramirez Sanchez
 * Grupo: 7CM2
 */

package source;

import java.util.HashMap;
import java.util.Map;

public class QueryUtils {

    public static Map<String, String> queryToMap(
            String query
    ) {

        Map<String, String> result =
                new HashMap<>();

        // Si no hay parametros
        if (query == null ||
                query.isEmpty()) {

            return result;
        }

        // Separar por &
        String[] params =
                query.split("&");

        for (String param : params) {

            // Separar clave=valor
            String[] pair =
                    param.split("=");

            // Verificar formato correcto
            if (pair.length == 2) {

                result.put(
                        pair[0],
                        pair[1]
                );
            }
        }

        return result;
    }
}