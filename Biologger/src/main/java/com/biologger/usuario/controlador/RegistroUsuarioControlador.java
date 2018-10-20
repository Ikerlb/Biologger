/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biologger.usuario.controlador;

import com.biologger.servicio.ImagenBase64;
import com.biologger.servicio.BCrypt;
import javax.faces.bean.ManagedBean;
import com.biologger.usuario.modelo.Usuario;
import com.biologger.usuario.modelo.UsuarioJpa;
import com.biologger.modelo.UtilidadDePersistencia;
import com.biologger.modelo.exceptions.IllegalOrphanException;
import com.biologger.servicio.SMTP;
import com.biologger.usuario.modelo.CodigoConfirmacion;
import com.biologger.usuario.modelo.ProfesorValidacion;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.Part;

/**
 *
 * @author alex aldaco
 */
@ManagedBean(name="registroUsuario")
@RequestScoped
public class RegistroUsuarioControlador {
    private UsuarioJpa usuarioJPA;
    private EntityManagerFactory emf;
    private Usuario usuario;
    private String confirmacionContrasena;
    private String numeroTrabajador;
    private String codigo;
    private Part file;
    private Date hoy;
    private FacesContext currentContext;
    private boolean profesor;
    private List<FacesMessage> mensajes;

    public RegistroUsuarioControlador() {
        this.currentContext = FacesContext.getCurrentInstance();
        currentContext.getViewRoot().setLocale(new Locale("es-Mx"));
        emf = UtilidadDePersistencia.getEntityManagerFactory();
        this.usuarioJPA = new UsuarioJpa(emf);
        this.usuario = new Usuario();
        this.file = null;
        this.hoy = new Date();
        mensajes = new ArrayList<>();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public String getConfirmacionContrasena() {
        return confirmacionContrasena;
    }

    public void setConfirmacionContrasena(String confirmacionContrasena) {
        this.confirmacionContrasena = confirmacionContrasena;
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
    
    public void subirImagen() throws IOException {
        try {
            String fotoCodificada = ImagenBase64.codificar(file);
            usuario.setFoto(fotoCodificada);
        } catch (IOException ex) {
            currentContext.addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "¡Algo salio mal!",ex.getMessage()));
        }
    }
    
    public void eliminarImagen() {
        usuario.setFoto(null);
    }
    
    public String registrarUsuario() 
            throws IllegalOrphanException, MessagingException, IOException,
            FileNotFoundException, IOException, Exception{
        mensajes.clear();
        try {
            mensajes = filtrosRegistroUsuario();
            if (!mensajes.isEmpty()) {
                for (FacesMessage mensaje : mensajes) {
                    currentContext.addMessage(null,mensaje);
                }
                return null;
            }
            usuario.setContrasena(BCrypt.hashpw(confirmacionContrasena, BCrypt.gensalt()));
            usuario.setFechaRegistro(hoy);
            usuario.setUltimaActualizacion(hoy);
            usuario.setRolid(3);
            usuario.setActivo(true);
            String codigoAleatorio = generarCodigoAleatorio();
            CodigoConfirmacion confirmacion = ConfirmacionCodigo.generar(codigoAleatorio, hoy);
            usuario.setCodigoConfirmacion(confirmacion);
            if (profesor) {
                ProfesorValidacion validacion = ValidacionProfesor.generar(numeroTrabajador);
                usuario.setProfesorValidacion(validacion);
            }
            usuarioJPA.create(usuario);
            enviarCodigoConfirmacion(codigoAleatorio, false);
        } catch (MessagingException | IOException ex) {
            currentContext.addMessage(null, new FacesMessage(
                     FacesMessage.SEVERITY_WARN,"Error", ex.getMessage()));
            return null;
        }
        return "confirmar-correo?faces-redirect=true";
    }
    
