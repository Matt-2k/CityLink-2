# CityLink - Sistema de gestion de reparto urbano

Proyecto Integrador - Programacion I 2026 - Entregable 2: estructuras dinamicas de Java y algoritmos

## Integrantes

- Matias Escudero

## Estructura del proyecto

```
CityLink/
  src/
    app/          Main, Menu y Pruebas
    algoritmos/   Busqueda, Ordenamiento, Intercalacion y Criterios (escritos por el grupo)
    excepciones/  excepciones propias
    modelo/       clases del negocio, interfaces, enums y el gestor CityLink
```

## Como compilar y ejecutar

Desde la carpeta CityLink:

```
javac -d out src/excepciones/*.java src/algoritmos/*.java src/modelo/*.java src/app/*.java
java -cp out app.Main
java -cp out app.Pruebas
```

`app.Main` inicia el sistema con datos de prueba. `app.Pruebas` ejecuta las pruebas de los algoritmos y de las estructuras. En Visual Studio Code: instalar "Extension Pack for Java", abrir la carpeta CityLink y ejecutar `app/Main.java` o `app/Pruebas.java` con el boton Run.

## Que cambio respecto del Entregable 1

Se conserva el modelo de objetos (herencia, abstraccion, interfaces, polimorfismo) y las excepciones. Los arreglos se reemplazaron por estructuras de `java.util` y se agregaron los algoritmos.

| Dato | Entregable 1 | Entregable 2 |
|------|--------------|--------------|
| Clientes | `Cliente[]` | `TreeMap<Integer, Cliente>` (arbol ordenado por codigo) |
| Repartidores | `Repartidor[]` | `ArrayList<Repartidor>` |
| Pedidos | `Pedido[]` | `ArrayList<Pedido>` |
| Pedidos de un repartidor | `Pedido[]` | `ArrayList<Pedido>` asignados y `ArrayList<Pedido>` con el historial de entregas |
| Pedidos pendientes | no existia | dos colas `ArrayDeque<Pedido>`: urgentes y normales |
| Deshacer | no existia | pila `ArrayDeque<Operacion>` |

## Estructuras dinamicas usadas

| Estructura | Clase | Donde | Para que |
|------------|-------|-------|----------|
| Lista | `ArrayList` (interfaz `List`) | `CityLink`, `Repartidor` | Repartidores, pedidos, pedidos asignados, historial de entregas y listas de cada turno |
| Pila | `ArrayDeque` (interfaz `Deque`) | `CityLink.pilaOperaciones` | Deshacer: `push` al registrar, `pop` al deshacer |
| Cola | `ArrayDeque` (interfaz `Deque`) | `CityLink.colaUrgentes` y `colaNormales` | Pedidos pendientes en orden de llegada: `offer`, `peek`, `poll` |
| Arbol de busqueda | `TreeMap` | `CityLink.clientes` | Clientes por codigo: `put`, `get`, `remove`, `containsKey`, `firstKey`, `lastKey` y recorrido en orden |

Los pedidos frageles van a la cola de normales. Al atender se saca primero de la cola de urgentes y, si esta vacia, de la de normales.

## Algoritmos escritos por el grupo

No se usa `Collections.sort`, `List.sort` ni `binarySearch`.

| Algoritmo | Clase y metodo | Descripcion |
|-----------|----------------|-------------|
| Busqueda lineal | `Busqueda.buscarPedidoLineal`, `buscarClientePorNombre` | Revisa los elementos uno por uno. Funciona con listas desordenadas |
| Busqueda binaria iterativa | `Busqueda.buscarBinariaIterativa` | Compara con el elemento del medio y descarta la mitad. Requiere lista ordenada por codigo |
| Busqueda binaria recursiva | `Busqueda.buscarBinariaRecursiva` | Misma idea, con recursion |
| Ordenamiento burbuja | `Ordenamiento.burbuja` | Intercambia vecinos en mal orden; termina antes si una pasada no intercambia |
| Ordenamiento mergesort | `Ordenamiento.mergeSort` | Divide en dos mitades, las ordena recursivamente y las une con la intercalacion |
| Intercalacion | `Intercalacion.intercalar` | Combina dos listas ya ordenadas en un solo recorrido, sin reordenar |

