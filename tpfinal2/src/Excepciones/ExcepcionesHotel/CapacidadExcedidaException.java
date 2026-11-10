package Excepciones.ExcepcionesHotel;

public class CapacidadExcedidaException extends Exception {
    public CapacidadExcedidaException() {
        super("Error: Capacidad Excedida.");
    }
}
