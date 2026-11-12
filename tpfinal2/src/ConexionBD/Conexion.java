package ConexionBD;

import Excepciones.ExcepcionesBD.ConexionException;
import Excepciones.ExcepcionesBD.PersistenciaException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLRecoverableException;
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
        } catch (FileNotFoundException e) {
            throw new ConexionException("no se encontro el archivo db.properties");
        } catch (IOException e) {
            throw new PersistenciaException("no se pudo leer db.properties: " + e.getMessage());
        } catch (SQLRecoverableException e) {
            throw new ConexionException("no se pudo conectar con MySQL, verifique que este encendido");
        } catch (SQLException e) {
            throw new PersistenciaException("error al conectar: " + e.getMessage());
        }
        return conn;
    }

    public static void closeConnection() throws PersistenciaException {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new ConexionException("Error al cerrar la conexion: " + e.getMessage());
        }
    }
}
