package horasextra.repositorio;
import java.util.Map;
import java.time.LocalDate;
import horasextra.modelo.RegistroSemanal;
import java.util.ArrayList;
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
    public String[] listarTodos() {
        String[] resultados = new String[registros.size()];
        int i = 0;
        for(RegistroSemanal registro : registros.values()){
            resultados[i] = formatearRegistro(registro);
            i++;
        }
        return resultados;
    }
    
    @Override
    public String[] listarEnRango(LocalDate fechaInicio, LocalDate fechaFin) {
        ArrayList<String> resultados = new ArrayList<>();
        for(RegistroSemanal registro : registros.values()){
            if(!registro.getFechaInicioSemana().isBefore(SemanaUtil.fechaInicioSemana(fechaInicio)) && !registro.getFechaInicioSemana().isAfter(SemanaUtil.fechaInicioSemana(fechaFin))){
                resultados.add(formatearRegistro(registro));
            }
        }
        return resultados.toArray(new String[0]);
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

    @Override
    public int getTotalMinutos() {
        int totalMinutos = 0;
        for(RegistroSemanal registro : registros.values()){
            totalMinutos += registro.getMinutosExtra();
        }
        return totalMinutos;
    }

    private String formatearRegistro(RegistroSemanal registro) {
        return "ID: " + registro.getId()
            + "\nFecha Inicio Semana: " + registro.getFechaInicioSemana()
            + "\nMinutos Extra: " + registro.getMinutosExtra()
            + "\nFecha Creación: " + registro.getFechaCreacion()
            + "\nFecha Modificación: " + registro.getFechaModificacion()
            + "\n----------------------------------------";
    }
}