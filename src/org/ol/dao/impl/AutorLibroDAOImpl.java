package org.ol.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.ol.dao.AutorLibroDAO;
import org.ol.exception.DaoException;
import org.ol.model.AutorLibro;
import org.ol.util.Conexion;

/**
 * Implementación de la interfaz AutorLibroDAO para gestionar las relaciones 
 * entre autores y libros en la base de datos MySQL.
 * 
 * @author Octavio Letona
 * @version 1.0.0
 * @see org.ol.dao.AutorLibroDAO
 */
public class AutorLibroDAOImpl implements AutorLibroDAO {

    /**
     * Obtiene la lista completa de relaciones entre autores y libros registradas en el sistema.
     * 
     * @return Un ArrayList de objetos AutorLibro.
     * @throws DaoException Si ocurre un error de SQL al consultar los registros.
     */
    @Override
    public ArrayList<AutorLibro> listarTodos() {
        ArrayList<AutorLibro> lista = new ArrayList<>();
        String sql = "{call sp_listarautoreslibro()}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql);
                ResultSet rs = consulta.executeQuery()) {
            while (rs.next()) {
                AutorLibro al = new AutorLibro();
                al.setIdAutorLibro(rs.getInt("id_autor_libro"));
                al.setIdAutor(rs.getInt("id_autor"));
                al.setIsbn(rs.getString("isbn"));
                lista.add(al);
            }
        } catch (SQLException e) {
            throw new DaoException("Error al listar autores_libro: " + e.getMessage(), e);
        }
        return lista;
    }

    /**
     * Busca y obtiene la relación entre un autor y un libro según su ID único.
     * 
     * @param idAutorLibro El identificador único del registro AutorLibro.
     * @return El objeto AutorLibro encontrado o null si no existe.
     * @throws DaoException Si ocurre un error de SQL durante la búsqueda.
     */
    @Override
    public AutorLibro buscarPorId(Integer idAutorLibro) {
        AutorLibro al = null;
        String sql = "{call sp_buscarautorlibro(?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setInt(1, idAutorLibro);
            try (ResultSet rs = consulta.executeQuery()) {
                if (rs.next()) {
                    al = new AutorLibro();
                    al.setIdAutorLibro(rs.getInt("id_autor_libro"));
                    al.setIdAutor(rs.getInt("id_autor"));
                    al.setIsbn(rs.getString("isbn"));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error al buscar autor_libro: " + e.getMessage(), e);
        }
        return al;
    }

    /**
     * Registra una nueva asignación de autor a un libro en la base de datos.
     * 
     * @param autorLibro El objeto AutorLibro con la información a insertar.
     * @return true si la inserción fue exitosa; false en caso contrario.
     * @throws DaoException Si ocurre un error de SQL al ejecutar el procedimiento almacenado.
     */
    @Override
    public boolean crear(AutorLibro autorLibro) {
        String sql = "{call sp_insertarautorlibro(?,?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setInt(1, autorLibro.getIdAutor());
            consulta.setString(2, autorLibro.getIsbn());
            return consulta.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Error al insertar autor_libro: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza la información de una relación autor-libro existente.
     * 
     * @param autorLibro El objeto AutorLibro con los datos modificados.
     * @return true si se actualizó al menos un registro; false en caso contrario.
     * @throws DaoException Si ocurre un error de SQL al ejecutar la actualización.
     */
    @Override
    public boolean actualizar(AutorLibro autorLibro) {
        String sql = "{call sp_actualizarautorlibro(?,?,?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setInt(1, autorLibro.getIdAutorLibro());
            consulta.setInt(2, autorLibro.getIdAutor());
            consulta.setString(3, autorLibro.getIsbn());
            return consulta.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Error al actualizar autor_libro: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina el registro de una relación entre autor y libro mediante su ID.
     * 
     * @param idAutorLibro El identificador de la relación autor-libro a eliminar.
     * @return true si la eliminación fue exitosa; false en caso contrario.
     * @throws DaoException Si ocurre un error de SQL al procesar la eliminación.
     */
    @Override
    public boolean eliminar(Integer idAutorLibro) {
        String sql = "{call sp_eliminarautorlibro(?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setInt(1, idAutorLibro);
            return consulta.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Error al eliminar autor_libro: " + e.getMessage(), e);
        }
    }
}