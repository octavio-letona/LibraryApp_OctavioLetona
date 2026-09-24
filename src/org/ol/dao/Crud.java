/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.ol.dao;

import java.util.ArrayList;

/**
 * Contrato genérico que define las operaciones estándar de persistencia (CRUD)
 * para las entidades del sistema.
 * 
 * @param <T> El tipo de la entidad o modelo de datos.
 * @param <K> El tipo de dato de la clave primaria (identificador único).
 * 
 * @author Octavio Letona
 * @version 1.0.0
 */
public interface Crud<T, K> {

    /**
     * Registra una nueva entidad en la base de datos.
     * 
     * @param entidad Objeto de tipo {@code T} que contiene los datos a registrar. No debe ser null.
     * @return {@code true} si la inserción fue exitosa; {@code false} en caso contrario.
     * @throws IllegalArgumentException si la entidad es null o contiene datos inválidos.
     * @throws RuntimeException si ocurre un error de conexión con la base de datos.
     */
    boolean crear(T entidad);

    /**
     * Actualiza la información de una entidad existente en la base de datos.
     * 
     * @param entidad Objeto de tipo {@code T} con los datos modificados. No debe ser null.
     * @return {@code true} si la actualización fue exitosa; {@code false} en caso contrario.
     * @throws IllegalArgumentException si la entidad es null o sus datos son inválidos.
     * @throws RuntimeException si ocurre un error al procesar la actualización en la base de datos.
     */
    boolean actualizar(T entidad);

    /**
     * Elimina el registro de una entidad de la base de datos mediante su identificador único.
     * 
     * @param id Clave primaria de tipo {@code K} correspondiente a la entidad a eliminar. No debe ser null.
     * @return {@code true} si la eliminación fue exitosa; {@code false} en caso contrario.
     * @throws IllegalArgumentException si el identificador es null.
     * @throws RuntimeException si ocurre un error durante el proceso de eliminación.
     */
    boolean eliminar(K id);

    /**
     * Busca y recupera una entidad específica mediante su clave primaria.
     * 
     * @param id Clave primaria de tipo {@code K} de la entidad a consultar.
     * @return El objeto de tipo {@code T} correspondiente si se encuentra; {@code null} en caso contrario.
     * @throws RuntimeException si ocurre un error durante la consulta a la base de datos.
     */
    T buscarPorId(K id);

    /**
     * Recupera el listado completo de registros para la entidad gestionada.
     * 
     * @return Un {@link ArrayList} que contiene los objetos de tipo {@code T}.
     *         Retorna una lista vacía si no existen registros.
     * @throws RuntimeException si ocurre un error al consultar la base de datos.
     */
    ArrayList<T> listarTodos();
}