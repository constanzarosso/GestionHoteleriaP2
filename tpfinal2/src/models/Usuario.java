package models;

import enums.Permiso;
import enums.TipoUsuario;
import interfaces.Identificable;

import java.util.HashSet;
import java.util.Set;

public abstract class Usuario implements Identificable {

    private int id;
    private String nombreUsuario;
    private String contrasenia;
    private boolean activo;
    private Set<Permiso> permisos;

    public Usuario(int id, String nombreUsuario, String contrasenia) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
        this.activo = true;
        this.permisos = new HashSet<Permiso>();
    }

    @Override
    public int getId(){
        return id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void darDeBaja(){
        this.activo = false;
    }

    //Evita el usuario.getPemisos().clear() osea que no lo pueden borrar los permisos
    public Set<Permiso> getPermisos() {
        return new HashSet<>(permisos);
    }

    public boolean validarContrasenia(String contrasenia){
        return this.contrasenia.equals(contrasenia);

    }

    public boolean tienePermiso(Permiso permiso){
        return permisos.contains(permiso);
    }

    public void agregarPermiso(Permiso permiso){
        permisos.add(permiso);
    }

    public void quitarPermiso(Permiso permiso){
        permisos.remove(permiso);
    }

    public void cambiarContrasenia(String contrasenia){
        this.contrasenia = contrasenia;
    }
    //Agrega el conjunto de permisos
    public void InicializarPermisos(){
        this.permisos.addAll(permisosIniciales());
    }

    public abstract TipoUsuario getTipoUsuario();

    public abstract Set<Permiso> permisosIniciales();

}
