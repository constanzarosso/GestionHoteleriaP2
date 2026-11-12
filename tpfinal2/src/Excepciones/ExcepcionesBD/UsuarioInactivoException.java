package Excepciones.ExcepcionesBD;

public class UsuarioInactivoException extends Exception {
    public UsuarioInactivoException() {
        super("Error: Usuario inactivo");
    }
}
