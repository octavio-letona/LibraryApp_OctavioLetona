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
 * Controlador del panel de operaciones para usuarios con rol de Cajero.
 * 
 * Proporciona una interfaz simplificada y enfocada en operaciones de caja y ventas,
 * permitiendo acceso a las siguientes funcionalidades:
 * - Crear y registrar nuevas ventas
 * - Gestionar detalles de ventas
 * - Ver lista de ventas registradas
 * - Consultar inventario disponible
 * 
 * Muestra información de bienvenida del usuario en sesión y proporciona acceso rápido
 * mediante tarjetas interactivas (cards) para las funcionalidades más frecuentes.
 * 
 * El controlador maneja la autenticación del usuario mediante SesionContext y
 * proporciona un flujo de navegación para cerrar sesión.
 * 
 * @author Octavio Letona
 * @version 1.0.0
 * @since 2026
 */
public class CajeroController implements Initializable {

    @FXML private Label lblBienvenida;
    @FXML private Label lblRol;
    @FXML private Button btnCerrarSesion;
    @FXML private Circle avatarCircle;

    @FXML private Button btnVenta;
    @FXML private Button btnDetalleVenta;
    @FXML private Button btnListaVentas;
    @FXML private Button btnInventario;

    @FXML private VBox cardAgregarVenta;
    @FXML private VBox cardDetalleVenta;
    @FXML private VBox cardListaVentas;
    @FXML private VBox cardVerInventario;

    private Usuario usuarioActual;

    /**
     * Inicializa el controlador del panel de cajero cargando la información del usuario en sesión.
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
     * Ejemplo: "cajero" → "Cajero", "VENDEDOR" → "Vendedor"
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
     * Maneja la navegación hacia la sección de creación de ventas.
     * 
     * @param evento ActionEvent generado por el botón correspondiente
     * @see #navegar(String)
     */
    @FXML
    public void irAVenta(ActionEvent evento) {
        navegar("/org/ol/view/VentaView.fxml");
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
     * Maneja la navegación hacia la sección de consulta de lista de ventas.
     * 
     * @param evento ActionEvent generado por el botón correspondiente
     * @see #navegar(String)
     */
    @FXML
    public void irAListaVentas(ActionEvent evento) {
        navegar("/org/ol/view/ListaVentasView.fxml");
    }

    /**
     * Maneja la navegación hacia la sección de consulta del inventario.
     * 
     * @param evento ActionEvent generado por el botón correspondiente
     * @see #navegar(String)
     */
    @FXML
    public void irAInventario(ActionEvent evento) {
        navegar("/org/ol/view/InventarioView.fxml");
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
     * Maneja el evento para consultar y gestionar detalles de ventas.
     * Navega hacia la vista de detalles de ventas.
     * 
     * @param evento MouseEvent generado por la tarjeta de detalles de venta
     * @see #navegar(String)
     */
    @FXML
    public void detalleVenta(MouseEvent evento) {
        navegar("/org/ol/view/DetalleVentaView.fxml");
    }

    /**
     * Maneja el evento para ver la lista de todas las ventas registradas.
     * Navega hacia la vista de lista de ventas.
     * 
     * @param evento MouseEvent generado por la tarjeta de lista de ventas
     * @see #navegar(String)
     */
    @FXML
    public void listaVentas(MouseEvent evento) {
        navegar("/org/ol/view/ListaVentasView.fxml");
    }

    /**
     * Maneja el evento para consultar el inventario disponible.
     * Navega hacia la vista del inventario.
     * 
     * @param evento MouseEvent generado por la tarjeta de ver inventario
     * @see #navegar(String)
     */
    @FXML
    public void verInventario(MouseEvent evento) {
        navegar("/org/ol/view/InventarioView.fxml");
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
}