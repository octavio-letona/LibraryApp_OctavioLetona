package org.ol.model;
/**
 * Representa una línea de venta en el proceso de compra, la cual funciona como
 * una fila temporal en la interfaz de usuario antes de guardar los detalles
 * definitivos en la base de datos.

 * @author Octavio Letona
 * @version 1.0.0
 */
public class LineaVenta {
    /** El libro asociado a la línea de venta. */
    private Libro libro;
    /** La cantidad de ejemplares seleccionados. */
    private int cantidad;
    /**
     * Constructor por defecto para crear una instancia vacía de LineaVenta.
     */
    public LineaVenta() {
    }
    /**
     * Constructor con parámetros para inicializar una línea de venta con su libro y cantidad.
     * @param libro El objeto Libro a incluir en la línea de venta.
     * @param cantidad La cantidad de ejemplares seleccionados.
     */
    public LineaVenta(Libro libro, int cantidad) {
        this.libro = libro;
        this.cantidad = cantidad;
    }
    /**
     * Obtiene el libro asociado a esta línea de venta.
     * @return El objeto Libro asignado.
     */
    public Libro getLibro() {
        return libro;
    }
    /**
     * Establece el libro asociado a esta línea de venta.
     * @param libro El nuevo objeto Libro a asignar.
     */
    public void setLibro(Libro libro) {
        this.libro = libro;
    }
    /**
     * Obtiene la cantidad de ejemplares seleccionados.
     * @return La cantidad de libros.
     */
    public int getCantidad() {
        return cantidad;
    }
    /**
     * Establece la cantidad de ejemplares para esta línea de venta.
     * @param cantidad La nueva cantidad a asignar.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    /**
     * Obtiene el código ISBN del libro contenido en la línea de venta.
     * @return El ISBN del libro como una cadena de texto.
     */
    public String getIsbn() {
        return libro.getIsbn();
    }
    /**
     * Obtiene el título del libro contenido en la línea de venta.
     * @return El título del libro.
     */
    public String getTitulo() {
        return libro.getTitulo();
    }
    /**
     * Obtiene el precio unitario del libro contenido en la línea de venta.
     * @return El precio unitario del libro.
     */
    public double getPrecio() {
        return libro.getPrecio();
    }
    /**
     * Calcula el subtotal multiplicando el precio unitario del libro por la cantidad.
     * @return El subtotal resultante de la línea de venta.
     */
    public double getSubtotal() {
        return libro.getPrecio() * cantidad;
    }
}