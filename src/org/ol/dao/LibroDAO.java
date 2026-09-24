/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.ol.dao;

import org.ol.model.Libro;

/**
 * Contrato DAO que define las operaciones de persistencia CRUD
 * para la entidad {@link Libro} dentro del sistema.
 * 
 * Extiende de {@link Crud} empleando el ISBN ({@link String}) como 
 * identificador único para la gestión de libros.
 * 
 * @author Octavio Letona
 * @version 1.0.0
 * @see org.ol.model.Libro
 * @see org.ol.dao.Crud
 */
public interface LibroDAO extends Crud<Libro, String> {

}