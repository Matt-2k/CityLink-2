package modelo;

import excepciones.PedidoInvalidoException;
import java.time.LocalDateTime;

/**
 * Pedido fragil: suma un adicional fijo de $800. No puede asignarse a una bicicleta.
 */
public class PedidoFragil extends Pedido {
    public PedidoFragil(int codigo, Cliente cliente, String destino, double pesoKg, double distanciaKm)
            throws PedidoInvalidoException {
        super(codigo, cliente, destino, pesoKg, distanciaKm);
    }

    public PedidoFragil(int codigo, Cliente cliente, String destino, double pesoKg, double distanciaKm,
            LocalDateTime fechaHora) throws PedidoInvalidoException {
        super(codigo, cliente, destino, pesoKg, distanciaKm, fechaHora);
    }

    @Override
    public double calcularCosto(Vehiculo vehiculo) {
        return costoBase(vehiculo) + 800;
    }

    @Override
    public boolean esFragil() {
        return true;
    }
}
