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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.ol.dao.EditorialDAO;
import org.ol.dao.impl.EditorialDAOImpl;
import org.ol.exception.DaoException;
import org.ol.exception.ValidacionException;
import org.ol.model.Editorial;
import org.ol.system.Main;

/**
 * Controlador FXML encargado de gestionar el CRUD y la navegación de las
 * editoriales dentro de la aplicación LibraryApp.
 * <p>
 * Permite registrar, editar, buscar y recorrer (primero, anterior, siguiente,
 * último) los registros de {@link Editorial}, enlazando la tabla visual con
 * la capa de acceso a datos ({@link EditorialDAO}).
 *
 * @author Octavio Javier Letona Figueroa
 * @version 1.0.0
 * @see Editorial
 * @see EditorialDAO
 */
public class EditorialController implements Initializable {

    @FXML
    private TextField txtNit;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtDireccion;
    @FXML
    private Label lblMensaje;
    @FXML
    private TableView<Editorial> tablaEditoriales;
    @FXML
    private TableColumn colNit;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colDireccion;
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
    private final EditorialDAO editorialDAO = new EditorialDAOImpl();
    private final ObservableList<Editorial> listaEditoriales = FXCollections.observableArrayList();
    private final FilteredList<Editorial> editorialesFiltradas = new FilteredList<>(listaEditoriales, p -> true);

