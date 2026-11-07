package Excepciones;

public class DniInvalidoException extends TextoInvalidoException {
    public DniInvalidoException() {
        super("Error: DNI invalido.");
    }
}
