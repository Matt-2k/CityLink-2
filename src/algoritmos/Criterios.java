package algoritmos;

import java.util.Comparator;
import modelo.Pedido;

/**
 * Criterios de comparacion de pedidos. Cada Comparator indica como se ordenan dos pedidos:
 * compare devuelve un negativo si el primero va antes, cero si son equivalentes y un positivo si va despues.
 */
public class Criterios {
    public static final Comparator<Pedido> POR_CODIGO = (a, b) -> Integer.compare(a.getCodigo(), b.getCodigo());
    public static final Comparator<Pedido> POR_COSTO = (a, b) -> Double.compare(a.calcularImporte(), b.calcularImporte());
    public static final Comparator<Pedido> POR_DISTANCIA = (a, b) -> Double.compare(a.getDistanciaKm(), b.getDistanciaKm());
    public static final Comparator<Pedido> POR_FECHA = (a, b) -> a.getFechaHora().compareTo(b.getFechaHora());

    private Criterios() {
    }
}
