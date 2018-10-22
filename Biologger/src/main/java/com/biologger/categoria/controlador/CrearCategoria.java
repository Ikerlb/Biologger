/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biologger.categoria.controlador;

import com.biologger.categoria.modelo.Categoria;
import com.biologger.categoria.modelo.CategoriaJpaController;
import com.biologger.modelo.UtilidadDePersistencia;
import com.biologger.modelo.exceptions.IllegalOrphanException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.mail.MessagingException;
/**
 *
 * @author Arlen
 */
@ManagedBean(name="agregarCategoria")
@RequestScoped
public class CrearCategoria {
    private Categoria categoria;
    private CategoriaJpaController categoriaJpa;
    private List<Categoria> listaCategorias;
    
    public CrearCategoria(){
        this.categoria = new Categoria();
        this.categoriaJpa = new CategoriaJpaController(UtilidadDePersistencia.getEntityManagerFactory());
        this.listaCategorias = new ArrayList();
    }
    
    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<Categoria> getListaCategorias() {
        listaCategorias.clear();
        listaCategorias = categoriaJpa.findCategoriaEntities();
        return listaCategorias;
    }

    public void setListaCategorias(List<Categoria> listaCategorias) {
        this.listaCategorias = listaCategorias;
    }
        
    public String guardarCategoria()throws IllegalOrphanException, MessagingException, IOException,
            FileNotFoundException, IOException, Exception{
        Categoria entidadCategoria;
        entidadCategoria = categoriaJpa.buscarNombreCategoria(categoria.getNombre().toUpperCase());
        if(entidadCategoria != null) {
            FacesContext.getCurrentInstance()
             .addMessage(null, new FacesMessage(
                     FacesMessage.SEVERITY_WARN,"Aviso", 
                     "La categoria que quieres ingresar ya existe. "));
            entidadCategoria = null;
            return null;
        }
        categoria.setIdcategoria(0);
        categoria.setSubcategoria(true);
        categoria.setIdcatprincipal(0);
        categoria.setNombre(categoria.getNombre().toUpperCase()); 
        categoriaJpa.create(categoria);
        return null;
    }
    
    
    
}
