/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.ol.exception;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Excepción utilizada para representar errores producidos durante
 * la validación de datos ingresados en la aplicación.
 *
 * Esta clase proporciona métodos estáticos para validar campos de texto,
 * números, correos electrónicos, fechas, longitudes y otros valores
 * utilizados dentro del sistema.
 *
 * @author Octavio Letona
 * @version 1.0
 */
public class ValidacionException extends Exception {

    /**
     * Logger utilizado para registrar eventos relacionados con
     * las validaciones realizadas por esta clase.
     */
    private static final Logger log
            = Logger.getLogger(ValidacionException.class.getName());

    /**
     * Construye una nueva excepción de validación con un mensaje
     * descriptivo del error ocurrido.
     *
     * @param mensaje mensaje que describe el error de validación
     */
    public ValidacionException(String mensaje) {
        super(mensaje);
    }

    /**
     * Valida que una cadena de texto no sea nula ni esté vacía.
     *
     * @param valor valor que será validado
     * @param nombreCampo nombre del campo que se está validando
     * @throws ValidacionException si el valor es nulo o está vacío
     */
    public static void validarNoVacio(String valor, String nombreCampo)
            throws ValidacionException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidacionException(
                    "El campo " + nombreCampo + " no puede estar vacío.");
        }

        log.log(Level.WARNING, "No puede estar vacío el campo: ", nombreCampo);
    }

    /**
     * Valida que dos cadenas de texto sean iguales.
     *
     * @param a primer valor que será comparado
     * @param b segundo valor que será comparado
     * @param mensaje mensaje mostrado cuando los valores no coinciden
     * @throws ValidacionException si los valores proporcionados no coinciden
     */
    public static void validarCoinciden(String a, String b, String mensaje)
            throws ValidacionException {
        if (!a.equals(b)) {
            throw new ValidacionException(mensaje);
        }
    }

    /**
     * Valida que una cadena tenga como mínimo una cantidad determinada
     * de caracteres.
     *
     * @param valor cadena cuya longitud será validada
     * @param min longitud mínima permitida
     * @param mensaje mensaje mostrado cuando no se cumple la longitud mínima
     * @throws ValidacionException si la longitud del valor es menor al mínimo
     */
    public static void validarLongitudMinima(String valor, int min, String mensaje)
            throws ValidacionException {
        if (valor.length() < min) {
            throw new ValidacionException(mensaje);
        }
    }

    /**
     * Valida que un objeto no sea nulo.
     *
     * @param obj objeto que será validado
     * @param mensaje mensaje mostrado cuando el objeto es nulo
     * @throws ValidacionException si el objeto proporcionado es nulo
     */
    public static void validarNoNulo(Object obj, String mensaje)
            throws ValidacionException {
        if (obj == null) {
            throw new ValidacionException(mensaje);
        }
    }

    /**
     * Valida que una cadena de texto represente un número entero válido.
     *
     * @param valor cadena que contiene el número que será validado
     * @param nombreCampo nombre del campo que se está validando
     * @throws ValidacionException si el valor no representa un número válido
     */
    public static void validarNumero(String valor, String nombreCampo)
            throws ValidacionException {
        try {
            Long.parseLong(valor.trim());
        } catch (NumberFormatException e) {
            throw new ValidacionException(
                    "El campo " + nombreCampo + " debe ser un número válido.");
        }
    }

    /**
     * Valida que una cadena posea exactamente la longitud especificada.
     *
     * @param valor cadena cuya longitud será validada
     * @param longitud cantidad exacta de caracteres requerida
     * @param mensaje mensaje mostrado cuando la longitud no es válida
     * @throws ValidacionException si la longitud del valor es diferente
     * a la longitud especificada
     */
    public static void validarLongitudExacta(
            String valor, int longitud, String mensaje)
            throws ValidacionException {
        if (valor.length() != longitud) {
            throw new ValidacionException(mensaje);
        }
    }

    /**
     * Valida que una cadena tenga un formato válido de correo electrónico.
     *
     * @param valor correo electrónico que será validado
     * @param mensaje mensaje mostrado cuando el formato no es válido
     * @throws ValidacionException si el valor no cumple con el formato
     * esperado de un correo electrónico
     */
    public static void validarFormatoEmail(String valor, String mensaje)
            throws ValidacionException {
        if (!valor.matches("[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+")) {
            throw new ValidacionException(mensaje);
        }
    }

    /**
     * Valida que una cadena pueda convertirse en un número decimal.
     *
     * @param valor cadena que contiene el número decimal que será validado
     * @param nombreCampo nombre del campo que se está validando
     * @throws ValidacionException si el valor no representa un número
     * decimal válido
     */
    public static void validarDecimal(String valor, String nombreCampo)
            throws ValidacionException {
        try {
            Double.parseDouble(valor.trim());
        } catch (NumberFormatException e) {
            throw new ValidacionException(
                    "El campo " + nombreCampo + " debe ser un número válido.");
        }
    }

    /**
     * Valida que una cadena tenga el formato de fecha AAAA-MM-DD.
     *
     * @param valor cadena que contiene la fecha que será validada
     * @param mensaje mensaje mostrado cuando el formato de fecha no es válido
     * @throws ValidacionException si el valor no cumple con el formato
     * AAAA-MM-DD
     */
    public static void validarFormatoFecha(String valor, String mensaje)
            throws ValidacionException {
        if (!valor.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new ValidacionException(mensaje);
        }
    }

    /**
     * Valida que una cadena represente un número entero mayor que cero.
     *
     * Primero se verifica que el valor sea un número válido y posteriormente
     * se comprueba que dicho número sea positivo.
     *
     * @param valor cadena que contiene el número que será validado
     * @param nombreCampo nombre del campo que se está validando
     * @throws ValidacionException si el valor no es numérico o si el número
     * es menor o igual a cero
     */
    public static void validarPositivo(String valor, String nombreCampo)
            throws ValidacionException {
        validarNumero(valor, nombreCampo);
        if (Long.parseLong(valor.trim()) <= 0) {
            throw new ValidacionException(
                    "El campo " + nombreCampo
                    + " debe ser un número mayor que cero.");
        }
    }
}