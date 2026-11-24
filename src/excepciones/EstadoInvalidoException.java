package excepciones;

/**
 * Excepcion unchecked (extiende RuntimeException) que se lanza ante una transicion de estado
 * no permitida. Al ser unchecked no obliga a declarar throws en los metodos que la lanzan.
 */
public class EstadoInvalidoException extends RuntimeException {
    public EstadoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
