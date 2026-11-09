package models;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class GestorOcupaciones extends GestorGenerico<Ocupacion> {

    public GestorOcupaciones() {
        super();
    }

    public GestorOcupaciones(Map<Integer, Ocupacion> elementos) {
        super(elementos);
    }

    public Ocupacion ocupacionActivaDe(int numero) {
        for (Ocupacion ocupacion : obtenerElementos()) {
            if (ocupacion.getHabitacion().getId() == numero
                    && ocupacion.estaActiva()) {
                return ocupacion;
            }
        }

        return null;
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

    public List<Ocupacion> historialDe(int dni) {
        List<Ocupacion> historial = new ArrayList<>();

        for (Ocupacion ocupacion : obtenerElementos()) {
            if (ocupacion.getTitular().getId() == dni) {
                historial.add(ocupacion);
            }
        }

        return historial;
    }
}