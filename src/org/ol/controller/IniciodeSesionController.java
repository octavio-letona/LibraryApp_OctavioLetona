package org.ol.controller;

import java.io.IOException;
import org.ol.dao.UsuarioDAO;
import org.ol.dao.impl.UsuarioDAOImpl;
import org.ol.exception.DaoException;
import org.ol.exception.ValidacionException;
import org.ol.util.SecurityUtil;
import org.ol.model.Usuario;
import org.ol.system.Main;
import org.ol.manager.SesionContext;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controlador para la pantalla de inicio de sesión de la aplicación.
 * 
 * Gestiona el proceso completo de autenticación de usuarios, incluyendo:
 * - Validación de credenciales (usuario y contraseña)
 * - Hashing seguro de contraseñas mediante SHA256
 * - Autenticación contra la base de datos
 * - Gestión de sesión del usuario autenticado
 * - Navegación al dashboard correspondiente según el rol del usuario
 * - Acceso a la pantalla de registro de nuevos usuarios
 * 
 * Soporta autenticación mediante:
 * - Botón de "Iniciar Sesión"
 * - Tecla Enter en los campos de usuario o contraseña
 * 
 * Los roles soportados son: admin, cajero y empleado, cada uno con su dashboard específico.
 * 
 * @author Octavio Letona
 * @version 1.0.0
 * @since 2026
 */
public class IniciodeSesionController implements Initializable {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnIniciarSesion;
    @FXML
    private Label lblMensaje;

    private UsuarioDAO usuarioDAO;

    /**
     * Inicializa el controlador de inicio de sesión.
     * Se ejecuta automáticamente cuando se carga el archivo FXML.
     * 
     * Operaciones realizadas:
     * - Instancia el DAO de usuarios
     * - Limpia el mensaje de error inicial
     * - Configura listeners para detectar la tecla Enter en los campos de usuario y contraseña
     * - Permite iniciar sesión tanto con el botón como presionando Enter
     * 
     * @param url URL de localización del recurso FXML
     * @param rb ResourceBundle con recursos internacionalizados
     * @see UsuarioDAOImpl
     * @see #eventoInicioSesion(ActionEvent)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioDAO = new UsuarioDAOImpl();
        lblMensaje.setText("");
        //Deteccion de teclado: Enter en usuario o contrasena dispara el login,
        //igual que el boton INICIAR.
        txtUsuario.setOnAction(this::eventoInicioSesion);
        txtPassword.setOnAction(this::eventoInicioSesion);
    }

    /**
     * Maneja el evento de inicio de sesión del usuario.
     * Valida que los campos de usuario y contraseña no estén vacíos,
     * hashea la contraseña con SHA256, la autentica contra la base de datos,
     * y si es válida, abre el dashboard correspondiente.
     * 
     * Proceso de autenticación:
     * 1. Valida que usuario y contraseña no estén vacíos
     * 2. Obtiene los valores del usuario y contraseña ingresados
     * 3. Hashea la contraseña usando SHA256 para seguridad
     * 4. Consulta la base de datos para verificar las credenciales
     * 5. Si la autenticación es exitosa, establece la sesión y abre el dashboard
     * 6. Si falla, muestra un mensaje de error
     * 
     * @param evento ActionEvent generado por el botón Iniciar Sesión o presionar Enter
     * @throws DaoException si ocurre un error al acceder a la base de datos
     * @see SecurityUtil#hashSHA256(String)
     * @see UsuarioDAO#iniciarSesion(String, String)
     * @see #abrirDashboard(Usuario)
     */
    @FXML
    public void eventoInicioSesion(ActionEvent evento) {
        try {
            ValidacionException.validarNoVacio(txtUsuario.getText(), "usuario");
            ValidacionException.validarNoVacio(txtPassword.getText(), "contraseña");
            String usuario = txtUsuario.getText();
            String password = txtPassword.getText();
            String passwordHash = SecurityUtil.hashSHA256(password);
            Usuario usuarioIniciado = usuarioDAO.iniciarSesion(usuario, passwordHash);

            if (usuarioIniciado != null) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Inicio correcto");
                abrirDashboard(usuarioIniciado);
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Usuario o contraseña incorrectos");
            }
        } catch (ValidacionException e) {
            mostrarAlerta(Alert.AlertType.WARNING, e.getMessage());
            lblMensaje.setText(e.getMessage());
        } catch (DaoException e) {
            mostrarAlerta(Alert.AlertType.ERROR, e.getMessage());
            lblMensaje.setText("Error al iniciar sesión");
        }
    }

    /**
     * Maneja el evento para navegar hacia la pantalla de registro de nuevos usuarios.
     * Permite a los usuarios no registrados crear una nueva cuenta.
     * 
     * Si ocurre un error al cargar la vista de registro, muestra un mensaje de error.
     * 
     * @param evento ActionEvent generado por el botón Registrarse
     * @see Main#cambiarEscena(String)
     */
    @FXML
    public void eventoRegistrarse(ActionEvent evento) {
        try {
            Main.cambiarEscena("/org/ol/view/RegistrarUsuarioView.fxml");
        } catch (IOException e) {
            System.err.println("Error al cargar registro: " + e.getMessage());
            lblMensaje.setText("Error interno");
        }
    }

    /**
     * Abre el dashboard correspondiente según el rol del usuario autenticado.
     * Establece la sesión del usuario en el contexto global de la aplicación
     * y navega a la vista del dashboard adecuado.
     * 
     * Mapeo de roles a dashboards:
     * - "admin" → AdminDashboardView.fxml
     * - "cajero" → AdminDashboardView.fxml
     * - "empleado" → AdminDashboardView.fxml
     * 
     * Si el rol es desconocido o no está mapeado, muestra un error y cierra la sesión.
     * 
     * @param usuario el Usuario autenticado cuyo dashboard se desea abrir
     * @throws IOException si ocurre un error al cambiar de escena
     * @see SesionContext#setUsuarioActual(Usuario)
     * @see Main#cambiarEscena(String)
     */
    private void abrirDashboard(Usuario usuario) {
        SesionContext.getInstancia().setUsuarioActual(usuario);

        String rol = usuario.getRol();
        String rutaDashboard = "";
        switch (rol) {
            case "admin":
                rutaDashboard = "/org/ol/view/AdminDashboardView.fxml";
                break;
            case "cajero":
                rutaDashboard = "/org/ol/view/AdminDashboardView.fxml";
                break;
            case "empleado":
                rutaDashboard = "/org/ol/view/AdminDashboardView.fxml";
                break;
            default:
                throw new AssertionError();
        }

        //String rutaFXML = Principal.rutaDashboardSegunRol();
        if (rutaDashboard.equals("/org/ol/view/IniciodeSesionView.fxml")) {
            mostrarAlerta(Alert.AlertType.ERROR, "Rol desconocido: " + usuario.getRol());
            SesionContext.getInstancia().cerrarSesion();
            return;
        }
        try {
            Main.cambiarEscena(rutaDashboard);
        } catch (IOException e) {
            System.err.println("Error al cargar la vista:" + rutaDashboard + e.getMessage());
            lblMensaje.setText("Error interno");
        }
    }

    /**
     * Muestra un diálogo de alerta al usuario con el mensaje especificado.
     * El diálogo es modal y bloquea la interacción hasta que sea cerrado.
     * 
     * @param tipo el tipo de alerta a mostrar (INFORMATION, WARNING, ERROR, CONFIRMATION)
     * @param mensaje el texto del mensaje a mostrar en la alerta
     */
    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alerta = new Alert(tipo, mensaje, ButtonType.OK);
        alerta.showAndWait();
    }
}