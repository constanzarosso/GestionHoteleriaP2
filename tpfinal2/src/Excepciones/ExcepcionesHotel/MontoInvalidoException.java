package Excepciones.ExcepcionesHotel;

import Excepciones.ExcepcionesDatos.DatoInvalidoException;

public class MontoInvalidoException extends DatoInvalidoException {
    public MontoInvalidoException(String mensaje) {
        super("Error: El monto ingresado no es valido. "+mensaje);
    }
}