    public String confirmarCorreo() throws Exception {
        mensajes.clear();
        try {
            usuario = usuarioJPA.buscarUsuarioCorreo(usuario.getCorreo());
            mensajes = filtrosConfirmarCorreo();
            if (!mensajes.isEmpty()) {
                for (FacesMessage mensaje : mensajes) {
                    currentContext.addMessage(null,mensaje);
                }
                return null;
            }
            CodigoConfirmacion codigoConf = usuario.getCodigoConfirmacion();
            String hash = codigoConf.getCodigo();
            if (!BCrypt.checkpw(codigo, hash)) {  
                currentContext.addMessage(null,new FacesMessage(
                                FacesMessage.SEVERITY_ERROR,"Error", 
                            "El código de confirmación es incorrecto"));
                return null;
            }
            Calendar calendario = Calendar.getInstance();
            calendario.setTime(codigoConf.getFechaCreacion());
            calendario.add(Calendar.DAY_OF_YEAR, 1);
            Date fechaExpiracion = calendario.getTime();
            if (hoy.after(fechaExpiracion)) {
                currentContext.addMessage(null,new FacesMessage(
                                FacesMessage.SEVERITY_ERROR,"Error", 
                            "El código de confirmación ha expirado, usa el boton "
                                    + "para reenviarte un nuevo código"));
                return null;
            }
            usuario.setUltimoAcceso(hoy);
            usuario.setUltimaActualizacion(hoy);
            usuarioJPA.edit(usuario);
        } catch (Exception ex) {
            currentContext.addMessage(null,new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,"Error", ex.getMessage()));
            return null;
        }
        currentContext.getExternalContext().getSessionMap().put("usuario", usuario);
        return "/faces/index?faces-redirect=true";
    }
    
    public void reenviarCodigoConfirmacion() {
        try {
            usuario = usuarioJPA.buscarUsuarioCorreo(usuario.getCorreo());
            if (usuario == null) {
                currentContext.addMessage(null, new FacesMessage(
                                FacesMessage.SEVERITY_WARN,"Correo no existe","El correo que ingresaste"
                                        + " no esta asociado a ninguna cuenta de usuario"));
            } else {
                CodigoConfirmacion confirmacion = usuario.getCodigoConfirmacion();
                String codigoAleatorio = generarCodigoAleatorio();
                if (confirmacion != null) {
                    ConfirmacionCodigo
                            .actualizar(confirmacion, codigoAleatorio, hoy, usuario);
                } else {
                    ConfirmacionCodigo.generar(codigoAleatorio, hoy, usuario);
                }
                enviarCodigoConfirmacion(codigoAleatorio, true);
                currentContext.addMessage(null, new FacesMessage(
                                FacesMessage.SEVERITY_INFO,"Código reenviado","Un nuevo código de"
                                        + " confirmación ha sido enviado a tu correo "
                                        + usuario.getCorreo() + ". Esposible que tengas que revisar"
                                                + " la carpeta de spam."));
            }
        } catch (Exception ex) {
            currentContext.addMessage(null, new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,"¡Algo salio mal!", ex.getMessage()));
        }
    }
    
    // Filtros
    
    private List<FacesMessage> filtrosRegistroUsuario() {
        if (!usuario.getContrasena().equals(confirmacionContrasena)) { 
            mensajes.add(new FacesMessage(
                        FacesMessage.SEVERITY_WARN,"Error de contraseña", "La contraseña no coincide "
                                + "con la confirmación."));
        }
        if (profesor && numeroTrabajador == "") {
            mensajes.add(new FacesMessage(
                     FacesMessage.SEVERITY_INFO,"# Trabajador", 
                     "Debes ingresar tu número de trabajador para validar tus datos"));
        }
        try {
            Usuario entidadUsuario;
            entidadUsuario = usuarioJPA.buscarUsuarioNombreUsuario(usuario.getNombreusuario());
            if(entidadUsuario != null) {
                entidadUsuario = null;
                mensajes.add(new FacesMessage(
                         FacesMessage.SEVERITY_ERROR,"Usuario no disponible", 
                         "El nombre de usuario que quieres usar ya está registrado, "
                                 + "ingresa otro nombre de usuario."));
            }
            entidadUsuario = usuarioJPA.buscarUsuarioCorreo(usuario.getCorreo());
            if(entidadUsuario != null) {
                entidadUsuario = null;
                mensajes.add(new FacesMessage(
                         FacesMessage.SEVERITY_ERROR,"Correo duplicado", 
                         "El correo ya está registrado, si eres el propietario "
                                 + "puedes restablecer tu contraseña"));
            }
        } catch (Exception ex) {
            throw ex;
        }
        return mensajes;
    }
    
    private List<FacesMessage> filtrosConfirmarCorreo() {
        List<FacesMessage> mensajes = new ArrayList<>();
        if (usuario == null) {
            mensajes.add(new FacesMessage(
                     FacesMessage.SEVERITY_WARN,"Correo inexistente", 
                     "El correo que quieres verificar no se encuentra registrado, "
                             + "tal vez quieras crear una nueva cuenta de usuario"));
        }
        if (usuario.getUltimoAcceso() != null) {
            mensajes.add(new FacesMessage(
                     FacesMessage.SEVERITY_WARN,"Correo confirmado", 
                     "Al parecer tu correo ya ha sido confirmado, si tienes problemas para entrar"
                             + " prueba con restaurar tu contraseña."));
        }
        if (codigo == null) {
            mensajes.add(new FacesMessage(
                     FacesMessage.SEVERITY_WARN,"Ingresa el codigo", 
                     "Debes ingresar el código de confirmación para hacer la validación."));
        }
        if (usuario.getCodigoConfirmacion() == null){
           mensajes.add(new FacesMessage(
                     FacesMessage.SEVERITY_WARN,"No tienes asignado un código", 
                     "Ago está mal. Prueba reenviando un nuevo codigo de confirmación "
                             + "con el botón reenviar código. ")); 
        }
        return mensajes;
    }
    
    // Métodos auxiliares
    private String generarCodigoAleatorio() {
        Random rnd = new Random();
        return Integer.toString(100000 + rnd.nextInt(900000));
    }
    
    private void enviarCodigoConfirmacion(String codigoAleatorio, boolean reenvio) 
            throws MessagingException, IOException {
        String asunto = reenvio ? "Nuevo código de confirmación" : "Código de confirmación";
        String encabezado;
        encabezado = reenvio ? ", tu nuevo código de confirmación" : ", te damos la bienvenida a Biologger";
        String cuerpoMensaje = String.join(
    	    System.getProperty("line.separator"),
    	    "<h1>",
            usuario.getNombre().trim(),
            encabezado,
            "</h1>",
    	    "<p>Usa el siguiente código para validar tu correo electrónico.</p>",
            "<p>El código tiene una vigencia de 24 horas.</p>",
    	    "<hr />",
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
