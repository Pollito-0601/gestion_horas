package horasextra.modelo;
import java.time.LocalDate;
import horasextra.util.SemanaUtil;

public class RegistroSemanal {
    private int id;
    private LocalDate fechaInicioSemana;
    private int minutosExtra;
    private LocalDate fechaCreacion;
    private LocalDate fechaModificacion;
    private static int contador = 0;

    public RegistroSemanal(LocalDate fechaInicioSemana, int minutosExtra, LocalDate fechaCreacion, LocalDate fechaModificacion){
        this.id = ++contador;
        this.fechaInicioSemana = SemanaUtil.fechaInicioSemana(fechaInicioSemana);
        this.minutosExtra = minutosExtra;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
    }

    //Setters

    public void setId(int id){
        this.id = id;
    }

    public void setMinutosExtra(int minutosExtra){
        this.minutosExtra = minutosExtra;
    }

    public void setFechaCreacion(LocalDate fechaCreacion){
        this.fechaCreacion = fechaCreacion;
    }

    public void setFechaModificacion(LocalDate fechaModificacion){
        this.fechaModificacion = fechaModificacion;
    }

    //Getters

    public int getId(){
        return id;
    }

    public LocalDate getFechaInicioSemana(){
        return fechaInicioSemana;
    }

    public int getMinutosExtra(){
        return minutosExtra;
    }

    public LocalDate getFechaCreacion(){
        return fechaCreacion;
    }

    public LocalDate getFechaModificacion(){
        return fechaModificacion;
    }
}
