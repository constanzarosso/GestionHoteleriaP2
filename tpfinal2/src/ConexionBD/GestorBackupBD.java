package ConexionBD;



import Excepciones.ExcepcionesBD.PersistenciaException;
import com.mysql.cj.x.protobuf.MysqlxPrepare;
import models.Backup;

import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public final class GestorBackupBD {
    private static final String MYSQL_DUMP = "C:\\Program Files\\MySQL\\MySQL Server 26.7\\bin\\mysqldump.exe";
    private static final String MYSQL = "C:\\Program Files\\MySQL\\MySQL Server 26.7\\bin\\mysql.exe";
    private static final String CARPETA_BACKUPS = "backups";

    private GestorBackupBD() {
    }

    //Crea la tabla en la BD
    private static void crearBackups() throws PersistenciaException {

        String sql = "CREATE TABLE IF NOT EXISTS backups (" +
                "id INT NOT NULL, " +
                "fecha_hora DATETIME NOT NULL, " +
                "nombre_usuario VARCHAR(20) NOT NULL, " +
                "nombre_archivo VARCHAR(255) NOT NULL," +
                "PRIMARY KEY (id))";
        try {
            Connection conn = Conexion.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.execute();
            } catch (SQLException ex) {
                throw new PersistenciaException("Error al crear la tabla backup: " + ex.getMessage());
            }

        } catch (PersistenciaException ex) {
            throw new PersistenciaException(ex.getMessage());
        }
    }

    public static void crearBackup(String nombreUsuario) throws PersistenciaException {


           Path archivoTemporal = null;
           File archivoBackup = null;

           try{

            File carpeta = new File(CARPETA_BACKUPS);

            if(!carpeta.exists()) {
                carpeta.mkdirs();
            }

            Properties datos = new Properties();


            try (FileInputStream archivo = new FileInputStream("./tpfinal2/db.properties")) {
                datos.load(archivo);
            }

            String usuario = datos.getProperty("usuario");
            String contra = datos.getProperty("contrasenia");

            //Nombre del archivo
            int id = siguienteIdBackup();

            String nombreArchivo = "backup_"+id+".sql";
            archivoBackup = new File(carpeta, nombreArchivo);

            //Crea archivos temporales de configuracion
            archivoTemporal = Files.createTempFile("mysql-backup-", ".cnf");


            String contenido = "[client]\n"
                    + "user=" + usuario + "\n"
                    + "password=" + contra + "\n"
                    + "host=localhost\n"
                    + "port=3306\n";

            Files.writeString(archivoTemporal, contenido);

                //Ejecuta mysql dump
                ProcessBuilder pb = new ProcessBuilder(
                        MYSQL_DUMP,
                        "--defaults-extra-file="+archivoTemporal.toAbsolutePath(),
                        "--set-gtid-purged=OFF",
                        "--result-file=" + archivoBackup.getAbsolutePath(),
                        "hotel"
                );

                pb.redirectErrorStream(true);
                Process proceso = pb.start();
                String resultado = new String(proceso.getInputStream().readAllBytes());

                int codigo = proceso.waitFor();

                if(codigo!=0){
                    throw new IOException("Error mysql dump: "+resultado);
                }
                //Registra el backup en la BD
                registrarBackup(id,nombreUsuario,nombreArchivo);
                System.out.println("Backup creado: "+nombreArchivo);


            }catch (Exception e){
               if(archivoTemporal != null){
                   archivoBackup.delete();
               }
                throw new PersistenciaException("Error al crear Backup: "+e.getMessage());
            }finally {
                if(archivoTemporal != null){
                    try {
                        Files.deleteIfExists(archivoTemporal);
                    }catch (IOException e){
                        System.out.println("No se pudo eleminar el archivo temporal. ");
                    }
                }
            }
    }

    private static int siguienteIdBackup() throws PersistenciaException {
        String sql ="SELECT COALESCE(MAX(id), 0) + 1 AS siguiente FROM backups";
        try {
            Connection conn = Conexion.getConnection();
            try(PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

                if(rs.next()){
                    return rs.getInt("siguiente");
                }
            }
        }catch (SQLException e){
            throw new PersistenciaException(e.getMessage());
        }
        return 1;
    }

    private static void registrarBackup(int id, String nombreUsuario, String nombreArchivo) throws PersistenciaException {
        String sql = "INSERT INTO backups"
                +"(id, fecha_hora, nombre_usuario, nombre_archivo) "
                +"VALUES (?, ?, ?, ?)";

        try {

            Connection conn = Conexion.getConnection();

            try(PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setInt(1,id);
                ps.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(3, nombreUsuario);
                ps.setString(4, nombreArchivo);

                ps.executeUpdate();
            }
        }catch (SQLException e){
            throw new PersistenciaException("Error al registrar Backup: "+e.getMessage());
        }
    }

    public static void createTable() throws PersistenciaException {
        crearBackups();
    }


    public static List<Backup> listarBackups() throws PersistenciaException {
        List<Backup> backups = new ArrayList<>();

        String sql = "SELECT * FROM backups ORDER BY fecha_hora DESC";

        try {
            Connection conn = Conexion.getConnection();

            try(PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()){

                while(rs.next()){
                    Backup backup = new Backup(
                            rs.getInt("id"),
                            rs.getTimestamp("fecha_hora").toLocalDateTime(),
                            rs.getString("nombre_usuario"),
                            rs.getString("nombre_archivo")
                    );

                    backups.add(backup);
                }
            }

        }catch (SQLException e){
            throw new PersistenciaException("Error al listar el backup: "+e.getMessage());
        }

        return backups;
    }

    public static void restaurarBackup(int id) throws PersistenciaException {
        Path archivoTemporal = null;

        try {
            String sql = "SELECT nombre_archivo FROM backups WHERE id = ?";

            String nombreArchivo;

            Connection conn = Conexion.getConnection();

            try(PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setInt(1,id);

                try(ResultSet rs = ps.executeQuery()) {
                    if(!rs.next()){
                        throw new PersistenciaException("No existe backup con esa id. ");
                    }
                    nombreArchivo = rs.getString("nombre_archivo");
                }
            }

            File carpeta = new File(CARPETA_BACKUPS).getCanonicalFile();
            File archivoBackup = new File(carpeta, nombreArchivo);

            if(!archivoBackup.getParentFile().equals(carpeta)){
                throw new PersistenciaException("Ruta de backup invalida.");
            }

            if(!archivoBackup.exists()){
                throw new PersistenciaException("No existe backup con esa id. ");
            }

            Properties datos = new Properties();

            try(FileInputStream archivo = new FileInputStream("./tpfinal2/db.properties")){
                datos.load(archivo);
            }

            String usuario = datos.getProperty("usuario");
            String contra = datos.getProperty("contrasenia");

            archivoTemporal = Files.createTempFile("mysql-restore-",".cnf");

            String contenido = "[client]\n"
                    +"user="+usuario+"\n"
                    +"password="+contra+"\n"
                    +"host=localhost\n"
                    +"port=3306\n";

            Files.writeString(archivoTemporal,contenido);

            ProcessBuilder pb = new ProcessBuilder(
                    MYSQL,
                    "--defaults-extra-file="+archivoTemporal.toAbsolutePath(),
                    "hotel"
            );

            pb.redirectInput(archivoBackup);
            pb.redirectErrorStream(true);

            Process proceso = pb.start();

            String resultado = new String(proceso.getInputStream().readAllBytes());

            int codigo = proceso.waitFor();

            if(codigo!=0){
                throw new IOException("MySQL devolvió el código " + codigo +
                        ". Detalle: " + resultado);
            }
            System.out.println("Backup restaurado con exito.");
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
            throw new PersistenciaException("La restauracion fue interrupida");
        }catch(Exception e){
            throw new PersistenciaException("Error al restaurar backup: "+e.getMessage());
        }finally {
            if(archivoTemporal != null){
                try {
                    Files.deleteIfExists(archivoTemporal);
                }catch (IOException e){
                    throw new PersistenciaException("Error al eliminar el archivo: "+e.getMessage());
                }
            }
        }
    }
}
