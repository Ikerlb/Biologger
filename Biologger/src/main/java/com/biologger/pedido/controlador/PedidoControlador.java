/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.biologger.pedido.controlador;

import com.biologger.modelo.Material;
import com.biologger.modelo.Pedido;
import com.biologger.modelo.UtilidadDePersistencia;
import com.biologger.modelo.jpa.MaterialJpaController;
import com.biologger.modelo.jpa.exceptions.NonexistentEntityException;
import com.biologger.pedido.modelo.PedidoJpa;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author alex aldaco
 */
@ManagedBean(name="pedidoBean")
@RequestScoped
public class PedidoControlador {
    Pedido pedido;
    PedidoJpa pjpa;
    
    public PedidoControlador() {
        this.pedido = new Pedido();
        this.pjpa = new PedidoJpa(UtilidadDePersistencia.getEntityManagerFactory());
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
    
    public Date expira(Date fecha) {
        Calendar calendario = Calendar.getInstance();
        calendario.setTime(fecha);
        calendario.add(Calendar.DAY_OF_YEAR, 3);
        return calendario.getTime();
    }
}
