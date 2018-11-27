/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biologger.material.controlador;

import com.biologger.modelo.Categoria;
import com.biologger.modelo.Material;
import com.biologger.modelo.Rmc;
import com.biologger.modelo.UtilidadDePersistencia;
import com.biologger.modelo.jpa.CategoriaJpaController;
import com.biologger.modelo.jpa.MaterialJpaController;
import com.biologger.modelo.jpa.RmcJpaController;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

/**
 *
 * @author ikerlb
 */
@ManagedBean(name="editarMaterial")
@ViewScoped
public class ControladorEditarMaterial {
    private EntityManagerFactory emf;
    private MaterialJpaController materialJPA;
    private CategoriaJpaController categoriaJPA;
    private RmcJpaController rmcJPA;
    private List<Categoria> categorias;
    private Material material;
    private List<String> categoriasSeleccionadas;
    private Part file;
    private String foto;
    private String nombre;
    private String descripcion;
    
    public ControladorEditarMaterial(){
        this.emf = UtilidadDePersistencia.getEntityManagerFactory();
        this.categoriaJPA = new CategoriaJpaController(emf);
        this.materialJPA = new MaterialJpaController(emf);
        this.rmcJPA = new RmcJpaController(emf);
        this.categoriasSeleccionadas= new ArrayList<String>();
        this.categorias = categoriaJPA.findCategoriaEntities();
        
        for(Categoria cat:this.categorias){
            System.out.println(cat.getNombre());
        }
        
        ExternalContext external = FacesContext.getCurrentInstance().getExternalContext();
        Map<String,String> parametros = external.getRequestParameterMap();
        String uri = ((HttpServletRequest) external.getRequest()).getRequestURI();
        String path = ((HttpServletRequest) external.getRequest()).getContextPath();
        if (!parametros.isEmpty()) {
            if (parametros.containsKey("id") && !parametros.get("id").isEmpty()) {
                int id = parseInt(parametros.get("id"));
                Material mat = materialJPA.findMaterial(id);
                if (mat != null) {
                    this.material = mat;
                    this.nombre=mat.getNombre();
                    this.descripcion=mat.getDescripcion();
                    this.foto=mat.getFoto();
                    System.out.println("!!!!!!!!!!!!!!"+material.getRmcList().isEmpty());
                    for(Rmc rmc:material.getRmcList()){
                        Categoria cat=categoriaJPA.findCategoria(rmc.getCategoria().getId());
                        System.out.println(cat.getNombre());
                        categoriasSeleccionadas.add(cat.getNombre());
                    }
                }
                if (uri.equals(path + "/faces/admin/material/editar.xhtml")) {
                    //this.categorias = cjpa.getCategoriasEdit(categoria.getId());
                    //if (categoria.getPadre() != null) {
                        //this.padreId = categoria.getPadre().getId();
                    //}
                } 

            }
        }

    }
    
    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
    
    public List<String> getCategoriasSeleccionadas() {
        return categoriasSeleccionadas;
    }

    public void setCategoriasSeleccionadas(List<String> categoriasSeleccionadas) {
        this.categoriasSeleccionadas = categoriasSeleccionadas;
    }
    
    public Part getFile() {
        return file;
    }
 
    public void setFile(Part file) {
        this.file = file;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public String getDescripcion(){
        return descripcion;
    }
    
    public void setNombre(String nombre){
        this.nombre=nombre;
    }
    
    public void setDescripcion(String descripcion){
        this.descripcion=descripcion;
    }    
}
