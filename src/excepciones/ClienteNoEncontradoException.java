package excepciones;

/**
 * Excepcion checked que se lanza al buscar un cliente que no existe en el sistema.
 */
public class ClienteNoEncontradoException extends Exception {
    public ClienteNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
