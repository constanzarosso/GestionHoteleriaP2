package Menus;

import Excepciones.ExcepcionesBD.AccesoDenegadoException;
import Excepciones.ExcepcionesBD.PersistenciaException;
import Excepciones.ExcepcionesDatos.ElementoDuplicadoException;
import Excepciones.ExcepcionesDatos.ElementoNoEncontradoException;
import Excepciones.ExcepcionesHotel.CapacidadExcedidaException;
import Excepciones.ExcepcionesHotel.HabitacionNoDisponibleException;
import Excepciones.ExcepcionesHotel.PeriodoInvalidoException;
import Excepciones.ExcepcionesHotel.ReservaInvalidaException;
import Validaciones.Teclado;
import enums.MetodoDePago;
import models.Consumo;
import models.Hotel;
import models.Ocupacion;
import models.Pasajero;
import models.Reserva;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static Menus.Consola.*;

/**
 * Menu del recepcionista.
 * Todas las lecturas se hacen con Teclado (un solo Scanner para todo el programa).
 * Si un dato es invalido se vuelve a pedir ese dato, sin salir de la operacion.
 */
public final class MenuRecepcionista {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private MenuRecepcionista() {
    }

    /**
     * Muestra el menu una vez y ejecuta la opcion elegida.
     * @return true para seguir en el menu, false cuando el usuario cierra sesion
     */
    public static boolean mostrar(Hotel hotel) {
        mostrarEncabezado("MENU RECEPCIONISTA");

        System.out.println("  1. Ver habitaciones");
        System.out.println("  2. Gestionar reservas");
        System.out.println("  3. Registrar pasajero");
        System.out.println("  4. Check-in / Check-out");
        System.out.println("  5. Consumos");
        System.out.println("  6. Cerrar sesion");

        mostrarSeparador();

        int opcion = Teclado.leerOpcion("  Seleccione una opcion: ", 1, 6);

        switch (opcion) {
            case 1:
                MenuHabitaciones.consultarHabitaciones(hotel);
                break;
            case 2:
                menuReservas(hotel);
                break;
            case 3:
                registrarPasajero(hotel);
                break;
            case 4:
                menuCheckInCheckOut(hotel);
                break;
            case 5:
                menuConsumos(hotel);
                break;
            case 6:
                mostrarExito("Sesion cerrada correctamente.");
                return false;
        }
        return true;
    }

    // ==================== RESERVAS ====================

    private static void menuReservas(Hotel hotel) {
        int opcion = 0;

        while (opcion != 5) {
            mostrarEncabezado("GESTIONAR RESERVAS");

            System.out.println("  1. Consultar disponibilidad de una habitacion");
            System.out.println("  2. Crear reserva");
            System.out.println("  3. Cancelar reserva");
            System.out.println("  4. Listar reservas pendientes");
            System.out.println("  5. Volver");

            mostrarSeparador();

            opcion = Teclado.leerOpcion("  Seleccione una opcion: ", 1, 5);

            switch (opcion) {
                case 1:
                    consultarDisponibilidad(hotel);
                    break;
                case 2:
                    crearReserva(hotel);
                    break;
                case 3:
                    cancelarReserva(hotel);
                    break;
                case 4:
                    listarReservasPendientes(hotel);
                    break;
            }
        }
    }

    private static void consultarDisponibilidad(Hotel hotel) {
        mostrarEncabezado("CONSULTAR DISPONIBILIDAD");

        int numero = Teclado.leerEntero("Numero de habitacion: ");
        if (!existeHabitacion(hotel, numero)) {
            return;
        }

        boolean seguir = true;

        while (seguir) {
            LocalDate desde = Teclado.leerFecha("Fecha desde (dd/MM/aaaa): ");
            LocalDate hasta = leerFechaPosterior("Fecha hasta (dd/MM/aaaa): ", desde);

            try {
                if (hotel.consultarDisponibilidad(numero, desde, hasta)) {
                    mostrarExito("La habitacion " + numero + " esta disponible del "
                            + desde.format(FORMATO_FECHA) + " al " + hasta.format(FORMATO_FECHA) + ".");
                    seguir = false;
                } else {
                    mostrarInfo("La habitacion " + numero + " no esta disponible en esas fechas.");
                    seguir = Teclado.leerSiNo("¿Consultar otras fechas? (S/N): ");
                }
            } catch (AccesoDenegadoException | ElementoNoEncontradoException | PeriodoInvalidoException e) {
                mostrarError(e.getMessage());
                seguir = false;
            }
        }
    }

