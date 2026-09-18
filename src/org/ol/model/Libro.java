/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ol.model;

/**
 * Representa un libro del catálogo de la librería. Contiene su ISBN, título,
 * fecha de publicación, precio, categoría, editorial y existencias en
 * inventario. Es la clase principal del modelo de dominio.
 *
 * @author Octavio Letona
 * @version 1.0.0
 * @see Categoria
 * @see AutorLibro
 * @see DetalleVenta
 */
public class Libro {

    /** ISBN del libro; identificador único (llave primaria). */
    private String isbn;

    /** Título del libro. */
    private String titulo;

    /** Fecha de publicación del libro, almacenada como texto. */
    private String fechaPublicacion;

    /** Precio de venta del libro. */
    private double precio;

    /** Identificador de la categoría del libro; referencia a {@link Categoria#getIdCategoria()}. */
    private int idCategoria;

    /** NIT de la editorial que publica el libro. */
    private String nitEditorial;

    /** Cantidad de ejemplares disponibles en inventario. */
    private int stock;

    /**
     * Constructor vacío. Crea un libro sin datos inicializados; los valores
     * se pueden asignar posteriormente mediante los métodos setter.
     */
    public Libro() {
    }

    /**
     * Constructor parametrizado. Crea un libro con todos sus datos.
     *
     * @param isbn             ISBN único del libro
     * @param titulo           título del libro
     * @param fechaPublicacion fecha de publicación, como texto
     * @param precio           precio de venta; no puede ser negativo
     * @param idCategoria      identificador de la categoría; debe existir en la tabla de categorías
     * @param nitEditorial     NIT de la editorial del libro
     * @param stock            cantidad de ejemplares en inventario; no puede ser negativa
     */
    public Libro(String isbn, String titulo, String fechaPublicacion, double precio, int idCategoria, String nitEditorial, int stock) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.fechaPublicacion = fechaPublicacion;
        this.precio = precio;
        this.idCategoria = idCategoria;
        this.nitEditorial = nitEditorial;
        this.stock = stock;
    }

    /**
     * Obtiene el ISBN del libro.
     *
     * @return el ISBN del libro
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Establece el ISBN del libro.
     *
     * @param isbn nuevo ISBN del libro
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    /**
     * Obtiene el título del libro.
     *
     * @return el título del libro
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título del libro.
     *
     * @param titulo nuevo título del libro
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene la fecha de publicación del libro.
     *
     * @return la fecha de publicación, como texto
     */
    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    /**
     * Establece la fecha de publicación del libro.
     *
     * @param fechaPublicacion nueva fecha de publicación, como texto
     */
    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    /**
     * Obtiene el precio de venta del libro.
     *
     * @return el precio del libro
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Establece el precio de venta del libro.
     *
     * @param precio nuevo precio; no puede ser negativo
     */
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    /**
     * Obtiene el identificador de la categoría del libro.
     *
     * @return el identificador de la categoría
     */
    public int getIdCategoria() {
        return idCategoria;
    }

    /**
     * Establece el identificador de la categoría del libro.
     *
     * @param idCategoria nuevo identificador de categoría; debe existir en la tabla de categorías
     */
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * Obtiene el NIT de la editorial del libro.
     *
     * @return el NIT de la editorial
     */
    public String getNitEditorial() {
        return nitEditorial;
    }

    /**
     * Establece el NIT de la editorial del libro.
     *
     * @param nitEditorial nuevo NIT de la editorial
     */
    public void setNitEditorial(String nitEditorial) {
        this.nitEditorial = nitEditorial;
    }

    /**
     * Obtiene la cantidad de ejemplares disponibles en inventario.
     *
     * @return el stock del libro
     */
    public int getStock() {
        return stock;
    }

    /**
     * Establece la cantidad de ejemplares disponibles en inventario.
     *
     * @param stock nuevo stock; no puede ser negativo
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Devuelve la representación en texto del libro, que corresponde a su
     * título. Es el texto que se muestra, por ejemplo, en los ComboBox de la
     * interfaz gráfica.
     *
     * @return el título del libro
     */
    @Override
    public String toString() {
        return titulo;
    }
}
