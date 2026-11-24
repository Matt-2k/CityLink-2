package modelo;

/**
 * Camioneta: 500 kg, 30 km/h, $300 por km, 10 minutos extra por carga y descarga.
 */
public class Camioneta extends Vehiculo {
    public Camioneta(String patente) {
        super(patente, 500, 30, 300);
    }

    @Override
    public double calcularTiempoEntrega(double km) {
        return tiempoDeViaje(km) + 10;
    }
}
