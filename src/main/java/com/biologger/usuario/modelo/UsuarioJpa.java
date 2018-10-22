/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.biologger.usuario.modelo;

import com.biologger.modelo.Kit;
import com.biologger.modelo.Material;
import com.biologger.usuario.modelo.exceptions.IllegalOrphanException;
import com.biologger.usuario.modelo.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
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
        if (usuario.getMaterialList() == null) {
            usuario.setMaterialList(new ArrayList<Material>());
        }
        if (usuario.getKitList() == null) {
            usuario.setKitList(new ArrayList<Kit>());
        }
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
            List<Material> attachedMaterialList = new ArrayList<Material>();
            for (Material materialListMaterialToAttach : usuario.getMaterialList()) {
                materialListMaterialToAttach = em.getReference(materialListMaterialToAttach.getClass(), materialListMaterialToAttach.getIdmaterial());
                attachedMaterialList.add(materialListMaterialToAttach);
            }
            usuario.setMaterialList(attachedMaterialList);
            List<Kit> attachedKitList = new ArrayList<Kit>();
            for (Kit kitListKitToAttach : usuario.getKitList()) {
                kitListKitToAttach = em.getReference(kitListKitToAttach.getClass(), kitListKitToAttach.getIdkit());
                attachedKitList.add(kitListKitToAttach);
            }
            usuario.setKitList(attachedKitList);
            em.persist(usuario);
            if (codigoConfirmacion != null) {
                Usuario oldIdusuarioOfCodigoConfirmacion = codigoConfirmacion.getIdusuario();
                if (oldIdusuarioOfCodigoConfirmacion != null) {
                    oldIdusuarioOfCodigoConfirmacion.setCodigoConfirmacion(null);
                    oldIdusuarioOfCodigoConfirmacion = em.merge(oldIdusuarioOfCodigoConfirmacion);
                }
                codigoConfirmacion.setIdusuario(usuario);
                codigoConfirmacion = em.merge(codigoConfirmacion);
            }
            if (profesorValidacion != null) {
                Usuario oldIdusuarioOfProfesorValidacion = profesorValidacion.getIdusuario();
                if (oldIdusuarioOfProfesorValidacion != null) {
                    oldIdusuarioOfProfesorValidacion.setProfesorValidacion(null);
                    oldIdusuarioOfProfesorValidacion = em.merge(oldIdusuarioOfProfesorValidacion);
                }
                profesorValidacion.setIdusuario(usuario);
                profesorValidacion = em.merge(profesorValidacion);
            }
            for (Material materialListMaterial : usuario.getMaterialList()) {
                Usuario oldIdusuarioOfMaterialListMaterial = materialListMaterial.getIdusuario();
                materialListMaterial.setIdusuario(usuario);
                materialListMaterial = em.merge(materialListMaterial);
                if (oldIdusuarioOfMaterialListMaterial != null) {
                    oldIdusuarioOfMaterialListMaterial.getMaterialList().remove(materialListMaterial);
                    oldIdusuarioOfMaterialListMaterial = em.merge(oldIdusuarioOfMaterialListMaterial);
                }
            }
            for (Kit kitListKit : usuario.getKitList()) {
                Usuario oldIdusuarioOfKitListKit = kitListKit.getIdusuario();
                kitListKit.setIdusuario(usuario);
                kitListKit = em.merge(kitListKit);
                if (oldIdusuarioOfKitListKit != null) {
                    oldIdusuarioOfKitListKit.getKitList().remove(kitListKit);
                    oldIdusuarioOfKitListKit = em.merge(oldIdusuarioOfKitListKit);
                }
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
            List<Material> materialListOld = persistentUsuario.getMaterialList();
            List<Material> materialListNew = usuario.getMaterialList();
            List<Kit> kitListOld = persistentUsuario.getKitList();
            List<Kit> kitListNew = usuario.getKitList();
            List<String> illegalOrphanMessages = null;
            for (Material materialListOldMaterial : materialListOld) {
                if (!materialListNew.contains(materialListOldMaterial)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Material " + materialListOldMaterial + " since its idusuario field is not nullable.");
                }
            }
            for (Kit kitListOldKit : kitListOld) {
                if (!kitListNew.contains(kitListOldKit)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Kit " + kitListOldKit + " since its idusuario field is not nullable.");
                }
            }
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
            List<Material> attachedMaterialListNew = new ArrayList<Material>();
            for (Material materialListNewMaterialToAttach : materialListNew) {
                materialListNewMaterialToAttach = em.getReference(materialListNewMaterialToAttach.getClass(), materialListNewMaterialToAttach.getIdmaterial());
                attachedMaterialListNew.add(materialListNewMaterialToAttach);
            }
            materialListNew = attachedMaterialListNew;
            usuario.setMaterialList(materialListNew);
            List<Kit> attachedKitListNew = new ArrayList<Kit>();
            for (Kit kitListNewKitToAttach : kitListNew) {
                kitListNewKitToAttach = em.getReference(kitListNewKitToAttach.getClass(), kitListNewKitToAttach.getIdkit());
                attachedKitListNew.add(kitListNewKitToAttach);
            }
            kitListNew = attachedKitListNew;
            usuario.setKitList(kitListNew);
            usuario = em.merge(usuario);
            if (codigoConfirmacionOld != null && !codigoConfirmacionOld.equals(codigoConfirmacionNew)) {
                codigoConfirmacionOld.setIdusuario(null);
                codigoConfirmacionOld = em.merge(codigoConfirmacionOld);
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
            for (Material materialListNewMaterial : materialListNew) {
                if (!materialListOld.contains(materialListNewMaterial)) {
                    Usuario oldIdusuarioOfMaterialListNewMaterial = materialListNewMaterial.getIdusuario();
                    materialListNewMaterial.setIdusuario(usuario);
                    materialListNewMaterial = em.merge(materialListNewMaterial);
                    if (oldIdusuarioOfMaterialListNewMaterial != null && !oldIdusuarioOfMaterialListNewMaterial.equals(usuario)) {
                        oldIdusuarioOfMaterialListNewMaterial.getMaterialList().remove(materialListNewMaterial);
                        oldIdusuarioOfMaterialListNewMaterial = em.merge(oldIdusuarioOfMaterialListNewMaterial);
                    }
                }
            }
            for (Kit kitListNewKit : kitListNew) {
                if (!kitListOld.contains(kitListNewKit)) {
                    Usuario oldIdusuarioOfKitListNewKit = kitListNewKit.getIdusuario();
                    kitListNewKit.setIdusuario(usuario);
                    kitListNewKit = em.merge(kitListNewKit);
                    if (oldIdusuarioOfKitListNewKit != null && !oldIdusuarioOfKitListNewKit.equals(usuario)) {
                        oldIdusuarioOfKitListNewKit.getKitList().remove(kitListNewKit);
                        oldIdusuarioOfKitListNewKit = em.merge(oldIdusuarioOfKitListNewKit);
                    }
                }
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
            List<Material> materialListOrphanCheck = usuario.getMaterialList();
            for (Material materialListOrphanCheckMaterial : materialListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Material " + materialListOrphanCheckMaterial + " in its materialList field has a non-nullable idusuario field.");
            }
            List<Kit> kitListOrphanCheck = usuario.getKitList();
            for (Kit kitListOrphanCheckKit : kitListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Kit " + kitListOrphanCheckKit + " in its kitList field has a non-nullable idusuario field.");
            }
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
