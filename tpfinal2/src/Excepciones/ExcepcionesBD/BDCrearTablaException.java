package Excepciones.ExcepcionesBD;

public class BDCrearTablaException extends BDPersistenciaException{
    public BDCrearTablaException(String message) {

        super("No se pudo crear la tabla " +  message);
    }
}
