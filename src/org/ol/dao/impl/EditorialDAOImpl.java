package org.ol.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.ol.dao.EditorialDAO;
import org.ol.exception.DaoException;
import org.ol.model.Editorial;
import org.ol.util.Conexion;

/**
 * Implementación de la interfaz EditorialDAO para gestionar las operaciones 
 * de persistencia (CRUD) de la entidad Editorial en la base de datos MySQL.
 * 
 * @author Octavio Letona
 * @version 1.0.0
 * @see org.ol.dao.EditorialDAO
 */
public class EditorialDAOImpl implements EditorialDAO {

    /**
     * Obtiene la lista completa de editoriales registradas en el sistema.
     * 
     * @return Un ArrayList de objetos Editorial.
     * @throws DaoException Si ocurre un error de SQL durante la consulta.
     */
    @Override
    public ArrayList<Editorial> listarTodos() {
        ArrayList<Editorial> lista = new ArrayList<>();
        String sql = "{call sp_listar_todos_editoriales()}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql);
                ResultSet rs = consulta.executeQuery()) {
            while (rs.next()) {
                Editorial e = new Editorial();
                e.setNit(rs.getString("nit"));
                e.setNombreEditorial(rs.getString("nombre_editorial"));
                e.setTelefonoEditorial(rs.getString("telefono_editorial"));
                e.setDireccionEditoria(rs.getString("direccion_editorial"));
                lista.add(e);
            }
        } catch (SQLException ex) {
            throw new DaoException("Error al listar editoriales: " + ex.getMessage(), ex);
        }
        return lista;
    }

    /**
     * Busca y obtiene los datos de una editorial por su número de NIT.
     * 
     * @param nit El NIT identificador de la editorial.
     * @return El objeto Editorial encontrado o null si no existe.
     * @throws DaoException Si ocurre un error de SQL durante la búsqueda.
     */
    @Override
    public Editorial buscarPorId(String nit) {
        Editorial e = null;
        String sql = "{call sp_buscar_editorial_por_id(?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setString(1, nit);
            try (ResultSet rs = consulta.executeQuery()) {
                if (rs.next()) {
                    e = new Editorial();
                    e.setNit(rs.getString("nit"));
                    e.setNombreEditorial(rs.getString("nombre_editorial"));
                    e.setTelefonoEditorial(rs.getString("telefono_editorial"));
                    e.setDireccionEditoria(rs.getString("direccion_editorial"));
                }
            }
        } catch (SQLException ex) {
            throw new DaoException("Error al buscar editorial: " + ex.getMessage(), ex);
        }
        return e;
    }

    /**
     * Registra una nueva editorial en la base de datos.
     * 
     * @param editorial El objeto Editorial con la información a insertar.
     * @return true si la inserción fue exitosa; false en caso contrario.
     * @throws DaoException Si ocurre un error de SQL al ejecutar el procedimiento almacenado.
     */
    @Override
    public boolean crear(Editorial editorial) {
        String sql = "{call sp_crear_editorial(?,?,?,?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setString(1, editorial.getNit());
            consulta.setString(2, editorial.getNombreEditorial());
            consulta.setString(3, editorial.getTelefonoEditorial());
            consulta.setString(4, editorial.getDireccionEditoria());
            return consulta.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new DaoException("Error al insertar editorial: " + ex.getMessage(), ex);
        }
    }

    /**
     * Actualiza los datos de una editorial existente.
     * 
     * @param editorial El objeto Editorial con los datos modificados.
     * @return true si se actualizó al menos una fila; false en caso contrario.
     * @throws DaoException Si ocurre un error de SQL al actualizar el registro.
     */
    @Override
    public boolean actualizar(Editorial editorial) {
        String sql = "{call sp_actualizar_editorial(?,?,?,?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setString(1, editorial.getNit());
            consulta.setString(2, editorial.getNombreEditorial());
            consulta.setString(3, editorial.getTelefonoEditorial());
            consulta.setString(4, editorial.getDireccionEditoria());
            return consulta.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new DaoException("Error al actualizar editorial: " + ex.getMessage(), ex);
        }
    }

    /**
     * Elimina el registro de una editorial mediante su número de NIT.
     * 
     * @param nit El NIT de la editorial a eliminar.
     * @return true si la eliminación fue exitosa; false en caso contrario.
     * @throws DaoException Si ocurre un error de SQL al procesar la eliminación.
     */
    @Override
    public boolean eliminar(String nit) {
        String sql = "{call sp_eliminar_editorial(?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setString(1, nit);
            return consulta.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new DaoException("Error al eliminar editorial: " + ex.getMessage(), ex);
        }
    }
}