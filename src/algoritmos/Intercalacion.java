package algoritmos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import modelo.Pedido;

/**
 * Algoritmo de intercalacion (merge) escrito a mano.
 */
public class Intercalacion {
    private Intercalacion() {
    }

    /**
     * Combina dos listas que ya estan ordenadas segun el mismo criterio en una unica lista ordenada.
     * Hace un solo recorrido: avanza un indice por lista, copia siempre el menor de los dos elementos
     * y al terminar una lista copia lo que queda de la otra. No vuelve a ordenar. Si hay empate,
     * se toma primero el elemento de la primera lista.
     *
     * @param a primera lista, ya ordenada
     * @param b segunda lista, ya ordenada
     * @param criterio criterio con el que estan ordenadas ambas listas
     * @return nueva lista con todos los elementos ordenados
     */
    public static List<Pedido> intercalar(List<Pedido> a, List<Pedido> b, Comparator<Pedido> criterio) {
        List<Pedido> resultado = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < a.size() && j < b.size()) {
            if (criterio.compare(a.get(i), b.get(j)) <= 0) {
                resultado.add(a.get(i));
                i++;
            } else {
                resultado.add(b.get(j));
                j++;
            }
        }
        while (i < a.size()) {
            resultado.add(a.get(i));
            i++;
        }
        while (j < b.size()) {
            resultado.add(b.get(j));
            j++;
        }
        return resultado;
    }
}
