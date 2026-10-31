package fachada;

import modelo.Pregunta;
import persistencia.GestorJSON;

import java.util.ArrayList;
import java.util.List;

public class Fachada {
    private GestorJSON persistencia;
    private List<Pregunta> preguntas;

    public Fachada() {
        this.persistencia = new GestorJSON();
        // Recuperamos la información al iniciar[cite: 1]
        this.preguntas = persistencia.cargarPreguntas();
    }

    public List<Pregunta> getPreguntas() {
        return preguntas;
    }

    public void agregarPregunta(Pregunta p) throws Exception {
        // Validación extra: Verificar identificador que no se repita[cite: 1]
        for (Pregunta existente : preguntas) {
            if (existente.getId().equals(p.getId())) {
                throw new Exception("Error: El ID de la pregunta ya existe.");
            }
        }

        p.validar(); // Llamamos a las reglas
        preguntas.add(p);
        persistencia.guardarPreguntas(preguntas); // Guardamos cambios en JSON[cite: 1]
    }

    public void modificarPregunta(Pregunta actualizada) throws Exception {
        actualizada.validar(); // Verificamos las reglas antes de guardar

        for (int i = 0; i < preguntas.size(); i++) {
            if (preguntas.get(i).getId().equals(actualizada.getId())) {
                preguntas.set(i, actualizada);
                persistencia.guardarPreguntas(preguntas); // Guardamos los cambios
                return;
            }
        }
        throw new Exception("No se encontró la pregunta a modificar.");
    }

    public void eliminarPregunta(String id) throws Exception {
        boolean eliminada = preguntas.removeIf(p -> p.getId().equals(id));
        if (eliminada) {
            persistencia.guardarPreguntas(preguntas); // Actualizamos el JSON tras eliminar
        } else {
            throw new Exception("No se pudo eliminar: ID no encontrado.");
        }
    }

    // Buscar o filtrar registros por un criterio (el enunciado)
    public List<Pregunta> buscarPreguntas(String textoBusqueda) {
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            return preguntas; // Si no hay filtro, devolvemos todas
        }

        List<Pregunta> resultados = new ArrayList<>();
        String filtro = textoBusqueda.toLowerCase();

        for (Pregunta p : preguntas) {
            if (p.getEnunciado().toLowerCase().contains(filtro)) {
                resultados.add(p);
            }
        }
        return resultados;
    }
}