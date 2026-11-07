package Excepciones;

public class MontoInvalidoException extends DatoInvalidoException {
    public MontoInvalidoException() {
        super("Error: El monto ingresado no es valido.");
    }
}
