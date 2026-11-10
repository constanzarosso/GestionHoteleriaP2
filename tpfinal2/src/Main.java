import ConexionBD.*;
import Excepciones.*;
import Validaciones.Teclado;
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
        try{
            GestorHabitacionesBD.createTable();
            GestorPasajerosBD.createTable();
            GestorUsuarioBD.createTable();
            GestorReservasBD.createTable();
            GestorOcupacionesBD.createTable();
            GestorBackupBD.createTable();
            System.out.println("Tablas creadas");
        } catch (PersistenciaException e) {
            System.out.println(e.getMessage());
        }

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

        //________________________________________________________________________________________________//

        Hotel hotel = new Hotel("Hotel UTN");
        int contador=0;

        while(contador!=4){
            try{
                    contador++;
                    System.out.println("\n============= LOGIN HOTEL UTN ===============");
                    String usuario = Teclado.leerNombreUsuario("Usuario: ");
                    String contrasenia = Teclado.leerContrasenia("Contraseña: ");

                    hotel.iniciarSesion(usuario,contrasenia);

                    switch(hotel.getUsuarioActual().getTipoUsuario()){
                        case ADMINISTRADOR:
                            System.out.println("Bienvenido administrador");
                            break;


                        case RECEPCIONISTA:
                            System.out.println("Bienvenido recepcionista");

                            break;
                    }

                hotel.cerrarSesion();
            }catch (CredencialesInvalidasException | ContraseniaInvalidaException | NombreUsuarioInvalidoException | UsuarioInactivoException e){
                System.out.println(e.getMessage());
            }

            if(contador==4){
                System.out.println("\n========================================" +
                        "\nExceso de intentos permitidos para iniciar sesion, contacte al administrador." +
                        "\n========================================");
            }
        }

    }
}