    private static void crearReserva(Hotel hotel) {
        mostrarEncabezado("CREAR RESERVA");

        int dni = leerDni("DNI del pasajero: ");
        if (!existePasajero(hotel, dni)) {
            return;
        }

        int numero = Teclado.leerEntero("Numero de habitacion: ");
        if (!existeHabitacion(hotel, numero)) {
            return;
        }

        int cantidadPersonas = Teclado.leerEntero("Cantidad de personas: ");

        boolean seguir = true;

        while (seguir) {
            LocalDate desde = Teclado.leerFecha("Fecha desde (dd/MM/aaaa): ");
            LocalDate hasta = leerFechaPosterior("Fecha hasta (dd/MM/aaaa): ", desde);

            try {
                hotel.crearReserva(dni, numero, desde, hasta, cantidadPersonas);
                mostrarExito("Reserva creada correctamente.");
                mostrarNumeroDeReserva(hotel, numero, desde);
                seguir = false;

            } catch (HabitacionNoDisponibleException e) {
                mostrarError("La habitacion " + numero + " no esta disponible en esas fechas.");
                seguir = Teclado.leerSiNo("¿Probar con otras fechas? (S/N): ");

            } catch (PeriodoInvalidoException e) {
                mostrarError("Fechas invalidas: la reserva no puede empezar antes de hoy.");
                seguir = Teclado.leerSiNo("¿Probar con otras fechas? (S/N): ");

            } catch (CapacidadExcedidaException e) {
                mostrarError("La habitacion " + numero + " no tiene capacidad para "
                        + cantidadPersonas + " personas.");
                seguir = false;

            } catch (AccesoDenegadoException | ElementoNoEncontradoException
                     | ElementoDuplicadoException | PersistenciaException e) {
                mostrarError(e.getMessage());
                seguir = false;
            }
        }
    }

    /**
     * Muestra el numero de la reserva recien creada, que es el que se pide
     * despues para cancelarla o para hacer el check-in.
     */
    private static void mostrarNumeroDeReserva(Hotel hotel, int numeroHabitacion, LocalDate desde) {
        try {
            for (Reserva reserva : hotel.listarReservasPendientes()) {
                if (reserva.getHabitacion().getId() == numeroHabitacion
                        && reserva.getFechaDesde().equals(desde)) {
                    mostrarInfo("Numero de reserva: " + reserva.getId()
                            + "  (guardelo: se pide para el check-in)");
                }
            }
        } catch (AccesoDenegadoException e) {
            mostrarError(e.getMessage());
        }
    }

    private static void cancelarReserva(Hotel hotel) {
        mostrarEncabezado("CANCELAR RESERVA");

        int idReserva = Teclado.leerEntero("Numero de reserva: ");

        try {
            hotel.cancelarReserva(idReserva);
            mostrarExito("Reserva " + idReserva + " cancelada correctamente.");

        } catch (ElementoNoEncontradoException e) {
            mostrarError("No existe la reserva numero " + idReserva + ".");

        } catch (ReservaInvalidaException e) {
            mostrarError("Solo se pueden cancelar reservas pendientes.");

        } catch (AccesoDenegadoException | PersistenciaException e) {
            mostrarError(e.getMessage());
        }
    }

    private static void listarReservasPendientes(Hotel hotel) {
        mostrarEncabezado("RESERVAS PENDIENTES");

        try {
            List<Reserva> pendientes = hotel.listarReservasPendientes();

            if (pendientes.isEmpty()) {
                mostrarInfo("No hay reservas pendientes.");
            } else {
                for (Reserva reserva : pendientes) {
                    System.out.println("  " + describirReserva(reserva));
                }
            }
        } catch (AccesoDenegadoException e) {
            mostrarError(e.getMessage());
        }
    }

    // ==================== PASAJEROS ====================

