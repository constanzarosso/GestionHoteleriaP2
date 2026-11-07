package Validaciones;

import Excepciones.CampoVacioException;
import Excepciones.NumeroInvalidoException;

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


}
