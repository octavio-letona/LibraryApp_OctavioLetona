package org.ol.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
/**
 * Gestiona la conexión a la base de datos utilizando el patrón Singleton y JDBC.
 * Carga las credenciales y configuración desde un archivo de propiedades externo.
 * @author Octavio Letona
 * @version 1.0.0
 * @see Connection
 */
public class Conexion {
    /** Instancia única de la clase Conexion (patrón Singleton). */
    private static Conexion instancia;
    /** Ruta del archivo de configuración de la base de datos en el classpath. */
    private static final String CONFIG_FILE = "/db.properties";
    /** URL de conexión para el driver JDBC de la base de datos. */
    private final String url;
    /** Nombre de usuario para la autenticación en la base de datos. */
    private final String user;
    /** Contraseña de acceso para el usuario de la base de datos. */
    private final String password;
    /**
     * Constructor privado que registra el driver de MySQL y carga las propiedades del archivo de configuración.
     * @throws IllegalStateException Si el archivo no existe o faltan propiedades obligatorias.
     */
    private Conexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error Driver: " + e.getMessage());
        }
        Properties config = new Properties();
        try (InputStream in = getClass().getResourceAsStream(CONFIG_FILE)) {
            if (in == null) {
                throw new IllegalStateException(
                        "No se encontro " + CONFIG_FILE + " en el classpath. "
                        + "Copia db.properties.example como src/db.properties y ajusta los valores.");
            }
            config.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Error al leer " + CONFIG_FILE, e);
        }
        this.url = config.getProperty("db.url");
        this.user = config.getProperty("db.user");
        this.password = config.getProperty("db.password");
        if (url == null || user == null || password == null) {
            throw new IllegalStateException(
                    "Faltan propiedades (db.url, db.user, db.password) en " + CONFIG_FILE);
        }
    }
    /**
     * Obtiene de forma sincronizada la única instancia activa del gestor de conexiones.
     * @return La instancia única de tipo {@code Conexion}.
     */
    public static synchronized Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }
    /**
     * Establece y retorna una nueva conexión activa hacia la base de datos.
     * @return Un objeto {@link Connection} listo para interactuar con la base de datos.
     * @throws SQLException Si ocurre un error de acceso o autenticación con la base de datos.
     */
    public Connection conectar() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}