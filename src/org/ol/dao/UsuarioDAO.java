/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.ol.dao;

import java.util.ArrayList;
import org.ol.model.Usuario;


public interface UsuarioDAO {
    public Usuario iniciarSesion(String usernarme, String passwordHash);
    public boolean crearUsuario(Usuario usuario);
    public boolean actualizarUsuario(Usuario usuario);
    public boolean cambiarPassword(int idUsuario, String passwordHash);
    public boolean desactivarUsuario(int idUsuario);
    public boolean eliminarUsuario(int idUsuario);
    public ArrayList<Usuario> listarTodosUsuarios();
    public Usuario obtenerUsuarioPorId(int idUsuario);
}