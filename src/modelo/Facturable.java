package modelo;

/**
 * Contrato para los objetos que generan un importe. Pedido devuelve el costo del envio
 * y Repartidor devuelve su comision.
 */
public interface Facturable {
    /**
     * @return importe calculado
     */
    double calcularImporte();
}
