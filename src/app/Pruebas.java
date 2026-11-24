package app;

import algoritmos.Busqueda;
import algoritmos.Criterios;
import algoritmos.Intercalacion;
import algoritmos.Ordenamiento;
import excepciones.CapacidadExcedidaException;
import excepciones.ClienteNoEncontradoException;
import excepciones.NadaParaDeshacerException;
import excepciones.PedidoInvalidoException;
import excepciones.SinPedidosPendientesException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import modelo.CityLink;
import modelo.Cliente;
import modelo.EstadoPedido;
import modelo.Moto;
import modelo.Pedido;
import modelo.PedidoNormal;
import modelo.Repartidor;

/**
 * Pruebas manuales de los algoritmos y de las estructuras. Cada prueba imprime OK o FALLO.
 * Se ejecuta con: java -cp out app.Pruebas
 */
public class Pruebas {
    private static int correctas = 0;
    private static int total = 0;

    public static void main(String[] args) throws Exception {
        Cliente cliente = new Cliente(1, "Ana", "111", "Centro");
        List<Pedido> vacia = new ArrayList<>();
        List<Pedido> uno = lista(cliente, 5);
        List<Pedido> varios = lista(cliente, 8, 3, 9, 1, 6);

        System.out.println("== Busqueda lineal ==");
        verificar("Normal: encuentra el codigo 9 en la posicion 2", Busqueda.buscarPedidoLineal(varios, 9) == 2);
        verificar("Borde: lista vacia devuelve -1", Busqueda.buscarPedidoLineal(vacia, 1) == -1);
        verificar("Borde: un solo elemento, existente", Busqueda.buscarPedidoLineal(uno, 5) == 0);
        verificar("Borde: elemento inexistente devuelve -1", Busqueda.buscarPedidoLineal(varios, 99) == -1);

        System.out.println("== Busqueda binaria ==");
        List<Pedido> ordenada = Ordenamiento.mergeSort(varios, Criterios.POR_CODIGO);
        verificar("Normal iterativa: codigo 6", Busqueda.buscarBinariaIterativa(ordenada, 6) == 2);
        verificar("Normal recursiva: codigo 6", Busqueda.buscarBinariaRecursiva(ordenada, 6) == 2);
        verificar("Borde: primer elemento", Busqueda.buscarBinariaIterativa(ordenada, 1) == 0);
        verificar("Borde: ultimo elemento", Busqueda.buscarBinariaIterativa(ordenada, 9) == 4);
        verificar("Borde: lista vacia", Busqueda.buscarBinariaIterativa(vacia, 1) == -1);
        verificar("Borde: un solo elemento", Busqueda.buscarBinariaRecursiva(uno, 5) == 0);
        verificar("Borde: inexistente iterativa", Busqueda.buscarBinariaIterativa(ordenada, 4) == -1);
        verificar("Borde: inexistente recursiva", Busqueda.buscarBinariaRecursiva(ordenada, 4) == -1);

        System.out.println("== Ordenamiento ==");
        verificar("Burbuja ordena por codigo", codigos(Ordenamiento.burbuja(varios, Criterios.POR_CODIGO)).equals("1 3 6 8 9"));
        verificar("Mergesort ordena por codigo", codigos(Ordenamiento.mergeSort(varios, Criterios.POR_CODIGO)).equals("1 3 6 8 9"));
        verificar("Borde: lista vacia (burbuja)", Ordenamiento.burbuja(vacia, Criterios.POR_CODIGO).isEmpty());
        verificar("Borde: lista vacia (mergesort)", Ordenamiento.mergeSort(vacia, Criterios.POR_CODIGO).isEmpty());
        verificar("Borde: un solo elemento", codigos(Ordenamiento.mergeSort(uno, Criterios.POR_CODIGO)).equals("5"));
        verificar("Borde: ya ordenada", codigos(Ordenamiento.burbuja(ordenada, Criterios.POR_CODIGO)).equals("1 3 6 8 9"));
        verificar("No modifica la lista original", codigos(varios).equals("8 3 9 1 6"));
        List<Pedido> porDistancia = Ordenamiento.mergeSort(varios, Criterios.POR_DISTANCIA);
        verificar("Por distancia: ambos algoritmos coinciden",
                codigos(porDistancia).equals(codigos(Ordenamiento.burbuja(varios, Criterios.POR_DISTANCIA))));
        verificar("Por costo: ambos algoritmos coinciden",
                codigos(Ordenamiento.mergeSort(varios, Criterios.POR_COSTO))
                        .equals(codigos(Ordenamiento.burbuja(varios, Criterios.POR_COSTO))));

        System.out.println("== Intercalacion ==");
        List<Pedido> a = lista(cliente, 1, 4, 7);
        List<Pedido> b = lista(cliente, 2, 3, 9, 10);
        verificar("Normal: dos listas ordenadas", codigos(Intercalacion.intercalar(a, b, Criterios.POR_CODIGO)).equals("1 2 3 4 7 9 10"));
        verificar("Borde: primera lista vacia", codigos(Intercalacion.intercalar(vacia, b, Criterios.POR_CODIGO)).equals("2 3 9 10"));
        verificar("Borde: segunda lista vacia", codigos(Intercalacion.intercalar(a, vacia, Criterios.POR_CODIGO)).equals("1 4 7"));
        verificar("Borde: ambas vacias", Intercalacion.intercalar(vacia, vacia, Criterios.POR_CODIGO).isEmpty());
        verificar("Borde: un elemento en cada lista", codigos(Intercalacion.intercalar(uno, lista(cliente, 2), Criterios.POR_CODIGO)).equals("2 5"));

        System.out.println("== Cola, pila y arbol ==");
        CityLink vacio = new CityLink();
        try {
            vacio.atenderSiguiente();
            verificar("Borde: atender con colas vacias lanza excepcion", false);
        } catch (SinPedidosPendientesException e) {
            verificar("Borde: atender con colas vacias lanza SinPedidosPendientesException", true);
        }
        try {
            vacio.deshacerUltima();
            verificar("Borde: deshacer con pila vacia lanza excepcion", false);
        } catch (NadaParaDeshacerException e) {
            verificar("Borde: deshacer con pila vacia lanza NadaParaDeshacerException", true);
        }
        try {
            vacio.buscarCliente(7);
            verificar("Borde: cliente inexistente lanza excepcion", false);
        } catch (ClienteNoEncontradoException e) {
            verificar("Borde: cliente inexistente lanza ClienteNoEncontradoException", true);
        }
        verificar("Borde: listado de arbol vacio", vacio.listarClientes().equals("No hay clientes registrados."));

        CityLink gestor = new CityLink();
        gestor.cargarDatosPrueba();
        verificar("Arbol: el listado sale ordenado por codigo",
                gestor.listarClientes().indexOf("#1") < gestor.listarClientes().indexOf("#2")
                        && gestor.listarClientes().indexOf("#4") < gestor.listarClientes().indexOf("#5"));
        verificar("Cola: urgentes primero (queda el 8 en urgentes)", gestor.estadoColas().contains("urgentes (1): #8"));
        verificar("Cola: normales en orden de llegada", gestor.estadoColas().contains("normales (5): #1 #3 #4 #6 #7"));
        verificar("Datos iniciales: la pila empieza vacia", gestor.estadoColas().contains("deshacer: 0"));
        gestor.atenderSiguiente();
        verificar("Atender: sale primero el urgente #8", gestor.buscarPedidoLineal(8).getEstado() == EstadoPedido.EN_CAMINO);
        gestor.deshacerUltima();
        verificar("Deshacer asignacion: #8 vuelve PENDIENTE al frente de urgentes",
                gestor.buscarPedidoLineal(8).getEstado() == EstadoPedido.PENDIENTE && gestor.estadoColas().contains("urgentes (1): #8"));
        gestor.registrarPedido(new PedidoNormal(20, new Cliente(9, "Test", "1", "Z"), "Plaza", 1, 1,
                LocalDate.now().atTime(10, 0)));
        verificar("Alta: el pedido nuevo entra a la cola de normales", gestor.estadoColas().contains("normales (6)"));
        gestor.deshacerUltima();
        verificar("Deshacer alta: el pedido ya no existe", pedidoNoExiste(gestor, 20));
        try {
            gestor.registrarPedido(new PedidoNormal(1, cliente, "Centro", 1, 1));
            verificar("Borde: codigo de pedido repetido lanza excepcion", false);
        } catch (PedidoInvalidoException e) {
            verificar("Borde: codigo de pedido repetido lanza PedidoInvalidoException", true);
        }
        CityLink soloBicis = new CityLink();
        soloBicis.registrarRepartidor(new Repartidor(1, "Solo Moto", new Moto("M-1")));
        soloBicis.registrarPedido(new PedidoNormal(1, cliente, "Centro", 100, 2));
        try {
            soloBicis.atenderSiguiente();
            verificar("Borde: nadie puede cargar el pedido", false);
        } catch (CapacidadExcedidaException e) {
            verificar("Borde: ningun vehiculo puede cargar 100 kg lanza CapacidadExcedidaException y el pedido sigue en la cola",
                    soloBicis.estadoColas().contains("normales (1)"));
        }

        System.out.println();
        System.out.println("Resultado: " + correctas + " de " + total + " pruebas correctas.");
    }

    private static boolean pedidoNoExiste(CityLink gestor, int codigo) {
        try {
            gestor.buscarPedidoLineal(codigo);
            return false;
        } catch (PedidoInvalidoException e) {
            return true;
        }
    }

    private static List<Pedido> lista(Cliente cliente, int... codigos) throws PedidoInvalidoException {
        List<Pedido> resultado = new ArrayList<>();
        for (int codigo : codigos) {
            resultado.add(new PedidoNormal(codigo, cliente, "Destino", 1.0 + codigo % 5, 1.0 + codigo * 0.5));
        }
        return resultado;
    }

    private static String codigos(List<Pedido> pedidos) {
        StringBuilder sb = new StringBuilder();
        for (Pedido pedido : pedidos) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(pedido.getCodigo());
        }
        return sb.toString();
    }

    private static void verificar(String descripcion, boolean condicion) {
        total++;
        if (condicion) {
            correctas++;
        }
        System.out.println((condicion ? "OK     " : "FALLO  ") + descripcion);
    }
}
