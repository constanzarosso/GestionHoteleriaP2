package Excepciones.ExcepcionesHotel;

public class HabitacionNoDisponibleException extends Exception {
    public HabitacionNoDisponibleException() {
        super("Error: Habitacion No Disponible");
    }
}
