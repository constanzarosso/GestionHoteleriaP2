package models;

import java.util.List;
import java.util.ArrayList;

/**
 * Gestiona las ocupaciones del hotel.
 */
public class GestorOcupaciones extends GestorGenerico<Ocupacion> {

    /**
     * Constructor.
     */
    public GestorOcupaciones(List<Ocupacion> iniciales) {
        super(iniciales);
    }

    /**
     * Busca la ocupación activa de una habitación.
     */
    public Ocupacion ocupacionActivaDe(int numero) {

        for (Ocupacion ocupacion : listar()) {

            if (ocupacion.getHabitacion().getId() == numero
                    && ocupacion.estaActiva()) {

                return ocupacion;
            }
        }

        return null;
    }

    /**
     * Obtiene todas las ocupaciones activas.
     */
    public List<Ocupacion> listarActivas() {

        List<Ocupacion> resultado = new ArrayList<>();

        for (Ocupacion ocupacion : listar()) {
            if (ocupacion.estaActiva()) {
                resultado.add(ocupacion);
            }
        }

        return resultado;
    }

    /**
     * Obtiene el historial de ocupaciones de un pasajero.
     */
    public List<Ocupacion> historialDe(int dni) {

        List<Ocupacion> resultado = new ArrayList<>();

        for (Ocupacion ocupacion : listar()) {

            if (ocupacion.getTitular().getDni() == dni) {
                resultado.add(ocupacion);
            }
        }

        return resultado;
    }
}