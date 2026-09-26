
package org.ol.model;

/**
 * Representa la relación entre un autor y un libro. Funciona como tabla
 * intermedia que resuelve la relación muchos a muchos entre {@link Autor}
 * y {@link Libro}: un libro puede tener varios autores y un autor puede
 * haber escrito varios libros.
 *
 * @author Octavio Letona
 * @version 1.0.0
 * @see Autor
 * @see Libro
 */
public class AutorLibro {
    /** Identificador único del registro de la relación autor-libro. */
    private int idAutorLibro;
    /** Identificador del autor; referencia a {@link Autor#getIdAutor()}. */
    private int idAutor;
    /** ISBN del libro; referencia a {@link Libro#getIsbn()}. */
    private String isbn;
    /**
     * Constructor vacío. Crea una relación autor-libro sin datos
     * inicializados; los valores se pueden asignar mediante los setter.
     */
    public AutorLibro() {
    }
    /**
     * Constructor parametrizado. Crea una relación entre un autor y un libro.
     * @param idAutorLibro identificador único del registro de la relación
     * @param idAutor identificador del autor; debe existir en la tabla de
     * autores
     * @param isbn ISBN del libro; debe existir en la tabla de libros
     */
    public AutorLibro(int idAutorLibro, int idAutor, String isbn) {
        this.idAutorLibro = idAutorLibro;
        this.idAutor = idAutor;
        this.isbn = isbn;
    }
    /**
     * Obtiene el identificador único del registro de la relación.
     * @return el identificador de la relación autor-libro
     */
    public int getIdAutorLibro() {
        return idAutorLibro;
    }
    /**
     * Establece el identificador único del registro de la relación.
     * @param idAutorLibro nuevo identificador de la relación autor-libro
     */
    public void setIdAutorLibro(int idAutorLibro) {
        this.idAutorLibro = idAutorLibro;
    }
    /**
     * Obtiene el identificador del autor asociado.
     * @return el identificador del autor
     */
    public int getIdAutor() {
        return idAutor;
    }
    /**
     * Establece el identificador del autor asociado.
     * @param idAutor nuevo identificador del autor; debe existir en la tabla de autores
     */
    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }
    /**
     * Obtiene el ISBN del libro asociado.
     * @return el ISBN del libro
     */
    public String getIsbn() {
        return isbn;
    }
    /**
     * Establece el ISBN del libro asociado.
     * @param isbn nuevo ISBN del libro; debe existir en la tabla de libros
     */
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
