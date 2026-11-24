package excepciones;

/**
 * Excepcion checked que se lanza al intentar deshacer una operacion cuando la pila esta vacia.
 */
public class NadaParaDeshacerException extends Exception {
    public NadaParaDeshacerException(String mensaje) {
        super(mensaje);
    }
}
