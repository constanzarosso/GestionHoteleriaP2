package Excepciones.ExcepcionesDatos;

public class ElementoNoEncontradoException extends Exception {
    public ElementoNoEncontradoException() {
        super("Error: Elemento no encontrado: ");
    }
}
