package modelo;

public class Pregunta {
    private String id;
    private String enunciado;
    private String opcionA;
    private String opcionB;
    private String opcionC;
    private String opcionD;
    private String respuestaCorrecta; // "A", "B", "C" o "D"
    private boolean activa;

    public Pregunta(String id, String enunciado, String opcionA, String opcionB,
                    String opcionC, String opcionD, String respuestaCorrecta, boolean activa) {
        this.id = id;
        this.enunciado = enunciado;
        this.opcionA = opcionA;
        this.opcionB = opcionB;
        this.opcionC = opcionC;
        this.opcionD = opcionD;
        this.respuestaCorrecta = respuestaCorrecta.toUpperCase();
        this.activa = activa;
    }

    // Getters
    public String getId() { return id; }
    public String getEnunciado() { return enunciado; }
    public String getOpcionA() { return opcionA; }
    public String getOpcionB() { return opcionB; }
    public String getOpcionC() { return opcionC; }
    public String getOpcionD() { return opcionD; }
    public String getRespuestaCorrecta() { return respuestaCorrecta; }
    public boolean isActiva() { return activa; }

    // Setters
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }
    public void setOpcionA(String opcionA) { this.opcionA = opcionA; }
    public void setOpcionB(String opcionB) { this.opcionB = opcionB; }
    public void setOpcionC(String opcionC) { this.opcionC = opcionC; }
    public void setOpcionD(String opcionD) { this.opcionD = opcionD; }
    public void setRespuestaCorrecta(String respuestaCorrecta) { this.respuestaCorrecta = respuestaCorrecta; }
    public void setActiva(boolean activa) { this.activa = activa; }

    /**
     * MÉTODOS DE VALIDACIÓN PROPIOS DEL NEGOCIO
     * Cumple con la consigna de incluir 3 validaciones pertinentes.
     */
    public void validar() throws Exception {
        // Validación 1
        if (enunciado == null || enunciado.trim().length() < 10) {
            throw new Exception("Error: El enunciado debe tener al menos 10 caracteres.");
        }

        // Validación 2
        if (opcionA.trim().isEmpty() || opcionB.trim().isEmpty() ||
                opcionC.trim().isEmpty() || opcionD.trim().isEmpty()) {
            throw new Exception("Error: Ninguna de las opciones puede quedar en blanco.");
        }

        // Validación 3
        if (opcionA.equalsIgnoreCase(opcionB) || opcionA.equalsIgnoreCase(opcionC) ||
                opcionA.equalsIgnoreCase(opcionD) || opcionB.equalsIgnoreCase(opcionC) ||
                opcionB.equalsIgnoreCase(opcionD) || opcionC.equalsIgnoreCase(opcionD)) {
            throw new Exception("Error: Las opciones de respuesta no pueden ser repetidas.");
        }
    }
}