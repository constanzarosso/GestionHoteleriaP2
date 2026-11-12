package Menus;

import Validaciones.Teclado;
import enums.MetodoDePago;
import models.Consumo;
import models.Hotel;
import models.Pasajero;

import java.time.LocalDate;
import java.util.Scanner;

public final class MenuRecepcionista {

    private MenuRecepcionista() {
    }

    public static void verHabitaciones(Hotel hotel) {
        Scanner scanner = new Scanner(System.in);

        MenuHabitaciones.consultarHabitaciones(hotel);

        try {
            System.out.println("Consultar disponibilidad de habitacion");

            int numero = Teclado.leerEntero("Numero de habitacion: ");

            System.out.print("Fecha desde (AAAA-MM-DD): ");
            LocalDate desde = LocalDate.parse(scanner.nextLine());

            System.out.print("Fecha hasta (AAAA-MM-DD): ");
            LocalDate hasta = LocalDate.parse(scanner.nextLine());

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

    public static void gestionarReservas(Hotel hotel) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("\n====== GESTIONAR RESERVAS =====");
            System.out.println("1. Crear reserva");
            System.out.println("2. Cancelar reserva");

            int opcionReserva = Teclado.leerEntero("Opcion: ");

            switch (opcionReserva) {

                case 1:
                    int dni = Teclado.leerEntero("DNI del pasajero: ");

                    int numeroHabitacion =
                            Teclado.leerEntero("Numero de habitacion: ");

                    System.out.print("Fecha desde (AAAA-MM-DD): ");
                    LocalDate fechaDesde =
                            LocalDate.parse(scanner.nextLine());

                    System.out.print("Fecha hasta (AAAA-MM-DD): ");
                    LocalDate fechaHasta =
                            LocalDate.parse(scanner.nextLine());

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

    public static void registrarPasajero(Hotel hotel) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("====== REGISTRAR PASAJERO ======");

            int dni = Teclado.leerEntero("DNI: ");

            System.out.print("Nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("Apellido: ");
            String apellido = scanner.nextLine();

            System.out.print("Origen: ");
            String origen = scanner.nextLine();

            System.out.print("Domicilio de origen: ");
            String domicilio = scanner.nextLine();

            System.out.print("Telefono: ");
            String telefono = scanner.nextLine();

            System.out.print("Email: ");
            String email = scanner.nextLine();

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

    public static void checkInCheckOut(Hotel hotel) {
        Scanner scanner = new Scanner(System.in);

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

                    System.out.print("Fecha hasta (AAAA-MM-DD): ");
                    LocalDate fechaHasta =
                            LocalDate.parse(scanner.nextLine());

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
                    System.out.println("EFECTIVO");
                    System.out.println("DEBITO");
                    System.out.println("CREDITO");
                    System.out.println("TRANSFERENCIA");

                    System.out.print("Metodo de pago: ");
                    MetodoDePago pago =
                            MetodoDePago.valueOf(scanner.nextLine().toUpperCase());

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

    public static void registrarConsumo(Hotel hotel) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println("====== REGISTRAR CONSUMO ======");

            int idOcupacion =
                    Teclado.leerEntero("ID de la ocupacion: ");

            System.out.print("Descripcion del consumo: ");
            String descripcion = scanner.nextLine();

            double monto = Teclado.leerEntero("Monto: ");

            Consumo consumo = new Consumo(
                    0,
                    descripcion,
                    monto,
                    LocalDate.now()
            );

            hotel.registrarConsumo(idOcupacion, consumo);

            System.out.println("Consumo registrado correctamente.");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}