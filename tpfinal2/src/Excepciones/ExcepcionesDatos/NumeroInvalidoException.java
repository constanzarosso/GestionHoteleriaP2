package Excepciones.ExcepcionesDatos;

public class NumeroInvalidoException extends DatoInvalidoException {
    public NumeroInvalidoException() {
        super("Error: El valor ingresado tiene que ser un numero entero");
    }
}
