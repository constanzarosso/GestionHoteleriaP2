package Excepciones;

public class CampoVacioException extends DatoInvalidoException {
    public CampoVacioException() {
        super("Error: Campo vacio");
    }
}
