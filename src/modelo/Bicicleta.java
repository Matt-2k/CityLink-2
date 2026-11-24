package modelo;

/**
 * Bicicleta: 10 kg, 15 km/h, $50 por km, sin tiempo extra.
 */
public class Bicicleta extends Vehiculo {
    public Bicicleta(String patente) {
        super(patente, 10, 15, 50);
    }

    @Override
    public double calcularTiempoEntrega(double km) {
        return tiempoDeViaje(km);
    }

    /**
     * Redefine la regla general: ademas del peso, una bicicleta rechaza pedidos fragiles.
     */
    @Override
    public boolean puedeTransportar(Pedido pedido) {
        return !pedido.esFragil() && super.puedeTransportar(pedido);
    }
}
