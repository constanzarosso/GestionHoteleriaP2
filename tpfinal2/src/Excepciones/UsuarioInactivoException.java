package Excepciones;

public class UsuarioInactivoException extends Exception {
    public UsuarioInactivoException() {
        super("Error: Usuario inactivo");
    }
}
