package modelo;

import algoritmos.Busqueda;
import algoritmos.Criterios;
import algoritmos.Intercalacion;
import algoritmos.Ordenamiento;
import excepciones.CapacidadExcedidaException;
import excepciones.ClienteNoEncontradoException;
import excepciones.EstadoInvalidoException;
import excepciones.NadaParaDeshacerException;
import excepciones.PedidoInvalidoException;
import excepciones.SinPedidosPendientesException;
import java.time.LocalDate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import java.util.TreeMap;

/**
 * Gestor del sistema. Usa las estructuras dinamicas de java.util: un TreeMap (arbol) para los clientes,
 * ArrayList para repartidores y pedidos, dos colas ArrayDeque para los pedidos pendientes
 * (urgentes y normales) y una pila ArrayDeque con las operaciones que se pueden deshacer.
 * No interactua con el usuario: eso lo hace el menu del paquete app.
 */
public class CityLink {
    public static final int HORA_CORTE_TURNO = 14;

    private final TreeMap<Integer, Cliente> clientes = new TreeMap<>();
    private final List<Repartidor> repartidores = new ArrayList<>();
    private final List<Pedido> pedidos = new ArrayList<>();
    private final Deque<Pedido> colaUrgentes = new ArrayDeque<>();
    private final Deque<Pedido> colaNormales = new ArrayDeque<>();
    private final Deque<Operacion> pilaOperaciones = new ArrayDeque<>();

    /**
     * Agrega un cliente al arbol, que lo mantiene ordenado por codigo.
     *
     * @throws IllegalArgumentException si ya existe un cliente con ese codigo
     */
    public void registrarCliente(Cliente cliente) {
        if (clientes.containsKey(cliente.getCodigo())) {
            throw new IllegalArgumentException("Ya existe un cliente con el codigo " + cliente.getCodigo());
        }
        clientes.put(cliente.getCodigo(), cliente);
    }

    /**
     * Busca un cliente por codigo usando el arbol (TreeMap.get).
     *
     * @throws ClienteNoEncontradoException si no existe
     */
    public Cliente buscarCliente(int codigo) throws ClienteNoEncontradoException {
        Cliente cliente = clientes.get(codigo);
        if (cliente == null) {
            throw new ClienteNoEncontradoException("No existe un cliente con el codigo " + codigo);
        }
        return cliente;
    }

    /**
     * Sobrecarga que busca un cliente por nombre con la busqueda lineal escrita por el grupo.
     *
     * @throws ClienteNoEncontradoException si no existe
     */
    public Cliente buscarCliente(String nombre) throws ClienteNoEncontradoException {
        List<Cliente> lista = new ArrayList<>(clientes.values());
        int posicion = Busqueda.buscarClientePorNombre(lista, nombre);
        if (posicion == -1) {
            throw new ClienteNoEncontradoException("No existe un cliente con el nombre " + nombre);
        }
        return lista.get(posicion);
    }

    /**
     * Elimina un cliente del arbol. No se puede eliminar un cliente que tiene pedidos registrados.
     *
     * @throws ClienteNoEncontradoException si no existe
     */
    public void eliminarCliente(int codigo) throws ClienteNoEncontradoException {
        if (!clientes.containsKey(codigo)) {
            throw new ClienteNoEncontradoException("No existe un cliente con el codigo " + codigo);
        }
        for (Pedido pedido : pedidos) {
            if (pedido.getCliente().getCodigo() == codigo) {
                throw new IllegalArgumentException("El cliente tiene pedidos registrados y no se puede eliminar");
            }
        }
        clientes.remove(codigo);
    }

    /**
     * Lista los clientes en orden de codigo, que es el recorrido en orden del arbol.
     */
    public String listarClientes() {
        if (clientes.isEmpty()) {
            return "No hay clientes registrados.";
        }
        StringBuilder sb = new StringBuilder();
        for (Cliente cliente : clientes.values()) {
            sb.append(cliente).append("\n");
        }
        sb.append("Primer codigo: ").append(clientes.firstKey())
                .append(" - Ultimo codigo: ").append(clientes.lastKey());
        return sb.toString();
    }

