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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.ol.dao.AutorDAO;
import org.ol.dao.impl.AutorDAOImpl;
import org.ol.exception.DaoException;
import org.ol.exception.ValidacionException;
import org.ol.model.Autor;
import org.ol.system.Main;


/**
 * Controlador para la gestión de autores en la aplicación de biblioteca.
 * 
 * Proporciona funcionalidades completas de CRUD (Crear, Leer, Actualizar, Eliminar)
 * para los registros de autores. Implementa búsqueda en tiempo real, navegación,
 * validación de datos y control de estados del formulario en la interfaz gráfica JavaFX.
 * 
 * Los campos gestionados incluyen:
 * - Nombre del autor
 * - Apellido del autor
 * - Nacionalidad
 * - Biografía
 * 
 * @author Octavio Letona
 * @version 1.0.0
 * @since 2026
 */
public class AutorViewController implements Initializable {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtNacionalidad;
    @FXML
    private TextArea txtBiografia;
    @FXML
    private Label lblMensaje;
    @FXML
    private TableView<Autor> tablaAutores;
    @FXML
    private TableColumn colIdAutor;
    @FXML
    private TableColumn colNombreAutor;
    @FXML
    private TableColumn colApellidoAutor;
    @FXML
    private TableColumn colNacionalidad;
    @FXML
    private TableColumn colBiografia;
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
    private Autor enEdicion;
    private final AutorDAO autorDAO = new AutorDAOImpl();
    private final ObservableList<Autor> listaAutores = FXCollections.observableArrayList();
    private final FilteredList<Autor> autoresFiltrados = new FilteredList<>(listaAutores, p -> true);

    /**
     * Inicializa el controlador cargando datos y configurando los componentes de la interfaz.
     * Se ejecuta automáticamente cuando se carga el archivo FXML.
     * 
     * Operaciones realizadas:
     * - Carga de todos los autores desde la base de datos
     * - Configuración de la tabla con los datos
     * - Configuración de listeners para selección de filas
     * - Configuración de columnas con PropertyValueFactory
     * - Configuración del sistema de búsqueda y filtrado
     * 
     * @param location URL de localización del recurso FXML
     * @param resources ResourceBundle con recursos internacionalizados
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarTabla();
        tablaAutores.setItems(autoresFiltrados);
        seleccionarFila();
        configurarTabla();
        configurarBusqueda();
    }

    /**
     * Configura las columnas de la tabla asignando las propiedades del modelo Autor
     * a cada columna mediante PropertyValueFactory.
     * 
     * Mapeos realizados:
     * - colIdAutor → idAutor
     * - colNombreAutor → nombreAutor
     * - colApellidoAutor → apellidoAutor
     * - colNacionalidad → nacionalidad
     * - colBiografia → biografia
     */
    public void configurarTabla() {
        colIdAutor.setCellValueFactory(new PropertyValueFactory<Autor, Integer>("idAutor"));
        colNombreAutor.setCellValueFactory(new PropertyValueFactory<Autor, String>("nombreAutor"));
        colApellidoAutor.setCellValueFactory(new PropertyValueFactory<Autor, String>("apellidoAutor"));
        colNacionalidad.setCellValueFactory(new PropertyValueFactory<Autor, String>("nacionalidad"));
        colBiografia.setCellValueFactory(new PropertyValueFactory<Autor, String>("biografia"));
    }

