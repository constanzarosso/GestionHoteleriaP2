package Excepciones.ExcepcionesDB;

public class PersistenciaException extends Exception {
    public PersistenciaException() {
        super("Error: problema al intentar persistencia de datos.");
    }
    public PersistenciaException(String mensaje) {
        super("Error con la base de datos: " + mensaje);
    }
}
