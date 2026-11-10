package ConexionBD;

import Excepciones.PersistenciaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class GestorOcupacionesBD {
    private static void crearOcupaciones() throws PersistenciaException {
        PreparedStatement ps = null;
        try {
            Connection conn = Conexion.getConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS ocupaciones ("
                    + "id INT NOT NULL, "
                    + "id_reserva INT NOT NULL, "
                    + "fecha_check_in DATE NOT NULL, "
                    + "fecha_check_out DATE NULL, "
                    + "total DOUBLE NOT NULL DEFAULT 0, "
                    + "metodo_pago ENUM('EFECTIVO', 'DEBITO', 'CREDITO', 'TRANSFERENCIA') NULL, "
                    + "PRIMARY KEY (id), "
                    + "UNIQUE (id_reserva), "
                    + "FOREIGN KEY (id_reserva) REFERENCES reservas(id))");
            ps.execute();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al crear la tabla ocupaciones: " + e.getMessage());
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

    private static void crearConsumos() throws PersistenciaException {
        PreparedStatement ps = null;
        try {
            Connection conn = Conexion.getConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS consumos ("
                    + "id INT NOT NULL, "
                    + "id_ocupacion INT NOT NULL, "
                    + "descripcion VARCHAR(100) NOT NULL, "
                    + "monto DOUBLE NOT NULL, "
                    + "fecha DATE NOT NULL, "
                    + "PRIMARY KEY (id), "
                    + "FOREIGN KEY (id_ocupacion) REFERENCES ocupaciones(id))");
            ps.execute();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al crear la tabla consumos: " + e.getMessage());
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
        crearOcupaciones();
        crearConsumos();
    }
}
