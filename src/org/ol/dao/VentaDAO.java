package org.ol.dao;

import java.util.List;
import org.ol.model.LineaVenta;
import org.ol.model.Venta;

/**
 * Contrato DAO que define las operaciones de persistencia y transacciones
 * para la entidad {@link Venta} dentro del sistema.
 * 
 * @author Octavio Letona
 * @version 1.0.0
 * @see org.ol.model.Venta
 * @see org.ol.model.LineaVenta
 * @see org.ol.dao.Crud
 */
public interface VentaDAO extends Crud<Venta, Integer> {

    /**
     * Registra una transacción de venta completa en la base de datos, insertando
     * el encabezado de la venta, procesando sus líneas de detalle y descontando el stock.
     * 
     * @param venta Objeto {@link Venta} que contiene los datos del encabezado. No debe ser null.
     * @param lineas Lista de objetos {@link LineaVenta} con el detalle de los productos a vender.
     * @return El número de venta {@code no_venta} generado automáticamente, o {@code -1} si la transacción falla.
     * @throws IllegalArgumentException si la venta es null o la lista de líneas está vacía.
     * @throws RuntimeException si ocurre un error de conexión con JDBC o durante el procesamiento de la transacción.
     */
    int crearVenta(Venta venta, List<LineaVenta> lineas);
}