package modelo;

import excepciones.PedidoInvalidoException;
import java.time.LocalDateTime;

/**
 * Pedido urgente: suma un 50% sobre el costo base. Se encola en la cola de urgentes.
 */
public class PedidoUrgente extends Pedido {
    public PedidoUrgente(int codigo, Cliente cliente, String destino, double pesoKg, double distanciaKm)
            throws PedidoInvalidoException {
        super(codigo, cliente, destino, pesoKg, distanciaKm);
    }

    public PedidoUrgente(int codigo, Cliente cliente, String destino, double pesoKg, double distanciaKm,
            LocalDateTime fechaHora) throws PedidoInvalidoException {
        super(codigo, cliente, destino, pesoKg, distanciaKm, fechaHora);
    }

    @Override
    public double calcularCosto(Vehiculo vehiculo) {
        return costoBase(vehiculo) * 1.5;
    }

    @Override
    public boolean esUrgente() {
        return true;
    }
}
