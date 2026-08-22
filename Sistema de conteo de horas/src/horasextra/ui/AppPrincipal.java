package horasextra.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.net.URL;
import horasextra.repositorio.RegistroRepositorioMemoria;

public class AppPrincipal extends Application {
    private final RegistroRepositorioMemoria registroRepositorio = new RegistroRepositorioMemoria();
    private final VistaResumen vistaResumen = new VistaResumen(registroRepositorio);
    private final VistaCalendario calendario = new VistaCalendario(registroRepositorio);
    private final VistaFormularioRegistro formularioRegistro = new VistaFormularioRegistro(
        registroRepositorio,
        calendario,
        vistaResumen::actualizarHorasTotales
    );

    @Override
    public void start(Stage stage) {
        Label tituloLabel = new Label("Sistema de Conteo de Horas Extra");
        Label label = new Label("Bienvenido al Sistema de Conteo de Horas Extra");
        Label instrucciones = new Label("Instrucciones: \n1. Seleccione la semana que quiere registrar.\n2. Seleccione si son horas extra (+) o horas menos (-).\n3. Ingrese la cantidad de horas.\n4. Ingrese la cantidad de minutos.\n5. Presione el botón 'Guardar' para registrar las horas extra.");
        tituloLabel.getStyleClass().add("titulo-principal");

        HBox paneles = new HBox(16);
        VBox formularioLayout = new VBox(10, calendario.crearVista(), formularioRegistro.crearVista());
        formularioLayout.getStyleClass().add("panel");
        VBox resumenLayout = new VBox(10, vistaResumen.crearVista());
        resumenLayout.getStyleClass().add("panel");
        paneles.getChildren().addAll(formularioLayout, resumenLayout);

        VBox layout = new VBox(12);
        VBox encabezadoLayout = new VBox(6, tituloLabel, label, instrucciones);
        encabezadoLayout.getStyleClass().add("panel");
        layout.getChildren().addAll(encabezadoLayout, paneles);

        Scene scene = new Scene(layout, 800, 800);
        URL css = AppPrincipal.class.getResource("app.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        stage.setTitle("Sistema de Conteo de Horas Extra");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}