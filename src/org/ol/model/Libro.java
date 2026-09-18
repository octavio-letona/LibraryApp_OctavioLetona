
package org.ol.model;
 
/**
* Representa un libro del catÃ¡logo de la librerÃ­a. Contiene su ISBN, tÃ­tulo,
* fecha de publicaciÃ³n, precio, categorÃ­a, editorial y existencias en
* inventario. Es la clase principal del modelo de dominio.
*
* @author Octavio Letona
* @version 1.0.0
* @see Categoria
* @see AutorLibro
* @see DetalleVenta
*/
public class Libro {

    /**
     * ISBN del libro; identificador Ãºnico (llave primaria).
     */
    private String isbn;

    /**
     * TÃ­tulo del libro.
     */
    private String titulo;

    /**
     * Fecha de publicaciÃ³n del libro, almacenada como texto.
     */
    private String fechaPublicacion;

    /**
     * Precio de venta del libro.
     */
    private double precio;

    /**
     * Identificador de la categorÃ­a del libro; referencia a
     * {@link Categoria#getIdCategoria()}.
     */
    private int idCategoria;

    /**
     * NIT de la editorial que publica el libro.
     */
    private String nitEditorial;

    /**
     * Cantidad de ejemplares disponibles en inventario.
     */
    private int stock;

    /**
     * Constructor vacÃ­o. Crea un libro sin datos inicializados; los valores se
     * pueden asignar posteriormente mediante los mÃ©todos setter.
     */
    public Libro() {
    }

    /**
     * Constructor parametrizado. Crea un libro con todos sus datos.
     *
     * @param isbn ISBN Ãºnico del libro
     * @param titulo tÃ­tulo del libro
     * @param fechaPublicacion fecha de publicaciÃ³n, como texto
     * @param precio precio de venta; no puede ser negativo
     * @param idCategoria identificador de la categorÃ­a; debe existir en la
     * tabla de categorÃ­as
     * @param nitEditorial NIT de la editorial del libro
     * @param stock cantidad de ejemplares en inventario; no puede ser negativa
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
     * Obtiene el tÃ­tulo del libro.
     *
     * @return el tÃ­tulo del libro
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el tÃ­tulo del libro.
     *
     * @param titulo nuevo tÃ­tulo del libro
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene la fecha de publicaciÃ³n del libro.
     *
     * @return la fecha de publicaciÃ³n, como texto
     */
    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    /**
     * Establece la fecha de publicaciÃ³n del libro.
     *
     * @param fechaPublicacion nueva fecha de publicaciÃ³n, como texto
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
     * Obtiene el identificador de la categorÃ­a del libro.
     *
     * @return el identificador de la categorÃ­a
     */
    public int getIdCategoria() {
        return idCategoria;
    }

    /**
     * Establece el identificador de la categorÃ­a del libro.
     *
     * @param idCategoria nuevo identificador de categorÃ­a; debe existir en la
     * tabla de categorÃ­as
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
     * Devuelve la representaciÃ³n en texto del libro, que corresponde a su
     * tÃ­tulo. Es el texto que se muestra, por ejemplo, en los ComboBox de la
     * interfaz grÃ¡fica.
     *
     * @return el tÃ­tulo del libro
     */
    @Override
    public String toString() {
        return titulo;
    }
}
