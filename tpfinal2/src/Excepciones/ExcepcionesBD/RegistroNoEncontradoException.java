package Excepciones.ExcepcionesBD;

public class RegistroNoEncontradoException extends PersistenciaException {
    public RegistroNoEncontradoException(String message) {

        super("No existe el registro: " + message);
    }
}
