package Validaciones;

import Excepciones.*;

public class Validacion {

    public static void validarEntero(String texto) throws NumeroInvalidoException{
        try{
            Integer.parseInt(texto);
        }catch(NumberFormatException e){
            throw new NumeroInvalidoException();
        }
    }

    public static void validarNoVacio(String texto) throws CampoVacioException{
        if(texto == null || texto.trim().isEmpty()){
            throw new CampoVacioException();
        }
    }

    public static void validarRango(){

    }

    public static void validarMonto(){

    }

    public static void validarSoloLetras(){

    }

    public static void validarDni(String dni) throws DniInvalidoException {
        // matches devuelve true/false si el string cumple -> "\\d" cualquier numero del 0 al 9 y (7,8) entre 7 y 8 digitos
        if(dni == null || !dni.trim().replace(".","").matches("\\d(7,8)")){
            throw new DniInvalidoException("El DNI debe tener 7 u 8 digitos.");
        }
    }

    public static void validarEmail(String email) throws EmailInvalidoException {
        // verifica que el string tenga "texto @ texto . texto"
        String contenido =  "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        if(email == null || !email.trim().matches(contenido)){
            throw new EmailInvalidoException("El formato del email no es valido.");
        }

    }

    public static void validarTelefono(String telefono) throws TelefonoInvalidoException {

        if(telefono == null){
            throw new TelefonoInvalidoException("El telefono no puede ser nulo.");
        } else if(!telefono.trim().matches("^\\+?[0-9() -]+$")){
            throw new TelefonoInvalidoException("El telefono tiene caracteres no permitidos.");
        }else if(telefono.replaceAll("\\D","").length() < 8 || telefono.length() > 15){ // "\\D" borra lo que no sea un digito
            throw new TelefonoInvalidoException("El telefono debe tener entre 8 y 15 caracteres.");
        }


    }

    public static void validarFecha(){

    }

    public static void validarNombreUsuario(){

    }

    public static void validarContrasenia(){

    }

    public static void validarSiNo(){

    }


}
