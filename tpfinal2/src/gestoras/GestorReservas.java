package gestoras;


<<<<<<< HEAD:tpfinal2/src/models/GestorReservas.java
import Excepciones.ExcepcionesDatos.ElementoNoEncontradoException;
import Excepciones.ExcepcionesHotel.PeriodoInvalidoException;
=======
import Excepciones.ElementoNoEncontradoException;
import Excepciones.PeriodoInvalidoException;
import models.Reserva;
>>>>>>> 8a738042df9978b8ff772ad1bf90466ae9491194:tpfinal2/src/gestoras/GestorReservas.java

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GestorReservas extends GestorGenerico<Reserva> {

    public GestorReservas() {
    }


    public List<Reserva> reservasDeHabitacion(int numero)
            throws ElementoNoEncontradoException {

        List<Reserva> reservas = new ArrayList<>();

        for (Reserva reserva : obtenerElementos()) {
            if (reserva.getHabitacion().getId() == numero) {
                reservas.add(reserva);
            }
        }

        if (reservas.isEmpty()) {
            throw new ElementoNoEncontradoException();
        }

        return reservas;
    }

    public List<Reserva> reservasDePasajero(int dni)
            throws ElementoNoEncontradoException {

        List<Reserva> reservas = new ArrayList<>();

        for (Reserva reserva : obtenerElementos()) {
            if (reserva.getPasajero().getId() == dni) {
                reservas.add(reserva);
            }
        }

        if (reservas.isEmpty()) {
            throw new ElementoNoEncontradoException();
        }

        return reservas;
    }

    public boolean hayReservaSuperpuesta(int numero,
                                         LocalDate desde,
                                         LocalDate hasta)
            throws PeriodoInvalidoException {

        if (desde == null || hasta == null || !desde.isBefore(hasta)) {
            throw new PeriodoInvalidoException();
        }

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
