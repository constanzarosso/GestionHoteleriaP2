package Excepciones.ExcepcionesBD;

public class ConexionException extends PersistenciaException {
    public ConexionException(String message) {

        super("No se pudo conectar a la base de datos " + message);
    }
}
