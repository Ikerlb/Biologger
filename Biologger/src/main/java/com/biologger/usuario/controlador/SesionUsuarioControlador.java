/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biologger.usuario.controlador;

import com.biologger.servicio.BCrypt;
import com.biologger.modelo.UtilidadDePersistencia;
import com.biologger.modelo.exceptions.NonexistentEntityException;
import com.biologger.usuario.modelo.Usuario;
import com.biologger.usuario.modelo.UsuarioJpa;
import java.util.Locale;
import java.util.Date;
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
    private String contrasena;
    private FacesContext currentContext;
    private Date hoy;

    public SesionUsuarioControlador() {
        this.currentContext = FacesContext.getCurrentInstance();
        currentContext.getViewRoot().setLocale(new Locale("es-Mx"));
        this.usuarioJPA = new UsuarioJpa(UtilidadDePersistencia.getEntityManagerFactory());
        hoy = new Date();
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String autenticar() throws NonexistentEntityException {
        Usuario entidadUsuario;
        entidadUsuario = usuarioJPA.buscarUsuarioNombreUsuario(usuario);
        if(entidadUsuario == null) {
            currentContext.addMessage(null,new FacesMessage(
                            FacesMessage.SEVERITY_WARN,"Aviso", "El usuario no está registrado"));
            return null;
        }
        String hash = entidadUsuario.getContrasena();
        if(!BCrypt.checkpw(contrasena, hash)) {
            currentContext.addMessage(null,new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,"Error de contraseña", 
                                "La contraseña que ingresaste no coincide con la almacenada en"
                                        + "la base de datos."));
            return null;
        }
        if(entidadUsuario.getUltimoAcceso() == null) {
            currentContext.addMessage(null,new FacesMessage(
                            FacesMessage.SEVERITY_WARN,"Confirma tu correo",
                                "Para iniciar sesión primero debes confirmar tu correo electrónico"));
            return "confirmar-correo?faces-redirect=true";
        }
        if(!entidadUsuario.getActivo()) {
            currentContext.addMessage(null,new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,"Cuenta inactiva","Tu cuenta está inactiva, "
                                    + "pasa con los administradores del sistema para mayor información"
                                    + " acerca del bloqueo de tu cuenta."));
            return null;
        }
        entidadUsuario.setUltimoAcceso(hoy);
        try {
            usuarioJPA.edit(entidadUsuario);
        } catch (Exception ex) {
            currentContext.addMessage(null,new FacesMessage(
                            FacesMessage.SEVERITY_FATAL,"¡Algo salió mal!",ex.getMessage()));
        }
        currentContext.getExternalContext().getSessionMap()
                .put("usuario", entidadUsuario);
        return "/index?faces-redirect=true";
    }

    public String cerrarSesion(){
        ExternalContext exContext = currentContext.getExternalContext();
        Object sesion = exContext.getSessionMap().get("usuario");
        if(sesion != null && sesion.getClass() == Usuario.class) {
            exContext.invalidateSession();
        }
        return "/faces/usuario/iniciar-sesion?faces-redirect=true";
    }

    public Usuario obtenerDatosSesion() {
        Usuario entidadUsuario;
        ExternalContext exContext = currentContext.getExternalContext();
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
