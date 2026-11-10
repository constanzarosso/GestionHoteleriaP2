package ConexionBD;


import Excepciones.ExcepcionesDB.PersistenciaException;
import models.Pasajero;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class GestorPasajerosBD {
    private GestorPasajerosBD() {
    }

    private static void crearPasajeros() throws PersistenciaException {
        PreparedStatement ps = null;
        try {
            Connection conn = Conexion.getConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS pasajeros ("
                    + "dni INT NOT NULL, "
                    + "nombre VARCHAR(45) NOT NULL, "
                    + "apellido VARCHAR(45) NOT NULL, "
                    + "origen VARCHAR(45) NOT NULL, "
                    + "domicilio_origen VARCHAR(100) NOT NULL, "
                    + "telefono VARCHAR(20) NULL, "
                    + "email VARCHAR(100) NULL, "
                    + "PRIMARY KEY (dni))");
            ps.execute();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al crear la tabla pasajeros: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new PersistenciaException("Error al cerrar: " + e.getMessage());
            }
        }
    }

    public static void createTable() throws PersistenciaException {
        crearPasajeros();
    }
    public List<Pasajero> listar() throws PersistenciaException {

        List<Pasajero> pasajeros = new ArrayList<>();

        String sql = "SELECT * FROM pasajeros";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Pasajero pasajero = new Pasajero(
                        rs.getInt("dni"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("origen"),
                        rs.getString("domicilio_origen"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );

                pasajeros.add(pasajero);
            }

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al listar pasajeros: " + e.getMessage());
        }

        return pasajeros;
    }

    public void insertar(Pasajero pasajero)
            throws PersistenciaException {

        String sql = "INSERT INTO pasajeros "
                + "(dni, nombre, apellido, origen, domicilio_origen, telefono, email) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, pasajero.getId());
            ps.setString(2, pasajero.getNombre());
            ps.setString(3, pasajero.getApellido());
            ps.setString(4, pasajero.getOrigen());
            ps.setString(5, pasajero.getDomicilioOrigen());
            ps.setString(6, pasajero.getTelefono());
            ps.setString(7, pasajero.getEmail());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al insertar pasajero: " + e.getMessage());
        }
    }

    public void modificar(Pasajero pasajero)
            throws PersistenciaException {

        String sql = "UPDATE pasajeros SET "
                + "domicilio_origen = ?, telefono = ?, email = ? "
                + "WHERE dni = ?";

        try (Connection conn = Conexion.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pasajero.getDomicilioOrigen());
            ps.setString(2, pasajero.getTelefono());
            ps.setString(3, pasajero.getEmail());
            ps.setInt(4, pasajero.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException(
                    "Error al modificar pasajero: " + e.getMessage());
        }
    }

}
