package algoritmos;

import java.util.List;
import modelo.Cliente;
import modelo.Pedido;

/**
 * Algoritmos de busqueda escritos a mano. Todos devuelven la posicion del elemento en la lista, o -1 si no esta.
 * Guardan la cantidad de comparaciones de la ultima busqueda para poder comparar la eficiencia de cada algoritmo.
 */
public class Busqueda {
    private static int comparaciones = 0;

    private Busqueda() {
    }

    /**
     * @return cantidad de comparaciones realizadas en la ultima busqueda
     */
    public static int getComparaciones() {
        return comparaciones;
    }

    /**
     * Busqueda lineal de un pedido por codigo: revisa los elementos uno por uno desde el principio.
     * Funciona con listas en cualquier orden.
     */
    public static int buscarPedidoLineal(List<Pedido> lista, int codigo) {
        comparaciones = 0;
        for (int i = 0; i < lista.size(); i++) {
            comparaciones++;
            if (lista.get(i).getCodigo() == codigo) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Busqueda lineal de un cliente por nombre, sin distinguir mayusculas.
     */
    public static int buscarClientePorNombre(List<Cliente> lista, String nombre) {
        comparaciones = 0;
        for (int i = 0; i < lista.size(); i++) {
            comparaciones++;
            if (lista.get(i).getNombre().equalsIgnoreCase(nombre.trim())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Busqueda binaria iterativa de un pedido por codigo. La lista debe estar ordenada por codigo de menor a mayor.
     * En cada vuelta compara con el elemento del medio y descarta la mitad donde no puede estar.
     */
    public static int buscarBinariaIterativa(List<Pedido> ordenada, int codigo) {
        comparaciones = 0;
        int inicio = 0;
        int fin = ordenada.size() - 1;
        while (inicio <= fin) {
            int medio = (inicio + fin) / 2;
            comparaciones++;
            int codigoMedio = ordenada.get(medio).getCodigo();
            if (codigoMedio == codigo) {
                return medio;
            }
            if (codigoMedio < codigo) {
                inicio = medio + 1;
            } else {
                fin = medio - 1;
            }
        }
        return -1;
    }

    /**
     * Busqueda binaria recursiva de un pedido por codigo. La lista debe estar ordenada por codigo.
     */
    public static int buscarBinariaRecursiva(List<Pedido> ordenada, int codigo) {
        comparaciones = 0;
        return buscarBinariaRecursiva(ordenada, codigo, 0, ordenada.size() - 1);
    }

    private static int buscarBinariaRecursiva(List<Pedido> ordenada, int codigo, int inicio, int fin) {
        if (inicio > fin) {
            return -1;
        }
        int medio = (inicio + fin) / 2;
        comparaciones++;
        int codigoMedio = ordenada.get(medio).getCodigo();
        if (codigoMedio == codigo) {
            return medio;
        }
        if (codigoMedio < codigo) {
            return buscarBinariaRecursiva(ordenada, codigo, medio + 1, fin);
        }
        return buscarBinariaRecursiva(ordenada, codigo, inicio, medio - 1);
    }
}
