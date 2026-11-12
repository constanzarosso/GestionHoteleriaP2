package models;

import interfaces.Facturable;
import interfaces.Identificable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Consumo implements Facturable, Identificable {

    private int id;
    private String descripcion;
    private double monto;
    private LocalDate fecha;


    public Consumo(int id, String descripcion,
                   double monto, LocalDate fecha) {

        this.id = id;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha = fecha;

    }

    @Override
    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getMonto() {
        return monto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Consumo consumo)) return false;
        return id == consumo.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public double calcularTotal() {
        return monto;
    }


    @Override
    public String toString() {
        return "Consumo{" +
                "id=" + id +
                ", descripcion='" + descripcion + '\'' +
                ", monto=" + monto +
                ", fecha=" + fecha +
                '}';
    }
}