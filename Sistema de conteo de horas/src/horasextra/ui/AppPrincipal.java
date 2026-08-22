package horasextra.ui;

import horasextra.repositorio.RegistroRepositorio;
import horasextra.repositorio.RegistroRepositorioSQLite;

import java.net.URL;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;

public class AppPrincipal extends Application {
    private final RegistroRepositorio registroRepositorio = new RegistroRepositorioSQLite(rutaBaseDatos());
    private final VistaResumen vistaResumen = new VistaResumen(registroRepositorio);
    private final VistaCalendario calendario = new VistaCalendario(registroRepositorio);
    private final VistaFormularioRegistro formularioRegistro = new VistaFormularioRegistro(
        registroRepositorio, calendario, vistaResumen::actualizarHorasTotales
    );

    private static String rutaBaseDatos() {
        String carpetaDatos = System.getProperty("user.home") + File.separator + ".horas_extra";
        new File(carpetaDatos).mkdirs();
        return carpetaDatos + File.separator + "horas_extra.db";
    }

    @Override
    public void start(Stage stage) {
        URL logoUrl = AppPrincipal.class.getResource("favicon-32x32.png");
        Image logo = logoUrl == null ? null : new Image(logoUrl.toExternalForm());
        ImageView icono = new ImageView(logo);
        icono.setFitWidth(18);
        icono.setFitHeight(18);
        icono.setPreserveRatio(true);
        Label titulo = new Label("Sistema de conteo de horas extra");
        titulo.getStyleClass().add("titulo-principal");
        ToggleButton interruptorTema = new ToggleButton("☀ Claro");
        interruptorTema.getStyleClass().add("interruptor-tema");
        HBox separador = new HBox();
        HBox.setHgrow(separador, Priority.ALWAYS);
        HBox barraSuperior = new HBox(10, icono, titulo, separador, interruptorTema);
        barraSuperior.getStyleClass().add("barra-superior");

        Label tituloInstrucciones = new Label("Instrucciones");
        tituloInstrucciones.getStyleClass().add("seccion-titulo");
        Label instrucciones = new Label(
            "1. Selecciona la semana que quieres registrar.\n"
            + "2. Elige si son horas extra o déficit.\n"
            + "3. Ingresa horas y minutos, y presiona Guardar."
        );
        instrucciones.getStyleClass().add("instrucciones");

        VBox encabezado = new VBox(10, barraSuperior, tituloInstrucciones, instrucciones);
        encabezado.getStyleClass().addAll("panel", "encabezado");

        VBox formulario = new VBox(12, calendario.crearVista(), formularioRegistro.crearVista());
        formulario.getStyleClass().addAll("panel", "panel-calendario");
        VBox resumen = new VBox(12, vistaResumen.crearVista());
        resumen.getStyleClass().addAll("panel", "panel-resumen");
        HBox.setHgrow(formulario, Priority.ALWAYS);
        HBox paneles = new HBox(16, formulario, resumen);
        paneles.getStyleClass().add("contenido-principal");

        VBox layout = new VBox(12, encabezado, paneles);
        layout.getStyleClass().add("contenedor-app");
        Scene scene = new Scene(layout, 860, 780);
        URL css = AppPrincipal.class.getResource("app.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }
        TemaUtil.aplicar(scene);
        interruptorTema.setOnAction(e -> {
            TemaUtil.establecerModoClaro(interruptorTema.isSelected());
            interruptorTema.setText(interruptorTema.isSelected() ? "☾ Oscuro" : "☀ Claro");
            TemaUtil.aplicar(scene);
        });

        stage.setTitle("Sistema de Conteo de Horas Extra");
        if (logo != null) {
            stage.getIcons().add(logo);
        }
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
