# QuizzApp - Plataforma Educativa y Sistema de Repaso Institucional

**Proyecto Final - Laboratorio de Java 2026**
**Integrantes:** Martín Taroco y Verónica Etcheverry[cite: 3]

---

## 1. Problema y Destinatarios
Como docentes en ejercicio, identificamos una carencia importante en las instituciones educativas respecto a herramientas digitales ágiles que funcionen de manera puramente local. Frecuentemente, las plataformas de evaluación requieren conexión permanente a internet o suscripciones pagas, lo cual es una barrera en muchos centros educativos. 
El problema elegido para este laboratorio es la **gestión centralizada de bancos de preguntas y la generación automatizada de autoevaluaciones**. 

La aplicación está diseñada para dos destinatarios principales:
* **Docentes:** Que necesitan un sistema de escritorio rápido, intuitivo y sin dependencias externas para cargar, editar y organizar el material de repaso de sus asignaturas.
* **Estudiantes:** Que requieren una interfaz limpia y sin distracciones para ponerse a prueba a sí mismos mediante cuestionarios generados aleatoriamente a partir del banco de preguntas del profesor.

## 2. Utilidad de la Solución
QuizzApp aporta un valor significativo al entorno educativo al descentralizar la evaluación. Al no depender de bases de datos complejas ni servidores web, cualquier docente puede llevar la aplicación y su archivo de datos en un pendrive y ejecutarla en las computadoras del laboratorio de informática de su liceo o escuela técnica. 
Para el estudiante, la utilidad radica en la retroalimentación inmediata, ya que el sistema automatiza la corrección de los exámenes aleatorios, permitiéndole practicar de forma autónoma y repetitiva los contenidos vistos en clase.

## 3. Adaptación del Modelo y Reglas
Partiendo de un prototipo genérico de registros, adaptamos el modelo de dominio para representar una plataforma de opción múltiple (Multiple Choice). La entidad central es la clase `Pregunta`, la cual encapsula los siguientes atributos: un `id` único autogenerado, un `enunciado` descriptivo, cuatro opciones (`opcionA`, `opcionB`, `opcionC`, `opcionD`), la `respuestaCorrecta` y un estado booleano `activa`.

Se implementaron tres validaciones de negocio estrictas dentro del modelo para garantizar la integridad pedagógica de los datos[cite: 2]:
1. **Regla de Formato:** El enunciado es obligatorio y se exige que tenga un mínimo de 10 caracteres, evitando registros accidentales vacíos o preguntas incompletas.
2. **Regla de Integridad:** Ninguna de las cuatro opciones de respuesta puede quedar en blanco.
3. **Regla propia del problema (Dominio):** No se permite registrar dos opciones con exactamente el mismo texto. Esta regla evita la ambigüedad y la confusión del estudiante al momento de rendir el examen.

## 4. Componentes Gráficos Nuevos y su Función
Cumpliendo con la restricción de incluir al menos ocho tipos de componentes funcionales, conservamos los obligatorios (`JTextField`, `JComboBox`, `JCheckBox`, `JButton`, `JTable`) y agregamos tres componentes nuevos con justificación específica:

1. **`JTextArea` (Área de Texto):** Reemplazó al `JTextField` original para el campo "Enunciado". Su función es permitir el ingreso de textos largos con saltos de línea (multilínea), lo cual es indispensable en el ámbito educativo para plantear problemas matemáticos largos, fragmentos de código o textos de comprensión lectora.
2. **`JSpinner` (Selector Numérico):** Implementado en el panel de estudiante. Su función es restringir matemáticamente (con un límite de 1 a 50) la cantidad de preguntas que el alumno desea solicitar para su examen, evitando el ingreso de texto erróneo o valores negativos.
3. **`JRadioButton` (Botón de Opción):** Utilizado en el panel de examen en conjunto con un `ButtonGroup`. Su función lógica es garantizar la exclusión mutua; el estudiante solo puede marcar una única respuesta (A, B, C o D) a la vez para cada pregunta, replicando el formato real de un examen múltiple opción.

## 5. Organización de Paquetes y Recorrido de una Operación
El código fue refactorizado para respetar estrictamente el patrón arquitectónico MVC y la separación de responsabilidades solicitada.
* **`app`:** Contiene únicamente la clase `Main` que inicia e interconecta los objetos.
* **`modelo`:** Aloja la clase `Pregunta` y la lógica de validaciones de negocio.
* **`vista`:** Contiene `VentanaQuiz`, encargada puramente de la interfaz Swing sin tomar decisiones de datos.
* **`controlador`:** Contiene la lógica de eventos y oyentes de los botones.
* **`fachada`:** Coordina la comunicación entre el controlador, el modelo y la persistencia.
* **`persistencia`:** Aloja `GestorJSON`, dedicada exclusivamente a leer y escribir el archivo de datos.

**Recorrido de la operación "Registrar Nueva Pregunta":**
1. El usuario completa los campos en la vista (`VentanaQuiz`) y presiona "Registrar".
2. El `Controlador` escucha el evento, recupera los textos de la vista, genera un ID basado en el tiempo del sistema y ensambla un objeto `Pregunta`.
3. El Controlador envía la orden `fachada.agregarPregunta(p)` a la `Fachada`.
4. La Fachada delega la validación llamando a `p.validar()` (en el `modelo`). Si las reglas de negocio se cumplen y el ID no está duplicado, agrega el objeto a la colección en memoria.
5. Finalmente, la Fachada llama a `persistencia.guardarPreguntas()`. El `GestorJSON` serializa la colección y sobrescribe el archivo `.json`, retornando el control a la Vista para actualizar la tabla.

