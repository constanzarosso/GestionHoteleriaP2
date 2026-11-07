package Excepciones;

public class FueraDeRangoException extends DatoInvalidoException {
    public FueraDeRangoException() {
        super("Error: Fuera de Rango");
    }
}
