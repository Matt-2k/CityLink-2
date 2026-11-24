package modelo;

/**
 * Registro de una operacion realizada por el operador. Se apila en CityLink y guarda lo necesario
 * para revertirla: el pedido afectado, el repartidor involucrado (si lo hay) y el estado previo del pedido.
 */
public class Operacion {
    private final TipoOperacion tipo;
    private final Pedido pedido;
    private final Repartidor repartidor;
    private final EstadoPedido estadoAnterior;

    public Operacion(TipoOperacion tipo, Pedido pedido, Repartidor repartidor, EstadoPedido estadoAnterior) {
        this.tipo = tipo;
        this.pedido = pedido;
        this.repartidor = repartidor;
        this.estadoAnterior = estadoAnterior;
    }

    public TipoOperacion getTipo() {
        return tipo;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    public EstadoPedido getEstadoAnterior() {
        return estadoAnterior;
    }
}
