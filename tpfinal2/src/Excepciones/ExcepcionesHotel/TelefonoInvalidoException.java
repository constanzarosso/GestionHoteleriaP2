package Excepciones.ExcepcionesHotel;

import Excepciones.ExcepcionesDatos.TextoInvalidoException;

public class TelefonoInvalidoException extends TextoInvalidoException {
    public TelefonoInvalidoException(String mensaje) {
        super("Error: Telefono Invalido. " + mensaje);
    }
}
