/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.ol.dao;

import java.util.ArrayList;
import org.ol.model.Usuario;

/**
 * Contrato DAO que define las operaciones de persistencia, autenticación
 * y gestión para la entidad {@link Usuario} dentro del sistema.
 * 
 * @author Octavio Letona
 * @version 1.0.0
 * @see org.ol.model.Usuario
 */
public interface UsuarioDAO {

    /**
     * Autentica a un usuario en el sistema mediante sus credenciales de acceso.
     * 
     * @param username Nombre de usuario o identificador de acceso.
     * @param passwordHash Hash de la contraseña ingresada por el usuario.
     * @return Objeto {@link Usuario} autenticado si las credenciales son válidas; {@code null} en caso contrario.
     * @throws RuntimeException si ocurre un error de conexión con JDBC o la base de datos.
     */
    public Usuario iniciarSesion(String username, String passwordHash);

    /**
     * Registra un nuevo usuario en la base de datos.
     * 
     * @param usuario Objeto {@link Usuario} que contiene los datos a registrar. No debe ser null.
     * @return {@code true} si la inserción fue exitosa; {@code false} en caso contrario.
     * @throws IllegalArgumentException si el objeto usuario contiene datos inválidos o nulos.
     * @throws RuntimeException si ocurre un error de conexión con la base de datos.
     */
    public boolean crearUsuario(Usuario usuario);

    /**
     * Actualiza la información de un usuario existente en la base de datos.
     * 
     * @param usuario Objeto {@link Usuario} con los datos modificados. No debe ser null.
     * @return {@code true} si la actualización fue exitosa; {@code false} en caso contrario.
     * @throws RuntimeException si ocurre un error al procesar la actualización.
     */
    public boolean actualizarUsuario(Usuario usuario);

    /**
     * Actualiza la contraseña o clave de acceso de un usuario específico.
     * 
     * @param idUsuario Identificador único del usuario.
     * @param passwordHash Nuevo hash de la contraseña a establecer.
     * @return {@code true} si la contraseña se actualizó correctamente; {@code false} en caso contrario.
     * @throws RuntimeException si ocurre un error de conexión con la base de datos.
     */
    public boolean cambiarPassword(int idUsuario, String passwordHash);

    /**
     * Realiza un borrado lógico desactivando la cuenta de usuario en el sistema.
     * 
     * @param idUsuario Identificador único del usuario a desactivar.
     * @return {@code true} si el estado del usuario fue actualizado a inactivo; {@code false} en caso contrario.
     * @throws RuntimeException si ocurre un error durante el proceso de desactivación.
     */
    public boolean desactivarUsuario(int idUsuario);

    /**
     * Elimina de forma física el registro de un usuario de la base de datos.
     * 
     * @param idUsuario Identificador único del usuario a eliminar.
     * @return {@code true} si la eliminación fue exitosa; {@code false} en caso contrario.
     * @throws RuntimeException si ocurre un error al intentar eliminar el registro.
     */
    public boolean eliminarUsuario(int idUsuario);

    /**
     * Recupera el listado completo de usuarios registrados en la base de datos.
     * 
     * @return Un {@link ArrayList} que contiene los objetos {@link Usuario}.
     *         Retorna una lista vacía si no existen registros.
     * @throws RuntimeException si ocurre un error al consultar la base de datos.
     */
    public ArrayList<Usuario> listarTodosUsuarios();

    /**
     * Busca y recupera la información de un usuario específico mediante su ID.
     * 
     * @param idUsuario Identificador único del usuario.
     * @return El objeto {@link Usuario} correspondiente, o {@code null} si no existe.
     * @throws RuntimeException si ocurre un error durante la búsqueda en la base de datos.
     */
    public Usuario obtenerUsuarioPorId(int idUsuario);
}