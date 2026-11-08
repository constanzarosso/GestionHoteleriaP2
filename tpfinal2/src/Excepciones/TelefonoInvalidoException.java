package Excepciones;

public class TelefonoInvalidoException extends TextoInvalidoException {
    public TelefonoInvalidoException(String mensaje) {
        super("Error: Telefono Invalido. " + mensaje);
    }
}
