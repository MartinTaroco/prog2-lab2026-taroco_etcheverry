package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class VentanaQuiz extends JFrame {
    // Tabla y Modelo
    public DefaultTableModel modelTabla;
    public JTable tabla;

    // Componentes del Formulario
    public JTextArea txtEnunciado;
    public JTextField txtOpA, txtOpB, txtOpC, txtOpD, txtBuscar; // Obligatorio 1[cite: 2]
    public JComboBox<String> comboCorrecta;
    public JCheckBox chkActiva;
    public JButton btnGuardar, btnModificar, btnEliminar, btnBuscar, btnLimpiar; // Obligatorio 4[cite: 2]


    public JSpinner spinnerCantidad;
    public JRadioButton rbtnA, rbtnB, rbtnC, rbtnD;
    public ButtonGroup bgOpciones;
    public JButton btnIniciarExamen, btnSiguiente;
    public JLabel lblPregunta, lblPuntajeFinal;

    public VentanaQuiz() {
        setTitle("Sistema de Gestión de Preguntas - MVC");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane pestañas = new JTabbedPane();
        pestañas.addTab("Gestión de Preguntas", crearPanelCreador());
        pestañas.addTab("Modo Estudiante", crearPanelEstudiante());

        add(pestañas);
    }

    private JPanel crearPanelCreador() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // --- Panel de Formulario ---
        JPanel form = new JPanel(new GridLayout(8, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Datos de la Pregunta"));

        txtEnunciado = new JTextArea(2, 20); // Ahora es un área de texto
        txtEnunciado.setLineWrap(true);
        txtOpA = new JTextField();
        txtOpB = new JTextField();
        txtOpC = new JTextField();
        txtOpD = new JTextField();
        comboCorrecta = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        chkActiva = new JCheckBox("Pregunta Activa para Examen", true);

        form.add(new JLabel("Enunciado (min 10 caract.):")); form.add(new JScrollPane(txtEnunciado));
        form.add(new JLabel("Opción A:")); form.add(txtOpA);
        form.add(new JLabel("Opción B:")); form.add(txtOpB);
        form.add(new JLabel("Opción C:")); form.add(txtOpC);
        form.add(new JLabel("Opción D:")); form.add(txtOpD);
        form.add(new JLabel("Opción Correcta:")); form.add(comboCorrecta);
        form.add(new JLabel("Estado:")); form.add(chkActiva);

        // --- Panel de Botones de Gestión ---
        JPanel panelBotones = new JPanel(new FlowLayout());
        btnGuardar = new JButton("Registrar Nueva");
        btnModificar = new JButton("Modificar Seleccionada");
        btnEliminar = new JButton("Eliminar Seleccionada");
        btnLimpiar = new JButton("Limpiar Campos");

        panelBotones.add(btnGuardar);
        panelBotones.add(btnModificar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        JPanel panelNorte = new JPanel(new BorderLayout());
        panelNorte.add(form, BorderLayout.CENTER);
        panelNorte.add(panelBotones, BorderLayout.SOUTH);
        panel.add(panelNorte, BorderLayout.NORTH);

        // --- Panel de Búsqueda y Tabla ---
        JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBuscar = new JTextField(20);
        btnBuscar = new JButton("Buscar por Enunciado");
        panelBusqueda.add(new JLabel("Buscar:"));
        panelBusqueda.add(txtBuscar);
        panelBusqueda.add(btnBuscar);

        // Tabla
        String[] columnas = {"ID", "Enunciado", "A", "B", "C", "D", "Correcta", "Activa"};
        modelTabla = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelTabla);

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.add(panelBusqueda, BorderLayout.NORTH);
        panelCentro.add(new JScrollPane(tabla), BorderLayout.CENTER);

        panel.add(panelCentro, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelEstudiante() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Cantidad de preguntas:"));
        spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        topPanel.add(spinnerCantidad);
        btnIniciarExamen = new JButton("Iniciar Examen");
        topPanel.add(btnIniciarExamen);
        panel.add(topPanel, BorderLayout.NORTH);

        JPanel panelCuestionario = new JPanel(new GridLayout(7, 1, 5, 5));
        lblPregunta = new JLabel("Inicia el examen para comenzar.");
        rbtnA = new JRadioButton("Opción A");
        rbtnB = new JRadioButton("Opción B");
        rbtnC = new JRadioButton("Opción C");
        rbtnD = new JRadioButton("Opción D");
        bgOpciones = new ButtonGroup();
        bgOpciones.add(rbtnA); bgOpciones.add(rbtnB); bgOpciones.add(rbtnC); bgOpciones.add(rbtnD);
        btnSiguiente = new JButton("Siguiente Pregunta");
        btnSiguiente.setEnabled(false);

        panelCuestionario.add(lblPregunta);
        panelCuestionario.add(rbtnA);
        panelCuestionario.add(rbtnB);
        panelCuestionario.add(rbtnC);
        panelCuestionario.add(rbtnD);
        panelCuestionario.add(btnSiguiente);
        panel.add(panelCuestionario, BorderLayout.CENTER);

        lblPuntajeFinal = new JLabel("", SwingConstants.CENTER);
        panel.add(lblPuntajeFinal, BorderLayout.SOUTH);
        return panel;
    }
}