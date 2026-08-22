package horasextra.ui;

import horasextra.repositorio.RegistroRepositorioMemoria;
import horasextra.logica.ConversorTiempo;
import java.net.URL;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import javafx.application.Platform;

public class VistaResumen {
    private final RegistroRepositorioMemoria registroRepositorio;
    private final ConversorTiempo conversorTiempo = new ConversorTiempo();
    private final Label horasLabel = new Label();

    public VistaResumen(RegistroRepositorioMemoria registroRepositorio) {
        this.registroRepositorio = registroRepositorio;
        actualizarHorasTotales();
    }

    public void actualizarHorasTotales() {
        horasLabel.setText(conversorTiempo.convertirAHorasString(registroRepositorio.getTotalMinutos()));
    }
    
    public VBox crearVista() {
        Label titulo = new Label("Resumen");
        titulo.getStyleClass().add("seccion-titulo");
        Label tituloTotal = new Label("Horas totales");
        tituloTotal.getStyleClass().add("titulo-total");
        horasLabel.getStyleClass().add("tarjeta-total");
        VBox tarjetaTotal = new VBox(0, tituloTotal, horasLabel);
        tarjetaTotal.getStyleClass().add("contenedor-total");
        Button mostrarResumenButton = new Button("Mostrar historial de horas extra");
        mostrarResumenButton.getStyleClass().add("boton-secundario");
        mostrarResumenButton.setOnAction(e -> {
            String[] registros = registroRepositorio.listarTodos();
            TextArea historial = new TextArea();
            historial.setEditable(false);
            historial.setWrapText(true);
            if (registros.length == 0) {
                historial.setText("No hay registros disponibles.");
            } else {
                for(String registro : registros) {
                    if (!historial.getText().isEmpty()) {
                        historial.appendText("\n");
                    }
                    historial.appendText(registro);
                }
            }
            Stage historialStage = new Stage();
            historialStage.setTitle("Historial de Horas Extra");
            VBox contenedor = new VBox(10, historial);
            contenedor.getStyleClass().add("ventana-secundaria");
            VBox.setVgrow(historial, Priority.ALWAYS);
            Scene historialScene = new Scene(contenedor, 760, 420);
            URL css = VistaResumen.class.getResource("app.css");
            if (css != null) {
                historialScene.getStylesheets().add(css.toExternalForm());
            }
            TemaUtil.aplicar(historialScene);
            historialStage.setScene(historialScene);
            historialStage.show();
        });

        HBox listarPorRangoLayout = new HBox(10);
        Button seleccionarRangoButton = new Button("Seleccionar rango de fechas");
        seleccionarRangoButton.getStyleClass().add("boton-secundario");
        Label historialRango = new Label();
        historialRango.getStyleClass().add("mensaje-confirmacion");
        seleccionarRangoButton.setOnAction(e -> {
            abrirSelectorRango(historialRango);
        });
        listarPorRangoLayout.getChildren().add(seleccionarRangoButton);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(titulo, tarjetaTotal, mostrarResumenButton, listarPorRangoLayout, historialRango);
        return layout;
    }

    private void abrirSelectorRango(Label historialRango) {
        Stage selectorStage = new Stage();
        selectorStage.setTitle("Seleccionar rango de fechas");

        Label ayuda = new Label("Seleccione inicio y fin en el mismo calendario.");
        Label seleccion = new Label("Seleccione la primera fecha.");
        DatePicker rangoPicker = new DatePicker(LocalDate.now());
        rangoPicker.getStyleClass().add("estilo-combo");
        LocalDate[] rango = new LocalDate[2];

        configurarCeldasRango(rangoPicker, rango);
        rangoPicker.setOnAction(e -> {
            LocalDate fechaSeleccionada = rangoPicker.getValue();
            if (fechaSeleccionada == null) {
                return;
            }

            if (rango[0] == null || rango[1] != null) {
                rango[0] = fechaSeleccionada;
                rango[1] = null;
                seleccion.setText("Inicio: " + rango[0] + ". Seleccione la fecha final.");
                configurarCeldasRango(rangoPicker, rango);
                Platform.runLater(rangoPicker::show);
                return;
            }

            if (fechaSeleccionada.isBefore(rango[0])) {
                rango[1] = rango[0];
                rango[0] = fechaSeleccionada;
            } else {
                rango[1] = fechaSeleccionada;
            }
            seleccion.setText("Rango: " + rango[0] + " a " + rango[1]);
            configurarCeldasRango(rangoPicker, rango);

            mostrarHistorialEnRango(rango[0], rango[1], historialRango);
            selectorStage.close();
        });

        VBox selectorLayout = new VBox(10, ayuda, seleccion, rangoPicker);
        selectorLayout.getStyleClass().add("ventana-secundaria");
        Scene selectorScene = new Scene(selectorLayout, 420, 180);
        URL cssSelector = VistaResumen.class.getResource("app.css");
        if (cssSelector != null) {
            selectorScene.getStylesheets().add(cssSelector.toExternalForm());
        }
        TemaUtil.aplicar(selectorScene);
        selectorStage.setScene(selectorScene);
        selectorStage.show();
        Platform.runLater(rangoPicker::show);
    }

