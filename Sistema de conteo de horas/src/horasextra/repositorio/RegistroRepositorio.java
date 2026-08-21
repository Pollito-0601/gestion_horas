package horasextra.repositorio;

public interface RegistroRepositorio {
    public void guardar(int id, String fechaInicioSemana, int minutosExtra, String fechaCreacion, String fechaModificacion);
    public void buscarPorSemana(String fechaInicioSemana);
    public void listarTodos();
    public void listarEnRango(String fechaInicio, String fechaFin);
    public void eliminar(int id);
    public void actualizar(int id, String fechaInicioSemana, int minutosExtra, String fechaCreacion, String fechaModificacion);
}
