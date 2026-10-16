package sgapatitas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class GestorAdoptante {

    public int registrarAdoptante(String nombre, String apellido, String dni, String telefono, String formularioIdoneidad) {
        String sql = "INSERT INTO adoptante (nombre, apellido, dni, telefono, formulario_idoneidad, fecha_registro) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, dni);
            ps.setString(4, telefono);
            ps.setString(5, formularioIdoneidad);
            ps.setObject(6, LocalDate.now());
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("No se pudo registrar el adoptante: " + e.getMessage());
        }
        return -1;
    }
}