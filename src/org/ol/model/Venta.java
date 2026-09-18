package org.ol.model;

/**
 * Representa el encabezado de una venta realizada en la librería: su número,
 * fecha, total, el cliente que compró y el usuario que la registró. Los
 * libros vendidos se detallan en objetos {@link DetalleVenta}.
 *
 * @author Octavio Letona
 * @version 1.0.0
 * @see DetalleVenta
 * @see Cliente
 * @see Usuario
 */
public class Venta {

    /** Número único de la venta (llave primaria). */
    private int noVenta;

    /** Fecha en que se realizó la venta, almacenada como texto. */
    private String fechaVenta;

    /** Monto total de la venta. */
    private double totalVenta;

    /** CUI del cliente que realizó la compra; referencia a {@link Cliente#getCui()}. */
    private long cuiCliente;

    /** Identificador del usuario que registró la venta; referencia a {@link Usuario#getId()}. */
    private int idUsuario;

    /**
     * Constructor vacío. Crea una venta sin datos inicializados; los
     * valores se pueden asignar posteriormente mediante los métodos setter.
     */
    public Venta() {
    }

    /**
     * Constructor parametrizado. Crea una venta con todos sus datos.
     *
     * @param noVenta    número único de la venta; debe ser un entero positivo
     * @param fechaVenta fecha de la venta, como texto
     * @param totalVenta monto total de la venta; no puede ser negativo
     * @param cuiCliente CUI del cliente; debe existir en la tabla de clientes
     * @param idUsuario  identificador del usuario que registró la venta; debe existir en la tabla de usuarios
     */
    public Venta(int noVenta, String fechaVenta, double totalVenta, long cuiCliente, int idUsuario) {
        this.noVenta = noVenta;
        this.fechaVenta = fechaVenta;
        this.totalVenta = totalVenta;
        this.cuiCliente = cuiCliente;
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene el número de la venta.
     *
     * @return el número de venta
     */
    public int getNoVenta() {
        return noVenta;
    }

    /**
     * Establece el número de la venta.
     *
     * @param noVenta nuevo número de venta; debe ser un entero positivo
     */
    public void setNoVenta(int noVenta) {
        this.noVenta = noVenta;
    }

    /**
     * Obtiene la fecha de la venta.
     *
     * @return la fecha de la venta, como texto
     */
    public String getFechaVenta() {
        return fechaVenta;
    }

    /**
     * Establece la fecha de la venta.
     *
     * @param fechaVenta nueva fecha de la venta, como texto
     */
    public void setFechaVenta(String fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    /**
     * Obtiene el monto total de la venta.
     *
     * @return el total de la venta
     */
    public double getTotalVenta() {
        return totalVenta;
    }

    /**
     * Establece el monto total de la venta.
     *
     * @param totalVenta nuevo total de la venta; no puede ser negativo
     */
    public void setTotalVenta(double totalVenta) {
        this.totalVenta = totalVenta;
    }

    /**
     * Obtiene el CUI del cliente que realizó la compra.
     *
     * @return el CUI del cliente
     */
    public long getCuiCliente() {
        return cuiCliente;
    }

    /**
     * Establece el CUI del cliente que realizó la compra.
     *
     * @param cuiCliente nuevo CUI del cliente; debe existir en la tabla de clientes
     */
    public void setCuiCliente(long cuiCliente) {
        this.cuiCliente = cuiCliente;
    }

    /**
     * Obtiene el identificador del usuario que registró la venta.
     *
     * @return el identificador del usuario
     */
    public int getIdUsuario() {
        return idUsuario;
    }
    /**
     * Establece el identificador del usuario que registró la venta.
     *
     * @param idUsuario nuevo identificador del usuario; debe existir en la tabla de usuarios
     */
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
