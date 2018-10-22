/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biologger.usuario.controlador;

import com.biologger.modelo.UtilidadDePersistencia;
import com.biologger.usuario.modelo.ProfesorValidacion;
import com.biologger.usuario.modelo.ProfesorValidacionJpa;
import com.biologger.usuario.modelo.Usuario;
import com.biologger.usuario.modelo.UsuarioJpa;
import com.biologger.usuario.modelo.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author alexa
 */
@ManagedBean(name="validacionProfesores")
@RequestScoped
public class ValidacionProfesoresControlador {
    private EntityManagerFactory emf;
    private ProfesorValidacionJpa pValidacionJPA;
    private List<ProfesorValidacion> listaValidaciones;

    public ValidacionProfesoresControlador() {
        this.emf = UtilidadDePersistencia.getEntityManagerFactory();
        this.pValidacionJPA = new ProfesorValidacionJpa(emf);
        this.listaValidaciones = new ArrayList();
    }

    public List<ProfesorValidacion> getListaValidaciones() {
        listaValidaciones.clear();
        listaValidaciones = pValidacionJPA.findProfesorValidacionEntities();
        return listaValidaciones;
    }

    public void setListaValidaciones(List<ProfesorValidacion> listaValidaciones) {
        this.listaValidaciones = listaValidaciones;
    }
    
    public String aceptar(ProfesorValidacion validacion) throws NonexistentEntityException, Exception {
        Usuario usuario = validacion.getIdusuario();
        UsuarioJpa usuarioJPA = new UsuarioJpa(emf);
        usuario.setRolid(2);
        try {
           usuarioJPA.edit(usuario);
           pValidacionJPA.destroy(validacion.getIdProfesorValidacion());
           FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO,"Actualización de permisos",
                    usuario.getNombre() + " ahora tiene nuevos permisos" ));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",
                    ex.getMessage()));
        }
        return "faces/admin/usuario/validar-profesores.xhtml";
    }
    
    public String eliminar(ProfesorValidacion validacion) throws NonexistentEntityException {
        try {
            pValidacionJPA.destroy(validacion.getIdProfesorValidacion());
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,"Solicitud rechazada",
                    "La solicitud de " +
                    validacion.getIdusuario().getNombre() + " ha sido rechazada" ));
        } catch (NonexistentEntityException ex) {
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",
                    ex.getMessage()));
        }
        return "faces/admin/usuario/validar-profesores.xhtml";
    }
}
