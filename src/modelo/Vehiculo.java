package modelo;

/**
 * Raiz de la jerarquia de vehiculos. Define los datos comunes y deja abstracto el calculo
 * del tiempo de entrega, que cada subclase redefine segun su tiempo extra.
 */
public abstract class Vehiculo {
    private String patente;
    private double capacidadKg;
    private double velocidadKmH;
    private double costoPorKm;

    public Vehiculo(String patente, double capacidadKg, double velocidadKmH, double costoPorKm) {
        setPatente(patente);
        setCapacidadKg(capacidadKg);
        setVelocidadKmH(velocidadKmH);
        setCostoPorKm(costoPorKm);
    }

    /**
     * @param km distancia del recorrido
     * @return tiempo estimado de entrega en minutos
     */
    public abstract double calcularTiempoEntrega(double km);

    /**
     * Calcula el tiempo de viaje puro, sin tiempo extra: (km / velocidad) * 60.
     */
    protected double tiempoDeViaje(double km) {
        return (km / velocidadKmH) * 60;
    }

    /**
     * Indica si el vehiculo puede transportar el pedido segun su peso.
     * Las subclases pueden redefinirlo para agregar restricciones.
     */
    public boolean puedeTransportar(Pedido pedido) {
        return pedido.getPesoKg() <= capacidadKg;
    }

    public String getPatente() {
        return patente;
    }

    public void setPatente(String patente) {
        if (patente == null || patente.trim().isEmpty()) {
            throw new IllegalArgumentException("La patente no puede estar vacia");
        }
        this.patente = patente.trim();
    }

    public double getCapacidadKg() {
        return capacidadKg;
    }

    public void setCapacidadKg(double capacidadKg) {
        if (capacidadKg <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero");
        }
        this.capacidadKg = capacidadKg;
    }

    public double getVelocidadKmH() {
        return velocidadKmH;
    }

    public void setVelocidadKmH(double velocidadKmH) {
        if (velocidadKmH <= 0) {
            throw new IllegalArgumentException("La velocidad debe ser mayor que cero");
        }
        this.velocidadKmH = velocidadKmH;
    }

    public double getCostoPorKm() {
        return costoPorKm;
    }

    public void setCostoPorKm(double costoPorKm) {
        if (costoPorKm <= 0) {
            throw new IllegalArgumentException("El costo por km debe ser mayor que cero");
        }
        this.costoPorKm = costoPorKm;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + patente + " (" + capacidadKg + " kg, "
                + velocidadKmH + " km/h, $" + costoPorKm + "/km)";
    }
}
