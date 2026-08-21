package horasextra.repositorio;
import java.time.LocalDate;
import horasextra.modelo.RegistroSemanal;

public interface RegistroRepositorio {
    public void guardar(int id, LocalDate fechaInicioSemana, int minutosExtra, LocalDate fechaCreacion, LocalDate fechaModificacion);
    public RegistroSemanal buscarPorSemana(LocalDate fechaInicioSemana);
    public void listarTodos();
    public void listarEnRango(LocalDate fechaInicio, LocalDate fechaFin);
    public void eliminar(LocalDate fechaInicioSemana);
    public void actualizar(int id, LocalDate fechaInicioSemana, int minutosExtra, LocalDate fechaModificacion);
}
