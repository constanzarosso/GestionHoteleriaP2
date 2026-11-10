package Excepciones.ExcepcionesBD;

import Excepciones.ExcepcionesDatos.TextoInvalidoException;

public class BDEmailInvalidoException extends TextoInvalidoException {
    public BDEmailInvalidoException(String mensaje) {
        super("Error: Email Invalido. " + mensaje);
    }
}
