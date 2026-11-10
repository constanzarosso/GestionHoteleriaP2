package gestoras;

import Excepciones.ExcepcionesDatos.ElementoNoEncontradoException;
import models.Pasajero;


import java.util.ArrayList;
import java.util.List;

public class GestorPasajeros extends GestorGenerico<Pasajero> {

    public GestorPasajeros() {
    }



    public List<Pasajero> buscarPorApellido(String apellido)
            throws ElementoNoEncontradoException {

        List<Pasajero> encontrados = new ArrayList<>();

        for (Pasajero pasajero : obtenerElementos()) {
            if (pasajero.getApellido().equalsIgnoreCase(apellido)) {
                encontrados.add(pasajero);
            }
        }

        if (encontrados.isEmpty()) {
            throw new ElementoNoEncontradoException();
        }

        return encontrados;
    }
}