    private static void registrarPasajero(Hotel hotel) {
        mostrarEncabezado("REGISTRAR PASAJERO");

        int dni = leerDni("DNI: ");
        String nombre = Teclado.leerLetras("Nombre: ");
        String apellido = Teclado.leerLetras("Apellido: ");
        String origen = Teclado.leerLetras("Origen: ");
        String domicilio = Teclado.leerTexto("Domicilio de origen: ");
        String telefono = Teclado.leerTelefono("Telefono: ");
        String email = Teclado.leerEmail("Email: ");

        Pasajero pasajero = new Pasajero(dni, nombre, apellido, origen, domicilio, telefono, email);

        try {
            hotel.registrarPasajero(pasajero);
            mostrarExito("Pasajero registrado correctamente.");

        } catch (ElementoDuplicadoException e) {
            mostrarError("Ya existe un pasajero con el DNI " + dni + ".");

        } catch (AccesoDenegadoException | PersistenciaException e) {
            mostrarError(e.getMessage());
        }
    }

    // ==================== CHECK-IN / CHECK-OUT ====================

    private static void menuCheckInCheckOut(Hotel hotel) {
        int opcion = 0;

        while (opcion != 5) {
            mostrarEncabezado("CHECK-IN / CHECK-OUT");

            System.out.println("  1. Check-in con reserva");
            System.out.println("  2. Check-in sin reserva");
            System.out.println("  3. Check-out");
            System.out.println("  4. Ver habitaciones ocupadas");
            System.out.println("  5. Volver");

            mostrarSeparador();

            opcion = Teclado.leerOpcion("  Seleccione una opcion: ", 1, 5);

            switch (opcion) {
                case 1:
                    checkInConReserva(hotel);
                    break;
                case 2:
                    checkInSinReserva(hotel);
                    break;
                case 3:
                    checkOut(hotel);
                    break;
                case 4:
                    listarOcupacionesActivas(hotel);
                    break;
            }
        }
    }

    private static void checkInConReserva(Hotel hotel) {
        mostrarEncabezado("CHECK-IN CON RESERVA");

        listarReservasPendientes(hotel);
        int idReserva = Teclado.leerEntero("Numero de reserva: ");

        try {
            hotel.hacerCheckIn(idReserva);
            mostrarExito("Check-in realizado correctamente.");

        } catch (ElementoNoEncontradoException e) {
            mostrarError("No existe la reserva numero " + idReserva + ".");

        } catch (ReservaInvalidaException e) {
            mostrarError("La reserva " + idReserva + " no esta pendiente, o hoy no esta dentro de sus fechas.");

        } catch (HabitacionNoDisponibleException e) {
            mostrarError("La habitacion de esa reserva no esta disponible en este momento.");

        } catch (AccesoDenegadoException | ElementoDuplicadoException | PersistenciaException e) {
            mostrarError(e.getMessage());
        }
    }

    private static void checkInSinReserva(Hotel hotel) {
        mostrarEncabezado("CHECK-IN SIN RESERVA");

        int dni = leerDni("DNI del pasajero: ");
        if (!existePasajero(hotel, dni)) {
            return;
        }

        int numero = Teclado.leerEntero("Numero de habitacion: ");
        if (!existeHabitacion(hotel, numero)) {
            return;
        }

        LocalDate hasta = leerFechaPosterior("Fecha de salida (dd/MM/aaaa): ", LocalDate.now());
        int cantidadPersonas = Teclado.leerEntero("Cantidad de personas: ");

        try {
            hotel.hacerCheckInSinReserva(dni, numero, hasta, cantidadPersonas);
            mostrarExito("Check-in realizado correctamente.");

        } catch (HabitacionNoDisponibleException e) {
            mostrarError("La habitacion " + numero + " no esta disponible: esta ocupada, no disponible"
                    + " o tiene una reserva en esos dias.");

        } catch (CapacidadExcedidaException e) {
            mostrarError("La habitacion " + numero + " no tiene capacidad para "
                    + cantidadPersonas + " personas.");

        } catch (AccesoDenegadoException | ElementoNoEncontradoException | ElementoDuplicadoException
                 | ReservaInvalidaException | PeriodoInvalidoException | PersistenciaException e) {
            mostrarError(e.getMessage());
        }
    }

