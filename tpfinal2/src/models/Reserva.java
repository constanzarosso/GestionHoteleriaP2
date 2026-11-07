package models;

import enums.EstadoReserva;
import interfaces.Identificable;

import java.time.LocalDate;

public class Reserva implements Identificable {
    private int idReserva;
 private Pasajero pasajero;
 private Habitacion habitacion;
private LocalDate fechaDesde;
 private LocalDate fechaHasta;
 private int cantidadPersonas;
 private EstadoReserva estado;

 public Reserva(int idReserva, Pasajero pasajero, LocalDate fechaDesde, Habitacion habitacion, LocalDate fechaHasta, int cantidadPersonas, EstadoReserva estado) {
  this.idReserva = idReserva;
  this.pasajero = pasajero;
  this.fechaDesde = fechaDesde;
  this.habitacion = habitacion;
  this.fechaHasta = fechaHasta;
  this.cantidadPersonas = cantidadPersonas;
  this.estado = estado;
 }

 @Override
 public int getId() {
  return 0;
 }

 public int getIdReserva() {
  return idReserva;
 }


 public Pasajero getPasajero() {
  return pasajero;
 }

 public void setPasajero(Pasajero pasajero) {
  this.pasajero = pasajero;
 }

 public Habitacion getHabitacion() {
  return habitacion;
 }

 public void setHabitacion(Habitacion habitacion) {
  this.habitacion = habitacion;
 }

 public LocalDate getFechaDesde() {
  return fechaDesde;
 }

 public void setFechaDesde(LocalDate fechaDesde) {
  this.fechaDesde = fechaDesde;
 }

 public LocalDate getFechaHasta() {
  return fechaHasta;
 }

 public void setFechaHasta(LocalDate fechaHasta) {
  this.fechaHasta = fechaHasta;
 }

 public int getCantidadPersonas() {
  return cantidadPersonas;
 }

 public void setCantidadPersonas(int cantidadPersonas) {
  this.cantidadPersonas = cantidadPersonas;
 }

 public EstadoReserva getEstado() {
  return estado;
 }

 public void setEstado(EstadoReserva estado) {
  this.estado = estado;
 }
 public boolean seSuperpone(LocalDate desde, LocalDate hasta) {

  return fechaDesde.isBefore(hasta)
          && fechaHasta.isAfter(desde);
 }

 public long calcularNoches() {
  return java.time.temporal.ChronoUnit.DAYS.between(
          fechaDesde, fechaHasta);
 }


 public boolean estaActiva() {
  return estado == EstadoReserva.PENDIENTE
          || estado == EstadoReserva.EN_CURSO;
 }


 public void cancelar() {
  if (estado == EstadoReserva.PENDIENTE) {
   estado = EstadoReserva.CANCELADA;
  }
 }

 public void pasarAEnCurso() {
  if (estado == EstadoReserva.PENDIENTE) {
   estado = EstadoReserva.EN_CURSO;
  }
 }

 public void finalizar() {
  if (estado == EstadoReserva.EN_CURSO) {
   estado = EstadoReserva.FINALIZADA;
  }
 }
}
