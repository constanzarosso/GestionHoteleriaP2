package Excepciones.ExcepcionesBD;

public class BDRegistroNoEncontradoException extends BDPersistenciaException{
    public BDRegistroNoEncontradoException(String message) {

        super("No existe el registro: " + message);
    }
}
