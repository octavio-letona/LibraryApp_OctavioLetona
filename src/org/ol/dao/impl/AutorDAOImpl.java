package org.ol.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.ol.dao.AutorDAO;
import org.ol.exception.DaoException;
import org.ol.model.Autor;
import org.ol.util.Conexion;

/**
 * Implementación de la interfaz AutorDAO para gestionar las operaciones 
 * de persistencia (CRUD) de la entidad Autor en la base de datos MySQL.
 * 
 * @author Octavio Letona
 * @version 1.0.0
 * @see org.ol.dao.AutorDAO
 */
public class AutorDAOImpl implements AutorDAO {

    /**
     * Obtiene la lista completa de autores registrados en el sistema.
     * 
     * @return Un ArrayList de objetos Autor.
     * @throws DaoException Si ocurre un error de SQL al consultar los registros.
     */
    @Override
    public ArrayList<Autor> listarTodos() {
        ArrayList<Autor> lista = new ArrayList<>();
        String sql = "{call sp_listarautores()}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql);
                ResultSet rs = consulta.executeQuery()) {
            while (rs.next()) {
                Autor a = new Autor();
                a.setIdAutor(rs.getInt("id_autor"));
                a.setNombreAutor(rs.getString("nombre_autor"));
                a.setApellidoAutor(rs.getString("apellido_autor"));
                a.setNacionalidad(rs.getString("nacionalidad"));
                a.setBiografia(rs.getString("biografia"));
                lista.add(a);
            }
        } catch (SQLException e) {
            throw new DaoException("Error al listar autores: " + e.getMessage(), e);
        }
        return lista;
    }

    /**
     * Busca y obtiene los datos de un autor específico por su ID.
     * 
     * @param idAutor El identificador único del autor.
     * @return El objeto Autor encontrado o null si no existe.
     * @throws DaoException Si ocurre un error de SQL durante la búsqueda.
     */
    @Override
    public Autor buscarPorId(Integer idAutor) {
        Autor a = null;
        String sql = "{call sp_buscarautor(?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setInt(1, idAutor);
            try (ResultSet rs = consulta.executeQuery()) {
                if (rs.next()) {
                    a = new Autor();
                    a.setIdAutor(rs.getInt("id_autor"));
                    a.setNombreAutor(rs.getString("nombre_autor"));
                    a.setApellidoAutor(rs.getString("apellido_autor"));
                    a.setNacionalidad(rs.getString("nacionalidad"));
                    a.setBiografia(rs.getString("biografia"));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error al buscar autor: " + e.getMessage(), e);
        }
        return a;
    }

    /**
     * Registra un nuevo autor en la base de datos.
     * 
     * @param autor El objeto Autor con la información a insertar.
     * @return true si la inserción fue exitosa; false en caso contrario.
     * @throws DaoException Si ocurre un error de SQL al ejecutar el procedimiento almacenado.
     */
    @Override
    public boolean crear(Autor autor) {
        String sql = "{call sp_insertarautor(?,?,?,?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setString(1, autor.getNombreAutor());
            consulta.setString(2, autor.getApellidoAutor());
            consulta.setString(3, autor.getNacionalidad());
            consulta.setString(4, autor.getBiografia());
            return consulta.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Error al insertar autor: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza los datos de un autor existente.
     * 
     * @param autor El objeto Autor con los datos modificados.
     * @return true si se actualizó al menos un registro; false en caso contrario.
     * @throws DaoException Si ocurre un error de SQL al actualizar los datos.
     */
    @Override
    public boolean actualizar(Autor autor) {
        String sql = "{call sp_actualizarautor(?,?,?,?,?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setInt(1, autor.getIdAutor());
            consulta.setString(2, autor.getNombreAutor());
            consulta.setString(3, autor.getApellidoAutor());
            consulta.setString(4, autor.getNacionalidad());
            consulta.setString(5, autor.getBiografia());
            return consulta.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Error al actualizar autor: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina el registro de un autor de la base de datos mediante su ID.
     * 
     * @param idAutor El identificador único del autor a eliminar.
     * @return true si la eliminación fue exitosa; false en caso contrario.
     * @throws DaoException Si ocurre un error de SQL al procesar la eliminación.
     */
    @Override
    public boolean eliminar(Integer idAutor) {
        String sql = "{call sp_eliminarautor(?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setInt(1, idAutor);
            return consulta.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Error al eliminar autor: " + e.getMessage(), e);
        }
    }
}