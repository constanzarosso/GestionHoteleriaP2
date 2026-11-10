package gestoras;

<<<<<<< HEAD:tpfinal2/src/models/GestorOcupaciones.java
import Excepciones.ExcepcionesDatos.ElementoNoEncontradoException;
=======
import Excepciones.ElementoNoEncontradoException;
import models.Ocupacion;
>>>>>>> 8a738042df9978b8ff772ad1bf90466ae9491194:tpfinal2/src/gestoras/GestorOcupaciones.java

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class GestorOcupaciones extends GestorGenerico<Ocupacion> {

    public GestorOcupaciones() {
    }



    public Ocupacion ocupacionActiva(int numero)
            throws ElementoNoEncontradoException {

        for (Ocupacion ocupacion : obtenerElementos()) {
            if (ocupacion.getHabitacion().getId() == numero
                    && ocupacion.estaActiva()) {
                return ocupacion;
            }
        }

        throw new ElementoNoEncontradoException();
    }

    public List<Ocupacion> listarActivas() {
        List<Ocupacion> activas = new ArrayList<>();

        for (Ocupacion ocupacion : obtenerElementos()) {
            if (ocupacion.estaActiva()) {
                activas.add(ocupacion);
            }
        }

        return activas;
    }

    public List<Ocupacion> historial(int dni)
            throws ElementoNoEncontradoException {

        List<Ocupacion> historial = new ArrayList<>();

        for (Ocupacion ocupacion : obtenerElementos()) {
            if (ocupacion.getTitular().getId() == dni) {
                historial.add(ocupacion);
            }
        }

        if (historial.isEmpty()) {
            throw new ElementoNoEncontradoException();
        }

        return historial;
    }
}