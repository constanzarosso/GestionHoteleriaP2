package Excepciones.ExcepcionesDB;

import Excepciones.ExcepcionesDatos.TextoInvalidoException;

public class NombreUsuarioInvalidoException extends TextoInvalidoException {
    public NombreUsuarioInvalidoException(String mensaje) {
        super("Error: Nombre de Usuario Invalido. "+mensaje);
    }
}
