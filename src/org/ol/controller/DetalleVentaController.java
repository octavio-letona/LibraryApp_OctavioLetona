/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.ol.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import org.ol.dao.DetalleVentaDAO;
import org.ol.dao.LibroDAO;
import org.ol.dao.VentaDAO;
import org.ol.dao.impl.DetalleVentaDAOImpl;
import org.ol.dao.impl.LibroDAOImpl;
import org.ol.dao.impl.VentaDAOImpl;
import org.ol.exception.DaoException;
import org.ol.exception.ValidacionException;
import org.ol.model.DetalleVenta;
import org.ol.model.Libro;
import org.ol.model.Venta;
import org.ol.system.Main;

/**
 * Controlador FXML encargado de gestionar el CRUD y la navegación de los
 * detalles de venta dentro de la aplicación LibraryApp.
 * <p>
 * Permite registrar, editar, buscar y recorrer (primero, anterior, siguiente,
 * último) los registros de {@link DetalleVenta}, enlazando la tabla visual
 * con la capa de acceso a datos ({@link DetalleVentaDAO}, {@link VentaDAO}
 * y {@link LibroDAO}).
 *
 * @author Octavio Javier Letona Figueroa
 * @version 1.0.0
 * @see DetalleVenta
 * @see DetalleVentaDAO
 */
public class DetalleVentaController implements Initializable {

    @FXML
    private ComboBox<Venta> cmbVenta;
    @FXML
    private ComboBox<Libro> cmbLibro;
    @FXML
    private TextField txtCantidad;
    @FXML
    private TextField txtPrecio;
    @FXML
    private Label lblMensaje;
    @FXML
    private TableView<DetalleVenta> tablaDetalleVenta;
    @FXML
    private TableColumn colIdDetalleVenta;
    @FXML
    private TableColumn colNoVenta;
    @FXML
    private TableColumn colIsbn;
    @FXML
    private TableColumn colCantidad;
    @FXML
    private TableColumn colPrecio;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnPrimero;
    @FXML
    private Button btnAnterior;
    @FXML
    private Button btnSiguiente;
    @FXML
    private Button btnUltimo;
    @FXML
    private TextField txtBuscar;

    private boolean modoEdicion = false;
    private DetalleVenta enEdicion;
    private final DetalleVentaDAO detalleVentaDAO = new DetalleVentaDAOImpl();
    private final VentaDAO ventaDAO = new VentaDAOImpl();
    private final LibroDAO libroDAO = new LibroDAOImpl();
    private final ObservableList<DetalleVenta> listaDetalles = FXCollections.observableArrayList();
    private final FilteredList<DetalleVenta> detallesFiltrados = new FilteredList<>(listaDetalles, p -> true);

