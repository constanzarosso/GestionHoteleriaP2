package Excepciones;

public class DatoInvalidoException extends Exception {
    public DatoInvalidoException(String message) {
        super("Error de dato invalido. ");
    }
}
