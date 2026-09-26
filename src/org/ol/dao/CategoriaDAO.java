package org.ol.dao;

import org.ol.model.Categoria;

/**
 * Contrato DAO que define las operaciones de persistencia CRUD
 * para la entidad {@link Categoria} dentro del sistema.
 * 
 * Extiende de {@link Crud} empleando un identificador de tipo {@link Integer}
 * (ID único de la categoría) como clave primaria.
 * 
 * @author Octavio Letona
 * @version 1.0.0
 * @see org.ol.model.Categoria
 * @see org.ol.dao.Crud
 */
public interface CategoriaDAO extends Crud<Categoria, Integer> {

}