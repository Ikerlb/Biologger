/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biologger.usuario.controlador;

import com.biologger.modelo.Profesor;
import com.biologger.modelo.Usuario;
import com.biologger.modelo.UtilidadDePersistencia;
import com.biologger.modelo.jpa.exceptions.NonexistentEntityException;
import com.biologger.usuario.modelo.ProfesorJpa;
import com.biologger.usuario.modelo.UsuarioJpa;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author alex aldaco
 */
@ManagedBean(name="validacionProfesores")
@RequestScoped
public class ValidacionProfesoresControlador {
    private ProfesorJpa pjpa;
    
    public ValidacionProfesoresControlador() {
        this.pjpa = new ProfesorJpa(UtilidadDePersistencia.getEntityManagerFactory());
    }
    
    public List<Profesor> getValidacionesPendientes() {
        return pjpa.obtenerPeticionesPendientes();
    }
    
    public List<Profesor> getValidacionesProcesadas() {
        Map<String,String> parametros = 
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String lista;
        if (parametros.get("listar") != null) {
            lista = parametros.get("listar");
        } else {
            lista = "todas";
        }
        switch (lista) {
            case "aceptadas" :
                break;
            case "rechazadas" :
                break;
            default : lista = "todas";
        }
        return pjpa.obtenerPeticionesProcesadas(lista);
    }

    public void aceptar(Profesor profesor) throws IOException {
        FacesContext current = FacesContext.getCurrentInstance();
        ExternalContext external = current.getExternalContext();
        String path = ((HttpServletRequest)external.getRequest()).getRequestURL().toString();
        Usuario usuario = profesor.getUsuario();
        UsuarioJpa ujpa = new UsuarioJpa(UtilidadDePersistencia.getEntityManagerFactory());
        usuario.setRol(2);
        profesor.setValidado(true);
        try {
            pjpa.edit(profesor);
            ujpa.edit(usuario);
        } catch (NonexistentEntityException ex) {
            current.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,"No existe la entidad",
                    ex.getMessage()));
        } catch (Exception ex) {
            current.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",
                    ex.getMessage()));
        }
        Flash flash = external.getFlash();
        flash.setKeepMessages(true);
        current.addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO,"Nuevo profesor",
            "El usuario " + usuario.getNombre() + " ahora tiene permisos de profesor"));
        external.redirect(path);
    }
    
    public void rechazar(Profesor profesor) throws IOException {
        FacesContext current = FacesContext.getCurrentInstance();
        ExternalContext external = current.getExternalContext();
        String path = ((HttpServletRequest)external.getRequest()).getRequestURL().toString();
        Usuario usuario = profesor.getUsuario();
        profesor.setValidado(true);
        try {
            pjpa.edit(profesor);
        } catch (NonexistentEntityException ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,"No existe la entidad",
                    ex.getMessage()));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",
                    ex.getMessage()));
        }
        Flash flash = external.getFlash();
        flash.setKeepMessages(true);
        current.addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_WARN,"Petición rechazada",
            "Al usuario " + usuario.getNombre() + " Se le han negado los permisos de profesor. "
                    + "Puede asignarle los permisos posteriormente desde la sección de peticiones "
                    + "procesadas/rechazadas"));
        external.redirect(path);
    }
    
    public void revocarPermisos(Usuario usuario) throws IOException {
        FacesContext current = FacesContext.getCurrentInstance();
        ExternalContext external = current.getExternalContext();
        HttpServletRequest request = (HttpServletRequest)external.getRequest();
        String path = request.getRequestURL().toString();
        UsuarioJpa ujpa = new UsuarioJpa(UtilidadDePersistencia.getEntityManagerFactory());
        if (usuario.getRol() != 3) {
            usuario.setRol(3);
            try {
                ujpa.edit(usuario);
            } catch (NonexistentEntityException ex) {
                current.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,"No existe la entidad",
                    ex.getMessage()));
            } catch (Exception ex) {
                current.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",
                    ex.getMessage()));
            }
        }
        Flash flash = external.getFlash();
        flash.setKeepMessages(true);
        current.addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_WARN,"Permisos revocados",
            "Al usuario " + usuario.getNombre() + " Se le han negado los permisos de profesor. "
                    + "Puede asignarle los permisos posteriormente desde la sección de peticiones "
                    + "procesadas/rechazadas.")); 
        external.redirect(path);
    }
    
    public void asignarPermisos(Usuario usuario) throws IOException {
        FacesContext current = FacesContext.getCurrentInstance();
        ExternalContext external = current.getExternalContext();
        HttpServletRequest request = (HttpServletRequest)external.getRequest();
        String path = request.getRequestURL().toString();
        UsuarioJpa ujpa = new UsuarioJpa(UtilidadDePersistencia.getEntityManagerFactory());
        if (usuario.getRol() != 2) {
            usuario.setRol(2);
            try {
                ujpa.edit(usuario);
            } catch (NonexistentEntityException ex) {
                current.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,"No existe la entidad",
                    ex.getMessage()));
            } catch (Exception ex) {
                current.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",
                    ex.getMessage()));
            }
        }
        Flash flash = external.getFlash();
        flash.setKeepMessages(true);
        current.addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO,"Permisos asignados",
            "Al usuario " + usuario.getNombre() + " Se le han otorgado los permisos de profesor. "
                    + "Puede revocarle los permisos posteriormente desde la sección de peticiones "
                    + "procesadas/aceptadas.")); 
        external.redirect(path);
    }
    
    public void eliminar(Profesor profesor) throws IOException {
        FacesContext current = FacesContext.getCurrentInstance();
        ExternalContext external = current.getExternalContext();
        HttpServletRequest request = (HttpServletRequest)external.getRequest();
        String path = request.getRequestURL().toString();
        Usuario usuario = profesor.getUsuario();
        try {
            pjpa.destroy(profesor.getId());
        } catch (NonexistentEntityException ex) {
            current.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",ex.getMessage()));
        }
        Flash flash = external.getFlash();
        flash.setKeepMessages(true);
        current.addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_WARN,"Registro eliminado",
            "Se ha borrado con éxito el registro del usuario " + usuario.getNombre() + 
                    ". Puedes asignarle o revocarle permisos editando al usuario "
                            + "manualmente en la lista de usuarios.")); 
        external.redirect(path);
    }
}
