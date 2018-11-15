/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.biologger.usuario.controlador;

import com.biologger.modelo.Usuario;
import com.biologger.modelo.UtilidadDePersistencia;
import com.biologger.modelo.jpa.exceptions.IllegalOrphanException;
import com.biologger.modelo.jpa.exceptions.NonexistentEntityException;
import com.biologger.servicio.ImagenBase64;
import com.biologger.usuario.modelo.ProfesorJpa;
import com.biologger.usuario.modelo.UsuarioJpa;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

/**
 *
 * @author alex aldaco
 */
@ManagedBean(name="beanUsuarios")
@ViewScoped
public class UsuariosControlador {
    private List<Usuario> usuarios;
    private Usuario usuario;
    private int maxResultados;
    private int pagina;
    private int totalPaginas;
    private int totalResultados;
    private UsuarioJpa ujpa;
    private Part file;

    public UsuariosControlador() {
        this.usuario = new Usuario();
        this.ujpa = new UsuarioJpa(UtilidadDePersistencia.getEntityManagerFactory());
        this.usuarios = new ArrayList();
        String uri = ((HttpServletRequest) FacesContext.getCurrentInstance()
                        .getExternalContext().getRequest()).getRequestURI();
        String path = ((HttpServletRequest) FacesContext.getCurrentInstance()
                        .getExternalContext().getRequest()).getContextPath();
        if (uri.equals(path + "/faces/admin/usuario/lista-de-usuarios.xhtml")) {
            generarListaUsuarios();
        } else {
            cargarUsuario();
        }
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getTotalResultados() {
        return totalResultados;
    }

    public void setTotalResultados(int totalResultados) {
        this.totalResultados = totalResultados;
    }

    public int getPagina() {
        return pagina;
    }

    public void setPagina(int pagina) {
        this.pagina = pagina;
    }

    public int getTotalPaginas() {
        return totalPaginas;
    }

    public void setTotalPaginas(int totalPaginas) {
        this.totalPaginas = totalPaginas;
    }

    public int getMaxResultados() {
        return maxResultados;
    }
    
    public void cargarUsuario(){
        String id = null;
        Map<String,String> parametros = 
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        if (parametros.get("id") != null) {
            id = parametros.get("id");
        }
        Usuario entidadUsuario = ujpa.findUsuario(Integer.parseInt(id));
        if (!Objects.equals(entidadUsuario.getId(), usuario.getId())) {
            usuario = entidadUsuario;
        }
    }
    
    public void editarUsuario() {
        FacesContext current = FacesContext.getCurrentInstance();
        try {
            ujpa.edit(usuario);
            current.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Cambios guardados","Los datos del usuario " + usuario.getNombre() + 
                        " han sido actualizados con éxito"));
            current.getExternalContext().redirect("ver.xhtml?id=" + usuario.getId());
            //external.redirect(((HttpServletRequest) external.getRequest()).getRequestURI() + "?id=" + usuario.getId());
            
        } catch (NonexistentEntityException ex) {
            current.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
                    "El usuario no existe","El usuario que deseas eliminar ya no existe en la base de datos."));
        } catch (Exception ex) {
            current.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
                    "Error", ex.getMessage()));
        }
    }
    
    public void subirImagen() throws IOException {
        FacesContext current = FacesContext.getCurrentInstance();
        try {
            String fotoCodificada = ImagenBase64.codificar(file);
            usuario.setFoto(fotoCodificada);
            setUsuario(usuario);
            current.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
                "Imagen cargada","La foto del usuario " + usuario.getNombre() + 
                        " ha sido cargada con éxito, para preservar los cambios, de click en el boton guardar."));
        } catch (Exception ex) {
            current.addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "¡Algo salio mal!",ex.getMessage()));
        }
    }
    
    public void generarListaUsuarios() {
        Map<String,String> parametros = 
                FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        pagina = getNumeroPagina(parametros.get("pagina")); 
        maxResultados = getIntMaxResultados(parametros.get("maxresultados"));
        int rol = getRolInt(parametros.get("rol"));
        Boolean activo = getEstadoBool(parametros.get("estado"));
        if (rol == -1 && activo == null) {
            totalResultados = ujpa.getUsuarioCount();
            totalPaginas = (int) Math.ceil((float)totalResultados/(float)maxResultados);
            usuarios = ujpa.findUsuarioEntities(maxResultados, (pagina -1)* maxResultados);
        } else {
            totalResultados = ujpa.countUsuarioEntitiesFilter(rol, activo);
            totalPaginas = (int) Math.ceil((float)totalResultados/(float)maxResultados);
            usuarios = ujpa.findUsuarioEntitiesFilter(rol, activo, maxResultados, (pagina -1)* maxResultados);
        }
    }
    
    private int getNumeroPagina(String pagina) {
        int p = 1;
        if (pagina != null && !pagina.equals("")){
            p = Integer.parseInt(pagina);
        }
        return p;
    }
    
    private int getIntMaxResultados(String maxResultados) {
        int mr = 25;
        if (maxResultados != null && !"".equals(maxResultados)){
            mr = Integer.parseInt(maxResultados);
        }
        return mr;
    }
    
    private int getRolInt(String rol) {
        int r = -1;
        if (rol != null) {
            switch (rol) {
                case "administrador" :
                    r = 1; break;
                case "profesor" :
                    r = 2; break;
                case "usuario" :
                    r = 3; break; 
                default: break;
            }
        }
        return r;
    }
    
    private Boolean getEstadoBool(String estado) {
        Boolean e = null;
        if (estado != null) {
            switch (estado) {
                case "activo" :
                    e = true; break;
                case "bloqueado" :
                    e = false; break;
                default: break;
            }
        }
        return e;
    }
    
    public void eliminarUsuario() throws IllegalOrphanException, NonexistentEntityException, IOException {
        FacesContext current = FacesContext.getCurrentInstance();
        try {
            String nombre = usuario.getNombre();
            if(usuario.getProfesor() != null) {
               ProfesorJpa pjpa = new ProfesorJpa(UtilidadDePersistencia.getEntityManagerFactory());
               pjpa.destroy(usuario.getProfesor().getId());
            }
            ujpa.destroy(usuario.getId());
            Flash flash = current.getExternalContext().getFlash();
            flash.setKeepMessages(true);
            current.addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_INFO,
                    "¡Usuario eliminado!", "El usuario " + nombre + " ha sido eliminado con éxito"));
            current.getExternalContext().redirect("lista-de-usuarios.xhtml");
        } catch (IllegalOrphanException ex) {
            current.addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "¡Algo salio mal!",ex.getMessage()));
        } catch (NonexistentEntityException ex) {
            current.addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                    "¡Algo salio mal!",ex.getMessage()));
        }
    }
}
