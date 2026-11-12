package Excepciones.ExcepcionesBD;

import Excepciones.ExcepcionesDatos.TextoInvalidoException;

public class ContraseniaInvalidaException extends TextoInvalidoException {
    public ContraseniaInvalidaException(String mensaje) {
        super("Error: Contraseña incorrecta. "+mensaje);
    }
}
