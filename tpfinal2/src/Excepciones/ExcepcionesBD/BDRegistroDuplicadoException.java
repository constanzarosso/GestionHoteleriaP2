package Excepciones.ExcepcionesBD;

public class BDRegistroDuplicadoException extends PersistenciaException {
    public BDRegistroDuplicadoException(String message) {

        super("Ya existe un registro con esos datos: " +  message);
    }
}
