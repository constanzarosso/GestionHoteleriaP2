package Excepciones;

public class EmailInvalidoException extends TextoInvalidoException {
    public EmailInvalidoException() {
        super("Error: Email Invalido.");
    }
}
