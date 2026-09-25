/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.ol.controller;


import java.io.IOException;
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
import org.ol.dao.UsuarioDAO;
import org.ol.dao.impl.UsuarioDAOImpl;
import org.ol.exception.DaoException;
import org.ol.exception.ValidacionException;
import org.ol.model.Usuario;
import org.ol.system.Main;
import org.ol.util.SecurityUtil;

public class RegistrarUsuarioController implements Initializable {

    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtConfirmarPassword;
    @FXML
    private Button btnRegistrar;
    @FXML
    private Button btnVolver;
    @FXML
    private Label lblMensaje;

    private UsuarioDAO usuarioDAO;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioDAO = new UsuarioDAOImpl();
        lblMensaje.setText("");
    }

    @FXML
    public void eventoRegistrar(ActionEvent evento) {
        try {
            ValidacionException.validarNoVacio(txtUsuario.getText(), "usuario");
            ValidacionException.validarNoVacio(txtEmail.getText(), "correo electrónico");
            ValidacionException.validarFormatoEmail(txtEmail.getText(), "El correo electrónico no es válido.");
            ValidacionException.validarNoVacio(txtPassword.getText(), "contraseña");
            ValidacionException.validarNoVacio(txtConfirmarPassword.getText(), "confirmar contraseña");
            ValidacionException.validarCoinciden(txtPassword.getText(), txtConfirmarPassword.getText(),
                    "Las contraseñas no coinciden.");
            ValidacionException.validarLongitudMinima(txtPassword.getText(), 6,
                    "La contraseña debe tener al menos 6 caracteres.");
            String usuario = txtUsuario.getText().trim();
            String email = txtEmail.getText().trim();
            String nombre = txtNombre.getText().trim();
            String apellido = txtApellido.getText().trim();
            String passwordHash = SecurityUtil.hashSHA256(txtPassword.getText());
            Usuario nuevoUsuario = new Usuario(usuario, email, nombre, apellido, passwordHash, "empleado");
            boolean registrado = usuarioDAO.crearUsuario(nuevoUsuario);

            if (registrado) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "Usuario registrado exitosamente.");
                Main.cambiarEscena("/org/ol/view/IniciodeSesionView.fxml");
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "Error al registrar. El usuario podría ya existir.");
            }
        } catch (ValidacionException e) {
            mostrarAlerta(Alert.AlertType.WARNING, e.getMessage());
            lblMensaje.setText(e.getMessage());
        } catch (IOException e) {
            System.err.println("Error al volver al login: " + e.getMessage());
        } catch (DaoException e) {
            mostrarAlerta(Alert.AlertType.ERROR, e.getMessage());
            lblMensaje.setText("Error al registrar");
        }
    }

    @FXML
    public void eventoVolver(ActionEvent evento) {
        try {
            Main.cambiarEscena("/org/ol/view/IniciodeSesionView.fxml");
        } catch (IOException e) {
            System.err.println("Error al volver al login: " + e.getMessage());
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alerta = new Alert(tipo, mensaje, ButtonType.OK);
        alerta.showAndWait();
    }
}