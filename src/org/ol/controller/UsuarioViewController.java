/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.ol.controller;

import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import org.ol.dao.UsuarioDAO;
import org.ol.dao.impl.UsuarioDAOImpl;
import org.ol.exception.DaoException;
import org.ol.exception.ValidacionException;
import org.ol.manager.SesionContext;
import org.ol.model.Usuario;
import org.ol.system.Main;
import org.ol.util.SecurityUtil;

/**
 * Controlador para la gestión completa de usuarios en la aplicación.
 * 
 * Proporciona funcionalidades avanzadas de CRUD (Crear, Leer, Actualizar, Eliminar)
 * para los registros de usuarios del sistema. Implementa:
 * - Creación de nuevos usuarios con validación de datos
 * - Edición de información de usuarios existentes
 * - Cambio de contraseñas con hashing seguro SHA256
 * - Desactivación temporal de usuarios
 * - Eliminación permanente de usuarios de la base de datos
 * - Búsqueda y filtrado en tiempo real
 * - Navegación avanzada por tabla
 * - Protección contra auto-eliminación o auto-desactivación
 * 
 * Los campos gestionados incluyen:
 * - Username (nombre de usuario único)
 * - Email (correo electrónico con validación de formato)
 * - Nombre y apellido
 * - Rol (admin, empleado, cajero)
 * - Estado activo/inactivo
 * - Contraseña (con hashing SHA256 para seguridad)
 * - Fecha de creación
 * 
 * @author Octavio Letona
 * @version 1.0.0
 * @since 2026
 */
public class UsuarioViewController implements Initializable {

    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private ComboBox<String> cmbRol;
    @FXML
    private CheckBox chkActivo;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblMensaje;
    @FXML
    private TableView<Usuario> tablaUsuarios;
    @FXML
    private TableColumn colId;
    @FXML
    private TableColumn colUsername;
    @FXML
    private TableColumn colEmail;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colApellido;
    @FXML
    private TableColumn colRol;
    @FXML
    private TableColumn colActivo;
    @FXML
    private TableColumn colFecha;
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
    private Button btnCambiarPassword;
    @FXML
    private Button btnDesactivar;
    @FXML
    private Button btnEliminar;
    @FXML
    private TextField txtBuscar;

    private boolean modoEdicion = false;
    private Usuario enEdicion;
    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    private final ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();
    private final FilteredList<Usuario> usuariosFiltrados = new FilteredList<>(listaUsuarios, p -> true);

