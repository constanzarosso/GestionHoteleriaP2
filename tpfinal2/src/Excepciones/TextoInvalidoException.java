package Excepciones;

public class TextoInvalidoException extends DatoInvalidoException {
    public TextoInvalidoException(String message) {
        super("Error tipo Texto Invalido. "+message);
    }
}
