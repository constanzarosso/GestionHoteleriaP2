package Excepciones.ExcepcionesBD;

public class BDConsultaException extends BDPersistenciaException {
    public BDConsultaException(String message) {

        super("No se pudieron obtener los datos de " +  message);
    }
}
