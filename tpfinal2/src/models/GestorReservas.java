package models;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GestorReservas extends GestorGenerico<Reserva> {

    public GestorReservas() {
        super();
    }

    public GestorReservas(Map<Integer, Reserva> elementos) {
        super(elementos);
    }

    public List<Reserva> reservasDeHabitacion(int numero) {
        List<Reserva> reservas = new ArrayList<>();

        for (Reserva reserva : obtenerElementos()) {
            if (reserva.getHabitacion().getId() == numero) {
                reservas.add(reserva);
            }
        }

        return reservas;
    }

    public List<Reserva> reservasDePasajero(int dni) {
        List<Reserva> reservas = new ArrayList<>();

        for (Reserva reserva : obtenerElementos()) {
            if (reserva.getPasajero().getId() == dni) {
                reservas.add(reserva);
            }
        }

        return reservas;
    }

    public boolean hayReservaSuperpuesta(int numero, LocalDate desde, LocalDate hasta) {
        for (Reserva reserva : obtenerElementos()) {
            if (reserva.getHabitacion().getId() == numero
                    && reserva.estaActiva()
                    && reserva.seSuperpone(desde, hasta)) {
                return true;
            }
        }

        return false;
    }
}

