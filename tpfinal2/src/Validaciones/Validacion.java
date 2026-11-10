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
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public final class Validacion {

    //Evita que se cree automaticamente el constructor publico para que no se instancie
    private Validacion() {
        throw new UnsupportedOperationException("Esta clase no puede ser instanciada");
    }

    public static void validarEntero(String texto) throws NumeroInvalidoException {
        try{
            Integer.parseInt(texto);
        }catch(NumberFormatException e){
            throw new NumeroInvalidoException();
        }
    }

    public static void validarNoVacio(String texto) throws CampoVacioException {
        if(texto == null || texto.trim().isEmpty()){
            throw new CampoVacioException();
        }
    }

    public static void validarRango(int numero, int minimo, int maximo) throws FueraDeRangoException {

        if(numero < minimo || numero > maximo){
            throw new FueraDeRangoException("El numero debe estar entre "+minimo+ " y "+maximo);
        }
    }

    public static void validarMonto(double monto) throws MontoInvalidoException {

        // devuelve true/false si el numero es real y valido o no es un numero
        if(!Double.isFinite(monto) || monto < 0){
            throw new MontoInvalidoException("El monto es negativo o invalido.");
        }
    }

    public static void validarSoloLetras(String texto) throws TextoInvalidoException{

        if(texto == null || !texto.trim().matches("[\\p{L} ]+")){ // permite solo letras
            throw new TextoInvalidoException("El texto solo puede contener letras y espacios.");
        }
    }

    public static void validarDni(String dni) throws DniInvalidoException {
        // matches devuelve true/false si el string cumple -> "\\d" cualquier numero del 0 al 9 y (7,8) entre 7 y 8 digitos
        if(dni == null || !dni.trim().replace(".","").matches("\\d{7,8}")){
            throw new DniInvalidoException("El DNI debe tener 7 u 8 digitos.");
        }
    }

    public static void validarEmail(String email) throws BDEmailInvalidoException {
        // verifica que el string tenga "texto @ texto . texto"
        String contenido =  "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        if(email == null || !email.trim().matches(contenido)){
            throw new BDEmailInvalidoException("El formato del email no es valido.");
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

    public static void validarFecha(String fecha) throws FechaInvalidaException {
        if(fecha == null){
            throw new FechaInvalidaException("La fecha no puede ser nula.");
        }

        // guardo las reglas de como tiene que verse el formato de la fecha dia/mes/año -> uuuu fechas normales -> yyyy obliga a poner d.C/a.C
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

        try{
            LocalDate.parse(fecha, formato);
        }catch (DateTimeParseException e){
            throw new FechaInvalidaException("La fecha debe tener formato dd/MM/yyyy (dia/mes/año) y ser valida");
        }
    }

    // Min 4 caract Max 20 carac -> letras,num,punto,guion -> No espacios ni Caracteres especiales
    public static void validarNombreUsuario(String nombreUsuario) throws BDNombreUsuarioInvalidoException {

        if(nombreUsuario == null || !nombreUsuario.matches("^[a-zA-Z0-9]([._]?[a-zA-Z0-9]){3,19}$")){
            throw new BDNombreUsuarioInvalidoException("El nombre de usuario debe tener entre 4 y 20 caracteres. " +
                    "Solo se permiten letras, numero, puntos y guiones bajos.");
        }

    }

    // Min 8, Max 30 carct -> Mayus 1, Minuscula 1, Num 1 , Caracter especial 1
    public static void validarContrasenia(String contrasenia) throws BDContraseniaInvalidaException {

        if(contrasenia == null ||
                contrasenia.length() < 8 ||
                contrasenia.length() > 30 ||
                contrasenia.contains(" ") ||
                !contrasenia.matches(".*[A-Z].*") ||
                !contrasenia.matches(".*[a-z].*") ||
                !contrasenia.matches(".*[0-9].*") ||
                !contrasenia.matches(".*[^a-zA-Z0-9].*")){

            throw new BDContraseniaInvalidaException("La contraseña tiene que tener entre 8 y 30 caracteres." +
                    "Sin espacios, con una mayuscula, una miniscula, un numero y un caracter especial.");
        }
    }

    public static void validarSiNo(String respuesta) throws TextoInvalidoException{

        respuesta = respuesta.toLowerCase();
        if(respuesta == null || !respuesta.trim().matches("(?i)(s|si|sí|n|no)")){ // ?i = ignora mayus y min
            throw new TextoInvalidoException("Debe ingresar S/N o si/no");
        }
    }


}
