package controlador;

import fachada.Fachada;
import modelo.Pregunta;
import vista.VentanaQuiz;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Controlador {
    private VentanaQuiz vista;
    private Fachada fachada;

    // Variables para el Modo Estudiante
    private List<Pregunta> examenActual;
    private int indicePreguntaActual = 0;
    private int puntaje = 0;

    public Controlador(VentanaQuiz vista, Fachada fachada) {
        this.vista = vista;
        this.fachada = fachada;

        actualizarTabla(fachada.getPreguntas());
        iniciarEventos();
    }

    private void iniciarEventos() {
        // --- EVENTOS DE GESTIÓN ---
        vista.btnGuardar.addActionListener(e -> {
            try {
                String id = String.valueOf(System.currentTimeMillis() % 10000);
                Pregunta p = crearPreguntaDesdeFormulario(id);
                fachada.agregarPregunta(p);
                actualizarTabla(fachada.getPreguntas());
                limpiarCampos();
                JOptionPane.showMessageDialog(vista, "Pregunta registrada correctamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, ex.getMessage(), "Error de Validación", JOptionPane.ERROR_MESSAGE);
            }
        });

        vista.btnEliminar.addActionListener(e -> {
            int filaSeleccionada = vista.tabla.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(vista, "Seleccione una pregunta de la tabla para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String id = (String) vista.modelTabla.getValueAt(filaSeleccionada, 0);
            int confirmacion = JOptionPane.showConfirmDialog(vista, "¿Está seguro de eliminar la pregunta con ID " + id + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                try {
                    fachada.eliminarPregunta(id);
                    actualizarTabla(fachada.getPreguntas());
                    limpiarCampos();
                    JOptionPane.showMessageDialog(vista, "Pregunta eliminada.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(vista, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        vista.tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = vista.tabla.getSelectedRow();
                if (fila != -1) {
                    vista.txtEnunciado.setText((String) vista.modelTabla.getValueAt(fila, 1));
                    vista.txtOpA.setText((String) vista.modelTabla.getValueAt(fila, 2));
                    vista.txtOpB.setText((String) vista.modelTabla.getValueAt(fila, 3));
                    vista.txtOpC.setText((String) vista.modelTabla.getValueAt(fila, 4));
                    vista.txtOpD.setText((String) vista.modelTabla.getValueAt(fila, 5));
                    vista.comboCorrecta.setSelectedItem((String) vista.modelTabla.getValueAt(fila, 6));
                    vista.chkActiva.setSelected((Boolean) vista.modelTabla.getValueAt(fila, 7));
                }
            }
        });

        vista.btnModificar.addActionListener(e -> {
            int filaSeleccionada = vista.tabla.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(vista, "Seleccione una pregunta para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                String id = (String) vista.modelTabla.getValueAt(filaSeleccionada, 0);
                Pregunta p = crearPreguntaDesdeFormulario(id);
                fachada.modificarPregunta(p);
                actualizarTabla(fachada.getPreguntas());
                limpiarCampos();
                JOptionPane.showMessageDialog(vista, "Pregunta modificada correctamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, ex.getMessage(), "Error de Validación", JOptionPane.ERROR_MESSAGE);
            }
        });

        vista.btnBuscar.addActionListener(e -> {
            String texto = vista.txtBuscar.getText();
            List<Pregunta> resultados = fachada.buscarPreguntas(texto);
            actualizarTabla(resultados);
        });

        vista.btnLimpiar.addActionListener(e -> limpiarCampos());

        // --- EVENTOS DEL MODO ESTUDIANTE ---
        vista.btnIniciarExamen.addActionListener(e -> comenzarExamen());
        vista.btnSiguiente.addActionListener(e -> evaluarYSiguiente());
    }

    // --- MÉTODOS AUXILIARES DE GESTIÓN ---
    private Pregunta crearPreguntaDesdeFormulario(String id) {
        return new Pregunta(
                id,
                vista.txtEnunciado.getText(),
                vista.txtOpA.getText(),
                vista.txtOpB.getText(),
                vista.txtOpC.getText(),
                vista.txtOpD.getText(),
                (String) vista.comboCorrecta.getSelectedItem(),
                vista.chkActiva.isSelected()
        );
    }

    private void actualizarTabla(List<Pregunta> lista) {
        vista.modelTabla.setRowCount(0);
        for (Pregunta p : lista) {
            vista.modelTabla.addRow(new Object[]{
                    p.getId(), p.getEnunciado(), p.getOpcionA(), p.getOpcionB(),
                    p.getOpcionC(), p.getOpcionD(), p.getRespuestaCorrecta(), p.isActiva()
            });
        }
    }

    private void limpiarCampos() {
        vista.txtEnunciado.setText("");
        vista.txtOpA.setText("");
        vista.txtOpB.setText("");
        vista.txtOpC.setText("");
        vista.txtOpD.setText("");
        vista.txtBuscar.setText("");
        vista.comboCorrecta.setSelectedIndex(0);
        vista.chkActiva.setSelected(true);
        vista.tabla.clearSelection();
    }

    // --- MÉTODOS DEL MODO ESTUDIANTE ---
    private void comenzarExamen() {
        List<Pregunta> todas = fachada.getPreguntas();
        List<Pregunta> activas = new ArrayList<>();

        for (Pregunta p : todas) {
            if (p.isActiva()) activas.add(p);
        }

        if (activas.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "No hay preguntas activas en la base de datos.");
            return;
        }

        int cantidadPedida = (int) vista.spinnerCantidad.getValue();
        Collections.shuffle(activas); // Mezcla aleatoria

        if (cantidadPedida > activas.size()) {
            cantidadPedida = activas.size();
        }
        examenActual = activas.subList(0, cantidadPedida);

        indicePreguntaActual = 0;
        puntaje = 0;
        vista.lblPuntajeFinal.setText("");
        vista.btnSiguiente.setEnabled(true);

        mostrarPreguntaActual();
    }

    private void mostrarPreguntaActual() {
        if (indicePreguntaActual < examenActual.size()) {
            Pregunta p = examenActual.get(indicePreguntaActual);
            vista.lblPregunta.setText((indicePreguntaActual + 1) + ". " + p.getEnunciado());
            vista.rbtnA.setText("A) " + p.getOpcionA());
            vista.rbtnB.setText("B) " + p.getOpcionB());
            vista.rbtnC.setText("C) " + p.getOpcionC());
            vista.rbtnD.setText("D) " + p.getOpcionD());
            vista.bgOpciones.clearSelection();
        } else {
            vista.lblPregunta.setText("¡Examen finalizado!");
            vista.rbtnA.setText("-"); vista.rbtnB.setText("-"); vista.rbtnC.setText("-"); vista.rbtnD.setText("-");
            vista.btnSiguiente.setEnabled(false);
            vista.lblPuntajeFinal.setText("Resultado final: " + puntaje + " de " + examenActual.size() + " correctas.");
        }
    }

    private void evaluarYSiguiente() {
        String seleccion = null;
        if (vista.rbtnA.isSelected()) seleccion = "A";
        else if (vista.rbtnB.isSelected()) seleccion = "B";
        else if (vista.rbtnC.isSelected()) seleccion = "C";
        else if (vista.rbtnD.isSelected()) seleccion = "D";

        if (seleccion == null) {
            JOptionPane.showMessageDialog(vista, "Por favor selecciona una opción.");
            return;
        }

        Pregunta p = examenActual.get(indicePreguntaActual);
        if (seleccion.equalsIgnoreCase(p.getRespuestaCorrecta())) {
            puntaje++;
        }

        indicePreguntaActual++;
        mostrarPreguntaActual();
    }
}