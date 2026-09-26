package org.ol.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.ol.dao.FacturaDAO;
import org.ol.exception.DaoException;
import org.ol.model.LineaFactura;
import org.ol.util.Conexion;

/**
 * Implementación de la interfaz FacturaDAO para la consulta y obtención
 * de los detalles de facturación en la base de datos MySQL.
 * 
 * @author Octavio Letona
 * @version 1.0.0
 * @see org.ol.dao.FacturaDAO
 */
public class FacturaDAOImpl implements FacturaDAO {

    /**
     * Busca y obtiene las líneas de detalle pertenecientes a un número de venta específico.
     * 
     * @param noVenta El número identificador de la venta/factura a consultar.
     * @return Un ArrayList de objetos LineaFactura con el desglose de la factura.
     * @throws DaoException Si ocurre un error de SQL durante la búsqueda de la factura.
     */
    @Override
    public ArrayList<LineaFactura> buscarFactura(int noVenta) {
        ArrayList<LineaFactura> lista = new ArrayList<>();
        String sql = "{call sp_buscar_factura(?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setInt(1, noVenta);
            try (ResultSet rs = consulta.executeQuery()) {
                while (rs.next()) {
                    LineaFactura linea = new LineaFactura();
                    linea.setNumeroFactura(rs.getInt("numero_factura"));
                    linea.setFechaEmision(rs.getString("fecha_emision"));
                    linea.setCuiCliente(rs.getLong("cui_cliente"));
                    linea.setNombreCliente(rs.getString("nombre_cliente"));
                    linea.setCorreoCliente(rs.getString("correo_cliente"));
                    linea.setIsbnLibro(rs.getString("isbn_libro"));
                    linea.setTituloLibro(rs.getString("titulo_libro"));
                    linea.setCantidad(rs.getInt("cantidad"));
                    linea.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    linea.setSubtotal(rs.getDouble("subtotal"));
                    linea.setUsuarioAtendio(rs.getString("usuario_atendio"));
                    linea.setGranTotal(rs.getDouble("gran_total"));
                    lista.add(linea);
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error al buscar la factura: " + e.getMessage(), e);
        }
        return lista;
    }
}