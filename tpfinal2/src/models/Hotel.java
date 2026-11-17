package models;

import ConexionBD.GestorBackupBD;
import ConexionBD.GestorHabitacionesBD;
import ConexionBD.GestorOcupacionesBD;
import ConexionBD.GestorPasajerosBD;
import ConexionBD.GestorReservasBD;
import Excepciones.ExcepcionesBD.*;
import Excepciones.ExcepcionesDatos.ElementoDuplicadoException;
import Excepciones.ExcepcionesDatos.ElementoNoEncontradoException;
import Excepciones.ExcepcionesHotel.CapacidadExcedidaException;
import Excepciones.ExcepcionesHotel.HabitacionNoDisponibleException;
import Excepciones.ExcepcionesHotel.PeriodoInvalidoException;
import Excepciones.ExcepcionesHotel.ReservaInvalidaException;
import Validaciones.Validacion;

import enums.*;

import enums.EstadoReserva;
import enums.MetodoDePago;
import enums.Permiso;
import gestoras.*;


import java.time.LocalDate;
import java.util.ArrayList;
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
        cargarPasajerosReservasYOcupaciones();
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

    private void cargarPasajerosReservasYOcupaciones() throws PersistenciaException {
        try {
            GestorPasajeros pasajerosBD = new GestorPasajeros(new GestorPasajerosBD().listar());
            GestorReservas reservasBD = new GestorReservas(new GestorReservasBD().listar(habitaciones, pasajerosBD));
            this.pasajeros = pasajerosBD;
            this.reservas = reservasBD;
            this.ocupaciones = new GestorOcupaciones(new GestorOcupacionesBD().listar(reservasBD));
        } catch (ElementoNoEncontradoException e) {
            throw new PersistenciaException("No se pudieron relacionar los datos guardados: " + e.getMessage());
        }
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
        verificarPermiso(Permiso.HACER_CHECKIN_CHECKOUT);
        Habitacion habitacion = habitaciones.buscarElemento(numero);
        habitacion.ocupar();
        GestorHabitacionesBD.modificarHabitacion(habitacion);
    }

    public void liberarHabitacion (int numero) throws AccesoDenegadoException, ElementoNoEncontradoException, HabitacionNoDisponibleException, PersistenciaException {
        verificarPermiso(Permiso.CAMBIAR_ESTADO_HABITACION);
        Habitacion habitacion = habitaciones.buscarElemento(numero);

        // Una habitacion con un check-in activo solo se libera haciendo el check-out
        if (ocupaciones.tieneOcupacionActiva(numero)) {
            throw new HabitacionNoDisponibleException();
        }
        habitacion.liberar();
        GestorHabitacionesBD.modificarHabitacion(habitacion);
    }

    public void marcarHabitacionNoDisponible (int numero, String motivo, EEstadoHabitacion estado) throws AccesoDenegadoException, ElementoNoEncontradoException, HabitacionNoDisponibleException, PersistenciaException {
        verificarPermiso(Permiso.CAMBIAR_ESTADO_HABITACION);
        if (usuarioActual.getTipoUsuario() != TipoUsuario.ADMINISTRADOR) {
            throw new AccesoDenegadoException();
        }
        Habitacion habitacion = habitaciones.buscarElemento(numero);

        // Una habitacion con un check-in activo solo se libera haciendo el check-out
        if (ocupaciones.tieneOcupacionActiva(numero)) {
            throw new HabitacionNoDisponibleException();
        }
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

        // El check-in se hace desde el dia de entrada y antes del dia de salida
        LocalDate hoy = LocalDate.now();
        if (hoy.isBefore(reserva.getFechaDesde()) || !hoy.isBefore(reserva.getFechaHasta())) {
            throw new ReservaInvalidaException();
        }

        Habitacion habitacion = reserva.getHabitacion();

        if (habitacion.getEstadoHabitacion() != EEstadoHabitacion.DISPONIBLE) {
            throw new HabitacionNoDisponibleException();
        }

        if (ocupaciones.tieneOcupacionActiva(habitacion.getId())) {
            throw new ReservaInvalidaException();
        }

        reserva.pasarAEnCurso();
        ocuparHabitacion(habitacion.getId());

        Ocupacion ocupacion = new Ocupacion(
                ocupaciones.siguienteId(),
                reserva,
                LocalDate.now()
        );

        ocupaciones.agregarElemento(ocupacion);
        new GestorOcupacionesBD().insertar(ocupacion);
        new GestorReservasBD().modificar(reserva);



    }

        public void hacerCheckOut(int numero, MetodoDePago pago)
                throws ElementoNoEncontradoException,
                ReservaInvalidaException,
                PersistenciaException, AccesoDenegadoException {

            verificarPermiso(Permiso.HACER_CHECKIN_CHECKOUT);
            Ocupacion ocupacion = ocupaciones.ocupacionActiva(numero);

            if (pago == null) {
                throw new ReservaInvalidaException();
            }

            ocupacion.finalizar(LocalDate.now(), pago);

            Reserva reserva = ocupacion.getReserva();
            reserva.finalizar();

            Habitacion habitacion = ocupacion.getHabitacion();
            habitacion.liberar();
            new GestorOcupacionesBD().modificar(ocupacion);
            new GestorReservasBD().modificar(reserva);
            GestorHabitacionesBD.modificarHabitacion(habitacion);


        }
    public void hacerCheckInSinReserva(int dni, int numeroHabitacion, LocalDate fechaHasta, int cantidadPersonas)
            throws AccesoDenegadoException,
            ElementoNoEncontradoException,
            HabitacionNoDisponibleException,
            CapacidadExcedidaException,
            ElementoDuplicadoException,
            ReservaInvalidaException, PersistenciaException, PeriodoInvalidoException {

        verificarPermiso(Permiso.HACER_CHECKIN_CHECKOUT);

        if (fechaHasta == null || !LocalDate.now().isBefore(fechaHasta)) {
            throw new PeriodoInvalidoException();
        }

        Pasajero pasajero = pasajeros.buscarElemento(dni);
        Habitacion habitacion = habitaciones.buscarElemento(numeroHabitacion);

        if (habitacion.getEstadoHabitacion() != EEstadoHabitacion.DISPONIBLE) {
            throw new HabitacionNoDisponibleException();
        }

        if (cantidadPersonas > habitacion.getCapacidad()) {
            throw new CapacidadExcedidaException();
        }
        // No se puede ocupar si se pisa con una reserva que ya existe para esos dias
        if (reservas.hayReservaSuperpuesta(numeroHabitacion, LocalDate.now(), fechaHasta)) {
            throw new HabitacionNoDisponibleException();
        }


        Reserva reserva = new Reserva(
                reservas.siguienteId(),
                pasajero,
                LocalDate.now(),
                habitacion,
                fechaHasta,
                cantidadPersonas,
                EstadoReserva.EN_CURSO
        );

        habitacion.ocupar();
        GestorHabitacionesBD.modificarHabitacion(habitacion);
        reservas.agregarElemento(reserva);

        Ocupacion ocupacion = new Ocupacion(
                ocupaciones.siguienteId(),
                reserva,
                LocalDate.now()
        );

        ocupaciones.agregarElemento(ocupacion);
        new GestorReservasBD().insertar(reserva);
        new GestorOcupacionesBD().insertar(ocupacion);
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


    // ==================== PASAJEROS ====================

    public void registrarPasajero(Pasajero pasajero)
            throws AccesoDenegadoException, ElementoDuplicadoException, PersistenciaException {

        verificarPermiso(Permiso.CARGAR_PASAJEROS);

        pasajeros.agregarElemento(pasajero);
        try {
            new GestorPasajerosBD().insertar(pasajero);
        } catch (PersistenciaException e) {
            try {
                pasajeros.eliminarElemento(pasajero.getId());
            } catch (ElementoNoEncontradoException ignorada) {
                e.addSuppressed(ignorada);
            }
            throw e;
        }
    }

    public Pasajero buscarPasajero(int dni)
            throws AccesoDenegadoException, ElementoNoEncontradoException {

        verificarPermiso(Permiso.GESTIONAR_RESERVAS);

        return pasajeros.buscarElemento(dni);
    }

    public List<Pasajero> buscarPasajerosPorApellido(String apellido)
            throws AccesoDenegadoException, ElementoNoEncontradoException {

        verificarPermiso(Permiso.GESTIONAR_RESERVAS);

        return pasajeros.buscarPorApellido(apellido);
    }


// ==================== RESERVAS ====================

    public boolean consultarDisponibilidad(int numeroHabitacion,
                                           LocalDate desde,
                                           LocalDate hasta)
            throws AccesoDenegadoException,
            ElementoNoEncontradoException,
            PeriodoInvalidoException {

        verificarPermiso(Permiso.GESTIONAR_RESERVAS);

        Habitacion habitacion = habitaciones.buscarElemento(numeroHabitacion);

        // Para fechas futuras no importa el estado de hoy: importa que no se pise con otra reserva.
        // Solo una habitacion fuera de servicio no se puede reservar.
        if (habitacion.getEstadoHabitacion() == EEstadoHabitacion.FUERA_DE_SERVICIO) {
            return false;
        }

        return !reservas.hayReservaSuperpuesta(numeroHabitacion, desde, hasta);
    }

    public void crearReserva(int dni,
                             int numeroHabitacion,
                             LocalDate desde,
                             LocalDate hasta,
                             int cantidadPersonas)
            throws AccesoDenegadoException,
            ElementoNoEncontradoException,
            ElementoDuplicadoException,
            PeriodoInvalidoException,
            HabitacionNoDisponibleException,
            CapacidadExcedidaException, PersistenciaException {

        verificarPermiso(Permiso.GESTIONAR_RESERVAS);

        if (desde == null || hasta == null || !desde.isBefore(hasta) || desde.isBefore(LocalDate.now())) {
            throw new PeriodoInvalidoException();
        }

        Pasajero pasajero = pasajeros.buscarElemento(dni);
        Habitacion habitacion = habitaciones.buscarElemento(numeroHabitacion);

        // Para fechas futuras no importa el estado de hoy: importa que no se pise con otra reserva.
        if (habitacion.getEstadoHabitacion() == EEstadoHabitacion.FUERA_DE_SERVICIO) {
            throw new HabitacionNoDisponibleException();
        }

        if (cantidadPersonas > habitacion.getCapacidad()) {
            throw new CapacidadExcedidaException();
        }

        if (reservas.hayReservaSuperpuesta(numeroHabitacion, desde, hasta)) {
            throw new HabitacionNoDisponibleException();
        }

        Reserva reserva = new Reserva(
                reservas.siguienteId(),
                pasajero,desde,
                habitacion,
                hasta,
                cantidadPersonas,
                EstadoReserva.PENDIENTE
        );

        reservas.agregarElemento(reserva);
        try {
            new GestorReservasBD().insertar(reserva);
        } catch (PersistenciaException e) {
            reservas.eliminarElemento(reserva.getId());
            throw e;
        }
    }

    public void cancelarReserva(int idReserva)
            throws AccesoDenegadoException, ElementoNoEncontradoException,
            ReservaInvalidaException, PersistenciaException {

        verificarPermiso(Permiso.GESTIONAR_RESERVAS);

        Reserva reserva = reservas.buscarElemento(idReserva);

        if (reserva.getEstado() != EstadoReserva.PENDIENTE) {
            throw new ReservaInvalidaException();
        }

        reserva.cancelar();
        new GestorReservasBD().modificar(reserva);
    }

    public List<Reserva> listarReservasPendientes()
            throws AccesoDenegadoException {

        verificarPermiso(Permiso.GESTIONAR_RESERVAS);

        List<Reserva> pendientes = new ArrayList<>();

        for (Reserva reserva : reservas.obtenerElementos()) {
            if (reserva.getEstado() == EstadoReserva.PENDIENTE) {
                pendientes.add(reserva);
            }
        }

        return pendientes;
    }


// ==================== OCUPACIONES ====================

    public List<Ocupacion> listarOcupacionesActivas()
            throws AccesoDenegadoException {

        verificarPermiso(Permiso.HACER_CHECKIN_CHECKOUT);

        return ocupaciones.listarActivas();
    }

    public List<Ocupacion> consultarHistorialOcupaciones(int dni)
            throws AccesoDenegadoException, ElementoNoEncontradoException {

        verificarPermiso(Permiso.HACER_CHECKIN_CHECKOUT);

        return ocupaciones.historial(dni);
    }
    //====================== consumo ====================
    public void registrarConsumo(int numeroHabitacion, Consumo consumo)
            throws AccesoDenegadoException,
            ElementoNoEncontradoException,
            ReservaInvalidaException, PersistenciaException {

        verificarPermiso(Permiso.REGISTRAR_CONSUMO);

        Ocupacion ocupacion = ocupaciones.ocupacionActiva(numeroHabitacion);

        if (consumo == null) {
            throw new ReservaInvalidaException();
        }

        new GestorOcupacionesBD().insertarConsumo(ocupacion.getId(), consumo);
        ocupacion.agregarConsumo(consumo);
    }

    //============== backup ===================
    public List<Backup> listarBackups() throws PersistenciaException, AccesoDenegadoException {
        verificarPermiso(Permiso.HACER_BACKUP);
        return GestorBackupBD.listarBackups();
    }

    public void crearBackup() throws PersistenciaException, AccesoDenegadoException {
        verificarPermiso(Permiso.HACER_BACKUP);
        String nombreUsuario = usuarioActual.getNombreUsuario();
        GestorBackupBD.crearBackup(nombreUsuario);
    }

    public void restaurarBackup(int id) throws PersistenciaException, AccesoDenegadoException {
        verificarPermiso(Permiso.HACER_BACKUP);
        GestorBackupBD.restaurarBackup(id);

        // Despues de restaurar, la base cambio: se vuelven a cargar los datos en memoria
        cargarHabitaciones();
        cargarPasajerosReservasYOcupaciones();
    }
}