## 6. Instrucciones de Ejecución, Versión de Java y Dependencias
* **Versión de Java:** Desarrollado utilizando Java 17.
* **Dependencias:** El proyecto utiliza la biblioteca externa **Gson** (provista por Google, versión `2.10.1`) para resolver la lectura y escritura del archivo JSON de manera eficiente.
* **Instrucciones para compilar y ejecutar:**
  1. Clonar o descargar el repositorio completo.
  2. Abrir la carpeta raíz (`prog2-lab2026-taroco_etcheverry`) con un IDE como IntelliJ IDEA.
  3. Asegurarse de que la carpeta `src` está marcada como "Sources Root".
  4. Agregar la dependencia de Gson (`com.google.code.gson:gson:2.10.1`) a través de Maven o importando el `.jar` en la estructura del proyecto.
  5. Ejecutar la aplicación desde la clase `Main.java` ubicada en el paquete `app`.

## 7. Origen del Código y Herramientas Externas
El prototipo entregado por la cátedra sirvió como inspiración para entender la dinámica de una fachada y la necesidad de persistencia en archivos. Sin embargo, el código fue completamente reescrito para adaptarse a la estructura de paquetes obligatoria. Se reemplazó la lógica manual de persistencia por la librería estandarizada Gson para manejar el formato JSON con mayor robustez. Para el rediseño de la interfaz gráfica y la depuración de errores específicos de Swing, nos apoyamos en herramientas de IA (Gemini) y en la documentación oficial de Oracle Java.

## 8. Pruebas Realizadas (Comprobación de Funcionamiento)

| Acción Realizada | Resultado Esperado | Resultado Obtenido |
| :--- | :--- | :--- |
| Crear un registro válido | La pregunta es validada, guardada en JSON y visible en la lista. | **Éxito**. Aparece de inmediato en la tabla y en `preguntas.json`. |
| Guardar datos incumpliendo las 3 validaciones | El sistema lanza excepciones controladas, avisa al usuario sin cerrar la app y no guarda. | **Éxito**. Los `JOptionPane` avisan de caracteres insuficientes, opciones vacías o repetidas. |
| Modificar un registro seleccionado de la tabla | Se aplican los cambios al enunciado u opciones sin alterar el ID original ni duplicar. | **Éxito**. La fila seleccionada se actualiza correctamente. |
| Cancelar eliminación y confirmar con otra prueba | Cancelar aborta la acción. Confirmar elimina el registro definitivamente de la tabla y archivo. | **Éxito**. El diálogo de Sí/No (`JOptionPane`) controla el flujo correctamente.
| Buscar o filtrar registros por criterio | La tabla muestra únicamente las preguntas cuyo enunciado coincide con el texto ingresado. | **Éxito**. La `JTable` se actualiza dinámicamente al presionar buscar. |
| Crear, modificar, cerrar y volver a abrir la app | La información editada debe estar intacta al reiniciar, cargada desde el disco. | **Éxito**. La serialización y deserialización JSON funciona de forma persistente. |
| Iniciar sin archivo JSON existente / archivo inválido | La aplicación inicia con un listado vacío, captura el error de lectura sin crashear. | **Éxito**. Manejo de excepciones adecuado al faltar la carpeta `datos`. |

## 9. Limitaciones Conocidas
* La búsqueda de texto en los enunciados distingue entre mayúsculas y minúsculas cuando se trata de caracteres especiales acentuados (sensibilidad de caracteres latinos)[.
* La aplicación no cifra (encripta) el archivo JSON local. En un entorno real de laboratorio escolar, los archivos deben protegerse con permisos de sistema operativo para que un estudiante avanzado no pueda abrir el `.json` y leer las respuestas correctas.

## 10. Aportes de Cada Integrante
Ambos integrantes participamos activamente en el diseño de la solución, la depuración del código y la elaboración de este documento, asegurando una comprensión global de la aplicación.
* **Martín Taroco:** Lideró el diseño de la entidad `Pregunta` y sus validaciones de negocio en el modelo. Estructuró la interfaz gráfica `VentanaQuiz` incorporando los componentes nuevos, y desarrolló el algoritmo central del "Modo Estudiante" (mezcla aleatoria y evaluación)[cite: 3, 4].
* **Verónica Etcheverry:** Encargada del desarrollo del paquete `controlador` para la gestión de eventos de la interfaz (CRUD). Implementó la arquitectura de la `Fachada` y el `GestorJSON` integrando la librería Gson. Llevó a cabo la batería de comprobaciones y testing[cite: 3, 4].

## 11. Video Explicativo
En el siguiente enlace se encuentra la defensa oral del proyecto, donde demostramos el funcionamiento de la aplicación, identificamos los componentes y explicamos el recorrido arquitectónico:

👉 **[Enlace al video en YouTube / Google Drive] (PEGAR AQUÍ EL LINK PÚBLICO DEL VIDEO ANTES DE ENTREGAR)**