El criterio de orden (costo, distancia, fecha y codigo) se pasa como `Comparator<Pedido>` y esta definido en `Criterios`. Cada busqueda guarda su cantidad de comparaciones para poder comparar la eficiencia de la lineal con la binaria.

## Valores de los vehiculos

| Vehiculo  | Capacidad | Velocidad | Costo por km | Tiempo extra |
|-----------|-----------|-----------|--------------|--------------|
| Bicicleta | 10 kg     | 15 km/h   | $50          | 0 min        |
| Moto      | 30 kg     | 40 km/h   | $120         | 5 min        |
| Camioneta | 500 kg    | 30 km/h   | $300         | 10 min       |

## Reglas de negocio

- Costo = $1000 + distancia x costo por km. Urgente: +50%. Fragil: +$800.
- Tiempo (min) = (distancia / velocidad) x 60 + tiempo extra.
- Estados: PENDIENTE a EN_CAMINO a ENTREGADO. CANCELADO solo desde PENDIENTE.
- Comision del repartidor: 10% del importe de los pedidos de su historial de entregas.

## Decisiones de diseno

- **Atender el siguiente pedido:** se asigna al primer repartidor de la lista cuyo vehiculo pueda transportarlo. Si ninguno puede, se lanza `CapacidadExcedidaException` y el pedido sigue al frente de su cola. El pedido pasa a EN_CAMINO al atenderlo.
- **Turnos:** el turno manana incluye los pedidos ingresados antes de las 14:00 y el turno tarde los ingresados desde las 14:00. Al registrar un pedido se puede indicar la hora (HH:mm) o dejar la actual.
- **Intercalar turnos:** cada turno se ordena con mergesort por el criterio elegido y luego se intercalan las dos listas ya ordenadas.
- **Busqueda binaria:** la lista de pedidos esta en orden de llegada, por lo que se ordena una copia por codigo (mergesort) antes de aplicar la busqueda binaria.
- **Deshacer:** revierte altas de pedido, asignaciones (atender) y entregas. La carga de datos de prueba no se puede deshacer porque la pila se vacia al terminar la carga.
- **Pedido sin asignar:** su costo y tiempo se calculan con una moto de referencia.
- **Un pedido solo puede salir EN_CAMINO si fue asignado** a un repartidor.
- **No se puede eliminar** un cliente con pedidos registrados ni un repartidor con pedidos asignados.
- **AlmacenamientoLlenoException:** se elimino porque las listas, colas y el arbol crecen dinamicamente y ya no existe el concepto de "lleno". Se agregaron `SinPedidosPendientesException` y `NadaParaDeshacerException` para las estructuras vacias.
- `IllegalArgumentException` se usa para datos invalidos de clientes, repartidores y vehiculos.

## Pruebas documentadas

### Pruebas automaticas de algoritmos y estructuras

Se ejecutan con `java -cp out app.Pruebas`. Resultado obtenido: **40 de 40 pruebas correctas**.

