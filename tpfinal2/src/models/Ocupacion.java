package models;

import interfaces.Facturable;
import interfaces.Identificable;
import enums.MetodoDePago;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Representa la estadía de un pasajero en una habitación.
 */
public class Ocupacion implements Identificable, Facturable {

    private int id;
    private Reserva reserva;
    private LocalDate fechaCheckIn;
    private LocalDate fechaCheckOut;
    private double total;
    private MetodoDePago metodoPago;
    private List<Consumo> consumos;

    /**
     * Constructor de Ocupacion.
     */
    public Ocupacion(int id, Reserva reserva,
                     LocalDate fechaCheckIn) {

        this.id = id;
        this.reserva = reserva;
        this.fechaCheckIn = fechaCheckIn;
        this.fechaCheckOut = null;
        this.total = 0;
        this.metodoPago = null;
        this.consumos = new ArrayList<>();
    }

    @Override
    public int getId() {
        return id;
    }

    public Habitacion getHabitacion() {
        return reserva.getHabitacion();
    }

    public Pasajero getTitular() {
        return reserva.getPasajero();
    }

    public Reserva getReserva() {
        return reserva;
    }

    public LocalDate getFechaCheckIn() {
        return fechaCheckIn;
    }

    public LocalDate getFechaCheckOut() {
        return fechaCheckOut;
    }

    public double getTotal() {
        return total;
    }

    public MetodoDePago getMetodoPago() {
        return metodoPago;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Ocupacion ocupacion)) return false;
        return id == ocupacion.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public List<Consumo> getConsumos() {
        return new ArrayList<>(consumos);
    }


    public void agregarConsumo(Consumo c) {
        consumos.add(c);
    }


    public boolean estaActiva() {
        return fechaCheckOut == null;
    }

    /**
     * Finaliza la ocupación y registra el método de pago.
     */
    public void finalizar(LocalDate fecha, MetodoDePago pago) {

        if (estaActiva()) {
            this.fechaCheckOut = fecha;
            this.metodoPago = pago;
            this.total = calcularTotal();
            reserva.finalizar();
        }
    }

    /**
     * Calcula el total de la estadía y sus consumos.
     */
    @Override
    public double calcularTotal() {

        long noches = java.time.temporal.ChronoUnit.DAYS.between(
                fechaCheckIn, fechaCheckOut != null
                        ? fechaCheckOut
                        : LocalDate.now());

       // double totalEstadia =
               // noches * reserva.getHabitacion().calcularPrecioPorNoche();

        double totalConsumos = 0;

        for (Consumo consumo : consumos) {
            totalConsumos += consumo.calcularTotal();
        }

        return 0; //totalEstadia + totalConsumos;
    }

    @Override
    public String toString() {
        return "Ocupacion{" +
                "id=" + id +
                ", reserva=" + reserva +
                ", fechaCheckIn=" + fechaCheckIn +
                ", fechaCheckOut=" + fechaCheckOut +
                ", total=" + total +
                ", metodoPago=" + metodoPago +
                ", consumos=" + consumos +
                '}';
    }
}