    /**
     * Inicializa el controlador cargando datos, configurando componentes y estableciendo listeners.
     * Se ejecuta automáticamente cuando se carga el archivo FXML.
     * 
     * Operaciones realizadas:
     * - Carga de roles disponibles en el ComboBox (admin, empleado, cajero)
     * - Carga de todos los usuarios desde la base de datos
     * - Configuración de la tabla con los datos filtrados
     * - Configuración de listeners para selección de filas
     * - Configuración de columnas con PropertyValueFactory
     * - Configuración del sistema de búsqueda y filtrado
     * - Desactivación inicial del formulario (modo lectura)
     * 
     * @param location URL de localización del recurso FXML
     * @param resources ResourceBundle con recursos internacionalizados
     * @see #cargarTabla()
     * @see #configurarTabla()
     * @see #configurarBusqueda()
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbRol.setItems(FXCollections.observableArrayList("admin", "empleado", "cajero"));
        cargarTabla();
        tablaUsuarios.setItems(usuariosFiltrados);
        seleccionarFila();
        configurarTabla();
        configurarBusqueda();
        desactivarFormulario();
    }

    /**
     * Configura las columnas de la tabla asignando las propiedades del modelo Usuario
     * a cada columna mediante PropertyValueFactory.
     * 
     * Mapeos realizados:
     * - colId → id
     * - colUsername → username
     * - colEmail → email
     * - colNombre → firstName
     * - colApellido → lastName
     * - colRol → rol
     * - colActivo → activo
     * - colFecha → fechaCreacion
     */
    public void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<Usuario, Integer>("id"));
        colUsername.setCellValueFactory(new PropertyValueFactory<Usuario, String>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Usuario, String>("email"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Usuario, String>("firstName"));
        colApellido.setCellValueFactory(new PropertyValueFactory<Usuario, String>("lastName"));
        colRol.setCellValueFactory(new PropertyValueFactory<Usuario, String>("rol"));
        colActivo.setCellValueFactory(new PropertyValueFactory<Usuario, Boolean>("activo"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Usuario, Timestamp>("fechaCreacion"));
    }

    /**
     * Carga todos los registros de usuarios desde la base de datos
     * y los adiciona a la lista observable.
     * 
     * @throws DaoException si ocurre un error al acceder a la base de datos
     */
    private void cargarTabla() {
        try {
            listaUsuarios.setAll(usuarioDAO.listarTodosUsuarios());
        } catch (DaoException e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Configura un listener en el TextField de búsqueda para filtrar
     * los registros en tiempo real según el texto ingresado por el usuario.
     * 
     * @see #filtrarUsuarios()
     */
    private void configurarBusqueda() {
        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> filtrarUsuarios());
    }

    /**
     * Filtra la lista de usuarios según el texto de búsqueda.
     * La búsqueda es insensible a mayúsculas y busca en los campos:
     * id, username, email y rol.
     * 
     * Si el campo de búsqueda está vacío, muestra todos los registros.
     */
    private void filtrarUsuarios() {
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        if (busqueda.isEmpty()) {
            usuariosFiltrados.setPredicate(p -> true);
        } else {
            usuariosFiltrados.setPredicate(usuario ->
                    String.valueOf(usuario.getId()).contains(busqueda)
                    || usuario.getUsername().toLowerCase().contains(busqueda)
                    || (usuario.getEmail() != null && usuario.getEmail().toLowerCase().contains(busqueda))
                    || usuario.getRol().toLowerCase().contains(busqueda));
        }
    }

    /**
     * Configura un listener que carga los datos del usuario seleccionado en el formulario
     * cuando se selecciona una fila de la tabla. Desactiva el formulario para modo de lectura.
     * 
     * @see #mostrarEnFormulario(Usuario)
     * @see #desactivarFormulario()
     */
    private void seleccionarFila() {
        tablaUsuarios.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        mostrarEnFormulario(newSelection);
                        desactivarFormulario();
                    }
                });
    }

    /**
     * Carga los datos de un usuario específico en los campos del formulario.
     * Llena todos los TextFields, ComboBox y CheckBox con la información del usuario.
     * El campo de contraseña siempre se limpia por seguridad.
     * 
     * @param usuario el Usuario cuyos datos se desean mostrar en el formulario
     */
    private void mostrarEnFormulario(Usuario usuario) {
        txtUsername.setText(usuario.getUsername());
        txtEmail.setText(usuario.getEmail());
        txtNombre.setText(usuario.getFirstName());
        txtApellido.setText(usuario.getLastName());
        cmbRol.setValue(usuario.getRol());
        chkActivo.setSelected(usuario.isActivo());
        txtPassword.clear();
    }

    /**
     * Maneja el evento de guardar un nuevo usuario o actualizar uno existente.
     * Valida todos los campos requeridos, aplica reglas de negocio, hashea la contraseña
     * y persiste los datos en la base de datos según el modo (nuevo o edición).
     * 
     * Validaciones realizadas:
     * - Username no vacío
     * - Email no vacío y formato válido
     * - Rol seleccionado
     * - Para nuevos usuarios: contraseña no vacía y mínimo 6 caracteres
     * - Para edición: no requiere contraseña (opcional)
     * 
     * Operaciones realizadas:
     * - Creación de la instancia Usuario con los datos del formulario
     * - Invocación de crear() o actualizar() según el modo
     * - Hashing SHA256 de la contraseña para seguridad
     * - Actualización de la interfaz (tabla, mensajes, controles)
     * - Limpieza del formulario y retorno al estado de navegación
     * 
     * @throws ValidacionException si algún campo no cumple con las reglas de validación
     * @throws Exception si ocurre un error general al guardar en la base de datos
     * @see SecurityUtil#hashSHA256(String)
     * @see #activarFormulario()
     * @see #desactivarFormulario()
     * @see #limpiarFormulario()
     */
    @FXML
    private void handleGuardar() {
        try {
            ValidacionException.validarNoVacio(txtUsername.getText(), "username");
            ValidacionException.validarNoVacio(txtEmail.getText(), "correo electrónico");
            ValidacionException.validarFormatoEmail(txtEmail.getText(), "El correo electrónico no es válido.");
            ValidacionException.validarNoNulo(cmbRol.getValue(), "Debe seleccionar un rol.");

            Usuario usuario = new Usuario();
            usuario.setId(modoEdicion ? enEdicion.getId() : 0);
            usuario.setUsername(txtUsername.getText().trim());
            usuario.setEmail(txtEmail.getText().trim());
            usuario.setFirstName(txtNombre.getText().trim());
            usuario.setLastName(txtApellido.getText().trim());
            usuario.setRol(cmbRol.getValue());
            usuario.setActivo(chkActivo.isSelected());

            boolean guardado;
            if (modoEdicion) {
                guardado = usuarioDAO.actualizarUsuario(usuario);
            } else {
                ValidacionException.validarNoVacio(txtPassword.getText(), "contraseña");
                ValidacionException.validarLongitudMinima(txtPassword.getText(), 6,
                        "La contraseña debe tener al menos 6 caracteres.");
                usuario.setPasswordHash(SecurityUtil.hashSHA256(txtPassword.getText()));
                guardado = usuarioDAO.crearUsuario(usuario);
            }

            if (guardado) {
                lblMensaje.setText(modoEdicion
                        ? "Usuario actualizado exitosamente."
                        : "Usuario registrado exitosamente.");
                cargarTabla();
                limpiarFormulario();
                desactivarFormulario();
                activarNavegacion();
                modoEdicion = false;
                enEdicion = null;
            } else {
                mostrarError("No se pudo guardar el usuario.");
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
        enEdicion = null;
        lblMensaje.setText("");
    }

    /**
     * Maneja el evento para iniciar la creación de un nuevo usuario.
     * Activa el formulario, desactiva la navegación y establece valores por defecto
     * (activo=true, rol=empleado) para que el usuario pueda ingresar nuevos datos.
     * 
     * @see #activarFormulario()
     * @see #desactivarNavegacion()
     * @see #limpiarFormulario()
     */
    @FXML
    private void handleNuevo() {
        modoEdicion = false;
        enEdicion = null;
        limpiarFormulario();
        chkActivo.setSelected(true);
        cmbRol.setValue("empleado");
        activarFormulario();
        desactivarNavegacion();
        tablaUsuarios.getSelectionModel().clearSelection();
        lblMensaje.setText("");
        txtUsername.requestFocus();
    }

    /**
     * Maneja el evento para editar el usuario seleccionado en la tabla.
     * Valida que exista una selección, carga los datos en el formulario,
     * activa el modo edición y desactiva la navegación.
     * 
     * La contraseña se desactiva en modo edición ya que se cambia mediante
     * un proceso separado (handleCambiarPassword).
     * 
     * @throws IllegalArgumentException si no hay usuario seleccionado en la tabla
     * @see #activarFormulario()
     * @see #desactivarNavegacion()
     * @see #mostrarEnFormulario(Usuario)
     */
    @FXML
    private void handleEditar() {
        Usuario seleccion = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            mostrarError("Seleccione un usuario de la tabla para editar.");
            return;
        }
        modoEdicion = true;
        enEdicion = seleccion;
        mostrarEnFormulario(seleccion);
        activarFormulario();
        desactivarNavegacion();
        lblMensaje.setText("");
    }

    /**
     * Maneja el evento para cambiar la contraseña de un usuario seleccionado.
     * Abre un diálogo de entrada donde el administrador puede ingresar una nueva contraseña,
     * la valida (mínimo 6 caracteres), la hashea con SHA256 y la actualiza en la base de datos.
     * 
     * Validaciones realizadas:
     * - Usuario seleccionado en la tabla
     * - Contraseña no vacía
     * - Contraseña con mínimo 6 caracteres
     * 
     * @throws ValidacionException si la contraseña no cumple con las reglas
     * @throws DaoException si ocurre un error al acceder a la base de datos
     * @see SecurityUtil#hashSHA256(String)
     * @see UsuarioDAO#cambiarPassword(int, String)
     */
    @FXML
    private void handleCambiarPassword() {
        Usuario seleccion = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            mostrarError("Seleccione un usuario para cambiar la contraseña.");
            return;
        }
        TextInputDialog dialogo = new TextInputDialog();
        dialogo.setTitle("Cambiar Contraseña");
        dialogo.setHeaderText("Nueva contraseña para: " + seleccion.getUsername());
        dialogo.setContentText("Contraseña:");
        dialogo.showAndWait().ifPresent(password -> {
            try {
                ValidacionException.validarNoVacio(password, "contraseña");
                ValidacionException.validarLongitudMinima(password, 6,
                        "La contraseña debe tener al menos 6 caracteres.");
                String hash = SecurityUtil.hashSHA256(password);
                if (usuarioDAO.cambiarPassword(seleccion.getId(), hash)) {
                    lblMensaje.setText("Contraseña actualizada exitosamente.");
                } else {
                    mostrarError("No se pudo cambiar la contraseña.");
                }
            } catch (ValidacionException e) {
                mostrarAdvertencia(e.getMessage());
            } catch (DaoException e) {
                mostrarError(e.getMessage());
            }
        });
    }

    /**
     * Maneja el evento para desactivar un usuario seleccionado.
     * Desactiva el usuario sin eliminarlo de la base de datos, permitiendo reactivarlo después.
     * 
     * Protecciones implementadas:
     * - Valida que exista usuario seleccionado
     * - Previene desactivar el usuario actual en sesión
     * - Solicita confirmación del administrador
     * 
     * @throws DaoException si ocurre un error al acceder a la base de datos
     * @see #esUsuarioActual(Usuario)
     * @see #confirmar(String, String)
     * @see UsuarioDAO#desactivarUsuario(int)
     */
    @FXML
    private void handleDesactivar() {
        Usuario seleccion = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            mostrarError("Seleccione un usuario para desactivar.");
            return;
        }
        if (esUsuarioActual(seleccion)) {
            mostrarError("No puede desactivar su propio usuario.");
            return;
        }
        if (!confirmar("Desactivar usuario", "¿Desea desactivar al usuario " + seleccion.getUsername() + "?")) {
            return;
        }
        try {
            if (usuarioDAO.desactivarUsuario(seleccion.getId())) {
                lblMensaje.setText("Usuario desactivado exitosamente.");
                cargarTabla();
            } else {
                mostrarError("No se pudo desactivar el usuario.");
            }
        } catch (DaoException e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Maneja el evento para eliminar permanentemente un usuario seleccionado.
     * Elimina el usuario definitivamente de la base de datos.
     * 
     * Protecciones implementadas:
     * - Valida que exista usuario seleccionado
     * - Previene eliminar el usuario actual en sesión
     * - Solicita confirmación explícita del administrador
     * 
     * @throws DaoException si ocurre un error al acceder a la base de datos
     * @see #esUsuarioActual(Usuario)
     * @see #confirmar(String, String)
     * @see UsuarioDAO#eliminarUsuario(int)
     */
    @FXML
    private void handleEliminar() {
        Usuario seleccion = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            mostrarError("Seleccione un usuario para eliminar.");
            return;
        }
        if (esUsuarioActual(seleccion)) {
            mostrarError("No puede eliminar su propio usuario.");
            return;
        }
        if (!confirmar("Eliminar usuario",
                "¿Desea eliminar definitivamente al usuario " + seleccion.getUsername() + "?")) {
            return;
        }
        try {
            if (usuarioDAO.eliminarUsuario(seleccion.getId())) {
                lblMensaje.setText("Usuario eliminado exitosamente.");
                cargarTabla();
            } else {
                mostrarError("No se pudo eliminar el usuario.");
            }
        } catch (DaoException e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Verifica si el usuario proporcionado es el usuario actual en sesión.
     * Se utiliza para prevenir que un usuario se auto-elimine o auto-desactive.
     * 
     * @param usuario el Usuario a verificar
     * @return true si el usuario es el usuario actual en sesión, false en caso contrario
     * @see SesionContext#getInstancia()
     */
    private boolean esUsuarioActual(Usuario usuario) {
        Usuario actual = SesionContext.getInstancia().getUsuarioActual();
        return actual != null && actual.getId() == usuario.getId();
    }

    /**
     * Navega al primer usuario de la tabla y lo selecciona.
     * Si la tabla está vacía, no realiza ninguna acción.
     */
    @FXML
    private void handlePrimero() {
        if (!tablaUsuarios.getItems().isEmpty()) {
            tablaUsuarios.getSelectionModel().selectFirst();
            tablaUsuarios.scrollTo(0);
        }
    }

    /**
     * Navega al usuario anterior en la tabla y lo selecciona.
     * Si la tabla está vacía o se alcanza el inicio, no realiza ninguna acción.
     */
    @FXML
    private void handleAnterior() {
        if (!tablaUsuarios.getItems().isEmpty()) {
            tablaUsuarios.getSelectionModel().selectPrevious();
            if (tablaUsuarios.getSelectionModel().getSelectedIndex() >= 0) {
                tablaUsuarios.scrollTo(tablaUsuarios.getSelectionModel().getSelectedIndex());
            }
        }
    }

    /**
     * Navega al siguiente usuario en la tabla y lo selecciona.
     * Si la tabla está vacía o se alcanza el final, no realiza ninguna acción.
     */
    @FXML
    private void handleSiguiente() {
        if (!tablaUsuarios.getItems().isEmpty()) {
            tablaUsuarios.getSelectionModel().selectNext();
            if (tablaUsuarios.getSelectionModel().getSelectedIndex() >= 0) {
                tablaUsuarios.scrollTo(tablaUsuarios.getSelectionModel().getSelectedIndex());
            }
        }
    }

    /**
     * Navega al último usuario de la tabla y lo selecciona.
     * Si la tabla está vacía, no realiza ninguna acción.
     */
    @FXML
    private void handleUltimo() {
        if (!tablaUsuarios.getItems().isEmpty()) {
            tablaUsuarios.getSelectionModel().selectLast();
            tablaUsuarios.scrollTo(tablaUsuarios.getItems().size() - 1);
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
     * Limpia todos los campos del formulario estableciendo los TextFields a vacíos,
     * los ComboBox a null y el CheckBox a desmarcar.
     */
    private void limpiarFormulario() {
        txtUsername.clear();
        txtEmail.clear();
        txtNombre.clear();
        txtApellido.clear();
        cmbRol.setValue(null);
        chkActivo.setSelected(false);
        txtPassword.clear();
    }

    /**
     * Activa los campos del formulario permitiendo que el usuario ingrese datos.
     * Habilita todos los TextFields, ComboBox y CheckBox.
     * La contraseña se desactiva en modo edición (solo habilitada para nuevos usuarios).
     */
    private void activarFormulario() {
        txtUsername.setDisable(false);
        txtEmail.setDisable(false);
        txtNombre.setDisable(false);
        txtApellido.setDisable(false);
        cmbRol.setDisable(false);
        chkActivo.setDisable(false);
        txtPassword.setDisable(modoEdicion);
    }

    /**
     * Desactiva los campos del formulario impidiendo que el usuario modifique los datos.
     * Deshabilita todos los TextFields, ComboBox y CheckBox.
     */
    private void desactivarFormulario() {
        txtUsername.setDisable(true);
        txtEmail.setDisable(true);
        txtNombre.setDisable(true);
        txtApellido.setDisable(true);
        cmbRol.setDisable(true);
        chkActivo.setDisable(true);
        txtPassword.setDisable(true);
    }

    /**
     * Activa todos los controles de navegación y búsqueda de la tabla.
     * Habilita tabla, botones de navegación y campo de búsqueda.
     */
    private void activarNavegacion() {
        tablaUsuarios.setDisable(false);
        btnNuevo.setDisable(false);
        btnEditar.setDisable(false);
        btnPrimero.setDisable(false);
        btnAnterior.setDisable(false);
        btnSiguiente.setDisable(false);
        btnUltimo.setDisable(false);
        btnCambiarPassword.setDisable(false);
        btnDesactivar.setDisable(false);
        btnEliminar.setDisable(false);
        txtBuscar.setDisable(false);
    }

    /**
     * Desactiva todos los controles de navegación y búsqueda de la tabla.
     * Deshabilita tabla, botones de navegación y campo de búsqueda durante
     * la edición o creación de un nuevo usuario.
     */
    private void desactivarNavegacion() {
        tablaUsuarios.setDisable(true);
        btnNuevo.setDisable(true);
        btnEditar.setDisable(true);
        btnPrimero.setDisable(true);
        btnAnterior.setDisable(true);
        btnSiguiente.setDisable(true);
        btnUltimo.setDisable(true);
        btnCambiarPassword.setDisable(true);
        btnDesactivar.setDisable(true);
        btnEliminar.setDisable(true);
        txtBuscar.setDisable(true);
    }

    /**
     * Muestra un diálogo de confirmación al usuario con opciones Sí y No.
     * El diálogo es modal y bloquea la interacción hasta que sea cerrado.
     * 
     * @param titulo el título de la ventana de confirmación
     * @param mensaje el texto del mensaje a mostrar
     * @return true si el usuario selecciona Sí, false si selecciona No
     */
    private boolean confirmar(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, mensaje, ButtonType.YES, ButtonType.NO);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        return alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
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