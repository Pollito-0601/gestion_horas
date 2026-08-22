package horasextra.ui;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public final class TemaUtil {
    private static boolean modoClaro;

    private TemaUtil() {
    }

    public static void establecerModoClaro(boolean activo) {
        modoClaro = activo;
    }

    public static void aplicar(Scene escena) {
        escena.getRoot().getStyleClass().remove("modo-claro");
        if (modoClaro) {
            escena.getRoot().getStyleClass().add("modo-claro");
        }
    }

    public static void aplicarIcono(Stage ventana) {
        var urlLogo = TemaUtil.class.getResource("favicon-32x32.png");
        if (urlLogo != null) {
            ventana.getIcons().setAll(new Image(urlLogo.toExternalForm()));
        }
    }
}
