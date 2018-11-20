package com.biologger.controlador;

import com.biologger.modelo.Material;
import com.biologger.modelo.Categoria;
import com.biologger.modelo.Rmc;
import com.biologger.modelo.jpa.MaterialJpaController;
import com.biologger.modelo.UtilidadDePersistencia;
import com.biologger.modelo.jpa.RmcJpaController;
import com.biologger.modelo.jpa.exceptions.IllegalOrphanException;
import com.biologger.modelo.jpa.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ikerlb
 */
@ManagedBean(name="mats")
@RequestScoped
public class ControladorMateriales {
    private EntityManagerFactory emf;
    private List<Material> materiales;
    private MaterialJpaController materialJPA;
    private RmcJpaController rmcJPA;
    
    public ControladorMateriales(){
        this.emf = UtilidadDePersistencia.getEntityManagerFactory();
        this.materiales = new ArrayList();
        this.rmcJPA = new RmcJpaController(emf);
        this.materialJPA = new MaterialJpaController(emf);
    }
    
    public List<Material> getMateriales() {
        materiales = materialJPA.findMaterialEntities();
        return materiales;
    }
    
    public void setMateriales(List<Material> materiales) {
        this.materiales = materiales;
    }
    
    public void eliminarMateriales(Material material) {
        try {
            List<Rmc> rmcList=material.getRmcList();
            for(Rmc r: rmcList){
                rmcJPA.destroy(r.getId());
            }
            materialJPA.destroy(material.getId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Eliminado","Se elimino el material"));
        } catch (NonexistentEntityException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error",e.getMessage()));
        } catch (IllegalOrphanException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error","No se pudo eliminar material"));
            Logger.getLogger(ControladorMateriales.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
