/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biologger.usuario.modelo;

import com.biologger.modelo.Usuario;
import com.biologger.modelo.jpa.UsuarioJpaController;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author alex aldaco
 */
public class UsuarioJpa extends UsuarioJpaController {
    
    public UsuarioJpa(EntityManagerFactory emf) {
        super(emf);
    }
    
    public Usuario buscarUsuarioNombreUsuario(String nombreUsuario) {
        EntityManager em = getEntityManager();
        try {
            List<Usuario> usuarios = null;
            usuarios = em.createNamedQuery("Usuario.findByNombreUsuario")
                         .setParameter("nombreUsuario", nombreUsuario)
                         .getResultList();
            if (!usuarios.isEmpty()) {
                return usuarios.get(0);
            }
            return null;
        } finally {
            em.close();
        }
    }
    
    public Usuario buscarUsuarioCorreo(String correo) {
        EntityManager em = getEntityManager();
        try {
            List<Usuario> usuarios = null;
            usuarios = em.createNamedQuery("Usuario.findByCorreo")
                         .setParameter("correo", correo)
                         .getResultList();
            if (!usuarios.isEmpty()) {
                return usuarios.get(0);
            }
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<Usuario> findUsuarioEntitiesFilter(int rol, Boolean activo, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        String query ="SELECT u FROM Usuario u WHERE";
        if (rol > 0 && activo != null) {
            query += " u.activo = :activo AND u.rol = :rol";
        } else {
            query += rol > 0 ? " u.rol = :rol" : " u.activo = :activo";
        }
        query += " ORDER BY u.id DESC";
        try {
            Query q = em.createQuery(query)
                .setMaxResults(maxResults)
                .setFirstResult(firstResult);
            if (activo !=  null) {
                q.setParameter("activo", activo);
            }    
            if (rol > 0) {
                q.setParameter("rol", rol);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public int countUsuarioEntitiesFilter(int rol, Boolean activo) {
        EntityManager em = getEntityManager();
        String query ="SELECT COUNT(u.id) FROM Usuario u WHERE";
        if (rol > 0 && activo != null) {
            query += " u.activo = :activo AND u.rol = :rol";
        } else {
            query += rol > 0 ? " u.rol = :rol" : " u.activo = :activo";
        }
        try {
            Query q = em.createQuery(query);
            if (activo !=  null) {
                q.setParameter("activo", activo);
            }    
            if (rol > 0) {
                q.setParameter("rol", rol);
            }
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
