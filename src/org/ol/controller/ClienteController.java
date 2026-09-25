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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.ol.dao.ClienteDAO;
import org.ol.dao.impl.ClienteDAOImpl;
import org.ol.exception.DaoException;
import org.ol.exception.ValidacionException;
import org.ol.model.Cliente;
import org.ol.system.Main;

/**
 * Controlador para la gestión de clientes en la aplicación de biblioteca.
 * 
 * Proporciona funcionalidades completas de CRUD (Crear, Leer, Actualizar, Eliminar)
 * para los registros de clientes. Implementa búsqueda en tiempo real, navegación,
 * validación compleja de datos (CUI, email) y control de estados del formulario 
 * en la interfaz gráfica JavaFX.
 * 
 * Los campos gestionados incluyen:
 * - CUI (Cédula Única de Identidad) - 13 dígitos exactos
 * - Nombre del cliente
 * - Apellido del cliente
 * - Correo electrónico (con validación de formato)
 * 
 * El CUI es el identificador único del cliente y debe cumplir con validaciones
 * estrictas de formato y longitud.
 * 
 * @author Octavio Letona
 * @version 1.0.0
 * @since 2026
 */
public class ClienteController implements Initializable {

    @FXML
    private TextField txtCui;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtCorreo;
    @FXML
    private Label lblMensaje;
    @FXML
    private TableView<Cliente> tablaClientes;
    @FXML
    private TableColumn colCUI;
    @FXML
    private TableColumn colNombreCliente;
    @FXML
    private TableColumn colApellidoCliente;
    @FXML
    private TableColumn colCorreoElectronico;
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
    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    private final FilteredList<Cliente> clientesFiltrados = new FilteredList<>(listaClientes, p -> true);

    /**
     * Inicializa el controlador cargando datos, configurando componentes y estableciendo listeners.
     * Se ejecuta automáticamente cuando se carga el archivo FXML.
     * 
     * Operaciones realizadas:
     * - Carga de todos los clientes desde la base de datos
     * - Configuración de la tabla con los datos filtrados
     * - Configuración de listeners para selección de filas
     * - Configuración de columnas con PropertyValueFactory
     * - Configuración del sistema de búsqueda y filtrado
     * 
     * @param location URL de localización del recurso FXML
     * @param resources ResourceBundle con recursos internacionalizados
     * @see #cargarTabla()
     * @see #configurarTabla()
     * @see #configurarBusqueda()
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarTabla();
        tablaClientes.setItems(clientesFiltrados);
        seleccionarFila();
        configurarTabla();
        configurarBusqueda();
    }

    /**
     * Configura las columnas de la tabla asignando las propiedades del modelo Cliente
     * a cada columna mediante PropertyValueFactory.
     * 
     * Mapeos realizados:
     * - colCUI → cui
     * - colNombreCliente → nombreCliente
     * - colApellidoCliente → apellidoCliente
     * - colCorreoElectronico → correoElectronico
     */
    public void configurarTabla() {
        colCUI.setCellValueFactory(new PropertyValueFactory<Cliente, Long>("cui"));
        colNombreCliente.setCellValueFactory(new PropertyValueFactory<Cliente, String>("nombreCliente"));
        colApellidoCliente.setCellValueFactory(new PropertyValueFactory<Cliente, String>("apellidoCliente"));
        colCorreoElectronico.setCellValueFactory(new PropertyValueFactory<Cliente, String>("correoElectronico"));
    }

