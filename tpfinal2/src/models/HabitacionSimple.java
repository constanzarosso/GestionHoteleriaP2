package models;

import enums.EEstadoHabitacion;
import enums.ETipoHabitacion;

public final class HabitacionSimple extends Habitacion{

    public HabitacionSimple(int numero, int piso, int capacidad, double precioBase) {
        super(numero, piso, capacidad, precioBase);
    }

    public HabitacionSimple(int numero, int piso, int capacidad, double precioBase, EEstadoHabitacion estadoHabitacion, String motivoNoDisponible) {
        super(numero, piso, capacidad, precioBase, estadoHabitacion, motivoNoDisponible);
    }

    @Override
    public double calcularPrecioPorNoche() {
        return getPrecioBase();
    }

    @Override
    public ETipoHabitacion getTipoHabitacion() {
        return ETipoHabitacion.SIMPLE;
    }


    @Override
    public String toString() {
        return "HabitacionSimple{" +
                super.toString() +
                ", tipo=" + getTipoHabitacion() +
                '}';
    }
}
