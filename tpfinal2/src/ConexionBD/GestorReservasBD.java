package ConexionBD;

import Excepciones.ExcepcionesDB.PersistenciaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class GestorReservasBD {
    private GestorReservasBD() {
    }

    private static void crearReservas() throws PersistenciaException {
        PreparedStatement ps = null;
        try {
            Connection conn = Conexion.getConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS reservas ("
                    + "id INT NOT NULL, "
                    + "dni_pasajero INT NOT NULL, "
                    + "numero_habitacion INT NOT NULL, "
                    + "fecha_desde DATE NOT NULL, "
                    + "fecha_hasta DATE NOT NULL, "
                    + "cantidad_personas INT NOT NULL, "
                    + "estado ENUM('PENDIENTE', 'EN_CURSO', 'FINALIZADA', 'CANCELADA') NOT NULL, "
                    + "PRIMARY KEY (id), "
                    + "FOREIGN KEY (dni_pasajero) REFERENCES pasajeros(dni), "
                    + "FOREIGN KEY (numero_habitacion) REFERENCES habitaciones(numero))");
            ps.execute();

        } catch (SQLException | PersistenciaException e) {
            throw new PersistenciaException("Error al crear la tabla reservas: " + e.getMessage());
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
        crearReservas();
    }
}
