package org.ol.model;

/**
 * Representa una línea de una factura, reuniendo en un solo objeto los datos
 * de la venta, del cliente, del libro vendido y del usuario que atendió la
 * transacción. Se utiliza para mostrar o imprimir facturas, no como entidad
 * de una sola tabla.
 *
 * @author Octavio Letona
 * @version 1.0.0
 * @see Venta
 * @see DetalleVenta
 * @see Cliente
 * @see Libro
 * @see Usuario
 */
public class LineaFactura {

    /** Número de la factura; corresponde al número de la venta. */
    private int numeroFactura;

    /** Fecha de emisión de la factura, almacenada como texto. */
    private String fechaEmision;

    /** CUI del cliente al que se le emite la factura. */
    private long cuiCliente;

    /** Nombre completo del cliente. */
    private String nombreCliente;

    /** Correo electrónico del cliente. */
    private String correoCliente;

    /** ISBN del libro incluido en esta línea. */
    private String isbnLibro;

    /** Título del libro incluido en esta línea. */
    private String tituloLibro;

    /** Cantidad de ejemplares del libro en esta línea. */
    private int cantidad;

    /** Precio unitario del libro en esta línea. */
    private double precioUnitario;

    /** Subtotal de la línea (cantidad por precio unitario). */
    private double subtotal;

    /** Usuario que atendió la venta. */
    private String usuarioAtendio;

    /** Total general de la factura. */
    private double granTotal;

    /**
     * Constructor vacío. Crea una línea de factura sin datos inicializados;
     * los valores se asignan mediante los métodos setter.
     */
    public LineaFactura() {
    }

    /**
     * Obtiene el número de la factura.
     *
     * @return el número de factura
     */
    public int getNumeroFactura() {
        return numeroFactura;
    }

    /**
     * Establece el número de la factura.
     *
     * @param numeroFactura nuevo número de factura
     */
    public void setNumeroFactura(int numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    /**
     * Obtiene la fecha de emisión de la factura.
     *
     * @return la fecha de emisión, como texto
     */
    public String getFechaEmision() {
        return fechaEmision;
    }

    /**
     * Establece la fecha de emisión de la factura.
     *
     * @param fechaEmision nueva fecha de emisión, como texto
     */
    public void setFechaEmision(String fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    /**
     * Obtiene el CUI del cliente.
     *
     * @return el CUI del cliente
     */
    public long getCuiCliente() {
        return cuiCliente;
    }

    /**
     * Establece el CUI del cliente.
     *
     * @param cuiCliente nuevo CUI del cliente; debe ser un valor numérico positivo
     */
    public void setCuiCliente(long cuiCliente) {
        this.cuiCliente = cuiCliente;
    }

    /**
     * Obtiene el nombre completo del cliente.
     *
     * @return el nombre del cliente
     */
    public String getNombreCliente() {
        return nombreCliente;
    }

    /**
     * Establece el nombre completo del cliente.
     *
     * @param nombreCliente nuevo nombre del cliente
     */
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    /**
     * Obtiene el correo electrónico del cliente.
     *
     * @return el correo del cliente
     */
    public String getCorreoCliente() {
        return correoCliente;
    }

    /**
     * Establece el correo electrónico del cliente.
     *
     * @param correoCliente nuevo correo del cliente; se espera un formato válido (usuario@dominio)
     */
    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }

    /**
     * Obtiene el ISBN del libro de esta línea.
     *
     * @return el ISBN del libro
     */
    public String getIsbnLibro() {
        return isbnLibro;
    }

    /**
     * Establece el ISBN del libro de esta línea.
     *
     * @param isbnLibro nuevo ISBN del libro
     */
    public void setIsbnLibro(String isbnLibro) {
        this.isbnLibro = isbnLibro;
    }

    /**
     * Obtiene el título del libro de esta línea.
     *
     * @return el título del libro
     */
    public String getTituloLibro() {
        return tituloLibro;
    }

    /**
     * Establece el título del libro de esta línea.
     *
     * @param tituloLibro nuevo título del libro
     */
    public void setTituloLibro(String tituloLibro) {
        this.tituloLibro = tituloLibro;
    }

    /**
     * Obtiene la cantidad de ejemplares de esta línea.
     *
     * @return la cantidad de ejemplares
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad de ejemplares de esta línea.
     *
     * @param cantidad nueva cantidad; debe ser mayor que cero
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el precio unitario del libro en esta línea.
     *
     * @return el precio unitario
     */
    public double getPrecioUnitario() {
        return precioUnitario;
    }

    /**
     * Establece el precio unitario del libro en esta línea.
     *
     * @param precioUnitario nuevo precio unitario; no puede ser negativo
     */
    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    /**
     * Obtiene el subtotal de la línea.
     *
     * @return el subtotal de la línea
     */
    public double getSubtotal() {
        return subtotal;
    }

    /**
     * Establece el subtotal de la línea.
     *
     * @param subtotal nuevo subtotal; no puede ser negativo
     */
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * Obtiene el usuario que atendió la venta.
     *
     * @return el usuario que atendió la venta
     */
    public String getUsuarioAtendio() {
        return usuarioAtendio;
    }

    /**
     * Establece el usuario que atendió la venta.
     *
     * @param usuarioAtendio nuevo usuario que atendió la venta
     */
    public void setUsuarioAtendio(String usuarioAtendio) {
        this.usuarioAtendio = usuarioAtendio;
    }

    /**
     * Obtiene el total general de la factura.
     *
     * @return el gran total de la factura
     */
    public double getGranTotal() {
        return granTotal;
    }

    /**
     * Establece el total general de la factura.
     *
     * @param granTotal nuevo gran total; no puede ser negativo
     */
    public void setGranTotal(double granTotal) {
        this.granTotal = granTotal;
    }
}