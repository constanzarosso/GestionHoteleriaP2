package ConexionBD;

import Excepciones.PersistenciaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class GestorPasajerosBD {
    private static void crearPasajeros () throws PersistenciaException{
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
        }finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new PersistenciaException("Error al cerrar: " + e.getMessage());
            }
        }
    }
    public static void createTable() throws PersistenciaException{
        crearPasajeros();
    }
}
