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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import org.ol.model.Usuario;
import org.ol.system.Main;
import org.ol.manager.SesionContext;

/**
 * Controlador del panel de control (dashboard) administrativo de la aplicación.
 * 
 * Proporciona la interfaz principal para que los administradores accedan a las
 * diferentes secciones de gestión del sistema, incluyendo:
 * - Gestión de usuarios
 * - Gestión de libros
 * - Gestión de autores
 * - Gestión de categorías
 * - Gestión de editoriales
 * - Gestión de ventas
 * - Gestión de relaciones autor-libro
 * - Gestión de detalles de ventas
 * - Gestión de clientes
 * 
 * Muestra información de bienvenida del usuario en sesión y proporciona acceso rápido
 * mediante tarjetas interactivas (cards) para funcionalidades frecuentes.
 * 
 * @author Octavio Letona
 * @version 1.0.0
 * @since 2026
 */
public class AdminDashboardViewController implements Initializable {

    @FXML private Label lblBienvenida;
    @FXML private Label lblRol;
    @FXML private Button btnCerrarSesion;
    @FXML private Circle avatarCircle;

    @FXML private Button btnUsuario;
    @FXML private Button btnLibro;
    @FXML private Button btnAutor;
    @FXML private Button btnCategoria;
    @FXML private Button btnEditorial;
    @FXML private Button btnVentas;
    @FXML private Button btnAutorLibro;
    @FXML private Button btnDetalleVenta;

    @FXML private VBox cardNuevoLibro;
    @FXML private VBox cardAgregarVenta;
    @FXML private VBox cardVerInventario;
    @FXML private VBox cardGestionarUsuarios;
    @FXML private VBox cardReportes;
    @FXML private VBox cardConfiguracion;

    private Usuario usuarioActual;

    /**
     * Inicializa el controlador del dashboard cargando la información del usuario en sesión.
     * Se ejecuta automáticamente cuando se carga el archivo FXML.
     * 
     * Operaciones realizadas:
     * - Obtiene el usuario actual del contexto de sesión
     * - Establece el nombre de usuario en el label de bienvenida
     * - Calcula las iniciales del usuario y establece el label de rol
     * - Maneja el caso de usuarios no autenticados mostrando "Invitado"
     * 
     * @param url URL de localización del recurso FXML
     * @param rb ResourceBundle con recursos internacionalizados
     * @see SesionContext#getInstancia()
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usuarioActual = SesionContext.getInstancia().getUsuarioActual();
        if (usuarioActual != null) {
            lblBienvenida.setText(usuarioActual.getUsername());
            String iniciales = usuarioActual.getUsername()
                    .substring(0, Math.min(2, usuarioActual.getUsername().length()))
                    .toUpperCase();
            lblRol.setText(iniciales + " · " + capitalize(usuarioActual.getRol()));
        } else {
            lblBienvenida.setText("Invitado");
            lblRol.setText("?? · Sin sesión");
        }
    }

    /**
     * Capitaliza la primera letra de un texto y convierte el resto a minúsculas.
     * 
     * Ejemplo: "admin" → "Admin", "USUARIO" → "Usuario"
     * 
     * @param texto el texto a capitalizar
     * @return el texto capitalizado, o cadena vacía si el texto es nulo o vacío
     */
    private String capitalize(String texto) {
        if (texto == null || texto.isEmpty()) return "";
        return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
    }

    /**
     * Maneja el evento de cerrar sesión del usuario actual.
     * Limpia el contexto de sesión y redirige a la pantalla de inicio de sesión.
     * 
     * @param evento ActionEvent generado por el botón Cerrar Sesión
     * @see SesionContext#cerrarSesion()
     * @see #navegar(String)
     */
    @FXML
    public void cerrarSesion(ActionEvent evento) {
        SesionContext.getInstancia().cerrarSesion();
        navegar("/org/ol/view/IniciodeSesionView.fxml");
    }

    /**
     * Maneja la navegación hacia la sección de gestión de usuarios.
     * 
     * @param evento ActionEvent generado por el botón correspondiente
     * @see #navegar(String)
     */
    @FXML
    public void irAUsuario(ActionEvent evento) {
        navegar("/org/ol/view/UsuarioView.fxml");
    }

    /**
     * Maneja la navegación hacia la sección de gestión de libros.
     * 
     * @param evento ActionEvent generado por el botón correspondiente
     * @see #navegar(String)
     */
    @FXML
    public void irALibro(ActionEvent evento) {
        navegar("/org/ol/view/LibroView.fxml");
    }

    /**
     * Maneja la navegación hacia la sección de gestión de autores.
     * 
     * @param evento ActionEvent generado por el botón correspondiente
     * @see #navegar(String)
     */
    @FXML
    public void irAAutor(ActionEvent evento) {
        navegar("/org/ol/view/AutorView.fxml");
    }

    /**
     * Maneja la navegación hacia la sección de gestión de categorías.
     * 
     * @param evento ActionEvent generado por el botón correspondiente
     * @see #navegar(String)
     */
    @FXML
    public void irACategoria(ActionEvent evento) {
        navegar("/org/ol/view/CategoriaView.fxml");
    }

    /**
     * Maneja la navegación hacia la sección de gestión de editoriales.
     * 
     * @param evento ActionEvent generado por el botón correspondiente
     * @see #navegar(String)
     */
    @FXML
    public void irAEditorial(ActionEvent evento) {
        navegar("/org/ol/view/EditorialView.fxml");
    }

