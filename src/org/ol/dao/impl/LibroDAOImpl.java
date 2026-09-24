package org.ol.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.ol.dao.LibroDAO;
import org.ol.exception.DaoException;
import org.ol.model.Libro;
import org.ol.util.Conexion;

/**
 * Implementación de la interfaz LibroDAO para gestionar las operaciones 
 * de persistencia (CRUD) de la entidad Libro en la base de datos MySQL.
 * 
 * @author Octavio Letona
 * @version 1.0.0
 * @see org.ol.dao.LibroDAO
 */
public class LibroDAOImpl implements LibroDAO {

    /**
     * Obtiene la lista completa de libros registrados en el sistema.
     * 
     * @return Un ArrayList de objetos Libro.
     * @throws DaoException Si ocurre un error al ejecutar la consulta SQL.
     */
    @Override
    public ArrayList<Libro> listarTodos() {
        ArrayList<Libro> lista = new ArrayList<>();
        String sql = "{call sp_listar_todos_libros()}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql);
                ResultSet rs = consulta.executeQuery()) {
            while (rs.next()) {
                Libro l = new Libro();
                l.setIsbn(rs.getString("isbn"));
                l.setTitulo(rs.getString("titulo"));
                l.setFechaPublicacion(rs.getString("fecha_publicacion"));
                l.setPrecio(rs.getDouble("precio"));
                l.setIdCategoria(rs.getInt("id_categoria"));
                l.setNitEditorial(rs.getString("nit_editorial"));
                l.setStock(rs.getInt("stock"));
                lista.add(l);
            }
        } catch (SQLException e) {
            throw new DaoException("Error al listar libros: " + e.getMessage(), e);
        }
        return lista;
    }

    /**
     * Busca los datos de un libro específico utilizando su código ISBN.
     * 
     * @param isbn El código ISBN identificador del libro.
     * @return El objeto Libro encontrado o null si no existe.
     * @throws DaoException Si ocurre un error al ejecutar la búsqueda SQL.
     */
    @Override
    public Libro buscarPorId(String isbn) {
        Libro l = null;
        String sql = "{call sp_buscar_libro_id(?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setString(1, isbn);
            try (ResultSet rs = consulta.executeQuery()) {
                if (rs.next()) {
                    l = new Libro();
                    l.setIsbn(rs.getString("isbn"));
                    l.setTitulo(rs.getString("titulo"));
                    l.setFechaPublicacion(rs.getString("fecha_publicacion"));
                    l.setPrecio(rs.getDouble("precio"));
                    l.setIdCategoria(rs.getInt("id_categoria"));
                    l.setNitEditorial(rs.getString("nit_editorial"));
                    l.setStock(rs.getInt("stock"));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error al buscar libro: " + e.getMessage(), e);
        }
        return l;
    }

    /**
     * Inserta un nuevo registro de libro en la base de datos.
     * 
     * @param libro El objeto Libro que contiene la información a guardar.
     * @return true si la inserción fue exitosa; false en caso contrario.
     * @throws DaoException Si ocurre un error al ejecutar el procedimiento almacenado.
     */
    @Override
    public boolean crear(Libro libro) {
        String sql = "{call sp_crear_libro(?,?,?,?,?,?,?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setString(1, libro.getIsbn());
            consulta.setString(2, libro.getTitulo());
            consulta.setString(3, libro.getFechaPublicacion());
            consulta.setDouble(4, libro.getPrecio());
            consulta.setInt(5, libro.getIdCategoria());
            consulta.setString(6, libro.getNitEditorial());
            consulta.setInt(7, libro.getStock());
            return consulta.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Error al insertar libro: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza la información de un libro previamente registrado.
     * 
     * @param libro El objeto Libro con los datos modificados.
     * @return true si la actualización modificó alguna fila; false en caso contrario.
     * @throws DaoException Si ocurre un error al actualizar los datos en la base de datos.
     */
    @Override
    public boolean actualizar(Libro libro) {
        String sql = "{call sp_actualizar_libro(?,?,?,?,?,?,?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setString(1, libro.getIsbn());
            consulta.setString(2, libro.getTitulo());
            consulta.setString(3, libro.getFechaPublicacion());
            consulta.setDouble(4, libro.getPrecio());
            consulta.setInt(5, libro.getIdCategoria());
            consulta.setString(6, libro.getNitEditorial());
            consulta.setInt(7, libro.getStock());
            return consulta.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Error al actualizar libro: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina el registro de un libro del sistema utilizando su código ISBN.
     * 
     * @param isbn El código ISBN del libro a eliminar.
     * @return true si el registro fue borrado correctamente; false en caso contrario.
     * @throws DaoException Si ocurre un error al procesar la eliminación SQL.
     */
    @Override
    public boolean eliminar(String isbn) {
        String sql = "{call sp_eliminar_libro(?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setString(1, isbn);
            return consulta.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Error al eliminar libro: " + e.getMessage(), e);
        }
    }
}