
package com.biologger.materiales.controlador;

import com.biologger.materiales.modelo.Material;
import com.biologger.materiales.modelo.MaterialJpaController;
import com.biologger.modelo.UtilidadDePersistencia;
import com.biologger.materiales.modelo.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author len
 */
@ManagedBean(name="elimMat")
@RequestScoped
public class eliminarMaterialesControlador {
    private EntityManagerFactory emf;
    private List<Material> materiales;
    private MaterialJpaController materialJPA;
    private Material material;

    public eliminarMaterialesControlador() {
        this.emf = UtilidadDePersistencia.getEntityManagerFactory();
        this.materiales = new ArrayList();
        this.materialJPA = new MaterialJpaController(emf);
        this.material = new Material();
    }

    public List<Material> getMateriales() {
        materiales = materialJPA.findMaterialEntities();
        return materiales;
    }

    public void setMateriales(List<Material> materiales) {
        this.materiales = materiales;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
    
    public void eliminarMateriales(Material material) {
        try {
            materialJPA.destroy(material.getIdmaterial());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Eliminado","Se elimino el material"));
        } catch (NonexistentEntityException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error",e.getMessage()));
        }
    }
    
    public void crearMateriales() {
        materialJPA.create(material);
    }
    
}
