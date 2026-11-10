package Excepciones.ExcepcionesHotel;

public class ReservaInvalidaException extends Exception {
    public ReservaInvalidaException() {
        super("Error: Reserva Invalido. ");
    }
}
