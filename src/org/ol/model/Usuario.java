/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ol.model;

import java.sql.Timestamp;

/**
 * Representa a un usuario del sistema (por ejemplo, un empleado que inicia
 * sesión y atiende ventas). Guarda sus credenciales, datos personales, rol
 * y estado. Es un POJO: atributos privados, constructores y métodos de
 * acceso (get y set).
 *
 * @author Octavio Letona
 * @version 1.0.0
 * @see Venta
 */
public class Usuario {

    /** Identificador único del usuario en la base de datos. */
    private int id;

    /** Nombre de usuario utilizado para iniciar sesión. */
    private String username;

    /** Correo electrónico del usuario. */
    private String email;

    /** Nombre(s) de pila del usuario. */
    private String firstName;

    /** Apellido(s) del usuario. */
    private String lastName;

    /** Hash de la contraseña; nunca se almacena la contraseña en texto plano. */
    private String passwordHash;

    /** Rol del usuario dentro del sistema, que define sus permisos. */
    private String rol;

    /** Indica si la cuenta está activa ({@code true}) o deshabilitada ({@code false}). */
    private boolean activo;

    /** Fecha y hora en que se creó la cuenta. */
    private Timestamp fechaCreacion;

    /**
     * Constructor vacío. Crea un usuario sin datos inicializados; los
     * valores se pueden asignar posteriormente mediante los métodos setter.
     */
    public Usuario() {
    }

    /**
     * Constructor con los datos mínimos de identificación. Los demás
     * atributos conservan su valor por defecto.
     *
     * @param id       identificador único del usuario; debe ser un entero positivo
     * @param username nombre de usuario para iniciar sesión
     * @param rol      rol del usuario dentro del sistema
     */
    public Usuario(int id, String username, String rol) {
        this.id = id;
        this.username = username;
        this.rol = rol;
    }

    /**
     * Constructor para registrar un usuario nuevo. No recibe el
     * identificador, la fecha de creación ni el estado, porque se asignan
     * al guardarlo en la base de datos.
     *
     * @param username     nombre de usuario para iniciar sesión
     * @param email        correo electrónico; se espera un formato válido (usuario@dominio)
     * @param firstName    nombre(s) de pila del usuario
     * @param lastName     apellido(s) del usuario
     * @param passwordHash hash de la contraseña; no debe ser la contraseña en texto plano
     * @param rol          rol del usuario dentro del sistema
     */
    public Usuario(String username, String email, String firstName, String lastName,
            String passwordHash, String rol) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }

    /**
     * Obtiene el rol del usuario.
     *
     * @return el rol del usuario
     */
    public String getRol() {
        return rol;
    }

    /**
     * Establece el rol del usuario.
     *
     * @param rol nuevo rol del usuario
     */
    public void setRol(String rol) {
        this.rol = rol;
    }

    /**
     * Obtiene el identificador único del usuario.
     *
     * @return el identificador del usuario
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el identificador único del usuario.
     *
     * @param id nuevo identificador del usuario; debe ser un entero positivo
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de usuario.
     *
     * @return el nombre de usuario
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario.
     *
     * @param username nuevo nombre de usuario
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     *
     * @return el correo electrónico del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     *
     * @param email nuevo correo electrónico; se espera un formato válido (usuario@dominio)
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el nombre de pila del usuario.
     *
     * @return el nombre del usuario
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Establece el nombre de pila del usuario.
     *
     * @param firstName nuevo nombre del usuario
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Obtiene el apellido del usuario.
     *
     * @return el apellido del usuario
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Establece el apellido del usuario.
     *
     * @param lastName nuevo apellido del usuario
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Obtiene el hash de la contraseña del usuario.
     *
     * @return el hash de la contraseña
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Establece el hash de la contraseña del usuario.
     *
     * @param passwordHash nuevo hash de la contraseña; no debe ser la contraseña en texto plano
     */
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    /**
     * Indica si la cuenta del usuario está activa.
     *
     * @return {@code true} si la cuenta está activa; {@code false} en caso contrario
     */
    public boolean isActivo() {
        return activo;
    }

    /**
     * Establece si la cuenta del usuario está activa.
     *
     * @param activo {@code true} para activar la cuenta; {@code false} para deshabilitarla
     */
    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    /**
     * Obtiene la fecha y hora de creación de la cuenta.
     *
     * @return la marca de tiempo de creación de la cuenta
     */
    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Establece la fecha y hora de creación de la cuenta.
     *
     * @param fechaCreacion nueva marca de tiempo de creación
     */
    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Devuelve la representación en texto del usuario, que corresponde a su
     * nombre de usuario.
     *
     * @return el nombre de usuario
     */
    @Override
    public String toString() {
        return username;
    }
}