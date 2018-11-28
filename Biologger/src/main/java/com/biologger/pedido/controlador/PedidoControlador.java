/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.biologger.pedido.controlador;

import com.biologger.modelo.Material;
import com.biologger.modelo.Pedido;
import com.biologger.modelo.Usuario;
import com.biologger.modelo.UtilidadDePersistencia;
import com.biologger.modelo.jpa.MaterialJpaController;
import com.biologger.modelo.jpa.exceptions.NonexistentEntityException;
import com.biologger.pedido.modelo.PedidoJpa;
import com.biologger.usuario.modelo.UsuarioJpa;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author alex aldaco
 */
@ManagedBean(name="pedidoBean")
@ViewScoped
public class PedidoControlador {
    private Pedido pedido;
    private List<Pedido> pedidos;
    private PedidoJpa pjpa;
    private Date hoy;
    private int id;
    
    
    public PedidoControlador() {
        this.pedido = new Pedido();
        this.pjpa = new PedidoJpa(UtilidadDePersistencia.getEntityManagerFactory());
        this.hoy = new Date();
        ExternalContext external = FacesContext.getCurrentInstance().getExternalContext();
        Map<String,String> parametros = external.getRequestParameterMap();
        this.id = parametros.containsKey("pedido_id") && parametros.get("pedido_id") != null ?
                parseInt(parametros.get("pedido_id")) : 0;
        if (id > 0) {
            cargarPedido(id);
        }
        String uri = ((HttpServletRequest) external.getRequest()).getRequestURI();
        String path = ((HttpServletRequest) external.getRequest()).getContextPath();
        if (uri.equals(path + "/faces/admin/pedido/lista.xhtml")) {
            this.pedidos = new ArrayList();
            generarListaPedidos();
        } 
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void cancelar(Pedido pedido) throws NonexistentEntityException, Exception {
        FacesContext current = FacesContext.getCurrentInstance();
        MaterialJpaController mjpa = new MaterialJpaController(UtilidadDePersistencia.getEntityManagerFactory());
        if (pedido.getEstado().equals("Activo") || pedido.getEstado().equals("Vencido")) {
            current.addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,
                        "Error","No se puede cancelar este préstamo."));
            return;
        }
        try {
            List<Material> materiales = pedido.getMateriales();
            for (Material material : materiales) {
                material.setEstado("Disponible");
                mjpa.edit(material);
            }
            pjpa.destroy(pedido.getId());
        } catch (NonexistentEntityException ex) {
            current.addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",ex.getMessage()));
        } catch (Exception ex) {
            current.addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",ex.getMessage()));
        }
    }
    
    public void eliminar() throws NonexistentEntityException, Exception {
        FacesContext current = FacesContext.getCurrentInstance();
        MaterialJpaController mjpa = new MaterialJpaController(UtilidadDePersistencia.getEntityManagerFactory());
        try {
            List<Material> materiales = pedido.getMateriales();
            for (Material material : materiales) {
                material.setEstado("Disponible");
                mjpa.edit(material);
            }
            pjpa.destroy(pedido.getId());
        } catch (NonexistentEntityException ex) {
            current.addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",ex.getMessage()));
        } catch (Exception ex) {
            current.addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error",ex.getMessage()));
        }
    }
    
    public Date expiraInfo(Date fecha) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        calendario.add(Calendar.DAY_OF_YEAR, 3);
        return calendario.getTime();
    }

    private void generarListaPedidos() {
        this.pedidos = pjpa.filtrarPedidos();
    }
    
    private void cargarPedido(int id) {
        Pedido p = new Pedido();
        p = pjpa.findPedido(id);
        if (p != null) {
            Calendar calendario = Calendar.getInstance();
            calendario.setTime(hoy);
            calendario.add(Calendar.DAY_OF_YEAR, 5);
            Date fechaEntrega = calendario.getTime();
            p.setFechaDespacho(hoy);
            p.setFechaEntrega(fechaEntrega);
            setPedido(p);
        }
    }
    
    public void prepararDespacho(Pedido pedido) throws IOException {
        String uri =  "procesar.xhtml?pedido_id=" + pedido.getId();
        FacesContext.getCurrentInstance().getExternalContext().redirect(uri);
    }
    
    public void quitarDelPedido(Material material,int size) throws NonexistentEntityException, Exception {
        MaterialJpaController mjpa = new MaterialJpaController(UtilidadDePersistencia.getEntityManagerFactory());
        Pedido p = material.getPedido();
        material.setPedido(null);
        String nombre = material.getNombre();
        try {
            mjpa.edit(material);
            if (size < 2) {
                pjpa.destroy(p.getId());
            }
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PedidoControlador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PedidoControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_INFO,"Eliminado","Se elimino el material " + nombre + " del pedido."
        ));
        FacesContext.getCurrentInstance().getExternalContext().redirect("procesar.xhtml?pedido_id=" + pedido.getId());
    }
    
    public void despachar() throws Exception {
        MaterialJpaController mjpa = new MaterialJpaController(UtilidadDePersistencia.getEntityManagerFactory());
        try {
            for (Material material : pedido.getMateriales()) {
                material.setEstado("En préstamo");
                mjpa.edit(material);
            }
            pedido.setEstado("Activo");
            pjpa.edit(pedido);
            FacesContext.getCurrentInstance().getExternalContext().redirect("lista.xhtml");
        } catch (Exception ex) {
            Logger.getLogger(PedidoControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
                FacesMessage.SEVERITY_INFO,"Peido en progreso","Se ha despachado el pedido y se le ha asignado la fecha de entrega " + pedido.getFechaEntrega()
        ));
    }
    
    public void guardarYBloquear() throws Exception {
        pedido.setEstado("Vencido");
        UsuarioJpa ujpa = new UsuarioJpa(UtilidadDePersistencia.getEntityManagerFactory());
        Usuario usuario = pedido.getUsuario();
        usuario.setActivo(false);
        try {
            ujpa.edit(usuario);
            pjpa.edit(pedido);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PedidoControlador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PedidoControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void desbloquearUsuario() {
        UsuarioJpa ujpa = new UsuarioJpa(UtilidadDePersistencia.getEntityManagerFactory());
        Usuario usuario = pedido.getUsuario();
        usuario.setActivo(true);
         try {
            ujpa.edit(usuario);
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(PedidoControlador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(PedidoControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
