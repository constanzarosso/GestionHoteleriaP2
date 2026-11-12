import ConexionBD.*;
import ConexionBD.GestorBD.InicializadorBD;
import Excepciones.ExcepcionesBD.*;
import Excepciones.ExcepcionesDatos.ElementoNoEncontradoException;
import Menus.MenuHabitaciones;
import Menus.MenuRecepcionista;
import Menus.MenuUsuarios;
import Validaciones.Teclado;
import models.Hotel;
import models.*;
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
                                        MenuUsuarios.menu(hotel);
                                        break;
                                    case 2:
                                        MenuHabitaciones.menuAdministrador(hotel);
                                        break;
                                    case 3:
                                        System.out.println("Gestionar reservas");
                                        break;
                                    case 4:
                                        System.out.println("Realizar backup");
                                        break;
                                    case 5:
                                        System.out.println("\n ====== CAMBIAR CONTRASEÑA =====");
                                        try {
                                            String actual = Teclado.leerContrasenia("Ingrese la contraseña actual: ");
                                            String nueva = Teclado.leerContrasenia("Ingrese la nueva contraseña: ");
                                            String confirmar = Teclado.leerContrasenia("Confirme la nueva contraseña: ");

                                            if (!nueva.equals(confirmar)) {
                                                System.out.println("Las contraseñas no coinciden");
                                                break;
                                            }

                                            hotel.cambiarContrasenia(actual, nueva);
                                            System.out.println("Contraseña cambiada con exito.");

                                        } catch (ElementoNoEncontradoException | ContraseniaInvalidaException |
                                                 PersistenciaException |
                                                 AccesoDenegadoException e) {
                                            System.out.println("Error al cambiar la contraseña: " + e.getMessage());
                                        }
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
                                        MenuRecepcionista.verHabitaciones(hotel);
                                        break;

                                    case 2:
                                        MenuRecepcionista.gestionarReservas(hotel);
                                        break;

                                    case 3:
                                        MenuRecepcionista.registrarPasajero(hotel);
                                        break;

                                    case 4:
                                        MenuRecepcionista.checkInCheckOut(hotel);
                                        break;

                                    case 5:
                                        MenuRecepcionista.registrarConsumo(hotel);
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
