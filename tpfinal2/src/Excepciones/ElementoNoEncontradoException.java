package Excepciones;

public class ElementoNoEncontradoException extends Exception {
    public ElementoNoEncontradoException() {
        super("Error: Elemento no encontrado: ");
    }
}
