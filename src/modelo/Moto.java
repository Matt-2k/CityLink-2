package modelo;

/**
 * Moto: 30 kg, 40 km/h, $120 por km, 5 minutos extra por estacionamiento.
 */
public class Moto extends Vehiculo {
    public Moto(String patente) {
        super(patente, 30, 40, 120);
    }

    @Override
    public double calcularTiempoEntrega(double km) {
        return tiempoDeViaje(km) + 5;
    }
}
