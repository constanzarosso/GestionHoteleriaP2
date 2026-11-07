package models;

import java.util.Objects;

public class Pasajero {
    private int id;
    private String nombre;
private String apellido;
 private String paisDeOrigen;
 private String domicilioOrigen;
private double telefono;
 private String email;

    public Pasajero(int id, String nombre, String apellido, String paisDeOrigen, String domicilioOrigen, double telefono, String email) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.paisDeOrigen = paisDeOrigen;
        this.domicilioOrigen = domicilioOrigen;
        this.telefono = telefono;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPaisDeOrigen() {
        return paisDeOrigen;
    }

    public void setPaisDeOrigen(String paisDeOrigen) {
        this.paisDeOrigen = paisDeOrigen;
    }

    public String getDomicilioOrigen() {
        return domicilioOrigen;
    }

    public void setDomicilioOrigen(String domicilioOrigen) {
        this.domicilioOrigen = domicilioOrigen;
    }

    public double getTelefono() {
        return telefono;
    }

    public void setTelefono(double telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pasajero pasajero)) return false;
        return id == pasajero.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Pasajero{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", paisDeOrigen='" + paisDeOrigen + '\'' +
                ", domicilioOrigen='" + domicilioOrigen + '\'' +
                ", telefono=" + telefono +
                ", email='" + email + '\'' +
                '}';
    }
}
