package horasextra.util;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class SemanaUtil {
    public static LocalDate fechaInicioSemana(LocalDate fecha){
        return fecha.with(DayOfWeek.MONDAY);
    }
}
