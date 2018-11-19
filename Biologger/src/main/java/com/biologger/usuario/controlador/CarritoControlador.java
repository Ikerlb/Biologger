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
    }

    public List<Material> getMateriales() {
        return materiales;
    }

    public void setMateriales(List<Material> materiales) {
        this.materiales = materiales;
    }
    
    
}
