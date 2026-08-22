package horasextra.ui;

import javafx.scene.Scene;

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
}
