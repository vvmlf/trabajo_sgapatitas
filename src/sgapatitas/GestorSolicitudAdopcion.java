package sgapatitas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class GestorSolicitudAdopcion {

    public int registrarSolicitud(int idAnimal, int idAdoptante, int idUsuario) throws SQLException {
        String sql = "INSERT INTO solicitud_adopcion (id_animal, id_adoptante, id_usuario, fecha, estado) VALUES (?,?,?,?,'pendiente')";
        String sqlEstado = "UPDATE animal SET estado = 'En proceso de adopcion' WHERE id_animal = ?";
        try (Connection con = Conexion.getConexion()) {
            con.setAutoCommit(false);
            try {
                int idSolicitud;
                try (PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, idAnimal);
                    ps.setInt(2, idAdoptante);
                    ps.setInt(3, idUsuario);
                    ps.setObject(4, LocalDate.now());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) throw new SQLException("No se pudo obtener el ID de la solicitud.");
                        idSolicitud = keys.getInt(1);
                    }
                }
                try (PreparedStatement ps2 = con.prepareStatement(sqlEstado)) {
                    ps2.setInt(1, idAnimal);
                    ps2.executeUpdate();
                }
                con.commit();
                return idSolicitud;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }
}
