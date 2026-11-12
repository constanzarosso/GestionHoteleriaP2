package models;

import java.time.LocalDateTime;

public class Backup {

    private int id;
    private LocalDateTime fechaHora;
    private String nombreUsuario;
    private String nombreArchivo;

    public Backup(int id, LocalDateTime fechaHora, String nombreUsuario, String nombreArchivo) {
        this.id = id;
        this.fechaHora = fechaHora;
        this.nombreUsuario = nombreUsuario;
        this.nombreArchivo = nombreArchivo;

    }


    public int getId() {
        return id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }


}
