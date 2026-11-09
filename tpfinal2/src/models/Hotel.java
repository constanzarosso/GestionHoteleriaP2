package models;

import Excepciones.*;
import Validaciones.Validacion;
import enums.Permiso;

public class Hotel {

    private String nombre;
    private Usuario usuarioActual;
    private GestorPasajeros pasajeros;
    private GestorOcupaciones ocupaciones;
    private GestorReservas reservas;
    private GestorUsuarios usuarios;


    public Hotel(String nombre){
        this.nombre = nombre;
        this.usuarioActual = null;
        this.pasajeros = new GestorPasajeros();
        this.ocupaciones = new GestorOcupaciones();
        this.reservas = new GestorReservas();
        this.usuarios = new GestorUsuarios();

        cargarDatos();
    }

    public String getNombre() {
        return nombre;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    //Gestor Usuarios
    private void cargarDatos(){
        if(usuarios.cantidadElementos() == 0){
            Administrador admin = new Administrador(usuarios.siguienteId(), "admin","Admin123!");

            try {
                usuarios.agregarElemento(admin);
            }catch (ElementoDuplicadoException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public Usuario iniciarSesion(String nombreUsuario, String contrasenia)throws CredencialesInvalidasException, UsuarioInactivoException {
        usuarioActual = usuarios.login(nombreUsuario, contrasenia);
        return usuarioActual;
    }

    public void cerrarSesion(){
        usuarioActual = null;
    }

    private void verificarPermiso(Permiso permiso) throws AccesoDenegadoException{
        if(usuarioActual == null || !usuarioActual.tienePermiso(permiso)){
            throw new AccesoDenegadoException();
        }
    }

    public void crearUsuario(Usuario usuario) throws AccesoDenegadoException, ElementoDuplicadoException{
        verificarPermiso(Permiso.GESTIONAR_USUARIOS);

        try {
            usuarios.buscarPorNombre(usuario.getNombreUsuario());
            throw new ElementoDuplicadoException();
        }catch (ElementoNoEncontradoException e){
            usuarios.agregarElemento(usuario);
        }
    }

    public void darDeBajaUsuario(int id) throws AccesoDenegadoException, ElementoNoEncontradoException{
        verificarPermiso(Permiso.GESTIONAR_USUARIOS);
        usuarios.darDeBajaUsuario(id);
    }

    public void asignarPermisos(int id, Permiso permiso) throws AccesoDenegadoException, ElementoNoEncontradoException{
        verificarPermiso(Permiso.ASIGNAR_PERMISOS);
        usuarios.asignarPermisos(id,permiso);
    }

    public void quitarPermiso(int id, Permiso permiso) throws AccesoDenegadoException, ElementoNoEncontradoException{
        verificarPermiso(Permiso.ASIGNAR_PERMISOS);
        usuarios.quitarPermisos(id,permiso);
    }

    public void cambiarContrasenia(String actual, String nueva) throws AccesoDenegadoException, ContraseniaInvalidaException, ElementoNoEncontradoException{
        if(usuarioActual == null || !usuarioActual.validarContrasenia(actual)){
            throw new AccesoDenegadoException();
        }
        Validacion.validarContrasenia(nueva);
        usuarios.cambiarContrasenia(usuarioActual.getId(),nueva);
    }

    //Gestor Habitaciones


}
