/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biologger.usuario.controlador;

import com.biologger.modelo.UtilidadDePersistencia;
import com.biologger.servicio.BCrypt;
import com.biologger.usuario.modelo.CodigoConfirmacionJpa;
import com.biologger.usuario.modelo.CodigoConfirmacion;
import com.biologger.usuario.modelo.Usuario;
import java.util.Date;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author alexa
 */
public class ConfirmacionCodigo {
    
    public static CodigoConfirmacion generar(String codigoAleatorio, Date fecha) {
        EntityManagerFactory emf = UtilidadDePersistencia.getEntityManagerFactory();
        CodigoConfirmacionJpa confirmacionJPA = new CodigoConfirmacionJpa(emf);
        CodigoConfirmacion confirmacion = new CodigoConfirmacion();
        confirmacion.setCodigo(BCrypt.hashpw(codigoAleatorio, BCrypt.gensalt()));
        confirmacion.setFechaCreacion(fecha);
        confirmacionJPA.create(confirmacion);        
        return confirmacion;
    }
    
    public static void generar(String codigoAleatorio, Date fecha, Usuario idusuario) {
        EntityManagerFactory emf = UtilidadDePersistencia.getEntityManagerFactory();
        CodigoConfirmacionJpa confirmacionJPA = new CodigoConfirmacionJpa(emf);
        CodigoConfirmacion confirmacion = new CodigoConfirmacion();
        confirmacion.setCodigo(BCrypt.hashpw(codigoAleatorio, BCrypt.gensalt()));
        confirmacion.setFechaCreacion(fecha);
        confirmacion.setIdusuario(idusuario);
        confirmacionJPA.create(confirmacion);        
    }
    
    public static void actualizar(CodigoConfirmacion confirmacion, 
            String codigoAleatorio, Date fecha, Usuario idusuario) throws Exception {
        try {
            EntityManagerFactory emf = UtilidadDePersistencia.getEntityManagerFactory();
            CodigoConfirmacionJpa confirmacionJPA = new CodigoConfirmacionJpa(emf);
            confirmacion.setCodigo(BCrypt.hashpw(codigoAleatorio, BCrypt.gensalt()));
            confirmacion.setFechaCreacion(fecha);
            confirmacion.setIdusuario(idusuario);
            confirmacionJPA.edit(confirmacion);   
        } catch (Exception ex) {
            throw ex;
        }
    }
}
