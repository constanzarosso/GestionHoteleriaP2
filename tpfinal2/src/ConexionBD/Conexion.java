package ConexionBD;

import Excepciones.ExcepcionesDB.PersistenciaException;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Conexion {

    private Conexion() {
    }

    private static Connection conn;

    public static Connection getConnection() throws PersistenciaException {
        try {
            if (conn == null || conn.isClosed()) {
                FileInputStream archi = new FileInputStream("./tpfinal2/db.properties");
                Properties datos = new Properties();
                datos.load(archi);
                archi.close();

                conn = DriverManager.getConnection(
                        datos.getProperty("url"),
                        datos.getProperty("usuario"),
                        datos.getProperty("contrasenia")
                );
            }
        } catch (IOException e) {
            throw new PersistenciaException("No se encontro el archivo db.properties o no pudo abrirse: " + e.getMessage());
        } catch (SQLException e) {
            throw new PersistenciaException("Error al conectar: " + e.getMessage());
        }
        return conn;
    }

    public static void closeConnection() throws PersistenciaException {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al cerrar la conexion: " + e.getMessage());
        }
    }
}
