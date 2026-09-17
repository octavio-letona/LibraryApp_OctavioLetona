/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ol.model;

/**
 *
 * @author Octavio letona
 * @version 1.0.0
 * @see org.ol.model.Libro
 */
public class Libro {

    public Libro(String isbn, String titulo, String fechaPublicacion, double precio, int idCategoria, String nitEditorial, int stock) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.fechaPublicacion = fechaPublicacion;
        this.precio = precio;
        this.idCategoria = idCategoria;
        this.nitEditorial = nitEditorial;
        this.stock = stock;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public void setNitEditorial(String nitEditorial) {
        this.nitEditorial = nitEditorial;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public double getPrecio() {
        return precio;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public String getNitEditorial() {
        return nitEditorial;
    }

    public int getStock() {
        return stock;
    }

    private String isbn;
    private String titulo;
    private String fechaPublicacion;
    private double precio;
    private int idCategoria;
    private String nitEditorial;
    private int stock;

    public Libro() {
    }

}
