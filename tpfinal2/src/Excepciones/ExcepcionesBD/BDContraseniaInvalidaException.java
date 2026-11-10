package Excepciones.ExcepcionesBD;

import Excepciones.ExcepcionesDatos.TextoInvalidoException;

public class BDContraseniaInvalidaException extends TextoInvalidoException {
    public BDContraseniaInvalidaException(String mensaje) {
        super("Error: Contraseña incorrecta. "+mensaje);
    }
}
