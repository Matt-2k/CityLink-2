package modelo;

import excepciones.CapacidadExcedidaException;
import java.util.ArrayList;
import java.util.List;

/**
 * Repartidor con un vehiculo, una lista de pedidos asignados y un historial de entregas.
 * Implementa Rastreable (resumen de su estado) y Facturable (comision del 10% de lo entregado).
 */
public class Repartidor implements Rastreable, Facturable {
    private static final double PORCENTAJE_COMISION = 0.10;

    private int legajo;
    private String nombre;
    private Vehiculo vehiculo;
    private List<Pedido> pedidos = new ArrayList<>();
    private List<Pedido> historialEntregas = new ArrayList<>();

    public Repartidor(int legajo, String nombre, Vehiculo vehiculo) {
        setLegajo(legajo);
        setNombre(nombre);
        setVehiculo(vehiculo);
    }

    /**
     * Asigna un pedido usando el vehiculo propio del repartidor.
     *
     * @throws CapacidadExcedidaException si el vehiculo no puede transportar el pedido
     */
    public void asignarPedido(Pedido pedido) throws CapacidadExcedidaException {
        asignarPedido(pedido, vehiculo);
    }

    /**
     * Sobrecarga que permite indicar el vehiculo con el que se transporta el pedido.
     * Verifica las restricciones del vehiculo antes de asignar.
     *
     * @throws CapacidadExcedidaException si el vehiculo no puede transportar el pedido
     */
    public void asignarPedido(Pedido pedido, Vehiculo vehiculoUsado) throws CapacidadExcedidaException {
        if (!vehiculoUsado.puedeTransportar(pedido)) {
            if (pedido.getPesoKg() > vehiculoUsado.getCapacidadKg()) {
                throw new CapacidadExcedidaException("El peso del pedido (" + pedido.getPesoKg()
                        + " kg) supera la capacidad del vehiculo (" + vehiculoUsado.getCapacidadKg() + " kg)");
            }
            throw new CapacidadExcedidaException("Este vehiculo no puede transportar pedidos fragiles");
        }
        pedidos.add(pedido);
        pedido.setVehiculo(vehiculoUsado);
    }

    /**
     * Indica si el pedido esta asignado a este repartidor.
     */
    public boolean tienePedido(Pedido pedido) {
        return pedidos.contains(pedido);
    }

    /**
     * Marca el pedido como ENTREGADO y lo agrega al historial de entregas. El pedido debe pertenecer al repartidor.
     *
     * @throws EstadoInvalidoException si el pedido no esta EN_CAMINO
     */
    public void registrarEntrega(Pedido pedido) {
        if (!tienePedido(pedido)) {
            throw new IllegalArgumentException("El pedido no esta asignado a este repartidor");
        }
        pedido.cambiarEstado(EstadoPedido.ENTREGADO);
        historialEntregas.add(pedido);
    }

    /**
     * Quita un pedido de los asignados y le saca el vehiculo. Se usa al deshacer una asignacion.
     */
    public void quitarPedido(Pedido pedido) {
        pedidos.remove(pedido);
        pedido.setVehiculo(null);
    }

    /**
     * Quita un pedido del historial de entregas. Se usa al deshacer una entrega.
     */
    public void quitarEntrega(Pedido pedido) {
        historialEntregas.remove(pedido);
    }

    /**
     * Comision del repartidor: 10% del importe de los pedidos de su historial de entregas.
     */
    @Override
    public double calcularImporte() {
        double total = 0;
        for (Pedido pedido : historialEntregas) {
            total += pedido.calcularImporte();
        }
        return total * PORCENTAJE_COMISION;
    }

    @Override
    public String obtenerEstadoActual() {
        return "Repartidor " + nombre + " (legajo " + legajo + ") - " + vehiculo.getClass().getSimpleName()
                + " " + vehiculo.getPatente() + " - Pedidos asignados: " + pedidos.size()
                + " - Entregados: " + historialEntregas.size();
    }

    public int getLegajo() {
        return legajo;
    }

    public void setLegajo(int legajo) {
        if (legajo <= 0) {
            throw new IllegalArgumentException("El legajo debe ser mayor que cero");
        }
        this.legajo = legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio");
        }
        this.nombre = nombre.trim();
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) {
            throw new IllegalArgumentException("El repartidor debe tener un vehiculo");
        }
        this.vehiculo = vehiculo;
    }

    public int getCantidadPedidos() {
        return pedidos.size();
    }

    public List<Pedido> getHistorialEntregas() {
        return new ArrayList<>(historialEntregas);
    }

    @Override
    public String toString() {
        return "Repartidor #" + legajo + " - " + nombre + " - " + vehiculo;
    }
}
