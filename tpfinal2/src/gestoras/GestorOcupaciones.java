package gestoras;

import Excepciones.ElementoNoEncontradoException;
import models.Ocupacion;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class GestorOcupaciones extends GestorGenerico<Ocupacion> {

    public GestorOcupaciones() {
    }

    public GestorOcupaciones(Map<Integer, Ocupacion> elementos) {
        super(elementos);
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