package Menus;

import Validaciones.Teclado;
import Validaciones.Validacion;
import enums.MetodoDePago;
import gestoras.GestorHabitaciones;
import models.Consumo;
import models.Hotel;
import models.Pasajero;

import java.time.LocalDate;

public final class MenuRecepcionista {

    private MenuRecepcionista() {
    }

    public static boolean mostrar(Hotel hotel) {
        System.out.println("Bienvenido recepcionista");

        System.out.println("\n====== MENU RECEPCIONISTA =====");
        System.out.println("1. Ver Habitaciones");
        System.out.println("2. Gestionar Reservas");
        System.out.println("3. Registrar Pasajeros");
        System.out.println("4. Check-in / Check-out");
        System.out.println("5. Registrar Consumos");
        System.out.println("6. Cerrar Sesion");

        int opcionRecepcion = Teclado.leerEntero("Opcion: ");

        switch (opcionRecepcion) {

            case 1:
                MenuHabitaciones.consultarHabitaciones(hotel);
                break;

            case 2:
                gestionarReservas(hotel);
                break;

            case 3:
                registrarPasajero(hotel);
                break;

            case 4:
                checkInCheckOut(hotel);
                break;

            case 5:
                registrarConsumo(hotel);
                break;

            case 6:
                System.out.println("Cerrando sesion...");
                return false;

            default:
                System.out.println("Opcion incorrecta.");
                break;
        }
        return true;
    }

    private static void consultarDisponibilidad(Hotel hotel) {


      //  MenuHabitaciones.consultarHabitaciones(hotel);


        try {
            System.out.println("Consultar disponibilidad de habitacion");

            int numero = Teclado.leerEntero("Numero de habitacion: ");

            LocalDate desde = LocalDate.parse(Teclado.leerTexto("Fecha desde (AAAA-MM-DD): "));

            LocalDate hasta = LocalDate.parse(Teclado.leerTexto("Fecha hasta (AAAA-MM-DD): "));

            boolean disponible = hotel.consultarDisponibilidad(
                    numero, desde, hasta);

            if (disponible) {
                System.out.println("La habitacion se encuentra disponible.");
            } else {
                System.out.println("La habitacion no se encuentra disponible.");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void gestionarReservas(Hotel hotel) {

        try {
            System.out.println("\n====== GESTIONAR RESERVAS =====");
            System.out.println("1. Crear reserva");
            System.out.println("2. Cancelar reserva");

            int opcionReserva = Teclado.leerEntero("Opcion: ");

            switch (opcionReserva) {

                case 1:
                        int dni = Teclado.leerEntero("DNI: ");

                        ///Validar que exista la habitacion
                    int numeroHabitacion =
                            Teclado.leerEntero("Numero de habitacion: ");



                    ///Validar las fechas
                    LocalDate fechaDesde =
                            LocalDate.parse(Teclado.leerTexto("Fecha desde (AAAA-MM-DD): "));

                    LocalDate fechaHasta =
                            LocalDate.parse(Teclado.leerTexto("Fecha hasta (AAAA-MM-DD): "));

                    /// Validar las cantidad de personas que no superen el limite de la habitacion segun su numero de habitacion
                    int cantidadPersonas =
                            Teclado.leerEntero("Cantidad de personas: ");

                    hotel.crearReserva(
                            dni,
                            numeroHabitacion,
                            fechaDesde,
                            fechaHasta,
                            cantidadPersonas
                    );

                    System.out.println("Reserva creada correctamente.");
                    break;

                case 2:
                    int idReserva =
                            Teclado.leerEntero("ID de la reserva: ");

                    hotel.cancelarReserva(idReserva);

                    System.out.println("Reserva cancelada correctamente.");
                    break;

                default:
                    System.out.println("Opcion incorrecta.");
                    break;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void registrarPasajero(Hotel hotel) {

        try {
            System.out.println("====== REGISTRAR PASAJERO ======");

            int dni = Integer.parseInt(Teclado.leerDni("Ingrese DNI: "));


            String nombre = Teclado.leerLetras("Nombre: ");

            String apellido = Teclado.leerLetras("Apellido: ");

            String origen = Teclado.leerLetras("Origen: ");

            String domicilio = Teclado.leerTexto("Domicilio: ");

            String telefono = Teclado.leerTelefono("Ingrese Telefono: ");

            String email = Teclado.leerEmail("Email: ");

            Pasajero pasajero = new Pasajero(
                    dni,
                    nombre,
                    apellido,
                    origen,
                    domicilio,
                    telefono,
                    email
            );

            hotel.registrarPasajero(pasajero);
            System.out.println("Pasajero registrado correctamente.");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void checkInCheckOut(Hotel hotel) {

        try {
            System.out.println("\n====== CHECK-IN / CHECK-OUT =====");
            System.out.println("1. Check-in con reserva");
            System.out.println("2. Check-in sin reserva");
            System.out.println("3. Check-out");

            int opcionCheck = Teclado.leerEntero("Opcion: ");

            switch (opcionCheck) {

                case 1:
                    int idReserva =
                            Teclado.leerEntero("ID de la reserva: ");

                    hotel.hacerCheckIn(idReserva);

                    System.out.println("Check-in realizado correctamente.");
                    break;

                case 2:
                    int dni = Teclado.leerEntero("DNI del pasajero: ");

                    int numero = Teclado.leerEntero(
                            "Numero de habitacion: ");

                    LocalDate fechaHasta =
                            LocalDate.parse(Teclado.leerTexto("Fecha hasta (AAAA-MM-DD): "));

                    int personas = Teclado.leerEntero(
                            "Cantidad de personas: ");

                    hotel.hacerCheckInSinReserva(
                            dni,
                            numero,
                            fechaHasta,
                            personas
                    );

                    System.out.println("Check-in realizado correctamente.");
                    break;

                case 3:
                    int numeroHabitacion = Teclado.leerEntero(
                            "Numero de habitacion: ");

                    System.out.println("Metodo de pago:");
                    System.out.println("1. EFECTIVO");
                    System.out.println("2. DEBITO");
                    System.out.println("3. CREDITO");
                    System.out.println("4. TRANSFERENCIA");

                    int opcionPago = Teclado.leerOpcion("Metodo de pago: ", 1, 4);
                    MetodoDePago pago = MetodoDePago.values()[opcionPago - 1];

                    hotel.hacerCheckOut(numeroHabitacion, pago);

                    System.out.println("Check-out realizado correctamente.");
                    break;

                default:
                    System.out.println("Opcion incorrecta.");
                    break;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void registrarConsumo(Hotel hotel) {

        try {
            System.out.println("====== REGISTRAR CONSUMO ======");

            int numeroHabitacion =
                    Teclado.leerEntero("Numero de habitacion: ");

            String descripcion = Teclado.leerTexto("Descripcion del consumo: ");

            double monto = Teclado.leerEntero("Monto: ");

            Consumo consumo = new Consumo(
                    0,
                    descripcion,
                    monto,
                    LocalDate.now()
            );

            hotel.registrarConsumo(numeroHabitacion, consumo);

            System.out.println("Consumo registrado correctamente.");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
