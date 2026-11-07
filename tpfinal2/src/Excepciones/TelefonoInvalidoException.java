package Excepciones;

public class TelefonoInvalidoException extends TextoInvalidoException {
    public TelefonoInvalidoException() {
        super("Error: Telefono Invalido.");
    }
}