    /**
     * @throws IllegalArgumentException si ya existe un repartidor con ese legajo
     */
    public void registrarRepartidor(Repartidor repartidor) {
        for (Repartidor existente : repartidores) {
            if (existente.getLegajo() == repartidor.getLegajo()) {
                throw new IllegalArgumentException("Ya existe un repartidor con el legajo " + repartidor.getLegajo());
            }
        }
        repartidores.add(repartidor);
    }

    /**
     * @throws IllegalArgumentException si no existe
     */
    public Repartidor buscarRepartidor(int legajo) {
        for (Repartidor repartidor : repartidores) {
            if (repartidor.getLegajo() == legajo) {
                return repartidor;
            }
        }
        throw new IllegalArgumentException("No existe un repartidor con el legajo " + legajo);
    }

    /**
     * Elimina un repartidor de la lista. No se puede eliminar si tiene pedidos asignados.
     *
     * @throws IllegalArgumentException si no existe o tiene pedidos asignados
     */
    public void eliminarRepartidor(int legajo) {
        Repartidor repartidor = buscarRepartidor(legajo);
        if (repartidor.getCantidadPedidos() > 0) {
            throw new IllegalArgumentException("El repartidor tiene pedidos asignados y no se puede eliminar");
        }
        repartidores.remove(repartidor);
    }

