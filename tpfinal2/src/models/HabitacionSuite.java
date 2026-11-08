package models;

import enums.EEstadoHabitacion;
import enums.ETipoHabitacion;

public final class HabitacionSuite extends Habitacion {
    private boolean tieneJacuzzi;

    public HabitacionSuite(int numero, int piso, int capacidad, double precioBase, boolean tieneJacuzzi) {
        super(numero, piso, capacidad, precioBase);
        this.tieneJacuzzi = tieneJacuzzi;
    }

    public HabitacionSuite(int numero, int piso, int capacidad, double precioBase, EEstadoHabitacion estadoHabitacion, String motivoNoDisponible, boolean tieneJacuzzi) {
        super(numero, piso, capacidad, precioBase, estadoHabitacion, motivoNoDisponible);
        this.tieneJacuzzi = tieneJacuzzi;
    }

    public boolean isTieneJacuzzi() {
        return tieneJacuzzi;
    }

    @Override
    public double calcularPrecioPorNoche() {
        double precio = getPrecioBase() * 2.5;
        if(tieneJacuzzi){
            precio = precio *  1.1;
        }
        return precio;
    }

    @Override
    public ETipoHabitacion getTipoHabitacion() {
        return ETipoHabitacion.SUITE;
    }

    @Override
    public String toString() {
        return "HabitacionSuite{" +
                super.toString() +
                ", tieneJacuzzi=" + tieneJacuzzi +
                '}';
    }
}
