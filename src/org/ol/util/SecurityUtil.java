package org.ol.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Clase de utilidad con operaciones de seguridad para el sistema. Actualmente
 * ofrece el hasheo de contraseñas con el algoritmo SHA-256, de modo que en la
 * base de datos nunca se guarde la contraseña en texto plano.
 *
 * @author Octavio Letona
 * @version 1.0.0
 * @see org.ol.model.Usuario#getPasswordHash()
 */
public class SecurityUtil {

    /**
     * Genera el hash SHA-256 de una contraseña. El texto se convierte a bytes
     * con codificación UTF-8 y el resultado se devuelve como una cadena
     * hexadecimal. Este método no aplica sal (salt), por lo que la misma
     * contraseña siempre produce el mismo hash.
     *
     * @param password contraseña en texto plano que se desea hashear; no debe ser {@code null}
     * @return cadena hexadecimal de 64 caracteres, en minúsculas, con el hash SHA-256 de la contraseña
     * @throws NullPointerException si {@code password} es {@code null}
     * @throws RuntimeException     si el algoritmo SHA-256 no está disponible en la máquina virtual de Java
     */
    public static String hashSHA256(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(password.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar la contraseña", e);
        }
    }

}