package Excepciones.ExcepcionesDatos;

public class FueraDeRangoException extends DatoInvalidoException {
    public FueraDeRangoException(String mensaje) {
        super("Error: Fuera de Rango" + mensaje);
    }
}