    private static void checkOut(Hotel hotel) {
        mostrarEncabezado("CHECK-OUT");

        int numero = Teclado.leerEntero("Numero de habitacion: ");

        Ocupacion ocupacion = buscarOcupacionActiva(hotel, numero);
        if (ocupacion == null) {
            mostrarError("La habitacion " + numero + " no tiene un check-in activo.");
            return;
        }

        // Antes de cobrar se muestra el detalle de lo que se va a pagar
        mostrarDetalleCuenta(ocupacion);

        MetodoDePago pago = leerMetodoDePago();

        try {
            hotel.hacerCheckOut(numero, pago);
            mostrarExito("Check-out realizado. Total cobrado: $" + ocupacion.getTotal()
                    + " (" + pago + ")");

        } catch (AccesoDenegadoException | ElementoNoEncontradoException
                 | ReservaInvalidaException | PersistenciaException e) {
            mostrarError(e.getMessage());
        }
    }

    private static void listarOcupacionesActivas(Hotel hotel) {
        mostrarEncabezado("HABITACIONES OCUPADAS");

        try {
            List<Ocupacion> activas = hotel.listarOcupacionesActivas();

            if (activas.isEmpty()) {
                mostrarInfo("No hay habitaciones ocupadas.");
            } else {
                for (Ocupacion ocupacion : activas) {
                    System.out.println("  " + describirOcupacion(ocupacion));
                }
            }
        } catch (AccesoDenegadoException e) {
            mostrarError(e.getMessage());
        }
    }

    // ==================== CONSUMOS ====================

    private static void menuConsumos(Hotel hotel) {
        int opcion = 0;

        while (opcion != 3) {
            mostrarEncabezado("CONSUMOS");

            System.out.println("  1. Registrar consumo");
            System.out.println("  2. Ver consumos de una habitacion");
            System.out.println("  3. Volver");

            mostrarSeparador();

            opcion = Teclado.leerOpcion("  Seleccione una opcion: ", 1, 3);

            switch (opcion) {
                case 1:
                    registrarConsumo(hotel);
                    break;
                case 2:
                    verConsumos(hotel);
                    break;
            }
        }
    }

    private static void registrarConsumo(Hotel hotel) {
        mostrarEncabezado("REGISTRAR CONSUMO");

        int numero = Teclado.leerEntero("Numero de habitacion: ");

        // Solo se cargan consumos en habitaciones que existen y tienen un check-in activo
        if (!existeHabitacion(hotel, numero)) {
            return;
        }
        if (buscarOcupacionActiva(hotel, numero) == null) {
            mostrarError("La habitacion " + numero + " no tiene un check-in activo.");
            return;
        }

        String descripcion = Teclado.leerTexto("Descripcion del consumo: ");
        double monto = Teclado.leerMonto("Monto: ");

        Consumo consumo = new Consumo(0, descripcion, monto, LocalDate.now());

        try {
            hotel.registrarConsumo(numero, consumo);
            mostrarExito("Consumo registrado correctamente.");

        } catch (AccesoDenegadoException | ElementoNoEncontradoException
                 | ReservaInvalidaException | PersistenciaException e) {
            mostrarError(e.getMessage());
        }
    }

    private static void verConsumos(Hotel hotel) {
        mostrarEncabezado("CONSUMOS DE UNA HABITACION");

        int numero = Teclado.leerEntero("Numero de habitacion: ");

        Ocupacion ocupacion = buscarOcupacionActiva(hotel, numero);
        if (ocupacion == null) {
            mostrarError("La habitacion " + numero + " no tiene un check-in activo.");
            return;
        }

        mostrarDetalleCuenta(ocupacion);
    }

    // ==================== METODOS DE AYUDA ====================

    /** Pide un DNI valido (7 u 8 digitos, con o sin puntos) y lo devuelve como numero. */
    private static int leerDni(String mensaje) {
        String dni = Teclado.leerDni(mensaje);
        return Integer.parseInt(dni.trim().replace(".", ""));
    }

    /** Pide una fecha hasta que sea posterior a la fecha indicada. */
    private static LocalDate leerFechaPosterior(String mensaje, LocalDate fechaMinima) {
        while (true) {
            LocalDate fecha = Teclado.leerFecha(mensaje);

            if (fecha.isAfter(fechaMinima)) {
                return fecha;
            }
            mostrarError("La fecha debe ser posterior al " + fechaMinima.format(FORMATO_FECHA) + ".");
        }
    }

