import ConexionBD.*;
import ConexionBD.GestorBD.InicializadorBD;
import Excepciones.ExcepcionesBD.*;
import Menus.MenuAdministrador;
import Menus.MenuRecepcionista;
import Validaciones.Teclado;
import models.Hotel;
import models.*;

import java.util.List;
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

                                        int opcionBackup;

                                        do{
                                            System.out.println("\n===== GESTION DE BACKUPS =====");
                                            System.out.println("1. Crear backup");
                                            System.out.println("2. Restaurar backup");
                                            System.out.println("3. Listar backups");
                                            System.out.println("4. Volver");

                                            opcionBackup = Teclado.leerEntero("Opcion: ");

                                            switch (opcionBackup) {
                                                case 1:
                                                        try{
                                                            hotel.crearBackup();
                                                            System.out.println("Backup creado con exito.");
                                                        }catch (PersistenciaException | AccesoDenegadoException e){
                                                            System.out.println("Error al crear backup: "+e.getMessage());
                                                        }
                                                    break;

                                                case 2:
                                                    try{
                                                        int id = Teclado.leerEntero("Ingrese ID del bakcup: ");

                                                        boolean confirmacion = Teclado.leerSiNo("Esto puede reemplazar datos actuales ¿Continuar? SI/NO");
                                                        if(confirmacion){
                                                            hotel.restaurarBackup(id);
                                                            System.out.println("Backup restaurado con exito.");
                                                        }else{
                                                            System.out.println("Restauracion cancelada");
                                                        }
                                                    }catch (PersistenciaException | AccesoDenegadoException e){
                                                        System.out.println("Error al crear backup: "+e.getMessage());
                                                    }
                                                    break;

                                                case 3: //listar backups
                                                    List<Backup> backups = hotel.listarBackups();

                                                    if(backups.isEmpty()){
                                                        System.out.println("No hay backups registrados.");
                                                    }else{
                                                        for(Backup backup : backups){
                                                            System.out.println("--------------------");
                                                            System.out.println("ID: "+backup.getId());
                                                            System.out.println("Fecha: "+backup.getFechaHora());
                                                            System.out.println("Usuario: "+backup.getNombreUsuario());
                                                            System.out.println("Archivo: "+backup.getNombreArchivo());
                                                        }
                                                    }
                                                    break;

                                                case 4:
                                                    System.out.println("Volviendo al menu principal de Admin");
                                                    break;

                                                default:
                                                    System.out.println("Opcion incorrecta. ");
                                                    break;
                                            }
                                        }while(opcionBackup != 4);

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
                } catch (AccesoDenegadoException ex) {
                    System.out.println(ex.getMessage());
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
