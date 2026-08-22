package horasextra.ui;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import javafx.stage.Stage;
import horasextra.repositorio.RegistroRepositorio;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import java.time.LocalDate;
import horasextra.logica.ConversorTiempo;

public class VistaFormularioRegistro {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final RegistroRepositorio registroRepositorio;
    private final VistaCalendario calendario;
    private final Runnable onRegistroGuardado;

    public VistaFormularioRegistro(RegistroRepositorio registroRepositorio, VistaCalendario calendario, Runnable onRegistroGuardado) {
        this.registroRepositorio = registroRepositorio;
        this.calendario = calendario;
        this.onRegistroGuardado = onRegistroGuardado;
    }

    public VBox crearVista() {
        Label tituloLabel = new Label("Horas");
        tituloLabel.getStyleClass().add("seccion-titulo");

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("+", "-");
        comboBox.setPromptText("Tipo (+ o -)");
        comboBox.getStyleClass().add("estilo-combo");

        ComboBox<Integer> horasComboBox = new ComboBox<>();
        configurarComboNumerico(horasComboBox, 99, "Horas (0-99)");

        ComboBox<Integer> minutosComboBox = new ComboBox<>();
        configurarComboNumerico(minutosComboBox, 59, "Minutos (0-59)");

        Button guardarButton = new Button("Guardar");
        guardarButton.getStyleClass().add("boton-primario");

        Label mensajeConfirmacion = new Label();
        mensajeConfirmacion.getStyleClass().add("mensaje-confirmacion");

        guardarButton.setOnAction(e -> {
            String tipoSeleccionado = comboBox.getValue();
            Integer horas = normalizarValorCombo(horasComboBox, 99);
            Integer minutos = normalizarValorCombo(minutosComboBox, 59);
            LocalDate fechaSeleccionada = calendario.getFechaSeleccionada();

            if (tipoSeleccionado == null || horas == null || minutos == null || fechaSeleccionada == null) {
                mostrarMensaje(mensajeConfirmacion, "Por favor, complete todos los campos.", true);
                return;
            }

            boolean tipo = "+".equals(tipoSeleccionado);
            ConversorTiempo conversor = new ConversorTiempo();
            Stage confirmacionStage = new Stage();
            VBox confirmacionLayout = new VBox(10);

            if (registroRepositorio.buscarPorSemana(fechaSeleccionada) != null) {
                Label confirmacionLabel = new Label(construirMensajeConfirmacion(fechaSeleccionada, tipo, horas, minutos, true));
                Button confirmarButton = new Button("Confirmar");
                confirmarButton.setOnAction(ev -> {
                    int totalMinutos = conversor.convertirAMinutos(tipo, horas, minutos);
                    registroRepositorio.actualizar(fechaSeleccionada, totalMinutos, LocalDate.now());
                    mostrarMensaje(mensajeConfirmacion, "Horas extra actualizadas: " + totalMinutos + " minutos.", false);
                    onRegistroGuardado.run();
                    calendario.refrescarCalendario();
                    confirmacionStage.close();
                    limpiarFormulario(comboBox, horasComboBox, minutosComboBox);
                });
                confirmacionLayout.getChildren().addAll(confirmacionLabel, confirmarButton);
            } else {
                Label confirmacionLabel = new Label(construirMensajeConfirmacion(fechaSeleccionada, tipo, horas, minutos, false));
                Button confirmarButton = new Button("Confirmar");
                confirmarButton.setOnAction(ev -> {
                    int totalMinutos = conversor.convertirAMinutos(tipo, horas, minutos);
                    registroRepositorio.guardar(fechaSeleccionada, totalMinutos, LocalDate.now(), LocalDate.now());
                    mostrarMensaje(mensajeConfirmacion, "Horas extra registradas: " + totalMinutos + " minutos.", false);
                    onRegistroGuardado.run();
                    calendario.refrescarCalendario();
                    confirmacionStage.close();
                    limpiarFormulario(comboBox, horasComboBox, minutosComboBox);
                });
                confirmacionLayout.getChildren().addAll(confirmacionLabel, confirmarButton);
            }

            Scene confirmacionScene = new Scene(confirmacionLayout, 430, 220);
            URL confirmacionCss = VistaFormularioRegistro.class.getResource("app.css");
            if (confirmacionCss != null) {
                confirmacionScene.getStylesheets().add(confirmacionCss.toExternalForm());
            }
            confirmacionLayout.getStyleClass().add("ventana-secundaria");
            TemaUtil.aplicar(confirmacionScene);
            confirmacionStage.setTitle("Confirmación");
            confirmacionStage.setScene(confirmacionScene);
            TemaUtil.aplicarIcono(confirmacionStage);
            confirmacionStage.show();
        });


        HBox campos = new HBox(8, comboBox, horasComboBox, minutosComboBox);
        campos.getStyleClass().add("fila-campos");
        VBox layout = new VBox(10);
        layout.getChildren().addAll(tituloLabel, campos, guardarButton, mensajeConfirmacion);
        return layout;
    }

    private void configurarComboNumerico(ComboBox<Integer> combo, int maximo, String prompt) {
        for (int i = 0; i <= maximo; i++) {
            combo.getItems().add(i);
        }
        combo.setEditable(true);
        combo.setPromptText(prompt);
        combo.getStyleClass().add("estilo-combo");
        combo.focusedProperty().addListener((obs, focusAnterior, ahoraConFoco) -> {
            if (!ahoraConFoco) {
                normalizarValorCombo(combo, maximo);
            }
        });
    }

    private Integer normalizarValorCombo(ComboBox<Integer> combo, int maximo) {
        String texto = combo.getEditor().getText();
        if (texto == null || texto.isBlank()) {
            return combo.getValue();
        }
        try {
            int valor = Integer.parseInt(texto.trim());
            if (valor < 0) {
                valor = 0;
            }
            if (valor > maximo) {
                valor = maximo;
            }
            combo.setValue(valor);
            combo.getEditor().setText(String.valueOf(valor));
            return valor;
        } catch (NumberFormatException ex) {
            if (combo.getValue() != null) {
                combo.getEditor().setText(String.valueOf(combo.getValue()));
                return combo.getValue();
            }
            combo.getEditor().clear();
            return null;
        }
    }

    private void mostrarMensaje(Label label, String mensaje, boolean esError) {
        label.setText(mensaje);
        label.getStyleClass().removeAll("mensaje-exito", "mensaje-error");
        label.getStyleClass().add(esError ? "mensaje-error" : "mensaje-exito");
    }

    private String construirMensajeConfirmacion(LocalDate inicioSemana, boolean tipo, int horas, int minutos, boolean esActualizacion) {
        LocalDate finSemana = inicioSemana.plusDays(6);
        String tiempo = String.format("%s%02d:%02d", tipo ? "+" : "-", horas, minutos);
        String accion = esActualizacion
            ? "¿Desea actualizar el registro de horas extra?"
            : "¿Desea guardar el registro de horas extra?";

        return "Semana del " + inicioSemana.format(FORMATO_FECHA) + " al " + finSemana.format(FORMATO_FECHA)
            + "\nTiempo: " + tiempo
            + "\n\n" + accion;
    }

    private void limpiarFormulario(ComboBox<String> tipo, ComboBox<Integer> horas, ComboBox<Integer> minutos) {
        tipo.setValue(null);
        horas.setValue(null);
        horas.getEditor().clear();
        minutos.setValue(null);
        minutos.getEditor().clear();
    }
}
