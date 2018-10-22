/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biologger.servicio;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import javax.servlet.http.Part;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author alexa
 */
public class ImagenBase64 {
    public static String codificar(Part file) throws IOException {
        InputStream input = null;
        try {
            input = file.getInputStream();
            byte[] bytes = IOUtils.toByteArray(input);
            String base64Encoded = "data:image/png;base64,";
            base64Encoded += Base64.getEncoder().encodeToString(bytes);
            return base64Encoded;
        } catch (IOException ex) {
            throw ex;
        } finally {
            try {
                input.close();
            } catch (IOException ex) {
                throw ex;
            }
        }
    }
}
