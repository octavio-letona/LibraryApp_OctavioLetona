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
import org.ol.dao.AutorDAO;
import org.ol.dao.AutorLibroDAO;
import org.ol.dao.LibroDAO;
import org.ol.dao.impl.AutorDAOImpl;
import org.ol.dao.impl.AutorLibroDAOImpl;
import org.ol.dao.impl.LibroDAOImpl;
import org.ol.exception.DaoException;
import org.ol.exception.ValidacionException;
import org.ol.model.Autor;
import org.ol.model.AutorLibro;
import org.ol.model.Libro;
import org.ol.system.Main;

/**
 * Controlador para gestionar la relación entre Autores y Libros en la aplicación.
 * 
 * Proporciona funcionalidades completas de CRUD (Crear, Leer, Actualizar, Eliminar)
 * para las asociaciones entre autores y libros. Implementa búsqueda, navegación,
 * y validación de datos en la interfaz gráfica JavaFX.
 * 
 * @author Octavio Letona
 * @version 1.0.0
 * @since 2026
 */
public class AutorLibroViewController implements Initializable {

    @FXML
    private ComboBox<Autor> cmbAutor;
    @FXML
    private ComboBox<Libro> cmbLibro;
    @FXML
    private Label lblMensaje;
    @FXML
    private TableView<AutorLibro> tablaAutoresLibro;
    @FXML
    private TableColumn colIdAutorLibro;
    @FXML
    private TableColumn colIdAutor;
    @FXML
    private TableColumn colIsbn;
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
    private AutorLibro enEdicion;
    private final AutorLibroDAO autorLibroDAO = new AutorLibroDAOImpl();
    private final AutorDAO autorDAO = new AutorDAOImpl();
    private final LibroDAO libroDAO = new LibroDAOImpl();
    private final ObservableList<AutorLibro> listaAutoresLibro = FXCollections.observableArrayList();
    private final FilteredList<AutorLibro> autoresLibroFiltrados = new FilteredList<>(listaAutoresLibro, p -> true);

