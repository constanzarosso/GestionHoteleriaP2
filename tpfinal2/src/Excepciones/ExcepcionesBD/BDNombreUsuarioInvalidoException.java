package Excepciones.ExcepcionesBD;

import Excepciones.ExcepcionesDatos.TextoInvalidoException;

public class BDNombreUsuarioInvalidoException extends TextoInvalidoException {
    public BDNombreUsuarioInvalidoException(String mensaje) {
        super("Error: Nombre de Usuario Invalido. "+mensaje);
    }
}
