package horasextra.repositorio;
import java.time.LocalDate;
import horasextra.modelo.RegistroSemanal;

public interface RegistroRepositorio {
    public void guardar(LocalDate fechaInicioSemana, int minutosExtra, LocalDate fechaCreacion, LocalDate fechaModificacion);
    public RegistroSemanal buscarPorSemana(LocalDate fechaInicioSemana);
    public void listarTodos();
    public void listarEnRango(LocalDate fechaInicio, LocalDate fechaFin);
    public void eliminar(LocalDate fechaInicioSemana);
    public void actualizar(LocalDate fechaInicioSemana, int minutosExtra, LocalDate fechaModificacion);
}
