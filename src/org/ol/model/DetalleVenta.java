package org.ol.model;

/**
 * Representa una línea de detalle de una venta: un libro específico que se
 * vendió, junto con la cantidad y el precio aplicado. Una {@link Venta}
 * puede tener varios detalles, uno por cada libro distinto vendido.
 *
 * @author Octavio Letona
 * @version 1.0.0
 * @see Venta
 * @see Libro
 */
public class DetalleVenta {

    /** Identificador único del detalle de venta. */
    private int idDetalleVenta;

    /** Número de la venta a la que pertenece este detalle; referencia a {@link Venta#getNoVenta()}. */
    private int noVenta;

    /** ISBN del libro vendido; referencia a {@link Libro#getIsbn()}. */
    private String isbn;

    /** Cantidad de ejemplares vendidos del libro. */
    private int cantidad;

    /** Precio unitario del libro aplicado en esta venta. */
    private double precio;

    /**
     * Constructor vacío. Crea un detalle de venta sin datos inicializados;
     * los valores se pueden asignar posteriormente mediante los métodos setter.
     */
    public DetalleVenta() {
    }

    /**
     * Constructor parametrizado. Crea un detalle de venta con todos sus datos.
     *
     * @param idDetalleVenta identificador único del detalle; debe ser un entero positivo
     * @param noVenta        número de la venta a la que pertenece; debe existir en la tabla de ventas
     * @param isbn           ISBN del libro vendido; debe existir en la tabla de libros
     * @param cantidad       cantidad de ejemplares vendidos; debe ser mayor que cero
     * @param precio         precio unitario aplicado; no puede ser negativo
     */
    public DetalleVenta(int idDetalleVenta, int noVenta, String isbn, int cantidad, double precio) {
        this.idDetalleVenta = idDetalleVenta;
        this.noVenta = noVenta;
        this.isbn = isbn;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    /**
     * Obtiene el identificador único del detalle de venta.
     *
     * @return el identificador del detalle de venta
     */
    public int getIdDetalleVenta() {
        return idDetalleVenta;
    }

    /**
     * Establece el identificador único del detalle de venta.
     *
     * @param idDetalleVenta nuevo identificador del detalle; debe ser un entero positivo
     */
    public void setIdDetalleVenta(int idDetalleVenta) {
        this.idDetalleVenta = idDetalleVenta;
    }

    /**
     * Obtiene el número de la venta a la que pertenece el detalle.
     *
     * @return el número de venta
     */
    public int getNoVenta() {
        return noVenta;
    }

    /**
     * Establece el número de la venta a la que pertenece el detalle.
     *
     * @param noVenta nuevo número de venta; debe existir en la tabla de ventas
     */
    public void setNoVenta(int noVenta) {
        this.noVenta = noVenta;
    }

    /**
     * Obtiene el ISBN del libro vendido.
     *
     * @return el ISBN del libro
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Establece el ISBN del libro vendido.
     *
     * @param isbn nuevo ISBN del libro; debe existir en la tabla de libros
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Obtiene la cantidad de ejemplares vendidos.
     *
     * @return la cantidad vendida
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad de ejemplares vendidos.
     *
     * @param cantidad nueva cantidad vendida; debe ser mayor que cero
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el precio unitario aplicado en la venta.
     *
     * @return el precio unitario del libro
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio unitario aplicado en la venta.
     *
     * @param precio nuevo precio unitario; no puede ser negativo
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }
}