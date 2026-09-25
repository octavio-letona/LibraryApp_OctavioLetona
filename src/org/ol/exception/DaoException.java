/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ol.exception;

/**
 * Excepción utilizada para representar errores ocurridos en la capa
 * de acceso a datos de la aplicación.
 *
 * Esta excepción permite propagar errores relacionados con operaciones
 * de base de datos hacia las capas superiores de la aplicación para
 * que puedan ser gestionados adecuadamente.
 *
 * @author Octavio Letona
 * @version 1.0
 */
public class DaoException extends RuntimeException {

    /**
     * Construye una nueva excepción de acceso a datos con un mensaje
     * descriptivo del error ocurrido.
     *
     * @param mensaje descripción del error que provocó la excepción
     */
    public DaoException(String mensaje) {
        super(mensaje);
    }

    /**
     * Construye una nueva excepción de acceso a datos con un mensaje
     * descriptivo y la causa original del error.
     *
     * @param mensaje descripción del error que provocó la excepción
     * @param causa excepción original que causó el error
     */
    public DaoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}