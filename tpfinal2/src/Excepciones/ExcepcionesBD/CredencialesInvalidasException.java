package Excepciones.ExcepcionesBD;

public class CredencialesInvalidasException extends Exception {
    public CredencialesInvalidasException() {
        super("Error: Credenciales invalidas.");
    }
}
