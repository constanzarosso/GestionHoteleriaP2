package models;

import Excepciones.CapacidadExcedidaException;
import enums.EEstadoHabitacion;
import enums.ETipoHabitacion;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GestorHabitaciones extends GestorGenerico<Habitacion> {

    public GestorHabitaciones() {
    }

    public GestorHabitaciones(Map<Integer, Habitacion> elementos) {
        super(elementos);
    }

    public List<Habitacion> listarDisponibles() {
        List<Habitacion> disponibles = new ArrayList<>();

        for (Habitacion h : obtenerElementos()) {
            if (h.estaDisponible()) {
                disponibles.add(h);
            }
        }

        return disponibles;
    }

    public List<Habitacion> listarOcupadas() {
        List<Habitacion> ocupadas = new ArrayList<>();

        for (Habitacion h : obtenerElementos()) {
            if (h.getEstadoHabitacion() == EEstadoHabitacion.OCUPADA) {
                ocupadas.add(h);
            }
        }

        return ocupadas;
    }

    public List<Habitacion> listarNoDisponibles() {
        List<Habitacion> noDisponibles = new ArrayList<>();

        for (Habitacion h : obtenerElementos()) {
            if (!h.estaDisponible() &&
                    h.getEstadoHabitacion() != EEstadoHabitacion.OCUPADA) {
                noDisponibles.add(h);
            }
        }

        return noDisponibles;
    }

    public List<Habitacion> listarPorEstado(EEstadoHabitacion estado) {
        List<Habitacion> habitaciones = new ArrayList<>();

        for (Habitacion h : obtenerElementos()) {
            if (h.getEstadoHabitacion() == estado) {
                habitaciones.add(h);
            }
        }

        return habitaciones;
    }

    public List<Habitacion> listarPorTipo(ETipoHabitacion tipo) {
        List<Habitacion> habitaciones = new ArrayList<>();

        for (Habitacion h : obtenerElementos()) {
            if (h.getTipoHabitacion() == tipo) {
                habitaciones.add(h);
            }
        }

        return habitaciones;
    }

    public List<Habitacion> listarDisponiblesParaHuesped(int cantidadDePersonas)
            throws CapacidadExcedidaException {

        if (cantidadDePersonas <= 0) {
            throw new CapacidadExcedidaException();
        }

        List<Habitacion> disponibles = new ArrayList<>();

        for (Habitacion h : obtenerElementos()) {
            if (h.estaDisponible() &&
                    h.getCapacidad() >= cantidadDePersonas) {
                disponibles.add(h);
            }
        }

        return disponibles;
    }
}


