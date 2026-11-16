package models;

import enums.EEstadoHabitacion;
import enums.ETipoHabitacion;
import interfaces.Identificable;

import java.util.Objects;

public abstract class Habitacion implements Identificable {

    private int numero;
    private int piso;
    private int capacidad;
    private double precioBase;
    private EEstadoHabitacion estadoHabitacion;
    private String motivoNoDisponible;

    public Habitacion(int numero, int piso, int capacidad, double precioBase) {
        this.numero = numero;
        this.piso = piso;
        this.capacidad = capacidad;
        this.precioBase = precioBase;
        this.estadoHabitacion = EEstadoHabitacion.DISPONIBLE;
        this.motivoNoDisponible = null;
    }

    public Habitacion(int numero, int piso, int capacidad, double precioBase, EEstadoHabitacion estadoHabitacion, String motivoNoDisponible) {
        this.numero = numero;
        this.piso = piso;
        this.capacidad = capacidad;
        this.precioBase = precioBase;
        this.estadoHabitacion = estadoHabitacion;
        this.motivoNoDisponible = estadoHabitacion == EEstadoHabitacion.OCUPADA
                ? null
                : motivoNoDisponible;
    }

    public int getNumero() {
        return numero;
    }

    public int getPiso() {
        return piso;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public EEstadoHabitacion getEstadoHabitacion() {
        return estadoHabitacion;
    }

    public String getMotivoNoDisponible() {
        return motivoNoDisponible;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public boolean estaDisponible() {
        return this.estadoHabitacion == EEstadoHabitacion.DISPONIBLE;
    }

    public void ocupar() {
        this.estadoHabitacion = EEstadoHabitacion.OCUPADA;
        this.motivoNoDisponible = null;
    }

    public void liberar() {
        this.estadoHabitacion = EEstadoHabitacion.DISPONIBLE;
        this.motivoNoDisponible = null;
    }

    public void marcarNoDisponible(String mensaje, EEstadoHabitacion estado) {
        this.estadoHabitacion = estado;
        motivoNoDisponible = mensaje;
    }

    public void modificarPrecio(double precio) {
        this.precioBase = precio;
    }

    public abstract double calcularPrecioPorNoche();

    public abstract ETipoHabitacion getTipoHabitacion();

    @Override
    public int getId() {
        return numero;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Habitacion that)) return false;
        return numero == that.numero;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(numero);
    }

    @Override
    public String toString() {
        String descripcion = "numero=" + numero +
                ", piso=" + piso +
                ", capacidad=" + capacidad +
                ", precioBase=" + precioBase +
                ", estadoHabitacion=" + estadoHabitacion;
        if (estadoHabitacion != EEstadoHabitacion.OCUPADA
                && motivoNoDisponible != null
                && !motivoNoDisponible.isBlank()) {
            descripcion += ", motivoNoDisponible=" + motivoNoDisponible;
        }
        return descripcion;
    }
}
