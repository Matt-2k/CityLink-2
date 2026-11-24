package modelo;

/**
 * Cliente de CityLink. Todos los atributos son privados y los setters validan los datos,
 * lanzando IllegalArgumentException si son invalidos.
 */
public class Cliente {
    private int codigo;
    private String nombre;
    private String telefono;
    private String zona;

    /**
     * Constructor vacio: inicializa los atributos con valores por defecto sin validar.
     */
    public Cliente() {
        this.codigo = 0;
        this.nombre = "";
        this.telefono = "";
        this.zona = "";
    }

    /**
     * Constructor completo: asigna los atributos a traves de los setters, por lo que valida los datos.
     */
    public Cliente(int codigo, String nombre, String telefono, String zona) {
        setCodigo(codigo);
        setNombre(nombre);
        setTelefono(telefono);
        setZona(zona);
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        if (codigo <= 0) {
            throw new IllegalArgumentException("El codigo del cliente debe ser mayor que cero");
        }
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio");
        }
        this.nombre = nombre.trim();
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("El telefono no puede estar vacio");
        }
        this.telefono = telefono.trim();
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        if (zona == null || zona.trim().isEmpty()) {
            throw new IllegalArgumentException("La zona no puede estar vacia");
        }
        this.zona = zona.trim();
    }

    @Override
    public String toString() {
        return "Cliente #" + codigo + " - " + nombre + " - Tel: " + telefono + " - Zona: " + zona;
    }
}
