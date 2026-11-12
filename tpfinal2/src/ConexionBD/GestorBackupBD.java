package ConexionBD;



import Excepciones.ExcepcionesBD.BDPersistenciaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class GestorBackupBD {
    private GestorBackupBD() {
    }

    private static void crearBackups() throws BDPersistenciaException {
        PreparedStatement ps = null;
        try {
            Connection conn = Conexion.getConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS backups ("
                    + "id INT NOT NULL, "
                    + "fecha_hora DATETIME NOT NULL, "
                    + "nombre_usuario VARCHAR(20) NOT NULL, "
                    + "PRIMARY KEY (id))");

            ps.execute();
        } catch (SQLException e) {
            throw new BDPersistenciaException("Error al crear tabla de backups: " + e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new BDPersistenciaException("Error al cerrar: " + e.getMessage());
            }
        }
    }

    public static void createTable() throws BDPersistenciaException {
        crearBackups();
    }
}
