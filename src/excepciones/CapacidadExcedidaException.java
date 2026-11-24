package excepciones;

/**
 * Excepcion checked que se lanza cuando el peso del pedido supera la capacidad del vehiculo
 * o cuando se intenta asignar un pedido fragil a una bicicleta.
 */
public class CapacidadExcedidaException extends Exception {
    public CapacidadExcedidaException(String mensaje) {
        super(mensaje);
    }
}
