package Excepciones;

public class ContraseniaInvalidaException extends TextoInvalidoException {
    public ContraseniaInvalidaException(String mensaje) {
        super("Error: Contraseña incorrecta. "+mensaje);
    }
}
