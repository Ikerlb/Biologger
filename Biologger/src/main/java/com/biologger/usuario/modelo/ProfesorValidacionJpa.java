/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biologger.usuario.modelo;

import com.biologger.usuario.modelo.exceptions.NonexistentEntityException;
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
public class ProfesorValidacionJpa implements Serializable {

    public ProfesorValidacionJpa(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProfesorValidacion profesorValidacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario idusuario = profesorValidacion.getIdusuario();
            if (idusuario != null) {
                idusuario = em.getReference(idusuario.getClass(), idusuario.getIdusuario());
                profesorValidacion.setIdusuario(idusuario);
            }
            em.persist(profesorValidacion);
            if (idusuario != null) {
                ProfesorValidacion oldProfesorValidacionOfIdusuario = idusuario.getProfesorValidacion();
                if (oldProfesorValidacionOfIdusuario != null) {
                    oldProfesorValidacionOfIdusuario.setIdusuario(null);
                    oldProfesorValidacionOfIdusuario = em.merge(oldProfesorValidacionOfIdusuario);
                }
                idusuario.setProfesorValidacion(profesorValidacion);
                idusuario = em.merge(idusuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProfesorValidacion profesorValidacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProfesorValidacion persistentProfesorValidacion = em.find(ProfesorValidacion.class, profesorValidacion.getIdProfesorValidacion());
            Usuario idusuarioOld = persistentProfesorValidacion.getIdusuario();
            Usuario idusuarioNew = profesorValidacion.getIdusuario();
            if (idusuarioNew != null) {
                idusuarioNew = em.getReference(idusuarioNew.getClass(), idusuarioNew.getIdusuario());
                profesorValidacion.setIdusuario(idusuarioNew);
            }
            profesorValidacion = em.merge(profesorValidacion);
            if (idusuarioOld != null && !idusuarioOld.equals(idusuarioNew)) {
                idusuarioOld.setProfesorValidacion(null);
                idusuarioOld = em.merge(idusuarioOld);
            }
            if (idusuarioNew != null && !idusuarioNew.equals(idusuarioOld)) {
                ProfesorValidacion oldProfesorValidacionOfIdusuario = idusuarioNew.getProfesorValidacion();
                if (oldProfesorValidacionOfIdusuario != null) {
                    oldProfesorValidacionOfIdusuario.setIdusuario(null);
                    oldProfesorValidacionOfIdusuario = em.merge(oldProfesorValidacionOfIdusuario);
                }
                idusuarioNew.setProfesorValidacion(profesorValidacion);
                idusuarioNew = em.merge(idusuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = profesorValidacion.getIdProfesorValidacion();
                if (findProfesorValidacion(id) == null) {
                    throw new NonexistentEntityException("The profesorValidacion with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProfesorValidacion profesorValidacion;
            try {
                profesorValidacion = em.getReference(ProfesorValidacion.class, id);
                profesorValidacion.getIdProfesorValidacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The profesorValidacion with id " + id + " no longer exists.", enfe);
            }
            Usuario idusuario = profesorValidacion.getIdusuario();
            if (idusuario != null) {
                idusuario.setProfesorValidacion(null);
                idusuario = em.merge(idusuario);
            }
            em.remove(profesorValidacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ProfesorValidacion> findProfesorValidacionEntities() {
        return findProfesorValidacionEntities(true, -1, -1);
    }

    public List<ProfesorValidacion> findProfesorValidacionEntities(int maxResults, int firstResult) {
        return findProfesorValidacionEntities(false, maxResults, firstResult);
    }

    private List<ProfesorValidacion> findProfesorValidacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProfesorValidacion.class));
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

    public ProfesorValidacion findProfesorValidacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProfesorValidacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getProfesorValidacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProfesorValidacion> rt = cq.from(ProfesorValidacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
