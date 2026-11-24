package algoritmos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import modelo.Pedido;

/**
 * Algoritmos de ordenamiento escritos a mano. Ambos devuelven una lista nueva y no modifican la original.
 * El criterio se recibe como Comparator, por lo que sirven para ordenar por costo, distancia, fecha o codigo.
 */
public class Ordenamiento {
    private Ordenamiento() {
    }

    /**
     * Ordenamiento burbuja: recorre la lista comparando pares de vecinos y los intercambia si estan en mal orden.
     * Despues de cada pasada el mayor elemento queda en su posicion final. Si una pasada no hace ningun
     * intercambio, la lista ya esta ordenada y se termina antes.
     */
    public static List<Pedido> burbuja(List<Pedido> lista, Comparator<Pedido> criterio) {
        List<Pedido> copia = new ArrayList<>(lista);
        int n = copia.size();
        for (int pasada = 0; pasada < n - 1; pasada++) {
            boolean huboCambio = false;
            for (int i = 0; i < n - 1 - pasada; i++) {
                if (criterio.compare(copia.get(i), copia.get(i + 1)) > 0) {
                    Pedido auxiliar = copia.get(i);
                    copia.set(i, copia.get(i + 1));
                    copia.set(i + 1, auxiliar);
                    huboCambio = true;
                }
            }
            if (!huboCambio) {
                break;
            }
        }
        return copia;
    }

    /**
     * Mergesort: divide la lista en dos mitades, ordena cada una recursivamente y las une con la intercalacion.
     * La recursion termina con listas de 0 o 1 elemento, que ya estan ordenadas.
     */
    public static List<Pedido> mergeSort(List<Pedido> lista, Comparator<Pedido> criterio) {
        if (lista.size() <= 1) {
            return new ArrayList<>(lista);
        }
        int medio = lista.size() / 2;
        List<Pedido> izquierda = mergeSort(new ArrayList<>(lista.subList(0, medio)), criterio);
        List<Pedido> derecha = mergeSort(new ArrayList<>(lista.subList(medio, lista.size())), criterio);
        return Intercalacion.intercalar(izquierda, derecha, criterio);
    }
}
