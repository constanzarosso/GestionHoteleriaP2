package models;

import utils.Identificable;

public class Pasajero implements Identificable {


        private int dni;
        private String nombre;
        private String apellido;
        private String origen;
        private String domicilioOrigen;
        private String telefono;
        private String email;


        public Pasajero( int dni, String nombre, String apellido,
            String origen, String domicilioOrigen,
            String telefono, String email){

        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.origen = origen;
        this.domicilioOrigen = domicilioOrigen;
        this.telefono = telefono;
        this.email = email;
    }

        @Override
        public int getId() {
        return dni;
    }

        public int getDni () {
        return dni;
    }

        public String getNombre () {
        return nombre;
    }

        public String getApellido () {
        return apellido;
    }

        public String getOrigen () {
        return origen;
    }

        public String getDomicilioOrigen () {
        return domicilioOrigen;
    }

        public String getTelefono () {
        return telefono;
    }

        public String getEmail () {
        return email;
    }


        public void modificarDatos (String domicilio,
            String telefono,
            String email){

        this.domicilioOrigen = domicilio;
        this.telefono = telefono;
        this.email = email;
    }

        @Override
        public String toString () {
        return "Pasajero{" +
                "dni=" + dni +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", origen='" + origen + '\'' +
                ", domicilioOrigen='" + domicilioOrigen + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
    }