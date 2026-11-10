package Excepciones.ExcepcionesHotel;

import Excepciones.ExcepcionesDatos.TextoInvalidoException;

public class DniInvalidoException extends TextoInvalidoException {
    public DniInvalidoException(String mensaje) {
        super(mensaje);
    }
}
