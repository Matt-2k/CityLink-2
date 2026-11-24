package excepciones;

/**
 * Excepcion checked que se lanza al intentar atender un pedido cuando las colas de pendientes estan vacias.
 */
public class SinPedidosPendientesException extends Exception {
    public SinPedidosPendientesException(String mensaje) {
        super(mensaje);
    }
}
