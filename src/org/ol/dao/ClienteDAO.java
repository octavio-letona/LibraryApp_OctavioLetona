/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package org.ol.dao;

import org.ol.model.Cliente;

/**
 * Contrato DAO que define las operaciones de persistencia CRUD
 * para la entidad {@link Cliente} dentro del sistema.
 * 
 * Extiende de {@link Crud} empleando un identificador de tipo {@link Long}
 * (como NIT o código de cliente) como clave primaria.
 * 
 * @author Octavio Letona
 * @version 1.0.0
 * @see org.ol.model.Cliente
 * @see org.ol.dao.Crud
 */
public interface ClienteDAO extends Crud<Cliente, Long> {

}