package gestoras;

<<<<<<< HEAD:tpfinal2/src/models/GestorPasajeros.java
import Excepciones.ExcepcionesDatos.ElementoNoEncontradoException;
=======
import Excepciones.ElementoNoEncontradoException;
import models.Pasajero;
>>>>>>> 8a738042df9978b8ff772ad1bf90466ae9491194:tpfinal2/src/gestoras/GestorPasajeros.java

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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


