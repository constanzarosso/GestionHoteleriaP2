package Excepciones.ExcepcionesBD;

public class CrearTablaException extends PersistenciaException {
    public CrearTablaException(String message) {

        super("No se pudo crear la tabla " +  message);
    }
}
