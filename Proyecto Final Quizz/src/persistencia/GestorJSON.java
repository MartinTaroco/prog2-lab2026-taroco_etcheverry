package persistencia;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import modelo.Pregunta;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GestorJSON {
    private static final String ARCHIVO = "datos/preguntas.json";
    private Gson gson = new Gson();

    public List<Pregunta> cargarPreguntas() {
        List<Pregunta> lista = new ArrayList<>();
        File file = new File(ARCHIVO);

        // Si el archivo no existe o hay error, iniciamos con una colección vacía
        if (!file.exists()) {
            return lista;
        }

        try (Reader reader = new FileReader(file)) {
            Type tipoLista = new TypeToken<ArrayList<Pregunta>>(){}.getType();
            lista = gson.fromJson(reader, tipoLista);
            if (lista == null) {
                lista = new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("Aviso: No se pudo leer el archivo JSON o está vacío. Iniciando lista vacía. Detalles: " + e.getMessage());
        }
        return lista;
    }

    public void guardarPreguntas(List<Pregunta> lista) {
        try (Writer writer = new FileWriter(ARCHIVO)) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            System.err.println("Error al guardar en el archivo JSON: " + e.getMessage());
        }
    }
}