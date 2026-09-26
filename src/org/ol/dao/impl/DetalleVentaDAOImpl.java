package org.ol.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.ol.dao.DetalleVentaDAO;
import org.ol.exception.DaoException;
import org.ol.model.DetalleVenta;
import org.ol.util.Conexion;

/**
 * Implementación de la interfaz DetalleVentaDAO para gestionar las operaciones 
 * de persistencia (CRUD) de la entidad DetalleVenta en la base de datos MySQL.
 * 
 * @author Octavio Letona
 * @version 1.0.0
 * @see org.ol.dao.DetalleVentaDAO
 */
public class DetalleVentaDAOImpl implements DetalleVentaDAO {

    /**
     * Obtiene la lista completa de detalles de ventas registrados en el sistema.
     * 
     * @return Un ArrayList de objetos DetalleVenta.
     * @throws DaoException Si ocurre un error de SQL al consultar los datos.
     */
    @Override
    public ArrayList<DetalleVenta> listarTodos() {
        ArrayList<DetalleVenta> lista = new ArrayList<>();
        String sql = "{call sp_listar_detalle_venta()}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql);
                ResultSet rs = consulta.executeQuery()) {
            while (rs.next()) {
                DetalleVenta dv = new DetalleVenta();
                dv.setIdDetalleVenta(rs.getInt("id_detalle_venta"));
                dv.setNoVenta(rs.getInt("no_venta"));
                dv.setIsbn(rs.getString("isbn"));
                dv.setCantidad(rs.getInt("cantidad"));
                dv.setPrecio(rs.getDouble("precio"));
                lista.add(dv);
            }
        } catch (SQLException e) {
            throw new DaoException("Error al listar detalle_venta: " + e.getMessage(), e);
        }
        return lista;
    }

    /**
     * Busca y obtiene el detalle de venta según su ID único.
     * 
     * @param idDetalleVenta El identificador único del detalle de venta.
     * @return El objeto DetalleVenta encontrado o null si no existe.
     * @throws DaoException Si ocurre un error de SQL durante la búsqueda.
     */
    @Override
    public DetalleVenta buscarPorId(Integer idDetalleVenta) {
        DetalleVenta dv = null;
        String sql = "{call sp_buscar_detalle_venta(?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setInt(1, idDetalleVenta);
            try (ResultSet rs = consulta.executeQuery()) {
                if (rs.next()) {
                    dv = new DetalleVenta();
                    dv.setIdDetalleVenta(rs.getInt("id_detalle_venta"));
                    dv.setNoVenta(rs.getInt("no_venta"));
                    dv.setIsbn(rs.getString("isbn"));
                    dv.setCantidad(rs.getInt("cantidad"));
                    dv.setPrecio(rs.getDouble("precio"));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error al buscar detalle_venta: " + e.getMessage(), e);
        }
        return dv;
    }

    /**
     * Registra un nuevo detalle de venta en la base de datos.
     * 
     * @param detalleVenta El objeto DetalleVenta con los datos a insertar.
     * @return true si la inserción fue exitosa; false en caso contrario.
     * @throws DaoException Si ocurre un error de SQL al ejecutar el procedimiento almacenado.
     */
    @Override
    public boolean crear(DetalleVenta detalleVenta) {
        String sql = "{call sp_insertar_detalle_venta(?,?,?,?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setInt(1, detalleVenta.getNoVenta());
            consulta.setString(2, detalleVenta.getIsbn());
            consulta.setInt(3, detalleVenta.getCantidad());
            consulta.setDouble(4, detalleVenta.getPrecio());
            return consulta.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Error al insertar detalle_venta: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza la información de un detalle de venta existente.
     * 
     * @param detalleVenta El objeto DetalleVenta con los datos actualizados.
     * @return true si se actualizó al menos un registro; false en caso contrario.
     * @throws DaoException Si ocurre un error de SQL al realizar la actualización.
     */
    @Override
    public boolean actualizar(DetalleVenta detalleVenta) {
        String sql = "{call sp_actualizar_detalle_venta(?,?,?,?,?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setInt(1, detalleVenta.getIdDetalleVenta());
            consulta.setInt(2, detalleVenta.getNoVenta());
            consulta.setString(3, detalleVenta.getIsbn());
            consulta.setInt(4, detalleVenta.getCantidad());
            consulta.setDouble(5, detalleVenta.getPrecio());
            return consulta.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Error al actualizar detalle_venta: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un detalle de venta del sistema mediante su ID.
     * 
     * @param idDetalleVenta El identificador del detalle de venta a eliminar.
     * @return true si la eliminación fue exitosa; false en caso contrario.
     * @throws DaoException Si ocurre un error de SQL al ejecutar el procedimiento almacenado.
     */
    @Override
    public boolean eliminar(Integer idDetalleVenta) {
        String sql = "{call sp_eliminar_detalle_venta(?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setInt(1, idDetalleVenta);
            return consulta.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Error al eliminar detalle_venta: " + e.getMessage(), e);
        }
    }
}