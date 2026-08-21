package horasextra.repositorio;
import java.util.Map;
import java.time.LocalDate;
import horasextra.modelo.RegistroSemanal;
import java.util.TreeMap;
import horasextra.util.SemanaUtil;

public class RegistroRepositorioMemoria implements RegistroRepositorio {
    private final Map<LocalDate, RegistroSemanal> registros = new TreeMap<>();

    @Override
    public void guardar(LocalDate fechaInicioSemana, int minutosExtra, LocalDate fechaCreacion, LocalDate fechaModificacion) {
        RegistroSemanal nuevoRegistro = new RegistroSemanal(fechaInicioSemana, minutosExtra, fechaCreacion, fechaModificacion);
        registros.put(nuevoRegistro.getFechaInicioSemana(), nuevoRegistro);
    }

    @Override
    public RegistroSemanal buscarPorSemana(LocalDate fechaInicioSemana) {
        LocalDate fechaInicio = SemanaUtil.fechaInicioSemana(fechaInicioSemana);
        return registros.get(fechaInicio);
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
            if(!registro.getFechaInicioSemana().isBefore(SemanaUtil.fechaInicioSemana(fechaInicio)) && !registro.getFechaInicioSemana().isAfter(SemanaUtil.fechaInicioSemana(fechaFin))){
                System.out.println("ID: " + registro.getId() + ", Fecha Inicio Semana: " + registro.getFechaInicioSemana() + ", Minutos Extra: " + registro.getMinutosExtra() + ", Fecha Creación: " + registro.getFechaCreacion() + ", Fecha Modificación: " + registro.getFechaModificacion());
            }
        }
    }

    @Override
    public void eliminar(LocalDate fechaInicioSemana) {
        LocalDate fechaInicio = SemanaUtil.fechaInicioSemana(fechaInicioSemana);
        registros.remove(fechaInicio);
    }

    @Override
    public void actualizar(LocalDate fechaInicioSemana, int minutosExtra, LocalDate fechaModificacion) {
        LocalDate fechaInicio = SemanaUtil.fechaInicioSemana(fechaInicioSemana);
        if(registros.containsKey(fechaInicio)){
            RegistroSemanal registroExistente = registros.get(fechaInicio);
            registroExistente.setMinutosExtra(minutosExtra);
            registroExistente.setFechaModificacion(fechaModificacion);
            registros.put(fechaInicio, registroExistente);
        }
    }
}