    /**
     * Inicializa el controlador después de que su elemento raíz haya sido
     * procesado por completo. Carga la tabla, configura la selección de
     * filas, las columnas de la tabla y el buscador.
     *
     * @param location  la ubicación usada para resolver rutas relativas del
     *                  objeto raíz, o {@code null} si no se conoce.
     * @param resources los recursos usados para localizar el objeto raíz,
     *                  o {@code null} si no se localizó.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarTabla();
        tablaEditoriales.setItems(editorialesFiltradas);
        seleccionarFila();
        configurarTabla();
        configurarBusqueda();
    }

    /**
     * Asocia cada columna de {@link #tablaEditoriales} con la propiedad
     * correspondiente del modelo {@link Editorial} mediante
     * {@link PropertyValueFactory}.
     */
    public void configurarTabla() {
        colNit.setCellValueFactory(new PropertyValueFactory<Editorial, String>("nit"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Editorial, String>("nombreEditorial"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<Editorial, String>("telefonoEditorial"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Editorial, String>("direccionEditoria"));
    }

    /**
     * Recupera todas las editoriales desde la base de datos a través de
     * {@link #editorialDAO} y las carga en {@link #listaEditoriales}. Si
     * ocurre un error de acceso a datos, se muestra una alerta al usuario.
     */
    private void cargarTabla() {
        try {
            listaEditoriales.setAll(editorialDAO.listarTodos());
        } catch (DaoException e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Registra un listener sobre {@link #txtBuscar} para filtrar la tabla
     * de editoriales cada vez que cambia el texto de búsqueda.
     */
    private void configurarBusqueda() {
        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> filtrarEditoriales());
    }

    /**
     * Aplica un predicado sobre {@link #editorialesFiltradas} según el
     * texto ingresado en {@link #txtBuscar}, comparando contra el NIT,
     * nombre, teléfono y dirección de cada {@link Editorial}.
     */
    private void filtrarEditoriales() {
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        if (busqueda.isEmpty()) {
            editorialesFiltradas.setPredicate(p -> true);
        } else {
            editorialesFiltradas.setPredicate(editorial ->
                    editorial.getNit().toLowerCase().contains(busqueda)
                    || editorial.getNombreEditorial().toLowerCase().contains(busqueda)
                    || editorial.getTelefonoEditorial().toLowerCase().contains(busqueda)
                    || editorial.getDireccionEditoria().toLowerCase().contains(busqueda));
        }
    }

    /**
     * Registra un listener sobre la selección de {@link #tablaEditoriales}
     * para precargar el formulario (NIT, nombre, teléfono y dirección) con
     * los datos de la fila seleccionada y desactivar el formulario para
     * evitar ediciones accidentales.
     */
    private void seleccionarFila() {
        tablaEditoriales.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        txtNit.setText(newSelection.getNit());
                        txtNombre.setText(newSelection.getNombreEditorial());
                        txtTelefono.setText(newSelection.getTelefonoEditorial());
                        txtDireccion.setText(newSelection.getDireccionEditoria());
                        desactivarFormulario();
                    }
                });
    }

    /**
     * Valida los datos del formulario y guarda la editorial, creando un
     * nuevo registro o actualizando uno existente según {@link #modoEdicion}.
     * Muestra mensajes de éxito, advertencia o error según el resultado de
     * la operación.
     *
     * @throws ValidacionException si algún campo requerido (NIT, nombre,
     *                              teléfono o dirección) está vacío
     *                              (capturada internamente y mostrada como
     *                              advertencia al usuario).
     */
    @FXML
    private void handleGuardar() {
        try {
            ValidacionException.validarNoVacio(txtNit.getText(), "NIT");
            ValidacionException.validarNoVacio(txtNombre.getText(), "nombre");
            ValidacionException.validarNoVacio(txtTelefono.getText(), "teléfono");
            ValidacionException.validarNoVacio(txtDireccion.getText(), "dirección");

            Editorial editorial = new Editorial();
            editorial.setNit(txtNit.getText().trim());
            editorial.setNombreEditorial(txtNombre.getText().trim());
            editorial.setTelefonoEditorial(txtTelefono.getText().trim());
            editorial.setDireccionEditoria(txtDireccion.getText().trim());

            boolean guardado;
            if (modoEdicion) {
                guardado = editorialDAO.actualizar(editorial);
            } else {
                guardado = editorialDAO.crear(editorial);
            }

            if (guardado) {
                lblMensaje.setText(modoEdicion
                        ? "Editorial actualizada exitosamente."
                        : "Editorial registrada exitosamente.");
                cargarTabla();
                limpiarFormulario();
                desactivarFormulario();
                activarNavegacion();
                modoEdicion = false;
            } else {
                mostrarError("No se pudo guardar la editorial.");
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
        lblMensaje.setText("");
    }

    /**
     * Prepara la interfaz para el registro de una nueva editorial:
     * desactiva el modo edición, limpia el formulario, lo activa, bloquea
     * la navegación y coloca el foco en {@link #txtNit}.
     */
    @FXML
    private void handleNuevo() {
        modoEdicion = false;
        limpiarFormulario();
        activarFormulario();
        desactivarNavegacion();
        tablaEditoriales.getSelectionModel().clearSelection();
        lblMensaje.setText("");
        txtNit.requestFocus();
    }

    /**
     * Habilita la edición de la editorial seleccionada en la tabla. Si no
     * hay ninguna fila seleccionada, muestra un mensaje de error y no
     * realiza ninguna acción adicional.
     */
    @FXML
    private void handleEditar() {
        Editorial seleccion = tablaEditoriales.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            mostrarError("Seleccione una editorial de la tabla para editar.");
            return;
        }
        modoEdicion = true;
        activarFormulario();
        desactivarNavegacion();
        lblMensaje.setText("");
    }

    /**
     * Selecciona y desplaza la vista hasta el primer registro de la tabla
     * de editoriales, si existe al menos uno.
     */
    @FXML
    private void handlePrimero() {
        if (!tablaEditoriales.getItems().isEmpty()) {
            tablaEditoriales.getSelectionModel().selectFirst();
            tablaEditoriales.scrollTo(0);
        }
    }

    /**
     * Selecciona el registro anterior al actualmente seleccionado en la
     * tabla de editoriales y desplaza la vista hacia él.
     */
    @FXML
    private void handleAnterior() {
        if (!tablaEditoriales.getItems().isEmpty()) {
            tablaEditoriales.getSelectionModel().selectPrevious();
            if (tablaEditoriales.getSelectionModel().getSelectedIndex() >= 0) {
                tablaEditoriales.scrollTo(tablaEditoriales.getSelectionModel().getSelectedIndex());
            }
        }
    }

    /**
     * Selecciona el registro siguiente al actualmente seleccionado en la
     * tabla de editoriales y desplaza la vista hacia él.
     */
    @FXML
    private void handleSiguiente() {
        if (!tablaEditoriales.getItems().isEmpty()) {
            tablaEditoriales.getSelectionModel().selectNext();
            if (tablaEditoriales.getSelectionModel().getSelectedIndex() >= 0) {
                tablaEditoriales.scrollTo(tablaEditoriales.getSelectionModel().getSelectedIndex());
            }
        }
    }

    /**
     * Selecciona y desplaza la vista hasta el último registro de la tabla
     * de editoriales, si existe al menos uno.
     */
    @FXML
    private void handleUltimo() {
        if (!tablaEditoriales.getItems().isEmpty()) {
            tablaEditoriales.getSelectionModel().selectLast();
            tablaEditoriales.scrollTo(tablaEditoriales.getItems().size() - 1);
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
     * Restablece los campos del formulario (NIT, nombre, teléfono y
     * dirección) a sus valores vacíos por defecto.
     */
    private void limpiarFormulario() {
        txtNit.clear();
        txtNombre.clear();
        txtTelefono.clear();
        txtDireccion.clear();
    }

    /**
     * Habilita los controles del formulario (NIT, nombre, teléfono y
     * dirección) para permitir el ingreso o edición de datos.
     */
    private void activarFormulario() {
        txtNit.setDisable(false);
        txtNombre.setDisable(false);
        txtTelefono.setDisable(false);
        txtDireccion.setDisable(false);
    }

    /**
     * Deshabilita los controles del formulario (NIT, nombre, teléfono y
     * dirección) para evitar modificaciones no deseadas.
     */
    private void desactivarFormulario() {
        txtNit.setDisable(true);
        txtNombre.setDisable(true);
        txtTelefono.setDisable(true);
        txtDireccion.setDisable(true);
    }

    /**
     * Habilita los controles de navegación y acciones sobre la tabla
     * (tabla, botones de CRUD/navegación y buscador).
     */
    private void activarNavegacion() {
        tablaEditoriales.setDisable(false);
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
        tablaEditoriales.setDisable(true);
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