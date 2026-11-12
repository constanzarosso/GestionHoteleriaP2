package Excepciones.ExcepcionesBD;

import Excepciones.ExcepcionesDatos.TextoInvalidoException;

public class EmailInvalidoException extends TextoInvalidoException {
    public EmailInvalidoException(String mensaje) {
        super("Error: Email Invalido. " + mensaje);
    }
}
