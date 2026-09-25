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
import org.ol.dao.CategoriaDAO;
import org.ol.dao.EditorialDAO;
import org.ol.dao.LibroDAO;
import org.ol.dao.impl.CategoriaDAOImpl;
import org.ol.dao.impl.EditorialDAOImpl;
import org.ol.dao.impl.LibroDAOImpl;
import org.ol.exception.DaoException;
import org.ol.exception.ValidacionException;
import org.ol.model.Categoria;
import org.ol.model.Editorial;
import org.ol.model.Libro;
import org.ol.system.Main;

/**
 * Controlador FXML encargado de gestionar el CRUD y la navegación de los
 * libros dentro de la aplicación LibraryApp.
 * <p>
 * Permite registrar, editar, buscar y recorrer (primero, anterior, siguiente,
 * último) los registros de {@link Libro}, enlazando la tabla visual con la
 * capa de acceso a datos ({@link LibroDAO}) y con los combos de
 * {@link Categoria} ({@link CategoriaDAO}) y {@link Editorial}
 * ({@link EditorialDAO}) asociados a cada libro.
 *
 * @author Octavio Javier Letona Figueroa
 * @version 1.0.0
 * @see Libro
 * @see LibroDAO
 */
public class LibroController implements Initializable {

    @FXML
    private TextField txtIsbn;
    @FXML
    private TextField txtTitulo;
    @FXML
    private TextField txtFecha;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextField txtStock;
    @FXML
    private ComboBox<Categoria> cmbCategoria;
    @FXML
    private ComboBox<Editorial> cmbEditorial;
    @FXML
    private Label lblMensaje;
    @FXML
    private TableView<Libro> tablaLibros;
    @FXML
    private TableColumn colIsbn;
    @FXML
    private TableColumn colTitulo;
    @FXML
    private TableColumn colFecha;
    @FXML
    private TableColumn colPrecio;
    @FXML
    private TableColumn colStock;
    @FXML
    private TableColumn colIdCategoria;
    @FXML
    private TableColumn colNitEditorial;
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
    private final LibroDAO libroDAO = new LibroDAOImpl();
    private final CategoriaDAO categoriaDAO = new CategoriaDAOImpl();
    private final EditorialDAO editorialDAO = new EditorialDAOImpl();
    private final ObservableList<Libro> listaLibros = FXCollections.observableArrayList();
    private final FilteredList<Libro> librosFiltrados = new FilteredList<>(listaLibros, p -> true);

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
        tablaLibros.setItems(librosFiltrados);
        seleccionarFila();
        configurarTabla();
        configurarBusqueda();
    }

    /**
     * Asocia cada columna de {@link #tablaLibros} con la propiedad
     * correspondiente del modelo {@link Libro} mediante
     * {@link PropertyValueFactory}.
     */
    public void configurarTabla() {
        colIsbn.setCellValueFactory(new PropertyValueFactory<Libro, String>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<Libro, String>("titulo"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Libro, String>("fechaPublicacion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<Libro, Double>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<Libro, Integer>("stock"));
        colIdCategoria.setCellValueFactory(new PropertyValueFactory<Libro, Integer>("idCategoria"));
        colNitEditorial.setCellValueFactory(new PropertyValueFactory<Libro, String>("nitEditorial"));
    }

    /**
     * Recupera todos los libros desde la base de datos a través de
     * {@link #libroDAO} y los carga en {@link #listaLibros}. Si ocurre un
     * error de acceso a datos, se muestra una alerta al usuario.
     */
    private void cargarTabla() {
        try {
            listaLibros.setAll(libroDAO.listarTodos());
        } catch (DaoException e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Carga los combos de {@link #cmbCategoria} y {@link #cmbEditorial}
     * con la información obtenida de {@link #categoriaDAO} y
     * {@link #editorialDAO} respectivamente.
     */
    private void cargarCombos() {
        try {
            cmbCategoria.setItems(FXCollections.observableArrayList(categoriaDAO.listarTodos()));
            cmbEditorial.setItems(FXCollections.observableArrayList(editorialDAO.listarTodos()));
        } catch (DaoException e) {
            mostrarError(e.getMessage());
        }
    }

    /**
     * Registra un listener sobre {@link #txtBuscar} para filtrar la tabla
     * de libros cada vez que cambia el texto de búsqueda.
     */
    private void configurarBusqueda() {
        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> filtrarLibros());
    }

    /**
     * Aplica un predicado sobre {@link #librosFiltrados} según el texto
     * ingresado en {@link #txtBuscar}, comparando contra el ISBN, título,
     * fecha de publicación, precio, stock, categoría y editorial de cada
     * {@link Libro}.
     */
    private void filtrarLibros() {
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        if (busqueda.isEmpty()) {
            librosFiltrados.setPredicate(p -> true);
        } else {
            librosFiltrados.setPredicate(libro ->
                    libro.getIsbn().toLowerCase().contains(busqueda)
                    || libro.getTitulo().toLowerCase().contains(busqueda)
                    || libro.getFechaPublicacion().toLowerCase().contains(busqueda)
                    || String.valueOf(libro.getPrecio()).contains(busqueda)
                    || String.valueOf(libro.getStock()).contains(busqueda)
                    || String.valueOf(libro.getIdCategoria()).contains(busqueda)
                    || libro.getNitEditorial().toLowerCase().contains(busqueda));
        }
    }

    /**
     * Registra un listener sobre la selección de {@link #tablaLibros} para
     * precargar el formulario (ISBN, título, fecha, precio, stock, categoría
     * y editorial) con los datos de la fila seleccionada y desactivar el
     * formulario para evitar ediciones accidentales.
     */
    private void seleccionarFila() {
        tablaLibros.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        txtIsbn.setText(newSelection.getIsbn());
                        txtTitulo.setText(newSelection.getTitulo());
                        txtFecha.setText(newSelection.getFechaPublicacion());
                        txtPrecio.setText(String.valueOf(newSelection.getPrecio()));
                        txtStock.setText(String.valueOf(newSelection.getStock()));
                        cmbCategoria.setValue(null);
                        for (Categoria categoria : cmbCategoria.getItems()) {
                            if (categoria.getIdCategoria() == newSelection.getIdCategoria()) {
                                cmbCategoria.setValue(categoria);
                                break;
                            }
                        }
                        cmbEditorial.setValue(null);
                        for (Editorial editorial : cmbEditorial.getItems()) {
                            if (editorial.getNit().equals(newSelection.getNitEditorial())) {
                                cmbEditorial.setValue(editorial);
                                break;
                            }
                        }
                        desactivarFormulario();
                    }
                });
    }

    /**
     * Valida los datos del formulario y guarda el libro, creando un nuevo
     * registro o actualizando uno existente según {@link #modoEdicion}.
     * Muestra mensajes de éxito, advertencia o error según el resultado de
     * la operación.
     *
     * @throws ValidacionException si algún campo requerido está vacío, si
     *                              el precio no es decimal, si el stock no
     *                              es numérico o es negativo, si la fecha
     *                              de publicación no tiene formato
     *                              YYYY-MM-DD, o si no se seleccionó
     *                              categoría o editorial (capturada
     *                              internamente y mostrada como advertencia
     *                              al usuario).
     */
    @FXML
    private void handleGuardar() {
        try {
            ValidacionException.validarNoVacio(txtIsbn.getText(), "ISBN");
            ValidacionException.validarNoVacio(txtTitulo.getText(), "título");
            ValidacionException.validarNoVacio(txtFecha.getText(), "fecha de publicación");
            ValidacionException.validarNoVacio(txtPrecio.getText(), "precio");
            ValidacionException.validarDecimal(txtPrecio.getText(), "precio");
            ValidacionException.validarNoVacio(txtStock.getText(), "stock");
            ValidacionException.validarNumero(txtStock.getText(), "stock");
            if (Integer.parseInt(txtStock.getText().trim()) < 0) {
                throw new ValidacionException("El campo stock no puede ser negativo.");
            }
            ValidacionException.validarFormatoFecha(txtFecha.getText(),
                    "La fecha de publicación debe tener formato YYYY-MM-DD.");
            ValidacionException.validarNoNulo(cmbCategoria.getValue(),
                    "Seleccione una categoría.");
            ValidacionException.validarNoNulo(cmbEditorial.getValue(),
                    "Seleccione una editorial.");

            Libro libro = new Libro(
                    txtIsbn.getText().trim(),
                    txtTitulo.getText().trim(),
                    txtFecha.getText().trim(),
                    Double.parseDouble(txtPrecio.getText().trim()),
                    cmbCategoria.getValue().getIdCategoria(),
                    cmbEditorial.getValue().getNit(),
                    Integer.parseInt(txtStock.getText().trim()));

            boolean guardado;
            if (modoEdicion) {
                guardado = libroDAO.actualizar(libro);
            } else {
                guardado = libroDAO.crear(libro);
            }

            if (guardado) {
                lblMensaje.setText(modoEdicion
                        ? "Libro actualizado exitosamente."
                        : "Libro registrado exitosamente.");
                cargarTabla();
                limpiarFormulario();
                desactivarFormulario();
                activarNavegacion();
                modoEdicion = false;
            } else {
                mostrarError("No se pudo guardar el libro.");
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
     * Prepara la interfaz para el registro de un nuevo libro: desactiva el
     * modo edición, limpia el formulario, lo activa, bloquea la navegación
     * y coloca el foco en {@link #txtIsbn}.
     */
    @FXML
    private void handleNuevo() {
        modoEdicion = false;
        limpiarFormulario();
        activarFormulario();
        desactivarNavegacion();
        tablaLibros.getSelectionModel().clearSelection();
        lblMensaje.setText("");
        txtIsbn.requestFocus();
    }

    /**
     * Habilita la edición del libro seleccionado en la tabla. Si no hay
     * ninguna fila seleccionada, muestra un mensaje de error y no realiza
     * ninguna acción adicional.
     */
    @FXML
    private void handleEditar() {
        Libro seleccion = tablaLibros.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            mostrarError("Seleccione un libro de la tabla para editar.");
            return;
        }
        modoEdicion = true;
        activarFormulario();
        desactivarNavegacion();
        lblMensaje.setText("");
    }

    /**
     * Selecciona y desplaza la vista hasta el primer registro de la tabla
     * de libros, si existe al menos uno.
     */
    @FXML
    private void handlePrimero() {
        if (!tablaLibros.getItems().isEmpty()) {
            tablaLibros.getSelectionModel().selectFirst();
            tablaLibros.scrollTo(0);
        }
    }

    /**
     * Selecciona el registro anterior al actualmente seleccionado en la
     * tabla de libros y desplaza la vista hacia él.
     */
    @FXML
    private void handleAnterior() {
        if (!tablaLibros.getItems().isEmpty()) {
            tablaLibros.getSelectionModel().selectPrevious();
            if (tablaLibros.getSelectionModel().getSelectedIndex() >= 0) {
                tablaLibros.scrollTo(tablaLibros.getSelectionModel().getSelectedIndex());
            }
        }
    }

    /**
     * Selecciona el registro siguiente al actualmente seleccionado en la
     * tabla de libros y desplaza la vista hacia él.
     */
    @FXML
    private void handleSiguiente() {
        if (!tablaLibros.getItems().isEmpty()) {
            tablaLibros.getSelectionModel().selectNext();
            if (tablaLibros.getSelectionModel().getSelectedIndex() >= 0) {
                tablaLibros.scrollTo(tablaLibros.getSelectionModel().getSelectedIndex());
            }
        }
    }

    /**
     * Selecciona y desplaza la vista hasta el último registro de la tabla
     * de libros, si existe al menos uno.
     */
    @FXML
    private void handleUltimo() {
        if (!tablaLibros.getItems().isEmpty()) {
            tablaLibros.getSelectionModel().selectLast();
            tablaLibros.scrollTo(tablaLibros.getItems().size() - 1);
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
     * Restablece los campos del formulario (ISBN, título, fecha, precio,
     * stock, categoría y editorial) a sus valores vacíos o nulos por
     * defecto.
     */
    private void limpiarFormulario() {
        txtIsbn.clear();
        txtTitulo.clear();
        txtFecha.clear();
        txtPrecio.clear();
        txtStock.clear();
        cmbCategoria.setValue(null);
        cmbEditorial.setValue(null);
    }

    /**
     * Habilita los controles del formulario (ISBN, título, fecha, precio,
     * stock, categoría y editorial) para permitir el ingreso o edición de
     * datos.
     */
    private void activarFormulario() {
        txtIsbn.setDisable(false);
        txtTitulo.setDisable(false);
        txtFecha.setDisable(false);
        txtPrecio.setDisable(false);
        txtStock.setDisable(false);
        cmbCategoria.setDisable(false);
        cmbEditorial.setDisable(false);
    }

    /**
     * Deshabilita los controles del formulario (ISBN, título, fecha,
     * precio, stock, categoría y editorial) para evitar modificaciones no
     * deseadas.
     */
    private void desactivarFormulario() {
        txtIsbn.setDisable(true);
        txtTitulo.setDisable(true);
        txtFecha.setDisable(true);
        txtPrecio.setDisable(true);
        txtStock.setDisable(true);
        cmbCategoria.setDisable(true);
        cmbEditorial.setDisable(true);
    }

    /**
     * Habilita los controles de navegación y acciones sobre la tabla
     * (tabla, botones de CRUD/navegación y buscador).
     */
    private void activarNavegacion() {
        tablaLibros.setDisable(false);
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
        tablaLibros.setDisable(true);
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