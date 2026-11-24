package modelo;

import excepciones.PedidoInvalidoException;
import java.time.LocalDateTime;

/**
 * Pedido normal: sin recargo sobre el costo base.
 */
public class PedidoNormal extends Pedido {
    public PedidoNormal(int codigo, Cliente cliente, String destino, double pesoKg, double distanciaKm)
            throws PedidoInvalidoException {
        super(codigo, cliente, destino, pesoKg, distanciaKm);
    }

    public PedidoNormal(int codigo, Cliente cliente, String destino, double pesoKg, double distanciaKm,
            LocalDateTime fechaHora) throws PedidoInvalidoException {
        super(codigo, cliente, destino, pesoKg, distanciaKm, fechaHora);
    }

    @Override
    public double calcularCosto(Vehiculo vehiculo) {
        return costoBase(vehiculo);
    }
}
