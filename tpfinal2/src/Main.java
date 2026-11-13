import ConexionBD.GestorBD.InicializadorBD;
import Excepciones.ExcepcionesBD.*;
import Menus.MenuAdministrador;
import Menus.MenuRecepcionista;
import Validaciones.Teclado;
import models.Hotel;

import static Menus.Consola.*;

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

            System.out.println("  Exceso de intentos permitidos para iniciar sesion.");
            System.out.println("  Contacte al administrador del sistema.");

            mostrarSeparador();

        } catch (PersistenciaException e) {

            mostrarError("Error de persistencia: " + e.getMessage());
        }
    }
}