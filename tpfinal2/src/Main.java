import ConexionBD.*;
import ConexionBD.GestorBD.InicializadorBD;
import Excepciones.ExcepcionesBD.*;
import Validaciones.Teclado;
import models.Hotel;


public class Main {
    public static void main(String[] args) throws BDNombreUsuarioInvalidoException, BDContraseniaInvalidaException, BDUsuarioInactivoException, BDCredencialesInvalidasException {
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
      /*
        try {
            GestorHabitacionesBD.createTable();
            GestorPasajerosBD.createTable();
            GestorUsuarioBD.createTable();
            GestorReservasBD.createTable();
            GestorOcupacionesBD.createTable();
            GestorBackupBD.createTable();
            System.out.println("Tablas creadas");
        } catch (BDPersistenciaException e) {
            System.out.println(e.getMessage());
        }
*/
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


                            System.out.println("\n ====== MENU RECEPCIONISTA =====");
                            System.out.println("1. Ver Habitaciones");
                            System.out.println("2. Gestionar Reservas");
                            System.out.println("3. Registrar Pasajeros");
                            System.out.println("4. Check-in / Check-out");
                            System.out.println("5. Registrar Consumos");
                            System.out.println("6. Cerrar Sesion");

                            int opcionRecepcion = Teclado.leerEntero("Opcion: ");

                            switch (opcionRecepcion) {
                                case 1:
                                    System.out.println("Ver Habitaciones");
                                    break;

                                case 2:
                                    System.out.println("Registrar Pasajeros");
                                    break;

                                case 3:
                                    System.out.println("Registrar Pasajeroi");
                                    break;

                                case 4:
                                    System.out.println("Checkin Checkout");
                                    break;

                                case 5:
                                    System.out.println("Registrar Consumos");
                                    break;

                                case 6:
                                    System.out.println("Cerrar sesion");
                                    cerrarSesion = true;
                                    contador = 0;
                                    break;

                                default:
                                    System.out.println("Opcion incorrecta");
                                    break;
                            }

                            break;
                    }

                }

                hotel.cerrarSesion();


            } catch (BDCredencialesInvalidasException | BDContraseniaInvalidaException |
                     BDNombreUsuarioInvalidoException |
                     BDUsuarioInactivoException e) {
                contador++;
                System.out.println("\n============= LOGIN HOTEL UTN ===============");
                String usuario = Teclado.leerNombreUsuario("Usuario: ");
                String contrasenia = Teclado.leerContrasenia("Contraseña: ");

                hotel.iniciarSesion(usuario, contrasenia);

                switch (hotel.getUsuarioActual().getTipoUsuario()) {
                    case ADMINISTRADOR:
                        System.out.println("Bienvenido administrador");
                        break;


                    case RECEPCIONISTA:
                        System.out.println("Bienvenido recepcionista");

                        break;
                }

                hotel.cerrarSesion();

                System.out.println("\n========================================" +
                        "\nExceso de intentos permitidos para iniciar sesion, contacte al administrador." +
                        "\n========================================");


            }
        }

    }

}