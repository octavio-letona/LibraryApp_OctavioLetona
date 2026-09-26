/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ol.manager;

import org.ol.model.Usuario;

/**
 * Gestiona la sesión del usuario dentro de la aplicación.
 * Esta clase mantiene una única instancia de la sesión y permite
 * almacenar, consultar y eliminar el usuario actualmente autenticado.
 *
 * @author Octavio Letona
 * @version 1.0
 */
public class SesionContext {

    /**
     * Instancia única de la clase SesionContext.
     */
    private static SesionContext instancia;

    /**
     * Usuario que actualmente tiene una sesión iniciada.
     */
    private Usuario usuarioActual;

    /**
     * Constructor privado que evita la creación directa de instancias
     * de SesionContext.
     */
    private SesionContext() {
    }

    /**
     * Obtiene la instancia única de SesionContext.
     * Si la instancia aún no existe, se crea antes de ser retornada.
     *
     * @return instancia única de SesionContext
     */
    public static synchronized SesionContext getInstancia() {
        if (instancia == null) {
            instancia = new SesionContext();
        }
        return instancia;
    }

    /**
     * Obtiene el usuario que actualmente tiene una sesión iniciada.
     *
     * @return usuario actualmente autenticado
     */
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    /**
     * Establece el usuario que tendrá la sesión activa.
     * @param usuario usuario que será almacenado como usuario actual
     */
    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    /**
     * Cierra la sesión actual eliminando la referencia al usuario
     * autenticado.
     */
    public void cerrarSesion() {
        this.usuarioActual = null;
    }
}