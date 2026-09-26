package org.ol.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import org.ol.dao.UsuarioDAO;
import org.ol.exception.DaoException;
import org.ol.model.Usuario;
import org.ol.util.Conexion;

/**
 * Implementación de la interfaz UsuarioDAO para gestionar el acceso a datos,
 * autenticación y administración de usuarios en la base de datos MySQL.
 * 
 * @author Octavio Letona
 * @version 1.0.0
 * @see org.ol.dao.UsuarioDAO
 */
public class UsuarioDAOImpl implements UsuarioDAO {

    /**
     * Autentica a un usuario en el sistema validando sus credenciales de acceso.
     * 
     * @param usernarme El nombre de usuario ingresado.
     * @param passwordHash La contraseña encriptada o hash correspondiente al usuario.
     * @return Un objeto Usuario cargado con ID, username y rol si las credenciales son válidas; null en caso contrario.
     * @throws DaoException Si ocurre un error de SQL durante el proceso de autenticación.
     */
    @Override
    public Usuario iniciarSesion(String usernarme, String passwordHash) {
        Usuario usuario = null;
        String sql = "{call sp_iniciar_sesion(?,?)}";

        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {

            consulta.setString(1, usernarme);
            consulta.setString(2, passwordHash);

            try (ResultSet tablaResultado = consulta.executeQuery()) {
                if (tablaResultado.next()) {
                    usuario = new Usuario();
                    usuario.setId(tablaResultado.getInt("id_usuario"));
                    usuario.setUsername(tablaResultado.getString("username"));
                    usuario.setRol(tablaResultado.getString("rol"));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error al iniciar sesion: " + e.getMessage(), e);
        }

        return usuario;
    }

    /**
     * Registra un nuevo usuario dentro del sistema.
     * 
     * @param usuario El objeto Usuario que contiene la información completa a registrar.
     * @return true si el registro fue insertado con éxito; false en caso contrario.
     * @throws DaoException Si ocurre un error de SQL al intentar registrar el usuario.
     */
    @Override
    public boolean crearUsuario(Usuario usuario) {
        String sql = "{call sp_crear_usuario(?,?,?,?,?,?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setString(1, usuario.getUsername());
            consulta.setString(2, usuario.getEmail());
            consulta.setString(3, usuario.getFirstName());
            consulta.setString(4, usuario.getLastName());
            consulta.setString(5, usuario.getPasswordHash());
            consulta.setString(6, usuario.getRol());
            int filasAfectadas = consulta.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            throw new DaoException("Error al crear usuario: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza la información personal, rol y estado de un usuario existente.
     * 
     * @param usuario El objeto Usuario con los datos modificados.
     * @return true si la actualización modificó al menos una fila; false en caso contrario.
     * @throws DaoException Si ocurre un error de SQL al actualizar los datos.
     */
    @Override
    public boolean actualizarUsuario(Usuario usuario) {
        String sql = "{call sp_actualizar_usuario(?,?,?,?,?,?,?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setInt(1, usuario.getId());
            consulta.setString(2, usuario.getUsername());
            consulta.setString(3, usuario.getEmail());
            consulta.setString(4, usuario.getFirstName());
            consulta.setString(5, usuario.getLastName());
            consulta.setString(6, usuario.getRol());
            consulta.setBoolean(7, usuario.isActivo());
            int filasAfectadas = consulta.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            throw new DaoException("Error al actualizar usuario: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza únicamente la clave de acceso de un usuario en particular.
     * 
     * @param idUsuario El identificador único del usuario.
     * @param passwordHash El nuevo hash de la contraseña a guardar.
     * @return true si el cambio de clave fue exitoso; false en caso contrario.
     * @throws DaoException Si ocurre un error de SQL al ejecutar el cambio de clave.
     */
    @Override
    public boolean cambiarPassword(int idUsuario, String passwordHash) {
        String sql = "{call sp_cambiar_password(?,?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setInt(1, idUsuario);
            consulta.setString(2, passwordHash);
            int filasAfectadas = consulta.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            throw new DaoException("Error al cambiar password: " + e.getMessage(), e);
        }
    }

    /**
     * Cambia el estado de un usuario a inactivo para impedir su acceso al sistema.
     * 
     * @param idUsuario El identificador único del usuario a desactivar.
     * @return true si la operación se completó correctamente; false en caso contrario.
     * @throws DaoException Si ocurre un error de SQL durante la desactivación.
     */
    @Override
    public boolean desactivarUsuario(int idUsuario) {
        String sql = "{call sp_desactivar_usuario(?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setInt(1, idUsuario);
            int filasAfectadas = consulta.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            throw new DaoException("Error al desactivar usuario: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina el registro de un usuario de manera definitiva de la base de datos.
     * 
     * @param idUsuario El identificador único del usuario a eliminar.
     * @return true si el registro fue borrado exitosamente; false en caso contrario.
     * @throws DaoException Si ocurre un error de SQL durante la eliminación del usuario.
     */
    @Override
    public boolean eliminarUsuario(int idUsuario) {
        String sql = "{call sp_eliminar_usuario(?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setInt(1, idUsuario);
            int filasAfectadas = consulta.executeUpdate();
            return filasAfectadas > 0;
        } catch (SQLException e) {
            throw new DaoException("Error al eliminar usuario: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene el listado completo de todos los usuarios registrados en el sistema.
     * 
     * @return Una lista de tipo ArrayList con todos los objetos Usuario recuperados.
     * @throws DaoException Si ocurre un error de SQL al consultar los registros.
     */
    @Override
    public ArrayList<Usuario> listarTodosUsuarios() {
        ArrayList<Usuario> lista = new ArrayList<>();
        String sql = "{call sp_listar_todos_usuarios()}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql);
                ResultSet tablaResultado = consulta.executeQuery()) {
            while (tablaResultado.next()) {
                Usuario u = new Usuario();
                u.setId(tablaResultado.getInt("id_usuario"));
                u.setUsername(tablaResultado.getString("username"));
                u.setEmail(tablaResultado.getString("email"));
                u.setFirstName(tablaResultado.getString("first_name"));
                u.setLastName(tablaResultado.getString("last_name"));
                u.setRol(tablaResultado.getString("rol"));
                u.setActivo(tablaResultado.getBoolean("activo"));
                u.setFechaCreacion(tablaResultado.getTimestamp("fecha_creacion"));
                lista.add(u);
            }
        } catch (SQLException e) {
            throw new DaoException("Error al listar todos los usuarios: " + e.getMessage(), e);
        }
        return lista;
    }

    /**
     * Busca y obtiene la información de un usuario específico a partir de su ID.
     * 
     * @param idUsuario El identificador único del usuario a consultar.
     * @return El objeto Usuario con sus detalles completos, o null si no se encuentra.
     * @throws DaoException Si ocurre un error de SQL al ejecutar la búsqueda.
     */
    @Override
    public Usuario obtenerUsuarioPorId(int idUsuario) {
        Usuario usuario = null;
        String sql = "{call sp_obtener_usuario_por_id(?)}";
        try (Connection conexion = Conexion.getInstancia().conectar();
                CallableStatement consulta = conexion.prepareCall(sql)) {
            consulta.setInt(1, idUsuario);
            try (ResultSet tablaResultado = consulta.executeQuery()) {
                if (tablaResultado.next()) {
                    usuario = new Usuario();
                    usuario.setId(tablaResultado.getInt("id_usuario"));
                    usuario.setUsername(tablaResultado.getString("username"));
                    usuario.setEmail(tablaResultado.getString("email"));
                    usuario.setFirstName(tablaResultado.getString("first_name"));
                    usuario.setLastName(tablaResultado.getString("last_name"));
                    usuario.setRol(tablaResultado.getString("rol"));
                    usuario.setActivo(tablaResultado.getBoolean("activo"));
                    usuario.setFechaCreacion(tablaResultado.getTimestamp("fecha_creacion"));
                }
            }
        } catch (SQLException e) {
            throw new DaoException("Error al obtener usuario por id: " + e.getMessage(), e);
        }
        return usuario;
    }
}