package Excepciones;

public class EmailInvalidoException extends TextoInvalidoException {
    public EmailInvalidoException(String mensaje) {
        super("Error: Email Invalido. " + mensaje);
    }
}
