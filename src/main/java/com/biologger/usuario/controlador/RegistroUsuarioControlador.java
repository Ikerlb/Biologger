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
import com.biologger.modelo.exceptions.IllegalOrphanException;
import com.biologger.smtp.SMTP;
import com.biologger.usuario.modelo.CodigoConfirmacion;
import com.biologger.usuario.modelo.CodigoConfirmacionJpa;
import com.biologger.usuario.modelo.ProfesorValidacion;
import com.biologger.usuario.modelo.ProfesorValidacionJpa;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Calendar;
import java.util.Random;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
import javax.servlet.http.Part;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author alex aldaco
 */
@ManagedBean(name="registroUsuario")
@RequestScoped
public class RegistroUsuarioControlador {
    private UsuarioJpa usuarioJPA;
    private Usuario usuario;
    private String confirmacionContraseña;
    private String numeroTrabajador;
    private String codigo;
    private Part file;
    private boolean profesor;

    public RegistroUsuarioControlador() {
        this.usuarioJPA = new UsuarioJpa(UtilidadDePersistencia.getEntityManagerFactory());
        this.usuario = new Usuario();
        this.file = null;
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

    public String getNumeroTrabajador() {
        return numeroTrabajador;
    }

    public void setNumeroTrabajador(String numeroTrabajador) {
        this.numeroTrabajador = numeroTrabajador;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public boolean isProfesor() {
        return profesor;
    }

    public void setProfesor(boolean profesor) {
        this.profesor = profesor;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }
    
    public String registrarUsuario() 
            throws IllegalOrphanException, MessagingException, IOException,
            FileNotFoundException, IOException, Exception{
        if (!usuario.getContrasena().equals(confirmacionContraseña)) { 
            FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(
                        FacesMessage.SEVERITY_WARN,"Aviso", "La contraseÃ±a no coincide"));
            return null;
        }
        Usuario entidadUsuario;
        entidadUsuario = usuarioJPA.buscarUsuarioNombreUsuario(usuario.getNombreusuario());
        if(entidadUsuario != null) {
            FacesContext.getCurrentInstance()
             .addMessage(null, new FacesMessage(
                     FacesMessage.SEVERITY_WARN,"Aviso", 
                     "El nombre de usuario que quieres usar ya estÃ¡ registrado, ingresa otro nombre de usuario"));
            entidadUsuario = null;
            return null;
        }
        entidadUsuario = usuarioJPA.buscarUsuarioCorreo(usuario.getCorreo());
        if(entidadUsuario != null) {
            FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(
                     FacesMessage.SEVERITY_WARN,"Aviso", 
                     "El correo ya esta registrado, si eres el propietario puedes restablecer tu contraseña"));
            entidadUsuario = null;
            return null;
        }
        if (profesor && numeroTrabajador == null) {
            FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(
                     FacesMessage.SEVERITY_WARN,"Aviso", 
                     "Debes ingresar tu numero de trabajador para validar tus datos"));
            return null;
        }
        usuario.setContrasena(BCrypt.hashpw(confirmacionContraseña, BCrypt.gensalt()));
        java.util.Date date = new java.util.Date();
        usuario.setFechaRegistro(date);
        usuario.setUltimaActualizacion(date);
        usuario.setRolid(3);
        usuario.setActivo(true);
        Random rnd = new Random();
        String codigoConf = Integer.toString(100000 + rnd.nextInt(900000));
        try {
            CodigoConfirmacion confirmacion = generarCodigoConfirmacion(codigoConf, date);
            usuario.setCodigoConfirmacion(confirmacion);
            if (profesor) {
                ProfesorValidacion validacion = generarValidacionProfesor();
                usuario.setProfesorValidacion(validacion);
            }
            if (file != null) {
                InputStream input = file.getInputStream();
                byte[] bytes = IOUtils.toByteArray(input);
                String base64Encoded = "data:image/png;base64,";
                base64Encoded += Base64.getEncoder().encodeToString(bytes);
                usuario.setFoto(base64Encoded);
            }
            usuarioJPA.create(usuario);
            enviarCodigoConfirmacion(codigoConf);
        } catch (MessagingException | IOException ex) {
            FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(
                     FacesMessage.SEVERITY_WARN,"Error", ex.getMessage()));
            return null;
        }
        return "confirmar-correo?faces-redirect=true";
    }
    
    private CodigoConfirmacion generarCodigoConfirmacion(String codigoConf, 
            java.util.Date fechaCreacion) {
        CodigoConfirmacion codigoConfirmacion = new CodigoConfirmacion();
        CodigoConfirmacionJpa confirmacionJPA = new CodigoConfirmacionJpa(UtilidadDePersistencia.getEntityManagerFactory());
        codigoConfirmacion.setCodigo(BCrypt.hashpw(codigoConf, BCrypt.gensalt()));
        codigoConfirmacion.setFechaCreacion(fechaCreacion);
        confirmacionJPA.create(codigoConfirmacion);        
        return codigoConfirmacion;
    }
    
    private ProfesorValidacion generarValidacionProfesor() {
        ProfesorValidacion validacion = new ProfesorValidacion();
        ProfesorValidacionJpa validacionJPA = new ProfesorValidacionJpa(UtilidadDePersistencia.getEntityManagerFactory());
        validacion.setNumeroTrabajador(numeroTrabajador);
        validacionJPA.create(validacion);
        return validacion;
    }
    
    private void enviarCodigoConfirmacion(String codigoConf) 
            throws MessagingException, IOException {
        String asunto = "codigo de confirmacion";
        String cuerpoMensaje = String.join(
    	    System.getProperty("line.separator"),
    	    "<h1>",
            usuario.getNombre(),
            "te damos la bienvenida a Biologger</h1>",
    	    "<p>Usa el siguiente cÃ³digo para validar tu correo electronico.</p>",
            "<p>El codigo tiene una vigencia de 24 horas.</p>",
    	    "<hr />",
            "<h2>",
            codigoConf,
            "</h2>"
    	);
        try {
            SMTP.enviarCorreo(usuario.getCorreo(), asunto, cuerpoMensaje);
        } catch (MessagingException | IOException ex) {
            throw ex;
        }
    }
    
    public String confirmarCorreo() throws Exception {
        if (!usuario.equals(usuarioJPA.buscarUsuarioCorreo(usuario.getCorreo()))){
            usuario = usuarioJPA.buscarUsuarioCorreo(usuario.getCorreo());
        } 
        if (usuario == null) {
            FacesContext.getCurrentInstance()
             .addMessage(null, new FacesMessage(
                     FacesMessage.SEVERITY_WARN,"Aviso", 
                     "El correo que quieres verificar no se encuentra registrado, "
                             + "tal vez quieras crear una nueva cuenta de usuario"));
            return null;
        }
        if (usuario.getUltimoAcceso() != null) {
            FacesContext.getCurrentInstance()
             .addMessage(null, new FacesMessage(
                     FacesMessage.SEVERITY_WARN,"Aviso", 
                     "Al parecer tu correo ya ha sido confirmado, si tienes problemas para entrar"
                             + " prueba con restaurar tu contraseÃ±a"));
            return null;
        }
        if (codigo == null) {
            FacesContext.getCurrentInstance()
             .addMessage(null, new FacesMessage(
                     FacesMessage.SEVERITY_WARN,"Aviso", 
                     "Debes ingresar el codigo de confirmacion"));
            return null;
        }
        CodigoConfirmacion codigoConf = usuario.getCodigoConfirmacion();
        String hash = codigoConf.getCodigo();
        if (!BCrypt.checkpw(codigo, hash)) {  
            FacesContext.getCurrentInstance()
                .addMessage(null,new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,"Error", 
                        "El codigo de confirmacion es incorrecto"));
            return null;
        }
        java.util.Date hoy = new java.util.Date();
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(codigoConf.getFechaCreacion());
        calendario.add(Calendar.DAY_OF_YEAR, 1);
        java.util.Date fechaExpiracion = calendario.getTime();
        if (hoy.after(fechaExpiracion)) {
            FacesContext.getCurrentInstance()
                .addMessage(null,new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,"Error", 
                        "El codigo de confirmacion ha expirado, usa el boton para "
                                + "reenviarte un nuevo codigo"));
            return null;
        }
        usuario.setUltimoAcceso(hoy);
        usuario.setUltimaActualizacion(hoy);
        try {
            usuarioJPA.edit(usuario);
        } catch (Exception ex) {
            FacesContext.getCurrentInstance()
                .addMessage(null,new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,"Error", ex.getMessage()));
            return null;
        }
        FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap()
                .put("usuario", usuario);
        return "/faces/index?faces-redirect=true";
    }
    
    public String reenviarCodigo() {
        return null;
    }
}
