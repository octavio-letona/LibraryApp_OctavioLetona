/*
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
* Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
*/
package org.ol.model;

/**
 * Representa una categoría o género en el que se clasifican los libros de
 * la librería (por ejemplo: novela, ciencia, historia).
 * @author Octavio Letona
 * @version 1.0.0
 * @see Libro
 */
public class Categoria {
    /** Identificador único de la categoría en la base de datos. */
    private int idCategoria;
    /** Nombre descriptivo de la categoría. */
    private String nombreCategoria;
    /**
     * Constructor vacío. Crea una categoría sin datos inicializados; los
     * valores se pueden asignar posteriormente mediante los métodos setter.
     */
    public Categoria() {
    }
    /**
     * Constructor parametrizado. Crea una categoría con todos sus datos.
     * @param idCategoria     identificador único de la categoría; debe ser un entero positivo
     * @param nombreCategoria nombre descriptivo de la categoría
     */
    public Categoria(int idCategoria, String nombreCategoria) {
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
    }
    /**
     * Obtiene el identificador único de la categoría.
     * @return el identificador de la categoría
     */
    public int getIdCategoria() {
        return idCategoria;
    }
    /**
     * Establece el identificador único de la categoría.
     * @param idCategoria nuevo identificador de la categoría; debe ser un entero positivo
     */
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }
    /**
     * Obtiene el nombre de la categoría.
     * @return el nombre de la categoría
     */
    public String getNombreCategoria() {
        return nombreCategoria;
    }
    /**
     * Establece el nombre de la categoría.
     * @param nombreCategoria nuevo nombre de la categoría
     */
    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
    /**
     * Devuelve la representación en texto de la categoría, que corresponde
     * a su nombre. Es el texto que se muestra, por ejemplo, en los ComboBox
     * de la interfaz gráfica.
     * @return el nombre de la categoría
     */
    @Override
    public String toString() {
        return nombreCategoria;
    }
}
