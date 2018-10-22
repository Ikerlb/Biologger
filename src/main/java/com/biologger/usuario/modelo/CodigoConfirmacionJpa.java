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
public class CodigoConfirmacionJpa implements Serializable {

    public CodigoConfirmacionJpa(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CodigoConfirmacion codigoConfirmacion) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario idusuario = codigoConfirmacion.getIdusuario();
            if (idusuario != null) {
                idusuario = em.getReference(idusuario.getClass(), idusuario.getIdusuario());
                codigoConfirmacion.setIdusuario(idusuario);
            }
            em.persist(codigoConfirmacion);
            if (idusuario != null) {
                CodigoConfirmacion oldCodigoConfirmacionOfIdusuario = idusuario.getCodigoConfirmacion();
                if (oldCodigoConfirmacionOfIdusuario != null) {
                    oldCodigoConfirmacionOfIdusuario.setIdusuario(null);
                    oldCodigoConfirmacionOfIdusuario = em.merge(oldCodigoConfirmacionOfIdusuario);
                }
                idusuario.setCodigoConfirmacion(codigoConfirmacion);
                idusuario = em.merge(idusuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CodigoConfirmacion codigoConfirmacion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CodigoConfirmacion persistentCodigoConfirmacion = em.find(CodigoConfirmacion.class, codigoConfirmacion.getIdconfirmacion());
            Usuario idusuarioOld = persistentCodigoConfirmacion.getIdusuario();
            Usuario idusuarioNew = codigoConfirmacion.getIdusuario();
            if (idusuarioNew != null) {
                idusuarioNew = em.getReference(idusuarioNew.getClass(), idusuarioNew.getIdusuario());
                codigoConfirmacion.setIdusuario(idusuarioNew);
            }
            codigoConfirmacion = em.merge(codigoConfirmacion);
            if (idusuarioOld != null && !idusuarioOld.equals(idusuarioNew)) {
                idusuarioOld.setCodigoConfirmacion(null);
                idusuarioOld = em.merge(idusuarioOld);
            }
            if (idusuarioNew != null && !idusuarioNew.equals(idusuarioOld)) {
                CodigoConfirmacion oldCodigoConfirmacionOfIdusuario = idusuarioNew.getCodigoConfirmacion();
                if (oldCodigoConfirmacionOfIdusuario != null) {
                    oldCodigoConfirmacionOfIdusuario.setIdusuario(null);
                    oldCodigoConfirmacionOfIdusuario = em.merge(oldCodigoConfirmacionOfIdusuario);
                }
                idusuarioNew.setCodigoConfirmacion(codigoConfirmacion);
                idusuarioNew = em.merge(idusuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = codigoConfirmacion.getIdconfirmacion();
                if (findCodigoConfirmacion(id) == null) {
                    throw new NonexistentEntityException("The codigoConfirmacion with id " + id + " no longer exists.");
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
            CodigoConfirmacion codigoConfirmacion;
            try {
                codigoConfirmacion = em.getReference(CodigoConfirmacion.class, id);
                codigoConfirmacion.getIdconfirmacion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The codigoConfirmacion with id " + id + " no longer exists.", enfe);
            }
            Usuario idusuario = codigoConfirmacion.getIdusuario();
            if (idusuario != null) {
                idusuario.setCodigoConfirmacion(null);
                idusuario = em.merge(idusuario);
            }
            em.remove(codigoConfirmacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CodigoConfirmacion> findCodigoConfirmacionEntities() {
        return findCodigoConfirmacionEntities(true, -1, -1);
    }

    public List<CodigoConfirmacion> findCodigoConfirmacionEntities(int maxResults, int firstResult) {
        return findCodigoConfirmacionEntities(false, maxResults, firstResult);
    }

    private List<CodigoConfirmacion> findCodigoConfirmacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CodigoConfirmacion.class));
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

    public CodigoConfirmacion findCodigoConfirmacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CodigoConfirmacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getCodigoConfirmacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CodigoConfirmacion> rt = cq.from(CodigoConfirmacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
