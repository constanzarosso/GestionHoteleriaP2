import ConexionBD.GestorBD.InicializadorBD;
import Excepciones.ExcepcionesBD.*;
import Menus.MenuAdministrador;
import Menus.MenuRecepcionista;
import Validaciones.Teclado;
import models.Hotel;

import static Menus.Consola.*;

public class Main {
    private static final String RESET = "\u001B[0m";

    private static final String ROSA = "\u001B[38;5;213m";
    private static final String FUCSIA = "\u001B[38;5;201m";
    private static final String LAVANDA = "\u001B[38;5;141m";
    private static final String VIOLETA = "\u001B[38;5;135m";
    private static final String TURQUESA = "\u001B[38;5;87m";
    private static final String DORADO = "\u001B[38;5;220m";
    private static final String BLANCO = "\u001B[38;5;255m";
    private static final String GRIS = "\u001B[38;5;244m";
    private static final String VERDE = "\u001B[38;5;118m";
    private static final String ROJO = "\u001B[38;5;203m";

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

                    String usuario = Teclado.leerNombreUsuario(
                            TURQUESA + "  Usuario       : " + RESET);

                    String contrasenia = Teclado.leerContrasenia(
                            ROSA + "  Contrasenia   : " + RESET);

                    hotel.iniciarSesion(usuario, contrasenia);

                    mostrarExito("Inicio de sesion exitoso.");

                    // ==================== MENUS ====================

                    while (!cerrarSesion) {

                        switch (hotel.getUsuarioActual().getTipoUsuario()) {

                            // ==================== ADMINISTRADOR ====================

                            case ADMINISTRADOR:

                                cerrarSesion = MenuAdministrador.mostrar(hotel);

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

            System.out.println(ROJO + "  Exceso de intentos permitidos para iniciar sesion." + RESET);
            System.out.println(ROSA + "  Contacte al administrador del sistema." + RESET);

            mostrarSeparador();

        } catch (PersistenciaException e) {

            mostrarError("Error de persistencia: " + e.getMessage());
        }
    }
    private static void mostrarEncabezado(String titulo) {
        System.out.println();

        System.out.println(FUCSIA + "╔════════════════════════════════════════════════════════════╗" + RESET);

        System.out.println(FUCSIA + "║" + RESET + "                                                            " + FUCSIA + "║" + RESET);

        System.out.println(FUCSIA + "║" + LAVANDA + "  ██╗  ██╗ ██████╗ ████████╗███████╗██╗                   " + FUCSIA + "║" + RESET);
        System.out.println(FUCSIA + "║" + LAVANDA + "  ██║  ██║██╔═══██╗╚══██╔══╝██╔════╝██║                   " + FUCSIA + "║" + RESET);
        System.out.println(FUCSIA + "║" + LAVANDA + "  ███████║██║   ██║   ██║   █████╗  ██║                   " + FUCSIA + "║" + RESET);
        System.out.println(FUCSIA + "║" + LAVANDA+ "  ██╔══██║██║   ██║   ██║   ██╔══╝  ██║                   " + FUCSIA + "║" + RESET);
        System.out.println(FUCSIA + "║" + LAVANDA + "  ██║  ██║╚██████╔╝   ██║   ███████╗██║                   " + FUCSIA + "║" + RESET);
        System.out.println(FUCSIA + "║" + LAVANDA+ "  ╚═╝  ╚═╝ ╚═════╝    ╚═╝   ╚══════╝╚═╝                   " + FUCSIA + "║" + RESET);

        System.out.println(FUCSIA + "║" + LAVANDA + "                    ✧ CONCARMA HOTEL ✧                    " + FUCSIA + "║" + RESET);

        System.out.println(FUCSIA + "║" + RESET + "                                                            " + FUCSIA + "║" + RESET);

        System.out.println(FUCSIA + "╠════════════════════════════════════════════════════════════╣" + RESET);

        System.out.printf(FUCSIA + "║" + TURQUESA + "  %-58s" + FUCSIA + "║%n" + RESET, titulo);

        System.out.println(FUCSIA + "╚════════════════════════════════════════════════════════════╝" + RESET);

        System.out.println();
    }

        private static void mostrarSeparador() {
            System.out.println(LAVANDA + "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━" + RESET);
        }

        private static void mostrarExito(String mensaje) {
            System.out.println();
            System.out.println(VERDE + "  ✧ " + mensaje + RESET);
            System.out.println();
        }

        private static void mostrarError(String mensaje) {
            System.out.println();
            System.out.println(ROJO + "  ✦ " + mensaje + RESET);
            System.out.println();
        }

        private static void mostrarInfo(String mensaje) {
            System.out.println(TURQUESA + "  ❀ " + mensaje + RESET);
        }
    }
