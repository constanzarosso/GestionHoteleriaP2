package Excepciones.ExcepcionesHotel;

import Excepciones.ExcepcionesDatos.TextoInvalidoException;

public class FechaInvalidaException extends TextoInvalidoException {
    public FechaInvalidaException(String mensaje) {
        super("Error: Fecha Invalida. " + mensaje);
    }
}
