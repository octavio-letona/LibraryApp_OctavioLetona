
package org.ol.model;
/**
 * Representa una entidad editorial dentro del sistema.
 * Permite gestionar los datos de identificación y contacto de las editoriales.
 * @author Octavio Letona
 * @version 1.0.0
 * @see Venta
 * @see Libro
 */
public class Editorial {
    /** Número de Identificación Tributaria (NIT) único de la editorial. */
    private String nit;
    /** Nombre comercial o razón social de la editorial. */
    private String nombreEditorial;
    /** Número telefónico de contacto de la editorial. */
    private String telefonoEditorial;
    /** Dirección física de las oficinas o instalaciones de la editorial. */
    private String direccionEditoria;
    /**
     * Constructor vacío por defecto para crear una instancia sin inicializar atributos.
     */
    public Editorial() {
    }
    /**
     * Constructor con parámetros para inicializar todos los atributos de la editorial.
     * @param nit Número de Identificación Tributaria.
     * @param nombreEditorial Nombre comercial de la editorial.
     * @param telefonoEditorial Teléfono de contacto.
     * @param direccionEditoria Dirección física de la editorial.
     */
    public Editorial(String nit, String nombreEditorial, String telefonoEditorial, String direccionEditoria) {
        this.nit = nit;
        this.nombreEditorial = nombreEditorial;
        this.telefonoEditorial = telefonoEditorial;
        this.direccionEditoria = direccionEditoria;
    }
    /**
     * Obtiene el NIT de la editorial.
     * @return Una cadena de texto con el NIT.
     */
    public String getNit() {
        return nit;
    }
    /**
     * Establece o modifica el NIT de la editorial.
     * @param nit El nuevo NIT a asignar.
     */
    public void setNit(String nit) {
        this.nit = nit;
    }
    /**
     * Obtiene el nombre de la editorial.
     * @return El nombre comercial de la editorial.
     */
    public String getNombreEditorial() {
        return nombreEditorial;
    }
    /**
     * Establece o modifica el nombre de la editorial.
     * @param nombreEditorial El nuevo nombre a asignar.
     */
    public void setNombreEditorial(String nombreEditorial) {
        this.nombreEditorial = nombreEditorial;
    }
    /**
     * Obtiene el teléfono de la editorial.
     * @return El número telefónico de contacto.
     */
    public String getTelefonoEditorial() {
        return telefonoEditorial;
    }
    /**
     * Establece o modifica el teléfono de la editorial.
     * @param telefonoEditorial El nuevo teléfono a asignar.
     */
    public void setTelefonoEditorial(String telefonoEditorial) {
        this.telefonoEditorial = telefonoEditorial;
    }
    /**
     * Obtiene la dirección física de la editorial.
     * @return La dirección de la editorial.
     */
    public String getDireccionEditoria() {
        return direccionEditoria;
    }
    /**
     * Establece o modifica la dirección física de la editorial.
     * @param direccionEditoria La nueva dirección a asignar.
     */
    public void setDireccionEditoria(String direccionEditoria) {
        this.direccionEditoria = direccionEditoria;
    }
    /**
     * Devuelve una representación en texto de la editorial.
     * @return El nombre de la editorial.
     */
    @Override
    public String toString() {
        return nombreEditorial;
    }
}