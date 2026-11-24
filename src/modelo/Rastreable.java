package modelo;

/**
 * Contrato para los objetos cuyo estado puede consultarse. Lo implementan Pedido y Repartidor.
 */
public interface Rastreable {
    /**
     * @return descripcion del estado actual del objeto
     */
    String obtenerEstadoActual();
}
