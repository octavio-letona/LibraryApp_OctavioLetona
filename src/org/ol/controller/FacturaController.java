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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.ol.dao.FacturaDAO;
import org.ol.dao.impl.FacturaDAOImpl;
import org.ol.exception.DaoException;
import org.ol.model.LineaFactura;
import org.ol.system.Main;

/**
 * Controlador FXML encargado de mostrar la factura correspondiente a una
 * venta previamente seleccionada en {@code ListaVentasController}.
 * <p>
 * No recibe la venta mediante parámetros de escena: el número de venta se
 * comunica a través del campo estático {@link #noVentaSeleccionada}, el cual
 * debe establecerse con {@link #setNoVentaSeleccionada(int)} antes de abrir
 * esta vista. Al inicializar, carga las líneas de la factura y los datos de
 * encabezado (cliente, fecha, usuario y total) desde {@link FacturaDAO}.
 *
 * @author Octavio Javier Letona Figueroa
 * @version 1.0.0
 * @see LineaFactura
 * @see FacturaDAO
 */
public class FacturaController implements Initializable {

    //Mecanismo del proyecto: no hay paso de datos entre vistas, se usa un campo
    //estatico que ListaVentasController setea antes de abrir la vista.
    private static int noVentaSeleccionada;

    /**
     * Establece el número de venta cuya factura se mostrará al inicializar
     * esta vista. Debe invocarse antes de cambiar a la escena de factura.
     *
     * @param noVenta el número de venta seleccionado previamente en la
     *                lista de ventas.
     */
    public static void setNoVentaSeleccionada(int noVenta) {
        noVentaSeleccionada = noVenta;
    }

    private final FacturaDAO facturaDAO = new FacturaDAOImpl();
    private final ObservableList<LineaFactura> lineasFactura = FXCollections.observableArrayList();

    @FXML
    private Label lblNoFactura;
    @FXML
    private Label lblFecha;
    @FXML
    private Label lblCliente;
    @FXML
    private Label lblCui;
    @FXML
    private Label lblCorreo;
    @FXML
    private Label lblUsuario;
    @FXML
    private Label lblTotal;
    @FXML
    private TableView<LineaFactura> tablaLineas;
    @FXML
    private TableColumn colTitulo;
    @FXML
    private TableColumn colIsbn;
    @FXML
    private TableColumn colCantidad;
    @FXML
    private TableColumn colPrecioUnitario;
    @FXML
    private TableColumn colSubtotal;
    @FXML
    private Button btnImprimir;

    /**
     * Inicializa el controlador después de que su elemento raíz haya sido
     * procesado por completo. Configura las columnas de la tabla de líneas
     * y carga la factura de {@link #noVentaSeleccionada}.
     *
     * @param location  la ubicación usada para resolver rutas relativas del
     *                  objeto raíz, o {@code null} si no se conoce.
     * @param resources los recursos usados para localizar el objeto raíz,
     *                  o {@code null} si no se localizó.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configurarTabla();
        cargarFactura();
    }

    /**
     * Asocia cada columna de {@link #tablaLineas} con la propiedad
     * correspondiente del modelo {@link LineaFactura} mediante
     * {@link PropertyValueFactory}.
     */
    public void configurarTabla() {
        colTitulo.setCellValueFactory(new PropertyValueFactory<LineaFactura, String>("tituloLibro"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<LineaFactura, String>("isbnLibro"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<LineaFactura, Integer>("cantidad"));
        colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<LineaFactura, Double>("precioUnitario"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<LineaFactura, Double>("subtotal"));
    }

    /**
     * Recupera las líneas de la factura correspondiente a
     * {@link #noVentaSeleccionada} desde {@link #facturaDAO}, y con la
     * primera línea (que trae el encabezado repetido) llena los labels de
     * número de factura, fecha, cliente, CUI, correo, usuario y gran total.
     * Si no se encuentra ninguna línea o ocurre un error de acceso a datos,
     * se muestra una alerta de error.
     */
    private void cargarFactura() {
        try {
            lineasFactura.setAll(facturaDAO.buscarFactura(noVentaSeleccionada));
            if (lineasFactura.isEmpty()) {
                mostrarError("No se encontró la factura de la venta " + noVentaSeleccionada + ".");
                return;
            }
            //La primera fila trae el encabezado repetido; se usa para llenar los labels.
            LineaFactura encabezado = lineasFactura.get(0);
            lblNoFactura.setText("# " + encabezado.getNumeroFactura());
            lblFecha.setText(encabezado.getFechaEmision());
            lblCliente.setText(encabezado.getNombreCliente());
            lblCui.setText(String.valueOf(encabezado.getCuiCliente()));
            lblCorreo.setText(encabezado.getCorreoCliente());
            lblUsuario.setText(encabezado.getUsuarioAtendio());
            lblTotal.setText(String.format("Q %.2f", encabezado.getGranTotal()));
            tablaLineas.setItems(lineasFactura);
        } catch (DaoException e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Regresa a la vista de lista de ventas (origen de la factura),
     * cambiando de escena mediante {@link Main#cambiarEscena(String)}.
     * Si ocurre un error al cambiar de escena, se muestra una alerta.
     */
    @FXML
    private void handleVolver() {
        try {
            //Regresa a la lista de ventas (origen de la factura), no al dashboard.
            Main.cambiarEscena("/org/ol/view/ListaVentasView.fxml");
        } catch (Exception e) {
            mostrarError("Error al volver al menú: " + e.getMessage());
        }
    }

    /**
     * Muestra un mensaje informativo indicando que la funcionalidad de
     * impresión de la factura aún está en desarrollo.
     */
    @FXML
    private void handleImprimir() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Imprimir");
        alert.setHeaderText(null);
        alert.setContentText("La impresión de la factura está en desarrollo.");
        alert.showAndWait();
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
}