package org.ol.dao;

import org.ol.model.Autor;

/**
 * Contrato DAO que define las operaciones de persistencia CRUD
 * para la entidad {@link Autor} dentro del sistema.
 * 
 * Extiende de {@link Crud} empleando un identificador de tipo {@link Integer}
 * (ID único del autor) como clave primaria.
 * 
 * @author Octavio Letona
 * @version 1.0.0
 * @see org.ol.model.Autor
 * @see org.ol.dao.Crud
 */
public interface AutorDAO extends Crud<Autor, Integer> {

}