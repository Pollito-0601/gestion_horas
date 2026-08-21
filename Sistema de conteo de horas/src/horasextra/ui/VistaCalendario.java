package horasextra.ui;

import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import horasextra.util.SemanaUtil;

public class VistaCalendario {
    private final DatePicker calendario = new DatePicker();

    public VBox crearVista() {
        Label titulo = new Label("Calendario");
        VBox layout = new VBox(10);
        layout.getChildren().addAll(titulo, calendario);
        return layout;
    }

    public LocalDate getFechaSeleccionada() {
        LocalDate fecha = calendario.getValue();
        if (fecha == null) {
            return null;
        }
        return SemanaUtil.fechaInicioSemana(fecha);
    }
}
