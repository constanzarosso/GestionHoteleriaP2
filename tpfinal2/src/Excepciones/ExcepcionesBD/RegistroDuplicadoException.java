package Excepciones.ExcepcionesBD;

public class RegistroDuplicadoException extends PersistenciaException {
    public RegistroDuplicadoException(String message) {

        super("Ya existe un registro con esos datos: " +  message);
    }
}