    /**
     * Inicializa el controlador después de que su elemento raíz haya sido
     * procesado por completo. Carga la tabla, los combos, configura la
     * selección de filas, las columnas de la tabla y el buscador.
     *
     * @param location  la ubicación usada para resolver rutas relativas del
     *                  objeto raíz, o {@code null} si no se conoce.
     * @param resources los recursos usados para localizar el objeto raíz,
     *                  o {@code null} si no se localizó.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarTabla();
        cargarCombos();
        tablaDetalleVenta.setItems(detallesFiltrados);
        seleccionarFila();
        configurarTabla();
        configurarBusqueda();
    }

    /**
     * Asocia cada columna de {@link #tablaDetalleVenta} con la propiedad
     * correspondiente del modelo {@link DetalleVenta} mediante
     * {@link PropertyValueFactory}.
     */
    public void configurarTabla() {
        colIdDetalleVenta.setCellValueFactory(new PropertyValueFactory<DetalleVenta, Integer>("idDetalleVenta"));
        colNoVenta.setCellValueFactory(new PropertyValueFactory<DetalleVenta, Integer>("noVenta"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<DetalleVenta, String>("isbn"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<DetalleVenta, Integer>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<DetalleVenta, Double>("precio"));
    }

    /**
     * Recupera todos los detalles de venta desde la base de datos a través
     * de {@link #detalleVentaDAO} y los carga en {@link #listaDetalles}.
     * Si ocurre un error de acceso a datos, se muestra una alerta al usuario.
     */
    private void cargarTabla() {
        try {
            listaDetalles.setAll(detalleVentaDAO.listarTodos());
        } catch (DaoException e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Carga los combos de {@link #cmbVenta} y {@link #cmbLibro} con la
     * información obtenida de {@link #ventaDAO} y {@link #libroDAO},
     * definiendo además el {@link StringConverter} que muestra el número
     * de venta en {@link #cmbVenta}.
     */
    private void cargarCombos() {
        try {
            cmbVenta.setItems(FXCollections.observableArrayList(ventaDAO.listarTodos()));
            cmbVenta.setConverter(new StringConverter<Venta>() {
                @Override
                public String toString(Venta venta) {
                    return venta == null ? "" : "Venta #" + venta.getNoVenta();
                }

                @Override
                public Venta fromString(String string) {
                    return null;
                }
            });
            cmbLibro.setItems(FXCollections.observableArrayList(libroDAO.listarTodos()));
        } catch (DaoException e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Registra un listener sobre {@link #txtBuscar} para filtrar la tabla
     * de detalles de venta cada vez que cambia el texto de búsqueda.
     */
    private void configurarBusqueda() {
        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> filtrarDetalles());
    }

    /**
     * Aplica un predicado sobre {@link #detallesFiltrados} según el texto
     * ingresado en {@link #txtBuscar}, comparando contra el id, número de
     * venta, ISBN, cantidad y precio de cada {@link DetalleVenta}.
     */
    private void filtrarDetalles() {
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        if (busqueda.isEmpty()) {
            detallesFiltrados.setPredicate(p -> true);
        } else {
            detallesFiltrados.setPredicate(detalle ->
                    String.valueOf(detalle.getIdDetalleVenta()).contains(busqueda)
                    || String.valueOf(detalle.getNoVenta()).contains(busqueda)
                    || detalle.getIsbn().toLowerCase().contains(busqueda)
                    || String.valueOf(detalle.getCantidad()).contains(busqueda)
                    || String.valueOf(detalle.getPrecio()).contains(busqueda));
        }
    }

    /**
     * Registra un listener sobre la selección de {@link #tablaDetalleVenta}
     * para precargar el formulario (combos de venta y libro, cantidad y
     * precio) con los datos de la fila seleccionada y desactivar el
     * formulario para evitar ediciones accidentales.
     */
    private void seleccionarFila() {
        tablaDetalleVenta.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        cmbVenta.setValue(null);
                        for (Venta venta : cmbVenta.getItems()) {
                            if (venta.getNoVenta() == newSelection.getNoVenta()) {
                                cmbVenta.setValue(venta);
                                break;
                            }
                        }
                        cmbLibro.setValue(null);
                        for (Libro libro : cmbLibro.getItems()) {
                            if (libro.getIsbn().equals(newSelection.getIsbn())) {
                                cmbLibro.setValue(libro);
                                break;
                            }
                        }
                        txtCantidad.setText(String.valueOf(newSelection.getCantidad()));
                        txtPrecio.setText(String.valueOf(newSelection.getPrecio()));
                        desactivarFormulario();
                    }
                });
    }

    /**
     * Valida los datos del formulario y guarda el detalle de venta,
     * creando un nuevo registro o actualizando uno existente según
     * {@link #modoEdicion}. Muestra mensajes de éxito, advertencia o error
     * según el resultado de la operación.
     *
     * @throws ValidacionException si algún campo requerido está vacío,
     *                              nulo, no es numérico o no es positivo
     *                              (capturada internamente y mostrada como
     *                              advertencia al usuario).
     */
    @FXML
    private void handleGuardar() {
        try {
            ValidacionException.validarNoNulo(cmbVenta.getValue(),
                    "Seleccione una venta.");
            ValidacionException.validarNoNulo(cmbLibro.getValue(),
                    "Seleccione un libro.");
            ValidacionException.validarNoVacio(txtCantidad.getText(), "cantidad");
            ValidacionException.validarPositivo(txtCantidad.getText(), "cantidad");
            ValidacionException.validarNoVacio(txtPrecio.getText(), "precio");
            ValidacionException.validarDecimal(txtPrecio.getText(), "precio");

            DetalleVenta detalle = new DetalleVenta(
                    modoEdicion ? enEdicion.getIdDetalleVenta() : 0,
                    cmbVenta.getValue().getNoVenta(),
                    cmbLibro.getValue().getIsbn(),
                    Integer.parseInt(txtCantidad.getText().trim()),
                    Double.parseDouble(txtPrecio.getText().trim()));

            boolean guardado;
            if (modoEdicion) {
                guardado = detalleVentaDAO.actualizar(detalle);
            } else {
                guardado = detalleVentaDAO.crear(detalle);
            }

            if (guardado) {
                lblMensaje.setText(modoEdicion
                        ? "Detalle de venta actualizado exitosamente."
                        : "Detalle de venta registrado exitosamente.");
                cargarTabla();
                limpiarFormulario();
                desactivarFormulario();
                activarNavegacion();
                modoEdicion = false;
            } else {
                mostrarError("No se pudo guardar el detalle de venta.");
            }
        } catch (ValidacionException e) {
            mostrarAdvertencia(e.getMessage());
            lblMensaje.setText(e.getMessage());
        } catch (Exception e) {
            mostrarError("Error al guardar: " + e.getMessage());
        }
    }

    /**
     * Cancela la operación de creación o edición en curso: limpia el
     * formulario, lo desactiva, reactiva la navegación y restablece el
     * estado de edición.
     */
    @FXML
    private void handleCancelar() {
        limpiarFormulario();
        desactivarFormulario();
        activarNavegacion();
        modoEdicion = false;
        enEdicion = null;
        lblMensaje.setText("");
    }

    /**
     * Prepara la interfaz para el registro de un nuevo detalle de venta:
     * desactiva el modo edición, limpia el formulario, lo activa, bloquea
     * la navegación y coloca el foco en {@link #cmbVenta}.
     */
    @FXML
    private void handleNuevo() {
        modoEdicion = false;
        enEdicion = null;
        limpiarFormulario();
        activarFormulario();
        desactivarNavegacion();
        tablaDetalleVenta.getSelectionModel().clearSelection();
        lblMensaje.setText("");
        cmbVenta.requestFocus();
    }

    /**
     * Habilita la edición del detalle de venta seleccionado en la tabla.
     * Si no hay ninguna fila seleccionada, muestra un mensaje de error y
     * no realiza ninguna acción adicional.
     */
    @FXML
    private void handleEditar() {
        DetalleVenta seleccion = tablaDetalleVenta.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            mostrarError("Seleccione un detalle de venta de la tabla para editar.");
            return;
        }
        modoEdicion = true;
        enEdicion = seleccion;
        activarFormulario();
        desactivarNavegacion();
        lblMensaje.setText("");
    }

    /**
     * Selecciona y desplaza la vista hasta el primer registro de la tabla
     * de detalles de venta, si existe al menos uno.
     */
    @FXML
    private void handlePrimero() {
        if (!tablaDetalleVenta.getItems().isEmpty()) {
            tablaDetalleVenta.getSelectionModel().selectFirst();
            tablaDetalleVenta.scrollTo(0);
        }
    }

    /**
     * Selecciona el registro anterior al actualmente seleccionado en la
     * tabla de detalles de venta y desplaza la vista hacia él.
     */
    @FXML
    private void handleAnterior() {
        if (!tablaDetalleVenta.getItems().isEmpty()) {
            tablaDetalleVenta.getSelectionModel().selectPrevious();
            if (tablaDetalleVenta.getSelectionModel().getSelectedIndex() >= 0) {
                tablaDetalleVenta.scrollTo(tablaDetalleVenta.getSelectionModel().getSelectedIndex());
            }
        }
    }

    /**
     * Selecciona el registro siguiente al actualmente seleccionado en la
     * tabla de detalles de venta y desplaza la vista hacia él.
     */
    @FXML
    private void handleSiguiente() {
        if (!tablaDetalleVenta.getItems().isEmpty()) {
            tablaDetalleVenta.getSelectionModel().selectNext();
            if (tablaDetalleVenta.getSelectionModel().getSelectedIndex() >= 0) {
                tablaDetalleVenta.scrollTo(tablaDetalleVenta.getSelectionModel().getSelectedIndex());
            }
        }
    }

    /**
     * Selecciona y desplaza la vista hasta el último registro de la tabla
     * de detalles de venta, si existe al menos uno.
     */
    @FXML
    private void handleUltimo() {
        if (!tablaDetalleVenta.getItems().isEmpty()) {
            tablaDetalleVenta.getSelectionModel().selectLast();
            tablaDetalleVenta.scrollTo(tablaDetalleVenta.getItems().size() - 1);
        }
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
     * Restablece los campos del formulario (combos, cantidad y precio) a
     * sus valores vacíos o nulos por defecto.
     */
    private void limpiarFormulario() {
        cmbVenta.setValue(null);
        cmbLibro.setValue(null);
        txtCantidad.clear();
        txtPrecio.clear();
    }

    /**
     * Habilita los controles del formulario (combos de venta y libro,
     * cantidad y precio) para permitir el ingreso o edición de datos.
     */
    private void activarFormulario() {
        cmbVenta.setDisable(false);
        cmbLibro.setDisable(false);
        txtCantidad.setDisable(false);
        txtPrecio.setDisable(false);
    }

    /**
     * Deshabilita los controles del formulario (combos de venta y libro,
     * cantidad y precio) para evitar modificaciones no deseadas.
     */
    private void desactivarFormulario() {
        cmbVenta.setDisable(true);
        cmbLibro.setDisable(true);
        txtCantidad.setDisable(true);
        txtPrecio.setDisable(true);
    }

    /**
     * Habilita los controles de navegación y acciones sobre la tabla
     * (tabla, botones de CRUD/navegación y buscador).
     */
    private void activarNavegacion() {
        tablaDetalleVenta.setDisable(false);
        btnNuevo.setDisable(false);
        btnEditar.setDisable(false);
        btnPrimero.setDisable(false);
        btnAnterior.setDisable(false);
        btnSiguiente.setDisable(false);
        btnUltimo.setDisable(false);
        txtBuscar.setDisable(false);
    }

    /**
     * Deshabilita los controles de navegación y acciones sobre la tabla
     * (tabla, botones de CRUD/navegación y buscador), típicamente mientras
     * el formulario está activo para creación o edición.
     */
    private void desactivarNavegacion() {
        tablaDetalleVenta.setDisable(true);
        btnNuevo.setDisable(true);
        btnEditar.setDisable(true);
        btnPrimero.setDisable(true);
        btnAnterior.setDisable(true);
        btnSiguiente.setDisable(true);
        btnUltimo.setDisable(true);
        txtBuscar.setDisable(true);
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