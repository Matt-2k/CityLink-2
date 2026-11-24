package modelo;

import excepciones.EstadoInvalidoException;
import excepciones.PedidoInvalidoException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Raiz de la jerarquia de pedidos. Implementa las interfaces Rastreable y Facturable.
 * El costo depende del tipo de pedido y se calcula con el metodo abstracto calcularCosto.
 * Los setters validan los datos y lanzan PedidoInvalidoException.
 */
public abstract class Pedido implements Rastreable, Facturable {
    private int codigo;
    private Cliente cliente;
    private String destino;
    private double pesoKg;
    private double distanciaKm;
    private LocalDateTime fechaHora;
    private EstadoPedido estado;
    private Vehiculo vehiculo;

    /**
     * Crea un pedido con la fecha y hora actuales. Queda en estado PENDIENTE y sin vehiculo asignado.
     * @throws PedidoInvalidoException si algun dato es invalido
     */
    public Pedido(int codigo, Cliente cliente, String destino, double pesoKg, double distanciaKm)
            throws PedidoInvalidoException {
        this(codigo, cliente, destino, pesoKg, distanciaKm, LocalDateTime.now());
    }

    /**
     * Crea un pedido con una fecha y hora indicadas. Queda en estado PENDIENTE y sin vehiculo asignado.
     * @throws PedidoInvalidoException si algun dato es invalido
     */
    public Pedido(int codigo, Cliente cliente, String destino, double pesoKg, double distanciaKm,
            LocalDateTime fechaHora) throws PedidoInvalidoException {
        setCodigo(codigo);
        setCliente(cliente);
        setDestino(destino);
        setPesoKg(pesoKg);
        setDistanciaKm(distanciaKm);
        this.fechaHora = fechaHora;
        this.estado = EstadoPedido.PENDIENTE;
        this.vehiculo = null;
    }

    /**
     * Calcula el costo del envio con el vehiculo indicado. Cada subclase aplica su recargo.
     * @param vehiculo vehiculo que realiza el envio
     * @return costo del envio
     */
    public abstract double calcularCosto(Vehiculo vehiculo);

    /**
     * Indica si el pedido es fragil. Permite que Bicicleta rechace estos pedidos sin usar instanceof.
     */
    public boolean esFragil() {
        return false;
    }

    /**
     * Indica si el pedido es urgente. Permite elegir la cola de pendientes sin usar instanceof.
     */
    public boolean esUrgente() {
        return false;
    }

    /**
     * Restaura un estado anterior. Solo debe usarse al deshacer una operacion, ya que salta
     * la validacion de transiciones de cambiarEstado.
     *
     * @param estadoAnterior estado que tenia el pedido antes de la operacion deshecha
     */
    public void restaurarEstado(EstadoPedido estadoAnterior) {
        this.estado = estadoAnterior;
    }

    /**
     * Costo comun a todos los tipos: $1000 + (distancia * costo por km del vehiculo).
     */
    protected double costoBase(Vehiculo vehiculo) {
        return 1000 + distanciaKm * vehiculo.getCostoPorKm();
    }

    /**
     * Cambia el estado validando la transicion. Se permite PENDIENTE a EN_CAMINO o CANCELADO,
     * y EN_CAMINO a ENTREGADO.
     * @throws EstadoInvalidoException si la transicion no esta permitida
     */
    public void cambiarEstado(EstadoPedido nuevoEstado) {
        boolean valida = false;
        if (estado == EstadoPedido.PENDIENTE
                && (nuevoEstado == EstadoPedido.EN_CAMINO || nuevoEstado == EstadoPedido.CANCELADO)) {
            valida = true;
        } else if (estado == EstadoPedido.EN_CAMINO && nuevoEstado == EstadoPedido.ENTREGADO) {
            valida = true;
        }
        if (!valida) {
            throw new EstadoInvalidoException("Transicion invalida: de " + estado + " a " + nuevoEstado);
        }
        estado = nuevoEstado;
    }

    /**
     * Devuelve el vehiculo asignado. Si el pedido aun no fue asignado, devuelve una moto
     * de referencia para poder estimar costo y tiempo.
     */
    public Vehiculo vehiculoParaCalculo() {
        if (vehiculo == null) {
            return new Moto("REFERENCIA");
        }
        return vehiculo;
    }

    /**
     * Importe del pedido: su costo de envio con el vehiculo asignado (o el de referencia).
     */
    @Override
    public double calcularImporte() {
        return calcularCosto(vehiculoParaCalculo());
    }

    @Override
    public String obtenerEstadoActual() {
        return "Pedido #" + codigo + ": " + estado;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) throws PedidoInvalidoException {
        if (codigo <= 0) {
            throw new PedidoInvalidoException("El codigo del pedido debe ser mayor que cero");
        }
        this.codigo = codigo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) throws PedidoInvalidoException {
        if (cliente == null) {
            throw new PedidoInvalidoException("El pedido debe tener un cliente");
        }
        this.cliente = cliente;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) throws PedidoInvalidoException {
        if (destino == null || destino.trim().isEmpty()) {
            throw new PedidoInvalidoException("El destino no puede estar vacio");
        }
        this.destino = destino.trim();
    }

    public double getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(double pesoKg) throws PedidoInvalidoException {
        if (pesoKg <= 0) {
            throw new PedidoInvalidoException("El peso debe ser mayor que cero");
        }
        this.pesoKg = pesoKg;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(double distanciaKm) throws PedidoInvalidoException {
        if (distanciaKm <= 0) {
            throw new PedidoInvalidoException("La distancia debe ser mayor que cero");
        }
        this.distanciaKm = distanciaKm;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return "Pedido #" + codigo + " [" + getClass().getSimpleName() + "] Cliente: " + cliente.getNombre()
                + " | Destino: " + destino + " | " + pesoKg + " kg | " + distanciaKm + " km | "
                + fechaHora.format(formato) + " | " + estado;
    }
}
