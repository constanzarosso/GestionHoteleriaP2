package Excepciones;

public class ContraseniaInvalidaException extends TextoInvalidoException {
    public ContraseniaInvalidaException() {
        super("Error: Contraseña incorrecta.");
    }
}
