/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.ol.dao;

import java.util.ArrayList;
import org.ol.model.LineaFactura;

/**
 * Contrato DAO que define las operaciones de persistencia y consulta 
 * para la generación de facturas y sus detalles dentro del sistema.
 * 
 * @author Octavio Letona
 * @version 1.0.0
 * @see org.ol.model.LineaFactura
 */
public interface FacturaDAO {

    /**
     * Busca y recupera las líneas de detalle que componen la factura 
     * correspondiente a un número de venta específico.
     * 
     * @param noVenta Número o identificador único de la venta a consultar.
     * @return Un {@link ArrayList} que contiene los objetos {@link LineaFactura} asociados.
     *         Retorna una lista vacía si no se encuentran registros para el número de venta especificado.
     * @throws RuntimeException si ocurre un error de conexión con JDBC o durante la consulta a la base de datos.
     */
    ArrayList<LineaFactura> buscarFactura(int noVenta);
}