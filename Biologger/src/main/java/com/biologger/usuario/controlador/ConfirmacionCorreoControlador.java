/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biologger.usuario.controlador;

import com.biologger.modelo.Usuario;
import com.biologger.modelo.UtilidadDePersistencia;
import com.biologger.servicio.BCrypt;
import com.biologger.servicio.SMTP;
import com.biologger.usuario.modelo.UsuarioJpa;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.mail.MessagingException;

/**
 *
 * @author alex aldaco
 */
@ManagedBean(name="confirmacion")
@RequestScoped
public class ConfirmacionCorreoControlador {
    private UsuarioJpa ujpa;
    private Usuario usuario;
    private String codigo;
    private Date hoy;
    
    public ConfirmacionCorreoControlador() {
        this.ujpa = new UsuarioJpa(UtilidadDePersistencia.getEntityManagerFactory());
        this.usuario = new Usuario();
        this.hoy = new Date();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public void confirmarCorreo() throws IOException {
        FacesContext current = FacesContext.getCurrentInstance();
        ExternalContext external = current.getExternalContext();
        try {
            usuario = ujpa.buscarUsuarioCorreo(usuario.getCorreo());
            if (usuario == null) {
                current.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Revisa el correo", "El correo que ingresaste no se encuentra registrado"));
                return;
            }
            if (!BCrypt.checkpw(codigo, usuario.getHashConfirmacion())) {
                current.addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Código no válido", "El código de confirmación es incorrecto"));
                return;
            }     
            Calendar calendario = Calendar.getInstance();
            calendario.setTime(usuario.getUltimaActualizacion());
            calendario.add(Calendar.DAY_OF_YEAR, 1);
            Date fechaExpiracion = calendario.getTime();
            if (hoy.after(fechaExpiracion)) {
                current.addMessage(null,new FacesMessage(
                                FacesMessage.SEVERITY_ERROR,"Error", 
                            "El código de confirmación ha expirado, usa el boton "
                                    + "para reenviarte un nuevo código"));
                return;
            }
            usuario.setUltimoAcceso(hoy);
            ujpa.edit(usuario);
        } catch (Exception ex) {
            current.addMessage(null,new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,"Error", ex.getMessage()));
            return;
        }
        external.getSessionMap().put("usuario", usuario);
        Flash flash = external.getFlash();
        flash.setKeepMessages(true);
        current.addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_INFO,"Has confirmado tu cuenta",
                usuario.getNombre() + " has confirmado tu cuenta correctamente, ya puedes iniciar"
                        + " sesión desde la pantalla de inicio de sesión."));
        external.redirect(external.getRequestContextPath() + "/faces/index.xhtml");
    }
    
    public void reenviarCodigoConfirmacion() {
        FacesContext current = FacesContext.getCurrentInstance();
        try {
            usuario = ujpa.buscarUsuarioCorreo(usuario.getCorreo());
            if (usuario == null) {
                current.addMessage(null, new FacesMessage(
                                FacesMessage.SEVERITY_WARN,"Correo no existe","El correo que ingresaste"
                                        + " no esta asociado a ninguna cuenta de usuario"));
            } else {
                String codigoAleatorio = generarCodigoAleatorio();
                usuario.setHashConfirmacion(BCrypt.hashpw(codigoAleatorio, BCrypt.gensalt()));
                usuario.setUltimaActualizacion(hoy);
                enviarCodigoConfirmacion(codigoAleatorio);
                current.addMessage(null, new FacesMessage(
                                FacesMessage.SEVERITY_INFO,"Código reenviado","Un nuevo código de"
                                        + " confirmación ha sido enviado a tu correo "
                                        + usuario.getCorreo() + ". Esposible que tengas que revisar"
                                                + " la carpeta de spam."));
            }
        } catch (Exception ex) {
            current.addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,"¡Algo salio mal!", ex.getMessage()));
        }
    }
    
    private String generarCodigoAleatorio() {
        Random rnd = new Random();
        return Integer.toString(100000 + rnd.nextInt(900000));
    }
    
    private void enviarCodigoConfirmacion(String codigoAleatorio) 
            throws MessagingException, IOException {
        ExternalContext external = FacesContext.getCurrentInstance().getExternalContext();
        String url = external.isSecure() ? "https://" : "http://";
        url += external.getRequestServerName();
        if (external.getRequestServerPort() != 80) {
            url += ":" + external.getRequestServerPort();
        }
        url += external.getApplicationContextPath();
        url += "/faces/usuario/confirmar-correo.xhtml";
        String asunto = "Nuevo código de confirmación";
        String cuerpoMensaje = String.join(
    	    System.getProperty("line.separator"),
    	    "<h1>",
            usuario.getNombre().trim(),
            "</h1>",
            "<p>Hemos creado un nuevo código de confirmación</p>",
    	    "<p>Usa el siguiente código para validar tu correo electrónico.</p>",
            "<p>El código tiene una vigencia de 24 horas.</p>",
            url,
            "<h2>",
            codigoAleatorio,
            "</h2>"
    	);
        try {
            SMTP.enviarCorreo(usuario.getCorreo(), asunto, cuerpoMensaje);
        } catch (MessagingException | IOException ex) {
            throw ex;
        }
    }
}
