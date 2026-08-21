package horasextra.ui;

import java.net.URL;
import javafx.stage.Stage;
import horasextra.repositorio.RegistroRepositorioMemoria;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import java.time.LocalDate;
import horasextra.logica.ConversorTiempo;

public class VistaFormularioRegistro {
    private final RegistroRepositorioMemoria registroRepositorio;
    private final VistaCalendario calendario;

    public VistaFormularioRegistro(RegistroRepositorioMemoria registroRepositorio, VistaCalendario calendario) {
        this.registroRepositorio = registroRepositorio;
        this.calendario = calendario;
    }

    public VBox crearVista() {
        Label tituloLabel = new Label("Horas");

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("+", "-");

        ComboBox<Integer> horasComboBox = new ComboBox<>();
        for (int i = 0; i <= 24; i++) {
            horasComboBox.getItems().add(i);
        }

        ComboBox<Integer> minutosComboBox = new ComboBox<>();
        for (int i = 0; i <= 59; i++) {
            minutosComboBox.getItems().add(i);
        }

        Button guardarButton = new Button("Guardar");

        Label mensajeConfirmacion = new Label();

        guardarButton.setOnAction(e -> {
            String tipoSeleccionado = comboBox.getValue();
            Integer horas = horasComboBox.getValue();
            Integer minutos = minutosComboBox.getValue();
            LocalDate fechaSeleccionada = calendario.getFechaSeleccionada();

            if (tipoSeleccionado == null || horas == null || minutos == null || fechaSeleccionada == null) {
                mensajeConfirmacion.setText("Por favor, complete todos los campos.");
                return;
            }

            boolean tipo = "+".equals(tipoSeleccionado);
            ConversorTiempo conversor = new ConversorTiempo();
            Stage confirmacionStage = new Stage();
            VBox confirmacionLayout = new VBox(10);

            if (registroRepositorio.buscarPorSemana(fechaSeleccionada) != null) {
                Label confirmacionLabel = new Label("Ya existe un registro para esta semana. ¿Desea actualizarlo?");
                Button confirmarButton = new Button("Confirmar");
                confirmarButton.setOnAction(ev -> {
                    int totalMinutos = conversor.convertirAMinutos(tipo, horas, minutos);
                    registroRepositorio.actualizar(fechaSeleccionada, totalMinutos, LocalDate.now());
                    mensajeConfirmacion.setText("Horas extra actualizadas: " + totalMinutos + " minutos.");
                    confirmacionStage.close();
                });
                confirmacionLayout.getChildren().addAll(confirmacionLabel, confirmarButton);
            } else {
                Label confirmacionLabel = new Label("¿Desea guardar el registro de horas extra?");
                Button confirmarButton = new Button("Confirmar");
                confirmarButton.setOnAction(ev -> {
                    int totalMinutos = conversor.convertirAMinutos(tipo, horas, minutos);
                    registroRepositorio.guardar(fechaSeleccionada, totalMinutos, LocalDate.now(), LocalDate.now());
                    mensajeConfirmacion.setText("Horas extra registradas: " + totalMinutos + " minutos.");
                    confirmacionStage.close();
                });
                confirmacionLayout.getChildren().addAll(confirmacionLabel, confirmarButton);
            }

            Scene confirmacionScene = new Scene(confirmacionLayout, 300, 200);
            URL confirmacionCss = VistaFormularioRegistro.class.getResource("app.css");
            if (confirmacionCss != null) {
                confirmacionScene.getStylesheets().add(confirmacionCss.toExternalForm());
            }
            confirmacionStage.setTitle("Confirmación");
            confirmacionStage.setScene(confirmacionScene);
            confirmacionStage.show();
        });


        VBox layout = new VBox(10);
        layout.getChildren().addAll(tituloLabel, comboBox, horasComboBox, minutosComboBox, guardarButton, mensajeConfirmacion);
        return layout;
    }
}
