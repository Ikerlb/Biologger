/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biologger.material.modelo;


import com.biologger.modelo.Material;
import com.biologger.modelo.Rmc;
import com.biologger.modelo.jpa.MaterialJpaController;
import com.biologger.modelo.jpa.exceptions.NonexistentEntityException;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author ikerlb
 */
public class MaterialJpa extends MaterialJpaController{
    
    public MaterialJpa(EntityManagerFactory emf) {
        super(emf);
    }
    
    public void editarMaterial(Material material) throws NonexistentEntityException{
        EntityManager em = null;
        try{
            em = getEntityManager();
            em.getTransaction().begin();
            material = em.merge(material);
            em.getTransaction().commit();

        }catch (Exception ex){
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = material.getId();
                if (findMaterial(id) == null) {
                    throw new NonexistentEntityException("The material with id " + id + " no longer exists.");
                }
            }
            throw ex;
        }finally{            
            if (em != null) {
                em.close();
            }
        }
    }
    
   public int countMaterialEntitiesFilter(Map<String,String> params,List<Rmc> rmcList) {
        String strCat="(";
        if(rmcList!=null){
            for(Rmc rmc:rmcList){
                strCat+=rmc.getMaterial().getId()+",";
            }
        }
        strCat=strCat.substring(0,strCat.length()-1)+")";
        EntityManager em = getEntityManager();
        String query ="SELECT count(m) FROM Material m";
        int i = 1;
        System.out.println(params);
        if (!params.isEmpty()) {
            query += " WHERE ";
            for (String key : params.keySet()) {
                if (i > 1) {
                    query += " AND ";
                }
                switch (key) {
                    case "nombre" :
                        query += "LOWER(m." + key + ") LIKE LOWER(concat('%', :nombre,'%'))";
                        break;
                    case "descripcion":
                        query += "LOWER(m." + key + ") LIKE LOWER(concat('%', :descripcion,'%'))";
                        break;
                    default :
                        query += "m." + key + " = :" + key;
                }
                i++;
            }
        }
        if(rmcList!=null){
            if(i>1){
                query+=" AND ";
            }else{
                query+=" WHERE ";
            }
            query+=" m.id in "+strCat;
        }
        try {
            Query q = em.createQuery(query);
            if (!params.isEmpty()) {
                for (String key : params.keySet()) {
                    q.setParameter(key,params.get(key));
                }
            } 
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

   
   
   public List<Material> findMaterialEntitiesFilter(Map<String,String> params,int maxResults, int firstResult, String orden, String modo, List<Rmc> rmcList) {
        String strCat="(";
        if(rmcList!=null){
            for(Rmc rmc:rmcList){
                strCat+=rmc.getMaterial().getId()+",";
            }
        }
        strCat=strCat.substring(0,strCat.length()-1)+")";
        EntityManager em = getEntityManager();
        String query ="SELECT m FROM Material m";
        int i = 1;
        System.out.println(params);
        if (!params.isEmpty()) {
            query += " WHERE ";
            for (String key : params.keySet()) {
                if (i > 1) {
                    query += " AND ";
                }
                switch (key) {
                    case "nombre" :
                        query += "LOWER(m." + key + ") LIKE LOWER(concat('%', :nombre,'%'))";
                        break;
                    case "descripcion":
                        query += "LOWER(m." + key + ") LIKE LOWER(concat('%', :descripcion,'%'))";
                        break;
                    default :
                        query += "m." + key + " = :" + key;
                }
                i++;
            }
        }
        if(rmcList!=null){
            if(i>1){
                query+=" AND ";
            }else{
                query+=" WHERE ";
            }
            query+=" m.id in "+strCat;
        }
        query += " ORDER BY m." + orden + " " + modo;
        try {
            Query q = em.createQuery(query)
                      .setMaxResults(maxResults)
                      .setFirstResult(firstResult);
            if (!params.isEmpty()) {
                for (String key : params.keySet()) {
                    q.setParameter(key,params.get(key));
                }
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }
   
}
