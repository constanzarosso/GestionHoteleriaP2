package ConexionBD;

import Excepciones.PersistenciaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class GestorHabitacionesBD {

    private static void crearHabitaciones() throws PersistenciaException {
        PreparedStatement ps = null;
        try {
            Connection conn = Conexion.getConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS habitaciones ("
                    + "numero INT NOT NULL, "
                    + "tipo VARCHAR(20) NOT NULL, "
                    + "piso INT NOT NULL, "
                    + "capacidad INT NOT NULL, "
                    + "precio_base DOUBLE NOT NULL, "
                    + "estado ENUM('DISPONIBLE', 'OCUPADA', 'LIMPIEZA', 'REPARACION', 'DESINFECCION', 'FUERA_DE_SERVICIO') NOT NULL, "
                    + "motivo VARCHAR(100) NULL, "
                    + "PRIMARY KEY (numero))");
            ps.execute();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al crear la tabla: " + e.getMessage());
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

    private static void crearHabitacionesDobles() throws PersistenciaException {
        PreparedStatement ps = null;
        try {
            Connection conn = Conexion.getConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS habitaciones_dobles ("
                    + "numero INT NOT NULL, "
                    + "camas_separadas BOOLEAN NOT NULL, "
                    + "PRIMARY KEY (numero), "
                    + "FOREIGN KEY (numero) REFERENCES habitaciones(numero))");
            ps.execute();

        } catch (SQLException e) {
            throw new PersistenciaException("Error al crear tabla de habitaciones dobles: " + e.getMessage());
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

    private static void crearHabitacionesSuite() throws PersistenciaException {
        PreparedStatement ps = null;
        try {
            Connection conn = Conexion.getConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS habitaciones_suites ("
                    + "numero INT NOT NULL, "
                    + "tiene_jacuzzi BOOLEAN NOT NULL, "
                    + "PRIMARY KEY (numero), "
                    + "FOREIGN KEY (numero) REFERENCES habitaciones(numero))");
            ps.execute();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al crear tabla de habitaciones suite: " + e.getMessage());
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
        crearHabitaciones();
        crearHabitacionesDobles();
        crearHabitacionesSuite();
    }
}
