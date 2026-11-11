package Excepciones.ExcepcionesBD;

public class BDCredencialesInvalidasException extends Exception {
    public BDCredencialesInvalidasException() {
        super("Error: Credenciales invalidas.");
    }
}
