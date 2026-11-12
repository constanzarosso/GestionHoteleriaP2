package Menus;

import Excepciones.ExcepcionesBD.AccesoDenegadoException;
import Excepciones.ExcepcionesBD.ContraseniaInvalidaException;
import Excepciones.ExcepcionesBD.PersistenciaException;
import Excepciones.ExcepcionesDatos.ElementoNoEncontradoException;
import Validaciones.Teclado;
import models.Hotel;

public final class MenuAdministrador {

    private MenuAdministrador() {
    }

    public static boolean mostrar(Hotel hotel) throws ContraseniaInvalidaException {
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
                cambiarContrasenia(hotel);
                break;
            case 6:
                return true;
            default:
                System.out.println("Opción incorrecta");
        }
        return false;
    }

    private static void cambiarContrasenia(Hotel hotel) {
        System.out.println("\n ====== CAMBIAR CONTRASEÑA =====");
        try {
            String actual = Teclado.leerContrasenia("Ingrese la contraseña actual: ");
            String nueva = Teclado.leerContrasenia("Ingrese la nueva contraseña: ");
            String confirmar = Teclado.leerContrasenia("Confirme la nueva contraseña: ");

            if (!nueva.equals(confirmar)) {
                System.out.println("Las contraseñas no coinciden");
                return;
            }

            hotel.cambiarContrasenia(actual, nueva);
            System.out.println("Contraseña cambiada con exito.");

        } catch (ElementoNoEncontradoException | ContraseniaInvalidaException |
                 PersistenciaException |
                 AccesoDenegadoException e) {
            System.out.println("Error al cambiar la contraseña: " + e.getMessage());
        }
    }
}