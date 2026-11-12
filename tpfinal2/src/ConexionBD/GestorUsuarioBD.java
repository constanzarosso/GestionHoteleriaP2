package ConexionBD;
import Excepciones.ExcepcionesBD.BDPersistenciaException;
import enums.Permiso;
import models.Administrador;
import models.Recepcionista;
import models.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class GestorUsuarioBD {
    public GestorUsuarioBD() {
    }

    private static void crearUsuarios() throws PersistenciaException {
        PreparedStatement ps = null;
        try {
            Connection conn = Conexion.getConnection();
            ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS usuarios ("
                    + "id INT NOT NULL, "
                    + "nombre_usuario VARCHAR(40) NOT NULL, "
                    + "contrasenia VARCHAR(255) NOT NULL, "
                    + "tipo ENUM('ADMINISTRADOR', 'RECEPCIONISTA', 'PASAJERO') NOT NULL, "
                    + "activo BOOLEAN NOT NULL, "
                    + "PRIMARY KEY (id), "
                    + "UNIQUE (nombre_usuario))");
            ps.execute();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al crear la tabla usuarios: " + e.getMessage());
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

    public static void createTable() throws PersistenciaException {
        crearUsuarios();
        crearUsuariosPermisos();
    }

    public void guardar(Usuario usuario) throws BDPersistenciaException, BDPersistenciaException { // Uso de transacciones para asegurar las operaciones

        String sql = "INSERT INTO usuarios "
                + "(id, nombre_usuario, contrasenia, tipo, activo) "
                + "VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;

        try{
                conn = Conexion.getConnection();
                conn.setAutoCommit(false); //Desactiva las confirmaciones automaticas

                try(PreparedStatement ps = conn.prepareStatement(sql)) {

                    ps.setInt(1, usuario.getId());
                    ps.setString(2, usuario.getNombreUsuario());
                    ps.setString(3, usuario.getContrasenia());
                    ps.setString(4, usuario.getTipoUsuario().name());
                    ps.setBoolean(5, usuario.isActivo());

                    ps.executeUpdate();
                }

            guardarPermisos(conn, usuario);
            conn.commit(); //confirma los cambios

        }catch (SQLException | BDPersistenciaException e){
            if(conn != null){
                try {
                    conn.rollback(); // deshace los cambios de la transaccion
                }catch (SQLException ex){
                    e.addSuppressed(ex); // junta los errores para que no se pierdan
                }
            }
            throw new BDPersistenciaException("Error al guardar la tabla usuarios: " + e.getMessage());
        } finally {
            if(conn != null){
                try {
                    conn.setAutoCommit(true);//vuelve a activar las confirmaciones
                    Conexion.closeConnection();

                }catch (SQLException ex){
                    System.out.println("Error al cerrar la Conexion: " + ex.getMessage());
                }
            }
        }

    }

    private void guardarPermisos(Connection conn , Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario_permisos (id_usuario, permiso) "
                + "VALUES (?, ?)";

            try(PreparedStatement ps = conn.prepareStatement(sql)) {

                for (Permiso permiso : usuario.getPermisos()) {
                    ps.setInt(1, usuario.getId());
                    ps.setString(2, permiso.name());

                    ps.executeUpdate();
                }
            }
    }

    public List<Usuario> listar() throws BDPersistenciaException {
        List<Usuario> usuarios = new ArrayList<>();

        String sql = "SELECT * FROM usuarios";

        try{
            Connection conn = Conexion.getConnection();

            try (PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

                while (rs.next()){
                    int id = rs.getInt("id");
                    String nombreUsuario = rs.getString("nombre_usuario");
                    String contrasenia = rs.getString("contrasenia");
                    String tipo = rs.getString("tipo");
                    boolean activo = rs.getBoolean("activo");

                    Usuario usuario;

                    switch (tipo) {
                        case "ADMINISTRADOR":
                            usuario = new Administrador(id,nombreUsuario,contrasenia);
                            break;

                        case "RECEPCIONISTA":
                            usuario = new Recepcionista(id, nombreUsuario,contrasenia);
                            break;

                            default:
                                throw new BDPersistenciaException("Tipo de usuario incorrecto "+ tipo);
                    }

                    usuario.setActivo(activo);
                    usuarios.add(usuario);

                }

            }

            for (Usuario usuario : usuarios){
                for(Permiso permiso : usuario.getPermisos()){
                    usuario.quitarPermiso(permiso);
                }

                cargarPermisos(conn,usuario);
            }

            return usuarios;

        }catch (SQLException e){
            throw new BDPersistenciaException("Error al cargar la tabla usuarios: " + e.getMessage());
        }
    }

    private void cargarPermisos(Connection conn, Usuario usuario) throws SQLException {
        String sql = "SELECT * FROM usuario_permisos WHERE id_usuario = ?";

        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setInt(1, usuario.getId());

            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){

                    String nombrePermiso = rs.getString("permiso");
                    Permiso permiso = Permiso.valueOf(nombrePermiso);
                    usuario.agregarPermiso(permiso);
                }
            }
        }
    }

    public void actualizar(Usuario usuario) throws BDPersistenciaException {
        String sql = "UPDATE usuarios SET "
                + "nombre_usuario = ?, "
                + "contrasenia = ?, "
                + "tipo = ?, "
                + "activo = ? "
                + "WHERE id = ?";

        Connection conn = null;

        try {
            conn = Conexion.getConnection();
            conn.setAutoCommit(false);

            try(PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setString(1, usuario.getNombreUsuario());
                ps.setString(2, usuario.getContrasenia());
                ps.setString(3, usuario.getTipoUsuario().name());
                ps.setBoolean(4, usuario.isActivo());
                ps.setInt(5, usuario.getId());

                int filas = ps.executeUpdate();

                if(filas==0){
                    throw new SQLException("No se encontro al usuario");
                }
            }

            String sqlEliminar = "DELETE FROM usuario_permisos WHERE id_usuario = ?";

            try(PreparedStatement ps = conn.prepareStatement(sqlEliminar)){
                ps.setInt(1,usuario.getId());
                ps.executeUpdate();
            }

            guardarPermisos(conn,usuario);
            conn.commit();
        }catch (SQLException | BDPersistenciaException e){
            if(conn != null){
                try {
                    conn.rollback();
                }catch (SQLException ex){
                    e.addSuppressed(ex);
                }
            }
            throw new BDPersistenciaException("Error al cargar la tabla usuarios: " + e.getMessage());
        }finally {
            if(conn != null){
                try{
                    conn.setAutoCommit(true);
                }catch (SQLException ex){
                    System.out.println("Error al cerrar la Conexion: " + ex.getMessage());
                }
            }
        }
    }


}
