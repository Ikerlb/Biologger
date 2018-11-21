/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.biologger.usuario.controlador;

import com.biologger.modelo.Material;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author alex aldaco
 */
@ManagedBean(name="carrito")
@SessionScoped
public class CarritoControlador {
    private List<Material> materiales;

    public CarritoControlador() {
        this.materiales = new ArrayList();
        Material mat = new Material("Material 1");
        Material mat2 = new Material("Material 2");
        Material mat3 = new Material("Material 3");
        this.materiales.add(mat);
        this.materiales.add(mat2);
        this.materiales.add(mat3);
    }

    public List<Material> getMateriales() {
        return materiales;
    }

    public void setMateriales(List<Material> materiales) {
        this.materiales = materiales;
    }
    
    public void agregarAlCarrito(Material material) {
        materiales.add(material);
    }
    
    public void eliminarDelCarrito(Material material) {
        if (materiales.contains(material)) {
            materiales.remove(material);
        }
    }
}
