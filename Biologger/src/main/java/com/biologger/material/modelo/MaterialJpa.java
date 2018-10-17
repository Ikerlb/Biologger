package com.biologger.material.modelo;

import com.biologger.modelo.jpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ikerlb
 */
public class MaterialJpa implements Serializable{
    private EntityManagerFactory emf = null;

    public MaterialJpa(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void crear(Material material){
        
    }
    
    public void editar(Material material) throws NonexistentEntityException, Exception{
        
    }
    
    public void borrar(Integer id) throws NonexistentEntityException, Exception{
        
    }
    
}
