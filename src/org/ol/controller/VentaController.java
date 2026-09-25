/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.ol.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.ol.dao.ClienteDAO;
import org.ol.dao.LibroDAO;
import org.ol.dao.VentaDAO;
import org.ol.dao.impl.ClienteDAOImpl;
import org.ol.dao.impl.LibroDAOImpl;
import org.ol.dao.impl.VentaDAOImpl;
import org.ol.exception.DaoException;
import org.ol.exception.ValidacionException;
import org.ol.manager.SesionContext;
import org.ol.model.Cliente;
import org.ol.model.Libro;
import org.ol.model.LineaVenta;
import org.ol.model.Venta;
import org.ol.system.Main;

/**
 * Controlador FXML encargado de registrar una nueva venta dentro de la
 * aplicación LibraryApp.
 * <p>
 * Arma líneas de venta (libro + cantidad) en una tabla temporal
 * ({@link #lineasVenta}), calcula el total automáticamente a partir de los
 * subtotales de cada {@link LineaVenta} y, al confirmar, crea la
 * {@link Venta} junto con sus detalles y descuenta el stock correspondiente
 * mediante {@link VentaDAO#crearVenta(Venta, ObservableList)}.
 *
 * @author Octavio Javier Letona Figueroa
 * @version 1.0.0
 * @see Venta
 * @see LineaVenta
 * @see VentaDAO
 */
public class VentaController implements Initializable {

    @FXML
    private ComboBox<Cliente> cmbCliente;
    @FXML
    private ComboBox<Libro> cmbLibro;
    @FXML
    private Spinner<Integer> spCantidad;
    @FXML
    private Button btnAgregar;
    @FXML
    private Button btnRegistrar;
    @FXML
    private Button btnQuitar;
    @FXML
    private Button btnVaciar;
    @FXML
    private TableView<LineaVenta> tablaLineas;
    @FXML
    private TableColumn colIsbn;
    @FXML
    private TableColumn colTitulo;
    @FXML
    private TableColumn colPrecio;
    @FXML
    private TableColumn colCantidad;
    @FXML
    private TableColumn colSubtotal;
    @FXML
    private Label lblTotal;
    @FXML
    private Label lblMensaje;

    private final VentaDAO ventaDAO = new VentaDAOImpl();
    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final LibroDAO libroDAO = new LibroDAOImpl();
    private final ObservableList<LineaVenta> lineasVenta = FXCollections.observableArrayList();

