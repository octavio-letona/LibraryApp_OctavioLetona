package org.ol.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import org.ol.dao.ClienteDAO;
import org.ol.dao.UsuarioDAO;
import org.ol.dao.VentaDAO;
import org.ol.dao.impl.ClienteDAOImpl;
import org.ol.dao.impl.UsuarioDAOImpl;
import org.ol.dao.impl.VentaDAOImpl;
import org.ol.exception.DaoException;
import org.ol.exception.ValidacionException;
import org.ol.manager.SesionContext;
import org.ol.model.Cliente;
import org.ol.model.Usuario;
import org.ol.model.Venta;
import org.ol.system.Main;

/**
 * Controlador FXML encargado de gestionar el CRUD, la navegación y la
 * consulta de facturas de las ventas dentro de la aplicación LibraryApp.
 * <p>
 * Permite registrar, editar, buscar y recorrer (primero, anterior, siguiente,
 * último) los registros de {@link Venta}, enlazando la tabla visual con la
 * capa de acceso a datos ({@link VentaDAO}) y con los combos de
 * {@link Cliente} ({@link ClienteDAO}) y {@link Usuario} ({@link UsuarioDAO})
 * asociados a cada venta. También permite abrir la factura de una venta
 * seleccionada a través de {@link FacturaController}.
 *
 * @author Octavio Javier Letona Figueroa
 * @version 1.0.0
 * @see Venta
 * @see VentaDAO
 * @see FacturaController
 */
public class ListaVentasController implements Initializable {

    @FXML
    private TextField txtTotal;
    @FXML
    private DatePicker dpFecha;
    @FXML
    private ComboBox<Usuario> cmbUsuario;
    @FXML
    private ComboBox<Cliente> cmbCliente;
    @FXML
    private Label lblMensaje;
    @FXML
    private TableView<Venta> tablaVentas;
    @FXML
    private TableColumn colNoVenta;
    @FXML
    private TableColumn colFechaVenta;
    @FXML
    private TableColumn colTotalVenta;
    @FXML
    private TableColumn colCuiCliente;
    @FXML
    private TableColumn colUsuario;
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
    private Venta enEdicion;
    private final VentaDAO ventaDAO = new VentaDAOImpl();
    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    private final ObservableList<Venta> listaVentas = FXCollections.observableArrayList();
    private final FilteredList<Venta> ventasFiltradas = new FilteredList<>(listaVentas, p -> true);

