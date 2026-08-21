package horasextra.repositorio;
import java.util.Map;
import java.time.LocalDate;
import horasextra.modelo.RegistroSemanal;

public class RegistroRepositorioMemoria implements RegistroRepositorio {
    private static Map<LocalDate, RegistroSemanal> registros;

    @Override
    public void guardar(int id, LocalDate fechaInicioSemana, int minutosExtra, LocalDate fechaCreacion, LocalDate fechaModificacion) {
        RegistroSemanal nuevoRegistro = new RegistroSemanal(id, fechaInicioSemana, minutosExtra, fechaCreacion, fechaModificacion);
        registros.put(nuevoRegistro.getFechaInicioSemana(), nuevoRegistro);
    }

    @Override
    public RegistroSemanal buscarPorSemana(LocalDate fechaInicioSemana) {
        return registros.get(fechaInicioSemana);
    }

    @Override
    public void listarTodos() {
        for(RegistroSemanal registro : registros.values()){
            System.out.println("ID: " + registro.getId() + ", Fecha Inicio Semana: " + registro.getFechaInicioSemana() + ", Minutos Extra: " + registro.getMinutosExtra() + ", Fecha Creación: " + registro.getFechaCreacion() + ", Fecha Modificación: " + registro.getFechaModificacion());
        }
    }
    
    @Override
    public void listarEnRango(LocalDate fechaInicio, LocalDate fechaFin) {
        for(RegistroSemanal registro : registros.values()){
            if(!registro.getFechaInicioSemana().isBefore(fechaInicio) && !registro.getFechaInicioSemana().isAfter(fechaFin)){
                System.out.println("ID: " + registro.getId() + ", Fecha Inicio Semana: " + registro.getFechaInicioSemana() + ", Minutos Extra: " + registro.getMinutosExtra() + ", Fecha Creación: " + registro.getFechaCreacion() + ", Fecha Modificación: " + registro.getFechaModificacion());
            }
        }
    }

    @Override
    public void eliminar(LocalDate fechaInicioSemana) {
        registros.values().removeIf(registro -> registro.getFechaInicioSemana() == fechaInicioSemana);
    }

    @Override
    public void actualizar(int id, LocalDate fechaInicioSemana, int minutosExtra, LocalDate fechaCreacion, LocalDate fechaModificacion) {
        if(registros.containsKey(fechaInicioSemana)){
            RegistroSemanal registroExistente = registros.get(fechaInicioSemana);
            registroExistente.setId(id);
            registroExistente.setMinutosExtra(minutosExtra);
            registroExistente.setFechaCreacion(fechaCreacion);
            registroExistente.setFechaModificacion(fechaModificacion);
            registros.put(fechaInicioSemana, registroExistente);
        }
    }
}