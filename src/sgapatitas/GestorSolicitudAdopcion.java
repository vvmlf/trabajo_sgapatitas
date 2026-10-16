package sgapatitas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class GestorSolicitudAdopcion {

    public int registrarSolicitud(int idAnimal, int idAdoptante, int idUsuario) {
        String sql = "INSERT INTO solicitud_adopcion (id_animal, id_adoptante, id_usuario, fecha, estado) VALUES (?,?,?,?,'pendiente')";
        String sqlEstado = "UPDATE animal SET estado = 'en_proceso_adopcion' WHERE id_animal = ?";
        try (Connection con = Conexion.getConexion()) {
            PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, idAnimal);
            ps.setInt(2, idAdoptante);
            ps.setInt(3, idUsuario);
            ps.setObject(4, LocalDate.now());
            ps.executeUpdate();

            PreparedStatement ps2 = con.prepareStatement(sqlEstado);
            ps2.setInt(1, idAnimal);
            ps2.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("no se pudo registrar la solicitud: " + e.getMessage());
        }
        return -1;
    }

}
