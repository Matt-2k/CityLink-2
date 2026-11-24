package app;

import algoritmos.Criterios;
import excepciones.CapacidadExcedidaException;
import excepciones.ClienteNoEncontradoException;
import excepciones.EstadoInvalidoException;
import excepciones.NadaParaDeshacerException;
import excepciones.PedidoInvalidoException;
import excepciones.SinPedidosPendientesException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import modelo.Bicicleta;
import modelo.Camioneta;
import modelo.CityLink;
import modelo.Cliente;
import modelo.Moto;
import modelo.Pedido;
import modelo.PedidoFragil;
import modelo.PedidoNormal;
import modelo.PedidoUrgente;
import modelo.Repartidor;
import modelo.Vehiculo;

/**
 * Menu de consola de CityLink. Se ocupa unicamente de la lectura por teclado y de mostrar resultados;
 * la logica del negocio esta en CityLink y los algoritmos en el paquete algoritmos.
 */
public class Menu {
    private final Scanner teclado = new Scanner(System.in);
    private final CityLink gestor = new CityLink();

    /**
     * Carga los datos de prueba y ejecuta el menu hasta que el usuario elige salir.
     * Captura las excepciones del modelo para informar el error y seguir funcionando.
     * El bloque finally se ejecuta despues de cada operacion, haya o no error.
     */
    public void iniciar() {
        try {
            gestor.cargarDatosPrueba();
            System.out.println("Datos de prueba cargados correctamente.");
        } catch (ClienteNoEncontradoException | PedidoInvalidoException | CapacidadExcedidaException
                | SinPedidosPendientesException e) {
            System.out.println("Error al cargar los datos de prueba: " + e.getMessage());
        }

        int opcion = 0;
        do {
            mostrarMenu();
            opcion = leerEntero("Elija una opcion: ");
            try {
                ejecutar(opcion);
            } catch (ClienteNoEncontradoException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (PedidoInvalidoException e) {
                System.out.println("Pedido invalido: " + e.getMessage());
            } catch (CapacidadExcedidaException e) {
                System.out.println("No se puede asignar: " + e.getMessage());
            } catch (SinPedidosPendientesException e) {
                System.out.println("Sin pedidos: " + e.getMessage());
            } catch (NadaParaDeshacerException e) {
                System.out.println("Nada para deshacer: " + e.getMessage());
            } catch (EstadoInvalidoException e) {
                System.out.println("Estado invalido: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Dato invalido: " + e.getMessage());
            } finally {
                System.out.println("----------------------------------------");
            }
        } while (opcion != 10);
        System.out.println("Programa finalizado.");
    }

    private void mostrarMenu() {
        System.out.println("===== CITYLINK =====");
        System.out.println("1. Gestion de clientes");
        System.out.println("2. Gestion de repartidores");
        System.out.println("3. Registrar un pedido");
        System.out.println("4. Atender el siguiente pedido");
        System.out.println("5. Registrar una entrega");
        System.out.println("6. Deshacer la ultima operacion");
        System.out.println("7. Ordenar y listar pedidos");
        System.out.println("8. Buscar un pedido por codigo");
        System.out.println("9. Intercalar los pedidos de dos turnos");
        System.out.println("10. Salir");
    }

    private void ejecutar(int opcion) throws ClienteNoEncontradoException, PedidoInvalidoException,
            CapacidadExcedidaException, SinPedidosPendientesException, NadaParaDeshacerException {
        switch (opcion) {
            case 1:
                gestionClientes();
                break;
            case 2:
                gestionRepartidores();
                break;
            case 3:
                registrarPedido();
                System.out.println(gestor.estadoColas());
                break;
            case 4:
                System.out.println(gestor.atenderSiguiente());
                System.out.println(gestor.estadoColas());
                break;
            case 5:
                gestor.registrarEntrega(leerEntero("Codigo del pedido entregado: "));
                System.out.println("Entrega registrada y agregada al historial del repartidor.");
                System.out.println(gestor.estadoColas());
                break;
            case 6:
                System.out.println(gestor.deshacerUltima());
                System.out.println(gestor.estadoColas());
                break;
            case 7:
                ordenarYListar();
                break;
            case 8:
                buscarPedido();
                break;
            case 9:
                intercalarTurnos();
                break;
            case 10:
                break;
            default:
                System.out.println("Opcion no valida. Elija un numero del 1 al 10.");
        }
    }

    private void gestionClientes() throws ClienteNoEncontradoException {
        System.out.println("Clientes: 1. Alta  2. Baja  3. Buscar por codigo (arbol)  4. Buscar por nombre (lineal)  5. Listado ordenado");
        int opcion = leerOpcion("Opcion: ", 1, 5);
        switch (opcion) {
            case 1:
                int codigo = leerEntero("Codigo: ");
                String nombre = leerTexto("Nombre: ");
                String telefono = leerTexto("Telefono: ");
                String zona = leerTexto("Zona: ");
                gestor.registrarCliente(new Cliente(codigo, nombre, telefono, zona));
                System.out.println("Cliente registrado.");
                break;
            case 2:
                gestor.eliminarCliente(leerEntero("Codigo del cliente a eliminar: "));
                System.out.println("Cliente eliminado.");
                break;
            case 3:
                System.out.println(gestor.buscarCliente(leerEntero("Codigo: ")));
                break;
            case 4:
                System.out.println(gestor.buscarCliente(leerTexto("Nombre: ")));
                System.out.println("Comparaciones realizadas: " + gestor.getUltimasComparaciones());
                break;
            default:
                System.out.println(gestor.listarClientes());
        }
    }

    private void gestionRepartidores() {
        System.out.println("Repartidores: 1. Alta  2. Baja  3. Listado  4. Historial de entregas  5. Comisiones");
        int opcion = leerOpcion("Opcion: ", 1, 5);
        switch (opcion) {
            case 1:
                registrarRepartidor();
                break;
            case 2:
                gestor.eliminarRepartidor(leerEntero("Legajo del repartidor a eliminar: "));
                System.out.println("Repartidor eliminado.");
                break;
            case 3:
                System.out.println(gestor.listarRepartidores());
                break;
            case 4:
                System.out.println(gestor.listarHistorial(leerEntero("Legajo: ")));
                break;
            default:
                System.out.println(gestor.resumenComisiones());
        }
    }

    private void registrarRepartidor() {
        int legajo = leerEntero("Legajo: ");
        String nombre = leerTexto("Nombre: ");
        System.out.println("Vehiculo: 1. Bicicleta  2. Moto  3. Camioneta");
        int tipo = leerOpcion("Tipo de vehiculo: ", 1, 3);
        String patente = leerTexto("Patente: ");
        Vehiculo vehiculo;
        switch (tipo) {
            case 1:
                vehiculo = new Bicicleta(patente);
                break;
            case 2:
                vehiculo = new Moto(patente);
                break;
            default:
                vehiculo = new Camioneta(patente);
        }
        gestor.registrarRepartidor(new Repartidor(legajo, nombre, vehiculo));
        System.out.println("Repartidor registrado.");
    }

    private void registrarPedido() throws ClienteNoEncontradoException, PedidoInvalidoException {
        System.out.println("Tipo: 1. Normal  2. Urgente  3. Fragil");
        int tipo = leerOpcion("Tipo de pedido: ", 1, 3);
        int codigo = leerEntero("Codigo del pedido: ");
        Cliente cliente = gestor.buscarCliente(leerEntero("Codigo del cliente: "));
        String destino = leerTexto("Destino: ");
        double peso = leerDecimal("Peso en kg: ");
        double distancia = leerDecimal("Distancia en km: ");
        LocalDateTime fechaHora = leerFechaHora();
        Pedido pedido;
        switch (tipo) {
            case 1:
                pedido = new PedidoNormal(codigo, cliente, destino, peso, distancia, fechaHora);
                break;
            case 2:
                pedido = new PedidoUrgente(codigo, cliente, destino, peso, distancia, fechaHora);
                break;
            default:
                pedido = new PedidoFragil(codigo, cliente, destino, peso, distancia, fechaHora);
        }
        gestor.registrarPedido(pedido);
        System.out.println("Pedido registrado y encolado.");
    }

    private void ordenarYListar() {
        Comparator<Pedido> criterio = elegirCriterio();
        System.out.println("Algoritmo: 1. Burbuja  2. Mergesort");
        boolean mergeSort = leerOpcion("Algoritmo: ", 1, 2) == 2;
        List<Pedido> ordenados = gestor.ordenarPedidos(criterio, mergeSort);
        System.out.println(gestor.describirPedidos(ordenados));
    }

    private void buscarPedido() throws PedidoInvalidoException {
        System.out.println("Busqueda: 1. Lineal  2. Binaria iterativa  3. Binaria recursiva");
        int tipo = leerOpcion("Busqueda: ", 1, 3);
        int codigo = leerEntero("Codigo del pedido: ");
        Pedido pedido;
        switch (tipo) {
            case 1:
                pedido = gestor.buscarPedidoLineal(codigo);
                break;
            case 2:
                pedido = gestor.buscarPedidoBinaria(codigo, false);
                break;
            default:
                pedido = gestor.buscarPedidoBinaria(codigo, true);
        }
        System.out.println(pedido);
        System.out.println(gestor.consultarEstadoPedido(codigo));
        System.out.println("Comparaciones realizadas en la busqueda: " + gestor.getUltimasComparaciones());
    }

    private void intercalarTurnos() {
        Comparator<Pedido> criterio = elegirCriterio();
        System.out.println("--- Turno manana (ordenado) ---");
        System.out.println(gestor.describirPedidos(gestor.pedidosDelTurno(true, criterio)));
        System.out.println("--- Turno tarde (ordenado) ---");
        System.out.println(gestor.describirPedidos(gestor.pedidosDelTurno(false, criterio)));
        System.out.println("--- Intercalados en un solo listado ---");
        System.out.println(gestor.describirPedidos(gestor.intercalarTurnos(criterio)));
    }

    private Comparator<Pedido> elegirCriterio() {
        System.out.println("Criterio: 1. Costo  2. Distancia  3. Fecha y hora");
        int opcion = leerOpcion("Criterio: ", 1, 3);
        if (opcion == 1) {
            return Criterios.POR_COSTO;
        }
        if (opcion == 2) {
            return Criterios.POR_DISTANCIA;
        }
        return Criterios.POR_FECHA;
    }

    private LocalDateTime leerFechaHora() {
        while (true) {
            System.out.print("Hora de ingreso (HH:mm, por ejemplo 09:30) o Enter para la actual: ");
            String texto = teclado.nextLine().trim();
            if (texto.isEmpty()) {
                return LocalDateTime.now();
            }
            try {
                return LocalDate.now().atTime(LocalTime.parse(texto));
            } catch (DateTimeParseException e) {
                System.out.println("Formato invalido. Use HH:mm, por ejemplo 14:30.");
            }
        }
    }

    /**
     * Lee un entero por teclado. Si el texto no es un numero, captura la excepcion y vuelve a pedir el dato.
     */
    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Integer.parseInt(teclado.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero entero.");
            }
        }
    }

    private double leerDecimal(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Double.parseDouble(teclado.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero (por ejemplo 2.5).");
            }
        }
    }

    private int leerOpcion(String mensaje, int minimo, int maximo) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor >= minimo && valor <= maximo) {
                return valor;
            }
            System.out.println("Ingrese un numero entre " + minimo + " y " + maximo + ".");
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine();
    }
}
