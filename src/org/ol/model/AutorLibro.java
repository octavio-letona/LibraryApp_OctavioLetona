/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ol.model;

/**
 *
 * @author informatica
 */

public class AutorLibro {
    private int idAutorLibro;
    private int idAutor;
    private String isbn;

    public AutorLibro() {
    }

    public AutorLibro(int idAutorLibro, int idAutor, String isbn) {
        this.idAutorLibro = idAutorLibro;
        this.idAutor = idAutor;
        this.isbn = isbn;
    }

    public int getIdAutorLibro() {
        return idAutorLibro;
    }

    public int getIdAutor() {
        return idAutor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIdAutorLibro(int idAutorLibro) {
        this.idAutorLibro = idAutorLibro;
    }

    public int getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    
    
}
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
