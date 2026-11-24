package excepciones;

/**
 * Excepcion checked que se lanza cuando un pedido es invalido: peso o distancia no positivos,
 * destino vacio, codigo duplicado, cliente inexistente o pedido no encontrado.
 */
public class PedidoInvalidoException extends Exception {
    public PedidoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
