package org.ol.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.ol.dao.DetalleVentaDAO;
import org.ol.dao.VentaDAO;
import org.ol.exception.DaoException;
import org.ol.model.DetalleVenta;
import org.ol.model.LineaVenta;
import org.ol.model.Venta;
import org.ol.util.Conexion;

/**
 * Implementación de la interfaz VentaDAO para la gestión e integración de
 * operaciones de persistencia de ventas y control de stock en la base de datos.
 * 
 * @author Octavio Letona
 * @version 1.0.0
 * @see org.ol.dao.VentaDAO
 */
public class VentaDAOImpl implements VentaDAO {

    /** Instancia del DAO para gestionar la persistencia de los detalles de la venta. */
    private final DetalleVentaDAO detalleVentaDAO = new DetalleVentaDAOImpl();

    /**
     * Recupera el listado completo de las ventas registradas en el sistema.
     * 
     * @return Una lista de tipo ArrayList que contiene objetos Venta.
     * @throws DaoException Si ocurre un fallo en la base de datos al ejecutar el procedimiento almacenado.
     */
    @Override
    public ArrayList<Venta> listarTodos() {
        ArrayList<Venta> lista = new ArrayList<>();
        String sql = "{call sp_listar_ventas()}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql);
                ResultSet rs = consulta.executeQuery()) {
            while (rs.next()) {
                Venta v = new Venta();
                v.setNoVenta(rs.getInt("no_venta"));
                v.setFechaVenta(rs.getString("fecha_venta"));
                v.setTotalVenta(rs.getDouble("total_venta"));
                v.setCuiCliente(rs.getLong("cui_cliente"));
                v.setIdUsuario(rs.getInt("id_usuario"));
                lista.add(v);
            }
        } catch (SQLException e) {
            throw new DaoException("Error al listar ventas: " + e.getMessage(), e);
        }
        return lista;
    }

    /**
     * Busca los datos de un encabezado de venta mediante su número identificador.
     * 
     * @param noVenta El número correlativo de la venta a consultar.
     * @return El objeto Venta correspondiente, o null si no se encuentra el registro.
     * @throws DaoException Si ocurre un error durante la consulta SQL.
     */
    @Override
    public Venta buscarPorId(Integer noVenta) {
        Venta v = null;
        String sql = "{call sp_buscar_venta(?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setInt(1, noVenta);
            try (ResultSet rs = consulta.executeQuery()) {
                if (rs.next()) {
                    v = new Venta();
                    v.setNoVenta(rs.getInt("no_venta"));
                    v.setFechaVenta(rs.getString("fecha_venta"));
                    v.setTotalVenta(rs.getDouble("total_venta"));
                    v.setCuiCliente(rs.getLong("cui_cliente"));
                    v.setIdUsuario(rs.getInt("id_usuario"));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error al buscar venta: " + e.getMessage(), e);
        }
        return v;
    }

    /**
     * Registra un nuevo encabezado de venta individual en la base de datos.
     * 
     * @param venta El objeto Venta con la información a insertar.
     * @return true si la inserción fue exitosa; false en caso contrario.
     * @throws DaoException Si ocurre un error al ejecutar la inserción en la base de datos.
     */
    @Override
    public boolean crear(Venta venta) {
        String sql = "{call sp_insertar_venta(?,?,?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setDouble(1, venta.getTotalVenta());
            consulta.setString(2, String.valueOf(venta.getCuiCliente()));
            consulta.setInt(3, venta.getIdUsuario());
            return consulta.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Error al insertar venta: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza la información de un registro de venta previamente existente.
     * 
     * @param venta El objeto Venta con los campos modificados.
     * @return true si el registro fue actualizado correctamente; false si no se modificó ninguna fila.
     * @throws DaoException Si ocurre un error durante el formateo de fecha o la actualización SQL.
     */
    @Override
    public boolean actualizar(Venta venta) {
        String sql = "{call sp_actualizar_venta(?,?,?,?,?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setInt(1, venta.getNoVenta());
            if (venta.getFechaVenta() == null || venta.getFechaVenta().isEmpty()) {
                consulta.setNull(2, java.sql.Types.DATE);
            } else {
                consulta.setDate(2, java.sql.Date.valueOf(venta.getFechaVenta().substring(0, 10)));
            }
            consulta.setDouble(3, venta.getTotalVenta());
            consulta.setLong(4, venta.getCuiCliente());
            consulta.setInt(5, venta.getIdUsuario());
            return consulta.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Error al actualizar venta: " + e.getMessage(), e);
        }
    }

    /**
     * Procesa una venta completa insertando el encabezado, obteniendo el ID generado,
     * guardando cada línea del detalle y descontando las existencias de inventario.
     * 
     * @param venta Objeto Venta que contiene el encabezado de la transacción.
     * @param lineas Lista de objetos LineaVenta con los productos y cantidades a procesar.
     * @return El número identificador (noVenta) asignado por la base de datos, o -1 si falla.
     * @throws DaoException Si ocurre un error en la inserción del encabezado, del detalle o en el ajuste del stock.
     */
    @Override
    public int crearVenta(Venta venta, List<LineaVenta> lineas) {
        int noVenta = -1;
        String sql = "{call sp_insertar_venta(?,?,?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setDouble(1, venta.getTotalVenta());
            consulta.setString(2, String.valueOf(venta.getCuiCliente()));
            consulta.setInt(3, venta.getIdUsuario());
            int filasAfectadas = consulta.executeUpdate();
            if (filasAfectadas > 0) {
                try (Statement sentencia = conexion.createStatement();
                        ResultSet rs = sentencia.executeQuery("SELECT LAST_INSERT_ID()")) {
                    if (rs.next()) {
                        noVenta = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error al insertar venta: " + e.getMessage(), e);
        }

        if (noVenta > 0) {
            for (LineaVenta linea : lineas) {
                DetalleVenta detalle = new DetalleVenta(0, noVenta,
                        linea.getIsbn(), linea.getCantidad(), linea.getPrecio());
                detalleVentaDAO.crear(detalle);
                descontarStock(linea.getIsbn(), linea.getCantidad());
            }
        }
        return noVenta;
    }

    /**
     * Reduce la cantidad existente en el inventario para un producto específico.
     * 
     * @param isbn El código ISBN identificador del libro.
     * @param cantidad La cantidad de unidades a restar del inventario.
     * @return true si la actualización de stock afectó al menos una fila; false en caso contrario.
     * @throws DaoException Si se genera un error de SQL al ejecutar el procedimiento de descuento.
     */
    private boolean descontarStock(String isbn, int cantidad) {
        String sql = "{call sp_descontar_stock(?,?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setString(1, isbn);
            consulta.setInt(2, cantidad);
            return consulta.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Error al descontar stock: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un registro de venta del sistema a través de su número identificador.
     * 
     * @param noVenta El número de la venta que se desea eliminar.
     * @return true si la venta se eliminó correctamente; false en caso contrario.
     * @throws DaoException Si se presenta un error durante el proceso de eliminación SQL.
     */
    @Override
    public boolean eliminar(Integer noVenta) {
        String sql = "{call sp_eliminar_venta(?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setInt(1, noVenta);
            return consulta.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Error al eliminar venta: " + e.getMessage(), e);
        }
    }
}