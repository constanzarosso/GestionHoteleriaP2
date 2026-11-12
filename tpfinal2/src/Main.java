import ConexionBD.*;
import ConexionBD.GestorBD.InicializadorBD;
import Excepciones.ExcepcionesBD.*;
import Menus.MenuAdministrador;
import Menus.MenuRecepcionista;
import Validaciones.Teclado;
import models.Hotel;
import models.*;


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
                                cerrarSesion = MenuAdministrador.mostrar(hotel);
                                break;
                            case RECEPCIONISTA:
                                cerrarSesion = MenuRecepcionista.mostrar(hotel);
                                break;
                        }
                    }
                    contador = 0;

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