    private void configurarCeldasRango(DatePicker picker, LocalDate[] rango) {
        picker.setDayCellFactory(datePicker -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                setStyle("");
                if (empty || item == null) {
                    return;
                }

                boolean tieneRegistro = registroRepositorio.buscarPorSemana(item) != null;
                boolean inicioSeleccionado = rango[0] != null && item.equals(rango[0]);
                boolean finSeleccionado = rango[1] != null && item.equals(rango[1]);
                boolean enRango = rango[0] != null && rango[1] != null
                    && !item.isBefore(rango[0]) && !item.isAfter(rango[1]);

                StringBuilder estilo = new StringBuilder();
                if (tieneRegistro) {
                    estilo.append("-fx-border-color: #22c55e; -fx-border-width: 1.5; -fx-text-fill: #166534;");
                }
                if (enRango) {
                    estilo.append("-fx-background-color: #dbeafe; -fx-text-fill: #1e3a8a;");
                }
                if (inicioSeleccionado || finSeleccionado) {
                    estilo.append("-fx-background-color: #3b82f6; -fx-text-fill: white; -fx-font-weight: bold;");
                }
                setStyle(estilo.toString());
            }
        });
    }

    private void mostrarHistorialEnRango(LocalDate fechaInicio, LocalDate fechaFin, Label historialRango) {
        String[] resultadosEnRango = registroRepositorio.listarEnRango(fechaInicio, fechaFin);
        if (resultadosEnRango.length == 0) {
            mostrarMensaje(historialRango, "No hay registros en el rango seleccionado.", true);
            return;
        }

        TextArea historial = new TextArea();
        historial.setEditable(false);
        historial.setWrapText(true);
        Label rangoSeleccionado = new Label("Rango seleccionado: " + fechaInicio + " a " + fechaFin);
        rangoSeleccionado.getStyleClass().add("mensaje-confirmacion");
        for (String registro : resultadosEnRango) {
            if (!historial.getText().isEmpty()) {
                historial.appendText("\n");
            }
            historial.appendText(registro);
        }

        Stage historialRangoStage = new Stage();
        historialRangoStage.setTitle("Historial por rango de fechas");
        VBox contenedor = new VBox(10, rangoSeleccionado, historial);
        contenedor.getStyleClass().add("ventana-secundaria");
        VBox.setVgrow(historial, Priority.ALWAYS);
        Scene historialRangoScene = new Scene(contenedor, 760, 420);
        URL cssRango = VistaResumen.class.getResource("app.css");
        if (cssRango != null) {
            historialRangoScene.getStylesheets().add(cssRango.toExternalForm());
        }
        TemaUtil.aplicar(historialRangoScene);
        historialRangoStage.setScene(historialRangoScene);
        historialRangoStage.show();
        mostrarMensaje(historialRango, "Historial cargado del " + fechaInicio + " al " + fechaFin + ".", false);
    }

    private void mostrarMensaje(Label label, String mensaje, boolean esError) {
        label.setText(mensaje);
        label.getStyleClass().removeAll("mensaje-exito", "mensaje-error");
        label.getStyleClass().add(esError ? "mensaje-error" : "mensaje-exito");
    }
}
