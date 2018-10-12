/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biologger.usuario.modelo;

import com.biologger.modelo.jpa.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author alexa
 */
public class UsuarioJpa implements Serializable {
    
    private EntityManagerFactory emf = null;

    public UsuarioJpa(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void crear(Usuario usuario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void editar(Usuario usuario) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            usuario = em.merge(usuario);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getIdusuario();
                if (buscarUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void borrar(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdusuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> buscarUsuarios() {
        return buscarUsuarios(true, -1, -1);
    }

    public List<Usuario> buscarUsuarios(int maxResults, int firstResult) {
        return buscarUsuarios(false, maxResults, firstResult);
    }

    private List<Usuario> buscarUsuarios(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Usuario buscarUsuario(Integer idUsuario) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, idUsuario);
        } finally {
            em.close();
        }
    }
    
    public Usuario buscarUsuarioNombreUsuario(String nombreUsuario) {
        EntityManager em = getEntityManager();
        Usuario entidadUsuario = null;
        try {
            List<Usuario> usuarios;
            usuarios = em.createNamedQuery("Usuario.findByNombreusuario")
                        .setParameter("nombreusuario", nombreUsuario)
                        .getResultList();
            if(!usuarios.isEmpty()) {
                entidadUsuario = usuarios.get(0);
            }
            return entidadUsuario;
        } finally {
            em.close();
        }
    }
    
    public Usuario buscarUsuarioCorreo(String correo) {
        EntityManager em = getEntityManager();
        Usuario entidadUsuario = null;
        try {
            List<Usuario> usuarios;
            usuarios = em.createNamedQuery("Usuario.findByCorreo")
                        .setParameter("correo", correo)
                        .getResultList();
            if(!usuarios.isEmpty()) {
                entidadUsuario = usuarios.get(0);
            }
            return entidadUsuario;
        } finally {
            em.close();
        }
    }
    
    public int obtenerTotalUsuarios() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
