package Excepciones;

public class NombreUsuarioInvalidoException extends TextoInvalidoException {
    public NombreUsuarioInvalidoException(String mensaje) {
        super("Error: Nombre de Usuario Invalido. "+mensaje);
    }
}