    /**
     * Inicializa el controlador después de que su elemento raíz haya sido
     * procesado por completo. Carga la tabla, los clientes y usuarios,
     * configura la selección de filas, las columnas de la tabla y el
     * buscador.
     *
     * @param location  la ubicación usada para resolver rutas relativas del
     *                  objeto raíz, o {@code null} si no se conoce.
     * @param resources los recursos usados para localizar el objeto raíz,
     *                  o {@code null} si no se localizó.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarTabla();
        cargarClientes();
        cargarUsuarios();
        tablaVentas.setItems(ventasFiltradas);
        seleccionarFila();
        configurarTabla();
        configurarBusqueda();
    }

    /**
     * Asocia cada columna de {@link #tablaVentas} con la propiedad
     * correspondiente del modelo {@link Venta} mediante
     * {@link PropertyValueFactory}.
     */
    public void configurarTabla() {
        colNoVenta.setCellValueFactory(new PropertyValueFactory<Venta, Integer>("noVenta"));
        colFechaVenta.setCellValueFactory(new PropertyValueFactory<Venta, String>("fechaVenta"));
        colTotalVenta.setCellValueFactory(new PropertyValueFactory<Venta, Double>("totalVenta"));
        colCuiCliente.setCellValueFactory(new PropertyValueFactory<Venta, Long>("cuiCliente"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<Venta, Integer>("idUsuario"));
    }

    /**
     * Recupera todas las ventas desde la base de datos a través de
     * {@link #ventaDAO} y las carga en {@link #listaVentas}. Si ocurre un
     * error de acceso a datos, se muestra una alerta al usuario.
     */
    private void cargarTabla() {
        try {
            listaVentas.setAll(ventaDAO.listarTodos());
        } catch (DaoException e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Carga el combo {@link #cmbCliente} con la información obtenida de
     * {@link #clienteDAO}. Si ocurre un error de acceso a datos, se muestra
     * una alerta al usuario.
     */
    private void cargarClientes() {
        try {
            cmbCliente.setItems(FXCollections.observableArrayList(clienteDAO.listarTodos()));
        } catch (DaoException e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Define el {@link StringConverter} de {@link #cmbUsuario} (mostrando
     * id y username) y lo carga con la información obtenida de
     * {@link #usuarioDAO}. Si ocurre un error de acceso a datos, se muestra
     * una alerta al usuario.
     */
    private void cargarUsuarios() {
        cmbUsuario.setConverter(new StringConverter<Usuario>() {
            @Override
            public String toString(Usuario usuario) {
                return usuario == null ? "" : usuario.getId() + " - " + usuario.getUsername();
            }

            @Override
            public Usuario fromString(String string) {
                return null;
            }
        });
        try {
            cmbUsuario.setItems(FXCollections.observableArrayList(usuarioDAO.listarTodosUsuarios()));
        } catch (DaoException e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Registra un listener sobre {@link #txtBuscar} para filtrar la tabla
     * de ventas cada vez que cambia el texto de búsqueda.
     */
    private void configurarBusqueda() {
        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> filtrarVentas());
    }

    /**
     * Aplica un predicado sobre {@link #ventasFiltradas} según el texto
     * ingresado en {@link #txtBuscar}, comparando contra el número de
     * venta, fecha, total, CUI del cliente e id del usuario de cada
     * {@link Venta}.
     */
    private void filtrarVentas() {
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        if (busqueda.isEmpty()) {
            ventasFiltradas.setPredicate(p -> true);
        } else {
            ventasFiltradas.setPredicate(venta ->
                    String.valueOf(venta.getNoVenta()).contains(busqueda)
                    || venta.getFechaVenta().toLowerCase().contains(busqueda)
                    || String.valueOf(venta.getTotalVenta()).contains(busqueda)
                    || String.valueOf(venta.getCuiCliente()).contains(busqueda)
                    || String.valueOf(venta.getIdUsuario()).contains(busqueda));
        }
    }

    /**
     * Registra un listener sobre la selección de {@link #tablaVentas} para
     * precargar el formulario (total, fecha, cliente y usuario) con los
     * datos de la fila seleccionada y desactivar el formulario para evitar
     * ediciones accidentales. La fecha se interpreta a partir de los
     * primeros 10 caracteres del valor almacenado; si no puede convertirse,
     * el {@link DatePicker} se deja vacío.
     */
    private void seleccionarFila() {
        tablaVentas.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        txtTotal.setText(String.valueOf(newSelection.getTotalVenta()));
                        cmbCliente.setValue(null);
                        for (Cliente cliente : cmbCliente.getItems()) {
                            if (cliente.getCui() == newSelection.getCuiCliente()) {
                                cmbCliente.setValue(cliente);
                                break;
                            }
                        }
                        desactivarFormulario();
                        String fecha = newSelection.getFechaVenta();
                        if (fecha != null && !fecha.isEmpty()) {
                            try {
                                dpFecha.setValue(LocalDate.parse(fecha.substring(0, 10)));
                            } catch (Exception e) {
                                dpFecha.setValue(null);
                            }
                        } else {
                            dpFecha.setValue(null);
                        }
                        cmbUsuario.setValue(null);
                        for (Usuario usuario : cmbUsuario.getItems()) {
                            if (usuario.getId() == newSelection.getIdUsuario()) {
                                cmbUsuario.setValue(usuario);
                                break;
                            }
                        }
                    }
                });
    }

    /**
     * Valida los datos del formulario y guarda la venta, creando un nuevo
     * registro o actualizando uno existente según {@link #modoEdicion}. Si
     * no se selecciona un usuario, se asigna el usuario en sesión mediante
     * {@link SesionContext}. Muestra mensajes de éxito, advertencia o error
     * según el resultado de la operación.
     *
     * @throws ValidacionException si el campo total está vacío o no es
     *                              decimal, o si no se seleccionó un
     *                              cliente o un usuario (capturada
     *                              internamente y mostrada como advertencia
     *                              al usuario).
     */
    @FXML
    private void handleGuardar() {
        try {
            ValidacionException.validarNoVacio(txtTotal.getText(), "total");
            ValidacionException.validarDecimal(txtTotal.getText(), "total");
            ValidacionException.validarNoNulo(cmbCliente.getValue(),
                    "Seleccione un cliente.");
            ValidacionException.validarNoNulo(cmbUsuario.getValue(), "Seleccione el usuario que atendió.");

            Venta venta = new Venta(
                    modoEdicion ? enEdicion.getNoVenta() : 0,
                    dpFecha.getValue() != null ? dpFecha.getValue().toString() : null,
                    Double.parseDouble(txtTotal.getText().trim()),
                    cmbCliente.getValue().getCui(),
                    cmbUsuario.getValue() != null
                            ? cmbUsuario.getValue().getId()
                            : SesionContext.getInstancia().getUsuarioActual().getId());

            boolean guardado;
            if (modoEdicion) {
                guardado = ventaDAO.actualizar(venta);
            } else {
                guardado = ventaDAO.crear(venta);
            }

            if (guardado) {
                lblMensaje.setText(modoEdicion
                        ? "Venta actualizada exitosamente."
                        : "Venta registrada exitosamente.");
                cargarTabla();
                limpiarFormulario();
                desactivarFormulario();
                activarNavegacion();
                modoEdicion = false;
            } else {
                mostrarError("No se pudo guardar la venta.");
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
     * Prepara la interfaz para el registro de una nueva venta: desactiva
     * el modo edición, limpia el formulario, lo activa, bloquea la
     * navegación, precarga la fecha actual en {@link #dpFecha} y coloca el
     * foco en {@link #txtTotal}.
     */
    @FXML
    private void handleNuevo() {
        modoEdicion = false;
        enEdicion = null;
        limpiarFormulario();
        activarFormulario();
        desactivarNavegacion();
        tablaVentas.getSelectionModel().clearSelection();
        lblMensaje.setText("");
        dpFecha.setValue(LocalDate.now());
        txtTotal.requestFocus();
    }

    /**
     * Habilita la edición de la venta seleccionada en la tabla. Si no hay
     * ninguna fila seleccionada, muestra un mensaje de error y no realiza
     * ninguna acción adicional.
     */
    @FXML
    private void handleEditar() {
        Venta seleccion = tablaVentas.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            mostrarError("Seleccione una venta de la tabla para editar.");
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
     * de ventas, si existe al menos uno.
     */
    @FXML
    private void handlePrimero() {
        if (!tablaVentas.getItems().isEmpty()) {
            tablaVentas.getSelectionModel().selectFirst();
            tablaVentas.scrollTo(0);
        }
    }

    /**
     * Selecciona el registro anterior al actualmente seleccionado en la
     * tabla de ventas y desplaza la vista hacia él.
     */
    @FXML
    private void handleAnterior() {
        if (!tablaVentas.getItems().isEmpty()) {
            tablaVentas.getSelectionModel().selectPrevious();
            if (tablaVentas.getSelectionModel().getSelectedIndex() >= 0) {
                tablaVentas.scrollTo(tablaVentas.getSelectionModel().getSelectedIndex());
            }
        }
    }

    /**
     * Selecciona el registro siguiente al actualmente seleccionado en la
     * tabla de ventas y desplaza la vista hacia él.
     */
    @FXML
    private void handleSiguiente() {
        if (!tablaVentas.getItems().isEmpty()) {
            tablaVentas.getSelectionModel().selectNext();
            if (tablaVentas.getSelectionModel().getSelectedIndex() >= 0) {
                tablaVentas.scrollTo(tablaVentas.getSelectionModel().getSelectedIndex());
            }
        }
    }

    /**
     * Selecciona y desplaza la vista hasta el último registro de la tabla
     * de ventas, si existe al menos uno.
     */
    @FXML
    private void handleUltimo() {
        if (!tablaVentas.getItems().isEmpty()) {
            tablaVentas.getSelectionModel().selectLast();
            tablaVentas.scrollTo(tablaVentas.getItems().size() - 1);
        }
    }

    /**
     * Abre la vista de factura correspondiente a la venta seleccionada en
     * la tabla, comunicando el número de venta a
     * {@link FacturaController#setNoVentaSeleccionada(int)} antes de
     * cambiar de escena. Si no hay ninguna fila seleccionada, muestra un
     * mensaje de error y no realiza ninguna acción adicional.
     */
    @FXML
    private void handleVerFactura() {
        Venta seleccion = tablaVentas.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            mostrarError("Seleccione una venta de la tabla para ver su factura.");
            return;
        }
        FacturaController.setNoVentaSeleccionada(seleccion.getNoVenta());
        try {
            Main.cambiarEscena("/org/ac/view/fxml/FacturaView.fxml");
        } catch (Exception e) {
            mostrarError("Error al abrir la factura: " + e.getMessage());
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
     * Restablece los campos del formulario (total, fecha, usuario y
     * cliente) a sus valores vacíos o nulos por defecto.
     */
    private void limpiarFormulario() {
        txtTotal.clear();
        dpFecha.setValue(null);
        cmbUsuario.setValue(null);
        cmbCliente.setValue(null);
    }

    /**
     * Habilita los controles del formulario (total, fecha, cliente y
     * usuario) para permitir el ingreso o edición de datos.
     */
    private void activarFormulario() {
        txtTotal.setDisable(false);
        dpFecha.setDisable(false);
        cmbCliente.setDisable(false);
        cmbUsuario.setDisable(false);
    }

    /**
     * Deshabilita los controles del formulario (total, fecha, usuario y
     * cliente) para evitar modificaciones no deseadas.
     */
    private void desactivarFormulario() {
        txtTotal.setDisable(true);
        dpFecha.setDisable(true);
        cmbUsuario.setDisable(true);
        cmbCliente.setDisable(true);
    }

    /**
     * Habilita los controles de navegación y acciones sobre la tabla
     * (tabla, botones de CRUD/navegación y buscador).
     */
    private void activarNavegacion() {
        tablaVentas.setDisable(false);
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
        tablaVentas.setDisable(true);
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