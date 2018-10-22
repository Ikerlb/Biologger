/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biologger.usuario.controlador;

import com.biologger.cifrado.BCrypt;
import com.biologger.modelo.UtilidadDePersistencia;
import com.biologger.modelo.exceptions.NonexistentEntityException;
import com.biologger.usuario.modelo.Usuario;
import com.biologger.usuario.modelo.UsuarioJpa;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author alex aldaco
 */

@ManagedBean(name="sesion")
@RequestScoped
public class SesionUsuarioControlador {
    private UsuarioJpa usuarioJPA;
    private String usuario;
    private String contrase�a;

    public SesionUsuarioControlador() {
        this.usuarioJPA = new UsuarioJpa(UtilidadDePersistencia.getEntityManagerFactory());
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrase�a() {
        return contrase�a;
    }

    public void setContrase�a(String contrase�a) {
        this.contrase�a = contrase�a;
    }

    @SuppressWarnings("empty-statement")
    public String autenticar() throws NonexistentEntityException {
        Usuario entidadUsuario;
        entidadUsuario = usuarioJPA.buscarUsuarioNombreUsuario(usuario);
        if(entidadUsuario == null) {
            FacesContext.getCurrentInstance()
                .addMessage(null,new FacesMessage(
                            FacesMessage.SEVERITY_WARN,"Aviso", "El usuario no esta� registrado"));
            return null;
        }
        String hash = entidadUsuario.getContrasena();
        if(!BCrypt.checkpw(contrase�a, hash)) {
            FacesContext.getCurrentInstance()
                .addMessage(null,new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,"Error", "La contrase�a no es la correcta"));
            return null;
        }
        if(entidadUsuario.getUltimoAcceso() == null) {
            FacesContext.getCurrentInstance()
                .addMessage(null,new FacesMessage(
                            FacesMessage.SEVERITY_WARN,"Aviso","Primero debes confirmar tu correo electronico"));
            return "confirmar-correo?faces-redirect=true";
        }
        if(!entidadUsuario.getActivo()) {
            FacesContext.getCurrentInstance()
                .addMessage(null,new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,"Error","Tu cuenta esta� inactiva, "
                                    + "pasa con los administradores del sistema para mayor informacion"));
            return null;
        }
        java.util.Date ultimoAcceso = new java.util.Date();
        entidadUsuario.setUltimoAcceso(ultimoAcceso);
        try {
            usuarioJPA.edit(entidadUsuario);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance()
                .addMessage(null,new FacesMessage(
                            FacesMessage.SEVERITY_FATAL,"Error",ex.getMessage()));
        }
        FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap()
                .put("usuario", entidadUsuario);
        return "/index?faces-redirect=true";
    }

    public String cerrarSesion(){
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        Object sesion = exContext.getSessionMap().get("usuario");
        if(sesion != null && sesion.getClass() == Usuario.class) {
            exContext.invalidateSession();
        }
        return "/faces/usuario/iniciar-sesion?faces-redirect=true";
    }

    public Usuario obtenerDatosSesion() {
        Usuario entidadUsuario;
        ExternalContext exContext = FacesContext.getCurrentInstance().getExternalContext();
        Object sesion = exContext.getSessionMap().get("usuario");
        if(sesion != null && sesion.getClass() == Usuario.class) {
            entidadUsuario = (Usuario) exContext.getSessionMap().get("usuario");
        } else {
            entidadUsuario = new Usuario();
            entidadUsuario.setNombre("Anónimo");
            entidadUsuario.setIdusuario(-1);
        }
        if (entidadUsuario.getFoto() == null) {
            entidadUsuario.setFoto("/resources/assets/images/user/icon.png");
        }
        return entidadUsuario;
    }


}
