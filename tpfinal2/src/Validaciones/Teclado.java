package Validaciones;

import Excepciones.DniInvalidoException;
import Excepciones.EmailInvalidoException;

import java.util.Scanner;

public final class Teclado {
    private static Scanner sc = new Scanner(System.in);

    public static int leerOpcion(int opcion){
        return opcion;
    }

    public static String leerDni(){

        while(true){
            System.out.println("Ingrese DNI: ");
            String dni = sc.nextLine();

            try{
                Validacion.validarDni(dni);
                return dni;
            }catch(DniInvalidoException e){
                System.out.println(e.getMessage());
            }
        }
    }

    public static String leerEmail(){
        while(true){
            System.out.println("Ingrese Email: ");
            String email = sc.nextLine();

            try{
                Validacion.validarEmail(email);
                return email;
            }catch(EmailInvalidoException e){
                System.out.println(e.getMessage());
            }
        }
    }

}
