package horasextra.ui;

import horasextra.repositorio.RegistroRepositorio;
import horasextra.util.SemanaUtil;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class VistaCalendario {
    private final RegistroRepositorio registroRepositorio;
    private final GridPane grillaDias = new GridPane();
    private final Label mesLabel = new Label();
    private final Label semanaSeleccionadaLabel = new Label();
    private LocalDate fechaSeleccionada = LocalDate.now();
    private YearMonth mesMostrado = YearMonth.now();

    public VistaCalendario(RegistroRepositorio registroRepositorio) {
        this.registroRepositorio = registroRepositorio;
        grillaDias.setHgap(4);
        grillaDias.setVgap(4);
        actualizarCalendario();
        actualizarSemanaSeleccionada();
    }

    private void actualizarSemanaSeleccionada() {
        LocalDate inicioSemana = getFechaSeleccionada();
        if (inicioSemana == null) {
            semanaSeleccionadaLabel.setText("No hay semana seleccionada.");
            return;
        }
        semanaSeleccionadaLabel.setText("Semana seleccionada (inicio): " + inicioSemana);
    }

    private void actualizarCalendario() {
        grillaDias.getChildren().clear();
        mesLabel.setText(mesMostrado.getMonth() + " " + mesMostrado.getYear());

        LocalDate primerDiaMes = mesMostrado.atDay(1);
        int desplazamiento = primerDiaMes.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
        if (desplazamiento < 0) {
            desplazamiento += 7;
        }
        LocalDate primerDiaGrilla = primerDiaMes.minusDays(desplazamiento);

        for (int i = 0; i < 42; i++) {
            LocalDate fechaCelda = primerDiaGrilla.plusDays(i);
            Button diaButton = new Button(String.valueOf(fechaCelda.getDayOfMonth()));
            diaButton.getStyleClass().add("cal-dia");
            // La selección se representa por semana; el botón no debe conservar
            // foco visual que resalte un único día.
            diaButton.setFocusTraversable(false);
            diaButton.setMaxWidth(Double.MAX_VALUE);
            diaButton.setPrefHeight(32);
            GridPane.setHgrow(diaButton, Priority.ALWAYS);

            if (!fechaCelda.getMonth().equals(mesMostrado.getMonth())) {
                diaButton.getStyleClass().add("cal-otro-mes");
            }
            if (registroRepositorio.buscarPorSemana(fechaCelda) != null) {
                diaButton.getStyleClass().add("cal-con-registro");
            }
            if (esMismaSemanaSeleccionada(fechaCelda)) {
                diaButton.getStyleClass().add("cal-semana-seleccionada");
            }

            diaButton.setOnAction(e -> {
                fechaSeleccionada = fechaCelda;
                mesMostrado = YearMonth.from(fechaSeleccionada);
                actualizarSemanaSeleccionada();
                actualizarCalendario();
            });

            grillaDias.add(diaButton, i % 7, i / 7);
        }
    }

    public void refrescarCalendario() {
        actualizarSemanaSeleccionada();
        actualizarCalendario();
    }

    public VBox crearVista() {
        Label titulo = new Label("Calendario");
        titulo.getStyleClass().add("seccion-titulo");
        Button mesAnterior = new Button("<");
        Button mesSiguiente = new Button(">");
        mesAnterior.getStyleClass().add("boton-navegacion");
        mesSiguiente.getStyleClass().add("boton-navegacion");
        mesAnterior.setOnAction(e -> {
            mesMostrado = mesMostrado.minusMonths(1);
            actualizarCalendario();
        });
        mesSiguiente.setOnAction(e -> {
            mesMostrado = mesMostrado.plusMonths(1);
            actualizarCalendario();
        });

        mesLabel.getStyleClass().add("mes-calendario");
        mesLabel.setMaxWidth(Double.MAX_VALUE);
        mesLabel.setAlignment(Pos.CENTER);
        HBox.setHgrow(mesLabel, Priority.ALWAYS);
        HBox cabecera = new HBox(8, mesAnterior, mesLabel, mesSiguiente);
        cabecera.setAlignment(Pos.CENTER_LEFT);

        HBox diasSemana = new HBox(4);
        String[] nombresDias = {"Lun", "Mar", "Mie", "Jue", "Vie", "Sab", "Dom"};
        for (String nombreDia : nombresDias) {
            Label diaLabel = new Label(nombreDia);
            diaLabel.getStyleClass().add("cal-dia-semana");
            diaLabel.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(diaLabel, Priority.ALWAYS);
            diaLabel.setAlignment(Pos.CENTER);
            diasSemana.getChildren().add(diaLabel);
        }

        Label leyendaRegistrada = new Label("■ Registrada");
        leyendaRegistrada.getStyleClass().add("leyenda-registrada");
        Label leyendaSeleccionada = new Label("■ Seleccionada");
        leyendaSeleccionada.getStyleClass().add("leyenda-seleccionada");
        HBox leyenda = new HBox(12, leyendaRegistrada, leyendaSeleccionada);
        leyenda.getStyleClass().add("leyenda-calendario");
        semanaSeleccionadaLabel.getStyleClass().add("semana-seleccionada-texto");

        VBox layout = new VBox(10);
        layout.getChildren().addAll(titulo, cabecera, diasSemana, grillaDias, leyenda, semanaSeleccionadaLabel);
        return layout;
    }

    public LocalDate getFechaSeleccionada() {
        if (fechaSeleccionada == null) {
            return null;
        }
        return SemanaUtil.fechaInicioSemana(fechaSeleccionada);
    }

    private boolean esMismaSemanaSeleccionada(LocalDate fecha) {
        if (fechaSeleccionada == null || fecha == null) {
            return false;
        }
        return SemanaUtil.fechaInicioSemana(fecha).equals(SemanaUtil.fechaInicioSemana(fechaSeleccionada));
    }
}
