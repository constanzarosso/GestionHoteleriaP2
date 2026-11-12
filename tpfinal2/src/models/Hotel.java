package models;

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


    public Hotel(String nombre){
        this.nombre = nombre;
        this.usuarioActual = null;
        this.pasajeros = new GestorPasajeros();
        this.ocupaciones = new GestorOcupaciones();
        this.reservas = new GestorReservas();
        this.usuarios = new GestorUsuarios();
        this.habitaciones = new GestorHabitaciones();
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

        } catch (BDPersistenciaException e) {
            throw new RuntimeException(e);
        } catch (ElementoDuplicadoException e) {
            throw new RuntimeException(e);
        }



    }

    public Usuario iniciarSesion(String nombreUsuario, String contrasenia)throws BDCredencialesInvalidasException, BDUsuarioInactivoException {
        usuarioActual = usuarios.login(nombreUsuario, contrasenia);
        return usuarioActual;
    }

    public void cerrarSesion(){
        usuarioActual = null;
    }

    private void verificarPermiso(Permiso permiso) throws BDAccesoDenegadoException {
        if(usuarioActual == null || !usuarioActual.tienePermiso(permiso)){
            throw new BDAccesoDenegadoException();
        }
    }

    public void crearUsuario(Usuario usuario) throws BDAccesoDenegadoException, ElementoDuplicadoException, BDPersistenciaException {
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

    public void darDeBajaUsuario(int id) throws BDAccesoDenegadoException, ElementoNoEncontradoException, BDPersistenciaException {
        verificarPermiso(Permiso.GESTIONAR_USUARIOS);
        usuarios.darDeBajaUsuario(id);
    }

    public void asignarPermisos(int id, Permiso permiso) throws BDAccesoDenegadoException, ElementoNoEncontradoException, BDPersistenciaException {
        verificarPermiso(Permiso.ASIGNAR_PERMISOS);
        usuarios.asignarPermisos(id,permiso);
    }

    public void quitarPermiso(int id, Permiso permiso) throws BDAccesoDenegadoException, ElementoNoEncontradoException, BDPersistenciaException {
        verificarPermiso(Permiso.ASIGNAR_PERMISOS);
        usuarios.quitarPermisos(id,permiso);
    }

    public void cambiarContrasenia(String actual, String nueva) throws BDAccesoDenegadoException, BDContraseniaInvalidaException, ElementoNoEncontradoException, BDPersistenciaException {
        if(usuarioActual == null || !usuarioActual.validarContrasenia(actual)){
            throw new BDAccesoDenegadoException();
        }
        Validacion.validarContrasenia(nueva);
        usuarios.cambiarContrasenia(usuarioActual.getId(),nueva);
    }

    //Gestor Habitaciones

    public void hacerCheckIn(int idReserva)
            throws ElementoNoEncontradoException,
            HabitacionNoDisponibleException,
            ReservaInvalidaException,
            BDPersistenciaException, ElementoDuplicadoException, BDAccesoDenegadoException {

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
                BDPersistenciaException, BDAccesoDenegadoException {

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


    public List<Habitacion> listarHabitacionesDisponibles() throws BDAccesoDenegadoException {
        verificarPermiso(Permiso.VER_HABITACIONES);
        return habitaciones.listarDisponibles();
    }

    public List<Habitacion> listarHabitacionesOcupadas() throws BDAccesoDenegadoException {
        verificarPermiso(Permiso.VER_HABITACIONES);
        return habitaciones.listarOcupadas();
    }

    public List<Habitacion> listarHabitacionesPorTipo(ETipoHabitacion tipoHabitacion) throws BDAccesoDenegadoException {
        verificarPermiso(Permiso.VER_HABITACIONES);
        return habitaciones.listarPorTipo(tipoHabitacion);
    }


    public List<Habitacion> listarHabitacionesPorEstado(EEstadoHabitacion estado) throws BDAccesoDenegadoException {
        verificarPermiso(Permiso.VER_HABITACIONES);
        return habitaciones.listarPorEstado(estado);
    }

    public List<Habitacion> listarHabitacionesDisponiblesParaHuesped(int cantidad) throws BDAccesoDenegadoException, CapacidadExcedidaException {
        verificarPermiso(Permiso.VER_HABITACIONES);
        return habitaciones.listarDisponiblesParaHuesped(cantidad);
    }

    public void ocuparHabitacion(int numero) throws BDAccesoDenegadoException, ElementoNoEncontradoException {
        verificarPermiso(Permiso.CAMBIAR_ESTADO_HABITACION);
        Habitacion habitacion = habitaciones.buscarElemento(numero);
        habitacion.ocupar();
    }

    public void liberarHabitacion(int numero) throws BDAccesoDenegadoException, ElementoNoEncontradoException {
        verificarPermiso(Permiso.CAMBIAR_ESTADO_HABITACION);
        Habitacion habitacion = habitaciones.buscarElemento(numero);
        habitacion.liberar();
    }

    public void marcarHabitacionNoDisponible(int numero, String motivo, EEstadoHabitacion estado) throws BDAccesoDenegadoException, ElementoNoEncontradoException {
        verificarPermiso(Permiso.CAMBIAR_ESTADO_HABITACION);
        Habitacion habitacion = habitaciones.buscarElemento(numero);
        habitacion.marcarNoDisponible(motivo, estado);
    }

    public Habitacion buscarHabitacion(int numero) throws BDAccesoDenegadoException, ElementoNoEncontradoException {
        verificarPermiso(Permiso.VER_HABITACIONES);
        return habitaciones.buscarElemento(numero);
    }

    public List<Usuario> listarUsuarios() throws BDAccesoDenegadoException {
        verificarPermiso(Permiso.GESTIONAR_USUARIOS);
        return usuarios.obtenerElementos();
    }


}


