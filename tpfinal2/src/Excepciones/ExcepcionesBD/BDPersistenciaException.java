package Excepciones.ExcepcionesBD;

public class BDPersistenciaException extends Exception {
    public BDPersistenciaException() {
        super("Error: problema al intentar persistencia de datos.");
    }
    public BDPersistenciaException(String mensaje) {
        super("Error con la base de datos: " + mensaje);
    }
}
