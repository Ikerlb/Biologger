/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biologger.usuario.controlador;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.Part;

/**
 *
 * @author alexa
 */
@FacesValidator(value="UploadImageValidator")
public class UploadImageValidator implements Validator{
 
    @Override
    public void validate(FacesContext context, UIComponent component, Object value)     
            throws ValidatorException {
        
        Part file = (Part) value;
        FacesMessage message=null;
 
        try {
            if (file.getSize()>524288) {
                message=new FacesMessage("Archivo demasiado grande. El máximo permitido para la foto es un peso de 512KB.");
            }
 
            if (message!=null && !message.getDetail().isEmpty())
                {
                    message.setSeverity(FacesMessage.SEVERITY_ERROR);
                    throw new ValidatorException(message );
                }
 
        } catch (Exception ex) {
               throw new ValidatorException(new FacesMessage(ex.getMessage()));
        }
 
    }


}
