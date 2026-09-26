package org.ol.dao;

import org.ol.model.AutorLibro;

/**
 * Contrato DAO que define las operaciones de persistencia CRUD
 * para la entidad {@link AutorLibro} dentro del sistema.
 * 
 * Extiende de {@link Crud} empleando un identificador de tipo {@link Integer}
 * (ID único de la relación autor-libro) como clave primaria.
 * 
 * @author Octavio Letona
 * @version 1.0.0
 * @see org.ol.model.AutorLibro
 * @see org.ol.dao.Crud
 */
public interface AutorLibroDAO extends Crud<AutorLibro, Integer> {

}