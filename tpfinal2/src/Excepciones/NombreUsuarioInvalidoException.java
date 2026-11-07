package Excepciones;

public class NombreUsuarioInvalidoException extends TextoInvalidoException {
    public NombreUsuarioInvalidoException( ) {
        super("Error: Nombre de Usuario Invalido.");
    }
}