| Componente | Caso | Tipo | Resultado esperado |
|------------|------|------|--------------------|
| Busqueda lineal | Codigo 9 en `[8, 3, 9, 1, 6]` | Normal | Posicion 2 |
| Busqueda lineal | Lista vacia | Borde | -1 |
| Busqueda lineal | Un solo elemento existente | Borde | Posicion 0 |
| Busqueda lineal | Codigo inexistente | Borde | -1 |
| Busqueda binaria (iterativa y recursiva) | Codigo 6 en `[1, 3, 6, 8, 9]` | Normal | Posicion 2 |
| Busqueda binaria | Primer y ultimo elemento | Borde | Posiciones 0 y 4 |
| Busqueda binaria | Lista vacia | Borde | -1 |
| Busqueda binaria | Un solo elemento | Borde | Posicion 0 |
| Busqueda binaria | Codigo inexistente (4) | Borde | -1 |
| Burbuja y mergesort | Ordenar por codigo `[8, 3, 9, 1, 6]` | Normal | `1 3 6 8 9` |
| Burbuja y mergesort | Lista vacia | Borde | Lista vacia |
| Burbuja y mergesort | Un solo elemento | Borde | Misma lista |
| Burbuja | Lista ya ordenada | Borde | Sin cambios |
| Burbuja y mergesort | La lista original no se modifica | Normal | `8 3 9 1 6` |
| Burbuja y mergesort | Por distancia y por costo | Normal | Ambos algoritmos dan el mismo orden |
| Intercalacion | `[1, 4, 7]` con `[2, 3, 9, 10]` | Normal | `1 2 3 4 7 9 10` |
| Intercalacion | Una lista vacia, o ambas vacias | Borde | La otra lista, o lista vacia |
| Intercalacion | Un elemento en cada lista | Borde | `2 5` |
| Cola | Atender con las colas vacias | Borde | `SinPedidosPendientesException` |
| Cola | Con urgentes y normales pendientes | Normal | Sale primero el urgente |
| Cola | Ningun vehiculo puede cargar 100 kg | Borde | `CapacidadExcedidaException` y el pedido sigue en la cola |
| Pila | Deshacer con la pila vacia | Borde | `NadaParaDeshacerException` |
| Pila | Deshacer una asignacion | Normal | El pedido vuelve PENDIENTE al frente de su cola |
| Pila | Deshacer un alta | Normal | El pedido deja de existir |
| Arbol | Buscar un cliente inexistente | Borde | `ClienteNoEncontradoException` |
| Arbol | Listar sin clientes | Borde | "No hay clientes registrados." |
| Arbol | Listar con clientes cargados en desorden | Normal | Salen ordenados por codigo |
| Lista | Registrar un pedido con codigo repetido | Borde | `PedidoInvalidoException` |

### Pruebas manuales con el menu

| Opcion | Pasos | Resultado obtenido |
|--------|-------|--------------------|
| 6 | Deshacer apenas se inicia el programa | "Nada para deshacer: No hay operaciones para deshacer" |
| Cualquiera | Escribir `abc` donde se pide un numero | "Debe ingresar un numero entero." y vuelve a preguntar |
| 4 | Atender 6 veces seguidas | Salen #8 (urgente) y despues #1, #3, #4, #6 y #7 en orden de llegada, cada uno al primer repartidor que puede cargarlo |
| 4 | Atender una septima vez | "Sin pedidos: No hay pedidos pendientes para atender" |
| 3 y 4 | Registrar un pedido de 600 kg y atenderlo | "Ningun repartidor tiene un vehiculo que pueda transportar el pedido" y el pedido sigue en la cola |
| 1 | Eliminar un cliente con pedidos | "El cliente tiene pedidos registrados y no se puede eliminar" |
| 1 | Alta de un cliente con codigo repetido | "Ya existe un cliente con el codigo 1" |
| 8 | Buscar el pedido 3 (binaria) | Lo encuentra con 3 comparaciones |
| 8 | Buscar el pedido 99 | "No existe un pedido con el codigo 99" |
| 2 | Comisiones con los datos de prueba | Juan Torres $198.75, Laura Mendez $0.00, Pedro Salas $0.00 |
| 9 | Intercalar los turnos por fecha | Los 4 pedidos de la manana y los 4 de la tarde quedan en un solo listado ordenado |

## Diagrama de clases

![Diagrama de clases de CityLink](diagrama_entregable2.png)


## Declaracion de uso de IA

- Herramientas: Claude y Gemini
- Para que se uso: Se usó Claude para ayudar en el desarrollo y comprensión de las estructuras dinámicas y algoritmos, y las partes más técnicas del README.
- Como se verifico el resultado: Ejecutando las pruebas automáticas de app.Pruebas (40/40 correctas), probando manualmente las opciones del menú y revisando el código.
