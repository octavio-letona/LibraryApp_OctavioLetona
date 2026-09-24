package org.ol.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.ol.dao.ClienteDAO;
import org.ol.exception.DaoException;
import org.ol.model.Cliente;
import org.ol.util.Conexion;

/**
 * Implementación de la interfaz ClienteDAO para gestionar las operaciones 
 * de acceso a datos (CRUD) de la entidad Cliente.
 * 
 * @author octavio letona
 * @version 1.0.0
 * @see org.ol.dao.ClienteDAO
 */
public class ClienteDAOImpl implements ClienteDAO {

    /**
     * Recupera una lista con todos los clientes registrados en el sistema.
     * @return Una lista de tipo ArrayList que contiene objetos Cliente.
     * @throws DaoException Si ocurre un error de SQL al ejecutar el procedimiento almacenado.
     */
    @Override
    public ArrayList<Cliente> listarTodos() {
        ArrayList<Cliente> lista = new ArrayList<>();
        String sql = "{call sp_listarclientes()}";
        try (Connection conexion = Conexion.getInstancia().conectar(); CallableStatement consulta = conexion.prepareCall(sql); ResultSet rs = consulta.executeQuery()) {
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setCui(rs.getLong("cui"));
                c.setNombreCliente(rs.getString("nombre_cliente"));
                c.setApellidoCliente(rs.getString("apellido_cliente"));
                c.setCorreoElectronico(rs.getString("correo_electronico"));
                lista.add(c);
            }
        } catch (SQLException e) {
            throw new DaoException("Error al listar clientes: " + e.getMessage(), e);
        }
        return lista;
    }
    /**
     * Busca los datos de un cliente específico utilizando su identificador.
     * @param cui El Código Único de Identificación (CUI) del cliente que se desea buscar.
     * @return Un objeto Cliente con los datos encontrados, o null si el cliente no existe.
     * @throws DaoException Si ocurre un error en la base de datos al buscar el registro.
     */
    @Override
    public Cliente buscarPorId(Long cui) {
        Cliente c = null;
        String sql = "{call sp_buscarcliente(?)}";
        try (Connection conexion = Conexion.getInstancia().conectar(); CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setLong(1, cui);
            try (ResultSet rs = consulta.executeQuery()) {
                if (rs.next()) {
                    c = new Cliente();
                    c.setCui(rs.getLong("cui"));
                    c.setNombreCliente(rs.getString("nombre_cliente"));
                    c.setApellidoCliente(rs.getString("apellido_cliente"));
                    c.setCorreoElectronico(rs.getString("correo_electronico"));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error al buscar cliente: " + e.getMessage(), e);
        }
        return c;
    }

    /**
     * Registra un nuevo cliente en la base de datos.
     * @param cliente El objeto Cliente que contiene los datos a insertar.
     * @return true si el registro se creó exitosamente; false en caso contrario.
     * @throws DaoException Si ocurre un error de SQL durante la inserción.
     */
    @Override
    public boolean crear(Cliente cliente) {
        String sql = "{call sp_insertarcliente(?,?,?,?)}";
        try (Connection conexion = Conexion.getInstancia().conectar(); CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setLong(1, cliente.getCui());
            consulta.setString(2, cliente.getNombreCliente());
            consulta.setString(3, cliente.getApellidoCliente());
            consulta.setString(4, cliente.getCorreoElectronico());
            return consulta.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Error al insertar cliente: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza la información de un cliente previamente registrado.
     * @param cliente El objeto Cliente con los datos modificados.
     * @return true si la actualización se aplicó correctamente; false si no se modificaron filas.
     * @throws DaoException Si ocurre un error de SQL al intentar realizar la actualización.
     */
    @Override
    public boolean actualizar(Cliente cliente) {
        String sql = "{call sp_actualizarcliente(?,?,?,?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setLong(1, cliente.getCui());
            consulta.setString(2, cliente.getNombreCliente());
            consulta.setString(3, cliente.getApellidoCliente());
            consulta.setString(4, cliente.getCorreoElectronico());
            return consulta.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Error al actualizar cliente: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina el registro de un cliente del sistema.
     * 
     * @param cui El Código Único de Identificación (CUI) del cliente a eliminar.
     * @return true si el cliente fue eliminado satisfactoriamente; false en caso contrario.
     * @throws DaoException Si ocurre un error de SQL al procesar la eliminación.
     */
    @Override
    public boolean eliminar(Long cui) {
        String sql = "{call sp_eliminarcliente(?)}";
        try (Connection conexion = Conexion.getInstancia().conectar(); CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setLong(1, cui);
            return consulta.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Error al eliminar cliente: " + e.getMessage(), e);
        }
    }
}