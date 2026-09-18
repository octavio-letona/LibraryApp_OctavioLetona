/*

* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license

* Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template

 */
package org.ol.model;

/**
 * Representa a un cliente de la librería. Guarda su CUI (Código Único de
 * Identificación), su nombre completo y su correo electrónico de contacto. /**
 * Representa a un cliente de la librería. Guarda su CUI (Código Único de
 * Identificación), su nombre completo y su correo electrónico de contacto.
 *
 * @author Octavio Letona
 * @version 1.0.0
 */
public class Cliente {

    /**
     * Código Único de Identificación (CUI) del cliente; funciona como llave
     * primaria.
     */
    private long cui;
    /**
     * Nombre(s) de pila del cliente.
     */
    private String nombreCliente;
    /**
     * Apellido(s) del cliente.
     */
    private String apellidoCliente;
    /**
     * Dirección de correo electrónico de contacto del cliente.
     */
    private String correoElectronico;

    /**
     * Constructor vacío. Crea un cliente sin datos inicializados; los valores
     * se pueden asignar posteriormente mediante los métodos setter.
     */
    public Cliente() {

    }

    /**
     * Constructor parametrizado. Crea un cliente con todos sus datos.
     *
     * @param cui Código Único de Identificación del cliente; debe ser un valor
     * numérico positivo
     * @param nombreCliente nombre(s) de pila del cliente
     * @param apellidoCliente apellido(s) del cliente
     * @param correoElectronico correo electrónico de contacto; se espera un
     * formato válido (usuario@dominio)
     */
    public Cliente(long cui, String nombreCliente, String apellidoCliente, String correoElectronico) {
        this.cui = cui;
        this.nombreCliente = nombreCliente;
        this.apellidoCliente = apellidoCliente;
        this.correoElectronico = correoElectronico;

    }

    /**
     * Obtiene el CUI del cliente.
     * @return el Código Único de Identificación del cliente
     */
    public long getCui() {
        return cui;
    }
    /**
     * Establece el CUI del cliente.
     * @param cui nuevo Código Único de Identificación; debe ser un valor
     * numérico positivo
     */
    public void setCui(long cui) {
        this.cui = cui;

    }

    /**
     * Obtiene el nombre de pila del cliente.
     * @return el nombre del cliente
     */
    public String getNombreCliente() {
        return nombreCliente;

    }

    /**
     * Establece el nombre de pila del cliente.
     * @param nombreCliente nuevo nombre del cliente
     */
    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }
  
    /**
     * Obtiene el apellido del cliente.
     *
     * @return el apellido del cliente
     */
    public String getApellidoCliente() {
        return apellidoCliente;
    }

    /**
     * Establece el apellido del cliente.
     *
     * @param apellidoCliente nuevo apellido del cliente
     */
    public void setApellidoCliente(String apellidoCliente) {
        this.apellidoCliente = apellidoCliente;
    }

    /**
     * Obtiene el correo electrónico del cliente.
     *
     * @return el correo electrónico de contacto del cliente
     */
    public String getCorreoElectronico() {
        return correoElectronico;
    }

    /**
     * Establece el correo electrónico del cliente.
     *
     * @param correoElectronico nuevo correo electrónico; se espera un formato
     * válido (usuario@dominio)
     */
    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    /**
     * Devuelve la representación en texto del cliente, formada por su nombre
     * seguido de su apellido. Es el texto que se muestra, por ejemplo, en los
     * ComboBox de la interfaz gráfica.
     *
     * @return el nombre completo del cliente
     */
    @Override
    public String toString() {
        return nombreCliente + " " + apellidoCliente;
    }
}
