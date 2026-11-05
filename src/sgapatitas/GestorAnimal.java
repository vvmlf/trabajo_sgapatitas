package sgapatitas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestorAnimal {

    public void guardar(Animal a) throws SQLException {
        String sql = "INSERT INTO animal (especie, edad_aprox, sexo, fecha_ingreso, descripcion, estado) VALUES (?,?,?,?,?,?)";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getEspecie());
            ps.setInt(2, a.getEdadAprox());
            ps.setString(3, a.getSexo());
            ps.setObject(4, a.getFechaIngreso());
            ps.setString(5, a.getDescripcion());
            ps.setString(6, a.getEstado());
            ps.executeUpdate();
        }
    }

    public List<Animal> listarDisponibles() throws SQLException {
        return listar("SELECT * FROM animal WHERE estado = 'disponible'");
    }

    public List<Animal> listarTodos() throws SQLException {
        return listar("SELECT * FROM animal ORDER BY id_animal");
    }

    private List<Animal> listar(String sql) throws SQLException {
        List<Animal> lista = new ArrayList<>();
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String especie = rs.getString("especie");
                int edad = rs.getInt("edad_aprox");
                String sexo = rs.getString("sexo");
                String desc = rs.getString("descripcion");
                Animal a;
                if ("gato".equalsIgnoreCase(especie)) {
                    a = new Animal.Gato(edad, sexo, desc);
                } else {
                    a = new Animal.Perro(edad, sexo, desc);
                }
                a.setIdAnimal(rs.getInt("id_animal"));
                a.setEstado(rs.getString("estado"));
                lista.add(a);
            }
        }
        return lista;
    }

    public void cambiarEstado(int idAnimal, String nuevoEstado) throws SQLException {
        String sql = "UPDATE animal SET estado = ? WHERE id_animal = ?";
        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, idAnimal);
            if (ps.executeUpdate() == 0) {
                throw new SQLException("No existe un animal con ese ID.");
            }
        }
    }
}