    /**
     * Inicializa el controlador después de que su elemento raíz haya sido
     * procesado por completo. Carga los combos de cliente y libro,
     * configura la tabla de líneas, el spinner de cantidad y calcula el
     * total inicial (Q0.00).
     *
     * @param location  la ubicación usada para resolver rutas relativas del
     *                  objeto raíz, o {@code null} si no se conoce.
     * @param resources los recursos usados para localizar el objeto raíz,
     *                  o {@code null} si no se localizó.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarCombos();
        tablaLineas.setItems(lineasVenta);
        configurarTabla();
        configurarSpinner();
        calcularTotal();
    }

    /**
     * Carga los combos de {@link #cmbCliente} y {@link #cmbLibro} con la
     * información obtenida de {@link #clienteDAO} y {@link #libroDAO}
     * respectivamente. Si ocurre un error de acceso a datos, se muestra una
     * alerta al usuario.
     */
    private void cargarCombos() {
        try {
            cmbCliente.setItems(FXCollections.observableArrayList(clienteDAO.listarTodos()));
            cmbLibro.setItems(FXCollections.observableArrayList(libroDAO.listarTodos()));
        } catch (DaoException e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Asocia cada columna de {@link #tablaLineas} con la propiedad
     * correspondiente del modelo {@link LineaVenta} mediante
     * {@link PropertyValueFactory}.
     */
    private void configurarTabla() {
        colIsbn.setCellValueFactory(new PropertyValueFactory<LineaVenta, String>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<LineaVenta, String>("titulo"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<LineaVenta, Double>("precio"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<LineaVenta, Integer>("cantidad"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<LineaVenta, Double>("subtotal"));
    }

    /**
     * Configura {@link #spCantidad} con un rango de 1 a 999 y valor inicial
     * de 1 mediante {@link SpinnerValueFactory.IntegerSpinnerValueFactory}.
     */
    private void configurarSpinner() {
        spCantidad.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999, 1));
    }

    /**
     * Recalcula el total de la venta sumando el subtotal de cada
     * {@link LineaVenta} en {@link #lineasVenta} y actualiza
     * {@link #lblTotal} con el formato "Total: Qxx.xx".
     */
    private void calcularTotal() {
        double total = 0;
        for (LineaVenta linea : lineasVenta) {
            total += linea.getSubtotal();
        }
        lblTotal.setText(String.format("Total: Q%.2f", total));
    }

    /**
     * Agrega una nueva línea a {@link #lineasVenta} con el libro
     * seleccionado en {@link #cmbLibro} y la cantidad de {@link #spCantidad},
     * validando que se haya seleccionado un libro y que exista stock
     * suficiente. Al agregar, recalcula el total y reinicia el combo de
     * libro y el spinner de cantidad.
     */
    @FXML
    private void handleAgregarLinea() {
        Libro libro = cmbLibro.getValue();
        if (libro == null) {
            mostrarAdvertencia("Seleccione un libro para agregar a la venta.");
            return;
        }
        int cantidad = spCantidad.getValue();
        if (libro.getStock() < cantidad) {
            mostrarAdvertencia("Stock insuficiente. Disponible: " + libro.getStock() + ".");
            return;
        }
        lineasVenta.add(new LineaVenta(libro, cantidad));
        calcularTotal();
        lblMensaje.setText("");
        cmbLibro.setValue(null);
        spCantidad.getValueFactory().setValue(1);
    }

    /**
     * Elimina de {@link #lineasVenta} la línea seleccionada en
     * {@link #tablaLineas} y recalcula el total. Si no hay ninguna línea
     * seleccionada, muestra una advertencia y no realiza ninguna acción
     * adicional.
     */
    @FXML
    private void handleQuitarLinea() {
        LineaVenta seleccion = tablaLineas.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            mostrarAdvertencia("Seleccione una línea de la tabla para quitar.");
            return;
        }
        lineasVenta.remove(seleccion);
        calcularTotal();
    }

    /**
     * Elimina todas las líneas de {@link #lineasVenta}, recalcula el total
     * y limpia el mensaje de estado.
     */
    @FXML
    private void handleVaciar() {
        lineasVenta.clear();
        calcularTotal();
        lblMensaje.setText("");
    }

    /**
     * Valida que se haya seleccionado un cliente y que exista al menos una
     * línea de venta, calcula el total y registra la venta junto con sus
     * líneas mediante {@link #ventaDAO}, lo que también descuenta el stock
     * de cada libro. Muestra un mensaje de éxito con el número de venta
     * generado o una alerta de error si el registro falla.
     *
     * @throws ValidacionException si no se seleccionó un cliente o si no
     *                              se agregó ninguna línea a la venta
     *                              (capturada internamente y mostrada como
     *                              advertencia al usuario).
     */
    @FXML
    private void handleRegistrarVenta() {
        try {
            ValidacionException.validarNoNulo(cmbCliente.getValue(),
                    "Seleccione el cliente de la venta.");
            if (lineasVenta.isEmpty()) {
                throw new ValidacionException("Agregue al menos un libro a la venta.");
            }

            //1. Guardar el encabezado de la venta con sus líneas y el stock.
            double total = 0;
            for (LineaVenta linea : lineasVenta) {
                total += linea.getSubtotal();
            }
            Venta venta = new Venta(0, null, total, cmbCliente.getValue().getCui(),
                    SesionContext.getInstancia().getUsuarioActual().getId());
            int noVenta = ventaDAO.crearVenta(venta, lineasVenta);

            if (noVenta <= 0) {
                mostrarError("No se pudo registrar la venta.");
                return;
            }

            lblMensaje.setText("Venta #" + noVenta + " registrada exitosamente.");
            limpiarVenta();
        } catch (ValidacionException e) {
            mostrarAdvertencia(e.getMessage());
            lblMensaje.setText(e.getMessage());
        } catch (Exception e) {
            mostrarError("Error al registrar la venta: " + e.getMessage());
        }
    }

    /**
     * Restablece el estado del formulario tras registrar una venta: limpia
     * las líneas, el cliente y el libro seleccionados, reinicia el spinner
     * de cantidad a 1 y recalcula el total.
     */
    private void limpiarVenta() {
        lineasVenta.clear();
        cmbCliente.setValue(null);
        cmbLibro.setValue(null);
        spCantidad.getValueFactory().setValue(1);
        calcularTotal();
    }

    /**
     * Regresa al menú principal correspondiente al rol del usuario,
     * cambiando de escena mediante {@link Main#cambiarEscena(String)}.
     * Si ocurre un error al cambiar de escena, se muestra una alerta.
     */
    @FXML
    private void handleVolver() {
        try {
            Main.cambiarEscena(Main.rutaDashboardSegunRol());
        } catch (Exception e) {
            mostrarError("Error al volver al menú: " + e.getMessage());
        }
    }

    /**
     * Muestra una alerta de tipo error con el mensaje indicado.
     *
     * @param mensaje el texto a mostrar en el cuerpo de la alerta.
     */
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra una alerta de tipo advertencia con el mensaje indicado.
     *
     * @param mensaje el texto a mostrar en el cuerpo de la alerta.
     */
    private void mostrarAdvertencia(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}