/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biologger.modelo;

import com.biologger.usuario.modelo.Usuario;
import com.biologger.modelo.exceptions.NonexistentEntityException;
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
public class KitJpaController implements Serializable {

    public KitJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Kit kit) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario idusuario = kit.getIdusuario();
            if (idusuario != null) {
                idusuario = em.getReference(idusuario.getClass(), idusuario.getIdusuario());
                kit.setIdusuario(idusuario);
            }
            em.persist(kit);
            if (idusuario != null) {
                idusuario.getKitList().add(kit);
                idusuario = em.merge(idusuario);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Kit kit) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Kit persistentKit = em.find(Kit.class, kit.getIdkit());
            Usuario idusuarioOld = persistentKit.getIdusuario();
            Usuario idusuarioNew = kit.getIdusuario();
            if (idusuarioNew != null) {
                idusuarioNew = em.getReference(idusuarioNew.getClass(), idusuarioNew.getIdusuario());
                kit.setIdusuario(idusuarioNew);
            }
            kit = em.merge(kit);
            if (idusuarioOld != null && !idusuarioOld.equals(idusuarioNew)) {
                idusuarioOld.getKitList().remove(kit);
                idusuarioOld = em.merge(idusuarioOld);
            }
            if (idusuarioNew != null && !idusuarioNew.equals(idusuarioOld)) {
                idusuarioNew.getKitList().add(kit);
                idusuarioNew = em.merge(idusuarioNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = kit.getIdkit();
                if (findKit(id) == null) {
                    throw new NonexistentEntityException("The kit with id " + id + " no longer exists.");
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
            Kit kit;
            try {
                kit = em.getReference(Kit.class, id);
                kit.getIdkit();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The kit with id " + id + " no longer exists.", enfe);
            }
            Usuario idusuario = kit.getIdusuario();
            if (idusuario != null) {
                idusuario.getKitList().remove(kit);
                idusuario = em.merge(idusuario);
            }
            em.remove(kit);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Kit> findKitEntities() {
        return findKitEntities(true, -1, -1);
    }

    public List<Kit> findKitEntities(int maxResults, int firstResult) {
        return findKitEntities(false, maxResults, firstResult);
    }

    private List<Kit> findKitEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Kit.class));
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

    public Kit findKit(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Kit.class, id);
        } finally {
            em.close();
        }
    }

    public int getKitCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Kit> rt = cq.from(Kit.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
