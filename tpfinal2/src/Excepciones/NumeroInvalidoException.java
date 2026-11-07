package Excepciones;

public class NumeroInvalidoException extends DatoInvalidoException {
    public NumeroInvalidoException() {
        super("Error: Numero incorrecto");
    }
}