    /**
     * Inicializa el controlador cargando datos, configurando componentes y estableciendo listeners.
     * Se ejecuta automáticamente cuando se carga el archivo FXML.
     * 
     * @param location URL de localización del recurso FXML
     * @param resources ResourceBundle con recursos internacionalizados
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarTabla();
        cargarCombos();
        tablaAutoresLibro.setItems(autoresLibroFiltrados);
        seleccionarFila();
        configurarTabla();
        configurarBusqueda();
    }

    /**
     * Configura las columnas de la tabla asignando las propiedades del modelo
     * a cada columna mediante PropertyValueFactory.
     * 
     * Mapea:
     * - colIdAutorLibro → idAutorLibro
     * - colIdAutor → idAutor
     * - colIsbn → isbn
     */
    public void configurarTabla() {
        colIdAutorLibro.setCellValueFactory(new PropertyValueFactory<AutorLibro, Integer>("idAutorLibro"));
        colIdAutor.setCellValueFactory(new PropertyValueFactory<AutorLibro, Integer>("idAutor"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<AutorLibro, String>("isbn"));
    }

    /**
     * Carga todos los registros de relaciones autor-libro desde la base de datos
     * y los adiciona a la lista observable.
     * 
     * @throws DaoException si ocurre un error al acceder a la base de datos
     */
    private void cargarTabla() {
        try {
            listaAutoresLibro.setAll(autorLibroDAO.listarTodos());
        } catch (DaoException e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Carga los listados de autores y libros desde la base de datos
     * y los asigna a los ComboBox correspondientes.
     * 
     * @throws DaoException si ocurre un error al acceder a la base de datos
     */
    private void cargarCombos() {
        try {
            cmbAutor.setItems(FXCollections.observableArrayList(autorDAO.listarTodos()));
            cmbLibro.setItems(FXCollections.observableArrayList(libroDAO.listarTodos()));
        } catch (DaoException e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Configura un listener en el TextField de búsqueda para filtrar
     * los registros en tiempo real según el texto ingresado.
     * 
     * @see #filtrarAutoresLibro()
     */
    private void configurarBusqueda() {
        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> filtrarAutoresLibro());
    }

    /**
     * Filtra la lista de relaciones autor-libro según el texto de búsqueda.
     * La búsqueda es insensible a mayúsculas y busca en los campos:
     * idAutorLibro, idAutor e isbn.
     * 
     * Si el campo de búsqueda está vacío, muestra todos los registros.
     */
    private void filtrarAutoresLibro() {
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        if (busqueda.isEmpty()) {
            autoresLibroFiltrados.setPredicate(p -> true);
        } else {
            autoresLibroFiltrados.setPredicate(autorLibro ->
                    String.valueOf(autorLibro.getIdAutorLibro()).contains(busqueda)
                    || String.valueOf(autorLibro.getIdAutor()).contains(busqueda)
                    || autorLibro.getIsbn().toLowerCase().contains(busqueda));
        }
    }

    /**
     * Configura un listener que actualiza los ComboBox cuando se selecciona
     * una fila de la tabla. Los ComboBox se actualizan con los valores
     * correspondientes a la relación seleccionada y se desactivan después.
     * 
     * @see #desactivarFormulario()
     */
    private void seleccionarFila() {
        tablaAutoresLibro.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        cmbAutor.setValue(null);
                        for (Autor autor : cmbAutor.getItems()) {
                            if (autor.getIdAutor() == newSelection.getIdAutor()) {
                                cmbAutor.setValue(autor);
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
                        desactivarFormulario();
                    }
                });
    }

    /**
     * Maneja el evento de guardar una nueva relación autor-libro o actualizar una existente.
     * Valida que ambos ComboBox tengan selecciones, crea el objeto AutorLibro y
     * lo persiste en la base de datos según el modo (nuevo o edición).
     * Operaciones realizadas:
     * - Validación de campos obligatorios
     * - Creación o actualización del registro
     * - Actualización de la interfaz (tabla, mensajes, controles)
     * @throws ValidacionException si falta seleccionar autor o libro
     * @throws Exception si ocurre un error general al guardar
     */
    @FXML
    private void handleGuardar() {
        try {
            ValidacionException.validarNoNulo(cmbAutor.getValue(),
                    "Seleccione un autor.");
            ValidacionException.validarNoNulo(cmbLibro.getValue(),
                    "Seleccione un libro.");

            AutorLibro autorLibro = new AutorLibro(
                    modoEdicion ? enEdicion.getIdAutorLibro() : 0,
                    cmbAutor.getValue().getIdAutor(),
                    cmbLibro.getValue().getIsbn());

            boolean guardado;
            if (modoEdicion) {
                guardado = autorLibroDAO.actualizar(autorLibro);
            } else {
                guardado = autorLibroDAO.crear(autorLibro);
            }

            if (guardado) {
                lblMensaje.setText(modoEdicion
                        ? "Relación autor-libro actualizada exitosamente."
                        : "Relación autor-libro registrada exitosamente.");
                cargarTabla();
                limpiarFormulario();
                desactivarFormulario();
                activarNavegacion();
                modoEdicion = false;
            } else {
                mostrarError("No se pudo guardar la relación autor-libro.");
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
     * Maneja el evento para iniciar la creación de una nueva relación autor-libro.
     * Activa el formulario, desactiva la navegación y limpia los campos para que
     * el usuario pueda ingresar nuevos datos.
     * 
     * @see #activarFormulario()
     * @see #desactivarNavegacion()
     */
    @FXML
    private void handleNuevo() {
        modoEdicion = false;
        enEdicion = null;
        limpiarFormulario();
        activarFormulario();
        desactivarNavegacion();
        tablaAutoresLibro.getSelectionModel().clearSelection();
        lblMensaje.setText("");
        cmbAutor.requestFocus();
    }

    /**
     * Maneja el evento para editar la relación autor-libro seleccionada en la tabla.
     * Valida que exista una selección, activa el modo edición y desactiva la navegación.
     * 
     * @throws IllegalArgumentException si no hay relación seleccionada en la tabla
     * @see #activarFormulario()
     * @see #desactivarNavegacion()
     */
    @FXML
    private void handleEditar() {
        AutorLibro seleccion = tablaAutoresLibro.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            mostrarError("Seleccione una relación autor-libro de la tabla para editar.");
            return;
        }
        modoEdicion = true;
        enEdicion = seleccion;
        activarFormulario();
        desactivarNavegacion();
        lblMensaje.setText("");
    }

    /**
     * Navega a la primera relación autor-libro de la tabla y la selecciona.
     * Si la tabla está vacía, no realiza ninguna acción.
     */
    @FXML
    private void handlePrimero() {
        if (!tablaAutoresLibro.getItems().isEmpty()) {
            tablaAutoresLibro.getSelectionModel().selectFirst();
            tablaAutoresLibro.scrollTo(0);
        }
    }

    /**
     * Navega a la relación anterior en la tabla y la selecciona.
     * Si la tabla está vacía o se alcanza el inicio, no realiza ninguna acción.
     */
    @FXML
    private void handleAnterior() {
        if (!tablaAutoresLibro.getItems().isEmpty()) {
            tablaAutoresLibro.getSelectionModel().selectPrevious();
            if (tablaAutoresLibro.getSelectionModel().getSelectedIndex() >= 0) {
                tablaAutoresLibro.scrollTo(tablaAutoresLibro.getSelectionModel().getSelectedIndex());
            }
        }
    }

    /**
     * Navega a la siguiente relación en la tabla y la selecciona.
     * Si la tabla está vacía o se alcanza el final, no realiza ninguna acción.
     */
    @FXML
    private void handleSiguiente() {
        if (!tablaAutoresLibro.getItems().isEmpty()) {
            tablaAutoresLibro.getSelectionModel().selectNext();
            if (tablaAutoresLibro.getSelectionModel().getSelectedIndex() >= 0) {
                tablaAutoresLibro.scrollTo(tablaAutoresLibro.getSelectionModel().getSelectedIndex());
            }
        }
    }

    /**
     * Navega a la última relación autor-libro de la tabla y la selecciona.
     * Si la tabla está vacía, no realiza ninguna acción.
     */
    @FXML
    private void handleUltimo() {
        if (!tablaAutoresLibro.getItems().isEmpty()) {
            tablaAutoresLibro.getSelectionModel().selectLast();
            tablaAutoresLibro.scrollTo(tablaAutoresLibro.getItems().size() - 1);
        }
    }

    /**
     * Maneja el evento para retornar al menú principal o dashboard según el rol del usuario.
     * Cambia la escena a la ruta correspondiente obtenida de Main.
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
     * Limpia todos los campos del formulario estableciendo los ComboBox a null.
     */
    private void limpiarFormulario() {
        cmbAutor.setValue(null);
        cmbLibro.setValue(null);
    }

    /**
     * Activa los campos del formulario permitiendo que el usuario ingrese datos.
     * Habilita los ComboBox de autor y libro.
     */
    private void activarFormulario() {
        cmbAutor.setDisable(false);
        cmbLibro.setDisable(false);
    }

    /**
     * Desactiva los campos del formulario impidiendo que el usuario modifique los datos.
     * Deshabilita los ComboBox de autor y libro.
     */
    private void desactivarFormulario() {
        cmbAutor.setDisable(true);
        cmbLibro.setDisable(true);
    }

    /**
     * Activa todos los controles de navegación y búsqueda de la tabla.
     * Habilita tabla, botones de navegación y campo de búsqueda.
     */
    private void activarNavegacion() {
        tablaAutoresLibro.setDisable(false);
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
     * la edición o creación de un nuevo registro.
     */
    private void desactivarNavegacion() {
        tablaAutoresLibro.setDisable(true);
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