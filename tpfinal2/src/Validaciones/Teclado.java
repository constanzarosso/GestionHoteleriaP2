package Validaciones;

import Excepciones.ExcepcionesBD.BDContraseniaInvalidaException;
import Excepciones.ExcepcionesBD.BDEmailInvalidoException;
import Excepciones.ExcepcionesBD.BDNombreUsuarioInvalidoException;
import Excepciones.ExcepcionesDatos.*;
import Excepciones.ExcepcionesHotel.DniInvalidoException;
import Excepciones.ExcepcionesHotel.FechaInvalidaException;
import Excepciones.ExcepcionesHotel.MontoInvalidoException;
import Excepciones.ExcepcionesHotel.TelefonoInvalidoException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public final class Teclado {
    private static final Scanner sc = new Scanner(System.in);

    public static String leerTexto(String mensaje){
        System.out.println(mensaje);

        return sc.nextLine();
    }

    public static int leerOpcion(String mensaje, int minimo, int maximo){
        while(true){
            int opcion = leerEntero(mensaje);

            try{
                Validacion.validarRango(opcion, minimo, maximo);
                return opcion;
            }catch (FueraDeRangoException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static int leerEntero(String mensaje){
        while(true){
            System.out.println(mensaje);
            String texto = sc.nextLine();

            try{
                Validacion.validarEntero(texto);

                return Integer.parseInt(texto);
            }catch (NumeroInvalidoException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static String leerDni(String mensaje){

        while(true){
            System.out.println(mensaje);
            String dni = sc.nextLine();

            try{
                Validacion.validarDni(dni);
                return dni;
            }catch(DniInvalidoException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static String leerEmail(String mensaje){
        while(true){
            System.out.println(mensaje);
            String email = sc.nextLine();
            try{
                Validacion.validarEmail(email);
                return email;
            }catch(BDEmailInvalidoException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static String leerContrasenia(String mensaje) throws BDContraseniaInvalidaException {

            System.out.print(mensaje);
            String contrasenia = sc.nextLine();
            Validacion.validarContrasenia(contrasenia);
            return  contrasenia;


    }

    public static String leerNombreUsuario(String mensaje) throws BDNombreUsuarioInvalidoException {

            System.out.print(mensaje);
            String nombreUsuario = sc.nextLine();
            Validacion.validarNombreUsuario(nombreUsuario);
            return nombreUsuario;

    }

    public static LocalDate leerFecha(String mensaje){
        while(true){
            System.out.println(mensaje);
            String fecha = sc.nextLine();

            try {
                Validacion.validarFecha(fecha);
                DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/uuuu");
                return  LocalDate.parse(fecha, formato);
            }catch (FechaInvalidaException e){
                System.out.println(e.getMessage());
            }
        }

    }

    public static String leerTelefono(String mensaje){
        while(true){
            System.out.println(mensaje);
            String telefono = sc.nextLine();

            try {
                Validacion.validarTelefono(telefono);
                return telefono;
            }catch(TelefonoInvalidoException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static double leerMonto(String mensaje){
        while(true){
            System.out.println(mensaje);
            String texto = sc.nextLine();

            try{
                double monto = Double.parseDouble(texto);
                Validacion.validarMonto(monto);
                return monto;
            }catch (NumberFormatException e){
                System.out.println("Debe ingresar un numero valido.");
            }catch (MontoInvalidoException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static String leerLetras(String mensaje){
        while(true){
            System.out.println(mensaje);
            String texto = sc.nextLine();

            try {
                Validacion.validarNoVacio(texto);
                Validacion.validarSoloLetras(texto);
                return texto;
            }catch (CampoVacioException | TextoInvalidoException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static boolean leerSiNo(String mensaje){
        while(true){
            System.out.println(mensaje);
            String respuesta = sc.nextLine();

            try {
                Validacion.validarSiNo(respuesta);
                return respuesta.equalsIgnoreCase("Si") || respuesta.equalsIgnoreCase("No");
            }catch (TextoInvalidoException e){
                System.out.println(e.getMessage());
            }
        }
    }

}
