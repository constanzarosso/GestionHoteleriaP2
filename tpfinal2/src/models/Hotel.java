package models;

import ConexionBD.GestorHabitacionesBD;
import Excepciones.ExcepcionesBD.*;
import Excepciones.ExcepcionesDatos.ElementoDuplicadoException;
import Excepciones.ExcepcionesDatos.ElementoNoEncontradoException;
import Excepciones.ExcepcionesHotel.CapacidadExcedidaException;
import Excepciones.ExcepcionesHotel.HabitacionNoDisponibleException;
import Excepciones.ExcepcionesHotel.ReservaInvalidaException;
import Validaciones.Validacion;

import enums.*;

import enums.EstadoReserva;
import enums.MetodoDePago;
import enums.Permiso;
import gestoras.*;


import java.time.LocalDate;
import java.util.List;

public class Hotel {

    private String nombre;
    private Usuario usuarioActual;
    private GestorPasajeros pasajeros;
    private GestorOcupaciones ocupaciones;
    private GestorReservas reservas;
    private GestorUsuarios usuarios;
    private GestorHabitaciones habitaciones;


    public Hotel(String nombre) throws PersistenciaException {
        this.nombre = nombre;
        this.usuarioActual = null;
        this.pasajeros = new GestorPasajeros();
        this.ocupaciones = new GestorOcupaciones();
        this.reservas = new GestorReservas();
        this.usuarios = new GestorUsuarios();
        this.habitaciones = new GestorHabitaciones();
        cargarHabitaciones();
        cargarCredencialesAdmin();
    }