    /**
     * Carga todos los registros de clientes desde la base de datos
     * y los adiciona a la lista observable.
     * 
     * @throws DaoException si ocurre un error al acceder a la base de datos
     */
    private void cargarTabla() {
        try {
            listaClientes.setAll(clienteDAO.listarTodos());
        } catch (DaoException e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Configura un listener en el TextField de búsqueda para filtrar
     * los registros en tiempo real según el texto ingresado por el usuario.
     * 
     * @see #filtrarClientes()
     */
    private void configurarBusqueda() {
        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> filtrarClientes());
    }

    /**
     * Filtra la lista de clientes según el texto de búsqueda.
     * La búsqueda es insensible a mayúsculas y busca en los campos:
     * CUI, nombreCliente y apellidoCliente.
     * 
     * Si el campo de búsqueda está vacío, muestra todos los registros.
     */
    private void filtrarClientes() {
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        if (busqueda.isEmpty()) {
            clientesFiltrados.setPredicate(p -> true);
        } else {
            clientesFiltrados.setPredicate(cliente ->
                    String.valueOf(cliente.getCui()).contains(busqueda)
                    || cliente.getNombreCliente().toLowerCase().contains(busqueda)
                    || cliente.getApellidoCliente().toLowerCase().contains(busqueda));
        }
    }

    /**
     * Configura un listener que carga los datos del cliente seleccionado en el formulario
     * cuando se selecciona una fila de la tabla. Desactiva el formulario para modo de lectura.
     * 
     * @see #desactivarFormulario()
     */
    private void seleccionarFila() {
        tablaClientes.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        txtCui.setText(String.valueOf(newSelection.getCui()));
                        txtNombre.setText(newSelection.getNombreCliente());
                        txtApellido.setText(newSelection.getApellidoCliente());
                        txtCorreo.setText(newSelection.getCorreoElectronico());
                        desactivarFormulario();
                    }
                });
    }

    /**
     * Maneja el evento de guardar un nuevo cliente o actualizar uno existente.
     * Valida todos los campos requeridos con reglas de negocio específicas y
     * persiste los datos en la base de datos según el modo (nuevo o edición).
     * 
     * Validaciones realizadas:
     * - CUI no vacío y exactamente 13 dígitos numéricos
     * - Nombre no vacío
     * - Apellido no vacío
     * - Correo electrónico no vacío y formato válido
     * 
     * Operaciones realizadas:
     * - Creación de la instancia Cliente con los datos del formulario
     * - Invocación de crear() o actualizar() según el modo
     * - Actualización de la interfaz (tabla, mensajes, controles)
     * - Limpieza del formulario y retorno al estado de navegación
     * 
     * @throws ValidacionException si algún campo no cumple con las reglas de validación
     * @throws Exception si ocurre un error general al guardar en la base de datos
     * @see #activarFormulario()
     * @see #desactivarFormulario()
     * @see #limpiarFormulario()
     */
    @FXML
    private void handleGuardar() {
        try {
            ValidacionException.validarNoVacio(txtCui.getText(), "CUI");
            ValidacionException.validarNoVacio(txtNombre.getText(), "nombre");
            ValidacionException.validarNoVacio(txtApellido.getText(), "apellido");
            ValidacionException.validarNoVacio(txtCorreo.getText(), "correo electrónico");
            ValidacionException.validarNumero(txtCui.getText(), "CUI");
            ValidacionException.validarLongitudExacta(txtCui.getText().trim(), 13,
                    "El CUI debe tener exactamente 13 dígitos.");
            ValidacionException.validarFormatoEmail(txtCorreo.getText(),
                    "El correo electrónico no tiene un formato válido.");

            Cliente cliente = new Cliente();
            cliente.setCui(Long.parseLong(txtCui.getText().trim()));
            cliente.setNombreCliente(txtNombre.getText().trim());
            cliente.setApellidoCliente(txtApellido.getText().trim());
            cliente.setCorreoElectronico(txtCorreo.getText().trim());

            boolean guardado;
            if (modoEdicion) {
                guardado = clienteDAO.actualizar(cliente);
            } else {
                guardado = clienteDAO.crear(cliente);
            }

            if (guardado) {
                lblMensaje.setText(modoEdicion
                        ? "Cliente actualizado exitosamente."
                        : "Cliente registrado exitosamente.");
                cargarTabla();
                limpiarFormulario();
                desactivarFormulario();
                activarNavegacion();
                modoEdicion = false;
            } else {
                mostrarError("No se pudo guardar el cliente.");
            }
        } catch (ValidacionException e) {
            mostrarAdvertencia(e.getMessage());
            lblMensaje.setText(e.getMessage());
        } catch (Exception e) {
            mostrarError("Error al guardar: " + e.getMessage());
        }
    }

    /**
     * Maneja el evento de cancelar la operación actual (nuevo o edición).
     * Limpia el formulario, desactiva los campos y retorna la interfaz al estado de navegación.
     * 
     * @see #limpiarFormulario()
     * @see #desactivarFormulario()
     * @see #activarNavegacion()
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
     * Maneja el evento para iniciar la creación de un nuevo cliente.
     * Activa el formulario, desactiva la navegación y limpia los campos
     * para que el usuario pueda ingresar nuevos datos.
     * 
     * @see #activarFormulario()
     * @see #desactivarNavegacion()
     * @see #limpiarFormulario()
     */
    @FXML
    private void handleNuevoCliente() {
        modoEdicion = false;
        limpiarFormulario();
        activarFormulario();
        desactivarNavegacion();
        tablaClientes.getSelectionModel().clearSelection();
        lblMensaje.setText("");
        txtCui.requestFocus();
    }

    /**
     * Maneja el evento para editar el cliente seleccionado en la tabla.
     * Valida que exista una selección, activa el modo edición y desactiva la navegación.
     * 
     * @throws IllegalArgumentException si no hay cliente seleccionado en la tabla
     * @see #activarFormulario()
     * @see #desactivarNavegacion()
     */
    @FXML
    private void handleEditar() {
        Cliente seleccion = tablaClientes.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            mostrarError("Seleccione un cliente de la tabla para editar.");
            return;
        }
        modoEdicion = true;
        activarFormulario();
        desactivarNavegacion();
        lblMensaje.setText("");
    }

    /**
     * Navega al primer cliente de la tabla y lo selecciona.
     * Si la tabla está vacía, no realiza ninguna acción.
     */
    @FXML
    private void handlePrimero() {
        if (!tablaClientes.getItems().isEmpty()) {
            tablaClientes.getSelectionModel().selectFirst();
            tablaClientes.scrollTo(0);
        }
    }

    /**
     * Navega al cliente anterior en la tabla y lo selecciona.
     * Si la tabla está vacía o se alcanza el inicio, no realiza ninguna acción.
     */
    @FXML
    private void handleAnterior() {
        if (!tablaClientes.getItems().isEmpty()) {
            tablaClientes.getSelectionModel().selectPrevious();
            if (tablaClientes.getSelectionModel().getSelectedIndex() >= 0) {
                tablaClientes.scrollTo(tablaClientes.getSelectionModel().getSelectedIndex());
            }
        }
    }

    /**
     * Navega al siguiente cliente en la tabla y lo selecciona.
     * Si la tabla está vacía o se alcanza el final, no realiza ninguna acción.
     */
    @FXML
    private void handleSiguiente() {
        if (!tablaClientes.getItems().isEmpty()) {
            tablaClientes.getSelectionModel().selectNext();
            if (tablaClientes.getSelectionModel().getSelectedIndex() >= 0) {
                tablaClientes.scrollTo(tablaClientes.getSelectionModel().getSelectedIndex());
            }
        }
    }

    /**
     * Navega al último cliente de la tabla y lo selecciona.
     * Si la tabla está vacía, no realiza ninguna acción.
     */
    @FXML
    private void handleUltimo() {
        if (!tablaClientes.getItems().isEmpty()) {
            tablaClientes.getSelectionModel().selectLast();
            tablaClientes.scrollTo(tablaClientes.getItems().size() - 1);
        }
    }

    /**
     * Maneja el evento para retornar al menú principal o dashboard según el rol del usuario.
     * Cambia la escena a la ruta correspondiente obtenida de Main.
     * 
     * @throws Exception si ocurre un error al cambiar de escena
     * @see Main#rutaDashboardSegunRol()
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
     * Limpia todos los campos del formulario estableciendo los TextFields a vacíos.
     */
    private void limpiarFormulario() {
        txtCui.clear();
        txtNombre.clear();
        txtApellido.clear();
        txtCorreo.clear();
    }

    /**
     * Activa los campos del formulario permitiendo que el usuario ingrese datos.
     * Habilita todos los TextFields del formulario.
     */
    private void activarFormulario() {
        txtCui.setDisable(false);
        txtNombre.setDisable(false);
        txtApellido.setDisable(false);
        txtCorreo.setDisable(false);
    }

    /**
     * Desactiva los campos del formulario impidiendo que el usuario modifique los datos.
     * Deshabilita todos los TextFields del formulario.
     */
    private void desactivarFormulario() {
        txtCui.setDisable(true);
        txtNombre.setDisable(true);
        txtApellido.setDisable(true);
        txtCorreo.setDisable(true);
    }

    /**
     * Activa todos los controles de navegación y búsqueda de la tabla.
     * Habilita tabla, botones de navegación y campo de búsqueda.
     */
    private void activarNavegacion() {
        tablaClientes.setDisable(false);
        btnNuevo.setDisable(false);
        btnEditar.setDisable(false);
        btnPrimero.setDisable(false);
        btnAnterior.setDisable(false);
        btnSiguiente.setDisable(false);
        btnUltimo.setDisable(false);
        txtBuscar.setDisable(false);
    }

    /**
     * Desactiva todos los controles de navegación y búsqueda de la tabla.
     * Deshabilita tabla, botones de navegación y campo de búsqueda durante
     * la edición o creación de un nuevo cliente.
     */
    private void desactivarNavegacion() {
        tablaClientes.setDisable(true);
        btnNuevo.setDisable(true);
        btnEditar.setDisable(true);
        btnPrimero.setDisable(true);
        btnAnterior.setDisable(true);
        btnSiguiente.setDisable(true);
        btnUltimo.setDisable(true);
        txtBuscar.setDisable(true);
    }

    /**
     * Muestra un diálogo de error al usuario con el mensaje especificado.
     * El diálogo es modal y bloquea la interacción hasta que sea cerrado.
     * 
     * @param mensaje el texto del mensaje de error a mostrar
     */
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra un diálogo de advertencia al usuario con el mensaje especificado.
     * El diálogo es modal y bloquea la interacción hasta que sea cerrado.
     * 
     * @param mensaje el texto del mensaje de advertencia a mostrar
     */
    private void mostrarAdvertencia(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}