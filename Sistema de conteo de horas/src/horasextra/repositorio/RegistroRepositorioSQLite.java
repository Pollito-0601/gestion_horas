package horasextra.repositorio;

import horasextra.modelo.RegistroSemanal;
import horasextra.util.SemanaUtil;
import horasextra.logica.ConversorTiempo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class RegistroRepositorioSQLite implements RegistroRepositorio {
    private final Connection conexion;
    private final ConversorTiempo conversorTiempo = new ConversorTiempo();

    public RegistroRepositorioSQLite(String url){
        try{
            conexion = DriverManager.getConnection("jdbc:sqlite:" + url);
            crearTablaSiNoExiste();
        } catch (SQLException e) {
            throw new RuntimeException("No pudimos abrir la base de datos: " + e.getMessage(), e);
        }
    }

    private void crearTablaSiNoExiste() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS registros_semanales (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "fecha_inicio_semana TEXT NOT NULL UNIQUE," +
                     "minutos_extra INTEGER NOT NULL," +
                     "fecha_creacion TEXT NOT NULL," +
                     "fecha_modificacion TEXT NOT NULL" +
                     ");";
        try (Statement stmt = conexion.createStatement()) {
            stmt.execute(sql);
        }
    }

    @Override
    public void guardar(LocalDate fechaInicioSemana, int minutosExtra, LocalDate fechaCreacion, LocalDate fechaModificacion) {
        LocalDate fechaInicio = SemanaUtil.fechaInicioSemana(fechaInicioSemana);
        String sql = "INSERT INTO registros_semanales (fecha_inicio_semana, minutos_extra, fecha_creacion, fecha_modificacion) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, fechaInicio.toString());
            stmt.setInt(2, minutosExtra);
            stmt.setString(3, fechaCreacion.toString());
            stmt.setString(4, fechaModificacion.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo guardar el registro: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizar(LocalDate fechaInicioSemana, int minutosExtra, LocalDate fechaModificacion) {
        LocalDate fechaInicio = SemanaUtil.fechaInicioSemana(fechaInicioSemana);
        String sql = "UPDATE registros_semanales SET minutos_extra = ?, fecha_modificacion = ? WHERE fecha_inicio_semana = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setInt(1, minutosExtra);
            stmt.setString(2, fechaModificacion.toString());
            stmt.setString(3, fechaInicio.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo actualizar el registro: " + e.getMessage(), e);
        }
    }

    @Override
    public RegistroSemanal buscarPorSemana(LocalDate fechaInicioSemana) {
        LocalDate fechaInicio = SemanaUtil.fechaInicioSemana(fechaInicioSemana);
        String sql = "SELECT * FROM registros_semanales WHERE fecha_inicio_semana = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, fechaInicio.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearFila(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo buscar el registro: " + e.getMessage(), e);
        }
    }

    private RegistroSemanal mapearFila(ResultSet rs) throws SQLException {
        LocalDate fechaInicioSemana = LocalDate.parse(rs.getString("fecha_inicio_semana"));
        int minutosExtra = rs.getInt("minutos_extra");
        LocalDate fechaCreacion = LocalDate.parse(rs.getString("fecha_creacion"));
        LocalDate fechaModificacion = LocalDate.parse(rs.getString("fecha_modificacion"));
        RegistroSemanal registro = new RegistroSemanal(fechaInicioSemana, minutosExtra, fechaCreacion, fechaModificacion);
        registro.setId(rs.getInt("id"));
        return registro;
    }

    @Override
    public String[] listarTodos() {
        String sql = "SELECT * FROM registros_semanales ORDER BY fecha_inicio_semana";
        ArrayList<String> resultados = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RegistroSemanal registro = mapearFila(rs);
                    resultados.add(formatearRegistro(registro));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo listar los registros: " + e.getMessage(), e);
        }
        return resultados.toArray(new String[0]);
    }

    @Override
    public String[] listarEnRango(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDate fechaInicioSemana = SemanaUtil.fechaInicioSemana(fechaInicio);
        LocalDate fechaFinSemana = SemanaUtil.fechaInicioSemana(fechaFin);
        String sql = "SELECT * FROM registros_semanales WHERE fecha_inicio_semana BETWEEN ? AND ? ORDER BY fecha_inicio_semana";
        ArrayList<String> resultados = new ArrayList<>();
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, fechaInicioSemana.toString());
            stmt.setString(2, fechaFinSemana.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RegistroSemanal registro = mapearFila(rs);
                    resultados.add(formatearRegistro(registro));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo listar los registros en el rango: " + e.getMessage(), e);
        }
        return resultados.toArray(new String[0]);
    }

    private String formatearRegistro(RegistroSemanal registro) {
        return "ID: " + registro.getId() +
        "\nFecha de inicio de semana: " + registro.getFechaInicioSemana() +
        "\nMinutos extra: " + registro.getMinutosExtra() +
        "\nHoras extra: " + conversorTiempo.convertirAHoras(registro.getMinutosExtra()) +
        "\nFecha de creación: " + registro.getFechaCreacion() +
        "\nFecha de modificación: " + registro.getFechaModificacion() +
        "\n-------------------------------------";
    }

    @Override
    public void eliminar(LocalDate fechaInicioSemana) {
        LocalDate fechaInicio = SemanaUtil.fechaInicioSemana(fechaInicioSemana);
        String sql = "DELETE FROM registros_semanales WHERE fecha_inicio_semana = ?";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, fechaInicio.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo eliminar el registro: " + e.getMessage(), e);
        }
    }

    @Override
    public int getTotalMinutos() {
        String sql = "SELECT SUM(minutos_extra) AS total_minutos FROM registros_semanales";
        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int totalMinutos = rs.getInt("total_minutos");
                    if (rs.wasNull()) {
                        return 0;
                    }
                    return totalMinutos;
                }
                return 0;
            } 
        } catch (SQLException e) {
            throw new RuntimeException("No se pudo calcular el total de minutos: " + e.getMessage(), e);
        }
    }
}
