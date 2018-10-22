/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biologger.usuario.modelo;


import com.biologger.usuario.modelo.exceptions.IllegalOrphanException;
import com.biologger.usuario.modelo.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author alexa
 */
public class UsuarioJpa implements Serializable {

    public UsuarioJpa(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CodigoConfirmacion codigoConfirmacion = usuario.getCodigoConfirmacion();
            if (codigoConfirmacion != null) {
                codigoConfirmacion = em.getReference(codigoConfirmacion.getClass(), codigoConfirmacion.getIdconfirmacion());
                usuario.setCodigoConfirmacion(codigoConfirmacion);
            }
            ProfesorValidacion profesorValidacion = usuario.getProfesorValidacion();
            if (profesorValidacion != null) {
                profesorValidacion = em.getReference(profesorValidacion.getClass(), profesorValidacion.getIdProfesorValidacion());
                usuario.setProfesorValidacion(profesorValidacion);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdusuario());
            CodigoConfirmacion codigoConfirmacionOld = persistentUsuario.getCodigoConfirmacion();
            CodigoConfirmacion codigoConfirmacionNew = usuario.getCodigoConfirmacion();
            ProfesorValidacion profesorValidacionOld = persistentUsuario.getProfesorValidacion();
            ProfesorValidacion profesorValidacionNew = usuario.getProfesorValidacion();
            List<String> illegalOrphanMessages = null;
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (codigoConfirmacionNew != null) {
                codigoConfirmacionNew = em.getReference(codigoConfirmacionNew.getClass(), codigoConfirmacionNew.getIdconfirmacion());
                usuario.setCodigoConfirmacion(codigoConfirmacionNew);
            }
            if (profesorValidacionNew != null) {
                profesorValidacionNew = em.getReference(profesorValidacionNew.getClass(), profesorValidacionNew.getIdProfesorValidacion());
                usuario.setProfesorValidacion(profesorValidacionNew);
            }
            if (codigoConfirmacionNew != null && !codigoConfirmacionNew.equals(codigoConfirmacionOld)) {
                Usuario oldIdusuarioOfCodigoConfirmacion = codigoConfirmacionNew.getIdusuario();
                if (oldIdusuarioOfCodigoConfirmacion != null) {
                    oldIdusuarioOfCodigoConfirmacion.setCodigoConfirmacion(null);
                    oldIdusuarioOfCodigoConfirmacion = em.merge(oldIdusuarioOfCodigoConfirmacion);
                }
                codigoConfirmacionNew.setIdusuario(usuario);
                codigoConfirmacionNew = em.merge(codigoConfirmacionNew);
            }
            if (profesorValidacionOld != null && !profesorValidacionOld.equals(profesorValidacionNew)) {
                profesorValidacionOld.setIdusuario(null);
                profesorValidacionOld = em.merge(profesorValidacionOld);
            }
            if (profesorValidacionNew != null && !profesorValidacionNew.equals(profesorValidacionOld)) {
                Usuario oldIdusuarioOfProfesorValidacion = profesorValidacionNew.getIdusuario();
                if (oldIdusuarioOfProfesorValidacion != null) {
                    oldIdusuarioOfProfesorValidacion.setProfesorValidacion(null);
                    oldIdusuarioOfProfesorValidacion = em.merge(oldIdusuarioOfProfesorValidacion);
                }
                profesorValidacionNew.setIdusuario(usuario);
                profesorValidacionNew = em.merge(profesorValidacionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuario.getIdusuario();
                if (findUsuario(id) == null) {
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            CodigoConfirmacion codigoConfirmacion = usuario.getCodigoConfirmacion();
            if (codigoConfirmacion != null) {
                codigoConfirmacion.setIdusuario(null);
                codigoConfirmacion = em.merge(codigoConfirmacion);
            }
            ProfesorValidacion profesorValidacion = usuario.getProfesorValidacion();
            if (profesorValidacion != null) {
                profesorValidacion.setIdusuario(null);
                profesorValidacion = em.merge(profesorValidacion);
            }
            em.remove(usuario);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
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

    public int getUsuarioCount() {
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
