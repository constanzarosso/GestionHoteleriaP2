package Excepciones.ExcepcionesBD;

public class BDConexionException extends BDPersistenciaException{
    public BDConexionException(String message) {

        super("No se pudo conectar a la base de datos " + message);
    }
}
