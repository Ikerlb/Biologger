package com.biologger.controlador;

import org.primefaces.event.FileUploadEvent;
import org.apache.commons.io.FilenameUtils;
import com.biologger.modelo.Material;
import com.biologger.modelo.Categoria;
import com.biologger.modelo.Rmc;
import com.biologger.modelo.jpa.MaterialJpaController;
import com.biologger.modelo.jpa.CategoriaJpaController;
import com.biologger.modelo.UtilidadDePersistencia;
import com.biologger.modelo.jpa.RmcJpaController;
import com.biologger.modelo.jpa.exceptions.IllegalOrphanException;
import com.biologger.modelo.jpa.exceptions.NonexistentEntityException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManagerFactory;
import org.apache.commons.io.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ikerlb
 */
@ManagedBean(name="agregaMat")
@ViewScoped
public class ControladorAgregarMaterial {
    
    private EntityManagerFactory emf;
    private MaterialJpaController materialJPA;
    private CategoriaJpaController categoriaJPA;
    private RmcJpaController rmcJPA;
    private List<Categoria> categorias;
    private Material material;
    private List<String> categoriasSeleccionadas;
    private UploadedFile file;
    private StreamedContent image;
    
    public ControladorAgregarMaterial(){
        this.emf = UtilidadDePersistencia.getEntityManagerFactory();
        this.categoriaJPA = new CategoriaJpaController(emf);
        this.materialJPA = new MaterialJpaController(emf);
        this.rmcJPA = new RmcJpaController(emf);
        this.material = new Material();
        this.categoriasSeleccionadas= new ArrayList<String>();
        this.categorias = categoriaJPA.findCategoriaEntities();
        this.image = new DefaultStreamedContent();
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
    
    public UploadedFile getFile() {
        return file;
    }
 
    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    public StreamedContent getImage() {
        return image;
    }

    public void setImage(StreamedContent image) {
        this.image = image;
    }

    public void handleUpload(FileUploadEvent event) {
        System.out.println(event.getFile());
        file=event.getFile();
        String str = file.getFileName();
        String prefix = FilenameUtils.getBaseName(str);
        String suffix = FilenameUtils.getExtension(str);
        InputStream input = null;
        try {
            input = file.getInputstream();
            byte[] bytes = IOUtils.toByteArray(input);
            String base64Encoded = "data:image/"+suffix+";base64,";
            base64Encoded += Base64.getEncoder().encodeToString(bytes);
            material.setFoto(base64Encoded);
            getImagestream();
            System.out.println(image!=null);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Imagen!"));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Hubo un error con la imagen"));
        } finally {
            try {
                input.close();
            } catch (Exception ex) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Hubo un error con la imagen"));
            }
        }        
    }
    
    public void getImagestream() {
        if( file != null ){
            image = new DefaultStreamedContent(new ByteArrayInputStream(file.getContents()), file.getContentType());
        }
    }
    
    public void crearMateriales() {
        material.setEstado("DISPONIBLE");
        try{
            materialJPA.create(material);
            try{
                for(String s : categoriasSeleccionadas){
                    Categoria cat=categoriaJPA.findCategoria(Integer.parseInt(s));
                    Rmc rmc=new Rmc();
                    rmc.setCategoria(cat);
                    rmc.setMaterial(material);
                    rmcJPA.create(rmc);
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Se ha creado el material"));
            }catch(Exception e){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error vinculando material con categorias. Intenta editarlo."));
            }
        }catch(Exception e){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", "Error creando material. Intentalo de nuevo"));
        }
        
        
    }
    
}
