import ConexionBD.*;
import ConexionBD.GestorBD.InicializadorBD;
import Excepciones.ExcepcionesDB.*;
import Excepciones.ExcepcionesDatos.ElementoDuplicadoException;
import Validaciones.Teclado;
import models.Hotel;
import models.*;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        //SOLO PROBE LA CONEXION//
        /*try{
            Conexion.getConnection();
            System.out.println("Conectado");
            Conexion.closeConnection();
        }catch (PersistenciaException e){
            System.out.println(e.getMessage());
        }
         */
        //PROBE SI FUNCIONABAN LAS TABLAS//


        //PROBE INSERTAR Y LISTAR HABITACIONES//

        /*try {
            GestorHabitacionesBD.insertHabitacion(new HabitacionSimple(101, 1, 1, 50000));
            GestorHabitacionesBD.insertHabitacion(new HabitacionDoble(205, 2, 2, 50000, true));
            GestorHabitacionesBD.insertHabitacion(new HabitacionSuite(401, 4, 2, 50000, true));

            for (Habitacion h : GestorHabitacionesBD.listar()) {
                System.out.println(h);
            }
        } catch (PersistenciaException e) {
            System.out.println(e.getMessage());
        }*/

        //___________________________________________________ Inicio _____________________________________________//
        try {

            InicializadorBD.inicializar();

            Hotel hotel = new Hotel("Hotel UTN");
            int contador=0;

            while(contador<5){
                try{
                        boolean cerrarSesion = false;
                        contador++;
                        System.out.println("\n============= LOGIN HOTEL UTN ===============");
                        String usuario = Teclado.leerNombreUsuario("Usuario: ");
                        String contrasenia = Teclado.leerContrasenia("Contraseña: ");

                        hotel.iniciarSesion(usuario,contrasenia);


                        while(!cerrarSesion){
                            switch(hotel.getUsuarioActual().getTipoUsuario()){
                                case ADMINISTRADOR:
                                    System.out.println("\n===== MENU ADMINISTRADOR =====");
                                    System.out.println("1. Gestionar usuarios");
                                    System.out.println("2. Gestionar habitaciones");
                                    System.out.println("3. Gestionar reservas");
                                    System.out.println("4. Realizar backup");
                                    System.out.println("5. Cambiar contraseña");
                                    System.out.println("6. Cerrar sesión");


                                    int opcionAdmin = Teclado.leerEntero("Opcion: ");

                                    switch(opcionAdmin){
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
                                    break;
                                     default:
                                    System.out.println("Opción incorrecta");
                                }
                                break;


                                case RECEPCIONISTA:
                                    System.out.println("Bienvenido recepcionista");






















                                    break;
                            }

                        }


                    System.out.println("Creando un Usuario: ");
                    Recepcionista recepcionista = new Recepcionista(2,"recepcion","Recepcion123!");
                    hotel.crearUsuario(recepcionista);



                    hotel.cerrarSesion();
                }catch (CredencialesInvalidasException | ContraseniaInvalidaException | NombreUsuarioInvalidoException |
                        UsuarioInactivoException | ElementoDuplicadoException | AccesoDenegadoException e){
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