    /**
     * Carga todos los registros de autores desde la base de datos
     * y los adiciona a la lista observable.
     * 
     * @throws DaoException si ocurre un error al acceder a la base de datos
     */
    private void cargarTabla() {
        try {
            listaAutores.setAll(autorDAO.listarTodos());
        } catch (DaoException e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Configura un listener en el TextField de búsqueda para filtrar
     * los registros en tiempo real según el texto ingresado por el usuario.
     * 
     * @see #filtrarAutores()
     */
    private void configurarBusqueda() {
        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> filtrarAutores());
    }

    /**
     * Filtra la lista de autores según el texto de búsqueda.
     * La búsqueda es insensible a mayúsculas y busca en los campos:
     * idAutor, nombreAutor, apellidoAutor, nacionalidad y biografia.
     * 
     * Si el campo de búsqueda está vacío, muestra todos los registros.
     */
    private void filtrarAutores() {
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        if (busqueda.isEmpty()) {
            autoresFiltrados.setPredicate(p -> true);
        } else {
            autoresFiltrados.setPredicate(autor ->
                    String.valueOf(autor.getIdAutor()).contains(busqueda)
                    || autor.getNombreAutor().toLowerCase().contains(busqueda)
                    || autor.getApellidoAutor().toLowerCase().contains(busqueda)
                    || autor.getNacionalidad().toLowerCase().contains(busqueda)
                    || autor.getBiografia().toLowerCase().contains(busqueda));
        }
    }

    /**
     * Configura un listener que actualiza los campos de texto cuando se selecciona
     * una fila de la tabla. Carga los datos del autor seleccionado en los TextFields
     * correspondientes y desactiva el formulario para modo de lectura.
     * 
     * @see #desactivarFormulario()
     */
    private void seleccionarFila() {
        tablaAutores.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        txtNombre.setText(newSelection.getNombreAutor());
                        txtApellido.setText(newSelection.getApellidoAutor());
                        txtNacionalidad.setText(newSelection.getNacionalidad());
                        txtBiografia.setText(newSelection.getBiografia());
                        desactivarFormulario();
                    }
                });
    }

    /**
     * Maneja el evento de guardar un nuevo autor o actualizar uno existente.
     * Valida que los campos obligatorios (nombre, apellido, nacionalidad) no estén vacíos,
     * crea el objeto Autor y lo persiste en la base de datos según el modo (nuevo o edición).
     * 
     * Operaciones realizadas:
     * - Validación de campos obligatorios mediante ValidacionException
     * - Creación de la instancia Autor con los datos del formulario
     * - Invocación de crear() o actualizar() según el modo
     * - Actualización de la interfaz (tabla, mensajes, controles)
     * - Limpieza del formulario y retorno al estado de navegación
     * 
     * @throws ValidacionException si algún campo obligatorio está vacío
     * @throws Exception si ocurre un error general al guardar en la base de datos
     * @see #activarFormulario()
     * @see #desactivarFormulario()
     * @see #limpiarFormulario()
     */
    @FXML
    private void handleGuardar() {
        try {
            ValidacionException.validarNoVacio(txtNombre.getText(), "nombre");
            ValidacionException.validarNoVacio(txtApellido.getText(), "apellido");
            ValidacionException.validarNoVacio(txtNacionalidad.getText(), "nacionalidad");

            Autor autor = new Autor(
                    modoEdicion ? enEdicion.getIdAutor() : 0,
                    txtNombre.getText().trim(),
                    txtApellido.getText().trim(),
                    txtNacionalidad.getText().trim(),
                    txtBiografia.getText().trim());

            boolean guardado;
            if (modoEdicion) {
                guardado = autorDAO.actualizar(autor);
            } else {
                guardado = autorDAO.crear(autor);
            }

            if (guardado) {
                lblMensaje.setText(modoEdicion
                        ? "Autor actualizado exitosamente."
                        : "Autor registrado exitosamente.");
                cargarTabla();
                limpiarFormulario();
                desactivarFormulario();
                activarNavegacion();
                modoEdicion = false;
            } else {
                mostrarError("No se pudo guardar el autor.");
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
     * Maneja el evento para iniciar la creación de un nuevo autor.
     * Activa el formulario, desactiva la navegación y limpia los campos
     * para que el usuario pueda ingresar nuevos datos.
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
        activarFormulario();
        desactivarNavegacion();
        tablaAutores.getSelectionModel().clearSelection();
        lblMensaje.setText("");
        txtNombre.requestFocus();
    }

    /**
     * Maneja el evento para editar el autor seleccionado en la tabla.
     * Valida que exista una selección, activa el modo edición y desactiva la navegación
     * para permitir modificaciones.
     * 
     * @throws IllegalArgumentException si no hay autor seleccionado en la tabla
     * @see #activarFormulario()
     * @see #desactivarNavegacion()
     */
    @FXML
    private void handleEditar() {
        Autor seleccion = tablaAutores.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            mostrarError("Seleccione un autor de la tabla para editar.");
            return;
        }
        modoEdicion = true;
        enEdicion = seleccion;
        activarFormulario();
        desactivarNavegacion();
        lblMensaje.setText("");
    }

    /**
     * Navega al primer autor de la tabla y lo selecciona.
     * Si la tabla está vacía, no realiza ninguna acción.
     */
    @FXML
    private void handlePrimero() {
        if (!tablaAutores.getItems().isEmpty()) {
            tablaAutores.getSelectionModel().selectFirst();
            tablaAutores.scrollTo(0);
        }
    }

    /**
     * Navega al autor anterior en la tabla y lo selecciona.
     * Si la tabla está vacía o se alcanza el inicio, no realiza ninguna acción.
     */
    @FXML
    private void handleAnterior() {
        if (!tablaAutores.getItems().isEmpty()) {
            tablaAutores.getSelectionModel().selectPrevious();
            if (tablaAutores.getSelectionModel().getSelectedIndex() >= 0) {
                tablaAutores.scrollTo(tablaAutores.getSelectionModel().getSelectedIndex());
            }
        }
    }

    /**
     * Navega al siguiente autor en la tabla y lo selecciona.
     * Si la tabla está vacía o se alcanza el final, no realiza ninguna acción.
     */
    @FXML
    private void handleSiguiente() {
        if (!tablaAutores.getItems().isEmpty()) {
            tablaAutores.getSelectionModel().selectNext();
            if (tablaAutores.getSelectionModel().getSelectedIndex() >= 0) {
                tablaAutores.scrollTo(tablaAutores.getSelectionModel().getSelectedIndex());
            }
        }
    }

    /**
     * Navega al último autor de la tabla y lo selecciona.
     * Si la tabla está vacía, no realiza ninguna acción.
     */
    @FXML
    private void handleUltimo() {
        if (!tablaAutores.getItems().isEmpty()) {
            tablaAutores.getSelectionModel().selectLast();
            tablaAutores.scrollTo(tablaAutores.getItems().size() - 1);
        }
    }

    /**
     * Maneja el evento para retornar al menú principal o dashboard según el rol del usuario.
     * Cambia la escena actual a la ruta correspondiente obtenida de Main.
     * 
     * @throws Exception si ocurre un error al cambiar de escena
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
     * Limpia todos los campos del formulario estableciendo los TextFields y TextArea a vacíos.
     */
    private void limpiarFormulario() {
        txtNombre.clear();
        txtApellido.clear();
        txtNacionalidad.clear();
        txtBiografia.clear();
    }

    /**
     * Activa los campos del formulario permitiendo que el usuario ingrese o modifique datos.
     * Habilita todos los TextFields y TextArea del formulario.
     */
    private void activarFormulario() {
        txtNombre.setDisable(false);
        txtApellido.setDisable(false);
        txtNacionalidad.setDisable(false);
        txtBiografia.setDisable(false);
    }

    /**
     * Desactiva los campos del formulario impidiendo que el usuario modifique los datos.
     * Deshabilita todos los TextFields y TextArea del formulario.
     */
    private void desactivarFormulario() {
        txtNombre.setDisable(true);
        txtApellido.setDisable(true);
        txtNacionalidad.setDisable(true);
        txtBiografia.setDisable(true);
    }

    /**
     * Activa todos los controles de navegación y búsqueda de la tabla.
     * Habilita la tabla, botones de navegación y campo de búsqueda.
     */
    private void activarNavegacion() {
        tablaAutores.setDisable(false);
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
     * Deshabilita la tabla, botones de navegación y campo de búsqueda durante
     * la edición o creación de un nuevo registro.
     */
    private void desactivarNavegacion() {
        tablaAutores.setDisable(true);
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