package org.ol.model;

/**
 * Representa a un autor de libros dentro del sistema de la librería.
 * Almacena sus datos de identificación, su nacionalidad y una breve
 * biografía. Se relaciona con los libros a través de {@link AutorLibro}.
 *
 * @author Octavio Letona
 * @version 1.0.0
 * @see AutorLibro
 * @see Libro
 */
public class Autor {

    /** Identificador único del autor en la base de datos. */
    private int idAutor;

    /** Nombre(s) de pila del autor. */
    private String nombreAutor;

    /** Apellido(s) del autor. */
    private String apellidoAutor;

    /** País o nacionalidad de origen del autor. */
    private String nacionalidad;

    /** Reseña biográfica del autor. */
    private String biografia;

    /**
     * Constructor vacío. Crea un autor sin datos inicializados; los valores
     * se pueden asignar posteriormente mediante los métodos setter.
     */
    public Autor() {
    }

    /**
     * Constructor parametrizado. Crea un autor con todos sus datos.
     *
     * @param idAutor         identificador único del autor; debe ser un entero positivo
     * @param nombreAutor     nombre(s) de pila del autor
     * @param apellidoAutor   apellido(s) del autor
     * @param nacionalidad    país o nacionalidad de origen del autor
     * @param biografia       reseña biográfica del autor
     */
    public Autor(int idAutor, String nombreAutor, String apellidoAutor, String nacionalidad, String biografia) {
        this.idAutor = idAutor;
        this.nombreAutor = nombreAutor;
        this.apellidoAutor = apellidoAutor;
        this.nacionalidad = nacionalidad;
        this.biografia = biografia;
    }

    /**
     * Obtiene el identificador único del autor.
     *
     * @return el identificador del autor
     */
    public int getIdAutor() {
        return idAutor;
    }

    /**
     * Establece el identificador único del autor.
     *
     * @param idAutor nuevo identificador del autor; debe ser un entero positivo
     */
    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    /**
     * Obtiene el nombre de pila del autor.
     *
     * @return el nombre del autor
     */
    public String getNombreAutor() {
        return nombreAutor;
    }

    /**
     * Establece el nombre de pila del autor.
     *
     * @param nombreAutor nuevo nombre del autor
     */
    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    /**
     * Obtiene el apellido del autor.
     *
     * @return el apellido del autor
     */
    public String getApellidoAutor() {
        return apellidoAutor;
    }

    /**
     * Establece el apellido del autor.
     *
     * @param apellidoAutor nuevo apellido del autor
     */
    public void setApellidoAutor(String apellidoAutor) {
        this.apellidoAutor = apellidoAutor;
    }

    /**
     * Obtiene la nacionalidad del autor.
     *
     * @return la nacionalidad del autor
     */
    public String getNacionalidad() {
        return nacionalidad;
    }

    /**
     * Establece la nacionalidad del autor.
     *
     * @param nacionalidad nueva nacionalidad del autor
     */
    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    /**
     * Obtiene la biografía del autor.
     *
     * @return la reseña biográfica del autor
     */
    public String getBiografia() {
        return biografia;
    }

    /**
     * Establece la biografía del autor.
     *
     * @param biografia nueva reseña biográfica del autor
     */
    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    /**
     * Devuelve la representación en texto del autor, formada por su nombre
     * seguido de su apellido. Es el texto que se muestra, por ejemplo, en
     * los ComboBox de la interfaz gráfica.
     *
     * @return el nombre completo del autor
     */
    @Override
    public String toString() {
        return nombreAutor + " " + apellidoAutor;
    }
}