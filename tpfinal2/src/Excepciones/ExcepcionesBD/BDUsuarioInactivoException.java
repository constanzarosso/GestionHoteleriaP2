package Excepciones.ExcepcionesBD;

public class BDUsuarioInactivoException extends Exception {
    public BDUsuarioInactivoException() {
        super("Error: Usuario inactivo");
    }
}
