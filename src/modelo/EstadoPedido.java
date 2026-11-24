package modelo;

/**
 * Estados posibles de un pedido. El flujo valido es PENDIENTE, EN_CAMINO y ENTREGADO;
 * un pedido solo puede pasar a CANCELADO desde PENDIENTE.
 */
public enum EstadoPedido {
    PENDIENTE,
    EN_CAMINO,
    ENTREGADO,
    CANCELADO
}
