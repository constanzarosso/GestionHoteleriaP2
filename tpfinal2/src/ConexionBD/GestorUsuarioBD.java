package ConexionBD;

import Excepciones.PersistenciaException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public final class GestorUsuarioBD {

    private static void crearUsuarios() throws PersistenciaException {
        PreparedStatement ps = null;
        try {
            Connection conn = Conexion.getConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS usuarios ("
                    + "id INT NOT NULL, "
                    + "nombre_usuario VARCHAR(20) NOT NULL, "
                    + "contrasenia VARCHAR(30) NOT NULL, "
                    + "tipo ENUM('ADMINISTRADOR', 'RECEPCIONISTA', 'PASAJERO') NOT NULL, "
                    + "activo BOOLEAN NOT NULL, "
                    + "PRIMARY KEY (id), "
                    + "UNIQUE (nombre_usuario))");
            ps.execute();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al crear la tabla usuarios: " + e.getMessage());
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

    private static void crearUsuariosPermisos() throws PersistenciaException {
        PreparedStatement ps = null;
        try {
            Connection conn = Conexion.getConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS usuario_permisos ("
                    + "id_usuario INT NOT NULL, "
                    + "permiso ENUM('GESTIONAR_USUARIOS', 'ASIGNAR_PERMISOS', 'HACER_BACKUP', "
                    + "'GESTIONAR_HABITACIONES', 'CAMBIAR_ESTADO_HABITACION', 'VER_HABITACIONES', "
                    + "'CARGAR_PASAJEROS', 'GESTIONAR_RESERVAS', 'HACER_CHECKIN_CHECKOUT', "
                    + "'VER_INFORMES', 'SOLICITAR_RESERVAS', 'REGISTRAR_CONSUMO') NOT NULL, "
                    + "PRIMARY KEY (id_usuario, permiso), "
                    + "FOREIGN KEY (id_usuario) REFERENCES usuarios(id))"
            );
            ps.execute();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al crear la tabla usuarios_permisos: " + e.getMessage());
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

    public static void createTable() throws PersistenciaException{
        crearUsuarios();
        crearUsuariosPermisos();
    }
}
