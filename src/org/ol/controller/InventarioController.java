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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.ol.dao.LibroDAO;
import org.ol.dao.impl.LibroDAOImpl;
import org.ol.exception.DaoException;
import org.ol.model.Libro;
import org.ol.system.Main;

/**
 * Controlador FXML encargado de mostrar el inventario de libros dentro de
 * la aplicación LibraryApp.
 * <p>
 * Es una vista de solo lectura: presenta el listado completo de
 * {@link Libro} (ISBN, título, precio y stock) obtenido de
 * {@link LibroDAO} y permite filtrarlo mediante un buscador de texto.
 *
 * @author Octavio Javier Letona Figueroa
 * @version 1.0.0
 * @see Libro
 * @see LibroDAO
 */
public class InventarioController implements Initializable {

    @FXML
    private TableView<Libro> tablaInventario;
    @FXML
    private TableColumn colIsbn;
    @FXML
    private TableColumn colTitulo;
    @FXML
    private TableColumn colPrecio;
    @FXML
    private TableColumn colStock;
    @FXML
    private TextField txtBuscar;

    private final LibroDAO libroDAO = new LibroDAOImpl();
    private final ObservableList<Libro> listaLibros = FXCollections.observableArrayList();
    private final FilteredList<Libro> librosFiltrados = new FilteredList<>(listaLibros, p -> true);

    /**
     * Inicializa el controlador después de que su elemento raíz haya sido
     * procesado por completo. Carga la tabla de inventario, configura sus
     * columnas y el buscador.
     *
     * @param location  la ubicación usada para resolver rutas relativas del
     *                  objeto raíz, o {@code null} si no se conoce.
     * @param resources los recursos usados para localizar el objeto raíz,
     *                  o {@code null} si no se localizó.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarTabla();
        tablaInventario.setItems(librosFiltrados);
        configurarTabla();
        configurarBusqueda();
    }

    /**
     * Asocia cada columna de {@link #tablaInventario} con la propiedad
     * correspondiente del modelo {@link Libro} mediante
     * {@link PropertyValueFactory}.
     */
    public void configurarTabla() {
        colIsbn.setCellValueFactory(new PropertyValueFactory<Libro, String>("isbn"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<Libro, String>("titulo"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<Libro, Double>("precio"));
        colStock.setCellValueFactory(new PropertyValueFactory<Libro, Integer>("stock"));
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
     * Registra un listener sobre {@link #txtBuscar} para filtrar la tabla
     * de inventario cada vez que cambia el texto de búsqueda.
     */
    private void configurarBusqueda() {
        txtBuscar.textProperty().addListener((obs, oldValue, newValue) -> filtrarLibros());
    }

    /**
     * Aplica un predicado sobre {@link #librosFiltrados} según el texto
     * ingresado en {@link #txtBuscar}, comparando contra el ISBN, título,
     * precio y stock de cada {@link Libro}.
     */
    private void filtrarLibros() {
        String busqueda = txtBuscar.getText().trim().toLowerCase();
        if (busqueda.isEmpty()) {
            librosFiltrados.setPredicate(p -> true);
        } else {
            librosFiltrados.setPredicate(libro ->
                    libro.getIsbn().toLowerCase().contains(busqueda)
                    || libro.getTitulo().toLowerCase().contains(busqueda)
                    || String.valueOf(libro.getPrecio()).contains(busqueda)
                    || String.valueOf(libro.getStock()).contains(busqueda));
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