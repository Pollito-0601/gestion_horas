package horasextra.modelo;
import java.util.Date;

public class RegistroSemanal {
    private int id;
    private Date fechaInicioSemana = new Date();
    private int minutosExtra;
    private Date fechaCreacion = new Date();
    private Date fechaModificacion = new Date();

    public RegistroSemanal(int id, Date fechaInicioSemana, int minutosExtra, Date fechaCreacion, Date fechaModificacion){
        this.id = id;
        this.fechaInicioSemana = fechaInicioSemana;
        this.minutosExtra = minutosExtra;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
    }

    //Setters

    public void setId(int id){
        this.id = id;
    }

    public void setFechaInicioSemana(Date fechaInicioSemana){
        this.fechaInicioSemana = fechaInicioSemana;
    }

    public void setMinutosExtra(int minutosExtra){
        this.minutosExtra = minutosExtra;
    }

    public void setFechaCreacion(Date fechaCreacion){
        this.fechaCreacion = fechaCreacion;
    }

    public void setFechaModificacion(Date fechaModificacion){
        this.fechaModificacion = fechaModificacion;
    }

    //Getters

    public int getId(){
        return id;
    }

    public Date getFechaInicioSemana(){
        return fechaInicioSemana;
    }

    public int getMinutosExtra(){
        return minutosExtra;
    }

    public Date getFechaCreacion(){
        return fechaCreacion;
    }

    public Date getFechaModificacion(){
        return fechaModificacion;
    }
}
