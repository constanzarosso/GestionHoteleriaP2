package Excepciones.ExcepcionesBD;

public class ConsultaException extends PersistenciaException {
    public ConsultaException(String message) {

        super("No se pudieron obtener los datos de " +  message);
    }
}