    public String getNombre() {
        return nombre;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    //Gestor Usuarios
    private void cargarCredencialesAdmin(){

        try {
            usuarios.cargarUsuarios();

            if(usuarios.cantidadElementos() == 0){

                Administrador admin = new Administrador(
                        usuarios.siguienteId(), "admin","Admin123!");

                usuarios.registrarUsuario(admin);
            }
        } catch (PersistenciaException e) {
            throw new RuntimeException(e);
        } catch (ElementoDuplicadoException e) {
            throw new RuntimeException(e);
        }



    }

    public Usuario iniciarSesion(String nombreUsuario, String contrasenia)throws CredencialesInvalidasException, UsuarioInactivoException {
        usuarioActual = usuarios.login(nombreUsuario, contrasenia);
        return usuarioActual;
    }

    public void cerrarSesion(){
        usuarioActual = null;
    }

    private void verificarPermiso(Permiso permiso) throws AccesoDenegadoException {
        if(usuarioActual == null || !usuarioActual.tienePermiso(permiso)){
            throw new AccesoDenegadoException();
        }
    }

    public void crearUsuario(Usuario usuario) throws AccesoDenegadoException, ElementoDuplicadoException, PersistenciaException {
        verificarPermiso(Permiso.GESTIONAR_USUARIOS);

        try {
            usuarios.buscarPorNombre(usuario.getNombreUsuario());
            throw new ElementoDuplicadoException();
        }catch (ElementoNoEncontradoException e){
            usuarios.registrarUsuario(usuario);
        }
    }
    public int siguienteIdUsuario(){
        return usuarios.siguienteId();
    }

    public void darDeBajaUsuario(int id) throws AccesoDenegadoException, ElementoNoEncontradoException, PersistenciaException {
        verificarPermiso(Permiso.GESTIONAR_USUARIOS);
        usuarios.darDeBajaUsuario(id);
    }

    public void asignarPermisos(int id, Permiso permiso) throws AccesoDenegadoException, ElementoNoEncontradoException, PersistenciaException {
        verificarPermiso(Permiso.ASIGNAR_PERMISOS);
        usuarios.asignarPermisos(id,permiso);
    }

    public void quitarPermiso(int id, Permiso permiso) throws AccesoDenegadoException, ElementoNoEncontradoException, PersistenciaException {
        verificarPermiso(Permiso.ASIGNAR_PERMISOS);
        usuarios.quitarPermisos(id,permiso);
    }

    public void cambiarContrasenia(String actual, String nueva) throws AccesoDenegadoException, ContraseniaInvalidaException, ElementoNoEncontradoException, PersistenciaException {
        if(usuarioActual == null || !usuarioActual.validarContrasenia(actual)){
            throw new AccesoDenegadoException();
        }
        Validacion.validarContrasenia(nueva);
        usuarios.cambiarContrasenia(usuarioActual.getId(),nueva);
    }

    //Gestor Habitaciones
    private void cargarHabitaciones() throws PersistenciaException {
        this.habitaciones = new GestorHabitaciones(GestorHabitacionesBD.listar());
    }

    public void agregarHabitacion(Habitacion h) throws AccesoDenegadoException, ElementoDuplicadoException, PersistenciaException {
        verificarPermiso(Permiso.GESTIONAR_HABITACIONES);
        habitaciones.agregarElemento(h);
        GestorHabitacionesBD.insertHabitacion(h);
    }

    public void modificarPrecio(int numero, double precio) throws AccesoDenegadoException, ElementoNoEncontradoException, PersistenciaException {
        verificarPermiso(Permiso.GESTIONAR_HABITACIONES);
        Habitacion habitacion = habitaciones.buscarElemento(numero);
        habitacion.modificarPrecio(precio);
        GestorHabitacionesBD.modificarHabitacion(habitacion);
    }

    public void ocuparHabitacion(int numero) throws AccesoDenegadoException, ElementoNoEncontradoException, PersistenciaException {
        verificarPermiso(Permiso.CAMBIAR_ESTADO_HABITACION);
        Habitacion habitacion = habitaciones.buscarElemento(numero);
        habitacion.ocupar();
        GestorHabitacionesBD.modificarHabitacion(habitacion);
    }

    public void liberarHabitacion (int numero) throws AccesoDenegadoException, ElementoNoEncontradoException, PersistenciaException {
        verificarPermiso(Permiso.CAMBIAR_ESTADO_HABITACION);
        Habitacion habitacion = habitaciones.buscarElemento(numero);
        habitacion.liberar();
        GestorHabitacionesBD.modificarHabitacion(habitacion);
    }

    public void marcarHabitacionNoDisponible (int numero, String motivo, EEstadoHabitacion estado) throws AccesoDenegadoException, ElementoNoEncontradoException, PersistenciaException {
        verificarPermiso(Permiso.CAMBIAR_ESTADO_HABITACION);
        Habitacion habitacion = habitaciones.buscarElemento(numero);
        habitacion.marcarNoDisponible(motivo,estado);
        GestorHabitacionesBD.modificarHabitacion(habitacion);
    }

    public List<Habitacion> listarHabitacionesNoDisponibles() throws AccesoDenegadoException {
        verificarPermiso(Permiso.VER_HABITACIONES);
        return habitaciones.listarNoDisponibles();
    }

    public List<Habitacion> listarHabitaciones() throws AccesoDenegadoException {
        verificarPermiso(Permiso.VER_HABITACIONES);
        return habitaciones.obtenerElementos();
    }

    public void hacerCheckIn(int idReserva)
            throws ElementoNoEncontradoException,
            HabitacionNoDisponibleException,
            ReservaInvalidaException,
            PersistenciaException, ElementoDuplicadoException, AccesoDenegadoException {

        verificarPermiso(Permiso.HACER_CHECKIN_CHECKOUT);
        Reserva reserva = reservas.buscarElemento(idReserva);

        if (reserva.getEstado() != EstadoReserva.PENDIENTE) {
            throw new ReservaInvalidaException();
        }

        Habitacion habitacion = reserva.getHabitacion();

        if (!habitacion.estaDisponible()) {
            throw new HabitacionNoDisponibleException();
        }

        if (ocupaciones.ocupacionActiva(habitacion.getId()) != null) {
            throw new ReservaInvalidaException();
        }

        reserva.pasarAEnCurso();
        habitacion.ocupar();

        Ocupacion ocupacion = new Ocupacion(
                ocupaciones.siguienteId(),
                reserva,
                LocalDate.now()
        );

        ocupaciones.agregarElemento(ocupacion);


        ///reservaDAO.modificar(reserva);
        ///ocupacionDAO.insertar(ocupacion);
        /// aca hay que meter persistencia de datos
    }

        public void hacerCheckOut(int numero, MetodoDePago pago)
                throws ElementoNoEncontradoException,
                ReservaInvalidaException,
                PersistenciaException, AccesoDenegadoException {

            verificarPermiso(Permiso.HACER_CHECKIN_CHECKOUT);
            Ocupacion ocupacion = ocupaciones.ocupacionActiva(numero);

            if (ocupacion == null) {
                throw new ReservaInvalidaException();
            }

            if (pago == null) {
                throw new ReservaInvalidaException();
            }

            ocupacion.finalizar(LocalDate.now(), pago);

            Reserva reserva = ocupacion.getReserva();
            reserva.finalizar();

            Habitacion habitacion = ocupacion.getHabitacion();
            habitacion.liberar();

            ///ocupacionDAO.modificar(ocupacion);
            ///reservaDAO.modificar(reserva);
            /// aca persistencia de datos
        }


    public List<Habitacion> listarHabitacionesDisponibles() throws AccesoDenegadoException {
        verificarPermiso(Permiso.VER_HABITACIONES);
        return habitaciones.listarDisponibles();
    }

    public List<Habitacion> listarHabitacionesOcupadas() throws AccesoDenegadoException {
        verificarPermiso(Permiso.VER_HABITACIONES);
        return habitaciones.listarOcupadas();
    }

    public List<Habitacion> listarHabitacionesPorTipo(ETipoHabitacion tipoHabitacion) throws AccesoDenegadoException {
        verificarPermiso(Permiso.VER_HABITACIONES);
        return habitaciones.listarPorTipo(tipoHabitacion);
    }


    public List<Habitacion> listarHabitacionesPorEstado(EEstadoHabitacion estado) throws AccesoDenegadoException {
        verificarPermiso(Permiso.VER_HABITACIONES);
        return habitaciones.listarPorEstado(estado);
    }

    public List<Habitacion> listarHabitacionesDisponiblesParaHuesped(int cantidad) throws AccesoDenegadoException, CapacidadExcedidaException {
        verificarPermiso(Permiso.VER_HABITACIONES);
        return habitaciones.listarDisponiblesParaHuesped(cantidad);
    }

    public Habitacion buscarHabitacion(int numero) throws AccesoDenegadoException, ElementoNoEncontradoException {
        verificarPermiso(Permiso.VER_HABITACIONES);
        return habitaciones.buscarElemento(numero);
    }

    public List<Usuario> listarUsuarios() throws AccesoDenegadoException {
        verificarPermiso(Permiso.GESTIONAR_USUARIOS);
        return usuarios.obtenerElementos();
    }


}


