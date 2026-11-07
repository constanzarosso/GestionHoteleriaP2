package Excepciones;

public class PersistenciaException extends Exception {
    public PersistenciaException() {
        super("Error: problema al intentar persistencia de datos.");
    }
}
