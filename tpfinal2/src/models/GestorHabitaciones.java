package models;

import enums.EEstadoHabitacion;
import enums.ETipoHabitacion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GestorHabitaciones extends GestorGenerico<Habitacion>{
    public GestorHabitaciones() {
    }

    public GestorHabitaciones(Map<Integer, Habitacion> elementos) {
        super(elementos);
    }

    public List<Habitacion> listarDisponibles(){
        List<Habitacion> disponibles = new ArrayList<>();
        for (Habitacion h : obtenerElementos()){
            if(h.estaDisponible()){
                disponibles.add(h);
            }
        }
        return disponibles;
    }

    public List<Habitacion> listarOcupadas (){
        List<Habitacion> ocupadas = new ArrayList<>();
        for(Habitacion h : obtenerElementos()){
            if(h.getEstadoHabitacion() == EEstadoHabitacion.OCUPADA){
                ocupadas.add(h);
            }
        }
        return ocupadas;
    }

    public List<Habitacion> listarNoDisponibles(){
        List<Habitacion> noDisponibles = new ArrayList<>();
        for(Habitacion h : obtenerElementos()){
            if(!h.estaDisponible() && h.getEstadoHabitacion() != EEstadoHabitacion.OCUPADA){
                noDisponibles.add(h);
            }
        }
        return noDisponibles;
    }

    public List<Habitacion> listarPorEstado(EEstadoHabitacion estado){
        List<Habitacion> porEstado = new ArrayList<>();
        for(Habitacion h : obtenerElementos()){
            if(h.getEstadoHabitacion() == estado){
                porEstado.add(h);
            }
        }
        return porEstado;
    }

    public List<Habitacion> listarPorTipo (ETipoHabitacion tipo){
        List<Habitacion> porTipo = new ArrayList<>();
        for(Habitacion h : obtenerElementos()){
            if(h.getTipoHabitacion() == tipo){
                porTipo.add(h);
            }
        }
        return porTipo;
    }

    public List<Habitacion> listarDisponiblesParaHuesped(int cantidadDePersonas){
        List<Habitacion> disponiblesSegunCapacidad =new ArrayList<>();
        for(Habitacion h : obtenerElementos()){
            if(h.estaDisponible() && h.getCapacidad() >= cantidadDePersonas){
                disponiblesSegunCapacidad.add(h);
            }
        }
        return disponiblesSegunCapacidad;
    }
    
}
