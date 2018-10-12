/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biologger.usuario.controlador;

import com.biologger.cifrado.BCrypt;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import com.biologger.usuario.modelo.Usuario;
import com.biologger.usuario.modelo.UsuarioJpa;
import com.biologger.modelo.UtilidadDePersistencia;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author alexa
 */
@ManagedBean(name="registroUsuario")
@RequestScoped
public class RegistroUsuarioControlador {
    private UsuarioJpa usuarioJPA;
    private Usuario usuario;
    private String confirmacionContraseña;
    private boolean profesor;

    public RegistroUsuarioControlador() {
        this.usuarioJPA = new UsuarioJpa(UtilidadDePersistencia.getEntityManagerFactory());
        this.usuario = new Usuario();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public String getConfirmacionContraseña() {
        return confirmacionContraseña;
    }

    public void setConfirmacionContraseña(String confirmacionContraseña) {
        this.confirmacionContraseña = confirmacionContraseña;
    }

    public boolean isProfesor() {
        return profesor;
    }

    public void setProfesor(boolean profesor) {
        this.profesor = profesor;
    }
    
    public String registrarUsuario() {
        if (!usuario.getContrasena().equals(confirmacionContraseña)) { 
            FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_WARN,"Aviso", "La contraseña no coincide"));
            return null;
        }
        Usuario entidadUsuario = null;
        entidadUsuario = usuarioJPA.buscarUsuarioNombreUsuario(usuario.getNombreusuario());
        if(entidadUsuario != null) {
            FacesContext.getCurrentInstance()
             .addMessage(null, new FacesMessage(
                     FacesMessage.SEVERITY_WARN,"Aviso", 
                     "El nombre de usuario que quieres usar ya está registrado, ingresa otro nombre de usuario"));
            return null;
        }
        entidadUsuario = usuarioJPA.buscarUsuarioCorreo(usuario.getCorreo());
        if(entidadUsuario != null) {
            FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(
                     FacesMessage.SEVERITY_WARN,"Aviso", 
                     "El correo ya está registrado, si eres el propietario puedes restablecer tu contraseña"));
            return null;
        } 
        usuario.setContrasena(BCrypt.hashpw(confirmacionContraseña, BCrypt.gensalt()));
        java.util.Date date = new java.util.Date();
        usuario.setFechaRegistro(date);
        usuario.setUltimaActualizacion(date);
        usuario.setRolid(profesor ? 2 : 3);
        usuarioJPA.crear(usuario);
        return "confirmar-correo?faces-redirect=true";
    }
}
