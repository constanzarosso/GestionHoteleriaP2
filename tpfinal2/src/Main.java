import ConexionBD.*;
import ConexionBD.GestorBD.InicializadorBD;
import Excepciones.ExcepcionesBD.*;
import Excepciones.ExcepcionesDatos.ElementoNoEncontradoException;
import Menus.MenuAdministrador;
import Menus.MenuHabitaciones;
import Menus.MenuRecepcionista;
import Menus.MenuUsuarios;
import Validaciones.Teclado;
import models.Hotel;
import models.*;

import java.util.List;
import java.util.Scanner;


import java.util.List;

public class Main {

    public static void main(String[] args) {

        try {
            InicializadorBD.inicializar();

            Hotel hotel = new Hotel("Hotel UTN");
            int contador = 0;

            while (contador < 5) {

                try {
                    boolean cerrarSesion = false;
                    contador++;

                    // ==================== LOGIN ====================

                    mostrarEncabezado("HOTEL UTN | INICIO DE SESION");

                    String usuario = Teclado.leerNombreUsuario("Usuario       : ");
                    String contrasenia = Teclado.leerContrasenia("Contrasenia   : ");

                    hotel.iniciarSesion(usuario, contrasenia);

                    mostrarExito("Inicio de sesion exitoso.");

                    // ==================== MENUS ====================

                    while (!cerrarSesion) {

                        switch (hotel.getUsuarioActual().getTipoUsuario()) {

                            // ==================== ADMINISTRADOR ====================

                            case ADMINISTRADOR:

                                mostrarEncabezado("MENU ADMINISTRADOR");

                                System.out.println("  1. Gestionar usuarios");
                                System.out.println("  2. Gestionar habitaciones");
                                System.out.println("  3. Gestionar reservas");
                                System.out.println("  4. Realizar backup");
                                System.out.println("  5. Cambiar contrasenia");
                                System.out.println("  6. Cerrar sesion");

                                mostrarSeparador();

                                int opcionAdmin = Teclado.leerEntero("  Seleccione una opcion: ");

                                switch (opcionAdmin) {

                                    case 1:
                                        MenuUsuarios.menu(hotel);
                                        break;

                                    case 2:
                                        MenuHabitaciones.menuAdministrador(hotel);
                                        break;

                                    case 3:
                                        mostrarInfo("Gestionar reservas");
                                        break;

                                    // ==================== BACKUP ====================

                                    case 4:

                                        int opcionBackup;

                                        do {
                                            mostrarEncabezado("GESTION DE BACKUPS");

                                            System.out.println("  1. Crear backup");
                                            System.out.println("  2. Restaurar backup");
                                            System.out.println("  3. Listar backups");
                                            System.out.println("  4. Volver al menu principal");

                                            mostrarSeparador();

                                            opcionBackup = Teclado.leerEntero("  Seleccione una opcion: ");

                                            switch (opcionBackup) {

                                                case 1:

                                                    try {
                                                        hotel.crearBackup();

                                                        mostrarExito("Backup creado correctamente.");

                                                    } catch (PersistenciaException | AccesoDenegadoException e) {

                                                        mostrarError("Error al crear backup: " + e.getMessage());
                                                    }

                                                    break;

                                                case 2:

                                                    try {
                                                        int id = Teclado.leerEntero("  Ingrese el ID del backup: ");

                                                        boolean confirmacion = Teclado.leerSiNo(
                                                                "  Esta operacion puede reemplazar datos actuales. ¿Continuar? SI/NO: "
                                                        );

                                                        if (confirmacion) {

                                                            hotel.restaurarBackup(id);

                                                            mostrarExito("Backup restaurado correctamente.");

                                                        } else {

                                                            mostrarInfo("Restauracion cancelada.");
                                                        }

                                                    } catch (PersistenciaException | AccesoDenegadoException e) {

                                                        mostrarError("Error al restaurar backup: " + e.getMessage());
                                                    }

                                                    break;

                                                case 3:

                                                    List<Backup> backups = hotel.listarBackups();

                                                    if (backups.isEmpty()) {

                                                        mostrarInfo("No hay backups registrados.");

                                                    } else {

                                                        mostrarEncabezado("BACKUPS REGISTRADOS");

                                                        for (Backup backup : backups) {

                                                            mostrarSeparador();

                                                            System.out.println("  ID       : " + backup.getId());
                                                            System.out.println("  Fecha    : " + backup.getFechaHora());
                                                            System.out.println("  Usuario  : " + backup.getNombreUsuario());
                                                            System.out.println("  Archivo  : " + backup.getNombreArchivo());
                                                        }

                                                        mostrarSeparador();
                                                    }

                                                    break;

                                                case 4:

                                                    mostrarInfo("Volviendo al menu administrador.");

                                                    break;

                                                default:

                                                    mostrarError("Opcion incorrecta. Intente nuevamente.");

                                                    break;
                                            }

                                        } while (opcionBackup != 4);

                                        break;

                                    // ==================== CAMBIAR CONTRASENIA ====================

                                    case 5:

                                        mostrarEncabezado("SEGURIDAD | CAMBIAR CONTRASENIA");

                                        try {

                                            String actual = Teclado.leerContrasenia(
                                                    "Contrasenia actual       : "
                                            );

                                            String nueva = Teclado.leerContrasenia(
                                                    "Nueva contrasenia        : "
                                            );

                                            String confirmar = Teclado.leerContrasenia(
                                                    "Confirmar contrasenia    : "
                                            );

                                            if (!nueva.equals(confirmar)) {

                                                mostrarError("Las contrasenias no coinciden.");

                                                break;
                                            }

                                            hotel.cambiarContrasenia(actual, nueva);

                                            mostrarExito("Contrasenia modificada correctamente.");

                                        } catch (ElementoNoEncontradoException
                                                 | ContraseniaInvalidaException
                                                 | PersistenciaException
                                                 | AccesoDenegadoException e) {

                                            mostrarError("Error al cambiar la contrasenia: " + e.getMessage());
                                        }

                                        break;

                                    // ==================== CERRAR SESION ====================

                                    case 6:

                                        cerrarSesion = true;
                                        contador = 0;

                                        mostrarExito("Sesion cerrada correctamente.");

                                        break;

                                    default:

                                        mostrarError("Opcion incorrecta. Intente nuevamente.");

                                        break;
                                }

                                break;

                            // ==================== RECEPCIONISTA ====================

                            case RECEPCIONISTA:

                                cerrarSesion = MenuRecepcionista.mostrar(hotel);

                                break;
                        }
                    }

                    contador = 0;

                    hotel.cerrarSesion();

                } catch (CredencialesInvalidasException
                         | ContraseniaInvalidaException
                         | UsuarioInactivoException e) {

                    mostrarError(e.getMessage());

                } catch (AccesoDenegadoException ex) {

                    mostrarError(ex.getMessage());
                }
            }

            // ==================== FIN DE INTENTOS ====================

            mostrarEncabezado("ACCESO BLOQUEADO");

            System.out.println("  Exceso de intentos permitidos para iniciar sesion.");
            System.out.println("  Contacte al administrador del sistema.");

            mostrarSeparador();

        } catch (PersistenciaException e) {

            mostrarError("Error de persistencia: " + e.getMessage());
        }
    }

    // ==================== METODOS DE ESTETICA ====================

    private static void mostrarEncabezado(String titulo) {

        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════╗");
        System.out.printf("║ %-50s ║%n", titulo);
        System.out.println("╚════════════════════════════════════════════════════╝");
    }

    private static void mostrarSeparador() {

        System.out.println("──────────────────────────────────────────────────────");
    }

    private static void mostrarExito(String mensaje) {

        System.out.println("\n[✓] " + mensaje);
    }

    private static void mostrarError(String mensaje) {

        System.out.println("\n[!] " + mensaje);
    }

    private static void mostrarInfo(String mensaje) {

        System.out.println("\n[i] " + mensaje);
    }
}