    /**
     * Maneja la navegación hacia la sección de gestión de ventas.
     * 
     * @param evento ActionEvent generado por el botón correspondiente
     * @see #navegar(String)
     */
    @FXML
    public void irAVentas(ActionEvent evento) {
        navegar("/org/ol/view/ListaVentasView.fxml");
    }

    /**
     * Maneja la navegación hacia la sección de gestión de relaciones autor-libro.
     * 
     * @param evento ActionEvent generado por el botón correspondiente
     * @see #navegar(String)
     */
    @FXML
    public void irAAutorLibro(ActionEvent evento) {
        navegar("/org/ol/view/AutorLibroView.fxml");
    }

    /**
     * Maneja la navegación hacia la sección de gestión de detalles de ventas.
     * 
     * @param evento ActionEvent generado por el botón correspondiente
     * @see #navegar(String)
     */
    @FXML
    public void irADetalleVenta(ActionEvent evento) {
        navegar("/org/ol/view/DetalleVentaView.fxml");
    }

    /**
     * Maneja la navegación hacia la sección de gestión de clientes.
     * Utiliza Main.cambiarEscena() de manera directa para cambiar de vista.
     * 
     * @param evento ActionEvent generado por el botón correspondiente
     */
    @FXML
    public void irAClientes(ActionEvent evento) {
        try {
            Main.cambiarEscena("/org/ol/view/ClienteView.fxml");
        } catch (IOException e) {
            System.err.println("Error al cargar clientes: " + e.getMessage());
            
        }
    }

    /**
     * Maneja el evento de crear un nuevo libro.
     * Navega hacia el formulario de creación de libros.
     * 
     * @param evento MouseEvent generado por la tarjeta de nuevo libro
     * @see #navegar(String)
     */
    @FXML
    public void nuevoLibro(MouseEvent evento) {
        navegar("/org/ol/view/LibroFormView.fxml");
    }

    /**
     * Maneja el evento de agregar una nueva venta.
     * Navega hacia el formulario de creación de ventas.
     * 
     * @param evento MouseEvent generado por la tarjeta de agregar venta
     * @see #navegar(String)
     */
    @FXML
    public void agregarVenta(MouseEvent evento) {
        navegar("/org/ol/view/VentaView.fxml");
    }

    /**
     * Maneja el evento para ver el inventario disponible.
     * Navega hacia la vista del inventario.
     * 
     * @param evento MouseEvent generado por la tarjeta de inventario
     * @see #navegar(String)
     */
    @FXML
    public void verInventario(MouseEvent evento) {
        navegar("/org/ol/view/InventarioView.fxml");
    }

    /**
     * Maneja el evento de gestionar usuarios del sistema.
     * Navega hacia la vista de gestión de usuarios.
     * 
     * @param evento MouseEvent generado por la tarjeta de gestión de usuarios
     * @see #navegar(String)
     */
    @FXML
    public void gestionarUsuarios(MouseEvent evento) {
        navegar("/org/ol/view/GestionUsuariosView.fxml");
    }

    /**
     * Maneja el evento para acceder a los reportes del sistema.
     * Navega hacia la vista de reportes.
     * 
     * @param evento MouseEvent generado por la tarjeta de reportes
     * @see #navegar(String)
     */
    @FXML
    public void reportes(MouseEvent evento) {
        navegar("/org/ol/view/ReportesView.fxml");
    }

    /**
     * Maneja el evento para acceder a la configuración del sistema.
     * Navega hacia la vista de configuración.
     * 
     * @param evento MouseEvent generado por la tarjeta de configuración
     * @see #navegar(String)
     */
    @FXML
    public void configuracion(MouseEvent evento) {
        navegar("/org/ol/view/ConfiguracionView.fxml");
    }

    /**
     * Navega hacia una vista específica cambiando la escena actual.
     * Si la ruta no existe o no está disponible, muestra un diálogo de información
     * indicando que la sección estará disponible próximamente.
     * 
     * @param ruta la ruta del archivo FXML a cargar
     * @throws IOException si ocurre un error al cargar la escena
     * @throws NullPointerException si la ruta no se encuentra o es nula
     */
    private void navegar(String ruta) {
        try {
            Main.cambiarEscena(ruta);
        } catch (IOException | NullPointerException e) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION,
                    "Esta sección estará disponible próximamente.", ButtonType.OK);
            alerta.setTitle("En construcción");
            alerta.setHeaderText(null);
            alerta.showAndWait();
        }
    }

    /**
     * Inicializa el dashboard con un usuario específico.
     * Actualiza los labels de bienvenida y rol con la información del usuario proporcionado.
     * 
     * Este método es útil para establecer el usuario después de que el controlador
     * ha sido inicializado si el usuario no estaba disponible en ese momento.
     * 
     * @param usuario el Usuario a establecer como usuario actual del dashboard
     */
    public void iniciarUsuario(Usuario usuario) {
        this.usuarioActual = usuario;
        lblBienvenida.setText(usuario.getUsername());
        String iniciales = usuario.getUsername()
                .substring(0, Math.min(2, usuario.getUsername().length()))
                .toUpperCase();
        lblRol.setText(iniciales + " · " + capitalize(usuario.getRol()));
    }
}