package Excepciones.ExcepcionesBD;

public class BDAccesoDenegadoException extends Exception {
    public BDAccesoDenegadoException() {
        super("Acceso Denegado");
    }
}
