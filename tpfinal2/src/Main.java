import ConexionBD.*;
import ConexionBD.GestorBD.InicializadorBD;
import Excepciones.ExcepcionesBD.*;
import Validaciones.Teclado;
import enums.MetodoDePago;
import models.Hotel;
import models.*;

import java.time.LocalDate;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        //___________________________________________________ Inicio _____________________________________________//
        try {

            InicializadorBD.inicializar();

            Hotel hotel = new Hotel("Hotel UTN");
            int contador = 0;

            while (contador < 5) {
                try {
                    boolean cerrarSesion = false;
                    contador++;
                    System.out.println("\n============= LOGIN HOTEL UTN ===============");
                    String usuario = Teclado.leerNombreUsuario("Usuario: ");
                    String contrasenia = Teclado.leerContrasenia("Contraseña: ");

                    hotel.iniciarSesion(usuario, contrasenia);


                    while (!cerrarSesion) {
                        switch (hotel.getUsuarioActual().getTipoUsuario()) {
                            case ADMINISTRADOR:
                                System.out.println("\n===== MENU ADMINISTRADOR =====");
                                System.out.println("1. Gestionar usuarios");
                                System.out.println("2. Gestionar habitaciones");
                                System.out.println("3. Gestionar reservas");
                                System.out.println("4. Realizar backup");
                                System.out.println("5. Cambiar contraseña");
                                System.out.println("6. Cerrar sesión");


                                int opcionAdmin = Teclado.leerEntero("Opcion: ");

                                switch (opcionAdmin) {
                                    case 1:
                                        System.out.println("Gestionar usuarios");
                                        break;
                                    case 2:
                                        System.out.println("Gestionar habitaciones");
                                        break;
                                    case 3:
                                        System.out.println("Gestionar reservas");
                                        break;
                                    case 4:
                                        System.out.println("Realizar backup");
                                        break;
                                    case 5:
                                        System.out.println("Cambiar contraseña");
                                        break;
                                    case 6:
                                        cerrarSesion = true;
                                        contador = 0;
                                        break;
                                    default:
                                        System.out.println("Opción incorrecta");
                                }
                                break;


                            case RECEPCIONISTA:


                                System.out.println("Bienvenido recepcionista");

                                Scanner scanner = new Scanner(System.in);

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
                                        break;

                                    case 2:
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
                                        break;

                                    case 3:
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
                                        break;

                                    case 4:
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
                                        break;

                                    case 5:
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
                                        break;

                                    case 6:
                                        System.out.println("Cerrando sesion...");
                                        cerrarSesion = true;
                                        contador = 0;
                                        break;

                                    default:
                                        System.out.println("Opcion incorrecta.");
                                        break;
                                }
                                break;
                        }

                    }

                    hotel.cerrarSesion();


                } catch (CredencialesInvalidasException | ContraseniaInvalidaException |
                         UsuarioInactivoException e) {
                    contador++;
                    System.out.println(e.getMessage());
                }
            }
            System.out.println("\n========================================" +
                    "\nExceso de intentos permitidos para iniciar sesion, contacte al administrador." +
                    "\n========================================");


        } catch (PersistenciaException e) {
            System.out.println(e.getMessage());
        }
    }
}
