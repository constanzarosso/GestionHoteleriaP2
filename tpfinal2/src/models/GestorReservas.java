package models;


import java.util.ArrayList;
import java.util.List;

    /**
     * Gestiona las reservas del hotel.
     */
    public class GestorReservas extends GestorGenerico<Reserva> {

        /**
         * Constructor.
         */
        public GestorReservas(List<Reserva> iniciales) {
            super(iniciales);
        }

        /**
         * Obtiene las reservas de una habitación.
         */
        public List<Reserva> reservasDeHabitacion(int numero) {

            List<Reserva> resultado = new ArrayList<>();

            for (Reserva reserva : listar()) {
                if (reserva.getHabitacion().getId() == numero) {
                    resultado.add(reserva);
                }
            }

            return resultado;
        }

        /**
         * Obtiene las reservas de un pasajero.
         */
        public List<Reserva> reservasDePasajero(int dni) {

            List<Reserva> resultado = new ArrayList<>();

            for (Reserva reserva : listar()) {
                if (reserva.getPasajero().getDni() == dni) {
                    resultado.add(reserva);
                }
            }

            return resultado;
        }

        /**
         * Verifica si existe una reserva superpuesta.
         */
        public boolean hayReservaSuperpuesta(int numero,
                                             LocalDate desde,
                                             LocalDate hasta) {

            for (Reserva reserva : listar()) {

                if (reserva.getHabitacion().getId() == numero
                        && reserva.estaActiva()
                        && reserva.seSuperpone(desde, hasta)) {

                    return true;
                }
            }

            return false;
        }
    }

