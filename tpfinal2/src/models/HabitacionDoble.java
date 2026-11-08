package models;

import enums.EEstadoHabitacion;
import enums.ETipoHabitacion;

public final class HabitacionDoble extends Habitacion{
    private boolean camasSeparadas;

    public HabitacionDoble(int numero, int piso, int capacidad, double precioBase, boolean camasSeparadas) {
        super(numero, piso, capacidad, precioBase);
        this.camasSeparadas = camasSeparadas;
    }

    public HabitacionDoble(int numero, int piso, int capacidad, double precioBase, EEstadoHabitacion estadoHabitacion, String motivoNoDisponible, boolean camasSeparadas) {
        super(numero, piso, capacidad, precioBase, estadoHabitacion, motivoNoDisponible);
        this.camasSeparadas = camasSeparadas;
    }

    public boolean isCamasSeparadas() {
        return camasSeparadas;
    }

    @Override
    public double calcularPrecioPorNoche() {
        return getPrecioBase() * 1.5;
    }

    @Override
    public ETipoHabitacion getTipoHabitacion() {
        return ETipoHabitacion.DOBLE;
    }

    @Override
    public String toString() {
        return "HabitacionDoble{" +
                super.toString() +
                " ,isCamasSeparadas=" + camasSeparadas +
                '}';
    }
}
