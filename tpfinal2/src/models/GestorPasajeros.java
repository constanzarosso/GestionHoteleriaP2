package models;

import java.util.ArrayList;
import java.util.List;

public class GestorPasajeros GestorGenerico<Pasajero> {


        public GestorPasajeros(List<Pasajero> iniciales) {
            super(iniciales);
        }

        /**
         * Busca pasajeros por apellido.
         */
        public List<Pasajero> buscarPorApellido(String apellido) {

            List<Pasajero> resultado = new ArrayList<>();

            for (Pasajero pasajero : listar()) {
                if (pasajero.getApellido().equalsIgnoreCase(apellido)) {
                    resultado.add(pasajero);
                }
            }

            return resultado;
        }
    }


