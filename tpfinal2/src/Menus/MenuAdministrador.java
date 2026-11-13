package Menus;

import Excepciones.ExcepcionesBD.AccesoDenegadoException;
import Excepciones.ExcepcionesBD.ContraseniaInvalidaException;
import Excepciones.ExcepcionesBD.PersistenciaException;
import Excepciones.ExcepcionesDatos.ElementoNoEncontradoException;
import Validaciones.Teclado;
import models.Backup;
import models.Hotel;

import java.util.List;

import static Menus.Consola.*;

/**
 * Menu del administrador. El codigo de cada opcion es el que estaba en el Main.
 */
public final class MenuAdministrador {

    private MenuAdministrador() {
    }

    /**
     * Muestra el menu una vez y ejecuta la opcion elegida.
     * @return true si el usuario eligio cerrar sesion
     */
    public static boolean mostrar(Hotel hotel) throws ContraseniaInvalidaException, PersistenciaException, AccesoDenegadoException {
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

            case 4:
                gestionarBackups(hotel);
                break;

            case 5:
                cambiarContrasenia(hotel);
                break;

            case 6:
                mostrarExito("Sesion cerrada correctamente.");
                return true;

            default:
                mostrarError("Opcion incorrecta. Intente nuevamente.");
                break;
        }
        return false;
    }

    // ==================== BACKUP ====================

    private static void gestionarBackups(Hotel hotel) throws PersistenciaException, AccesoDenegadoException {
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
    }

    // ==================== CAMBIAR CONTRASENIA ====================

    private static void cambiarContrasenia(Hotel hotel) {
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

                return;
            }

            hotel.cambiarContrasenia(actual, nueva);

            mostrarExito("Contrasenia modificada correctamente.");

        } catch (ElementoNoEncontradoException
                 | ContraseniaInvalidaException
                 | PersistenciaException
                 | AccesoDenegadoException e) {

            mostrarError("Error al cambiar la contrasenia: " + e.getMessage());
        }
    }
}