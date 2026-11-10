package Excepciones.ExcepcionesDatos;

public class LargoExcedidoException extends DatoInvalidoException {
    public LargoExcedidoException( ) {
        super("Error: Largo Excedido");
    }
}
