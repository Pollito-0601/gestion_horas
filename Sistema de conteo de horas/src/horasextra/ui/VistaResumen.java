package horasextra.ui;

import horasextra.repositorio.RegistroRepositorioMemoria;
import horasextra.logica.ConversorTiempo;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.net.URL;

public class VistaResumen {
    private final RegistroRepositorioMemoria registroRepositorio;
    private final ConversorTiempo conversorTiempo = new ConversorTiempo();

    public VistaResumen(RegistroRepositorioMemoria registroRepositorio) {
        this.registroRepositorio = registroRepositorio;
    }
    
    public VBox crearVista() {
        Label horas = new Label("Horas totales: " + conversorTiempo.convertirAHorasString(registroRepositorio.getTotalMinutos()));
        Button actualizarResumenButton = new Button("Actualizar total");
        actualizarResumenButton.setOnAction(e -> {
            horas.setText("Horas totales: " + conversorTiempo.convertirAHorasString(registroRepositorio.getTotalMinutos()));
        });

        Button mostrarResumenButton = new Button("Mostrar historial de horas extra");
        mostrarResumenButton.setOnAction(e -> {
            Label historial = new Label();
            for(String registro : registroRepositorio.listarTodos()) {
                historial.setText(historial.getText() + "\n" + registro);
            }
            Stage historialStage = new Stage();
            historialStage.setTitle("Historial de Horas Extra");
            Scene historialScene = new Scene(new VBox(10, historial), 500, 350);
            URL css = VistaResumen.class.getResource("app.css");
            if (css != null) {
                historialScene.getStylesheets().add(css.toExternalForm());
            }
            historialStage.setScene(historialScene);
            historialStage.show();
        });

        HBox listarPorRangoLayout = new HBox(10);
        DatePicker fechaInicioPicker = new DatePicker();
        DatePicker fechaFinPicker = new DatePicker();
        Button seleccionarRangoButton = new Button("Seleccionar rango de fechas");
        Label historialRango = new Label();
        seleccionarRangoButton.setOnAction(e -> {
            LocalDate fechaInicio = fechaInicioPicker.getValue();
            LocalDate fechaFin = fechaFinPicker.getValue();
            if (fechaInicio == null || fechaFin == null) {
                historialRango.setText("Seleccione ambas fechas para listar el rango.");
                return;
            }
            if (fechaFin.isBefore(fechaInicio)) {
                historialRango.setText("La fecha final no puede ser anterior a la fecha inicial.");
                return;
            }

            historialRango.setText("Historial de horas extra del " + fechaInicio + " al " + fechaFin);
            registroRepositorio.listarEnRango(fechaInicio, fechaFin);
        });
        listarPorRangoLayout.getChildren().addAll(fechaInicioPicker, fechaFinPicker, seleccionarRangoButton);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(horas, actualizarResumenButton, mostrarResumenButton, listarPorRangoLayout, historialRango);
        return layout;
    }
}