    public String listarRepartidores() {
        if (repartidores.isEmpty()) {
            return "No hay repartidores registrados.";
        }
        StringBuilder sb = new StringBuilder();
        for (Repartidor repartidor : repartidores) {
            sb.append(repartidor).append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * Muestra el historial de entregas de un repartidor, en el orden en que las realizo.
     */
    public String listarHistorial(int legajo) {
        Repartidor repartidor = buscarRepartidor(legajo);
        List<Pedido> historial = repartidor.getHistorialEntregas();
        if (historial.isEmpty()) {
            return "El repartidor " + repartidor.getNombre() + " todavia no registro entregas.";
        }
        StringBuilder sb = new StringBuilder("Historial de " + repartidor.getNombre() + ":\n");
        for (Pedido pedido : historial) {
            sb.append("   ").append(pedido).append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * Muestra la comision de cada repartidor usando la interfaz Facturable.
     */
    public String resumenComisiones() {
        StringBuilder sb = new StringBuilder("Comisiones (10% de lo entregado):\n");
        for (Repartidor repartidor : repartidores) {
            Facturable facturable = repartidor;
            sb.append(String.format("   %s: $%.2f", repartidor.getNombre(), facturable.calcularImporte())).append("\n");
        }
        return sb.toString().trim();
    }

    /**
     * Registra un pedido: lo agrega a la lista, lo encola en la cola que corresponde (urgentes o normales)
     * y apila la operacion para poder deshacerla.
     *
     * @throws PedidoInvalidoException si ya existe un pedido con ese codigo
     */
    public void registrarPedido(Pedido pedido) throws PedidoInvalidoException {
        if (Busqueda.buscarPedidoLineal(pedidos, pedido.getCodigo()) != -1) {
            throw new PedidoInvalidoException("Ya existe un pedido con el codigo " + pedido.getCodigo());
        }
        pedidos.add(pedido);
        if (pedido.esUrgente()) {
            colaUrgentes.offer(pedido);
        } else {
            colaNormales.offer(pedido);
        }
        pilaOperaciones.push(new Operacion(TipoOperacion.ALTA_PEDIDO, pedido, null, EstadoPedido.PENDIENTE));
    }

    /**
     * Atiende el siguiente pedido: toma el primero de la cola de urgentes (o de la de normales si no hay urgentes),
     * lo asigna al primer repartidor cuyo vehiculo pueda transportarlo y lo pasa a EN_CAMINO.
     * El pedido solo sale de la cola si la asignacion fue exitosa.
     *
     * @return descripcion de lo realizado
     * @throws SinPedidosPendientesException si ambas colas estan vacias
     * @throws CapacidadExcedidaException si ningun repartidor puede transportar el pedido
     */
    public String atenderSiguiente() throws SinPedidosPendientesException, CapacidadExcedidaException {
        Deque<Pedido> cola = colaUrgentes.isEmpty() ? colaNormales : colaUrgentes;
        Pedido pedido = cola.peek();
        if (pedido == null) {
            throw new SinPedidosPendientesException("No hay pedidos pendientes para atender");
        }
        Repartidor repartidor = buscarRepartidorDisponible(pedido);
        repartidor.asignarPedido(pedido);
        pedido.cambiarEstado(EstadoPedido.EN_CAMINO);
        cola.poll();
        pilaOperaciones.push(new Operacion(TipoOperacion.ASIGNACION, pedido, repartidor, EstadoPedido.PENDIENTE));
        return "Pedido #" + pedido.getCodigo() + " asignado a " + repartidor.getNombre() + " ("
                + repartidor.getVehiculo().getClass().getSimpleName() + ") y puesto EN_CAMINO";
    }

    private Repartidor buscarRepartidorDisponible(Pedido pedido) throws CapacidadExcedidaException {
        for (Repartidor repartidor : repartidores) {
            if (repartidor.getVehiculo().puedeTransportar(pedido)) {
                return repartidor;
            }
        }
        throw new CapacidadExcedidaException("Ningun repartidor tiene un vehiculo que pueda transportar el pedido #"
                + pedido.getCodigo());
    }

    /**
     * Registra la entrega de un pedido a traves del repartidor que lo tiene a cargo, lo agrega al historial
     * de ese repartidor y apila la operacion.
     *
     * @throws PedidoInvalidoException si el pedido no existe o todavia no esta asignado
     * @throws EstadoInvalidoException si el pedido no esta EN_CAMINO
     */
    public void registrarEntrega(int codigoPedido) throws PedidoInvalidoException {
        Pedido pedido = buscarPedidoLineal(codigoPedido);
        for (Repartidor repartidor : repartidores) {
            if (repartidor.tienePedido(pedido)) {
                repartidor.registrarEntrega(pedido);
                pilaOperaciones.push(new Operacion(TipoOperacion.CAMBIO_ESTADO, pedido, repartidor,
                        EstadoPedido.EN_CAMINO));
                return;
            }
        }
        throw new PedidoInvalidoException("El pedido #" + codigoPedido + " todavia no esta asignado a un repartidor");
    }

    /**
     * Deshace la ultima operacion apilada (pop) y revierte sus efectos: una alta se elimina de la lista y de su cola,
     * una asignacion libera al repartidor y devuelve el pedido al frente de su cola, y una entrega
     * se quita del historial y el pedido vuelve a EN_CAMINO.
     *
     * @return descripcion de lo deshecho
     * @throws NadaParaDeshacerException si la pila esta vacia
     */
    public String deshacerUltima() throws NadaParaDeshacerException {
        if (pilaOperaciones.isEmpty()) {
            throw new NadaParaDeshacerException("No hay operaciones para deshacer");
        }
        Operacion operacion = pilaOperaciones.pop();
        Pedido pedido = operacion.getPedido();
        switch (operacion.getTipo()) {
            case ALTA_PEDIDO:
                pedidos.remove(pedido);
                colaUrgentes.remove(pedido);
                colaNormales.remove(pedido);
                return "Se deshizo el alta del pedido #" + pedido.getCodigo();
            case ASIGNACION:
                operacion.getRepartidor().quitarPedido(pedido);
                pedido.restaurarEstado(operacion.getEstadoAnterior());
                if (pedido.esUrgente()) {
                    colaUrgentes.offerFirst(pedido);
                } else {
                    colaNormales.offerFirst(pedido);
                }
                return "Se deshizo la asignacion del pedido #" + pedido.getCodigo()
                        + ": volvio al frente de su cola como PENDIENTE";
            default:
                operacion.getRepartidor().quitarEntrega(pedido);
                pedido.restaurarEstado(operacion.getEstadoAnterior());
                return "Se deshizo la entrega del pedido #" + pedido.getCodigo() + ": volvio a EN_CAMINO";
        }
    }

    /**
     * Busqueda lineal de un pedido por codigo.
     *
     * @throws PedidoInvalidoException si no existe
     */
    public Pedido buscarPedidoLineal(int codigo) throws PedidoInvalidoException {
        int posicion = Busqueda.buscarPedidoLineal(pedidos, codigo);
        if (posicion == -1) {
            throw new PedidoInvalidoException("No existe un pedido con el codigo " + codigo);
        }
        return pedidos.get(posicion);
    }

    /**
     * Busqueda binaria de un pedido por codigo. Como la lista de pedidos esta en orden de llegada,
     * primero se ordena una copia por codigo con mergesort, y sobre esa copia se aplica la busqueda binaria.
     *
     * @param recursiva true para usar la version recursiva, false para la iterativa
     * @throws PedidoInvalidoException si no existe
     */
    public Pedido buscarPedidoBinaria(int codigo, boolean recursiva) throws PedidoInvalidoException {
        List<Pedido> ordenada = Ordenamiento.mergeSort(pedidos, Criterios.POR_CODIGO);
        int posicion;
        if (recursiva) {
            posicion = Busqueda.buscarBinariaRecursiva(ordenada, codigo);
        } else {
            posicion = Busqueda.buscarBinariaIterativa(ordenada, codigo);
        }
        if (posicion == -1) {
            throw new PedidoInvalidoException("No existe un pedido con el codigo " + codigo);
        }
        return ordenada.get(posicion);
    }

    /**
     * @return cantidad de comparaciones de la ultima busqueda realizada
     */
    public int getUltimasComparaciones() {
        return Busqueda.getComparaciones();
    }

    /**
     * Devuelve los pedidos ordenados con el criterio y el algoritmo elegidos.
     *
     * @param mergeSort true para mergesort, false para burbuja
     */
    public List<Pedido> ordenarPedidos(Comparator<Pedido> criterio, boolean mergeSort) {
        if (mergeSort) {
            return Ordenamiento.mergeSort(pedidos, criterio);
        }
        return Ordenamiento.burbuja(pedidos, criterio);
    }

    /**
     * Devuelve los pedidos de un turno ordenados por el criterio. El turno manana va hasta las 13:59
     * y el turno tarde desde las 14:00, segun la hora de ingreso del pedido.
     */
    public List<Pedido> pedidosDelTurno(boolean manana, Comparator<Pedido> criterio) {
        List<Pedido> turno = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            boolean esManana = pedido.getFechaHora().getHour() < HORA_CORTE_TURNO;
            if (esManana == manana) {
                turno.add(pedido);
            }
        }
        return Ordenamiento.mergeSort(turno, criterio);
    }

    /**
     * Intercala en una sola lista ordenada los pedidos del turno manana y del turno tarde.
     */
    public List<Pedido> intercalarTurnos(Comparator<Pedido> criterio) {
        List<Pedido> manana = pedidosDelTurno(true, criterio);
        List<Pedido> tarde = pedidosDelTurno(false, criterio);
        return Intercalacion.intercalar(manana, tarde, criterio);
    }

    /**
     * Describe una lista de pedidos con su costo y tiempo estimado. Usa polimorfismo: llama a calcularCosto
     * y calcularTiempoEntrega sin distinguir el tipo concreto de pedido ni de vehiculo.
     */
    public String describirPedidos(List<Pedido> lista) {
        if (lista.isEmpty()) {
            return "   (sin pedidos)";
        }
        StringBuilder sb = new StringBuilder();
        for (Pedido pedido : lista) {
            Vehiculo vehiculo = pedido.vehiculoParaCalculo();
            String nota = "";
            if (pedido.getVehiculo() == null) {
                nota = " (sin asignar: moto de referencia)";
            }
            sb.append(pedido).append("\n");
            sb.append(String.format("   Costo: $%.2f | Tiempo estimado: %.1f min%s",
                    pedido.calcularCosto(vehiculo),
                    vehiculo.calcularTiempoEntrega(pedido.getDistanciaKm()),
                    nota)).append("\n");
        }
        return sb.toString().trim();
    }

    public String consultarEstadoPedido(int codigo) throws PedidoInvalidoException {
        Rastreable rastreable = buscarPedidoLineal(codigo);
        return rastreable.obtenerEstadoActual();
    }

    /**
     * Resume el contenido de las colas de pendientes y la cantidad de operaciones que se pueden deshacer.
     */
    public String estadoColas() {
        return "Cola de urgentes " + codigos(colaUrgentes) + " | Cola de normales " + codigos(colaNormales)
                + " | Operaciones para deshacer: " + pilaOperaciones.size();
    }

    private String codigos(Deque<Pedido> cola) {
        StringBuilder sb = new StringBuilder("(" + cola.size() + "):");
        for (Pedido pedido : cola) {
            sb.append(" #").append(pedido.getCodigo());
        }
        return sb.toString();
    }

    /**
     * Precarga 5 clientes, 3 repartidores y 8 pedidos de los tres tipos, repartidos en los dos turnos.
     * Usa las mismas operaciones del sistema: se registran los 8 pedidos, se atienden los dos primeros
     * (los urgentes 2 y 5) y se entrega el pedido 2. Al final se vacia la pila, por lo que esta carga
     * inicial no se puede deshacer.
     */
    public void cargarDatosPrueba() throws ClienteNoEncontradoException, PedidoInvalidoException,
            CapacidadExcedidaException, SinPedidosPendientesException {
        registrarCliente(new Cliente(3, "Maria Lopez", "2657-333333", "Terminal"));
        registrarCliente(new Cliente(1, "Ana Perez", "2657-111111", "Centro"));
        registrarCliente(new Cliente(5, "Sofia Ruiz", "2657-555555", "Hospital"));
        registrarCliente(new Cliente(2, "Luis Gomez", "2657-222222", "Barrio Norte"));
        registrarCliente(new Cliente(4, "Carlos Diaz", "2657-444444", "Parque"));

        registrarRepartidor(new Repartidor(101, "Juan Torres", new Bicicleta("BIC-001")));
        registrarRepartidor(new Repartidor(102, "Laura Mendez", new Moto("MOT-001")));
        registrarRepartidor(new Repartidor(103, "Pedro Salas", new Camioneta("CAM-001")));

        LocalDate hoy = LocalDate.now();
        registrarPedido(new PedidoNormal(1, buscarCliente(1), "Centro", 2.0, 3.0, hoy.atTime(8, 30)));
        registrarPedido(new PedidoUrgente(2, buscarCliente(2), "Barrio Norte", 5.0, 6.5, hoy.atTime(9, 15)));
        registrarPedido(new PedidoFragil(3, buscarCliente(3), "Terminal", 8.0, 4.0, hoy.atTime(10, 40)));
        registrarPedido(new PedidoNormal(4, buscarCliente(4), "Parque", 1.5, 2.5, hoy.atTime(11, 20)));
        registrarPedido(new PedidoUrgente(5, buscarCliente(5), "Hospital", 12.0, 8.0, hoy.atTime(14, 10)));
        registrarPedido(new PedidoFragil(6, buscarCliente(1), "Plaza", 20.0, 5.0, hoy.atTime(15, 0)));
        registrarPedido(new PedidoNormal(7, buscarCliente(2), "Zona Industrial", 150.0, 12.0, hoy.atTime(15, 45)));
        registrarPedido(new PedidoUrgente(8, buscarCliente(3), "Universidad", 3.0, 3.5, hoy.atTime(16, 30)));

        atenderSiguiente();
        atenderSiguiente();
        registrarEntrega(2);
        pilaOperaciones.clear();
    }
}