    private static MetodoDePago leerMetodoDePago() {
        MetodoDePago[] metodos = MetodoDePago.values();

        System.out.println("Metodo de pago:");
        for (int i = 0; i < metodos.length; i++) {
            System.out.println("  " + (i + 1) + ". " + metodos[i]);
        }

        int opcion = Teclado.leerOpcion("Opcion: ", 1, metodos.length);
        return metodos[opcion - 1];
    }

    /** Muestra las noches, cada consumo y el total que se cobraria hoy. */
    private static void mostrarDetalleCuenta(Ocupacion ocupacion) {
        long noches = ChronoUnit.DAYS.between(ocupacion.getFechaCheckIn(), LocalDate.now());
        if (noches < 1) {
            noches = 1;
        }
        double precioNoche = ocupacion.getHabitacion().calcularPrecioPorNoche();

        System.out.println("  Habitacion " + ocupacion.getHabitacion().getId()
                + " | " + ocupacion.getTitular().getApellido() + ", " + ocupacion.getTitular().getNombre());
        System.out.println("  Estadia: " + noches + " noche(s) x $" + precioNoche + " = $" + (noches * precioNoche));

        if (ocupacion.getConsumos().isEmpty()) {
            System.out.println("  Consumos: ninguno");
        } else {
            System.out.println("  Consumos:");
            for (Consumo consumo : ocupacion.getConsumos()) {
                System.out.println("    - " + consumo.getFecha().format(FORMATO_FECHA)
                        + "  " + consumo.getDescripcion() + "  $" + consumo.getMonto());
            }
        }
        System.out.println("  TOTAL: $" + ocupacion.calcularTotal());
        mostrarSeparador();
    }

    /** Avisa si la habitacion no existe, antes de pedir el resto de los datos. */
    private static boolean existeHabitacion(Hotel hotel, int numero) {
        try {
            hotel.buscarHabitacion(numero);
            return true;
        } catch (ElementoNoEncontradoException e) {
            mostrarError("No existe la habitacion numero " + numero + ".");
        } catch (AccesoDenegadoException e) {
            mostrarError(e.getMessage());
        }
        return false;
    }

    /** Avisa si el pasajero no existe, antes de pedir el resto de los datos. */
    private static boolean existePasajero(Hotel hotel, int dni) {
        try {
            hotel.buscarPasajero(dni);
            return true;
        } catch (ElementoNoEncontradoException e) {
            mostrarError("No existe un pasajero con el DNI " + dni + ". Registrelo primero.");
        } catch (AccesoDenegadoException e) {
            mostrarError(e.getMessage());
        }
        return false;
    }

    /** Devuelve la ocupacion activa de la habitacion, o null si no tiene. */
    private static Ocupacion buscarOcupacionActiva(Hotel hotel, int numeroHabitacion) {
        try {
            for (Ocupacion ocupacion : hotel.listarOcupacionesActivas()) {
                if (ocupacion.getHabitacion().getId() == numeroHabitacion) {
                    return ocupacion;
                }
            }
        } catch (AccesoDenegadoException e) {
            mostrarError(e.getMessage());
        }
        return null;
    }

    private static String describirReserva(Reserva reserva) {
        return "Reserva " + reserva.getId()
                + " | Habitacion " + reserva.getHabitacion().getId()
                + " | " + reserva.getPasajero().getApellido() + ", " + reserva.getPasajero().getNombre()
                + " (DNI " + reserva.getPasajero().getId() + ")"
                + " | del " + reserva.getFechaDesde().format(FORMATO_FECHA)
                + " al " + reserva.getFechaHasta().format(FORMATO_FECHA)
                + " | " + reserva.getCantidadPersonas() + " personas";
    }

    private static String describirOcupacion(Ocupacion ocupacion) {
        return "Habitacion " + ocupacion.getHabitacion().getId()
                + " | " + ocupacion.getTitular().getApellido() + ", " + ocupacion.getTitular().getNombre()
                + " (DNI " + ocupacion.getTitular().getId() + ")"
                + " | desde " + ocupacion.getFechaCheckIn().format(FORMATO_FECHA)
                + " | consumos: " + ocupacion.getConsumos().size()
                + " | a pagar hoy: $" + ocupacion.calcularTotal();
    }
}
