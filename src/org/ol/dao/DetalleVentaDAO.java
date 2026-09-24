package org.ol.dao;

import org.ol.model.DetalleVenta;

/**
 * Contrato DAO que define las operaciones de persistencia CRUD
 * para la entidad {@link DetalleVenta} dentro del sistema.
 * 
 * Extiende de {@link Crud} empleando un identificador de tipo {@link Integer}
 * (ID único del detalle de venta) como clave primaria.
 * 
 * @author Octavio Letona
 * @version 1.0.0
 * @see org.ol.model.DetalleVenta
 * @see org.ol.dao.Crud
 */
public interface DetalleVentaDAO extends Crud<DetalleVenta, Integer> {

}