package app;

import controlador.Controlador;
import fachada.Fachada;
import vista.VentanaQuiz;

public class Main {
    public static void main(String[] args) {
        // 1. Instanciamos la fachada (que manejará los datos y la persistencia)
        Fachada fachada = new Fachada();

        // 2. Instanciamos la vista (la ventana gráfica)
        VentanaQuiz vista = new VentanaQuiz();

        // 3. Instanciamos el controlador y le pasamos la vista y la fachada para conectarlos
        Controlador controlador = new Controlador(vista, fachada);

        // 4. Iniciamos la ventana
        vista.setVisible(true);
    }
}